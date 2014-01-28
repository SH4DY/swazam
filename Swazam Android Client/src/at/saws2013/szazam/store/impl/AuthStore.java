package at.saws2013.szazam.store.impl;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import at.saws2013.szazam.store.IAuthStore;

/**
 * Singleton class which stores and gets the authentification
 * token from the shared preferences
 * 
 * @author René
 *
 */
public class AuthStore extends ContextWrapper implements IAuthStore {
	
	private final static String TOKEN = "app_token";
	
	private volatile static AuthStore instance;
	private SharedPreferences prefs;
	
	public static AuthStore getInstance(Context context){
		if (instance == null){
			synchronized (AuthStore.class) {
				if (instance == null) {
					Log.d("tokenStore", "creating new instance");
					instance = new AuthStore(context);
				}
			}
		}
		return instance;
	}

	private AuthStore(Context context){
		super(context);
	}

	@Override
	public String getToken() {
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		return prefs.getString(TOKEN, null);
	}

	@Override
	public void setToken(String token) {
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(TOKEN, token);
		editor.commit();
	}

	@Override
	public void invalidateToken() {
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(TOKEN);
		editor.commit();
	}

}
