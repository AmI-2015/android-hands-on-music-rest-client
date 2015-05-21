package it.polito.elite.mremote2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.net.MalformedURLException;
import java.net.URL;

import it.polito.elite.mremote2.fragments.OnTrackSelectedListener;
import it.polito.elite.mremote2.fragments.TrackFragment;
import it.polito.elite.mremote2.fragments.TrackListFragment;

public class HomeActivity extends AppCompatActivity implements OnTrackSelectedListener
{
    // the key for the endpoint
    public static final String API_ENDPOINT = "api_endpoint";
    // the base URL
    private String apiEndpoint = "http://192.168.56.1:8081/music/api/v1.0/";

    //the endpoint request code
    public static final int ENDPOINT_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // get the shared preferences if available
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        this.apiEndpoint = preferences.getString(HomeActivity.API_ENDPOINT, this.apiEndpoint);

        //set-up the action bar using an icon
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //set the "home" acvtivity layout
        setContentView(R.layout.activity_home);

        //prepare for loading 2 fragments (summary / detail) for this activity

        //check that actually the loaded layout contains a FrameLayou entry having a "fragment_container" id
        if (findViewById(R.id.fragment_container) != null)
        {
            //if this activity is restored from a previous state do not do
            //anything to avoid generating overlapping fragments
            if (savedInstanceState != null)
            {
                return;
            }

            //create the initial fragment
            this.createInitialFragment();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //get the option item id
        int id = item.getItemId();

        //check if corresponds to the setting menu
        if (id == R.id.action_settings)
        {
            //open the settings activity
            Intent settingsActivity = new Intent(this, Settings.class);

            //add the current API endpoint as extra
            settingsActivity.putExtra(HomeActivity.API_ENDPOINT, this.apiEndpoint);

            //start the activity for result
            this.startActivityForResult(settingsActivity, HomeActivity.ENDPOINT_REQUEST);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //get the data
        if (requestCode == HomeActivity.ENDPOINT_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                String endpoint = data.getExtras().get(HomeActivity.API_ENDPOINT).toString();

                if ((endpoint != null) && (!endpoint.isEmpty()))
                {
                    try
                    {
                        //should check if a valid url
                        URL url = new URL(endpoint);

                        //ok valid, can be storef
                        this.apiEndpoint = endpoint;

                        //save the shared preferences
                        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(HomeActivity.API_ENDPOINT,endpoint);
                        editor.commit();

                        //update the currently viewed fragment
                        this.createInitialFragment();

                        //update shared preferences
                    } catch (MalformedURLException e)
                    {
                        //do nothing
                    }
                }
            }
        }
    }

    @Override
    public void onTrackSelected(int position, int trackId)
    {
        //handle fragment change
        TrackFragment tFragment = new TrackFragment();

        //prepare for transaction
        Bundle args = new Bundle();

        //add an argument to "pass" to the fragment the position of the selected item
        args.putInt(TrackFragment.ARG_POSITION, position);
        args.putInt(TrackFragment.ARG_TRACK_ID, trackId);
        args.putString(HomeActivity.API_ENDPOINT, this.apiEndpoint);
        //set the argument
        tFragment.setArguments(args);

        //start the fragment transaction
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();

        //replace the fragment
        transaction.replace(R.id.fragment_container, tFragment);

        //add the old fragment to the backward stack
        transaction.addToBackStack(null);

        //commit the transaction
        transaction.commit();

    }

    private void createInitialFragment()
    {
        //create a new fragment instance
        TrackListFragment firstFragment = new TrackListFragment();

        //in case this activity is created from an Intent with accompanying data, pass the
        //data to the fragment
        if(this.getIntent().getExtras()!=null)
            firstFragment.setArguments(this.getIntent().getExtras());
        else //otherwise create a new bundle for holding arguments
            firstFragment.setArguments(new Bundle());

        //set the fragment arguments
        firstFragment.getArguments().putString(HomeActivity.API_ENDPOINT, this.apiEndpoint);

        //add the fragment to the frame container layout

        //check if a fragment already exist (allow only one fragment at time)
        if(this.getSupportFragmentManager().getFragments()!=null)
        {
            //start the fragment transaction
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();

            //replace the fragment
            transaction.replace(R.id.fragment_container, firstFragment);

            //commit the transaction
            transaction.commit();
        }
        else
            this.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
    }
}
