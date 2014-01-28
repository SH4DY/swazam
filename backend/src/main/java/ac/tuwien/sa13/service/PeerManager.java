package ac.tuwien.sa13.service;

import java.io.IOException;
import java.util.Random;

import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import ac.tuwien.sa13.entity.Request;

public class PeerManager {

	@Autowired
	IRequestService requestService;

	private final static int PORT = 4000;

	private static PeerManager instance = null;

	private Peer p;

	private PeerManager() throws IOException {
		p = new PeerMaker(new Number160(new Random())).setPorts(PORT)
				.makeAndListen();
	}

	public static PeerManager getInstance() throws IOException {
		if (instance == null) {
			instance = new PeerManager();
		}

		return instance;
	}

	public void issueRequest(final Request r) throws IOException {
		String[] temp = { "" + r.getId(), r.getFingerprint() };
		FutureDHT fdht = p.send(new Number160(new Random())).setObject(temp)
				.start();

		fdht.addListener(new BaseFutureAdapter<FutureDHT>() {

			@Override
			public void operationComplete(FutureDHT f) throws Exception {
				if (f.isFailed()) {
					Logger.getLogger(PeerManager.class.getName()).log(
							Level.WARN,
							"Request " + r.getId() + "from User " + r.getUser().getName()
									+ " failed.");
				} else {
					Logger.getLogger(PeerManager.class.getName()).log(
							Level.INFO,
							"Request " + r.getId() + r.getId() + "from User "
									+ r.getUser().getName() + " successfully issued.");
				}

			}

		});
	}

	public void shutdown() {
		p.shutdown();
		instance = null;
	}
}
