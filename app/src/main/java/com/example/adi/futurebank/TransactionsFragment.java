package com.example.adi.futurebank;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends Fragment {

    MyRecyclerViewAdapter adapter;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    View v;


    public TransactionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        sharedPref = getContext().getSharedPreferences("Nume",0);
        editor = sharedPref.edit();

        String nume = sharedPref.getString("user",null);

        new AsyncE().execute(nume);
        v = inflater.inflate(R.layout.fragment_transactions, container, false);

        // data to populate the RecyclerView with
        /*ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");

        // set up the RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyRecyclerViewAdapter(getContext(), animalNames);
        //adapter.setClickListener(getContext());
        recyclerView.setAdapter(adapter);*/

        return v;
    }

    private class AsyncE extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                url = new URL("http://192.168.18.165:80/getexpenses.php");
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                return "exception1";
            }
            try
            {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append POST parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
                return "exception2";
            }
            try
            {
                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK)
                {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null)
                    {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());
                }
                else
                {
                    return("unsuccessful");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return "exception";
            }
            finally
            {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            //this method will be running on UI thread
            Log.d("Classtag",result);
            ArrayList<String> animalNames = new ArrayList<>();

            /*animalNames.add("Horse");
            animalNames.add("Cow");
            animalNames.add("Camel");
            animalNames.add("Sheep");
            animalNames.add("Goat");*/

            String[] items = result.split("!");
            for (String item : items)
            {
                animalNames.add(item);
            }



            // set up the RecyclerView
            RecyclerView recyclerView = v.findViewById(R.id.myRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new MyRecyclerViewAdapter(getContext(), animalNames);
            //adapter.setClickListener(getContext());
            recyclerView.setAdapter(adapter);
            pdLoading.dismiss();

        }
    }

}
