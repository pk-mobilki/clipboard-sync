package pl.edu.pk.mobilki.clipboardsync.tasks;

import android.content.Context;
import android.os.AsyncTask;

public class NetworkScanningTask extends AsyncTask<String, String, String>
{
    Boolean found = false;
    Context context;

    //initiate vars
    public NetworkScanningTask(Context con, InformComplete callback)
    {
        super();
        this.context = con;
    }

    @Override
    protected void onPreExecute()
    {
        // set up your dialog
    }

    @Override
    protected String doInBackground(String... strings)
    {
        found = true;


        return null;
    }


    public interface InformComplete
    {
        public void PostData(String ip, String port);
    }
}