package net.development.mitw.packetlistener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

/**
 * An utility class that simplifies reflection in Bukkit plugins.
 *
 * @author Kristian
 */
public final class Reflection {
	/**
	 * An interface for invoking a specific constructor.
	 */
	public interface ConstructorInvoker {
		/**
		 * Invoke a constructor for a specific class.
		 *
		 * @param arguments - the arguments to pass to the constructor.
		 * @return The constructed object.
		 */
		public Object invoke(Object... arguments);
	}

	/**
	 * An interface for invoking a specific method.
	 */
	public interface MethodInvoker {
		/**
		 * Invoke a method on a specific target object.
		 *
		 * @param target - the target object, or NULL for a static method.
		 * @param arguments - the arguments to pass to the method.
		 * @return The return value, or NULL if is void.
		 */
		public Object invoke(Object target, Object... arguments);
	}

	/**
	 * An interface for retrieving the field content.
	 *
	 * @param <T> - field type.
	 */
	public interface FieldAccessor<T> {
		/**
		 * Retrieve the content of a field.
		 *
		 * @param target - the target object, or NULL for a static field.
		 * @return The value of the field.
		 */
		public T get(Object target);

		/**
		 * Set the content of a field.
		 *
		 * @param target - the target object, or NULL for a static field.
		 * @param value - the new value of the field.
		 */
		public void set(Object target, Object value);

		/**
		 * Determine if the given object has this field.
		 *
		 * @param target - the object to test.
		 * @return TRUE if it does, FALSE otherwise.
		 */
		public boolean hasField(Object target);
	}

	// Deduce the net.minecraft.server.v* package
	private static String OBC_PREFIX = Bukkit.getServer().getClass().getPackage().getName();
	private static String NMS_PREFIX = OBC_PREFIX.replace("org.bukkit.craftbukkit", "net.minecraft.server");
	public static String VERSION = OBC_PREFIX.replace("org.bukkit.craftbukkit", "").replace(".", "");

	// Variable replacement
	private static Pattern MATCH_VARIABLE = Pattern.compile("\\{([^\\}]+)\\}");

	private Reflection() {
		// Seal class
	}

	/**
	 * Retrieve a field accessor for a specific field type and name.
	 *
	 * @param target - the target type.
	 * @param name - the name of the field, or NULL to ignore.
	 * @param fieldType - a compatible field type.
	 * @return The field accessor.
	 */
	public static <T> FieldAccessor<T> getField(final Class<?> target, final String name, final Class<T> fieldType) {
		return getField(target, name, fieldType, 0);
	}

	/**
	 * Retrieve a field accessor for a specific field type and name.
	 *
	 * @param className - lookup name of the class, see {@link #getClass(String)}.
	 * @param name - the name of the field, or NULL to ignore.
	 * @param fieldType - a compatible field type.
	 * @return The field accessor.
	 */
	public static <T> FieldAccessor<T> getField(final String className, final String name, final Class<T> fieldType) {
		return getField(getClass(className), name, fieldType, 0);
	}

	/**
	 * Retrieve a field accessor for a specific field type and name.
	 *
	 * @param target - the target type.
	 * @param fieldType - a compatible field type.
	 * @param index - the number of compatible fields to skip.
	 * @return The field accessor.
	 */
	public static <T> FieldAccessor<T> getField(final Class<?> target, final Class<T> fieldType, final int index) {
		return getField(target, null, fieldType, index);
	}

	/**
	 * Retrieve a field accessor for a specific field type and name.
	 *
	 * @param className - lookup name of the class, see {@link #getClass(String)}.
	 * @param fieldType - a compatible field type.
	 * @param index - the number of compatible fields to skip.
	 * @return The field accessor.
	 */
	public static <T> FieldAccessor<T> getField(final String className, final Class<T> fieldType, final int index) {
		return getField(getClass(className), fieldType, index);
	}

	// Common method
	private static <T> FieldAccessor<T> getField(final Class<?> target, final String name, final Class<T> fieldType, int index) {
		for (final Field field : target.getDeclaredFields()) {
			if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
				field.setAccessible(true);

				// A function for retrieving a specific field value
				return new FieldAccessor<T>() {

					@Override
					@SuppressWarnings("unchecked")
					public T get(final Object target) {
						try {
							return (T) field.get(target);
						} catch (final IllegalAccessException e) {
							throw new RuntimeException("Cannot access reflection.", e);
						}
					}

					@Override
					public void set(final Object target, final Object value) {
						try {
							field.set(target, value);
						} catch (final IllegalAccessException e) {
							throw new RuntimeException("Cannot access reflection.", e);
						}
					}

					@Override
					public boolean hasField(final Object target) {
						// target instanceof DeclaringClass
						return field.getDeclaringClass().isAssignableFrom(target.getClass());
					}
				};
			}
		}

		// Search in parent classes
		if (target.getSuperclass() != null)
			return getField(target.getSuperclass(), name, fieldType, index);

		throw new IllegalArgumentException("Cannot find field with type " + fieldType);
	}

	/**
	 * Search for the first publicly and privately defined method of the given name and parameter count.
	 *
	 * @param className - lookup name of the class, see {@link #getClass(String)}.
	 * @param methodName - the method name, or NULL to skip.
	 * @param params - the expected parameters.
	 * @return An object that invokes this specific method.
	 * @throws IllegalStateException If we cannot find this method.
	 */
	public static MethodInvoker getMethod(final String className, final String methodName, final Class<?>... params) {
		return getTypedMethod(getClass(className), methodName, null, params);
	}

	/**
	 * Search for the first publicly and privately defined method of the given name and parameter count.
	 *
	 * @param clazz - a class to start with.
	 * @param methodName - the method name, or NULL to skip.
	 * @param params - the expected parameters.
	 * @return An object that invokes this specific method.
	 * @throws IllegalStateException If we cannot find this method.
	 */
	public static MethodInvoker getMethod(final Class<?> clazz, final String methodName, final Class<?>... params) {
		return getTypedMethod(clazz, methodName, null, params);
	}

	/**
	 * Search for the first publicly and privately defined method of the given name and parameter count.
	 *
	 * @param clazz - a class to start with.
	 * @param methodName - the method name, or NULL to skip.
	 * @param returnType - the expected return type, or NULL to ignore.
	 * @param params - the expected parameters.
	 * @return An object that invokes this specific method.
	 * @throws IllegalStateException If we cannot find this method.
	 */
	public static MethodInvoker getTypedMethod(final Class<?> clazz, final String methodName, final Class<?> returnType, final Class<?>... params) {
		for (final Method method : clazz.getDeclaredMethods()) {
			if ((methodName == null || method.getName().equals(methodName))
					&& (returnType == null || method.getReturnType().equals(returnType))
					&& Arrays.equals(method.getParameterTypes(), params)) {
				method.setAccessible(true);

				return (target, arguments) -> {
					try {
						return method.invoke(target, arguments);
					} catch (final Exception e) {
						throw new RuntimeException("Cannot invoke method " + method, e);
					}
				};
			}
		}

		// Search in every superclass
		if (clazz.getSuperclass() != null)
			return getMethod(clazz.getSuperclass(), methodName, params);

		throw new IllegalStateException(String.format("Unable to find method %s (%s).", methodName, Arrays.asList(params)));
	}

	/**
	 * Search for the first publically and privately defined constructor of the given name and parameter count.
	 *
	 * @param className - lookup name of the class, see {@link #getClass(String)}.
	 * @param params - the expected parameters.
	 * @return An object that invokes this constructor.
	 * @throws IllegalStateException If we cannot find this method.
	 */
	public static ConstructorInvoker getConstructor(final String className, final Class<?>... params) {
		return getConstructor(getClass(className), params);
	}

	/**
	 * Search for the first publically and privately defined constructor of the given name and parameter count.
	 *
	 * @param clazz - a class to start with.
	 * @param params - the expected parameters.
	 * @return An object that invokes this constructor.
	 * @throws IllegalStateException If we cannot find this method.
	 */
	public static ConstructorInvoker getConstructor(final Class<?> clazz, final Class<?>... params) {
		for (final Constructor<?> constructor : clazz.getDeclaredConstructors()) {
			if (Arrays.equals(constructor.getParameterTypes(), params)) {
				constructor.setAccessible(true);

				return arguments -> {
					try {
						return constructor.newInstance(arguments);
					} catch (final Exception e) {
						throw new RuntimeException("Cannot invoke constructor " + constructor, e);
					}
				};
			}
		}

		throw new IllegalStateException(String.format("Unable to find constructor for %s (%s).", clazz, Arrays.asList(params)));
	}

	/**
	 * Retrieve a class from its full name, without knowing its type on compile time.
	 * <p>
	 * This is useful when looking up fields by a NMS or OBC type.
	 * <p>
	 *
	 * @see {@link #getClass()} for more information.
	 * @param lookupName - the class name with variables.
	 * @return The class.
	 */
	public static Class<Object> getUntypedClass(final String lookupName) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final
		Class<Object> clazz = (Class) getClass(lookupName);
		return clazz;
	}

	/**
	 * Retrieve a class from its full name.
	 * <p>
	 * Strings enclosed with curly brackets - such as {TEXT} - will be replaced according to the following table:
	 * <p>
	 * <table border="1">
	 * <tr>
	 * <th>Variable</th>
	 * <th>Content</th>
	 * </tr>
	 * <tr>
	 * <td>{nms}</td>
	 * <td>Actual package name of net.minecraft.server.VERSION</td>
	 * </tr>
	 * <tr>
	 * <td>{obc}</td>
	 * <td>Actual pacakge name of org.bukkit.craftbukkit.VERSION</td>
	 * </tr>
	 * <tr>
	 * <td>{version}</td>
	 * <td>The current Minecraft package VERSION, if any.</td>
	 * </tr>
	 * </table>
	 *
	 * @param lookupName - the class name with variables.
	 * @return The looked up class.
	 * @throws IllegalArgumentException If a variable or class could not be found.
	 */
	public static Class<?> getClass(final String lookupName) {
		return getCanonicalClass(expandVariables(lookupName));
	}

	/**
	 * Retrieve a class in the net.minecraft.server.VERSION.* package.
	 *
	 * @param name - the name of the class, excluding the package.
	 * @throws IllegalArgumentException If the class doesn't exist.
	 */
	public static Class<?> getMinecraftClass(final String name) {
		return getCanonicalClass(NMS_PREFIX + "." + name);
	}

	/**
	 * Retrieve a class in the org.bukkit.craftbukkit.VERSION.* package.
	 *
	 * @param name - the name of the class, excluding the package.
	 * @throws IllegalArgumentException If the class doesn't exist.
	 */
	public static Class<?> getCraftBukkitClass(final String name) {
		return getCanonicalClass(OBC_PREFIX + "." + name);
	}

	/**
	 * Retrieve a class by its canonical name.
	 *
	 * @param canonicalName - the canonical name.
	 * @return The class.
	 */
	private static Class<?> getCanonicalClass(final String canonicalName) {
		try {
			return Class.forName(canonicalName);
		} catch (final ClassNotFoundException e) {
			throw new IllegalArgumentException("Cannot find " + canonicalName, e);
		}
	}

	/**
	 * Expand variables such as "{nms}" and "{obc}" to their corresponding packages.
	 *
	 * @param name - the full name of the class.
	 * @return The expanded string.
	 */
	private static String expandVariables(final String name) {
		final StringBuffer output = new StringBuffer();
		final Matcher matcher = MATCH_VARIABLE.matcher(name);

		while (matcher.find()) {
			final String variable = matcher.group(1);
			String replacement = "";

			// Expand all detected variables
			if ("nms".equalsIgnoreCase(variable)) {
				replacement = NMS_PREFIX;
			} else if ("obc".equalsIgnoreCase(variable)) {
				replacement = OBC_PREFIX;
			} else if ("version".equalsIgnoreCase(variable)) {
				replacement = VERSION;
			} else
				throw new IllegalArgumentException("Unknown variable: " + variable);

			// Assume the expanded variables are all packages, and append a dot
			if (replacement.length() > 0 && matcher.end() < name.length() && name.charAt(matcher.end()) != '.') {
				replacement += ".";
			}
			matcher.appendReplacement(output, Matcher.quoteReplacement(replacement));
		}

		matcher.appendTail(output);
		return output.toString();
	}

}