package dev.thec0dec8ter.hablo.webRTC;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

public class SignallingClient {
    private static SignallingClient instance;

    private DatabaseReference callArenaRef;
    private DatabaseReference callRoomRef;
    private CallRoom myCallRoom = null;

    public boolean isChannelReady = false;
    public boolean isInitiator = false;
    public boolean isStarted = false;
    private SignalingInterface callback;

    private Peer me;


    public static SignallingClient getInstance() {
        if (instance == null) {
            instance = new SignallingClient();
        }
        if (instance.myCallRoom == null) {
            //set the room name here
            instance.myCallRoom = new CallRoom("phone_number");
        }
        return instance;
    }

    public void initialize(SignalingInterface signalingInterface) {
        this.callback = signalingInterface;
        callArenaRef = FirebaseDatabase.getInstance().getReference("call_arena");

        if (!myCallRoom.getName().isEmpty()) {
            me = new Peer("tolu");
            myCallRoom.getPeers().add(me);
            callArenaRef.child("phone_number").setValue(myCallRoom);
            callRoomRef = callArenaRef.child("phone_number");
        }

        callArenaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                CallRoom room = snapshot.getValue(CallRoom.class);
                if(room.getName().equals(myCallRoom.getName())){
                    isInitiator = true;
                    callback.onCreatedRoom();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                CallRoom room = snapshot.getValue(CallRoom.class);
                if(room.getName().equalsIgnoreCase(myCallRoom.getName())){
                    callback.onRemoteHangUp(room.getName());
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        callRoomRef.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() instanceof String) {
                    String data = snapshot.getValue(String.class);
                    if (data.equalsIgnoreCase("got user media")) {
                        callback.onTryToStart();
                    }
                    if (data.equalsIgnoreCase("bye")) {
                        callback.onRemoteHangUp(data);
                    }
                } else if (snapshot.getValue() instanceof JSONObject) {
                    try {

                        JSONObject data = snapshot.getValue(JSONObject.class);
                        String type = data.getString("type");
                        if (type.equalsIgnoreCase("offer")) {
                            callback.onOfferReceived(data);
                        } else if (type.equalsIgnoreCase("answer") && isStarted) {
                            callback.onAnswerReceived(data);
                        } else if (type.equalsIgnoreCase("candidate") && isStarted) {
                            callback.onIceCandidateReceived(data);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        callRoomRef.child("peers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Peer peer = snapshot.getValue(Peer.class);
                isChannelReady = true;
                if(peer.getName().equals(me.getName())){
                    callback.onJoinedRoom();
                }else{
                    callback.onNewPeerJoined();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void sendMessage(String message) {
        callRoomRef.child("message").setValue(message);
    }

    public void sendSessionDescription(SessionDescription sdp) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("type", sdp.type.canonicalForm());
            obj.put("sdp", sdp.description);
            callRoomRef.child("message").setValue(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendIceCandidate(IceCandidate iceCandidate) {
        try {
            JSONObject object = new JSONObject();
            object.put("type", "candidate");
            object.put("label", iceCandidate.sdpMLineIndex);
            object.put("id", iceCandidate.sdpMid);
            object.put("candidate", iceCandidate.sdp);
            callRoomRef.child("message").setValue(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeCallRoom() {
        callRoomRef.removeValue();
    }

    public interface SignalingInterface {
        void onRemoteHangUp(String msg);

        void onOfferReceived(JSONObject data);

        void onAnswerReceived(JSONObject data);

        void onIceCandidateReceived(JSONObject data);

        void onTryToStart();

        void onCreatedRoom();

        void onJoinedRoom();

        void onNewPeerJoined();
    }
}
