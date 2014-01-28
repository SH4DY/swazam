package at.ac.tuwien.swa13.swazam.library.impl;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

import at.ac.tuwien.swa13.swazam.fingerprint.impl.FingerPrintCreator;
import at.ac.tuwien.swa13.swazam.library.ISong;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public final class Song implements ISong
{
  private final String filePath;
  private Fingerprint fingerprint;
  private Tag metadata;

  public Song(String filePath)
  {
      // Read metadata
      this.filePath = filePath;
      
      this.extractMetadata();
      this.createFingerprint();
  }
  
  private void extractMetadata()
  {
      try {
          File songFile = new File(filePath);
          AudioFile f = AudioFileIO.read(songFile);
          metadata = f.getTag();
      } catch (CannotReadException ex) {
          Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
          Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
      } catch (TagException ex) {
          Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
      } catch (ReadOnlyFileException ex) {
          Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
      } catch (InvalidAudioFrameException ex) {
          Logger.getLogger(Song.class.getName()).log(Level.SEVERE, null, ex);
      }
  }

  @Override
  public void createFingerprint()
  {
    FingerPrintCreator fpc = new FingerPrintCreator();
    this.fingerprint = fpc.createFingerprintFromFilePath(this.filePath);
  }

  @Override
  public boolean matchFingerprint(Fingerprint fingerprint)
  {
    if (this.fingerprint == null)
      return false;

    return this.fingerprint.match(fingerprint) != -1;
  }

  @Override
  public String toString()
  {
    return this.getArtist() + " - " + this.getAlbum() + " - " + this.getTitle();
  }
  
  @Override
  public String getAlbum()
  {
      return this.metadata.getFirst(FieldKey.ALBUM);
  }
  
  @Override
  public String getArtist()
  {
      return this.metadata.getFirst(FieldKey.ALBUM);
  }
  
  @Override
  public String getTitle()
  {
      return this.metadata.getFirst(FieldKey.TITLE);
  }
}
