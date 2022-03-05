package dev.thec0dec8ter.hablo.adapter.message;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.thec0dec8ter.hablo.R;
import dev.thec0dec8ter.hablo.model.Document;
import dev.thec0dec8ter.hablo.model.Media;
import dev.thec0dec8ter.hablo.model.Message;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<Object> messages;

    public MessageAdapter(Context context){
        this.context = context;
        messages = new ArrayList<>();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
                return new MessageViewHolder(view);
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_document, parent, false);
                return new DocumentViewHolder(view);
            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_audio, parent, false);
                return new AudioViewHolder(view);
            case 3:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_image, parent, false);
                return new ImageViewHolder(view);
            case 4:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_video, parent, false);
                return new VideoViewHolder(view);
            default:
                return null;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(messages.size() > 0){
            int type = -1;
            if(messages.get(position) instanceof Message){
                type = 0;
            }else if(messages.get(position) instanceof Document){
                type = 1;
            }else if(messages.get(position) instanceof Media){
                Media media = (Media) messages.get(position);
                switch (media.getType()){
                    case "audio":
                        type = 2;
                    case "image":
                        type = 3;
                    case "video":
                        type = 4;
                }
            }
            return type;
        }else {
            return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
                messageViewHolder.bind((Message) messages.get(position));
                break;
            case 1:
                DocumentViewHolder documentViewHolder = (DocumentViewHolder) holder;
                documentViewHolder.bind((Document) messages.get(position));
                break;
            case 2:
                AudioViewHolder audioViewHolder = (AudioViewHolder) holder;
                audioViewHolder.bind((Media) messages.get(position));
                break;
            case 3:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                imageViewHolder.bind((Media) messages.get(position));
                break;
            case 4:
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                videoViewHolder.bind((Media) messages.get(position));
                break;
        }
        setAlignment(holder);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private void setAlignment(RecyclerView.ViewHolder holder) {
        boolean isMe = (boolean) holder.itemView.getTag();
        if (isMe) {
            LinearLayout container = holder.itemView.findViewById(R.id.container);
            container.setGravity(Gravity.END);


//            RelativeLayout.LayoutParams lp =
//                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
//            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
//            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            holder.content.setLayoutParams(lp);
//            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
//            layoutParams.gravity = Gravity.RIGHT;
//            holder.txtMessage.setLayoutParams(layoutParams);
//
//            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
//            layoutParams.gravity = Gravity.RIGHT;
//            holder.txtInfo.setLayoutParams(layoutParams);
        } else {
        }
    }


    public void addMessage(Message message){
        messages.add(message);
        notifyDataSetChanged();
    }

    public void addDocument(Document document){
        messages.add(document);
        notifyDataSetChanged();
    }

    public void addMedia(Media media){
        this.messages.add(media);
        notifyDataSetChanged();
    }

    public void clearMessages(){
        messages.clear();
        notifyDataSetChanged();
    }
}