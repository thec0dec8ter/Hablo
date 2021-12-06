package dev.thec0dec8ter.hablo.adapter.message;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.thec0dec8ter.hablo.model.Document;

public class DocumentViewHolder extends RecyclerView.ViewHolder {

    public DocumentViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(Document document){
        if(document.getSender().equals("isMe")){
            itemView.setTag(true);
        }else {
            itemView.setTag(false);
        }
    }
}
