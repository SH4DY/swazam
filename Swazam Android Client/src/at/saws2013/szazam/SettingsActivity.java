package at.saws2013.szazam;

import android.os.Bundle;
import android.app.Activity;
import at.saws2013.szazam.fragments.SettingsFragment;

/**
 * Activity which holds the settings fragment
 * 
 * @author René
 *
 */
public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
	}

}
