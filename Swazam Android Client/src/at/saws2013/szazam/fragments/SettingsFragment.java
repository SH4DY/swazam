package at.saws2013.szazam.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import at.saws2013.szazam.R;

/**
 * Fragment which allows the configuration of the
 * hostname. After that the hostname will automatically
 * stored in the shared preferences
 * 
 * @author René
 *
 */
public class SettingsFragment extends PreferenceFragment {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }


}
