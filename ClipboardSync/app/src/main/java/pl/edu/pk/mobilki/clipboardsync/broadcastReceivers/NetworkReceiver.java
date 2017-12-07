package pl.edu.pk.mobilki.clipboardsync.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
        {
            Toast.makeText( context, "YAY", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText( context, "NO YAY", Toast.LENGTH_SHORT).show();
        }
    }
}