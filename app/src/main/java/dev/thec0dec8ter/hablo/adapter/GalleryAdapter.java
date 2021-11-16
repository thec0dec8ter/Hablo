package dev.thec0dec8ter.hablo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import dev.thec0dec8ter.hablo.R;
import dev.thec0dec8ter.hablo.model.Media;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private final HashMap<String, ArrayList<Media>> mediaList = new HashMap<>();

    private Context context;

    public GalleryAdapter(Context context){
        this.context = context;
    }

    @NonNull

    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        holder.bind((String) mediaList.keySet().toArray()[position]);

    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder{
        TextView bucketName;
        ImageView thumbnail;

        public GalleryViewHolder(@NonNull  View itemView) {
            super(itemView);
            bucketName = itemView.findViewById(R.id.bucket_name);
            thumbnail = itemView.findViewById(R.id.img_thumbnail);
        }

        public void bind(String key){
//            Media media = mediaList.get(key).get(0);
//            ContentResolver contentResolver = context.getContentResolver();
//            Bitmap bitmap = null;
//            if(media.getType().equals("image")){
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, media.getUri());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }else {
//                bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, media.getId(), MediaStore.Video.Thumbnails.MINI_KIND, null);
//            }
//            thumbnail.setImageBitmap(bitmap);
//            thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            bucketName.setText(key);

        }
    }

    public HashMap<String, ArrayList<Media>> getMediaList() {
        return mediaList;
    }

    public void addMedia(Media media){

//        if(mediaList.containsKey(media.getBucketName())){
//            mediaList.get(media.getBucketName()).add(media);
//        }else {
//            ArrayList<Media> mediaArrayList = new ArrayList<>();
//            mediaArrayList.add(media);
//            mediaList.put(media.getBucketName(), mediaArrayList);
//        }
//        notifyDataSetChanged();
    }
}
