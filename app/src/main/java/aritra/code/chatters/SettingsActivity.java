package aritra.code.chatters;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import aritra.code.chatters.Models.Users;
import aritra.code.chatters.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    Intent intent;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    RelativeLayout relativeLayout;
    ReviewManager manager;
    ReviewInfo reviewInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        initReviewInfo();
        auth = FirebaseAuth.getInstance();
        relativeLayout = findViewById(R.id.relative_layout);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.backArrow.setColorFilter(ContextCompat.getColor(this,
                R.color.whatsAppColor), android.graphics.PorterDuff.Mode.SRC_IN);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReview();
            }
        });

        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                Picasso.get().load(users.getProfilePic()).fit().centerCrop().placeholder(R.drawable.ic_profile).into(binding.profilePic);
                binding.userName.setText(users.getUserName());
                binding.userAbout.setText(users.getAbout());
                binding.userPhone.setText(users.getPhoneNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        binding.addProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 99);

            }
        });

        binding.profileSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String about = binding.userAbout.getText().toString();
                String name = binding.userName.getText().toString();

                HashMap<String, Object> obj = new HashMap<>();
                obj.put("userName", name);
                obj.put("about", about);

                database.getReference().child("Users").child(auth.getUid()).updateChildren(obj);

                intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                Snackbar.make(relativeLayout, "Woo.. Looking Awesome", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void initReviewInfo() {
        manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                reviewInfo = task.getResult();
            } else {
                // There was some problem, continue regardless of the result.
            }
        });
    }

    @Override
    public void onBackPressed() {
        openReview();
        super.onBackPressed();
    }

    private void openReview() {
        if (reviewInfo != null) {
            Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);
            flow.addOnCompleteListener(task -> {
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            });
        }
        startActivity(new Intent(SettingsActivity.this, MainActivity.class));

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            Uri fileUrl = data.getData();
            binding.profilePic.setImageURI(fileUrl);
            Snackbar.make(relativeLayout, "Woo.. Looking Awesome", Snackbar.LENGTH_SHORT).show();
            final StorageReference storageReference = storage.getReference().child("Profile Pictures").child(auth.getUid());
            storageReference.putFile(fileUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(auth.getUid()).child("profilePic").setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }
}