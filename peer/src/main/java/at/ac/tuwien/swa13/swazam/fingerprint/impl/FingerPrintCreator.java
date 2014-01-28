package at.ac.tuwien.swa13.swazam.fingerprint.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;
import ac.at.tuwien.infosys.swa.audio.FingerprintSystem;
import ac.at.tuwien.infosys.swa.audio.SubFingerprint;

import at.ac.tuwien.swa13.swazam.fingerprint.IFingerPrintCreator;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

public class FingerPrintCreator implements IFingerPrintCreator
{
  private static final int SAMPLING_RATE = 8000;

  private FingerprintSystem fingerPrintSystem;
  private boolean isCancelled;

  public FingerPrintCreator()
  {
    this.fingerPrintSystem = new FingerprintSystem(SAMPLING_RATE);
  }

  private ByteArrayOutputStream readAudioTrack(FileInputStream fileInputStream) throws IOException
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    final byte[] buffer = new byte[1024];
    int read = fileInputStream.read(buffer);

    while (-1 < read && !isCancelled) {
      out.write(buffer, 0, read);
      read = fileInputStream.read(buffer);
    }

    return out;
  }

  public static Fingerprint fromString(String fingerprintString)
  {
      Collection<SubFingerprint> subFingerprints = new LinkedList<SubFingerprint>();
      
      Scanner scanner = new Scanner(fingerprintString);
      while(scanner.hasNext()) {
          String line = scanner.next();
          int value = Integer.parseInt(line, 2);
          SubFingerprint sub = new SubFingerprint(value);
          subFingerprints.add(sub);
      }
      
      float shift = (float)1/32 / (float)FingerPrintCreator.SAMPLING_RATE;
      return new Fingerprint(0, shift, subFingerprints);
  }
  
  @Override
  public Fingerprint createFingerprintFromFilePath(String filePath)
  {
    Fingerprint fingerprint = null;
    FileInputStream input;

    try {
      File audioTrack = new File(filePath);
      input = new FileInputStream(audioTrack);
      final ByteArrayOutputStream out = readAudioTrack(input);
      input.close();

      try {
        fingerprint = fingerPrintSystem.fingerprint(out.toByteArray());
      } catch(IllegalArgumentException e){
        System.out.println(e.getMessage());
      } catch (OutOfMemoryError e) {
        System.out.println(e.getMessage());
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return fingerprint;
  }

}
