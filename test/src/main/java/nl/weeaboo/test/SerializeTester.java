package nl.weeaboo.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class SerializeTester {

    private SerializeTester() {
    }

    /**
     * Serializes a Java object to a {@code byte[]}.
     */
    public static <T> byte[] serializeObject(T obj) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(bout);
            try {
                out.writeObject(obj);
            } finally {
                out.close();
            }
        } catch (IOException ioe) {
            throw new AssertionError(ioe);
        }
        return bout.toByteArray();
    }

    /**
     * Deserializes a Java object previously serialized with {@link #serializeObject(Object)}.
     * @see #deserializeObject(InputStream, Class)
     */
    public static <T> T deserializeObject(byte[] data, Class<T> clazz) {
        try {
            return deserializeObject(new ByteArrayInputStream(data), clazz);
        } catch (IOException e) {
            throw new AssertionError(e);
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Deserializes a Java object previously serialized with {@link #serializeObject(Object)}.
     * @see #deserializeObject(byte[], Class)
     *
     * @throws ClassNotFoundException If the stored object is of an unknown type.
     * @throws ClassCastException If the stored object is of an incompatible type.
     * @throws IOException If an I/O error occurs while reading from the input stream.
     */
    public static <T> T deserializeObject(InputStream in, Class<T> clazz) throws IOException, ClassNotFoundException {
        ObjectInputStream oin = new ObjectInputStream(in);
        return clazz.cast(oin.readObject());
    }

    /**
     * Serializes, then immediately deserializes a Java object.
     *
     * @return The deserialized object.
     */
    public static <T> T reserialize(T object) {
        @SuppressWarnings("unchecked")
        Class<? extends T> type = (Class<? extends T>)object.getClass();
        return deserializeObject(serializeObject(object), type);
    }

}
