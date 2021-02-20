package aritra.code.chatters.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import aritra.code.chatters.Adapters.StatusAdapter;
import aritra.code.chatters.Adapters.UserAdapter;
import aritra.code.chatters.Models.Contacts;
import aritra.code.chatters.Models.Status;
import aritra.code.chatters.Models.StatusData;
import aritra.code.chatters.Models.Users;
import aritra.code.chatters.StatusUploadActivity;
import aritra.code.chatters.databinding.FragmentChatBinding;

public class
ChatFragment extends Fragment {

    ArrayList<String> contactNumbersList = new ArrayList<String>();
    FragmentChatBinding chatBinding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    ArrayList<Users> list = new ArrayList<Users>();
    ArrayList<Status> listAdapter = new ArrayList<>();
    Users users;
    Users statusUsers;
    Status status;
    UserAdapter userAdapter;
    StatusAdapter statusAdapter;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        chatBinding = FragmentChatBinding.inflate(inflater, container, false);
        ArrayList<Contacts> contactList = loadContactsFromPhone();
        userAdapter = new UserAdapter(list, getContext());
        chatBinding.chatRecyclerView.setAdapter(userAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        chatBinding.chatRecyclerView.setLayoutManager(linearLayoutManager);
        statusAdapter = new StatusAdapter(listAdapter, getContext());
        chatBinding.statusRecyclerView.setAdapter(statusAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        chatBinding.statusRecyclerView.setLayoutManager(layoutManager);
        showAllStatuses();
        showUsers();
        updateLastMessages();


        //        GATHERING ALL CONTACTS FROM USER
        for (Contacts contact : contactList) {
            String contactNumbers = "";

            contactNumbers = contact.getContactNumber().replaceAll("\\s", "");

            if (!contactNumbers.startsWith("+91")) {
                contactNumbers = "+91" + contactNumbers;
            }
            contactNumbersList.add(contactNumbers);
        }


        chatBinding.uploadStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 98);
            }
        });


//        SHARE BUTTON CLICK
        chatBinding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();
            }
        });

        database.getReference().child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                statusUsers = snapshot.getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        return chatBinding.getRoot();
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        String textBody = "Join me in Chatters app and Lets chat and have fun together." +
                "\n Click here  https://play.google.com/store/apps/details?id=aritra.code.chatters to download now";
        String textSubject = "Hey Join me in Chatters App";
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, textBody);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, textSubject);
        startActivity(Intent.createChooser(shareIntent, "Share with"));


    }

    //    LOADING ALL CONTACTS FROM USER
    private ArrayList<Contacts> loadContactsFromPhone() {

        ArrayList<Contacts> list = new ArrayList<>();

        Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Contacts contacts = new Contacts(contactName, contactNumber);
                list.add(contacts);
            }
        }
        return list;
    }

    public void showUsers() {
        //        CHECKING WHICH USERS TO SHOW ON MAIN PAGE CHAT
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());

                    if (dataSnapshot.child("phoneNumber").exists()) {
                        if (!dataSnapshot.getKey().equals(auth.getUid()) &&
                                contactNumbersList.contains(dataSnapshot.child("phoneNumber").getValue(String.class))) {
                            list.add(users);
                        }
                    }

                    chatBinding.progressBar.setVisibility(View.GONE);
                    userAdapter.notifyDataSetChanged();
                }
                //  If New User DOES NOT HAVE ANY FRIENDS  THEN SHOW DEFAULT THREE CONTACTS
                if (list.size() < 1) {
                    chatBinding.noChatCard.setVisibility(View.VISIBLE);
                    chatBinding.uploadStatusBtn.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) chatBinding.shareBtn.getLayoutParams();
                    // position on right bottom
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    public void updateLastMessages() {
        // CHECKING LAST MESSAGES
        database.getReference().child("Chats").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void showAllStatuses() {
        //        FETCHING ALL THE STATUSES OF USER FROM DB AND ADDING TO LIST
        database.getReference().child("Status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listAdapter.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        status = new Status();
                        status.setUserId(snapshot1.getKey());

                        if (contactNumbersList.contains(snapshot1.child("phoneNumber").getValue(String.class)) ||
                                auth.getCurrentUser().getPhoneNumber().equals(snapshot1.child("phoneNumber").getValue(String.class))) {

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
                            String nodeId = snapshot1.getKey();

                            status.setUserName(snapshot1.child("name").getValue(String.class));
                            status.setProfileImage(snapshot1.child("profileImage").getValue(String.class));
                            status.setLastUpdated(snapshot1.child("lastUpdated").getValue(Long.class));
                            status.setHasSeen(snapshot1.child("hasSeen").getValue(Boolean.class));
                            status.setPhoneNumber(snapshot1.child("phoneNumber").getValue(String.class));

                            ArrayList<StatusData> statusData = new ArrayList<>();

                            for (DataSnapshot statusSnapshot : snapshot1.child("Stories").getChildren()) {

                                if (statusSnapshot.exists()) {
                                    status.setStatusId(statusSnapshot.getKey());
                                    if (!dateFormat.format(new Date()).equals(dateFormat.format(statusSnapshot.child("timeStamp").getValue(Long.class)))) {
                                        Log.i("Date is ", dateFormat.format(status.getLastUpdated()));
                                        Log.i("Date TODAY ", dateFormat.format(new Date()));


                                        StorageReference storageReference = storage.getReference().child("Status").child(nodeId).child(statusSnapshot.child("timeStamp")
                                                .getValue(Long.class).toString());

                                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(chatBinding.chatFrag, "Your Status has been removed", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });

                                        database.getReference().child("Status").child(nodeId).child("Stories").child(status.getStatusId()).setValue(null);

                                    } else {

                                        StatusData statusImages = statusSnapshot.getValue(StatusData.class);
                                        statusData.add(statusImages);

                                    }
                                }

                            }

                            status.setStatuses(statusData);
                            if (status.getStatuses().size() > 0) {
                                listAdapter.add(status);
                            }
                        }


                    }

                    statusAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //    Putting User status to Storage and calling StatusUpload Intent to put data to db
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
//            progressDialog.show();
            Uri imageUrl = data.getData();
            Long timeUploaded = new Date().getTime();
            final StorageReference storageReference = storage.getReference().child("Status").child(auth.getUid()).child(timeUploaded.toString());
            storageReference.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            status = new Status();
                            status.setUserName(statusUsers.getUserName());
                            status.setProfileImage(statusUsers.getProfilePic());
                            status.setLastUpdated(timeUploaded);
                            status.setPhoneNumber(statusUsers.getPhoneNumber());

                            HashMap<String, Object> hashObj = new HashMap<>();
                            hashObj.put("name", status.getUserName());
                            hashObj.put("profileImage", status.getProfileImage());
                            hashObj.put("lastUpdated", status.getLastUpdated());
                            hashObj.put("phoneNumber", status.getPhoneNumber());
                            hashObj.put("hasSeen", false);

                            Intent intent = new Intent(getContext(), StatusUploadActivity.class);
                            intent.putExtra("Image", imageUrl.toString());
                            intent.putExtra("Uri", uri.toString());
                            intent.putExtra("Time", status.getLastUpdated().toString());
                            startActivity(intent);
                            database.getReference().child("Status").child(auth.getUid()).updateChildren(hashObj);
                        }
                    });
                }
            });


        }
    }


}