package fcfp.pp;

/**
 * Custom generic ClassLoader module.
 *
 * @author Simão Paulo Rato Alves Reis
 * @version 1.0
 */
class ByteClassLoader extends ClassLoader {

    /**
     * Loads a new class to the Java Virtual Machine using a serialized class.
     *
     * @param className name of the class to be loaded.
     * @param classBytes serialization of the ".class" file.
     * @return return the ".class" file class metadata.
     * @throws ClassNotFoundException when the classByets don't contain any class
     * with the given name.
     */
    Class<?> defineClass(String className, byte[] classBytes) throws ClassNotFoundException {

        return super.defineClass(className, classBytes, 0, classBytes.length);
    }
}
