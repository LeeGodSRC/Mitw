package net.development.mitw.commands;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import lombok.Getter;
import net.development.mitw.Mitw;
import net.development.mitw.commands.param.Parameter;
import net.development.mitw.commands.param.ParameterData;
import net.development.mitw.commands.param.ParameterType;
import net.development.mitw.commands.param.defaults.BooleanParameterType;
import net.development.mitw.commands.param.defaults.DoubleParameterType;
import net.development.mitw.commands.param.defaults.FloatParameterType;
import net.development.mitw.commands.param.defaults.GameModeParameterType;
import net.development.mitw.commands.param.defaults.IntegerParameterType;
import net.development.mitw.commands.param.defaults.ItemStackParameterType;
import net.development.mitw.commands.param.defaults.LongParameterType;
import net.development.mitw.commands.param.defaults.PlayerParameterType;
import net.development.mitw.commands.param.defaults.UUIDParameterType;
import net.development.mitw.commands.param.defaults.WorldParameterType;
import net.development.mitw.utils.ClassUtil;

public final class CommandHandler implements Listener {

	@Getter private static List<CommandData> commands = new ArrayList<>();
	private static Map<Class<?>, ParameterType> parameterTypes = new HashMap<>();
	private static boolean initiated = false;

	// Static class -- cannot be created.
	private CommandHandler() {
	}

	/**
	 * Initiates the command handler. This can only be called once, and is called automatically when Nucleus enables.
	 */
	public static void init() {
		// Only allow the CommandHandler to be initiated once.
		// Note the '!' in the .checkState call.
		Preconditions.checkState(!initiated);
		initiated = true;

		Mitw.getInstance().getServer().getPluginManager()
		.registerEvents(new CommandHandler(), Mitw.getInstance());

		// Run this on a delay so everything is registered.
		// Not really needed, but it's nice to play it safe.
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					// Command map field (we have to use reflection to get this)
					final Field commandMapField = Mitw.getInstance().getServer().getClass().getDeclaredField("commandMap");
					commandMapField.setAccessible(true);

					final Object oldCommandMap = commandMapField.get(Mitw.getInstance().getServer());
					final CommandMap newCommandMap = new CommandMap(Mitw.getInstance().getServer());

					// Start copying the knownCommands field over
					// (so any commands registered before we hook in are kept)
					final Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
					knownCommandsField.setAccessible(true);

					// The knownCommands field is final,
					// so to be able to set it in the new command map we have to remove it.
					final Field modifiersField = Field.class.getDeclaredField("modifiers");
					modifiersField.setAccessible(true);
					modifiersField.setInt(knownCommandsField, knownCommandsField.getModifiers() & ~Modifier.FINAL);

					knownCommandsField.set(newCommandMap, knownCommandsField.get(oldCommandMap));
					// End copying the knownCommands field over

					commandMapField.set(Mitw.getInstance().getServer(), newCommandMap);
				} catch (final Exception e) {
					// Shouldn't happen, so we can just
					// printout the exception (and do nothing else)
					e.printStackTrace();
				}
			}
		}.runTaskLater(Mitw.getInstance(), 5L);

		// Register our default parameter types.
		// boolean.class is the same as Boolean.TYPE,
		// however using .class improves readability.
		registerParameterType(UUID.class, new UUIDParameterType());
		registerParameterType(boolean.class, new BooleanParameterType());
		registerParameterType(float.class, new FloatParameterType());
		registerParameterType(double.class, new DoubleParameterType());
		registerParameterType(long.class, new LongParameterType());
		registerParameterType(int.class, new IntegerParameterType());
		registerParameterType(Player.class, new PlayerParameterType());
		registerParameterType(World.class, new WorldParameterType());
		registerParameterType(ItemStack.class, new ItemStackParameterType());
		registerParameterType(GameMode.class, new GameModeParameterType());
	}

	/**
	 * Loads all commands from the given package into the command handler.
	 *
	 * @param plugin      The plugin responsible for these commands. This is here because the .getClassesInPackage
	 *                    method requires it (for no real reason)
	 * @param packageName The package to load commands from. Example: "me.joeleoli.game.commands"
	 */
	public static void loadCommandsFromPackage(final Plugin plugin, final String packageName) {
		for (final Class<?> clazz : ClassUtil.getClassesInPackage(plugin, packageName)) {
			registerClass(clazz);
		}
	}

	/**
	 * Register a custom parameter adapter.
	 *
	 * @param transforms    The class this parameter type will return (IE KOTH.class, Player.class, etc.)
	 * @param parameterType The ParameterType object which will perform the transformation.
	 */
	public static void registerParameterType(final Class<?> transforms, final ParameterType parameterType) {
		parameterTypes.put(transforms, parameterType);
	}

	/**
	 * Registers a single class with the command handler.
	 *
	 * @param registeredClass The class to scan/register.
	 */
	protected static void registerClass(final Class<?> registeredClass) {
		for (final Method method : registeredClass.getMethods()) {
			if (method.getAnnotation(Command.class) != null) {
				registerMethod(method);
			}
		}
	}

	/**
	 * Registers a single method with the command handler.
	 *
	 * @param method The method to register (if applicable)
	 */
	protected static void registerMethod(final Method method) {
		final Command commandAnnotation = method.getAnnotation(Command.class);
		final List<ParameterData> parameterData = new ArrayList<>();

		// Offset of 1 here for the sender parameter.
		for (int parameterIndex = 1; parameterIndex < method.getParameterTypes().length; parameterIndex++) {
			Parameter parameterAnnotation = null;

			for (final Annotation annotation : method.getParameterAnnotations()[parameterIndex]) {
				if (annotation instanceof Parameter) {
					parameterAnnotation = (Parameter) annotation;
					break;
				}
			}

			if (parameterAnnotation != null) {
				parameterData.add(new ParameterData(parameterAnnotation, method.getParameterTypes()[parameterIndex]));
			} else {
				Mitw.getInstance().getLogger()
				.warning("Method '" + method.getName() + "' has a parameter without a @Parameter annotation.");
				return;
			}
		}

		commands.add(new CommandData(commandAnnotation, parameterData, method,
				method.getParameterTypes()[0].isAssignableFrom(Player.class)
				));

		Collections.sort(commands, (o1, o2) -> (o2.getName().length() - o1.getName().length()));
	}

	/**
	 * @return the full command line input of a player before running or tab completing a Nucleus command
	 */
	public static String[] getParameters(final Player player) {
		return CommandMap.parameters.get(player.getUniqueId());
	}

	/**
	 * Process a command (permission checks, argument validation, etc.)
	 *
	 * @param sender  The CommandSender executing this command. It should be noted that any non-player sender is treated
	 *                with full permissions.
	 * @param command The command to process (without a prepended '/')
	 *
	 * @return The Command executed
	 */
	public static CommandData evalCommand(final CommandSender sender, final String command) {
		String[] args = new String[]{ };
		CommandData found = null;

		CommandLoop:
			for (final CommandData commandData : commands) {
				for (final String alias : commandData.getNames()) {
					final String messageString = command.toLowerCase() + " ";
					final String aliasString = alias.toLowerCase() + " ";

					if (messageString.startsWith(aliasString)) {
						found = commandData;

						if (messageString.length() > aliasString.length()) {
							if (found.getParameters().size() == 0) {
								continue;
							}
						}

						// If there's 'space' after the command, parse args.
						// The +1 is there to account for a space after the command if there's parameters
						if (command.length() > alias.length() + 1) {
							// See above as to... why this works.
							args = (command.substring(alias.length() + 1)).split(" ");
						}

						// We break to the command loop as we have 2 for loops here.
						break CommandLoop;
					}
				}
			}

		if (found == null)
			return (null);

		if (!(sender instanceof Player) && !found.isConsoleAllowed()) {
			sender.sendMessage(ChatColor.RED + "This command does not support execution from the console.");
			return (found);
		}

		if (!found.canAccess(sender)) {
			sender.sendMessage(ChatColor.RED + "No permission.");
			return (found);
		}

		if (found.isAsync()) {
			final CommandData foundClone = found;
			final String[] argsClone = args;

			new BukkitRunnable() {
				@Override
				public void run() {
					foundClone.execute(sender, argsClone);
				}
			}.runTaskAsynchronously(Mitw.getInstance());
		} else {
			found.execute(sender, args);
		}

		return (found);
	}

	/**
	 * Transforms a parameter.
	 *
	 * @param sender      The CommandSender executing the command (or whoever we should transform 'for')
	 * @param parameter   The String to transform ('' if none)
	 * @param transformTo The class we should use to fetch our ParameterType (which we delegate transforming down to)
	 *
	 * @return The Object that we've transformed the parameter to.
	 */
	protected static Object transformParameter(final CommandSender sender, final String parameter, final Class<?> transformTo) {
		// Special-case Strings as they never need transforming.
		if (transformTo.equals(String.class))
			return (parameter);

		// This will throw a NullPointerException if there's no registered
		// parameter type, but that's fine -- as that's what we'd do anyway.
		return (parameterTypes.get(transformTo).transform(sender, parameter));
	}

	/**
	 * Tab completes a parameter.
	 *
	 * @param sender           The Player tab completing the command (not CommandSender as tab completion is for players
	 *                         only)
	 * @param parameter        The last thing the player typed in their style box before hitting tab ('' if none)
	 * @param transformTo      The class we should use to fetch our ParameterType (which we delegate tab completing down
	 *                         to)
	 * @param tabCompleteFlags The list of custom flags to use when tab completing this parameter.
	 *
	 * @return A List<String> of available tab completions. (empty if none)
	 */
	protected static List<String> tabCompleteParameter(final Player sender, final String parameter, final Class<?> transformTo,
			final String[] tabCompleteFlags) {
		if (!parameterTypes.containsKey(transformTo))
			return (new ArrayList<>());

		return (parameterTypes.get(transformTo).tabComplete(sender, ImmutableSet.copyOf(tabCompleteFlags), parameter));
	}

	/**
	 * Executes a command for the given player. Use this instead of Player#performCommand as that method does not call a
	 * PlayerCommandPreprocess event.
	 *
	 * @param sender The player to execute the command for.
	 */
	public static void executeCommand(final Player sender, final String commandLine) {
		final PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(sender, "/" + commandLine);
		Bukkit.getPluginManager().callEvent(event);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCommandPreProcess(final PlayerCommandPreprocessEvent event) {
		final String command = event.getMessage().substring(1);

		CommandMap.parameters.put(event.getPlayer().getUniqueId(), command.split(" "));

		if (evalCommand(event.getPlayer(), command) != null) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onConsoleCommand(final ServerCommandEvent event) {
		if (evalCommand(event.getSender(), event.getCommand()) != null) {
			event.setCancelled(true);
		}
	}

}