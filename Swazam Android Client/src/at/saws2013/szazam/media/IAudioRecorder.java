package at.saws2013.szazam.media;

public interface IAudioRecorder {
	public void startRecording(String recordPath);
	public void stopRecording();
	public String getRecortPath();
	public void release();
}
