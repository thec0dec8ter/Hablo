package dev.thec0dec8ter.hablo.webRTC;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class CallRoom implements Serializable {
    private String name = "";
    private String message = "";
    private ArrayList<Peer> peers = new ArrayList<>();
    private int max = 0;

    public CallRoom(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Peer> getPeers() {
        return peers;
    }

    public void addPeer(Peer peer){
        getPeers().add(peer);
    }

    public void setPeers(ArrayList<Peer> peers) {
        this.peers = peers;
    }
}
