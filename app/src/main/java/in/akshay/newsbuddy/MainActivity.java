package in.akshay.newsbuddy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.google.android.gms.location.LocationCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import in.akshay.newsbuddy.Notification.Notification_Receiver;
import in.akshay.newsbuddy.api.Retrofitclient;
import in.akshay.newsbuddy.api.RetroInterface;
import in.akshay.newsbuddy.model.article;
import in.akshay.newsbuddy.model.newsmodel;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, Listener {

    public static final String API_KEY = "38d3d4708967467e9f04937c7547b833";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<article> articles = new ArrayList<>();
    private Adapter adapter;
    private String TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private String countrycd="in";
    LinearLayout catlay;
    int k = 0;
    TextView buss, tech, sports, enter, sci, gen, health;
    String category = "general";
    String sortby = "publishedAt";
    EasyWayLocation easyWayLocation;
    int j = 0;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!isNetworkAvailable()) {
            FancyToast.makeText(this, "Internet Not Available,Redirected to saved articles", FancyToast.LENGTH_LONG, FancyToast.WARNING, true);
            Intent intent = new Intent(MainActivity.this, Bookmarked.class);
            startActivity(intent);
            return;
        }


        catlay = (LinearLayout) findViewById(R.id.layoutcat);

        buss = (TextView) findViewById(R.id.business);
        buss.setOnClickListener(this);

        tech = (TextView) findViewById(R.id.technology);
        tech.setOnClickListener(this);

        sports = (TextView) findViewById(R.id.sports);
        sports.setOnClickListener(this);

        enter = (TextView) findViewById(R.id.entertainment);
        enter.setOnClickListener(this);

        sci = (TextView) findViewById(R.id.science);
        sci.setOnClickListener(this);

        gen = (TextView) findViewById(R.id.general);
        gen.setBackground(getDrawable(R.drawable.ic_catbg));
        gen.setOnClickListener(this);

        health = (TextView) findViewById(R.id.health);
        health.setOnClickListener(this);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);


        boolean requireFineGranularity = false;

        bundle = getIntent().getExtras();
        if(bundle!=null){
            j=bundle.getInt("keyv");
        }


        easyWayLocation = new EasyWayLocation(this, requireFineGranularity);
        easyWayLocation.setListener(this);


        startAlarm(true, true);


    }


   /*public static void showDebugDBAddressLogToast(Context context) {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
                Method getAddressLog = debugDB.getMethod("getAddressLog");
                Object value = getAddressLog.invoke(null);
                Log.i("ip", String.valueOf(value));
                Toast.makeText(context, (String) value, Toast.LENGTH_LONG).show();
            } catch (Exception ignore) {

            }
        }
    }*/


    public void LoadJson(final String keyword) {

        swipeRefreshLayout.setRefreshing(true);
        String country = countrycd;
        RetroInterface service = Retrofitclient.getClient().create(RetroInterface.class);
        String language = Utils.getLanguage();


        if (keyword.length() > 0) {
            final Dialog dialog = new Dialog(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.setContentView(R.layout.dialogue);
            final TextView rel = (TextView) dialog.findViewById(R.id.relevance);
            final TextView pubat = (TextView) dialog.findViewById(R.id.publishedAt);
            final TextView popu = (TextView) dialog.findViewById(R.id.popularity);

            if (k == 1) {
                rel.setBackground(getDrawable(R.drawable.ic_sortbg));

            } else if (k == 2) {
                popu.setBackground(getDrawable(R.drawable.ic_sortbg));
            } else {
                pubat.setBackground(getDrawable(R.drawable.ic_sortbg));
            }
            rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    k = 1;
                    sortby = "relevancy";
                    dialog.dismiss();
                }
            });
            pubat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    k = 0;
                    sortby = "publishedAt";
                    dialog.dismiss();
                }
            });
            popu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    k = 2;
                    sortby = "popularity";
                    dialog.dismiss();
                }
            });
            dialog.show();


            catlay.setVisibility(View.GONE);

            service.getNewsSearch(keyword, language, sortby, "100", API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<newsmodel>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(newsmodel newsmodel) {
                            articles = new ArrayList<>(newsmodel.getArticle());

                        }


                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            adapter = new Adapter(articles, MainActivity.this);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            initListener();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });


        } else {

            service.getNews(country, category, "100", API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<newsmodel>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(newsmodel newsmodel) {
                            articles = new ArrayList<>(newsmodel.getArticle());

                        }


                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            adapter = new Adapter(articles, MainActivity.this);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            catlay.setVisibility(View.VISIBLE);
                            initListener();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
        }


    }


    @Override
    public void onRefresh() {
        LoadJson("");
    }

    private void onLoadingSwipeRefresh(final String keyword) {

        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson(keyword);
                    }
                }
        );

    }


    public void updatecd(EasyWayLocation location) {
        Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses.size() > 0) {
            countrycd = addresses.get(0).getCountryCode().toLowerCase();
        }

        LoadJson("");
    }


    @Override
    protected void onPause() {
        super.onPause();
        easyWayLocation.endUpdates();


    }

    @Override
    protected void onResume() {
        super.onResume();
        easyWayLocation.beginUpdates();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {

                    onLoadingSwipeRefresh(query);
                } else {
                    Toast.makeText(MainActivity.this, "Type more than two letters!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mynews:
                Intent intent = new Intent(MainActivity.this, Bookmarked.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        super.onSupportActionModeStarted(mode);
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.general:
                category = "general";
                gen.setBackground(getDrawable(R.drawable.ic_catbg));
                buss.setBackgroundColor(Color.WHITE);
                tech.setBackgroundColor(Color.WHITE);
                sports.setBackgroundColor(Color.WHITE);
                enter.setBackgroundColor(Color.WHITE);
                sci.setBackgroundColor(Color.WHITE);
                health.setBackgroundColor(Color.WHITE);
                onLoadingSwipeRefresh("");

                break;

            case R.id.business:
                category = "business";
                buss.setBackground(getDrawable(R.drawable.ic_catbg));
                gen.setBackgroundColor(Color.WHITE);
                tech.setBackgroundColor(Color.WHITE);
                sports.setBackgroundColor(Color.WHITE);
                enter.setBackgroundColor(Color.WHITE);
                sci.setBackgroundColor(Color.WHITE);
                health.setBackgroundColor(Color.WHITE);
                onLoadingSwipeRefresh("");

                break;


            case R.id.technology:
                category = "technology";
                tech.setBackground(getDrawable(R.drawable.ic_catbg));
                gen.setBackgroundColor(Color.WHITE);
                buss.setBackgroundColor(Color.WHITE);
                sports.setBackgroundColor(Color.WHITE);
                enter.setBackgroundColor(Color.WHITE);
                sci.setBackgroundColor(Color.WHITE);
                health.setBackgroundColor(Color.WHITE);
                onLoadingSwipeRefresh("");

                break;
            case R.id.sports:

                category = "sports";
                sports.setBackground(getDrawable(R.drawable.ic_catbg));
                gen.setBackgroundColor(Color.WHITE);
                buss.setBackgroundColor(Color.WHITE);
                tech.setBackgroundColor(Color.WHITE);
                enter.setBackgroundColor(Color.WHITE);
                sci.setBackgroundColor(Color.WHITE);
                health.setBackgroundColor(Color.WHITE);
                onLoadingSwipeRefresh("");

                break;
            case R.id.science:

                category = "science";
                sci.setBackground(getDrawable(R.drawable.ic_catbg));
                gen.setBackgroundColor(Color.WHITE);
                buss.setBackgroundColor(Color.WHITE);
                sports.setBackgroundColor(Color.WHITE);
                enter.setBackgroundColor(Color.WHITE);
                tech.setBackgroundColor(Color.WHITE);
                health.setBackgroundColor(Color.WHITE);
                onLoadingSwipeRefresh("");

                break;
            case R.id.entertainment:

                category = "entertainment";
                enter.setBackground(getDrawable(R.drawable.ic_catbg));
                gen.setBackgroundColor(Color.WHITE);
                buss.setBackgroundColor(Color.WHITE);
                sports.setBackgroundColor(Color.WHITE);
                tech.setBackgroundColor(Color.WHITE);
                sci.setBackgroundColor(Color.WHITE);
                health.setBackgroundColor(Color.WHITE);
                onLoadingSwipeRefresh("");

                break;
            case R.id.health:

                category = "health";
                health.setBackground(getDrawable(R.drawable.ic_catbg));
                gen.setBackgroundColor(Color.WHITE);
                buss.setBackgroundColor(Color.WHITE);
                sports.setBackgroundColor(Color.WHITE);
                tech.setBackgroundColor(Color.WHITE);
                sci.setBackgroundColor(Color.WHITE);
                tech.setBackgroundColor(Color.WHITE);
                onLoadingSwipeRefresh("");

                break;

            default:

                break;
        }

    }

    private void startAlarm(boolean isNotification, boolean isRepeat) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 00);


        myIntent = new Intent(MainActivity.this, Notification_Receiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);


        if (!isRepeat)
            manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 3000, pendingIntent);
        else
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void initListener() {

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView imageView = view.findViewById(R.id.img);
                Intent intent = new Intent(MainActivity.this, NewsInDetaik.class);

                article article = articles.get(position);
                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img", article.getUrlToImage());
                intent.putExtra("date", article.getPublishedAt());
                intent.putExtra("source", article.getSource().getName());
                intent.putExtra("author", article.getAuthor());

                Pair<View, String> pair = Pair.create((View) imageView, ViewCompat.getTransitionName(imageView));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this,
                        pair
                );


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, optionsCompat.toBundle());
                } else {
                    startActivity(intent);
                }

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void locationOn() {
        if(j==0) {
            FancyToast.makeText(this, "Location ON", FancyToast.INFO, FancyToast.LENGTH_SHORT, true).show();
        }

        easyWayLocation.beginUpdates();

        updatecd(easyWayLocation);

    }

    @Override
    public void onPositionChanged() {


    }

    @Override
    public void locationCancelled() {

        easyWayLocation.showAlertDialog(getString(R.string.app_name), getString(R.string.app_name), null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCATION_SETTING_REQUEST_CODE:
                easyWayLocation.onActivityResult(resultCode);
                break;
        }
    }
}