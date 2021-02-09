package aritra.code.chatters;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import aritra.code.chatters.Models.StatusData;
import aritra.code.chatters.databinding.ActivityStatusUploadBinding;

public class StatusUploadActivity extends AppCompatActivity {
    ActivityStatusUploadBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    MediaPlayer statusSound;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatusUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        statusSound = MediaPlayer.create(this, R.raw.status_upload);
        intent = getIntent();
        Uri imageUri = Uri.parse(intent.getStringExtra("Image"));
        String imageUrl = intent.getStringExtra("Uri");
        Long time = Long.parseLong(intent.getStringExtra("Time"));

        binding.uploadImage.setImageURI(imageUri);

        StatusData statusData = new StatusData(imageUrl, time);

        binding.uploadStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.imageDescription.getText().toString().isEmpty()) {
                    statusData.setImageDescription(binding.imageDescription.getText().toString());
                }
                database.getReference().child("Status").child(auth.getUid()).child("Stories").push().setValue(statusData);
                statusSound.start();
                startActivity(new Intent(StatusUploadActivity.this, MainActivity.class));
                finishAffinity();
            }
        });


    }
}