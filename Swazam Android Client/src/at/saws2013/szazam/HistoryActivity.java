package at.saws2013.szazam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;
import at.saws2013.szazam.entities.IListItem;
import at.saws2013.szazam.store.impl.AuthStore;
import at.saws2013.szazam.store.impl.RequestStore;
import at.saws2013.szazam.store.impl.TransactionStore;
import at.saws2013.szazam.ui.CustomListAdapter;
import at.saws2013.szazam.volley.CustomVolleyStringRequest;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;


/**
 * Activity which displays the transactions or requests
 * history in a list, depending on the intent parameters
 * 
 * @author René
 *
 */
public class HistoryActivity extends BaseActivity {
	
	public static final String HISTORY_TYPE = "history";
	public static final int TRANSACTIONS = 1;
	public static final int REQUESTS = 2;
	private App app;
	private ListView lv_history;
	private CustomListAdapter adapter;
	private List<IListItem> list;	
	private CustomVolleyStringRequest fetchFromServerRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_history);
		// Show the Up button in the action bar.
		setupActionBar();
		app = (App) getApplication();
		initViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (fetchFromServerRequest != null){
			setProgressBarIndeterminateVisibility(true);
			app.getVolleyQueue().add(fetchFromServerRequest);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		app.getVolleyQueue().cancelAll(HistoryActivity.class.getName());
	}
	

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	void initViews() {
		lv_history = (ListView) findViewById(R.id.lv_history);
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(HISTORY_TYPE)){
			switch (extras.getInt(HISTORY_TYPE, REQUESTS)) {
			case TRANSACTIONS:
				getActionBar().setTitle(getString(R.string.transactions));
				TransactionStore transactionStore = TransactionStore.getInstance(getApplicationContext());
				list = transactionStore.getTransactionHistory();
				adapter = new CustomListAdapter(HistoryActivity.this, R.layout.listitem_transaction, list);
				lv_history.setAdapter(adapter);
				initServerRequest(app.getHostFromSettings()+"/transactions?token=" +AuthStore.getInstance(getApplicationContext()).getToken());
				break;
			default:
				getActionBar().setTitle(getString(R.string.requests));
				RequestStore requestStore = RequestStore.getInstance(getApplicationContext());
				list = requestStore.getRequestHistory();
				adapter = new CustomListAdapter(HistoryActivity.this, R.layout.listitem_request, list);
				lv_history.setAdapter(adapter);
				initServerRequest(app.getHostFromSettings()+"/requests?token=" +AuthStore.getInstance(getApplicationContext()).getToken());
				break;
			}
		}
	}
	
	private void initServerRequest(final String url) {
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("token", AuthStore.getInstance(getApplicationContext()).getToken());
		try {
			fetchFromServerRequest = new CustomVolleyStringRequest(Method.GET, url, new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {
					Log.d("HistoryActivity", response);
					JSONArray responseJSON;
					try {
						responseJSON = new JSONArray(response);
						if (url.contains("transactions")){
							TransactionStore.getInstance(getApplicationContext()).storeTransactions(responseJSON);
							list = TransactionStore.getInstance(getApplicationContext()).transformToList(responseJSON);
						} else {
							RequestStore.getInstance(getApplicationContext()).storeRequests(responseJSON);
							list = RequestStore.getInstance(getApplicationContext()).transformToList(responseJSON);
						}
						adapter.notifyDataSetChanged();
					} catch (JSONException e) {
						e.printStackTrace();
					}	
					setProgressBarIndeterminateVisibility(false);
				}
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Log.d("HistoryActivity", "" +error.getMessage());
					setProgressBarIndeterminateVisibility(false);			
				}
			}, params);
			fetchFromServerRequest.setTag(HistoryActivity.class.getName());
		} catch (NullPointerException e) {
			Toast.makeText(getApplicationContext(), "Incorrect host URL", Toast.LENGTH_SHORT).show();
		}
	}

}
