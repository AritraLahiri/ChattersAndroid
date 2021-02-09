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

import aritra.code.chatters.Adapters.VideoAdapter;
import aritra.code.chatters.ApiInterface;
import aritra.code.chatters.Models.DummyPOJO;
import aritra.code.chatters.Models.JsonResponse;
import aritra.code.chatters.RetrofitInstance;
import aritra.code.chatters.databinding.FragmentGroupsBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GroupsFragment extends Fragment {

    FragmentGroupsBinding binding;
    ArrayList<DummyPOJO> list = new ArrayList<>();
    int totalCount, currentCount, countOutOfVisible, totalApiResults;
    Boolean isScrolling = false;
    String nextPageToken = "";
    LinearLayoutManager linearLayoutManager;
    private VideoAdapter adapter;
    private ApiInterface apiInterface;


    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGroupsBinding.inflate(inflater, container, false);
        adapter = new VideoAdapter(getContext(), list);
        linearLayoutManager = new LinearLayoutManager(getContext());
        binding.groupRecyclerView.setAdapter(adapter);
        binding.groupRecyclerView.setLayoutManager(linearLayoutManager);
        binding.groupRecyclerView.setAdapter(adapter);
        fetchVideos(nextPageToken);

        binding.groupRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                currentCount = linearLayoutManager.getChildCount();
                totalCount = linearLayoutManager.getItemCount();
                countOutOfVisible = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentCount + countOutOfVisible) >= totalCount) {
                    isScrolling = false;
                    fetchVideos(nextPageToken);
                }


            }
        });


        return binding.getRoot();
    }


    private void fetchVideos(String pageToken) {


        apiInterface = RetrofitInstance.getRetrofit().create(ApiInterface.class);
        apiInterface.getPosts(pageToken).enqueue(new Callback<JsonResponse>() {

            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                JsonResponse jsonResponse = response.body();
                nextPageToken = jsonResponse.getNextPageToken();
                totalApiResults = jsonResponse.getTotalResult();
                list.addAll(jsonResponse.getItemsArray());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


        return;
    }


}