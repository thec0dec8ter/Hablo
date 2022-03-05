package dev.thec0dec8ter.hablo.webRTC;

import org.webrtc.SessionDescription;

import java.io.Serializable;

public class Peer implements Serializable {
    private String name;
    private SessionDescription sdp;

    public Peer(String name){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


