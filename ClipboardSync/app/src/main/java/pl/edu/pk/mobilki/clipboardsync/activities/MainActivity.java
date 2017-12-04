package pl.edu.pk.mobilki.clipboardsync.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import pl.edu.pk.mobilki.clipboardsync.R;
import pl.edu.pk.mobilki.clipboardsync.activities.data.DataManagerImpl;

public class MainActivity extends AppCompatActivity
{

    private DataManagerImpl dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataManager = new DataManagerImpl(this);
    }
}
