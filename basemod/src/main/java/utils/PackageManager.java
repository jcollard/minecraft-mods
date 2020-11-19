package utils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.io.PatternFilenameFilter;

public class PackageManager {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static Set<Class<?>> loadClassesInPackage(String packageName) {  
		Set<Class<?>> set = new HashSet<>();
		set.addAll(getClassesInPackage(packageName));
		return set;
	}
	
	private static String modsPath = null;
	
	private static boolean findModsPath(String pathToFind, String path) {
		LOGGER.info("Searching for " + pathToFind);
		LOGGER.info("Searching " + path);
		if(path.contains(pathToFind)) {
			LOGGER.info("FOUND IT!");
			modsPath = path.substring(0, path.indexOf(pathToFind) + pathToFind.length() - "libraries".length()) + "mods" + File.separator;
			LOGGER.info("Updating modsPath: " + modsPath);
			return true;
		}
		return false;
	}
	
	public static final List<Class<?>> getClassesInPackage(String packageName) {
		LOGGER.info("Searching for class packages: " + packageName);
	    String path = packageName.replaceAll("\\.", File.separator);
	    List<Class<?>> classes = new ArrayList<>();
	    List<String> classPathEntries = new LinkedList<String>();
	    classPathEntries.addAll(Arrays.asList(System.getProperty("java.class.path").split(
	            System.getProperty("path.separator")
	    )));
	    
	    LOGGER.info("Class Path Entries: " + classPathEntries);
	    
	    String name;
	    
	    if (modsPath == null) {
	    	LOGGER.info("modsPath is null, searching for libraries.");
	    	// Try to discover the path to minecraft mods
	    	// TODO: This is a super hack; there has to be a better way to find
	    	// where the mods folder is
	    	for (String classpathEntry : classPathEntries) {
	    		if(findModsPath("minecraft" + File.separator + "libraries", classpathEntry)) {
	    			break;
	    		}
	    	}
	    }
	    
	    if (modsPath != null) {
	    	LOGGER.info("Mods path found: " + modsPath);
	    	File pathToMod = new File(modsPath);
	    	String[] files= pathToMod.list();
	    	LOGGER.info("Adding Jars: " + Arrays.toString(files));
	    	for (String file : files) {
	    		if (file.contains(".jar")) {
	    			classPathEntries.add(pathToMod + File.separator + file);
	    		}
	    	}
	    	
	    }
	    
	    for (String classpathEntry : classPathEntries) { 
	        if (classpathEntry.endsWith(".jar")) {
	            File jar = new File(classpathEntry);
	            try {
	                @SuppressWarnings("resource")
					JarInputStream is = new JarInputStream(new FileInputStream(jar));
	                JarEntry entry;
	                while((entry = is.getNextJarEntry()) != null) {
	                    name = entry.getName();
	                    if (name.endsWith(".class")) {
	                        if (name.contains(path) && name.endsWith(".class")) {
	                            String classPath = name.substring(0, entry.getName().length() - 6);
	                            classPath = classPath.replaceAll("[\\|/]", ".");
	                            classes.add(Class.forName(classPath));
	                        }
	                    }
	                }
	            } catch (Exception ex) {
	                // Silence is gold
	            	System.err.println("**************************************");
	            	System.err.println("**************************************");
	            	System.err.println("**************************************");
	            	System.err.println("**************************************");
	            	System.err.println("**************************************");
	            	ex.printStackTrace();
	            	
	            }
	        } else {
	            try {
	                File base = new File(classpathEntry + File.separatorChar + path);
	                if(base.listFiles() == null) {
	                	// Skip empty file structures
	                	continue;
	                }
	                for (File file : base.listFiles()) {
	                    name = file.getName();
	                    if (name.endsWith(".class")) {
	                        name = name.substring(0, name.length() - 6);
	                        System.out.println(packageName + "." + name);
	                        try { 
	                        	classes.add(Class.forName(packageName + "." + name));
	                        } catch (ClassNotFoundException e) {  
	                        	System.out.println(packageName + "." + name + " does not appear to be a valid class.");  
        					}  catch (ExceptionInInitializerError e) {
        						System.err.println(packageName + "." + name + " could not be initialized, skipping.");  
        					} catch (NoClassDefFoundError e){
        						System.err.println(packageName + "." + name + " could not be initialized, skipping.");
        					}
	                    }
	                }
	            } catch (Exception ex) {
	                // Silence is gold
	            	System.err.println("**************************************");
	            	System.err.println("**************************************");
	            	System.err.println("**************************************");
	            	System.err.println("**************************************");
	            	System.err.println("**************************************");
	            	ex.printStackTrace();
	            }
	        }
	    }

	    return classes;
	}

	public static void main(String ...strings) {
		String path = "C:\\Users\\josep\\AppData\\Roaming\\.minecraft\\mods\\";
		File file = new File(path);
		System.out.println(file);
		System.out.println(Arrays.toString(file.list()));
	}
}
