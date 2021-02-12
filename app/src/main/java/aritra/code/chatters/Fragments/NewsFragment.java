package aritra.code.chatters.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import aritra.code.chatters.Adapters.NewsAdapter;
import aritra.code.chatters.BuildConfig;
import aritra.code.chatters.Models.NewsArray;
import aritra.code.chatters.Models.NewsPOJO;
import aritra.code.chatters.NewsApi;
import aritra.code.chatters.RetrofitInstance;
import aritra.code.chatters.databinding.FragmentNewsBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {

    FragmentNewsBinding binding;
    int currentItems, scrolledOutItems, totalResults, totalItems, pageCount;
    private ArrayList<NewsPOJO> list = new ArrayList<>();
    private String NEWS_BASE_URL = "https://newsapi.org/";
    private String newsApiKey = BuildConfig.NewsApiKey;
    private String pageHeader = "&page=";
    private String pageSize = "&pageSize=100";
    private String newsHeader = "v2/top-headlines?country=in&apiKey=";
    private String categoryHeader = "&category=";
    private NewsAdapter adapter;
    private NewsApi newsApi;
    private Boolean isScrolling = false;
    private LinearLayoutManager linearLayoutManager;


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        linearLayoutManager = new LinearLayoutManager(getContext());
        pageCount = 1;
        list = new ArrayList<>();
        adapter = new NewsAdapter(getContext(), list);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    categoryHeader = "&category=" + compoundButton.getText().toString().toLowerCase();
                } else {
                    categoryHeader = "&category=";
                }
                list.clear();
                adapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.VISIBLE);
                getNews(pageCount);
                adapter = new NewsAdapter(getContext(), list);
                binding.recyclerView.setAdapter(adapter);
            }
        };

        binding.politics.setOnCheckedChangeListener(checkedChangeListener);
        binding.movies.setOnCheckedChangeListener(checkedChangeListener);
        binding.sports.setOnCheckedChangeListener(checkedChangeListener);
        binding.technology.setOnCheckedChangeListener(checkedChangeListener);
        binding.health.setOnCheckedChangeListener(checkedChangeListener);
        binding.business.setOnCheckedChangeListener(checkedChangeListener);

        getNews(pageCount);


        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = binding.recyclerView.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrolledOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (linearLayoutManager.getItemCount() < totalResults &&
                        isScrolling && (currentItems + scrolledOutItems) == totalItems) {
                    isScrolling = false;
                    pageCount++;
                    getNews(pageCount);
                }


            }
        });

        return binding.getRoot();
    }

    private void getNews(int pageCount) {
        newsApi = RetrofitInstance.getRetrofit().create(NewsApi.class);
        newsApi.getData(NEWS_BASE_URL + newsHeader + newsApiKey + pageSize + pageHeader + pageCount + categoryHeader).enqueue(new Callback<NewsArray>() {
            @Override
            public void onResponse(Call<NewsArray> call, Response<NewsArray> response) {
                binding.progressBar.setVisibility(View.GONE);
                NewsArray data = response.body();
                if (data != null) {
                    totalResults = data.getTotalResults();
                    list.addAll(data.getArticles());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewsArray> call, Throwable t) {
                Toast.makeText(getContext(), "Data Fetching Error" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}