package at.ac.tuwien.swa13.swazam.fingerprint;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public interface IFingerPrintCreator
{

  public Fingerprint createFingerprintFromFilePath(String filePath);

}
