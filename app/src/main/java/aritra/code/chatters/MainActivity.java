package aritra.code.chatters;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import aritra.code.chatters.Adapters.FragmentAdapter;
import aritra.code.chatters.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    TabLayout tabLayout;
    ViewPager viewPager;
    FirebaseDatabase database;


    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getUid() != null) {
            updateUserState("Online");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isTaskRoot() && auth.getUid() != null) {
            if (auth.getUid() != null) {
                updateUserState("Offline");
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (isTaskRoot() && auth.getUid() != null) {
            if (auth.getUid() != null) {
                updateUserState("Offline");
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setElevation(0);
        auth = FirebaseAuth.getInstance();
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        database = FirebaseDatabase.getInstance();
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.news);
        tabLayout.getTabAt(2).setIcon(R.drawable.viral_icon);
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
    }


    public final void updateUserState(String state) {
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        String currentDate, currentTime;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        currentDate = dateFormat.format(calendar.getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        currentTime = timeFormat.format(calendar.getTime());
        HashMap<String, Object> userStateMap = new HashMap<>();
        userStateMap.put("state", state);
        userStateMap.put("time", currentTime);
        userStateMap.put("date", currentDate);
        database.getReference().child("Users").child(auth.getUid()).child("State").updateChildren(userStateMap);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.setting:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.privacy:
                openPrivacyPolicy("https://ggamingparadise.blogspot.com/2021/02/privacy-policy-aritra-lahiri-built_12.html");
                break;

            case R.id.logout:
                updateUserState("Offline");
                auth.signOut();
                intent = new Intent(MainActivity.this, PhoneAuthActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void openPrivacyPolicy(String s) {
        Uri uri = Uri.parse(s);
        Intent launchPage = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(launchPage);
    }
}