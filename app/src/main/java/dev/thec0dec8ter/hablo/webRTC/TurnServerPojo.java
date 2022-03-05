package dev.thec0dec8ter.hablo.webRTC;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.webrtc.PeerConnection;

import java.util.List;

public class TurnServerPojo {

    @SerializedName("s")
    @Expose
    public Integer s;
    @SerializedName("p")
    @Expose
    public String p;
    @SerializedName("e")
    @Expose
    public Object e;
    @SerializedName("v")
    @Expose
    public IceServerList iceServerList;

    public class IceServerList {

        @SerializedName("iceServers")
        @Expose
        public List<IceServer> iceServers = null;

    }

}