package it.polito.elite.mremote2.tasks;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bonino on 5/18/15.
 */
public class AsyncTrackPlayControl extends AsyncTask<String, Void, Void>
{
    @Override
    protected Void doInBackground(String... params)
    {
        try
        {
            //prepare the URL representing the API endpoint to call
            URL flaskAPIURL = new URL(params[0]);

            //get a connection object for the given url
            HttpURLConnection connection = (HttpURLConnection) flaskAPIURL.openConnection();

            connection.setRequestMethod("PUT");

            connection.setRequestProperty("Content-Type", "application/json; charset=UTF8");
            connection.setRequestProperty("Accept", "application/json");

            connection.setDoInput(true);
            connection.setDoOutput(true);


            OutputStream out = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(String.format("{\"command\":\"play\",\"track\":%d}", Integer.valueOf(params[1])));
            writer.flush();
            writer.close();
            out.close();

            connection.connect();
            connection.getResponseCode();


        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
