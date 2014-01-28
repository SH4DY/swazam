package at.saws2013.szazam;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import at.saws2013.szazam.entities.Request;
import at.saws2013.szazam.fingerprint.IFingerPrintSystem;
import at.saws2013.szazam.fingerprint.impl.FingerPrintCreator;
import at.saws2013.szazam.media.IAudioFilePicker;
import at.saws2013.szazam.media.IAudioRecorder;
import at.saws2013.szazam.media.impl.AudioFilePicker;
import at.saws2013.szazam.media.impl.AudioRecorder;
import at.saws2013.szazam.store.IAuthStore;
import at.saws2013.szazam.store.impl.AuthStore;
import at.saws2013.szazam.store.impl.RequestStore;
import at.saws2013.szazam.ui.ViewTools;
import at.saws2013.szazam.volley.CustomVolleyStringRequest;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import de.passsy.holocircularprogressbar.HoloCircularProgressBar;

/**
 * RecognitionActivity is the application's main Activity.
 * It will be launched when the app starts and allows to create
 * a fingerprint by either record some sound using the phone's
 * microphone of by picking a audio on the phone.
 * 
 * @author René
 *
 */
public class RecognitionActivity extends Activity {

	private static final float RECORD_DURATION = 20000;
	private static final float MAX_PROGRESS = 1000;

	private static final String LOG_TAG = "Swazam";
	private static String mFileName;
	private HoloCircularProgressBar progress;
	private ProgressBar progress_loading;
	private ImageButton imgbtn_pick_file, imgbtn_record;
	private Fingerprint fingerprint;
	private AudioSampleWorker fingerprintWorkerTask;
	
	private IAuthStore authStore;
	
	private CustomVolleyStringRequest recognitionRequest;

	private IAudioFilePicker mAudioFilePicker;
	private IAudioRecorder mAudioRecorder;

	private boolean filepick;
	
	private App app;

	private CountDownTimer timer;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("filepick", filepick);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		filepick = savedInstanceState.getBoolean("filepick");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recognition);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		app = (App) getApplication();
		
		authStore = AuthStore.getInstance(getApplicationContext());
		initHelpers();
		initViews();
		initTimer();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// Start the login procedure if there is no token
		if (authStore.getToken() == null){
			login();
		}
	}
	
	/**
	 * Start the LoginActivity by intent
	 */
	private void login(){
		Intent i = new Intent(RecognitionActivity.this, LoginActivity.class);
		startActivity(i);
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
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
		case R.id.menu_requests:
			getRequestHistory();
			return true;		
		case R.id.action_settings:
			i = new Intent(RecognitionActivity.this, SettingsActivity.class);
			startActivity(i);
			return true;
		case R.id.menu_transactions:
			getTransactionHistory();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Start the HisoryActivity and put TRANSACTIONS
	 * as intent extra
	 */
	private void getTransactionHistory() {
		Intent i = new Intent(RecognitionActivity.this, HistoryActivity.class);
		i.putExtra(HistoryActivity.HISTORY_TYPE, HistoryActivity.TRANSACTIONS);
		startActivity(i);
	}
	
	/**
	 * Start the HisoryActivity and put REQUESTS
	 * as intent extra
	 */
	private void getRequestHistory() {
		Intent i = new Intent(RecognitionActivity.this, HistoryActivity.class);
		i.putExtra(HistoryActivity.HISTORY_TYPE, HistoryActivity.REQUESTS);
		startActivity(i);
	}

	/**
	 * Initialize all needed components
	 */
	private void initHelpers(){
		mFileName = getDefaultTrackLocation();
		mAudioFilePicker = new AudioFilePicker(RecognitionActivity.this);
		mAudioRecorder = new AudioRecorder(RecognitionActivity.this);
	}
	
	private String getDefaultTrackLocation(){
		return getFilesDir() + File.separator + "audiotrack.3gp";
	}

	/**
	 * Start the file-pick procedure to select an audio file from
	 * disc
	 * 
	 * @param v	Button defined in activity_recognition layout
	 */
	public void pickAudioFile(View v) {
		filepick = true;
		mAudioFilePicker.startPickIntent(RecognitionActivity.this);
	}

	@Override
	public void onPause() {
		super.onPause();
		mAudioRecorder.release();
		if (fingerprintWorkerTask != null) {
			fingerprintWorkerTask.cancel(true);
		}
		// Cancel all running volley requests in order to not block the network
		app.getVolleyQueue().cancelAll(RecognitionActivity.class.getName());

		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		/*	
		 * if a previous audio-pick intent has been started create a 
		 * fingerprint using the received data
		*/
		case AudioFilePicker.AUDIO_FILE_PICKER_REQUEST:
			if (resultCode == Activity.RESULT_OK) {
				if (null != mAudioFilePicker) {
					createFingerPrint(mAudioFilePicker.getAudioTrackPath(data));
				}
			}
			break;
		}
	}

	private void createFingerPrint(String path) {
		if (null != path) {
			fingerprintWorkerTask = new AudioSampleWorker(path);
			fingerprintWorkerTask
			.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	/**
	 * Instantiate all views
	 */
	private void initViews() {
		progress = (HoloCircularProgressBar) findViewById(R.id.progress);
		progress.setMarkerEnabled(false);
		progress.setProgress(0f);
		progress_loading = (ProgressBar) findViewById(R.id.progress_loading);
		imgbtn_pick_file = (ImageButton) findViewById(R.id.imgbtn_pick_file);
		imgbtn_record = (ImageButton) findViewById(R.id.imgbtn_record);
	}

	/**
	 * Initialize a timer which will stop the audio-recording
	 * if the record_duration has terminated
	 */
	private void initTimer() {
		timer = new CountDownTimer((long) RECORD_DURATION, 25) {

			public void onTick(long millisUntilFinished) {
				progress.setProgress(((RECORD_DURATION - millisUntilFinished) * 10000)
						/ (RECORD_DURATION * MAX_PROGRESS) / 10.2f + 0.025f);
			}

			public void onFinish() {
				stopRecording();
				createFingerPrint(mFileName);
			}
		};
	}

	/**
	 * Method which will be called by the record button
	 * 
	 * @param v	Button which is defined in activity_recognition layout
	 */
	public void recordSample(View v) {
		Log.d(LOG_TAG, "start recording");
		timer.start();
		ViewTools.disableViews(imgbtn_pick_file, imgbtn_record);
		mAudioRecorder.startRecording(mFileName);
	}

	private void stopRecording() {
		ViewTools.enableViews(imgbtn_pick_file, imgbtn_record);
		mAudioRecorder.stopRecording();
	}
	
	/**
	 * Worker-task which creates a fingerprint off the UI task
	 * due to Android design-guidelines this has to be an inner class
	 * 
	 * @author René
	 *
	 */
	private class AudioSampleWorker extends AsyncTask<Void, Float, Integer> {
		private String fileName;
		private IFingerPrintSystem fingerprintCreator;

		public AudioSampleWorker(String fileName) {
			this.fileName = fileName;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress_loading.setVisibility(View.VISIBLE);
			fingerprintCreator = new FingerPrintCreator(RecognitionActivity.this);
			ViewTools.disableViews(imgbtn_pick_file, imgbtn_record);
		}

		@Override
		protected Integer doInBackground(Void... params) {
			fingerprint = fingerprintCreator.createFingerprintFromFilePath(fileName);
			return 1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (null != fingerprint){
				Crouton.makeText(RecognitionActivity.this, getString(R.string.fingerprint_created), Style.INFO).show();				
			} else {
				Crouton.makeText(RecognitionActivity.this, getString(R.string.error_creating_fingerprint), Style.ALERT).show();				
			}
			progress_loading.setVisibility(View.GONE);
			ViewTools.enableViews(imgbtn_pick_file, imgbtn_record);
			filepick = false;
			makeRecognitionRequest(fingerprint);
		}
	}

	/**
	 * Send a recognition-request to the server using a custom volley 
	 * request
	 * 
	 * On a successful request, store the request on disk using the
	 * RequestStore
	 * 
	 * Notify the user if the request was not successful
	 * 
	 * @param fingerprint2
	 */
	public void makeRecognitionRequest(Fingerprint fingerprint2) {
		final Gson gson = new Gson();
		//Long id = System.currentTimeMillis();
		//Request request = new Request(id, fingerprint2.toString(), "null", DateUtils.formatDateTime(getApplicationContext(), id, (DateUtils.FORMAT_24HOUR | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE)));
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", AuthStore.getInstance(getApplicationContext()).getToken());
		params.put("fingerprint", fingerprint2.toString());
		recognitionRequest = new CustomVolleyStringRequest(Method.POST, app.getHostFromSettings() + "/request",  
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						RequestStore.getInstance(getApplicationContext()).storeRequest(gson.fromJson(response.toString(), Request.class));
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), "Error sending request to server", Toast.LENGTH_SHORT).show();
					}
				}, params);
		recognitionRequest.setTag(RecognitionActivity.class.getName());
		app.getVolleyQueue().add(recognitionRequest);
	}
}
