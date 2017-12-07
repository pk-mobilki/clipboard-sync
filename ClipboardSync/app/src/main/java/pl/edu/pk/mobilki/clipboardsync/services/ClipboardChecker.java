package pl.edu.pk.mobilki.clipboardsync.services;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import pl.edu.pk.mobilki.clipboardsync.database.ClipboardDbAdapter;
import pl.edu.pk.mobilki.clipboardsync.tasks.SenderTask;

public class ClipboardChecker extends Service
{
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ClipboardManager.OnPrimaryClipChangedListener mPrimaryChangeListener = new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String serverIp = prefs.getString("server_ip", "");
                String serverPort = prefs.getString("server_port", "");
                Boolean sendNotification = prefs.getBoolean("notifications_clipboard", false);

                if (!serverIp.isEmpty() && !serverPort.isEmpty())
                {
                    try
                    {
                        String message = "&START&" + Base64.encodeToString(clipboard.getText().toString().getBytes(), Base64.NO_WRAP) + "&STOP&";

                        SenderTask task = new SenderTask();
                        task.execute(serverIp, serverPort, message);

                        ClipboardDbAdapter adapter = new ClipboardDbAdapter(getApplicationContext());
                        adapter.open();
                        adapter.insertClipboard(clipboard.getText().toString());

                        if (sendNotification)
                        {
                            Toast.makeText( getApplicationContext(), "Clipboard sent", Toast.LENGTH_SHORT).show();
                        }
                        adapter.close();
                    }
                    catch (Exception ex)
                    {
                        Log.e("ERROR", ex.getMessage() + ex.getStackTrace());
                    }
                }
            }
        };

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(mPrimaryChangeListener);

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
