package pl.edu.pk.mobilki.clipboardsync.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mati on 2017-12-04.
 */

public class DataManagerImpl {
    private Context context;
    private SQLiteDatabase db;


    private String DATABASE_NAME = "db";

    public DataManagerImpl(Context context) {
        this.context = context;

        SQLiteOpenHelper openHelper = new OpenHelper(this.context, DATABASE_NAME, null, 1);
        db = openHelper.getWritableDatabase();
    }
}

