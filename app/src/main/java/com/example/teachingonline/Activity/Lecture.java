package com.example.teachingonline.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.teachingonline.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lecture extends AppCompatActivity {

    private static String crname;
    String email;
    Button addLec,saveCrs,addSec;
    TextInputEditText lname, sname;
    private Spinner langSpin, catSpin,secspin;
    String URL_CAT = "http://192.168.1.105/Onlineteaching/get_categories.php";
    String URL_LANG = "http://192.168.1.105/Onlineteaching/getLanguages.php";
    String URL_EDIT="http://192.168.1.105/Onlineteaching/editcourse.php";
    String URL_SEC=" http://192.168.1.105/Onlineteaching/getSection.php";
    String URL_AddSec="http://192.168.1.105/Onlineteaching/addSection.php";
    String URL_Lec="http://192.168.1.105/Onlineteaching/addLecture.php";
    TextInputEditText coursename, coursedesc, courserequirements, courseoriginalprice;
    private ArrayList<String> categoriesList = new ArrayList<>();
    ArrayAdapter<String> categoryAdapter;
    private ArrayList<String> sectionList = new ArrayList<>();
    ArrayAdapter<String> sectionAdapter;
    private ArrayList<String> languageList = new ArrayList<>();
    ArrayAdapter<String> languageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);

        Bundle getemail = this.getIntent().getExtras();
        email = getemail.getString("email");

        saveCrs = findViewById(R.id.saveCrs);
        crname = getemail.getString("coursename");
        addLec = findViewById(R.id.addLectBtn);
        langSpin = findViewById(R.id.languageSpinner);
        catSpin = findViewById(R.id.categorySpinner);
        coursename = findViewById(R.id.crsUpdateName);
        coursedesc = findViewById(R.id.courseDescUpd);
        courserequirements = findViewById(R.id.courseReqUpd);
        courseoriginalprice = findViewById(R.id.courseUdpPrice);
        secspin = findViewById(R.id.sectionSpinner);
        lname = findViewById(R.id.lectureName);
        sname = findViewById(R.id.sectionName);
        addSec = findViewById(R.id.addSecBtn);
        new GetCategories().execute();
        new GetLanguages().execute();
        new GetSection().execute();



        saveCrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cname, cdesc, creq, corgprice, clang, ccat,instmail, crsname;
                cname = String.valueOf(coursename.getText());
                cdesc = String.valueOf(coursedesc.getText());
                creq = String.valueOf(courserequirements.getText());
                corgprice = String.valueOf(courseoriginalprice.getText());
                clang = String.valueOf(langSpin.getSelectedItem());
                ccat = String.valueOf(catSpin.getSelectedItem());
                instmail = email;
                crsname = crname;

                if(cname.isEmpty() || cdesc.isEmpty() ){
                    Toast.makeText(getApplicationContext(),"Required field",Toast.LENGTH_SHORT).show();
                }else{
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    message(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    message(error.getMessage());
                                }
                            }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("cname",cname);
                            params.put("cdesc",cdesc);
                            params.put("creq",creq);
                            params.put("corgprice",corgprice);
                            params.put("instmail",instmail);
                            params.put("ccat",ccat);
                            params.put("clang",clang);
                            params.put("crsname",crsname);
                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(Lecture.this);
                    queue.add(stringRequest);
                }

            }
        });

        addSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String lectname = String.valueOf(lname.getText());
                String secname = String.valueOf(sname.getText());
                String crsname = crname;

                if(secname.isEmpty() ){
                    Toast.makeText(getApplicationContext(),"Required field",Toast.LENGTH_SHORT).show();
                }else{
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_AddSec,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    message(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    message(error.getMessage());
                                }
                            }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            //params.put("lectname",lectname);
                            params.put("secname",secname);
                            params.put("crsname",crsname);
                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(Lecture.this);
                    queue.add(stringRequest);
                }

            }
        });

        addLec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lectname = String.valueOf(lname.getText());
                String secname = String.valueOf(secspin.getSelectedItem());
                String crsname = crname;

                if(secname.isEmpty() && lectname.isEmpty() ){
                    Toast.makeText(getApplicationContext(),"Required field",Toast.LENGTH_SHORT).show();
                }else{
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Lec,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    message(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    message(error.getMessage());
                                }
                            }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("lectname",lectname);
                            params.put("secname",secname);
                            params.put("crsname",crsname);
                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(Lecture.this);
                    queue.add(stringRequest);
                }

            }
        });

    }

    private class GetSection extends AsyncTask<String, String, String >{

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
                url = new URL(URL_SEC);
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
                JSONArray section = jsonObj.getJSONArray("section");
                String crsname = crname;
                for(int i=0; i<section.length(); i++) {
                    JSONObject jsonObject = section.getJSONObject(i);
                    if(jsonObject.getString("coursename").contains(crsname)){
                        String sec = jsonObject.optString("sectionname");
                        sectionList.add(sec);
                        sectionAdapter = new ArrayAdapter<>(Lecture.this, android.R.layout.simple_spinner_item,sectionList);
                        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        secspin.setAdapter(sectionAdapter);

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void message(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    private class GetCategories extends AsyncTask<String, String, String > {

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
                    categoryAdapter = new ArrayAdapter<>(Lecture.this, android.R.layout.simple_spinner_item,categoriesList);
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
                    languageAdapter = new ArrayAdapter<>(Lecture.this, android.R.layout.simple_spinner_item,languageList);
                    languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    langSpin.setAdapter(languageAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }








}