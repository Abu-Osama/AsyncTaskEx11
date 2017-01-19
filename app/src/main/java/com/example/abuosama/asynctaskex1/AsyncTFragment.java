package com.example.abuosama.asynctaskex1;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class AsyncTFragment extends Fragment {

   TextView textView;
    Button button;

    MyAsyncTask myAsyncTask;

    public boolean checkInternet(){

        ConnectivityManager manager= (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        //b. from network manger & get active network information

        //NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();

        //c.check if network connected or not
        if(networkInfo==null  ||networkInfo.isConnected()==false) {

            //means there is no internet
            //webview.loadData("<h1>No Internet check internet<h1>", "text/html", null);


            return  false;
        }

        return true;


    }


    public AsyncTFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_async_t, container, false);
        textView = (TextView) v.findViewById(R.id.textview1);
        button = (Button) v.findViewById(R.id.button1);
        myAsyncTask=new MyAsyncTask();//don't execute or don't asynctask immediately

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //10:ccal, above method to check if internet is there are noy
                if(checkInternet()){
                    if(myAsyncTask.getStatus()== AsyncTask.Status.FINISHED||
                            myAsyncTask.getStatus()==AsyncTask.Status.FINISHED) {

                        Toast.makeText(getActivity(), "Already running ple wait", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    myAsyncTask.execute("http://skillgun.com");

                }

                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            }
        });
        return  v;

    }

    private class MyAsyncTask  extends AsyncTask<String,Void,String>{
        URL myurl;
        HttpURLConnection connection;
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        String line;
        StringBuilder stringBuilder;
        @Override
        protected String doInBackground(String... p1) {


            try {
                myurl=new URL(p1[0]);
                connection= (HttpURLConnection) myurl.openConnection();
                inputStream=connection.getInputStream();
                inputStreamReader=new InputStreamReader(inputStream);
                bufferedReader=new BufferedReader(inputStreamReader);
                stringBuilder=new StringBuilder();
                //we are now reading data from buffered reader
                line=bufferedReader.readLine();
                while (line!=null){
                    stringBuilder.append(line);
                    line=bufferedReader.readLine();
                }
                //return the complete data to onpost execute
                return stringBuilder.toString();

            }
              catch (MalformedURLException e){
                  e.printStackTrace();
              }
              catch (IOException e) {
                e.printStackTrace();
            }
            catch (SecurityException e){
              e.printStackTrace();
                return "no internet permission";
            }

            return "something went wrong";
        }

        @Override
        protected void onPostExecute(String s) {
            textView.setText(s);
            super.onPostExecute(s);
        }
    }
}
