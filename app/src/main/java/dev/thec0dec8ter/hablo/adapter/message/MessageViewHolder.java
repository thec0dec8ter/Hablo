package dev.thec0dec8ter.hablo.adapter.message;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dev.thec0dec8ter.hablo.model.Message;

public class MessageViewHolder extends RecyclerView.ViewHolder{
    LinearLayout container;
    TextView user;
    TextView content;
    TextView timestamp;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

    }

    public void bind(Message message){
        if(message.getSender().equals("isMe")){
            itemView.setTag(true);
        }else {
            itemView.setTag(false);
        }
    }
}