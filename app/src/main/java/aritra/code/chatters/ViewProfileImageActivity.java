package aritra.code.chatters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import aritra.code.chatters.databinding.ActivityViewProfileImageBinding;

public class ViewProfileImageActivity extends AppCompatActivity {

    ActivityViewProfileImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProfileImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        String profilePic = getIntent().getStringExtra("profilePic");
        String userId = getIntent().getStringExtra("UserId");
        Picasso.get().load(profilePic).fit().centerInside().placeholder(R.drawable.ic_profile).into(binding.userProfilePic);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewProfileImageActivity.this, ChatProfileActivity.class);
                intent.putExtra("UserId", userId);
                startActivity(intent);

            }
        });


    }
}