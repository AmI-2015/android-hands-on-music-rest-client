package it.polito.elite.mremote2.tasks;

import org.json.JSONObject;

/**
 * Created by bonino on 5/18/15.
 */
public interface JsonGetterCompletionListener
{
    public <Track> void onTracksLoadingComplete(JSONObject object);
}
