package com.example.teachingonline.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.teachingonline.R;
import com.example.teachingonline.utils.SessionManager;

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

public class Dashboard extends AppCompatActivity {

    TextView name;
    Button createCourse;
    Button editProf;
    Button managecrs;
    Button logout;
    String namebyemail;
    String JSON_STRING;


    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sessionManager = new SessionManager(this);
        sessionManager.checkILogin();

        Bundle getemail = this.getIntent().getExtras();
        namebyemail = getemail.getString("passemail");

        name = findViewById(R.id.instructorName);
        createCourse = findViewById(R.id.createCourseButton);
        editProf = findViewById(R.id.editProfileBtn);
        logout = findViewById(R.id.logout);
        managecrs = findViewById(R.id.manageCourseButton);
        new Dashboard.getNameByemail().execute();


        namebyemail = getemail.getString("passemail");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.ilogout();
                //finish();
            }
        });


        managecrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ManageCourses.class);
                i.putExtra("instemail", namebyemail);
                startActivity(i);
            }
        });

        createCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateCourse.class);
                i.putExtra("instemail", namebyemail);
                startActivity(i);
            }
        });

        editProf.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditInstProfile.class);
               i.putExtra("instemail", namebyemail);
               startActivity(i);
            }
        });

    }
    class getNameByemail extends AsyncTask<Void,Void,String> {
        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "http://192.168.1.105/Onlineteaching/Getnamebyemail.php";
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((JSON_STRING = bufferedReader.readLine()) !=null){
                    stringBuilder.append(JSON_STRING+ "" +
                            "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String unused) {
            JSONObject js = null;
            try {
                js = new JSONObject(unused);
                JSONArray ja = js.getJSONArray("course_data");

                for(int i=0;i<ja.length();i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    if(jo.getString("email").equals(namebyemail)){
                        name.setText(jo.getString("name"));
                        break;
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}