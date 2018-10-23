package net.development.mitw.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;

public abstract class Reflection {
	public static String getVersion() {
		/*  39 */ final String name = Bukkit.getServer().getClass().getPackage().getName();
		/*  40 */ final String version = name.substring(name.lastIndexOf('.') + 1) + ".";
		/*  41 */ return version;
	}

	public static Class<?> getNMSClass(String className) {
		/*  45 */ final String fullName = "net.minecraft.server." + getVersion() + className;
		/*  46 */ Class<?> clazz = null;
		try {
			/*  48 */ clazz = Class.forName(fullName);
		} catch (final Exception e) {
			/*  50 */ e.printStackTrace();
		}
		/*  52 */ return clazz;
	}

	public static Class<?> getNMSClassWithException(String className) throws Exception {
		/*  56 */ final String fullName = "net.minecraft.server." + getVersion() + className;
		/*  57 */ final Class<?> clazz = Class.forName(fullName);
		/*  58 */ return clazz;
	}

	public static Class<?> getOBCClass(String className) {
		/*  62 */ final String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
		/*  63 */ Class<?> clazz = null;
		try {
			/*  65 */ clazz = Class.forName(fullName);
		} catch (final Exception e) {
			/*  67 */ e.printStackTrace();
		}
		/*  69 */ return clazz;
	}

	public static Object getHandle(Object obj) {
		try {
			/*  74 */ return getMethod(obj.getClass(), "getHandle", new Class[0]).invoke(obj, new Object[0]);
		} catch (final Exception e) {
			/*  76 */ e.printStackTrace();
		}
		/*  78 */ return null;
	}

	public static Field getField(Class<?> clazz, String name) {
		try {
			/*  83 */ final Field field = clazz.getDeclaredField(name);
			/*  84 */ field.setAccessible(true);
			/*  85 */ return field;
		} catch (final Exception e) {
			/*  87 */ e.printStackTrace();
		}
		/*  89 */ return null;
	}

	public static Field getFieldWithException(Class<?> clazz, String name) throws Exception {
		/*  93 */ final Field field = clazz.getDeclaredField(name);
		/*  94 */ field.setAccessible(true);
		/*  95 */ return field;
	}

	public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
		/*  99 */ for (final Method m : clazz.getMethods()) {
			/* 100 */ if ((m.getName().equals(name)) && ((args.length == 0) || (ClassListEqual(args, m.getParameterTypes())))) {
				/* 101 */ m.setAccessible(true);
				/* 102 */ return m;
			}
		}
		/* 105 */ return null;
	}

	public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
		/* 109 */ boolean equal = true;
		/* 110 */ if (l1.length != l2.length)
			return false;
		/* 111 */ for (int i = 0; i < l1.length; i++) {
			/* 112 */ if (l1[i] != l2[i]) {
				/* 113 */ equal = false;
				/* 114 */ break;
			}
		}
		/* 117 */ return equal;
	}
}

/* Location:              C:\Users\msi\Downloads\HologramAPI_v1.6.2.jar!\de\inventivegames\hologram\reflection\Reflection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */