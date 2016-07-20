package com.maurya91.newsfeed;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maurya91.newsfeed.model.NewRelease;
import com.maurya91.newsfeed.wrapper.GameFeedWrapper;
import com.maurya91.newsfeed.wrapper.NewReleaseWrapper;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class NewReleasesActivity extends AppCompatActivity {
   private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private NewReleaseAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_releases);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
           mProgressBar = (ProgressBar) findViewById(R.id.progressBarNewRelease);
           mRecyclerView= (RecyclerView) findViewById(R.id.recyclerViewNewRelease);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter= new NewReleaseAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();
        if (getSupportActionBar()!=null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new NewReleaseFetchWorker().execute();
    }

    class NewReleaseFetchWorker extends AsyncTask<Void,Void,ArrayList<NewRelease>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<NewRelease> doInBackground(Void... params) {
            int timeOut=10*1000;
            try {
                Document doc  = Jsoup.connect(GameFeedWrapper.getSite()).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").referrer("http://www.google.com").timeout(timeOut).get();
                return new NewReleaseWrapper().getNewReleaseList(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<NewRelease> newReleases) {
            super.onPostExecute(newReleases);
            mProgressBar.setVisibility(View.GONE);
            if (newReleases!=null)
                mAdapter.updateList(newReleases);
        }
    }
    class NewReleaseAdapter extends RecyclerView.Adapter<NewReleaseAdapter.MyViewHolder>{
        ArrayList<NewRelease> mNewReleaseList;
         Context mContext;
        public NewReleaseAdapter(Context context) {
           this.mContext=context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.new_release_recycler_row_layout,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
             NewRelease release=mNewReleaseList.get(position);
            holder.titleText.setText(release.getTitle());
            holder.releaseDateText.setText(release.getDate());
            Picasso.with(mContext).load(Uri.parse(release.getImageUrl())).placeholder(R.drawable.placeholder_image).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return mNewReleaseList==null?0:mNewReleaseList.size();
        }

        public void updateList(ArrayList<NewRelease> newReleases) {
            this.mNewReleaseList=newReleases;
            notifyDataSetChanged();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
           TextView titleText,releaseDateText;
            ImageView imageView;
            public MyViewHolder(View itemView) {
                super(itemView);
                imageView= (ImageView) itemView.findViewById(R.id.newReleaseImageView);
                titleText= (TextView) itemView.findViewById(R.id.newReleaseTitle);
                releaseDateText= (TextView) itemView.findViewById(R.id.newReleaseDate);
            }
        }
    }
}
