package com.example.teachingonline.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.teachingonline.R;

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

public class StudentCourses extends AppCompatActivity {
    //TextView studentemail;
    String studentemail, JSON_STRING;
    ListView listenrolled;
    ArrayList<String> enrolledcoursename, enrolledcoursed;
    ArrayAdapter enrolled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_courses);
        //  studentemail = findViewById(R.id.studentname);
        Bundle getemail = this.getIntent().getExtras();
        studentemail = getemail.getString("studentemail");
        //  studentemail.setText(getemail.getString("studentemail"));
        listenrolled = findViewById(R.id.listenrolled);
        enrolledcoursename = new ArrayList();
        enrolledcoursed = new ArrayList<>();
        new getenrolled().execute();

    }

    class getenrolled extends AsyncTask<Void, Void, String> {
        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "http://192.168.1.105/Onlineteaching/getenrolledcourses.php";
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
                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "" +
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

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    if (jo.getString("email").equals(studentemail)) {
                        enrolledcoursename.add(jo.getString("name"));
                        enrolledcoursed.add(jo.getString("enroll").toString());
                        enrolled = new ArrayAdapter(StudentCourses.this, android.R.layout.simple_list_item_1, enrolledcoursename);
                        listenrolled.setAdapter(enrolled);
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}