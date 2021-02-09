package aritra.code.chatters;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import aritra.code.chatters.databinding.ActivityPhoneAuthBinding;

public class PhoneAuthActivity extends AppCompatActivity {

    ActivityPhoneAuthBinding binding;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(PhoneAuthActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) + checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    + checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS ,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION }, 10);
            }
        }


        binding.phoneNumber.requestFocus();

        binding.onContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.phoneNumber.getText().toString().isEmpty()) {
                    Intent intent = new Intent(PhoneAuthActivity.this, OTPActivity.class);
                    intent.putExtra("phoneNumber", binding.phoneNumber.getText().toString());
                    startActivity(intent);
                } else {
                    binding.phoneNumber.setError("Enter phone number");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 10 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Lets Go", Toast.LENGTH_SHORT).show();
        }
    }
}