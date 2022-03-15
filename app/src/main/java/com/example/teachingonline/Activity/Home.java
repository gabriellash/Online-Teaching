package com.example.teachingonline.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teachingonline.Adapter.HomeAdapter;
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
import java.util.ArrayList;

public class Home extends AppCompatActivity {
    String JSON_STRING;
    TextView homename;
    String namebyemail;
    ListView listView;
    TextView seeAll;
    SessionManager sessionManager;
    ArrayList<String> category;
    ArrayList<String> course;
    ArrayAdapter<String> adapt;
    AutoCompleteTextView autocomplete;
    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView = findViewById(R.id.courseListView);


        Bundle getemail = this.getIntent().getExtras();
        namebyemail = getemail.getString("passemail");
        homename = findViewById(R.id.homename);
        grid = findViewById(R.id.grid);

        category = new ArrayList();
        category.add("Web Development");
        category.add("Marketing");
        category.add("Business");
        category.add("Mobile Development");
        autocomplete = findViewById(R.id.autocomplete);

        course = new ArrayList<>();

        new getcourses().execute();
        HomeAdapter adapter = new HomeAdapter(this, category);
        grid.setAdapter(adapter);


        seeAll = findViewById(R.id.seeall);
        //String namebyemail = getemail.getString("passemail");
        new getNameByemail().execute();

        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,AllCategories.class);
                intent.putExtra("useremail", namebyemail);
                intent.putExtra("username", homename.getText().toString());
                startActivity(intent);
            }
        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Category Chosen: " + category.get(position), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Home.this, CategoryCourses.class);
                i.putExtra("category", category.get(position));
                startActivity(i);
            }
        });

        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent autocompletecourse = new Intent(Home.this, CourseData.class);
                autocompletecourse.putExtra("coursename", course.get(position));
                startActivity(autocompletecourse);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.editProfileMenu:
                Intent i = new Intent(getApplicationContext(), StudentProfile.class);
                i.putExtra("studentemail", namebyemail);
                startActivity(i);
                break;
            case R.id.viewCoursesMenu:
                Intent j = new Intent(getApplicationContext(), StudentCourses.class);
                j.putExtra("studentemail", namebyemail);
                startActivity(j);
                break;
            case R.id.paymentDetailMenu:
                Intent k = new Intent(getApplicationContext(), StudentPayment.class);
                k.putExtra("studentemail", namebyemail);
                startActivity(k);
                break;
            case R.id.slogout:
                sessionManager.slogout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class getNameByemail extends AsyncTask<Void,Void,String>{
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
                        homename.setText(jo.getString("name"));
                        break;
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void search(View v) {
        autocomplete.setVisibility(View.VISIBLE);
    }

    public class getcourses extends AsyncTask<Void, Void, String> {
        String json_url;


        @Override
        protected void onPreExecute() {
            json_url = "http://192.168.1.105/Onlineteaching/getC.php";
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
                JSONArray ja = js.getJSONArray("course_name");

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    String name = jo.getString("name");
                    course.add(name);
                    adapt = new ArrayAdapter<String>(Home.this, android.R.layout.simple_dropdown_item_1line, course);
                    autocomplete.setAdapter(adapt);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }




}