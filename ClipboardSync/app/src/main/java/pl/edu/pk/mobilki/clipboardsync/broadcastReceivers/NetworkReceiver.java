package pl.edu.pk.mobilki.clipboardsync.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.widget.Toast;
import pl.edu.pk.mobilki.clipboardsync.tasks.SenderTask;

public class NetworkReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            String serverIp = prefs.getString("server_ip", "");
            String serverPort = prefs.getString("server_port", "");

            String message = "&INIT&" + Base64.encodeToString(android.os.Build.MODEL.getBytes(), Base64.NO_WRAP) + "&STOP&";

            try
            {
                SenderTask task = new SenderTask();
                task.execute(serverIp, serverPort, message);
            }
            catch (Exception E)
            {

            }
        }
        else
        {
            Toast.makeText( context, "NO YAY", Toast.LENGTH_SHORT).show();
        }
    }
}