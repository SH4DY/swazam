package at.ac.tuwien.swa13.swazam.library.impl;

import java.io.File;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

import at.ac.tuwien.swa13.swazam.library.ILibraryManager;
import at.ac.tuwien.swa13.swazam.library.ISong;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LibraryManager implements ILibraryManager
{
  private final String libraryPath;
  private final Set<ISong> songs;

  public LibraryManager(String libraryPath)
  {
    this.songs = new HashSet<ISong>();
    this.libraryPath = libraryPath;

    this.updateLibrary();
  }

  @Override
  public void updateLibrary()
  {
    String[] extensions = new String[] { "mp3", "m4a" };
    File library = new File(this.libraryPath);
    Iterator<File> files = FileUtils.iterateFiles(library, extensions, true);

    // Remove songs from library
    this.songs.clear();
    
    Logger.getLogger(LibraryManager.class.getName()).log(Level.INFO, "Starting to build library...");
    
    // Create song in library
    while(files.hasNext()) {
        new Thread(new SongCreator(files.next().getPath())).start();
    }
  }

  @Override
  public ISong findSong(Fingerprint fingerprint)
  {
    for (ISong song : this.songs) {
      if (song.matchFingerprint(fingerprint))
        return song;
    }

    return null;
  }
  
  private class SongCreator implements Runnable
  {
      private final String songPath;
      
      public SongCreator(String songPath)
      {
          this.songPath = songPath;
      }
      
      @Override
      public void run()
      {
        ISong song = new Song(this.songPath);
        songs.add(song);
        Logger.getLogger(LibraryManager.class.getName()).log(Level.INFO, "Added song {0} to library", song.toString());
      }
  }
}
