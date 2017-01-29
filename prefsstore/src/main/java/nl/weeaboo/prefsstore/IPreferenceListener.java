package nl.weeaboo.prefsstore;

public interface IPreferenceListener {

    /**
     * Called when a preference's value changes.
     */
    <T> void onPreferenceChanged(Preference<T> pref, T oldValue, T newValue);

}
