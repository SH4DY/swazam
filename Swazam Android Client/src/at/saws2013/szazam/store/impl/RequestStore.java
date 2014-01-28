package at.saws2013.szazam.store.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.util.Log;
import at.saws2013.szazam.App;
import at.saws2013.szazam.entities.IListItem;
import at.saws2013.szazam.entities.Request;
import at.saws2013.szazam.store.IRequestStore;

/**
 * Singleton class which stores and gets requests from
 * the shared prefereces
 * 
 * @author René
 *
 */
public class RequestStore extends ContextWrapper implements IRequestStore{
	
	private final static String REQUESTS = "requests";
	
	private static volatile RequestStore instance;
	private Gson gson;
	
	private SharedPreferences prefs;

	private RequestStore(Context base) {
		super(base);
		gson = new Gson();
	}
	
	public static RequestStore getInstance(Context context){
		if (instance == null){
			synchronized (RequestStore.class) {
				if (instance == null){
					instance = new RequestStore(context);
				}
			}
		}
		return instance;
	}

	@Override
	public List<IListItem> getRequestHistory() {
		prefs = getSharedPreferences(App.PREFS, MODE_MULTI_PROCESS);
		Type collectionType = new TypeToken<List<Request>>(){}.getType();
		String requestsOnDisk = prefs.getString(REQUESTS, null);
		Log.d(REQUESTS, requestsOnDisk);
		List<Request> requests = gson.fromJson(requestsOnDisk, collectionType);
		List<IListItem> requestsList;
		if (requests != null){
			requestsList = new ArrayList<IListItem>(requests);
			Collections.reverse(requestsList);
		} else {
			requestsList = new ArrayList<IListItem>();
		}
		return requestsList;
	}

	@Override
	public void storeRequest(Request request) {
		prefs = getSharedPreferences(App.PREFS, MODE_MULTI_PROCESS);
		Type collectionType = new TypeToken<ArrayList<Request>>(){}.getType();
		List<Request> requests = gson.fromJson(prefs.getString(REQUESTS, null), collectionType);
		if (requests == null){
			requests = new ArrayList<Request>();
		}
		requests.add(request);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REQUESTS, gson.toJson(requests));
		editor.commit();
	}

	public void storeRequests(JSONArray response) {
		prefs = getSharedPreferences(App.PREFS, MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REQUESTS, response.toString());
		editor.commit();
	}

	public List<IListItem> transformToList(JSONArray responseJSON) {
		Type collectionType = new TypeToken<List<Request>>(){}.getType();
		List<Request> requests = gson.fromJson(responseJSON.toString(), collectionType);
		List<IListItem> requestsList;
		if (requests != null){
			requestsList = new ArrayList<IListItem>(requests);
			Collections.reverse(requestsList);
		} else {
			requestsList = new ArrayList<IListItem>();
		}
		return requestsList;
	}

}
