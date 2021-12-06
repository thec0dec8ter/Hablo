package dev.thec0dec8ter.hablo.webRTC;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioTrack;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.RtpReceiver;
import org.webrtc.VideoTrack;

public class MyPeerConnectionObserver implements PeerConnection.Observer {
    private final String TAG = "";

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {
        Log.d(TAG, "onSignalingChange: ");
    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
        Log.d(TAG, "onIceConnectionChange: ");
    }

    @Override
    public void onIceConnectionReceivingChange(boolean b) {
        Log.d(TAG, "onIceConnectionReceivingChange: ");

    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
        Log.d(TAG, "onIceGatheringChange: ");
    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        Log.d(TAG, "onIceCandidate: ");
        JSONObject message = new JSONObject();

        try {
            message.put("type", "candidate");
            message.put("label", iceCandidate.sdpMLineIndex);
            message.put("id", iceCandidate.sdpMid);
            message.put("candidate", iceCandidate.sdp);

            Log.d(TAG, "onIceCandidate: sending candidate " + message);
            sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
        Log.d(TAG, "onIceCandidatesRemoved: ");
    }

    @Override
    public void onAddStream(MediaStream mediaStream) {
        Log.d(TAG, "onAddStream: " + mediaStream.videoTracks.size());
        VideoTrack remoteVideoTrack = mediaStream.videoTracks.get(0);
        AudioTrack remoteAudioTrack = mediaStream.audioTracks.get(0);
        remoteAudioTrack.setEnabled(true);
        remoteVideoTrack.setEnabled(true);
        remoteVideoTrack.addRenderer(new VideoRenderer(binding.surfaceView2));
    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {
        Log.d(TAG, "onRemoveStream: ");
    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {
        Log.d(TAG, "onDataChannel: ");
    }

    @Override
    public void onRenegotiationNeeded() {
        Log.d(TAG, "onRenegotiationNeeded: ");
    }

    @Override
    public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {

    }
}
