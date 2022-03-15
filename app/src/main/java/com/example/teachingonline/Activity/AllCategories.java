package com.example.teachingonline.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.teachingonline.Adapter.CategoryAdapter;
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

public class AllCategories extends AppCompatActivity {
    String JSON_STRING;
    GridView gridView;
    ArrayList<String> categoryNames;
    CategoryAdapter adapter;
    String usernamecat, useremailcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);

        gridView = (GridView) findViewById(R.id.gridview);
        categoryNames = new ArrayList<String>();
        new getcategory().execute();
        //CategoryAdapter adapter = new CategoryAdapter(AllCategories.this, categoryNames);
        //gridView.setAdapter(adapter);
        Bundle useremailname = this.getIntent().getExtras();
        usernamecat = useremailname.getString("username");
        useremailcat = useremailname.getString("useremail");

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "Category Chosen: " + categoryNames.get(position), Toast.LENGTH_SHORT).show();
                Intent intentcategory = new Intent(AllCategories.this, CategoryCourses.class);
                intentcategory.putExtra("categoryname", categoryNames.get(position));
                intentcategory.putExtra("useremailcat", useremailcat);
                intentcategory.putExtra("usernamecat", usernamecat);
                startActivity(intentcategory);

            }
        });



    }

    class getcategory extends AsyncTask<Void, Void, String> {
        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "http://192.168.1.105/Onlineteaching/displayCat.php";
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
            categoryNames.clear();
            try {
                js = new JSONObject(unused);
                JSONArray ja = js.getJSONArray("course_data");

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    String name = jo.getString("name");
                    categoryNames.add(name);
                    adapter = new CategoryAdapter(AllCategories.this, categoryNames);
                    gridView.setAdapter(adapter);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
