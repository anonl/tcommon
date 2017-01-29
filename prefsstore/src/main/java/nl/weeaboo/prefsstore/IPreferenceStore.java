package nl.weeaboo.prefsstore;

import java.io.IOException;

public interface IPreferenceStore {

    /**
     * Attaches a new preference listener.
     */
    void addPreferenceListener(IPreferenceListener l);

    /**
     * Removes a previously attached preference listener.
     */
    void removePreferenceListener(IPreferenceListener l);

    /**
     * Loads saved preference values from the backing storage.
     *
     * @throws IOException If the backing storage is corrupted or an error occurs while reading.
     */
    void loadVariables() throws IOException;

    /**
     * Saves preference values to the backing storage.
     *
     * @throws IOException If an I/O error occurred while attempting to write to the backing storage.
     */
    void saveVariables() throws IOException;

    /**
     * Returns the current value of the supplied preference.
     */
    <T> T get(Preference<T> pref);

    /**
     * Sets the value of the supplied preference.
     */
    <T, V extends T> void set(Preference<T> pref, V value);

}
