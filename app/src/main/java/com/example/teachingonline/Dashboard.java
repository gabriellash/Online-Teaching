package com.example.teachingonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;

import com.vishnusivadas.advanced_httpurlconnection.FetchData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Dashboard extends AppCompatActivity {

    TextView name;
    HttpURLConnection httpURLConnection;
    Bundle extras;
    Button createCourse;
    Button editProf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        name = findViewById(R.id.instructorName);

        createCourse = findViewById(R.id.createCourseButton);

        createCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateCourse.class);
                startActivity(i);
                finish();
            }
        });




    }
}