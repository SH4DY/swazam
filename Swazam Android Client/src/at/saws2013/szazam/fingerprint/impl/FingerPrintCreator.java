package at.saws2013.szazam.fingerprint.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;
import ac.at.tuwien.infosys.swa.audio.FingerprintSystem;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import at.saws2013.szazam.fingerprint.IFingerPrintSystem;

/**
 * Class to create a fingerprint either using an FileInputStream
 * or the path to an audio file
 * 
 * @author René
 *
 */
public class FingerPrintCreator extends ContextWrapper implements IFingerPrintSystem{

	private static final String LOG_TAG = "FingerPrintCreator";
	private static final int SAMPLING_RATE = 8000;
	
	private FingerprintSystem fingerPrintSystem;
	private boolean isCancelled;
	
	public void cancel(){
		this.isCancelled = true;
	}

	public FingerPrintCreator(Context base) {
		super(base);
		fingerPrintSystem = new FingerprintSystem(SAMPLING_RATE);
	}
	
	private ByteArrayOutputStream readAudioTrack(FileInputStream fileInputStream) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		final byte[] buffer = new byte[1024];
        int read = fileInputStream.read(buffer);
        while (-1 < read && !isCancelled) {
            out.write(buffer, 0, read);
            read = fileInputStream.read(buffer);
        }
        try {
            fileInputStream.close();
		} catch (Exception e) {
		}
		return out;
	}

	@Override
	public Fingerprint createFingerprintFromFilePath(String filePath){
		this.isCancelled = false;
		Fingerprint fingerprint = null;
		FileInputStream input;
		try {
			File audioTrack = new File(filePath);
			input = new FileInputStream(audioTrack);
			final ByteArrayOutputStream out = readAudioTrack(input);	        
	        input.close();
	        try{
	        	fingerprint = fingerPrintSystem.fingerprint(out.toByteArray());
	        } catch(IllegalArgumentException e){
	        	Log.d(LOG_TAG, e.getMessage());
	        } catch (OutOfMemoryError e) {
	        	Log.d(LOG_TAG, e.getMessage());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fingerprint;
	}

}
