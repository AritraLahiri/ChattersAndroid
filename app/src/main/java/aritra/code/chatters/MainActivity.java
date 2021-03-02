package aritra.code.chatters;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import aritra.code.chatters.Adapters.FragmentAdapter;
import aritra.code.chatters.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final int my_requestCode = 999;
    ActivityMainBinding binding;
    FirebaseAuth auth;
    TabLayout tabLayout;
    ViewPager viewPager;
    FirebaseDatabase database;
    private AppUpdateManager appUpdateManager;
    private Task<AppUpdateInfo> appUpdateInfoTask;


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
    protected void onResume() {
        super.onResume();
        checkAndUpdateApp();
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
        getInAppUpdates();
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


    public void getInAppUpdates() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this);
        // Returns an intent object that you use to check for an update.
        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        // Checks that the platform will allow the specified type of update.
        checkAndUpdateApp();

    }

    private void checkAndUpdateApp() {
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.

                try {
                    appUpdateManager
                            .startUpdateFlowForResult(
                                    appUpdateInfo,
                                    AppUpdateType.IMMEDIATE,
                                    MainActivity.this,
                                    my_requestCode
                            );
                } catch (IntentSender.SendIntentException e) {
                    Log.e("Error in App Update", e.getLocalizedMessage());

                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == my_requestCode) {
            if (resultCode != RESULT_OK) {
                finish();
            }
        }
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
                finishAffinity();
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