package com.example.adi.futurebank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.prefs.PreferenceChangeEvent;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText email, password;
    CheckBox checkBox;
    ImageButton signin;
    Button signup;
    String userEmail;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Boolean id = false;
        getIntent().getBooleanExtra("Logat",id);
        if(id == true)sendUserToMainActivity();

        sharedPref = LoginActivity.this.getSharedPreferences("Nume",0);
        editor = sharedPref.edit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        checkBox = (CheckBox)findViewById(R.id.checkbox);
        signin =(ImageButton)findViewById(R.id.signin);
        signup = (Button) findViewById(R.id.signup);

        signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                checkUserExistence();
                //sendUserToMainActivity();

            }
        });

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this,"Sign up Button will be updated soon", Toast.LENGTH_LONG).show();
                //Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                //startActivity(i);

            }
        });
    }

    private void checkUserExistence()
    {
        userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        new AsyncLogin().execute(userEmail,userPassword);
    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
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
                url = new URL("http://192.168.18.165:80/login.php");
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
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
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
            pdLoading.dismiss();
            if(result.equalsIgnoreCase("Yes"))
            {
                //Save his data to SharedPreferences when he comes back, to redirect him to his account
                Intent this_intent = getIntent();
                this_intent.putExtra("Logat",true);

                editor.putString("user", userEmail);
                editor.commit();


                //Successful login
                Intent intent = new Intent (LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();


            }

            else if (result.equalsIgnoreCase("No"))
            {
                // Unsuccessful login
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
            }
            else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful"))
            {
                Toast.makeText(LoginActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendUserToMainActivity()
    {
        Toast.makeText(LoginActivity.this,"Sign In Button Clicked", Toast.LENGTH_LONG).show();
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
}
