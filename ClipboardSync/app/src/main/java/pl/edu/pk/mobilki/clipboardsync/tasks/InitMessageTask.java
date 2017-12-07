package pl.edu.pk.mobilki.clipboardsync.tasks;

import android.os.AsyncTask;
import pl.edu.pk.mobilki.clipboardsync.sync.TcpClient;

public class InitMessageTask extends AsyncTask<String, String, String>
{
    @Override
    protected String doInBackground(String... strings)
    {
        TcpClient sender = new TcpClient(strings[0], strings[1]);
        sender.sendMessage(strings[2]);
        sender.stopClient();
        return null;
    }
}