package net.development.mitw.utils;

import com.google.common.collect.ImmutableSet;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassUtil {

	private ClassUtil() {
		throw new RuntimeException("Cannot instantiate a utility class.");
	}

	/**
	 * Gets all the classes in a the provided package.
	 *
	 * @param plugin      The plugin who owns the package
	 * @param packageName The package to scan classes in.
	 *
	 * @return The classes in the package packageName.
	 */
	public static Collection<Class<?>> getClassesInPackage(Plugin plugin, String packageName) {
		Collection<Class<?>> classes = new ArrayList<>();

		CodeSource codeSource = plugin.getClass().getProtectionDomain().getCodeSource();
		URL resource = codeSource.getLocation();
		String relPath = packageName.replace('.', '/');
		String resPath = resource.getPath().replace("%20", " ");
		String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
		JarFile jarFile;

		try {
			jarFile = new JarFile(jarPath);
		} catch (IOException e) {
			throw (new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e));
		}

		Enumeration<JarEntry> entries = jarFile.entries();

		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String entryName = entry.getName();
			String className = null;

			if (entryName.endsWith(".class") && entryName.startsWith(relPath) &&
			    entryName.length() > (relPath.length() + "/".length())) {
				className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
			}

			if (className != null) {
				Class<?> clazz = null;

				try {
					clazz = Class.forName(className);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				if (clazz != null) {
					classes.add(clazz);
				}
			}
		}

		try {
			jarFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (ImmutableSet.copyOf(classes));
	}

	public static String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
		URL dirURL = clazz.getClassLoader().getResource(path);
		if (dirURL != null && dirURL.getProtocol().equals("file")) {
			/* A file path: easy enough */
			return new File(dirURL.toURI()).list();
		}

		if (dirURL == null) {
			/*
			 * In case of a jar file, we can't actually find a directory.
			 * Have to assume the same jar as clazz.
			 */
			String me = clazz.getName().replace(".", "/")+".class";
			dirURL = clazz.getClassLoader().getResource(me);
		}

		if (dirURL.getProtocol().equals("jar")) {
			/* A JAR path */
			String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
			Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
			while(entries.hasMoreElements()) {
				String name = entries.nextElement().getName();
				if (name.startsWith(path)) { //filter according to the path
					String entry = name.substring(path.length());
					int checkSubdir = entry.indexOf("/");
					if (checkSubdir >= 0) {
						// if it is a subdirectory, we just return the directory name
						entry = entry.substring(0, checkSubdir);
					}
					result.add(entry);
				}
			}
			return result.toArray(new String[result.size()]);
		}

		throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
	}

}