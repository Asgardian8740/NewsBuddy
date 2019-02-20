package in.akshay.newsbuddy;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.akshay.newsbuddy.database.DBManager;
import in.akshay.newsbuddy.database.DatabaseHelper;

public class Bookmarked extends AppCompatActivity {
    private DBManager dbManager;


    final String[] from =   new String[] { DatabaseHelper._ID,DatabaseHelper.A_ID, DatabaseHelper.AUTHOR, DatabaseHelper.SNAME, DatabaseHelper.TITLE,DatabaseHelper.DESC,
            DatabaseHelper.URL,DatabaseHelper.URLToIMAGE,DatabaseHelper.PUBLISHEDAt };

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked);

            dbManager = new DBManager(this);
            dbManager.open();
            Cursor cursor = dbManager.fetch();
    }












}
