# Peer Client

## Description

The Peer Client is a command-line application, which recursively scans a folder
with music files (only mp3 and m4a) and creates fingerprints for every song.

It also connects to a given supernode (here: the server), to listen for work. If
the node doesn't find a match in its library, it notifies its neighbors to pass
on the task.


## Used Frameworks

- TomP2P: P2P networking
- jAudioTagger: MP3 and M4A metadata access
- Apache Commons HTTPClient: HTTP requests to server


## CLOC statistics

32 text files.
30 unique files.
20 files ignored.

http://cloc.sourceforge.net v 1.60  T=0.38 s (39.6 files/s, 1608.8 lines/s)
-------------------------------------------------------------------------------
Language                     files          blank        comment           code
-------------------------------------------------------------------------------
Java                             9             80             19            341
Maven                            3              9              0             90
XML                              3              0              0             70
-------------------------------------------------------------------------------
SUM:                            15             89             19            501
-------------------------------------------------------------------------------


## Run with Maven

mvn clean compile exec:java -Dlibrary=<PATH TO MUSIC> -Dusername=<USERNAME> -Dsupernode=<SERVER IP>


# Mapping between models and implementation

All classes are named as stated in the model file (Or prependet with I if they have an interface).

NetworkConnection   -> at.ac.tuwien.swa13.swazam.NetworkConnection
Song                -> at.ac.tuwien.swa13.swazam.library.ISong (Implemenation: at.ac.tuwien.swa13.swazam.library.impl.Song)
LibraryManager      -> at.ac.tuwien.swa13.swazam.library.ILibraryManager (Implemenation: at.ac.tuwien.swa13.swazam.library.impl.LibraryManager)
PeerConnector       -> at.ac.tuwien.swa13.swazam.p2p.PeerConnector
FingerprintCreator  -> at.ac.tuwien.swa13.swazam.fingerprint.IFingerprintCreator (Implementation: at.ac.tuwien.swa13.swazam.fingerprint.FingerprintCreator)

The at.ac.tuwien.swa13.swazam.Peer class, only contains the main method and has no corresponding entity in the model file.