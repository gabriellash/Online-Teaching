package com.example.teachingonline.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

public class StudentPayment extends AppCompatActivity {
    String JSON_STRING;
    Spinner spinnerpayment;
    ArrayList spinnercourses;
    ArrayAdapter payment;
    String studentemail, selectedcourse;
    TextView credit, price, date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_payment);

        spinnerpayment = findViewById(R.id.spinnerpayment);
        credit = findViewById(R.id.credit);
        price = findViewById(R.id.price);
        date = findViewById(R.id.date);
        Bundle getstudentemail= this.getIntent().getExtras();
        studentemail = getstudentemail.getString("studentemail");
        spinnercourses = new ArrayList();
        new getenrolledpayment().execute();
        spinnerpayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedcourse = (String) spinnercourses.get(position).toString();
                new getenrolledpaymentdetails().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    class getenrolledpayment extends AsyncTask<Void, Void, String> {
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
                        spinnercourses.add(jo.getString("name"));
                        //enrolledcoursed.add(jo.getString("enroll").toString());
                        payment = new ArrayAdapter(StudentPayment.this, android.R.layout.simple_spinner_dropdown_item, spinnercourses);
                        spinnerpayment.setAdapter(payment);
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class getenrolledpaymentdetails extends AsyncTask<Void, Void, String> {
        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "http://192.168.1.105/Onlineteaching/paymentdetails.php";
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
                    if (jo.getString("course").equals(selectedcourse)) {
                        credit.setText(jo.getString("credit"));
                        price.setText(jo.getString("price")+"$");
                        date.setText(jo.getString("date"));
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}