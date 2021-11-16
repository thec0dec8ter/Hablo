package dev.thec0dec8ter.hablo.utility;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MediaUtility {
    private final Context context;
    private MediaRecorder mediaRecorder;

    private AudioRecordListener audioRecordListener;

    public MediaUtility(Context context){
        this.context = context;
    }

    public String startAudioRecording(){
        // Verify that the device has a mic first
        PackageManager pManager = context.getPackageManager();
        if (pManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            // Set the file location for the audio
            String mFileName = context.getFilesDir().getAbsolutePath();
            mFileName += "/audiorecordtest.m4a";
            // Create the recorder
            // Set the audio format and encoder
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setAudioEncodingBitRate(128000);
            mediaRecorder.setAudioSamplingRate(44100);
            // Setup the output location
            mediaRecorder.setOutputFile(mFileName);
            // Start the recording
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                audioRecordListener.onStart(mFileName);
                return mFileName;
            } catch (IOException e) {
                Log.e("AudioRecord: ", "prepare() failed");
            }
        } else {
            // no mic on device
            Toast.makeText(context, "This device doesn't have a mic!", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    public void stopAudioRecording(){
        // Stop the recording of the audio
        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            audioRecordListener.onStop();
        } catch(RuntimeException e) {
            //TODO: you must delete the output file when the recorder stop failed.
        } finally {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public interface AudioRecordListener{
        void onStart(String filePath);

        void onStop();
    }

    public void setAudioRecordListener(AudioRecordListener audioRecordListener) {
        this.audioRecordListener = audioRecordListener;
    }

    public static String timerConversion(long value) {
        String audioTime;
        int dur = (int) value;
        int hrs = (dur / (3600 * 1000));
        int mns = (dur / (60 * 1000)) % (60 * 1000);
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            audioTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            audioTime = String.format("%02d:%02d", mns, scs);
        }
        return audioTime;
    }

    private void saveAudioFile(){
        File audioFile = context.getFilesDir();

        // Setup values for the media file
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "audio file");
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp4");
        values.put(MediaStore.Audio.Media.DATA, audioFile.getAbsolutePath());
        ContentResolver contentResolver = context.getContentResolver();
        // Construct uris
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);
        // Trigger broadcast to add
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        Toast.makeText(context, "Added File " + newUri, Toast.LENGTH_LONG).show();
    }
}
