package com.maurya91.newsfeed;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maurya91.newsfeed.model.GameFeed;
import com.maurya91.newsfeed.utils.MkUtils;
import com.maurya91.newsfeed.wrapper.GameFeedWrapper;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DetailNewsActivity extends AppCompatActivity {
    private String mNewsFeedUrl,mNewsFeedImageUrl;
    private TextView mDetailNewsText,mDetailNewsTitleText,mDetailNewsDescriptionText,mDetailNewsAuthorText;
    private ImageView mNewsFeedImageView;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         if (getIntent().hasExtra("FEED_URL")){
             mNewsFeedUrl=getIntent().getStringExtra("FEED_URL");
             mNewsFeedImageUrl=getIntent().getStringExtra("FEED_IMAGE_URL");
         }
        Log.d("Received",""+mNewsFeedUrl);
        mProgressBar= (ProgressBar) findViewById(R.id.progressBarDetailNews);
        mDetailNewsText= (TextView) findViewById(R.id.detailNews);
        mDetailNewsTitleText= (TextView) findViewById(R.id.detailNewsTitle);
        mDetailNewsDescriptionText= (TextView) findViewById(R.id.detailNewsDescription);
        mDetailNewsAuthorText= (TextView) findViewById(R.id.detailNewsAuthor);
        mNewsFeedImageView= (ImageView) findViewById(R.id.detailNewsImage);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        if (getSupportActionBar()!=null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (MkUtils.isInternetConnected(this)){
        new DetailNewsFetchWorker().execute(mNewsFeedUrl);}
        else {
            finish();
            Toast.makeText(DetailNewsActivity.this, "No Intenet Conection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,"For more visit : "+ mNewsFeedUrl);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    class DetailNewsFetchWorker extends AsyncTask<String,Void,GameFeed>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mFab.hide();

        }

        @Override
        protected GameFeed doInBackground(String... params) {
            int timeOut=10*1000;
            try {
                Document doc  = Jsoup.connect(params[0]).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").referrer("http://www.google.com").timeout(timeOut).get();
                return new GameFeedWrapper().getDetailNewsFeed(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(GameFeed gameFeed) {
            super.onPostExecute(gameFeed);
            mProgressBar.setVisibility(View.GONE);
            mFab.show();
            if (gameFeed!=null) {
                mDetailNewsText.setText(gameFeed.getDetailNews());
                mDetailNewsTitleText.setText(gameFeed.getTitle());
                mDetailNewsDescriptionText.setText(gameFeed.getDescriptionText());
                mDetailNewsAuthorText.setText(gameFeed.getAuthor());
                String url;
                if (gameFeed.getImageUrl().equalsIgnoreCase(""))
                url=mNewsFeedImageUrl;
                else
                url=gameFeed.getImageUrl();
                Picasso.with(DetailNewsActivity.this).load(Uri.parse(url)).resize(320,340).placeholder(R.drawable.placeholder_image).into(mNewsFeedImageView);
            }else{
            Snackbar.make(mProgressBar,"Feeds not Found! Please try again later.",Snackbar.LENGTH_INDEFINITE).setAction("EXIT", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).show();
        }
        }
    }
}
