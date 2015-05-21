package it.polito.elite.mremote2.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import it.polito.elite.mremote2.HomeActivity;
import it.polito.elite.mremote2.R;
import it.polito.elite.mremote2.tasks.AsyncJsonGetter;
import it.polito.elite.mremote2.tasks.JsonGetterCompletionListener;

public class TrackFragment extends Fragment implements JsonGetterCompletionListener{

    //the key used for retrieving the position of the list item for which details are shown.
    public static final String ARG_POSITION = "position";
    public static final String ARG_TRACK_ID = "trackId";
    public TrackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get the item selected
        int trackId = (Integer) this.getArguments().get(TrackFragment.ARG_TRACK_ID);
        String apiEndpoint = (String) this.getArguments().get(HomeActivity.API_ENDPOINT);
        //async task for getting the track info
        AsyncJsonGetter jsonGetter = new AsyncJsonGetter(this);
        jsonGetter.execute(apiEndpoint+"tracks/"+trackId);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public <Track> void onTracksLoadingComplete(JSONObject object)
    {
        //parse json object
        try
        {
            JSONObject track = object.getJSONObject("track");

            JSONObject metadata = track.getJSONObject("metadata");

            this.setText(R.id.trackDetailTitle, metadata.getString("title"));
            this.setText(R.id.trackDetailArtist,"ARTIST: "+ metadata.getString("artist"));
            this.setText(R.id.trackDetailAlbum, "ALBUM: "+metadata.getString("album"));
            this.setText(R.id.trackDetailGenre, "GENRE: "+metadata.getString("genre"));

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    private void setText(int id, String text)
    {
        TextView titleView = (TextView) this.getView().findViewById(id);
        titleView.setText(text);
    }
}
