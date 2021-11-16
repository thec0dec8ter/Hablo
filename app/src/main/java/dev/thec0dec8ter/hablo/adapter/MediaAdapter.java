package dev.thec0dec8ter.hablo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dev.thec0dec8ter.hablo.MediaActivity;
import dev.thec0dec8ter.hablo.R;
import dev.thec0dec8ter.hablo.model.Media;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private final Context context;
    private final int layoutResId;
    private final ArrayList<Media> mediaList = new ArrayList<>();


    public MediaAdapter(Context context, int layoutResId){
        this.context = context;
        this.layoutResId = layoutResId;
    }



    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        holder.bind(mediaList.get(position));
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);

            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MediaActivity.class);
                    intent.putExtra("media", (Media)thumbnail.getTag());
                    context.startActivity(intent);
                }
            });
        }

        public void bind(Media media){
            thumbnail.setTag(media);
            Picasso.get()
                    .load(media.getUri())
                    .fit()
                    .into(thumbnail);

        }
    }

    public void addMedia(Media media){
        mediaList.add(media);
        notifyDataSetChanged();
    }

    public void clearMedia(){
        mediaList.clear();
        notifyDataSetChanged();
    }
}
