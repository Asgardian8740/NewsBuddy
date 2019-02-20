package in.akshay.newsbuddy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static in.akshay.newsbuddy.database.DatabaseHelper.A_ID;
import static in.akshay.newsbuddy.database.DatabaseHelper.TABLE_NAME;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String author,String aid, String sname, String title, String description, String url, String urlToImage, String publishedAt) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.A_ID, aid);
        contentValue.put(DatabaseHelper.AUTHOR, author);
        contentValue.put(DatabaseHelper.SNAME, sname);
        contentValue.put(DatabaseHelper.TITLE, title);
        contentValue.put(DatabaseHelper.DESC, description);
        contentValue.put(DatabaseHelper.URL, url);
        contentValue.put(DatabaseHelper.URLToIMAGE, urlToImage);
        contentValue.put(DatabaseHelper.PUBLISHEDAt, publishedAt);
        database.insert(TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID,DatabaseHelper.A_ID, DatabaseHelper.AUTHOR, DatabaseHelper.SNAME, DatabaseHelper.TITLE,DatabaseHelper.DESC,
                DatabaseHelper.URL,DatabaseHelper.URLToIMAGE,DatabaseHelper.PUBLISHEDAt };
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id,String aid,String author, String sname, String title, String description, String url, String urlToImage, String publishedAt) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.A_ID, aid);
        contentValues.put(DatabaseHelper.AUTHOR, author);
        contentValues.put(DatabaseHelper.SNAME, sname);
        contentValues.put(DatabaseHelper.TITLE, title);
        contentValues.put(DatabaseHelper.DESC, description);
        contentValues.put(DatabaseHelper.URL, url);
        contentValues.put(DatabaseHelper.URLToIMAGE, urlToImage);
        contentValues.put(DatabaseHelper.PUBLISHEDAt, publishedAt);
        int i = database.update(TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }






    public void delete(long _id) {
        database.delete(TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

    public boolean ifNumberExists(String strNumber)
    {
        Cursor cursor = null;
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE  " + A_ID + " = '" + strNumber +"'";
        cursor= database.rawQuery(selectQuery,null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }



}
