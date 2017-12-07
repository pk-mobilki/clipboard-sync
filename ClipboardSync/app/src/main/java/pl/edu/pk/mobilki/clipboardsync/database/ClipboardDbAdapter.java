package pl.edu.pk.mobilki.clipboardsync.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ClipboardDbAdapter
{
    private static final String DATABASE_NAME = "clipboard.sqlite";
    private static final String CLIPBOARD_TABLE_NAME = "clipboard";
    private static final int DB_VERSION = 2;
    private static final String KEY_ID = "_id";
    private static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String KEY_DESCRIPTION = "description";
    private static final String DESCRIPTION_OPTIONS = "TEXT NOT NULL";

    private static final String DB_CREATE_CLIPBOARD_TABLE =
            "CREATE TABLE " + CLIPBOARD_TABLE_NAME + "( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_DESCRIPTION + " " + DESCRIPTION_OPTIONS +
                    ");";
    private static final String DROP_TODO_TABLE =
            "DROP TABLE IF EXISTS " + CLIPBOARD_TABLE_NAME;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;


    public ClipboardDbAdapter(Context context)
    {
        this.context = context;
    }

    public ClipboardDbAdapter open()
    {
        dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DB_VERSION);
        try
        {
            db = dbHelper.getWritableDatabase();
        }
        catch (SQLException e)
        {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close()
    {
        dbHelper.close();
    }

    public long insertClipboard(String description)
    {
        ContentValues newTodoValues = new ContentValues();
        newTodoValues.put(KEY_DESCRIPTION, description);
        return db.insert(CLIPBOARD_TABLE_NAME, null, newTodoValues);
    }

    public ArrayList<String> getAllClipboards()
    {
        ArrayList<String> list = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + CLIPBOARD_TABLE_NAME;

        try
        {
            Cursor cursor = db.rawQuery(selectQuery, null);
            try
            {
                if (cursor.moveToFirst())
                {
                    do
                    {
                        String obj = cursor.getString(1);

                        list.add(obj);
                    }
                    while (cursor.moveToNext());
                }

            }
            finally
            {
                try
                {
                    cursor.close();
                }
                catch (Exception ignore)
                {
                }
            }
        }
        finally
        {
            try
            {
                db.close();
            }
            catch (Exception ignore)
            {
            }
        }
        return list;
    }

    public void clearDatabase()
    {
        String deleteQuery = "DELETE FROM " + CLIPBOARD_TABLE_NAME;

        try
        {
            db.execSQL(deleteQuery);
        }
        finally
        {
            try
            {
                db.close();
            }
            catch (Exception ignore)
            {
            }
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        public DatabaseHelper(Context context, String name,
        SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DB_CREATE_CLIPBOARD_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL(DROP_TODO_TABLE);

            onCreate(db);
        }
    }
}
