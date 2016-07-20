package com.maurya91.newsfeed;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maurya91.newsfeed.model.GameFeed;
import com.maurya91.newsfeed.utils.MkUtils;
import com.maurya91.newsfeed.wrapper.GameFeedWrapper;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    private GameFeedAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProgressBar= (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter= new GameFeedAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                updateFeed();
                startActivity(new Intent(MainActivity.this,NewReleasesActivity.class));
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
            }
        });
        //fab.hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
           navigationView.setNavigationItemSelectedListener(this);
       updateFeed();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            updateFeed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(MainActivity.this,NewReleasesActivity.class));
        } else if (id == R.id.nav_gallery) {
           // startActivity(new Intent(MainActivity.this,NewReleasesActivity.class));
        } else if (id == R.id.nav_slideshow) {
           // startActivity(new Intent(MainActivity.this,NewReleasesActivity.class));
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    class DataFetchWorker extends AsyncTask<Void,Void,ArrayList<GameFeed>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<GameFeed> doInBackground(Void... params) {
          int timeOut=10*1000;
            try {
                Document doc  = Jsoup.connect(GameFeedWrapper.getSite()+"/news/").userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").referrer("http://www.google.com").timeout(timeOut).get();
                return new GameFeedWrapper().getFeedList(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<GameFeed> gameFeeds) {
            super.onPostExecute(gameFeeds);
            mProgressBar.setVisibility(View.GONE);
            if (gameFeeds!=null){
                 mAdapter.updateList(gameFeeds);

            }else{
                Snackbar.make(mProgressBar,"Feeds not Found! Please try again later.",Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      updateFeed();
                    }
                }).show();
            }


        }
    }

    private void updateFeed() {
        if (MkUtils.isInternetConnected(this)){
        new DataFetchWorker().execute();
        }else {
            Snackbar.make(mRecyclerView,"No Internet Connection! Please try again later.",Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateFeed();
                }
            }).show();
        }
    }
    private void mk(String s){
        Log.d("MK",">> "+s);
    }

    class GameFeedAdapter extends RecyclerView.Adapter<GameFeedAdapter.MyViewHolder>{
        ArrayList<GameFeed> mFeedList= new ArrayList<>();
        Context mContext;
        public GameFeedAdapter(Context context) {
            this.mContext=context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_layout,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            GameFeed feed= mFeedList.get(position);
             holder.feedTitleText.setText(feed.getTitle());
              //holder.feedTimeText.setText(feed.getTime());
            if (feed.getDescriptionText().equalsIgnoreCase(""))
            {    // Feed description not found hide that
                holder.feedDescriptionText.setVisibility(View.GONE);
            }else {
                //show
                holder.feedDescriptionText.setVisibility(View.VISIBLE);
                holder.feedDescriptionText.setText(feed.getDescriptionText());
            }
            holder.feedTextTimeText.setText(feed.getTextTime());
            Picasso.with(mContext).load(Uri.parse(feed.getImageUrl())).placeholder(R.drawable.placeholder_image).into(holder.feedImageView);
        }

        @Override
        public int getItemCount() {
            return mFeedList==null?0:mFeedList.size();
        }
        public void updateList(ArrayList<GameFeed> list){
          this.mFeedList=list;
            notifyDataSetChanged();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView feedTitleText,feedTextTimeText,feedTimeText,feedDescriptionText;
            ImageView feedImageView;
            public MyViewHolder(View itemView) {
                super(itemView);
                feedImageView = (ImageView) itemView.findViewById(R.id.feedImage);
                feedTitleText= (TextView) itemView.findViewById(R.id.feedTitle);
                feedTextTimeText= (TextView) itemView.findViewById(R.id.feedTextTime);
               // feedTimeText= (TextView) itemView.findViewById(R.id.feedTime);
                itemView.setOnClickListener(this);
                feedDescriptionText= (TextView) itemView.findViewById(R.id.feedDesc);
            }

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,DetailNewsActivity.class);
                intent.putExtra("FEED_URL",mFeedList.get(getAdapterPosition()).getFeedUrl());
                intent.putExtra("FEED_IMAGE_URL",mFeedList.get(getAdapterPosition()).getImageUrl());
                startActivity(intent);
            }
        }
    }
}
