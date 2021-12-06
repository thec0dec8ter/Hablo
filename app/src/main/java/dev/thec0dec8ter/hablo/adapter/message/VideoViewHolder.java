package dev.thec0dec8ter.hablo.adapter.message;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.thec0dec8ter.hablo.MediaActivity;
import dev.thec0dec8ter.hablo.R;
import dev.thec0dec8ter.hablo.model.Media;

public class VideoViewHolder extends RecyclerView.ViewHolder {
    ImageView thumbnail;

    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        thumbnail = itemView.findViewById(R.id.thumbnail);

        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MediaActivity.class);
                intent.putExtra("media", (Media)thumbnail.getTag());
            }
        });
    }

    public void bind(Media video){
        thumbnail.setTag(video);
        if(video.getSender().equals("isMe")){
            itemView.setTag(true);
        }else {
            itemView.setTag(false);
        }

    }
}
