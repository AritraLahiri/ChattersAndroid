package aritra.code.chatters;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import java.net.MalformedURLException;
import java.net.URL;

import aritra.code.chatters.databinding.ActivityFullNewsBinding;

public class FullNewsActivity extends AppCompatActivity {

    ActivityFullNewsBinding binding;
    String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        Intent intent = getIntent();
        webUrl = intent.getStringExtra("url");
        String providerName = intent.getStringExtra("name");




        if (webUrl != null) {
            binding.webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
            binding.webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.webView.setVisibility(View.VISIBLE);
                }
            });
            binding.webView.loadUrl(webUrl);
        }
    }




}