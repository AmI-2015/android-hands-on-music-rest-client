package it.polito.elite.mremote2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class Settings extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        EditText apiTextField = (EditText) this.findViewById(R.id.api_endpoint);
        apiTextField.setText(this.getIntent().getExtras().getString(HomeActivity.API_ENDPOINT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        else
        {
            //simplified, this is the back button
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish()
    {
        //get the text field value
        EditText apiTextField = (EditText) this.findViewById(R.id.api_endpoint);
        String endpoint = apiTextField.getText().toString();

        if((endpoint!=null)&&(!endpoint.isEmpty()))
        {
            Intent dataIntent = new Intent(this, HomeActivity.class);
            dataIntent.putExtra(HomeActivity.API_ENDPOINT,endpoint);
            //send back the result
            this.setResult(RESULT_OK, dataIntent);
        }
        else
        {
            this.setResult(RESULT_CANCELED);
        }

        super.finish();
    }
}
