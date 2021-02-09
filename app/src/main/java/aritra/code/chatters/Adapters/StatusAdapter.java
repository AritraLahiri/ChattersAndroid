package aritra.code.chatters.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import aritra.code.chatters.MainActivity;
import aritra.code.chatters.Models.Status;
import aritra.code.chatters.Models.StatusData;
import aritra.code.chatters.R;
import aritra.code.chatters.databinding.SampleStatusBinding;
import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {


    Context context;
    ArrayList<Status> list;

    public StatusAdapter(ArrayList<Status> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_status, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {
        Status status = list.get(position);


        if (status.getStatuses().size() > 0) {

            StatusData lastStatusImage = status.getStatuses().get(status.getStatuses().size() - 1);
            holder.binding.circularStatusView.setPortionsCount(status.getStatuses().size());
            Picasso.get().load(lastStatusImage.getImageUrl()).fit().centerCrop().placeholder(R.drawable.ic_profile).into(holder.binding.statusImage);
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

            holder.binding.circularStatusView.setPortionsColor(status.getHasSeen() ? R.color.white : R.color.whatsAppColor);


            holder.binding.imageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<MyStory> myStories = new ArrayList<>();

                    for (StatusData status : status.getStatuses()) {
                        myStories.add(new MyStory(
                                status.getImageUrl(),
                                null,
                                status.getImageDescription()

                        ));
                    }

                    new StoryView.Builder(((MainActivity) context).getSupportFragmentManager())
                            .setStoriesList(myStories) // Required
                            .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                            .setTitleText(status.getUserName()) // Default is Hidden
                            .setSubtitleText(dateFormat.format(new Date(status.getLastUpdated()))) // Default is Hidden
                            .setTitleLogoUrl(status.getProfileImage()) // Default is Hidden
                            .setStoryClickListeners(new StoryClickListeners() {
                                @Override
                                public void onDescriptionClickListener(int position) {
                                    //your action
                                }

                                @Override
                                public void onTitleIconClickListener(int position) {
                                    //your action
                                }
                            })
                            .build() // Must be called before calling show method
                            .show();

                    HashMap<String, Object> hashObj = new HashMap<>();
                    hashObj.put("hasSeen", true);
                    hashObj.put("lastUpdated", status.getLastUpdated());
                    hashObj.put("name", status.getUserName());
                    hashObj.put("phoneNumber", status.getPhoneNumber());
                    hashObj.put("profileImage", status.getProfileImage());
                    FirebaseDatabase.getInstance().getReference().child("Status").child(FirebaseAuth.getInstance().getUid()).updateChildren(hashObj);

                    notifyDataSetChanged();


                }

            });


        }
    }

    @Override
    public int getItemCount() {

            return list.size();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {

        SampleStatusBinding binding;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleStatusBinding.bind(itemView);
        }
    }

}



