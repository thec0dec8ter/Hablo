package dev.thec0dec8ter.hablo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.thec0dec8ter.hablo.R;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder> {

    private ArrayList<String> participants = new ArrayList<>();

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participant, parent, false);
        return new ParticipantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 14;
    }

    public class ParticipantViewHolder extends RecyclerView.ViewHolder{

        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
