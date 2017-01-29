package nl.weeaboo.prefsstore;

public abstract class Preference<T> {

    private final String key;
    private final String name;
    private final Class<T> type;
    private final T defaultValue;
    private final boolean constant;
    private final String description;

    /**
     * @param key Unique preference ID.
     * @param name Human-readable name.
     * @param type Preference value type.
     * @param defaultVal Default value.
     * @param c Is this a constant.
     * @param description Human-readable description of the preference's function.
     */
    public Preference(String key, String name, Class<T> type, T defaultVal, boolean c, String description) {
        this.key = key;
        this.name = name;
        this.type = type;
        this.defaultValue = defaultVal;
        this.constant = c;
        this.description = description;
    }

    // --- Factory functions (regular) ---

    /**
     * Constructs a new non-const boolean preference.
     * @see #Preference(String, String, Class, Object, boolean, String)
     */
    public static Preference<Boolean> newPreference(String key, String name, boolean defaultVal, String desc) {
        return new BasicPreference<Boolean>(key, Boolean.class, defaultVal, false, name, desc);
    }

    /**
     * Constructs a new non-const int preference.
     * @see #Preference(String, String, Class, Object, boolean, String)
     */
    public static Preference<Integer> newPreference(String key, String name, int defaultVal, String desc) {
        return new BasicPreference<Integer>(key, Integer.class, defaultVal, false, name, desc);
    }

    /**
     * Constructs a new non-const double preference.
     * @see #Preference(String, String, Class, Object, boolean, String)
     */
    public static Preference<Double> newPreference(String key, String name, double defaultVal, String desc) {
        return new BasicPreference<Double>(key, Double.class, defaultVal, false, name, desc);
    }

    /**
     * Constructs a new non-const string preference.
     * @see #Preference(String, String, Class, Object, boolean, String)
     */
    public static Preference<String> newPreference(String key, String name, String defaultVal, String desc) {
        return new BasicPreference<String>(key, String.class, defaultVal, false, name, desc);
    }

    /**
     * Constructs a new non-const enum preference.
     * @see #Preference(String, String, Class, Object, boolean, String)
     */
    public static <E extends Enum<E>> Preference<E> newPreference(String key, String name, Class<E> clazz, E defaultVal,
            String desc) {
        return new EnumPreference<E>(key, clazz, defaultVal, false, name, desc);
    }

    // -- Factory functions (const) ---

    /**
     * Constructs a new const boolean preference.
     * @see #Preference(String, String, Class, Object, boolean, String)
     */
    public static Preference<Boolean> newConstPreference(String key, String name, boolean defaultVal, String desc) {
        return new BasicPreference<Boolean>(key, Boolean.class, defaultVal, true, name, desc);
    }

    /**
     * Constructs a new const int preference.
     * @see #Preference(String, String, Class, Object, boolean, String)
     */
    public static Preference<Integer> newConstPreference(String key, String name, int defaultVal, String desc) {
        return new BasicPreference<Integer>(key, Integer.class, defaultVal, true, name, desc);
    }

    /**
     * Constructs a new const double preference.
     * @see #Preference(String, String, Class, Object, boolean, String)
     */
    public static Preference<Double> newConstPreference(String key, String name, double defaultVal, String desc) {
        return new BasicPreference<Double>(key, Double.class, defaultVal, true, name, desc);
    }

    /**
     * Constructs a new const string preference.
     * @see #Preference(String, String, Class, Object, boolean, String)
     */
    public static Preference<String> newConstPreference(String key, String name, String defaultVal, String desc) {
        return new BasicPreference<String>(key, String.class, defaultVal, true, name, desc);
    }

    /**
     * Constructs a new const enum preference.
     * @see #Preference(String, String, Class, Object, boolean, String)
     */
    public static <E extends Enum<E>> Preference<E> newConstPreference(String key, String name, Class<E> clazz,
            E defaultVal, String desc) {
        return new EnumPreference<E>(key, clazz, defaultVal, true, name, desc);
    }

    /**
     * Converts from a preference value to a serialized string representation.
     * @see #fromString(String)
     */
    public abstract String toString(T value);

    /**
     * Converts from a serialized string representation back to the preference value.
     * @see #toString(Object)
     */
    public abstract T fromString(String string);

    /**
     * Unique preference ID.
     */
    public String getKey() {
        return key;
    }

    /**
     * Preference value type.
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * Default value.
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * Is this a constant.
     */
    public boolean isConstant() {
        return constant;
    }

    /**
     * Human-readable name.
     */
    public String getName() {
        return name;
    }

    /**
     * Human-readable description of the preference's function.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param value The value to check.
     */
    public boolean isValidValue(T value) {
        return true;
    }

}
