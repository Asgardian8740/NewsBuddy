package in.akshay.newsbuddy;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import in.akshay.newsbuddy.database.DBManager;
import in.akshay.newsbuddy.database.DatabaseHelper;

public class Bookmarked extends AppCompatActivity {
    private DBManager dbManager;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SimpleCursorAdapter adapter;
    private ListView listView;

    final String[] from = new String[]{DatabaseHelper._ID, DatabaseHelper.AUTHOR, DatabaseHelper.SNAME, DatabaseHelper.TITLE, DatabaseHelper.DESC, DatabaseHelper.DATE};

    final int[] to = new int[]{R.id.id,R.id.author, R.id.source, R.id.title, R.id.desc, R.id.publishedAt};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked);

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));
        listView.setAdapter(adapter);


        adapter = new SimpleCursorAdapter(this, R.layout.item_bookmark, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

    }


}
