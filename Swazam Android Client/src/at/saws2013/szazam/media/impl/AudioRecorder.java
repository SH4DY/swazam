package at.saws2013.szazam.media.impl;

import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaRecorder;
import android.util.Log;
import at.saws2013.szazam.media.IAudioRecorder;

/**
 * Class to record an audio file using the phone's
 * microphone
 * 
 * @author René
 *
 */
public class AudioRecorder extends ContextWrapper implements IAudioRecorder{	
	
	private final static String LOG_TAG = "AudioRecorder";
	private String mRecordPath;

    private MediaRecorder mRecorder = null;

	public AudioRecorder(Context base) {
		super(base);
	}
	
	@Override
	public String getRecortPath(){
		return mRecordPath;
	}
	
	@Override
	public void startRecording(String recordPath){
		this.mRecordPath = recordPath;
		if (mRecorder == null){
	        mRecorder = new MediaRecorder();
	        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        mRecorder.setOutputFile(mRecordPath);
	        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	
	        try {
	            mRecorder.prepare();
	        } catch (IOException e) {
	            Log.e(LOG_TAG, "prepare() failed");
	        }	
	        mRecorder.start();
		}
	}
	
	@Override
	public void stopRecording(){
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
	}

	@Override
	public void release() {
		if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
	}
	

}
