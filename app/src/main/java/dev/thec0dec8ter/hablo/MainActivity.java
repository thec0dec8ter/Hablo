package dev.thec0dec8ter.hablo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dev.thec0dec8ter.hablo.adapter.message.MessageAdapter;
import dev.thec0dec8ter.hablo.model.Document;
import dev.thec0dec8ter.hablo.model.Media;
import dev.thec0dec8ter.hablo.model.Message;
import dev.thec0dec8ter.hablo.utility.MediaUtility;

import static dev.thec0dec8ter.hablo.utility.MediaUtility.timerConversion;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView emojiPicker;
    private ImageView btnAttach;
    private ImageView btnCamera;
    private TextView timerText;
    private CardView btnVoiceNote;

    private RecyclerView messageRecycler;

    private MessageAdapter messageAdapter;
    private MediaUtility mediaUtility;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar  = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        messageRecycler = findViewById(R.id.message_recycler);
        emojiPicker = findViewById(R.id.btn_emoji_picker);
        btnAttach = findViewById(R.id.btn_attach);
        btnCamera = findViewById(R.id.btn_camera);
        timerText = findViewById(R.id.timer);
        btnVoiceNote = findViewById(R.id.btn_voice_note);

        messageAdapter = new MessageAdapter(this);
        mediaUtility = new MediaUtility(this);

        mediaUtility.setAudioRecordListener(new MediaUtility.AudioRecordListener() {
            Media voiceNote;
            Handler handler;
            Runnable handlerRunnable;
            long startTime;
            @Override
            public void onStart(String filePath) {
                voiceNote = new Media();
                handler = new Handler();
                startTime = SystemClock.uptimeMillis();
                handlerRunnable = new Runnable() {
                    @Override
                    public void run() {

                        try {
                            long timeElapsed = SystemClock.uptimeMillis() - startTime;
                            timerText.setText(timerConversion(timeElapsed));
                            handler.postDelayed(this, 0);
                        } catch (IllegalStateException ed){
                            ed.printStackTrace();
                        }
                    }
                };
                handler.postDelayed(handlerRunnable, 0);
                voiceNote.setUri(Uri.parse(filePath).toString());
                voiceNote.setType("audio");
            }

            @Override
            public void onStop() {
                handler.removeCallbacks(handlerRunnable);
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(getApplicationContext(), Uri.parse(voiceNote.getUri()));
                String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                voiceNote.setDuration(Long.parseLong(durationStr));
                messageAdapter.addMedia(voiceNote);
            }
        });

        btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContentPicker(v.getContext());
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
            }
        });

        btnVoiceNote.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mediaUtility.startAudioRecording();
                        return true;
                    case MotionEvent.ACTION_UP:
                        mediaUtility.stopAudioRecording();
                        return true;
                    default:
                        return false;
                }
            }
        });

        messageRecycler.setAdapter(messageAdapter);
        setDummy();

    }

    private void showContentPicker(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.content_picker, null, false);
        FloatingActionButton btnDocument = view.findViewById(R.id.btn_document);
        FloatingActionButton btnAudio = view.findViewById(R.id.btn_audio);
        FloatingActionButton btnCamera = view.findViewById(R.id.btn_camera);
        FloatingActionButton btnGallery = view.findViewById(R.id.btn_gallery);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_call:
                //TODO: add call function
            case R.id.menu_video_call:
                //TODO: add video call function
            case R.id.show_participants:
                startActivity(new Intent(this, InfoActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setDummy(){
        Message message = new Message();
        Document document = new Document();
        Media audio = new Media();
        Media image = new Media();
        Media video = new Media();
        audio.setType("audio");
        image.setType("image");
        video.setType("video");
        message.setSender("isMe");
        messageAdapter.addMessage(message);
        messageAdapter.addDocument(document);
        messageAdapter.addMedia(audio);
        messageAdapter.addMedia(image);
        messageAdapter.addMedia(video);

    }
}