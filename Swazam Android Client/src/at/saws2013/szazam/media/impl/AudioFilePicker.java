package at.saws2013.szazam.media.impl;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import at.saws2013.szazam.media.IAudioFilePicker;

/**
 * Class which launches a picker intent to pick an audio
 * file from the device
 * 
 * @author René
 *
 */
public class AudioFilePicker extends ContextWrapper implements IAudioFilePicker{
	
	public final static int AUDIO_FILE_PICKER_REQUEST = 2013;

	public AudioFilePicker(Context base) {
		super(base);
	}

	@Override
	public void startPickIntent(Activity activity){
		Intent intent = new Intent();
		intent.setType("audio/*");
	    intent.setAction(Intent.ACTION_GET_CONTENT);
	    activity.startActivityForResult(Intent.createChooser(intent,"Select Audio "), AudioFilePicker.AUDIO_FILE_PICKER_REQUEST);
	}

	@Override
	public String getAudioTrackPath(Intent data) {
		if (null != data){
			Uri audioFile= data.getData();
			return getRealPathFromURI(audioFile);
		} else {
			return null;
		}
	}
	
	private String getRealPathFromURI(Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Audio.Media.DATA };
			cursor = getContentResolver().query(contentUri, proj, null,
					null, null);
			String[] columns = cursor.getColumnNames();
			for (int i = 0; i < columns.length ; i++){
				Log.d("cursor", columns[i]);
			}
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

}
