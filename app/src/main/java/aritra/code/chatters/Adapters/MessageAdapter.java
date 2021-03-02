package aritra.code.chatters.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import aritra.code.chatters.Models.MessagesModel;
import aritra.code.chatters.R;

public class MessageAdapter extends RecyclerView.Adapter {

    ArrayList<MessagesModel> messagesModels;
    Context context;
    String receiverId;
    String senderRoom, receiverRoom;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;


    public MessageAdapter(ArrayList<MessagesModel> messagesModels, Context context, String receiverId, String senderRoom, String receiverRoom) {
        this.messagesModels = messagesModels;
        this.context = context;
        this.receiverId = receiverId;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;

    }

    public MessageAdapter(ArrayList<MessagesModel> messagesModels, Context context) {
        this.messagesModels = messagesModels;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver, parent, false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messagesModels.get(position).getUserId().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessagesModel messagesModel = messagesModels.get(position);


        int[] reactions = new int[]{
                R.drawable.like,
                R.drawable.love,
                R.drawable.lol,
                R.drawable.wow,
                R.drawable.sad,
                R.drawable.angry
        };

        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();

        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {

            if (holder.getClass() == SenderViewHolder.class) {

                if (pos >= 0) {
                    SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
                    senderViewHolder.feeling.setImageResource(reactions[pos]);
                    senderViewHolder.feeling.setVisibility(View.VISIBLE);

                    switch (pos) {
                        case 0:
                            MediaPlayer.create(context, R.raw.like).start();
                            break;
                        case 1:
                            MediaPlayer.create(context, R.raw.heart).start();
                            break;
                        case 2:
                            MediaPlayer.create(context, R.raw.lolface).start();
                            break;
                        case 3:
                            MediaPlayer.create(context, R.raw.wow).start();
                            break;
                        case 4:
                            MediaPlayer.create(context, R.raw.disappointedface).start();
                            break;
                        case 5:
                            MediaPlayer.create(context, R.raw.angry).start();
                            break;
                    }
                }
            } else {

                if (pos >= 0) {
                    RecieverViewHolder viewHolder = (RecieverViewHolder) holder;
                    viewHolder.feeling.setImageResource(reactions[pos]);
                    viewHolder.feeling.setVisibility(View.VISIBLE);

                    switch (pos) {
                        case 0:
                            MediaPlayer.create(context, R.raw.like).start();
                            break;
                        case 1:
                            MediaPlayer.create(context, R.raw.heart).start();
                            break;
                        case 2:
                            MediaPlayer.create(context, R.raw.lolface).start();
                            break;
                        case 3:
                            MediaPlayer.create(context, R.raw.wow).start();
                            break;
                        case 4:
                            MediaPlayer.create(context, R.raw.disappointedface).start();
                            break;
                        case 5:
                            MediaPlayer.create(context, R.raw.angry).start();
                            break;
                    }
                }
            }
            messagesModel.setFeeling(pos);

            FirebaseDatabase.getInstance().getReference().child("Chats")
                    .child(senderRoom + receiverRoom)
                    .child(messagesModel.getMessageId())
                    .setValue(messagesModel);
            FirebaseDatabase.getInstance().getReference().child("Chats")
                    .child(receiverRoom + senderRoom)
                    .child(messagesModel.getMessageId())
                    .setValue(messagesModel);

            return true; // true is closing popup, false is requesting a new selection
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context).setTitle("Delete").setMessage("Are you sure")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderId = FirebaseAuth.getInstance().getUid() + receiverId;
                                database.getReference().child("Chats").child(senderId).child(messagesModel.getMessageId()).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                return false;
            }
        });

        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder) holder).sendMessage.setText(messagesModel.getMessage());
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            ((SenderViewHolder) holder).sendMessageTime.setText(dateFormat.format(new Date(messagesModel.getMessageTime())));
            ((SenderViewHolder) holder).messageStatus.setImageResource(R.drawable.check_unread_msg);


            FirebaseDatabase.getInstance().getReference().child("Chats")
                    .child(receiverRoom + senderRoom).limitToLast(1)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Log.i("CHILDREN ", snapshot.hasChildren() + "");
                                if (snapshot1.child("hasSeen").getValue(Boolean.class)) {
                                    ((SenderViewHolder) holder).messageStatus.setImageResource(R.drawable.ic_baseline_check_24);
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            if (messagesModel.getFeeling() >= 0) {
                ((SenderViewHolder) holder).feeling.setImageResource(reactions[messagesModel.getFeeling()]);
                ((SenderViewHolder) holder).feeling.setVisibility(View.VISIBLE);
            } else {
                ((SenderViewHolder) holder).feeling.setVisibility(View.INVISIBLE);
            }
            ((SenderViewHolder) holder).sendMessage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    popup.onTouch(view, motionEvent);
                    return false;
                }
            });


        } else {

            ((RecieverViewHolder) holder).recievedMessage.setText(messagesModel.getMessage());
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            ((RecieverViewHolder) holder).recievedMessageTime.setText(dateFormat.format(new Date(messagesModel.getMessageTime())));


            String senderRoom = FirebaseAuth.getInstance().getUid() + receiverId;
            messagesModel.setHasSeen(true);
            FirebaseDatabase.getInstance().getReference().child("Chats").child(senderRoom).
                    child(messagesModel.getMessageId()).setValue(messagesModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            });

            if (messagesModel.getFeeling() >= 0) {
                ((RecieverViewHolder) holder).feeling.setVisibility(View.VISIBLE);
                ((RecieverViewHolder) holder).feeling.setImageResource(reactions[messagesModel.getFeeling()]);
            } else {
                ((RecieverViewHolder) holder).feeling.setVisibility(View.INVISIBLE);

            }
            ((RecieverViewHolder) holder).recievedMessage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    popup.onTouch(view, motionEvent);
                    return false;
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return messagesModels.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder {

        TextView recievedMessage, recievedMessageTime;
        ImageView feeling;
        RelativeLayout messageBox;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recievedMessage = itemView.findViewById(R.id.messsage_receive);
            recievedMessageTime = itemView.findViewById(R.id.time_receive_text);
            feeling = itemView.findViewById(R.id.feeling);
            messageBox = itemView.findViewById(R.id.layout_receive);
        }

    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView sendMessage, sendMessageTime;
        ImageView feeling, messageStatus;
        RelativeLayout messageBox;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            sendMessage = itemView.findViewById(R.id.messsage_send);
            sendMessageTime = itemView.findViewById(R.id.time_send_text);
            feeling = itemView.findViewById(R.id.feeling);
            messageBox = itemView.findViewById(R.id.messageBox);
            messageStatus = itemView.findViewById(R.id.send_affirm);

        }
    }
}
