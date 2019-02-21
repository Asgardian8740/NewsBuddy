package in.akshay.newsbuddy.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "COUNTRIES";

    // Table columns


    public static final String _ID= "_id";
    public static final String AUTHOR= "author";
    public static final String SNAME= "source";
    public static final String TITLE= "title";
    public static final String DESC= "description";
    public static final String URL= "url";
    public static final String PUBLISHEDAt= "publishedAt";
    public static final String DATE= "date";
    public static final String CONTENT= "content";



    public static final String SUBJECT = "subject";

    // Database Information
    static final String DB_NAME = "OFFLINE.DB";

    // database version
    static final int DB_VERSION = 3;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + AUTHOR + " VARCHAR, " + SNAME + " VARCHAR, " + TITLE + " VARCHAR, " + DESC + " VARCHAR, " + URL + " VARCHAR, " + PUBLISHEDAt + " VARCHAR, " + DATE + " VARCHAR, " + CONTENT + " VARCHAR);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
