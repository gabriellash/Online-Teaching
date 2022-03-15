package com.example.teachingonline.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teachingonline.Pojo.Course;
import com.example.teachingonline.R;

import java.util.ArrayList;

public class ManageCourseAdapter extends ArrayAdapter<Course> {

    private static final String TAG = "CourseAdapter";
    private Context mcontext;
    int resources;


    public ManageCourseAdapter(Context context, int resource, ArrayList<Course> object) {
        super(context, resource, object);
        mcontext = context;
        this.resources = resource;
    }



    public View getView(int position, View convertView, ViewGroup parent){
        //get the person info
        String name = getItem(position).getCourseName();
        int image = getItem(position).getCoursePicture();
        //create object
        Course course = new Course(name,image);

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = LayoutInflater.from(mcontext).inflate(R.layout.manage_course_item,null);
        convertView = inflater.inflate(resources,parent,false);

        TextView cname = convertView.findViewById(R.id.courseNameText);
        ImageView cpic = convertView.findViewById(R.id.coursePic);

        cname.setText(name);
        cpic.setImageResource(image);

        return convertView;
    }
}
