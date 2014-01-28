package at.ac.tuwien.swa13.swazam.p2p;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;
import at.ac.tuwien.swa13.swazam.NetworkConnection;
import at.ac.tuwien.swa13.swazam.fingerprint.impl.FingerPrintCreator;
import at.ac.tuwien.swa13.swazam.library.ILibraryManager;
import at.ac.tuwien.swa13.swazam.library.ISong;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Random;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

public class PeerConnector {
    private final ILibraryManager libraryManager;
    private final String username;
    private String serverAddress;
    
    private LinkedList<String> previousTasks;

    private Peer peer;

    private static final int serverPort = 4000;

    public PeerConnector(ILibraryManager library, String username)
    {
        this.libraryManager = library;
        this.username = username;

        this.previousTasks = new LinkedList<String>();
        
        System.out.println("Created PeerConnector");
    }

    public void listen(String ipAddress) throws Exception
    {
        this.serverAddress = ipAddress;

        // TODO: Listen to Master Peer for new data and do stuff
        this.peer = new PeerMaker(new Number160(new Random(43L))).makeAndListen();
        PeerAddress supernode = new PeerAddress(Number160.ZERO, InetAddress.getByName(ipAddress), serverPort, serverPort);
        FutureDiscover fd = peer.discover().setPeerAddress(supernode).start();
        fd.awaitUninterruptibly();

        if (fd.isSuccess()) {
            //FutureBootstrap fb = peer.bootstrap().setPeerAddress(supernode).start();
            //fb.awaitUninterruptibly();

            System.out.println("Bootstrap complete (" + peer.getPeerAddress() + "). Waiting for work...");
        } else {
            System.out.println("Discover failed: " + fd.getFailedReason());

            return;
        }

        // Setup request handler
        peer.setObjectDataReply(new ObjectDataReply() {
            @Override
            public Object reply(PeerAddress sender, Object request) throws Exception
            {
                System.out.println("Received task from network. Looking for song...");

                String[] rData = (String[])request;
                
                if (previousTasks.contains(rData[0])) {
                    System.out.println("Had this request before. Dropping it...");
                    return null;
                } else {
                    previousTasks.add(rData[0]);
                    // Trim it
                    if (previousTasks.size() > 100)
                        previousTasks.remove();
                }
                
                findSong(rData[0], rData[1]);

                // We don't care about the return
                return null;
            }
        });
    }

    private void notifyNeighbors(String taskId, String songFragment)
    {
        System.out.println("Song not found. notifing neighbors...");

        // Ask known peers to work on this task
        String[] data = { taskId, songFragment };
        peer.send(Number160.createHash(taskId)).setObject(data).start();
    }

    private void findSong(String taskId, String songFragmentString)
    {
        // Convert fingerprint to usable object
        Fingerprint songFragment = FingerPrintCreator.fromString(songFragmentString);

        ISong foundSong = this.libraryManager.findSong(songFragment);

        System.out.println(foundSong);
        
        if (foundSong == null) {
            // Notify neighboring peers to start looking for the song
            this.notifyNeighbors(taskId, songFragmentString);
        } else {
            // Notify server via REST interface that we have found the song
            NetworkConnection con = new NetworkConnection(this.serverAddress);
            con.makeRequest(taskId, this.username, foundSong);
        }
    }
}
