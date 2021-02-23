package aritra.code.chatters.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import aritra.code.chatters.Models.DummyPOJO;
import aritra.code.chatters.R;
import aritra.code.chatters.databinding.SampleVideoBinding;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private static String apiKey = "AIzaSyA6xGwp7f305cbE5F8lsHi4VrMcbIN-Y_Q";
    Context context;
    List<DummyPOJO> list;

    public VideoAdapter(Context context, List<DummyPOJO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        DummyPOJO data = list.get(position);
        holder.binding.videoTitle.setText(data.getDetails().getTitle());
        holder.binding.channelName.setText(data.getDetails().getChannelTitle());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.ENGLISH);
            Date date = sdf.parse(data.getDetails().getPublishedAt());
            Date currentDate = new Date();

            if (currentDate.getDate() != date.getDate()) {
                holder.binding.date.setText(currentDate.getDate() - date.getDate() + " day ago");
            } else {
                holder.binding.date.setText(currentDate.getHours() - date.getHours() + " hour ago");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        Picasso.get().load(data.getDetails()
                .getThumbnails().getHigh()
                .getUrl()).fit()
                .centerCrop()
                .placeholder(R.drawable.ic_newsplaceholder)
                .into(holder.binding.youtubeThumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startVideo(data.getVideoId());
            }
        });

        holder.binding.youtubeThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVideo(data.getVideoId());
            }
        });


    }

    private void startVideo(String id) {
        Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, apiKey,
                id,
                100,
                true,
                false
        );
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        SampleVideoBinding binding;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleVideoBinding.bind(itemView);
        }


    }


}
