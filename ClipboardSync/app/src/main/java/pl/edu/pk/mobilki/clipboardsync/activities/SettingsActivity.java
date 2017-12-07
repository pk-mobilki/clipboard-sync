package pl.edu.pk.mobilki.clipboardsync.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.*;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.cedric.clipboardsync.R;
import pl.edu.pk.mobilki.clipboardsync.database.ClipboardDbAdapter;
import pl.edu.pk.mobilki.clipboardsync.sync.TcpClient;

import java.net.InetAddress;
import java.util.List;

public class SettingsActivity extends AppCompatPreferenceActivity
{
    Context aplicationContext;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        aplicationContext = getApplicationContext();
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener()
    {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value)
        {
            String stringValue = value.toString();
            preference.setSummary(stringValue);

            return true;
        }
    };

    private static boolean isXLargeTablet(Context context)
    {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static void bindPreferenceSummaryToValue(Preference preference)
    {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        String pref = PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), "");

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, pref);
    }


    @Override
    public boolean onIsMultiPane()
    {
        return isXLargeTablet(this);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target)
    {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    protected boolean isValidFragment(String fragmentName)
    {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference("server_ip"));
            bindPreferenceSummaryToValue(findPreference("server_port"));

            // Search for PC button
            Preference button = findPreference("search_PC");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                @Override
                public boolean onPreferenceClick(Preference preference)
                {
                    new AsyncTask<Void, Void, Void>()
                    {
                        ProgressDialog dialog;

                        Boolean wifiConneced = false;
                        String detectedServerIpAddress;

                        @Override
                        protected void onPreExecute()
                        {
                            String scanningString = getResources().getString(R.string.scanning);
                            dialog = ProgressDialog.show(getContext(), "", scanningString, true);
                        }

                        @Override
                        protected Void doInBackground( final Void ... params )
                        {
                            try
                            {
                                ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                                if (activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                                {
                                    wifiConneced = true;

                                    WifiManager wManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                    WifiInfo wInfo = wManager.getConnectionInfo();
                                    int ipAddress = wInfo.getIpAddress();
                                    String ipStr =
                                            String.format("%d.%d.%d.%d",
                                                    (ipAddress & 0xff),
                                                    (ipAddress >> 8 & 0xff),
                                                    (ipAddress >> 16 & 0xff),
                                                    (ipAddress >> 24 & 0xff));

                                    InetAddress ipFormal = InetAddress.getByName(ipStr);
                                    byte[] ip = ipFormal.getAddress();

                                    for (int i = 1; i <= 254; i++)
                                    {
                                        try
                                        {
                                            ip[3] = (byte)i;
                                            InetAddress address = InetAddress.getByAddress(ip);

                                            String output = address.toString().substring(1);

                                            Log.e("MESSAGE", "Scanning: " + output);

                                            if (TcpClient.isPortOpen(output, 40004, 20))
                                            {
                                                detectedServerIpAddress = output;
                                                return null;
                                            }
                                        }
                                        catch (Exception ex)
                                        {
                                            Log.e("ERROR", ex.getMessage());
                                        }
                                    }
                                    Thread.sleep(3000);
                                }
                                else
                                {
                                    wifiConneced = false;
                                }
                            }
                            catch (Exception ex)
                            {
                                Log.e("ERROR", ex.getMessage());
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute( final Void result )
                        {
                            if (!wifiConneced)
                            {
                                String notConnectedString = getResources().getString(R.string.not_connected_to_wifi);
                                Toast.makeText( getContext(), notConnectedString, Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();

                            if (detectedServerIpAddress != null && !detectedServerIpAddress.isEmpty())
                            {
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("server_ip", detectedServerIpAddress);
                                editor.putString("server_port", "40004");
                                editor.apply();

                                EditTextPreference serverIpPreference = (EditTextPreference) findPreference("server_ip");
                                serverIpPreference.setSummary(detectedServerIpAddress);

                                EditTextPreference serverPortPreference = (EditTextPreference) findPreference("server_port");
                                serverPortPreference.setSummary("40004");
                            }
                            else
                            {
                                String NoPCFound = getResources().getString(R.string.no_pc_found);
                                Toast.makeText( getContext(), NoPCFound, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute();

                    return true;
                }
            });

            // Clear database button
            button = findPreference("clear_database");
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                @Override
                public boolean onPreferenceClick(Preference preference)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    String areYouSureString = getResources().getString(R.string.are_you_sure);
                    String yesString = getResources().getString(R.string.yes);
                    String noString = getResources().getString(R.string.no);
                    builder.setMessage(areYouSureString).setPositiveButton(yesString, dialogClickListener).setNegativeButton(noString, dialogClickListener).show();
                    return true;
                }
            });
        }

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        ClipboardDbAdapter adapter = new ClipboardDbAdapter(getContext());

                        adapter.open();
                        adapter.clearDatabase();
                        adapter.close();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            int id = item.getItemId();
            if (id == android.R.id.home)
            {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            SwitchPreference swPref = (SwitchPreference) findPreference("notifications_clipboard");
            swPref.setSummary(R.string.notifications_summary);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            int id = item.getItemId();
            if (id == android.R.id.home)
            {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
