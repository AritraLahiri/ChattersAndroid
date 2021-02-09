package aritra.code.chatters;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import aritra.code.chatters.Adapters.MessageAdapter;
import aritra.code.chatters.Models.MessagesModel;
import aritra.code.chatters.Models.Users;
import aritra.code.chatters.databinding.ActivityChatDetailBinding;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding chatDetailBinding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    Intent intent;
    MainActivity mainActivity;
    String profilePic;
    String userName;
    ArrayList<MessagesModel> messagesModels = new ArrayList<MessagesModel>();


    @Override
    protected void onStart() {
        super.onStart();
        mainActivity.updateUserState("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainActivity.updateUserState("Offline");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatDetailBinding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(chatDetailBinding.getRoot());
        getSupportActionBar().hide();
        mainActivity = new MainActivity();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        MediaPlayer sendSound = MediaPlayer.create(this, R.raw.message_send);
        final String senderId = auth.getUid();
        final String receiverId = getIntent().getStringExtra("UserId");
        profilePic = getIntent().getStringExtra("ProfilePic");
        userName = getIntent().getStringExtra("Username");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = dateFormat.format(calendar.getTime());


        database.getReference().child("Users").child(receiverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users = snapshot.getValue(Users.class);

                if (users.getState() != null) {
                    if (users.getState().getState().equals("Offline") && users.getState().getDate().equals(currentDate)) {
                        chatDetailBinding.userState.setText("Active at " + users.getState().getTime());
                    } else if (users.getState().getState().equals("Offline") && !users.getState().getDate().equals(currentDate)) {
                        chatDetailBinding.userState.setText("Last Active " + users.getState().getDate());
                    } else {
                        chatDetailBinding.userState.setText(users.getState().getState());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        chatDetailBinding.userName.setText(userName.toString());
        Picasso.get().load(profilePic).resize(50, 50).centerCrop().placeholder(R.drawable.ic_profile).into(chatDetailBinding.profileImage);

        chatDetailBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        chatDetailBinding.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ChatDetailActivity.this, ChatProfileActivity.class);
                intent.putExtra("UserId", receiverId);
                startActivity(intent);
            }
        });
        chatDetailBinding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ChatDetailActivity.this, ChatProfileActivity.class);
                intent.putExtra("UserId", receiverId);
                startActivity(intent);
            }
        });


        chatDetailBinding.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), chatDetailBinding.option);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.setting:
                                startActivity(new Intent(ChatDetailActivity.this, SettingsActivity.class));
                                break;

                            case R.id.logout:
                                auth.signOut();
                                startActivity(new Intent(ChatDetailActivity.this, PhoneAuthActivity.class));
                                break;

                            case R.id.profile:
                                intent = new Intent(ChatDetailActivity.this, ChatProfileActivity.class);
                                intent.putExtra("UserId", receiverId);
                                startActivity(intent);
                                break;


                        }

                        return false;
                    }
                });

                popupMenu.show();
            }
        });


        final ArrayList<MessagesModel> messagesModels = new ArrayList<>();
        final MessageAdapter messageAdapter = new MessageAdapter(messagesModels, this, receiverId, senderId, receiverId);
        chatDetailBinding.chatDetailRecyclerView.setAdapter(messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        chatDetailBinding.chatDetailRecyclerView.setLayoutManager(linearLayoutManager);

        final String senderRoom = senderId + receiverId;
        final String receiverRoom = receiverId + senderId;


        database.getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesModels.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MessagesModel model = snapshot1.getValue(MessagesModel.class);
                    model.setMessageId(snapshot1.getKey());
                    messagesModels.add(model);

                }
                chatDetailBinding.chatDetailRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        chatDetailBinding.messsageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = chatDetailBinding.message.getText().toString();
                final MessagesModel model = new MessagesModel(senderId, message);
                model.setMessageTime(new Date().getTime());
                model.setHasSeen(true);
                chatDetailBinding.message.setText("");
                database.getReference().child("Chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        model.setHasSeen(false);
                        database.getReference().child("Chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendSound.start();
                                messageAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });


    }


}