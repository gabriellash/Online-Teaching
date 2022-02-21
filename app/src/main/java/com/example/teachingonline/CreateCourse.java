package com.example.teachingonline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateCourse extends AppCompatActivity {
    String[] lang = {"English", "French","Arabic","German","Spanish","Chinese","Japanese","Russian"};
        String[] cat = {"Web Development", "UX Design","Programming","Photography","Networking "};
    Spinner langSpin, catSpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        langSpin = findViewById(R.id.languageSpinner);
        catSpin = findViewById(R.id.categorySpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lang);
        langSpin.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cat);
        catSpin.setAdapter(adapter1);

    }
}