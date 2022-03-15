package com.example.teachingonline.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.teachingonline.Adapter.SectionAdapter;
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

public class CourseContent extends AppCompatActivity {

    String JSON_STRING;
    String spinneritemselected;
    TextView crsName, textv;
    Spinner lectureSpinner;
    ListView sectionView;
    ArrayList<String> sectionNames, lectureNames;
    ArrayAdapter sections;
    SectionAdapter lectures;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);
        crsName = findViewById(R.id.crsName);
        textv = findViewById(R.id.textv);
        sectionView = findViewById(R.id.sectionView);
        Bundle getcrsName = this.getIntent().getExtras();
        crsName.setText(getcrsName.getString("crsname"));
        lectureSpinner = findViewById(R.id.lectureSpinner);
        sectionNames = new ArrayList<String>();
        lectureNames = new ArrayList<String>();
        new getsections().execute();

        lectureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new getlectures().execute();
                spinneritemselected = sectionNames.get(position);
                textv.setText(sectionNames.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    class getsections extends AsyncTask<Void, Void, String> {
        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "http://192.168.1.105/Onlineteaching/getsectioncourses.php";
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
            JSONObject js;

            try {
                js = new JSONObject(unused);
                JSONArray ja = js.getJSONArray("course_data");

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    if(jo.getString("course").equals(crsName.getText().toString())){
                        sectionNames.add(jo.getString("section"));
                        sections = new ArrayAdapter(CourseContent.this, android.R.layout.simple_spinner_dropdown_item, sectionNames);
                        lectureSpinner.setAdapter(sections);
                    }




                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    class getlectures extends AsyncTask<Void, Void, String> {
        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "http://192.168.1.105/Onlineteaching/getlecturesections.php";
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
            JSONObject js;
            lectureNames.clear();
            try {
                js = new JSONObject(unused);
                JSONArray ja = js.getJSONArray("course_data");

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    if(jo.getString("section").equals(textv.getText().toString())){
                        lectureNames.add(jo.getString("lecture"));
                        lectures = new SectionAdapter(CourseContent.this, lectureNames);
                        sectionView.setAdapter(lectures);
                    }




                }




            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}