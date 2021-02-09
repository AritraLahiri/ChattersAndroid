package aritra.code.chatters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import aritra.code.chatters.Models.Users;
import aritra.code.chatters.databinding.ActivityChatProfileBinding;

public class ChatProfileActivity extends AppCompatActivity {

    ActivityChatProfileBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        String userId = getIntent().getStringExtra("UserId");


        database.getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                users = snapshot.getValue(Users.class);
                users.setUserId(snapshot.getKey());
                String userProfilePic = users.getProfilePic();
                Picasso.get().load(userProfilePic).fit().placeholder(R.drawable.ic_profile).into(binding.profilePic);
                binding.userName.setText(users.getUserName());
                binding.phoneNumber.setText(users.getPhoneNumber());

                if (users.getAbout() == null) {
                    users.setAbout("Hey I'm using Chatters");
                }

                binding.about.setText(users.getAbout());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatProfileActivity.this, ViewProfileImageActivity.class);
                intent.putExtra("profilePic",users.getProfilePic());
                intent.putExtra("UserId",users.getUserId() );
                startActivity(intent);
            }
        });


        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatProfileActivity.this, ChatDetailActivity.class);
                intent.putExtra("UserId", users.getUserId());
                intent.putExtra("ProfilePic", users.getProfilePic());
                intent.putExtra("Username", users.getUserName());
                startActivity(intent);
            }
        });


    }
}