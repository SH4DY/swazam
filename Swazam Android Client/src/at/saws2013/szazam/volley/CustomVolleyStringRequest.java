package at.saws2013.szazam.volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

/**
 * Custom volley request which overwrites the getHeaders()
 * and getParams() method to set the "Content-Type" in the
 * header and to add request parameters
 * @author René
 *
 */
public class CustomVolleyStringRequest extends StringRequest {
	
	private Map<String,String> params;

	public CustomVolleyStringRequest(int method, String url, Listener<String> listener,
			ErrorListener errorListener, Map<String,String> params) {
		super(method, url, listener, errorListener);
		this.params = params;
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<String, String>();
		headers.putAll(super.getHeaders());
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		return headers;
	}
	
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		if (null != params){
			for (Entry<String, String> param : params.entrySet()) {
				Log.d("params", param.getKey() + " : " + param.getValue());
			}
			return params;
		}
		else
			return super.getParams();
	}
	
	

}
