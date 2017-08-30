package com.example.emmett.phorest_test_emmett_brogan;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

/**
 * Created by Emmett Brogan on 30/08/2017.
 */

public class ClientDetailsActivity extends ActionBarActivity {
    private TextView clientId;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView mobile;
    private TextView landLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get data from MainActivity, sent via intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            String json = bundle.getString("clientModel"); // getting the model from MainActivity send via extras
            ClientModel clientModel = new Gson().fromJson(json, ClientModel.class);

            clientId = (TextView)findViewById(R.id.clientId);
            clientId.setText("Client ID: " + clientModel.getClientId());
            firstName = (TextView)findViewById(R.id.name);
            firstName.setText("Name: " + clientModel.getFirstName() + " " + clientModel.getLastName());
            email = (TextView)findViewById(R.id.email);
            email.setText("Email: " + clientModel.getEmail());
            mobile = (TextView)findViewById(R.id.mobile);
            mobile.setText("Mobile: " + clientModel.getMobile());
            landLine = (TextView)findViewById(R.id.landLine);
            landLine.setText("Landline: " + clientModel.getLandLine());

        }

    }
}
