//this app uses a separate thread to download the web data
//UI layout stuff should be on the main thread and any big data stuff should be on a background thread

//this app also demonstrates the use of try/catch since it downloads web data

//permission to connect to the internet must be granted in AndroidManifest

package com.chromsicle.webcontentdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.chromsicle.webcontentdemo.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        //protected is similar to public,
        //it's available throughout the whole package so you can get access throughout the whole app
        //the String... strings is like an array, add as many strings as you want then work with them
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpsURLConnection urlConnection = null; //HttpsURLConnection is like a browser

            //convert the string into a URL
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();//creates a URL connection
                InputStream in = urlConnection.getInputStream(); //creates an input stream to collect the data as it comes through
                InputStreamReader reader = new InputStreamReader(in); //reads the data
                int data = reader.read();

                //keep reading data until it's all read
                //convert the int data that the reader is returning into a char and keep adding it to the result string
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        String result = null;

        try {
            result = task.execute("https:zappycode.com").get();

        } catch (Exception e) {
            e.printStackTrace();
        }

    Log.i("Result", result);
    }
}
