package nl.weeaboo.prefsstore;

import java.io.IOException;

public interface IPreferenceStore {

    void addPreferenceListener(IPreferenceListener l);

    void removePreferenceListener(IPreferenceListener l);

    void loadVariables() throws IOException;

    void saveVariables() throws IOException;

    <T> T get(Preference<T> pref);

    <T, V extends T> void set(Preference<T> pref, V value);

}
