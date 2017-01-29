package nl.weeaboo.prefsstore;

public interface IPreferenceListener {

    <T> void onPreferenceChanged(Preference<T> pref, T oldValue, T newValue);

}
