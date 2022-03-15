package com.example.teachingonline.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teachingonline.Adapter.CourseAdapter;
import com.example.teachingonline.Pojo.courses;
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

public class CategoryCourses extends AppCompatActivity {

    String JSON_STRING;
    private Context context;
    ListView listView;
    TextView categorychosen,categorychosenfromhome;
    CourseAdapter adap;
    ArrayList courseslist;
    String usernamelist, useremaillist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_courses);

        categorychosen = findViewById(R.id.categorychosen);
        listView = findViewById(R.id.courseListView);
        courseslist = new ArrayList();
        categorychosenfromhome =findViewById(R.id.categorychosenfromhome);
        Bundle getcategory = this.getIntent().getExtras();
        categorychosen.setText(getcategory.getString("categoryname"));
        Bundle categoire = this.getIntent().getExtras();
        categorychosenfromhome.setText(categoire.getString("category"));
        usernamelist = getcategory.getString("usernamecat");
        useremaillist = getcategory.getString("useremailcat");

        new getcategorycourse().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                courses course = (courses) courseslist.get(position);
                Toast.makeText(getApplicationContext(), "Course " + course.getName(), Toast.LENGTH_SHORT).show();
                Intent courseintent = new Intent(CategoryCourses.this, CourseData.class);
                courseintent.putExtra("coursename", course.getName());
                courseintent.putExtra("usernamelist", usernamelist);
                courseintent.putExtra("useremaillist", useremaillist);
                startActivity(courseintent);
            }
        });


    }

    class getcategorycourse extends AsyncTask<Void, Void, String> {
        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "http://192.168.1.105/Onlineteaching/getCategoryCourse.php";
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
                    String name = jo.getString("name");
                    String orgprice = jo.getString("orgprice");
                    String category = jo.getString("categoryname");
                    if(category.equals(categorychosen.getText().toString()) || category.equals(categorychosenfromhome.getText().toString())){
                        courses courses = new courses(name,orgprice);
                        courseslist.add(courses);
                        adap = new CourseAdapter(CategoryCourses.this,courseslist);
                        listView.setAdapter(adap);

                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}