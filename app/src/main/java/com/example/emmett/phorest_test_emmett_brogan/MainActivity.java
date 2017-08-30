package com.example.emmett.phorest_test_emmett_brogan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    //TextView textForClients;
    //Button getClientListButton;

    private String URL = "http://api-gateway-dev.phorest.com/third-party-api-server/api/business/eTC3QY5W3p_HmGHezKfxJw/client?size=20&page=0";
    private ListView clientList;
    private ProgressDialog dialog;

/*
    public static final String BASE_URL = " http://api-gateway-dev.phorest.com/third-party-api-server/api/business/";
    //http://api-gateway-dev.phorest.com/third-party-api-server/api/business/eTC3QY5W3p_HmGHezKfxJw/client?size=20&page=0
    */
    public String username = "global/cloud@apiexamples.com";
    public String password = "VMlRo/eh+Xd8M~l";
    public static String businessID = "eTC3QY5W3p_HmGHezKfxJw";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading Client List...");

        /*

        MIGHT NOT BE NEEDED!!! CHECK RESPONSE IN THEIR API SITE!!!

        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
        */

        clientList = (ListView)findViewById(R.id.clientList);

        new retrieveClientList().execute(URL);

        /*
        getClientListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(BASE_URL, username, password);
                Toast.makeText(getApplicationContext(),"Your message.", Toast.LENGTH_LONG).show();

            }
        });*/
    }

    public class retrieveClientList extends AsyncTask<String, String, List<ClientModel>>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<ClientModel> doInBackground(String... params)
        {
            //Setup authentication details + readers for data
            String uName = "global/cloud@apiexamples.com";
            String pWord = "VMlRo/eh+Xd8M~l";
            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;

            byte[] loginBytes = (uName + ":" + pWord).getBytes();
            StringBuilder loginBuilder = new StringBuilder()
                    .append("Basic ")
                    .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

            //Try to connect
            try
            {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Authorization", loginBuilder.toString());
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = "";
                //Get data here
                while((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }
                String jsonData = stringBuffer.toString();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray jsonArray = jsonObject.getJSONArray("Client");
                //JSONArray jsonArray = jsonObject.getJSONArray("content");

                List<ClientModel> clientList = new ArrayList<>();
                Gson gson = new Gson();
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject finalObject = jsonArray.getJSONObject(i);

                    //gson useful as we can just use our model and it knows where to put the stuff we want
                    //instead of having to go in and set each one corresponding to our client model!
                    ClientModel clientModel = gson.fromJson(finalObject.toString(), ClientModel.class);
                    clientList.add(clientModel);
                }
                return clientList;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            //Close connections + buffered reader
            finally
            {
                if(connection != null)
                {
                    connection.disconnect();
                }
                try
                {
                    if (bufferedReader != null)
                    {
                        bufferedReader.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(final List<ClientModel> resultClientModel) {
            super.onPostExecute(resultClientModel);

            //Turn off dialog spinner anim
            dialog.dismiss();

            if(resultClientModel != null)
            {
                ClientAdapter adapter = new ClientAdapter(getApplicationContext(), R.layout.row, resultClientModel);
                clientList.setAdapter(adapter);
                clientList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        ClientModel clientModel = resultClientModel.get(position);
                        Intent intent = new Intent(MainActivity.this, ClientDetailsActivity.class);
                        intent.putExtra("clientModel", new Gson().toJson(clientModel));
                        startActivity(intent);
                    }
                });
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Cannot retrieve data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ClientAdapter extends ArrayAdapter
    {
        private List<ClientModel> clientModelList;
        private int resource;
        private LayoutInflater inflater;


        public ClientAdapter(@NonNull Context context, @LayoutRes int resource, List<ClientModel> objects)
        {
            super(context, resource, objects);

            clientModelList = objects;
            this.resource = resource;

            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            ViewHolder holder = null;

            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.clientId = (TextView)convertView.findViewById(R.id.clientId);
                holder.firstName = (TextView)convertView.findViewById(R.id.name);
                //holder.lastName = (TextView)convertView.findViewById(R.id.name);
                holder.email = (TextView)convertView.findViewById(R.id.email);
                holder.mobile = (TextView)convertView.findViewById(R.id.mobile);
                holder.landLine = (TextView)convertView.findViewById(R.id.landLine);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ClientAdapter.ViewHolder) convertView.getTag();
            }

            //Set text of textviews once you do ui stuff!!!

            holder.clientId.setText(clientModelList.get(position).getClientId());
            holder.firstName.setText(clientModelList.get(position).getFirstName() + " " + clientModelList.get(position).getLastName());
            holder.email.setText(clientModelList.get(position).getEmail());
            holder.mobile.setText(clientModelList.get(position).getMobile());
            holder.landLine.setText(clientModelList.get(position).getLandLine());



            return convertView;
        }


        class ViewHolder
        {
            private TextView clientId;
            private TextView firstName;
            private TextView lastName;
            private TextView email;
            private TextView mobile;
            private TextView landLine;
        }

    }



    /*
    private class FetchWeatherData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&appid=2de143494c0b295cca9337e1e96b00e0");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                return forecastJsonStr;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textForClients.setText(s);
            Log.i("json", s);
        }
    }
    */

    /*
    public static String getData(String uri, String userName, String userPassword) {
        BufferedReader reader = null;
        byte[] loginBytes = (userName + ":" + userPassword).getBytes();

        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Authorization", loginBuilder.toString());

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine())!= null){
                sb.append(line);
                sb.append("\n");
            }

            return  sb.toString();

        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if (null != reader)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            new retrieveClientList().execute(URL);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
