package aritra.code.chatters.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import aritra.code.chatters.FullNewsActivity;
import aritra.code.chatters.Models.NewsPOJO;
import aritra.code.chatters.R;
import aritra.code.chatters.databinding.SampleNewsBinding;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {


    Context context;
    ArrayList<NewsPOJO> list;
    SampleNewsBinding binding;

    public NewsAdapter(Context context, ArrayList<NewsPOJO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_news, parent, false);
        return new NewsViewHolder(view);
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
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsPOJO data = list.get(position);

        Picasso.get().load(data.getUrlToImage()).fit().centerCrop().placeholder(R.drawable.ic_newsplaceholder).into(binding.newsPic);
        binding.newsTitle.setText(data.getTitle());
        binding.newsDesc.setText(data.getDescription());
        binding.provider.setText(data.getSource().getName());

        binding.provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL providerUrl = null;
                try {
                    providerUrl = new URL(data.getUrl());
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse("https://" + providerUrl.getHost()));
                    context.startActivity(viewIntent);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


            }
        });


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.ENGLISH);
            Date date = sdf.parse(data.getPublishedAt());
            Date currentDate = new Date();

            if (currentDate.getDate() != date.getDate()) {
                binding.time.setText(currentDate.getDate() - date.getDate() + " day ago");
            } else {
                binding.time.setText(currentDate.getHours() - date.getHours() + " hour ago");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullNewsActivity.class);
                intent.putExtra("url", data.getUrl());
                intent.putExtra("name",data.getSource().getName());
                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleNewsBinding.bind(itemView);
        }
    }

}
