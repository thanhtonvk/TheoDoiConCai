package com.tondz.theodoimaytinhapp.views;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TrackingPhoneActivity extends AppCompatActivity {

    ActivityTrackingPhoneBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<CellPhone> cellPhoneList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackingPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            onLoadImage();
        }
    }

    private void init() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void onLoadImage() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);
        reference.child(common.id).child("cellphone").child(formattedDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cellPhoneList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()
                ) {
                    CellPhone cellPhone = dataSnapshot.getValue(CellPhone.class);
                    cellPhoneList.add(cellPhone);
                }
                if (!cellPhoneList.isEmpty()) {
                    CellPhone cellPhone = cellPhoneList.get(cellPhoneList.size() - 1);
                    Glide.with(TrackingPhoneActivity.this)
                            .load(cellPhone.getUrl()).placeholder(binding.preView.getDrawable())
                            .into(binding.preView);
                    runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            binding.tvDateTime.setText("Thời gian :" + cellPhone.getTime());
                            binding.tvSoLan.setText("Số lần :" + cellPhoneList.size());
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