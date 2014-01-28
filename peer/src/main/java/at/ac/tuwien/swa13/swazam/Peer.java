package at.ac.tuwien.swa13.swazam;

import at.ac.tuwien.swa13.swazam.library.ILibraryManager;
import at.ac.tuwien.swa13.swazam.library.impl.LibraryManager;
import at.ac.tuwien.swa13.swazam.p2p.PeerConnector;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class Peer
{

  public static void main(String [] args) throws Exception
  {
    if (args.length < 3) {
      System.out.println("Call with arguments <path to music> <username> <supernode address>");
      return;
    }

    // Make the audiotagger silent
    Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);

    String library = args[0];
    String username = args[1];
    String supernode = args[2];

    ILibraryManager manager = new LibraryManager(library);
    PeerConnector peerConnector = new PeerConnector(manager, username);
      try {
            peerConnector.listen(supernode);
      } catch (IOException ex) {
          Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
      }
  }
}