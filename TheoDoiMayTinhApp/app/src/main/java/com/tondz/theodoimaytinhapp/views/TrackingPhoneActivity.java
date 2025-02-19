package com.tondz.theodoimaytinhapp.views;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tondz.theodoimaytinhapp.R;
import com.tondz.theodoimaytinhapp.common;
import com.tondz.theodoimaytinhapp.databinding.ActivityTrackingPhoneBinding;
import com.tondz.theodoimaytinhapp.models.CellPhone;

public class TrackingPhoneActivity extends AppCompatActivity {

    ActivityTrackingPhoneBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackingPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        onLoadImage();
    }

    private void init() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    private void onLoadImage() {
        reference.child(common.id).child("cellphone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CellPhone cellPhone = snapshot.getValue(CellPhone.class);
                if (cellPhone != null) {
                    Glide.with(TrackingPhoneActivity.this)
                            .load(cellPhone.getUrl()).placeholder(binding.preView.getDrawable())
                            .into(binding.preView);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.tvDateTime.setText("Thời gian :"+cellPhone.getTime());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}