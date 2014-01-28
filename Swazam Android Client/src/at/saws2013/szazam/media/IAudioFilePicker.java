package at.saws2013.szazam.media;

import android.app.Activity;
import android.content.Intent;

public interface IAudioFilePicker {
	public void startPickIntent(Activity activity);
	public String getAudioTrackPath(Intent data);
}
