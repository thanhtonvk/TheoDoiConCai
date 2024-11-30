package com.tondz.theodoimaytinhapp.views;

import android.os.Bundle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tondz.theodoimaytinhapp.adapters.DownloadAdapter;
import com.tondz.theodoimaytinhapp.common;
import com.tondz.theodoimaytinhapp.databinding.ActivityTheoDoiBinding;
import com.tondz.theodoimaytinhapp.models.Download;
import com.tondz.theodoimaytinhapp.models.History;
import com.tondz.theodoimaytinhapp.adapters.LichSuAdapter;

import java.util.ArrayList;
import java.util.List;

public class TheoDoiActivity extends AppCompatActivity {
    ActivityTheoDoiBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<History> historyList = new ArrayList<>();
    LichSuAdapter historyAdapter;
    DownloadAdapter downloadAdapter;
    List<Download> downloadList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTheoDoiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        onLoadImage();
        onClick();
    }

    void onClick() {
        binding.btnHistory.setOnClickListener(v -> {
            historyAdapter = new LichSuAdapter(getApplicationContext(), historyList);
            binding.rvHistory.setAdapter(historyAdapter);
            onLoadHistory();
        });
        binding.btnDownload.setOnClickListener(v -> {
            downloadAdapter = new DownloadAdapter(getApplicationContext(), downloadList);
            binding.rvHistory.setAdapter(downloadAdapter);
            onLoadDownload();
        });
        binding.btnSend.setOnClickListener(v -> {
            String value = binding.edtMessage.getText().toString().strip();
            if (!value.isEmpty()) {
                reference.child(common.id).child("message").setValue(value);
                binding.edtMessage.setText("");
            }
        });
    }

    private void onLoadDownload() {
        reference.child(common.id).child("download").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                downloadList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()
                ) {
                    Download download = dataSnapshot.getValue(Download.class);
                    downloadList.add(download);
                }
                downloadAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onLoadHistory() {
        reference.child(common.id).child("history").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historyList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()
                ) {
                    History history = dataSnapshot.getValue(History.class);
                    historyList.add(history);
                }
                historyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onLoadImage() {
        reference.child(common.id).child("screen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("TAG", "onDataChange: " + snapshot);
                String value = snapshot.getValue(String.class);
                if (value != null) {
                    Glide.with(TheoDoiActivity.this)
                            .load(value).placeholder(binding.preView.getDrawable())
                            .into(binding.preView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void init() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

}