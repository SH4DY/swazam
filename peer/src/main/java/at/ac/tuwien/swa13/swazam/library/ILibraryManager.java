package at.ac.tuwien.swa13.swazam.library;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public interface ILibraryManager
{

  public void updateLibrary();

  public ISong findSong(Fingerprint fingerprint);

}
