package kz.tem.portal.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

//import org.apache.log4j.Logger;

/**
 * Miscellaneous class utility methods. Mainly for internal use within the
 * framework; consider Jakarta's Commons Lang for a more comprehensive suite
 * of class utilities.
 *
 * @author Keith Donald
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.1
 * @see TypeUtils
 * @see ReflectionUtils
 */
@SuppressWarnings("unchecked")
public abstract class ClassUtils {

  /** Suffix for array class names: "[]" */
  public static final String ARRAY_SUFFIX = "[]";

  /** Prefix for internal array class names: "[L" */
  private static final String INTERNAL_ARRAY_PREFIX = "[L";

  /** The ".class" file suffix */
  public static final String CLASS_FILE_SUFFIX = ".class";

  //private static final Logger logger = Logger.getLogger(ClassUtils.class);

  /**
   * Map with primitive type name as key and corresponding primitive
   * type as value, for example: "int" -> "int.class".
   */
  private static final Map<String,Class> primitiveTypeNameMap = new HashMap<String,Class>(16);


  static {

    Set<Class> primitiveTypeNames = new HashSet<Class>(16);
    primitiveTypeNames.add(boolean.class);
    primitiveTypeNames.add( byte.class);
    primitiveTypeNames.add( char.class);
    primitiveTypeNames.add( double.class);
    primitiveTypeNames.add( float.class);
    primitiveTypeNames.add( int.class);
    primitiveTypeNames.add(long.class);
    primitiveTypeNames.add( short.class);
    primitiveTypeNames.addAll(Arrays.asList(new Class[] {
        boolean[].class, byte[].class, char[].class, double[].class,
        float[].class, int[].class, long[].class, short[].class}));
    for (Iterator it = primitiveTypeNames.iterator(); it.hasNext();) {
      Class primitiveClass = (Class) it.next();
      primitiveTypeNameMap.put(primitiveClass.getName(), primitiveClass);
    }
  }


  /**
   * Return the default ClassLoader to use: typically the thread context
   * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
   * class will be used as fallback.
   * <p>Call this method if you intend to use the thread context ClassLoader
   * in a scenario where you absolutely need a non-null ClassLoader reference:
   * for example, for class path resource loading (but not necessarily for
   * <code>Class.forName</code>, which accepts a <code>null</code> ClassLoader
   * reference as well).
   * @return the default ClassLoader (never <code>null</code>)
   * @see java.lang.Thread#getContextClassLoader()
   */
  public static ClassLoader getDefaultClassLoader() {
    ClassLoader cl = null;
    try {
      cl = Thread.currentThread().getContextClassLoader();
    }
    catch (Throwable ex) {
      //logger.debug("Cannot access thread context ClassLoader - falling back to system class loader", ex);
    }
    if (cl == null) {
      // No thread context class loader -> use class loader of this class.
      cl = ClassUtils.class.getClassLoader();
    }
    return cl;
  }

  /**
   * Replacement for <code>Class.forName()</code> that also returns Class instances
   * for primitives (like "int") and array class names (like "String[]").
   * @param name the name of the Class
   * @param classLoader the class loader to use
   * (may be <code>null</code>, which indicates the default class loader)
   * @return Class instance for the supplied name
   * @throws ClassNotFoundException if the class was not found
   * @throws LinkageError if the class file could not be loaded
   * @see Class#forName(String, boolean, ClassLoader)
   */
  public static Class forName(String name, ClassLoader classLoader) throws ClassNotFoundException, LinkageError {

    Class clazz = resolvePrimitiveClassName(name);
    if (clazz != null) {
      return clazz;
    }

    // "java.lang.String[]" style arrays
    if (name.endsWith(ARRAY_SUFFIX)) {
      String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
      Class elementClass = forName(elementClassName, classLoader);
      return Array.newInstance(elementClass, 0).getClass();
    }

    // "[Ljava.lang.String;" style arrays
    int internalArrayMarker = name.indexOf(INTERNAL_ARRAY_PREFIX);
    if (internalArrayMarker != -1 && name.endsWith(";")) {
      String elementClassName = null;
      if (internalArrayMarker == 0) {
        elementClassName = name.substring(INTERNAL_ARRAY_PREFIX.length(), name.length() - 1);
      }
      else if (name.startsWith("[")) {
        elementClassName = name.substring(1);
      }
      Class elementClass = forName(elementClassName, classLoader);
      return Array.newInstance(elementClass, 0).getClass();
    }

    ClassLoader classLoaderToUse = classLoader;
    if (classLoaderToUse == null) {
      classLoaderToUse = getDefaultClassLoader();
    }
    return classLoaderToUse.loadClass(name);
  }

  /**
   * Resolve the given class name into a Class instance. Supports
   * primitives (like "int") and array class names (like "String[]").
   * <p>This is effectively equivalent to the <code>forName</code>
   * method with the same arguments, with the only difference being
   * the exceptions thrown in case of class loading failure.
   * @param className the name of the Class
   * @param classLoader the class loader to use
   * (may be <code>null</code>, which indicates the default class loader)
   * @return Class instance for the supplied name
   * @throws IllegalArgumentException if the class name was not resolvable
   * (that is, the class could not be found or the class file could not be loaded)
   * @see #forName(String, ClassLoader)
   */
  public static Class resolveClassName(String className, ClassLoader classLoader) throws IllegalArgumentException {
    try {
      return forName(className, classLoader);
    }
    catch (ClassNotFoundException ex) {
      throw new IllegalArgumentException("Cannot find class [" + className + "]. Root cause: " + ex);
    }
    catch (LinkageError ex) {
      throw new IllegalArgumentException("Error loading class [" + className +
          "]: problem with class file or dependent class. Root cause: " + ex);
    }
  }

  /**
   * Resolve the given class name as primitive class, if appropriate,
   * according to the JVM's naming rules for primitive classes.
   * <p>Also supports the JVM's internal class names for primitive arrays.
   * Does <i>not</i> support the "[]" suffix notation for primitive arrays;
   * this is only supported by {@link #forName}.
   * @param name the name of the potentially primitive class
   * @return the primitive class, or <code>null</code> if the name does not denote
   * a primitive class or primitive array class
   */
  public static Class resolvePrimitiveClassName(String name) {
    Class result = null;
    // Most class names will be quite long, considering that they
    // SHOULD sit in a package, so a length check is worthwhile.
    if (name != null && name.length() <= 8) {
      // Could be a primitive - likely.
      result = primitiveTypeNameMap.get(name);
    }
    return result;
  }

  /**
   * Return the qualified name of the given class: usually simply
   * the class name, but component type class name + "[]" for arrays.
   * @param clazz the class
   * @return the qualified name of the class
   */
  public static String getQualifiedName(Class clazz) {
    if (clazz.isArray()) {
      return getQualifiedNameForArray(clazz);
    }
    return clazz.getName();
  }

  /**
   * Build a nice qualified name for an array:
   * component type class name + "[]".
   * @param clazz the array class
   * @return a qualified name for the array class
   */
  private static String getQualifiedNameForArray(Class clazz) {
    StringBuffer buffer = new StringBuffer();
    while (clazz.isArray()) {
      clazz = clazz.getComponentType();
      buffer.append(ClassUtils.ARRAY_SUFFIX);
    }
    buffer.insert(0, clazz.getName());
    return buffer.toString();
  }

}
