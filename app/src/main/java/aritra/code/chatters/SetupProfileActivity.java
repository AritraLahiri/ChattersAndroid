package aritra.code.chatters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import aritra.code.chatters.Models.Users;
import aritra.code.chatters.databinding.ActivitySetupProfileBinding;

public class SetupProfileActivity extends AppCompatActivity {

    ActivitySetupProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage;
    ProgressDialog progressDialog;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        generateNotificationToken();

        binding.addProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 99);
            }
        });

        binding.accountCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.userName.getText().toString().isEmpty()) {
                    binding.userName.setError("Enter Your Name");
                } else if (selectedImage != null) {
                    progressDialog.show();
                    byte[] userProfilePic = ImageSizeCompress.compressImage(selectedImage, getApplicationContext());

                    StorageReference storageReference = storage.getReference().child("Profile Pictures").child(auth.getUid());
                    storageReference.putBytes(userProfilePic).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String profilePic = uri.toString();
                                        Users users = new Users(auth.getCurrentUser().getPhoneNumber(), binding.userName.getText().toString(), token);
                                        users.setProfilePic(profilePic);
                                        database.getReference().child("Users").child(auth.getUid()).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(SetupProfileActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finishAffinity();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });

                } else {
                    progressDialog.show();
                    Users users = new Users(auth.getCurrentUser().getPhoneNumber(), binding.userName.getText().toString(), token);
                    database.getReference().child("Users").child(auth.getUid()).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(SetupProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    });
                }

            }
        });

    }

    private void generateNotificationToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    token = task.getResult();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            selectedImage = data.getData();
            binding.profilePhoto.setImageURI(data.getData());


        }
    }
}