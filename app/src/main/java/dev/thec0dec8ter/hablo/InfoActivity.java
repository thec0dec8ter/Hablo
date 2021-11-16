package dev.thec0dec8ter.hablo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import dev.thec0dec8ter.hablo.adapter.ParticipantAdapter;

public class InfoActivity extends AppCompatActivity {

    private RecyclerView participantRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        participantRecycler = findViewById(R.id.participant_recycler);

        participantRecycler.setAdapter(new ParticipantAdapter());
    }
}