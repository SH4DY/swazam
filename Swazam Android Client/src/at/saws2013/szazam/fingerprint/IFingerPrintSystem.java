package at.saws2013.szazam.fingerprint;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;

public interface IFingerPrintSystem {
	public Fingerprint createFingerprintFromFilePath(String filePath);
}
