package dev.thec0dec8ter.hablo.adapter.message;

import android.net.Uri;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

import dev.thec0dec8ter.hablo.R;
import dev.thec0dec8ter.hablo.model.Media;

import static dev.thec0dec8ter.hablo.utility.MediaUtility.timerConversion;

public class AudioViewHolder extends RecyclerView.ViewHolder {
    private final ImageView controlBtn;
    private final SeekBar seekBar;
    private final TextView currentTime;

    private CountDownTimer countDownTimer;
    private ExoPlayer exoPlayer;

    public AudioViewHolder(@NonNull View itemView) {
        super(itemView);
        controlBtn = itemView.findViewById(R.id.control_btn);
        seekBar = itemView.findViewById(R.id.seekbar);
        currentTime = itemView.findViewById(R.id.current_time);


        controlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controlBtn.getDrawable() == v.getContext().getDrawable(R.drawable.ic_play)){
                        playAudio();
                    controlBtn.setImageResource(R.drawable.ic_pause);
                }else {
                    pauseAudio();
                    controlBtn.setImageResource(R.drawable.ic_play);
                }

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentTime.setText(timerConversion(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void bind(Media audio){
        seekBar.setTag(audio);
        if(audio.getSender().equals("isMe")){
            itemView.setTag(true);
        }else {
            itemView.setTag(false);
        }

    }

    private void playAudio() {
        Media audio = (Media) seekBar.getTag();
        exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(audio.getUri())));
        exoPlayer.prepare();
        exoPlayer.play();
    }

    private void pauseAudio() {
        exoPlayer.pause();
    }
}
