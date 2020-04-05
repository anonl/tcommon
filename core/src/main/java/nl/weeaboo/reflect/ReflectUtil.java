package nl.weeaboo.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Functions related to Java reflection.
 */
public final class ReflectUtil {

    private ReflectUtil() {
    }

    /**
     * Retrieves all public static fields with the requested type.
     *
     * @return A map of {@code Field name -> value}
     * @throws IllegalAccessException If one of the fields is inaccessible. See {@link Field#get(Object)}.
     */
    public static <T> Map<String, T> getConstants(Class<?> constantsHolder, Class<T> type)
            throws IllegalAccessException {

        Map<String, T> result = new HashMap<String, T>();
        for (Field field : constantsHolder.getFields()) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && type.isAssignableFrom(field.getType())) {
                Object value = field.get(null);
                if (value != null) {
                    // Can't used checked cast, because Class.cast() doesn't work for primitive types
                    @SuppressWarnings("unchecked")
                    T typed = (T)value;

                    result.put(field.getName(), typed);
                }
            }
        }
        return result;
    }

}
