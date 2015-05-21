package it.polito.elite.mremote2.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.polito.elite.mremote2.HomeActivity;
import it.polito.elite.mremote2.R;
import it.polito.elite.mremote2.data.Track;
import it.polito.elite.mremote2.tasks.AsyncJsonGetter;
import it.polito.elite.mremote2.tasks.AsyncTrackPlayControl;
import it.polito.elite.mremote2.tasks.JsonGetterCompletionListener;


/**
 * A fragment class holding a list of items
 */
public class TrackListFragment extends ListFragment implements JsonGetterCompletionListener
{

    // the track selection listener
    protected OnTrackSelectedListener itemSelectedListener;

    // the array of tracks object
    private Track allTracks[] = new Track[0];

    //the rest end point
    private String apiEndpoint ="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        //create an asynchronous getter for JSON data
        AsyncJsonGetter loader = new AsyncJsonGetter(this);

        //get the API endpoint, passed to the fragment as a parameter
        String apiEndpoint = (String) this.getArguments().get(HomeActivity.API_ENDPOINT);

        //store the endpoint
        this.apiEndpoint = apiEndpoint;

        //async load the tracks
        loader.execute(this.apiEndpoint+"tracks");

        //inflate the UI
        return inflater.inflate(R.layout.fragment_track_list, container, false);
    }

    @Override
    public void onAttach(Activity activity) throws ClassCastException
    {
        super.onAttach(activity);

        //check that the "attached" activity implements the needed listener interface
        if (activity instanceof OnTrackSelectedListener)
        {
            this.itemSelectedListener = (OnTrackSelectedListener) activity;
        }
        else
        {
            //not compatible
            throw new ClassCastException(activity.toString()
                                                 + " must implement OnTrackSelectedListener");
        }
    }

    /**
     * Reacts to clicks on items belonging to the ListView passed as parameter
     * @param l
     * @param v
     * @param position
     * @param id
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        //send the event to the listener (i.e., the HomeActivity)
        this.itemSelectedListener.onTrackSelected(position, this.allTracks[position].getId());

        //trigger play of the selected track using an asynchronous task
        AsyncTrackPlayControl playControl = new AsyncTrackPlayControl();

        //exec the task
        playControl.execute(this.apiEndpoint+"player", "" + position);
    }

    /**
     * Called when the async loader has finished, handles tracks and places them into a ListView
     * @param object
     */
    @Override
    public void onTracksLoadingComplete(JSONObject object)
    {
        //check that something has been retrieved
        if (object != null)
        {
            //prepare the set of tracks to inster in the ListView
            List<Track> tracks = new ArrayList<Track>();

            try
            {
                //access the "tracks" section of the retrieved json
                JSONArray singleTracks = object.getJSONArray("tracks");

                //for all tracks, parse the track data
                for (int i = 0; i < singleTracks.length(); i++)
                {
                    //get the track object
                    JSONObject trackObj = singleTracks.getJSONObject(i);

                    //get the track metadata object
                    JSONObject trackMetadata = trackObj.getJSONObject("metadata");

                    //fill a Track instance with relevant data
                    int id = trackObj.getInt("id");
                    String title = trackMetadata.getString("title");
                    String album = trackMetadata.getString("album");
                    String genre = trackMetadata.getString("genre");
                    String artist = trackMetadata.getString("artist");

                    //add the Track instance to the list of tracks to be handled
                    tracks.add(new Track(id, title, album, genre, artist));
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            //convert the list to an array
            this.allTracks = tracks.toArray(this.allTracks);

            //prepare the array adapter
            ArrayAdapter<Track> adapter = new ArrayAdapter<Track>(this.getActivity(), R.layout.track_list_item, R.id.trackTitle, tracks);

            //attach the adapter to the list
            this.getListView().setAdapter(adapter);
        }
        else
        {
            //empty the list if no data is received
            this.getListView().setAdapter(null);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }
}
