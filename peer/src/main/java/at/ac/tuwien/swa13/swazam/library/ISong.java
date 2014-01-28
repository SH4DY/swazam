package at.ac.tuwien.swa13.swazam.library;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public interface ISong
{
  public void     createFingerprint();
  public boolean  matchFingerprint(Fingerprint fingerprint);

  public String getAlbum();
  public String getArtist();
  public String getTitle();
}
