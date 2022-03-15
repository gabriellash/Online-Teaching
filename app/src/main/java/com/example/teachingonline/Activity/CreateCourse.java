package com.example.teachingonline.Activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teachingonline.R;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class CreateCourse extends AppCompatActivity {

    String jsonStr;
    private Spinner langSpin, catSpin;
    String URL_CAT = "http://192.168.1.105/Onlineteaching/get_categories.php";
    String URL_LANG = "http://192.168.1.105/Onlineteaching/getLanguages.php";

    private ArrayList<String> categoriesList = new ArrayList<>();
    ArrayAdapter<String> categoryAdapter;
    private ArrayList<String> languageList = new ArrayList<>();
    ArrayAdapter<String> languageAdapter;
    ImageView coursepicture;
    TextInputEditText coursename, coursedesc, courserequirements, courseoriginalprice;
    String email;
    Button addpicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        coursename = findViewById(R.id.courseNameInput);
        coursedesc = findViewById(R.id.courseDescInput);
        courserequirements = findViewById(R.id.courseReqInput);
        courseoriginalprice = findViewById(R.id.courseOrgPriceInput);
        langSpin = findViewById(R.id.languageSpinner);
        catSpin = findViewById(R.id.categorySpinner);
        Bundle togetemail = this.getIntent().getExtras();
        email = togetemail.getString("instemail");

        langSpin = findViewById(R.id.languageSpinner);
        catSpin = findViewById(R.id.categorySpinner);
        new GetCategories().execute();
        new GetLanguages().execute();
    }




    private class GetCategories extends AsyncTask<String, String, String >{

        String json_url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                url = new URL(URL_CAT);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    current += (char) data;
                    data = isw.read();
                    System.out.print(current);
                }
                Log.d("datalength",""+current.length());
                // return the data to onPostExecute method
                return current;


            } catch ( MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("data", result.toString());
            try {
                JSONObject jsonObj = new JSONObject((result));
                JSONArray category = jsonObj.getJSONArray("category");
                for(int i=0; i<category.length(); i++) {
                    JSONObject jsonObject = category.getJSONObject(i);
                    String catName = jsonObject.optString("name");
                    categoriesList.add(catName);
                    categoryAdapter = new ArrayAdapter<>(CreateCourse.this, android.R.layout.simple_spinner_item,categoriesList);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    catSpin.setAdapter(categoryAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    private class GetLanguages extends AsyncTask<String, String, String >{

        String json_url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                url = new URL(URL_LANG);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    current += (char) data;
                    data = isw.read();
                    System.out.print(current);
                }
                Log.d("datalength",""+current.length());
                // return the data to onPostExecute method
                return current;


            } catch ( MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("data", result.toString());
            try {
                JSONObject jsonObj = new JSONObject((result));
                JSONArray language = jsonObj.getJSONArray("language");
                for(int i=0; i<language.length(); i++) {
                    JSONObject jsonObject = language.getJSONObject(i);
                    String langName = jsonObject.optString("name");
                    languageList.add(langName);
                    languageAdapter = new ArrayAdapter<>(CreateCourse.this, android.R.layout.simple_spinner_item,languageList);
                    languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    langSpin.setAdapter(languageAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CreateCourse.this,Dashboard.class);
        startActivity(i);
    }





    //@RequiresApi(api = Build.VERSION_CODES.O)
    public void createCourse(View v) {

       // Bitmap image = ((BitmapDrawable) coursepicture.getDrawable()).getBitmap();
       // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
       // image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
      //  String pictureencoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), android.util.Base64.DEFAULT);
        HttpsTrustManager.allowAllSSL();
        String cname, cdesc, creq, corgprice,  clang, ccat, instmail;
        cname = String.valueOf(coursename.getText());
        cdesc = String.valueOf(coursedesc.getText());
        creq = String.valueOf(courserequirements.getText());
        corgprice = String.valueOf(courseoriginalprice.getText());
        clang = String.valueOf(langSpin.getSelectedItem());
        ccat = String.valueOf(catSpin.getSelectedItem());
        instmail = email;


        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[7];
                field[0] = "cname";
                field[1] = "cdesc";
                field[2] = "creq";
                field[3] = "corgprice";
                field[4] = "clang";
                field[5] = "ccat";
                field[6] = "instmail";

                //Creating array for data
                String[] data = new String[7];
                data[0] = cname;
                data[1] = cdesc;
                data[2] = creq;
                data[3] = corgprice;
                data[4] = clang;
                data[5] = ccat;
                data[6] = instmail;

                PutData putData = new PutData("http://192.168.1.105/Onlineteaching/createCourse.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        HttpsTrustManager.allowAllSSL();
                        String result = putData.getResult();
                        if (result.equals("Course Created")) {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        }
                        // Log.i("PutData", result);
                    }
                }
            }
        });
    }



}