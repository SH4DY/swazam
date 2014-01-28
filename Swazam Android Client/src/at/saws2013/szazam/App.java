package at.saws2013.szazam;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import android.app.Application;
import android.content.SharedPreferences;


/**
 * Application class which holds some app-wide
 * volleyQueue instance
 * 
 * @author René
 *
 */
public class App extends Application {
	
	private RequestQueue volleyQueue;
	private SharedPreferences prefs;
	
	public final static String PREFS = "at.saws2013.szazam_preferences";
	
	@Override
	public void onCreate() {
		super.onCreate();		
		volleyQueue = Volley.newRequestQueue(this);
	}
	
	public synchronized RequestQueue getVolleyQueue(){
		return volleyQueue;
	}
	
	/**
	 * Get the hostname from settings
	 * @return Hostname which is set in the settings
	 */
	public String getHostFromSettings(){
		prefs = getSharedPreferences(PREFS, MODE_MULTI_PROCESS);
		return prefs.getString("host", "http://localhost");		
	}

}
