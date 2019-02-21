package in.akshay.newsbuddy;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.shashank.sony.fancytoastlib.FancyToast;

import in.akshay.newsbuddy.database.DBManager;
import in.akshay.newsbuddy.database.DatabaseHelper;
import in.akshay.newsbuddy.model.article;

public class Bookmarked extends AppCompatActivity {
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
     Cursor cursor;

    final String[] from = new String[]{DatabaseHelper.PUBLISHEDAt, DatabaseHelper.AUTHOR, DatabaseHelper.SNAME, DatabaseHelper.TITLE, DatabaseHelper.DESC, DatabaseHelper.DATE};

    final int[] to = new int[]{R.id.id,R.id.author, R.id.source, R.id.title, R.id.desc, R.id.publishedAt};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dbManager = new DBManager(this);
        dbManager.open();
        cursor = dbManager.fetch();

        final SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.empty));


        adapter = new SimpleCursorAdapter(this, R.layout.item_bookmark, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);


        final SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(230,
                        230, 230)));
                // set item width
                deleteItem.setWidth(200);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        View childView = listView.getChildAt(position);
                        TextView authordate = (TextView) childView.findViewById(R.id.id);
                        dbManager.delete(authordate.getText().toString());
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        break;

                }


                return false;
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(isNetworkAvailable()) {
                    Intent myIntent = new Intent(Bookmarked.this, MainActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myIntent);
                }else{
                    FancyToast.makeText(this,"Connect to Internet First !",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}
