package pl.edu.pk.mobilki.clipboardsync.activities.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.edu.pk.mobilki.clipboardsync.activities.data.clipboard.ClipboardTabla;

/**
 * Created by mati on 2017-12-04.
 */

public class OpenHelper extends SQLiteOpenHelper {
    private static OpenHelper instance;
    private Context context;
    private static final int DATABASE_VERSION = 1;

    public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }


    @Override
    public void onCreate(final SQLiteDatabase db) {
        ClipboardTabla.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        ClipboardTabla.onCreate(db);
    }
}
