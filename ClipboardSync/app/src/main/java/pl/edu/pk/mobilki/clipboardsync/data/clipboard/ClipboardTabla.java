package pl.edu.pk.mobilki.clipboardsync.data.clipboard;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by mati on 2017-12-04.
 */

public class ClipboardTabla {
    public static final String TABLE_NAME = "clipboard";

    public static class ClipboardColumns implements BaseColumns
    {
        public static final String TEXT = "text";
        public static final String DATE_TIME = "date_time";
    }

    public static void onCreate(SQLiteDatabase db)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS " + ClipboardTabla.TABLE_NAME + "(");
        sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY,");
        sb.append(ClipboardColumns.TEXT + " TEXT,");
        sb.append(ClipboardColumns.DATE_TIME + " TEXT");
        sb.append(");");
        db.execSQL(sb.toString());
    }
}
