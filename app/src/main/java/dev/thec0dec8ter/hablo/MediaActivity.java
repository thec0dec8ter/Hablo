package dev.thec0dec8ter.hablo;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import dev.thec0dec8ter.hablo.model.Media;

public class MediaActivity extends AppCompatActivity {

    private ImageView btnPlay;
    private ImageView imageView;
    private PlayerView videoView;

    private ExoPlayer exoPlayer;

    private String mp4Url = "https://html5demos.com/assets/dizzy.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        Media media = null;
        if(getIntent().getSerializableExtra("media") != null){
            media = (Media) getIntent().getSerializableExtra("media");
        }else {
            finish();
        }

        exoPlayer = new ExoPlayer.Builder(this).build();

        btnPlay = findViewById(R.id.btn_play);
        imageView = findViewById(R.id.image_view);
        videoView = findViewById(R.id.video_view);

        if(media.getType().equals("image")){
            videoView.setVisibility(View.GONE);
            btnPlay.setVisibility(View.GONE);
            Picasso.get()
                    .load(media.getUri())
                    .into(imageView);

        }else if(media.getType().equals("video")){
            videoView.setPlayer(exoPlayer);
            exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(mp4Url)));
            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(true);
        }

    }
}