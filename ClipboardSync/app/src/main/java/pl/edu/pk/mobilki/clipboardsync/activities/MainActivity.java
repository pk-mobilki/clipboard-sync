package pl.edu.pk.mobilki.clipboardsync.activities;

import android.content.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.cedric.clipboardsync.R;
import pl.edu.pk.mobilki.clipboardsync.database.ClipboardDbAdapter;
import pl.edu.pk.mobilki.clipboardsync.services.ClipboardChecker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private ArrayAdapter<String> adapter;
    private List<String> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toast.makeText( getApplicationContext(), "On Create", Toast.LENGTH_SHORT).show();

        SetupToolbar();
        SetupHistoryList();
        SetupClipboardChecker();
    }

    private void CheckServer()
    {
        TextView notConnectedText = (TextView) this.findViewById(R.id.noServerText);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String serverIp = prefs.getString("server_ip", "");
        String serverPort = prefs.getString("server_port", "");

        if (serverIp.isEmpty() || serverPort.isEmpty())
        {
            notConnectedText.setVisibility(View.VISIBLE);
        }
        else
        {
            notConnectedText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        ClipboardDbAdapter dbAdapter = new ClipboardDbAdapter(getApplicationContext());
        dbAdapter.open();
        ArrayList<String> clipboards = dbAdapter.getAllClipboards();
        dbAdapter.close();

        //Toast.makeText( getApplicationContext(), "On start" + clipboards, Toast.LENGTH_SHORT).show();

        historyList.clear();
        historyList.addAll(clipboards);
        adapter.notifyDataSetChanged();

        CheckServer();
    }

    private void SetupClipboardChecker()
    {
        ComponentName service = startService(new Intent(getApplicationContext(), ClipboardChecker.class));
    }

    private void SetupHistoryList()
    {
        ListView myListView = (ListView) this.findViewById(R.id.historyList);
        historyList = new ArrayList<String>();

        TextView emptyText = (TextView)findViewById(R.id.list_empty_text);
        myListView.setEmptyView(emptyText);

        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, historyList);
        myListView.setAdapter(adapter);
    }

    private void SetupToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }
        else if (id == R.id.action_about)
        {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
