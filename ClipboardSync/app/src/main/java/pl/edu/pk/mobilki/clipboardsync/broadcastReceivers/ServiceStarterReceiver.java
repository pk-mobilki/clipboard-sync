package pl.edu.pk.mobilki.clipboardsync.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import pl.edu.pk.mobilki.clipboardsync.services.ClipboardChecker;

public class ServiceStarterReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Start service on device startup
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            ComponentName service = context.startService(new Intent(context, ClipboardChecker.class));
        }
    }
}
