GIT checkout:
git clone https://se.techfak.de/git/g-se-tut-ws-2019.project-template.git

## Welcome to VoteRadio

Latest Version: https://gitlab.ub.uni-bielefeld.de/till.johanndeiter/voteradio/-/jobs/17044/artifacts/download



## Motivation

Last semester i work as a tutor at my university. The students had to program with java and and libraries (VlcJ, Jackson, NanotHttp, JavaFx,...) a desktop app similar to festify https://festify.rocks/. For prepreation of my tutorial i made the project.

During the development process i learned many new things about programming and software development beyond how to explain newbies the basics and motivate others to improve their coding skills. Not only how to desing a rest api, but how to set up a ci pipline with my own docker container also use patterns like strategy, abstract factory or template in real projects.

## User Manual

**VoteRadio requires a correct installation of VLC independently if you use it as server, client or offline.**
You can download the supported version here: https://www.videolan.org/.

### Terminal Mode / Randomplay

If you just want to enjoy music and don't care about order, voting and graphical user interfaces you can start the VoteRadio without any arguments except an optional path of .mp3 files which is used for playlist creation. If their is no path specified the current folder will be used as default path. Please remember that VoteRadio requires a folder with at least one mp3 file. Otherwise it can not start. With the commands _song_ _playlist_ and _exit_ you get informations and can exit.

./gseRadio optionalFolder

### JukeBox / Offline voting

To use the voting mode with GUI you add _-g_ or _--gui_ argument. Likewise in terminal mode you can add a optional folder. If you want so start or stop music you can use the button. Also you can increase or decrease music volumen in by using the control panel. Also their is the option to set the minimal plays before a song can replayed.


./gseRadio musicFolder -g optionalFolder
./gseRadio musicFolder --gui optionalFolder

### Client

Interface in client mode is mostly same as jukebox. But instead of the controll panel with start, stop and pause you can connect or disconnect via network address and port to server. After handshake music music is received. Another feature is to upload songs to the server. Please notice that a upload will be written to the music folder of server. One Client is identified by his network address and can vote only once for a song.

./gseRadio --client


### Server

The server mode starts deamon rest server. You can specify port of rest server with _--restPort_ argument otherwise 8080 will be used as default. Likewise you can specify location of music rtp Stream with _--streamPort_ and _streamAddress_. For network streaming a multicast adress is required. If both argument aren't available music will be played local on the server. Like in jukebox mode you can specify minimal replays with _--replays_ otherwise 1 is used as default.
Additionally to commands form terminal mode you get with port and adress informations about server location.

./gseRadio --server --streamPort=5050 --streamAddress=239.255.0.1 --restPort=8080 --replays=5 music


## Sources

Thank to everybody who upload their work in the internet and allow other to resuse their work!

CD Icon: Jrdn88 https://commons.wikimedia.org/wiki/File:CD_Audio_icon.png
Play/Stop/Pause Button https://pixabay.com/de/vectors/tasten-anschlag-datensatz-anhalten-304219/

Music: Head Run - 	7OOP3D http://ccmixter.org/files/texasradiofish/60392
       Night Owl - Broke For Free https://freemusicarchive.org/music/Broke_For_Free/Directionless_EP/Broke_For_Free_-_Directionless_EP_-_01_Night_Owl
       Enthusiast - Tours https://freemusicarchive.org/music/Tours/Enthusiast/Tours_-_Enthusiast
       Leaving Babylon - Zep Hurme http://ccmixter.org/files/zep_hurme/60052
       One True Connection - Whitewolf http://ccmixter.org/files/Whitewolf225/59987
       Sirius Crystal - Speck - http://ccmixter.org/files/speck/60126
       Streets of my Hometown - Scomber http://ccmixter.org/files/scomber/60268