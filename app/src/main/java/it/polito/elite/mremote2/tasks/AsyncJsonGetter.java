package it.polito.elite.mremote2.tasks;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bonino on 5/15/15.
 */
public class AsyncJsonGetter extends AsyncTask<String, Void, JSONObject>
{
    private JsonGetterCompletionListener theListener;

    public AsyncJsonGetter(JsonGetterCompletionListener listener)
    {
        this.theListener = listener;
    }

    @Override
    protected void onPostExecute(JSONObject obj)
    {
        super.onPostExecute(obj);

        //notify the listener
        this.theListener.onTracksLoadingComplete(obj);
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... params)
    {
        JSONObject tracks = null;

        try
        {
            //prepare the URL representing the API endpoint to call
            URL flaskAPIURL = new URL(params[0]);

            //get a connection object for the given url
            HttpURLConnection connection = (HttpURLConnection) flaskAPIURL.openConnection();

            //set the connection method
            connection.setRequestMethod("GET");

            //connect
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {

                //open an input stream on the connection
                InputStream is = connection.getInputStream();

                //create a reader on the input stream
                InputStreamReader isr = new InputStreamReader(is);

                //create a reader
                BufferedReader bf = new BufferedReader(isr);

                //read
                String line;
                StringBuffer jsonBody = new StringBuffer();
                while ((line = bf.readLine()) != null)
                {
                    jsonBody.append(line);
                }

                //close the stream
                bf.close();
                isr.close();
                is.close();

                tracks = new JSONObject(jsonBody.toString());
            }

        } catch (IOException | JSONException e)
        {
            // print the error
            //TODO: remove and replace with a more structured approach
            e.printStackTrace();
        }

        return tracks;
    }
}
