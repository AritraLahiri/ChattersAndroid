package aritra.code.chatters.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import aritra.code.chatters.ChatDetailActivity;
import aritra.code.chatters.ChatProfileActivity;
import aritra.code.chatters.Models.Users;
import aritra.code.chatters.R;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    ArrayList<Users> list;
    Context context;


    public UserAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_all_chats, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Users users = list.get(position);


        Picasso.get().load(users.getProfilePic()).fit().centerInside().placeholder(R.drawable.ic_profile).into(holder.profilePic);
        holder.userName.setText(users.getUserName());

        String childKey = FirebaseAuth.getInstance().getUid() + users.getUserId();


        FirebaseDatabase.getInstance()
                .getReference()
                .child("Chats")
                .child(childKey)
                .orderByChild("timestamp")
                .limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                holder.lastMessage.setText(snapshot1.child("message").getValue(String.class));
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


        FirebaseDatabase.getInstance().getReference()
                .child("Chats")
                .child(childKey)
                .orderByChild("hasSeen")
                .equalTo(false)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChildren()) {
                            holder.unread_text.setVisibility(View.VISIBLE);
                            holder.unread_text.setText(String.valueOf(snapshot.getChildrenCount()));
                            holder.lastMessage.setTextColor(R.color.fb_color);


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.unread_text.setVisibility(View.INVISIBLE);
                holder.lastMessage.setTextColor(Color.GRAY);
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("UserId", users.getUserId());
                intent.putExtra("ProfilePic", users.getProfilePic());
                intent.putExtra("Username", users.getUserName());
                context.startActivity(intent);
            }

        });

        holder.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatProfileActivity.class);
                intent.putExtra("UserId", users.getUserId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView userName, lastMessage, unread_text;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.user_name);
            lastMessage = itemView.findViewById(R.id.last_message);
            unread_text = itemView.findViewById(R.id.unread_msg_text_view);

        }

    }

}
