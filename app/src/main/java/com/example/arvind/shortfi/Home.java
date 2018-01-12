package com.example.arvind.shortfi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

import static com.example.arvind.shortfi.R.id.player;

public class  Home extends AppCompatActivity {

    private RecyclerView recyclerView;
    NavigationTabBar navigationTabBar;
    ViewPager viewPager;
    DatabaseReference mref;
    LinearLayoutManager mlinearLayoutManager;
    static Home activity;
    private static final String TAG = "HOME_ACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        recyclerView = (RecyclerView) findViewById(R.id.relv);
        recyclerView.setHasFixedSize(true);
        Log.d(TAG, "ACT STARTED1");
        mlinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mlinearLayoutManager);
        mref = FirebaseDatabase.getInstance().getReference().child("/Uploads");
        final FirebaseRecyclerAdapter<Blog, BlogViewHolder> adapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class, R.layout.row, BlogViewHolder.class, mref
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
                viewHolder.setTitle(model.getName());
                Log.d(TAG, "INSIDE POPULATE");
                viewHolder.setDesc(model.getDesciption());
                viewHolder.setVid(model.getVideo());
            }
        };
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapter.getItemCount();
                int lastVisiblePosition =
                        mlinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }

        });


        recyclerView.setAdapter(adapter);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        if (viewPager == null)
            Log.d(TAG, "vp is null");
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        navigationTabBar = (NavigationTabBar) findViewById(R.id.nav_tb);
        final ArrayList<NavigationTabBar.Model> barModel = new ArrayList<>();

        barModel.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_account_circle_black_24dp),
                        R.color.colorPrimaryDark)
                        .title("Home")
                        .badgeTitle("NTB HOME")
                        .build()
        );

        barModel.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_account_circle_black_24dp),
                        R.color.colorPrimaryDark)
                        .title("Dashboard")
                        .badgeTitle("NTB DASH")
                        .build()
        );

        barModel.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_account_circle_black_24dp),
                        R.color.colorPrimaryDark).
                        title("Chat")
                        .badgeTitle("NTB CHAT")
                        .build()
        );

        barModel.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_account_circle_black_24dp),
                        R.color.colorPrimaryDark).
                        title("Profile")
                        .badgeTitle("NTB PROF")
                        .build()
        );

        navigationTabBar.setModels(barModel);
        navigationTabBar.setViewPager(viewPager, 0);

        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder implements EasyVideoCallback {
        TextView title, desc;
        EasyVideoPlayer vid;

        public BlogViewHolder(View itemView) {
            super(itemView);


        }


        public void setTitle(String tiltle) {
            title = (TextView) itemView.findViewById(R.id.title);
            title.setText(tiltle + "");
            Log.d(TAG, "TITLE TEXT IS:" + tiltle);
        }

        public void setDesc(String des) {
            desc = (TextView) itemView.findViewById(R.id.desc);
            desc.setText(des + "");
            Log.d(TAG, "DES SET:" + des);
        }

        public void setVid(String vurl) {
            Uri videourl = Uri.parse(vurl);
            vid = (EasyVideoPlayer) itemView.findViewById(player);
            vid.setCallback(this);
            vid.seekTo(vid.getCurrentPosition());
            vid.hideControls();
            vid.setSource(videourl);
        }


        @Override
        public void onStarted(EasyVideoPlayer player) {
        }

        @Override
        public void onPaused(EasyVideoPlayer player) {
        }

        @Override
        public void onPreparing(EasyVideoPlayer player) {

        }

        @Override
        public void onPrepared(EasyVideoPlayer player) {
            Log.d("EVP-Sample", "onPrepared()");
        }

        @Override
        public void onBuffering(int percent) {
            Log.d("EVP-Sample", "onBuffering(): " + percent + "%");
        }

        @Override
        public void onError(EasyVideoPlayer player, Exception e) {
            Log.d("EVP-Sample", "onError(): " + e.getMessage());

        }

        @Override
        public void onCompletion(EasyVideoPlayer player) {
            Log.d("EVP-Sample", "onCompletion()");
        }

        @Override
        public void onRetry(EasyVideoPlayer player, Uri source) {

        }

        @Override
        public void onSubmit(EasyVideoPlayer player, Uri source) {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.activity_user_details) {
            Intent intent = new Intent(Home.this, User_details.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.activity_add_videos) {
            Intent intent = new Intent(Home.this, Add_videos.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public static Home close() {
        return activity;
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {

        private MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                  Scroll frag1=new Scroll();
                    return frag1;
                case 1:
                    Scroll frag2=new Scroll();
                    return frag2;
                case 2:
                    Scroll frag3=new Scroll();
                    return frag3;
                case 3:
                    Scroll frag4=new Scroll();
                    return frag4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }


    }
}


