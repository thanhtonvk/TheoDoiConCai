package com.tondz.theodoimaytinhapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tondz.theodoimaytinhapp.databinding.ActivityMainBinding;
import com.tondz.theodoimaytinhapp.views.TheoDoiActivity;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseDatabase database;
    DatabaseReference reference;
    private static final String PREF_NAME = "MyAppPrefs";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        onClick();
        onLoad();
    }

    void onLoad() {
        String value = sharedPreferences.getString("id", "");
        binding.edtId.setText(value);
    }

    void onClick() {
        binding.btnConfirm.setOnClickListener(v -> {
            String value = binding.edtId.getText().toString().strip();
            if (value.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Không được bỏ trống", Toast.LENGTH_SHORT).show();
            } else {
                editor.putString("id", value);
                editor.apply();
                common.id = value;
                startActivity(new Intent(getApplicationContext(), TheoDoiActivity.class));
            }
        });

    }

    void init() {
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

}