package com.example.teachingonline.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.teachingonline.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfile extends AppCompatActivity {
    String studentemail;
    EditText opass, sname, npass;
    ImageView stdProfilePic;
    EditText nemail;
    Button stdProfilePicBtn, changePass;
    final static String URL_RESET= "http://192.168.1.105/Onlineteaching/resetPassword.php";
    String getId;
    private static String URL_READ="http://192.168.1.105/Onlineteaching/readSinfo.php";
    Button editInfo, editPic;
    private static String URL_UPLOAD="http://192.168.1.105/Onlineteaching/uploadSPic.php";
    private static final String TAG = StudentProfile.class.getSimpleName();

    private Bitmap bitmap;
    CircleImageView instPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        Bundle getstudentemail= this.getIntent().getExtras();
        studentemail = getstudentemail.getString("studentemail");
        //nemail = findViewById(R.id.semail);
        sname = findViewById(R.id.sname);
        stdProfilePic = findViewById(R.id.stdProfilePic);
        opass = findViewById(R.id.oldpass);
        npass = findViewById(R.id.newpass);
        changePass = findViewById(R.id.changePass);
        editPic = findViewById(R.id.stdProfilePicBtn);

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpsTrustManager.allowAllSSL();
                String newpassword= npass.getText().toString();
                String oldpassword = opass.getText().toString();
                String email = studentemail;
                HttpsTrustManager.allowAllSSL();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Starting Write and Read data with URL
                        //Creating array for parameters
                        String[] field = new String[3];
                        field[0] = "email";
                        field[1] = "newpassword";
                        field[2] = "oldpassword";

                        //Creating array for data
                        String[] data = new String[3];
                        data[0] = email;
                        data[1] = newpassword;
                        data[2] = oldpassword;


                        PutData putData = new PutData("http://192.168.1.105/Onlineteaching/resetPassword.php", "POST", field, data);
                        if (putData.startPut()) {
                            HttpsTrustManager.allowAllSSL();
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if (result.equals("Password Changed")) {
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                }
                                //   Log.i("PutData", result);
                            }
                        }
                    }
                });
            }

        });
        //  inemail.setText(instemail);

       /* stdProfilePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
        */

    }

   /* private  void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select Picture"), 1);
    }

    */

    public void message(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    public void editstudent(View v) {
       // Bitmap image = ((BitmapDrawable) stdProfilePic.getDrawable()).getBitmap();
       // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
       // image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
      //  String profileencoded = Base64.encodeToString(byteArrayOutputStream.toByteArray(), android.util.Base64.DEFAULT);
        HttpsTrustManager.allowAllSSL();
        String name = sname.getText().toString();
        String email = studentemail;
        HttpsTrustManager.allowAllSSL();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[2];
                field[0] = "name";
                field[1] = "email";

                //Creating array for data
                String[] data = new String[2];
                data[0] = name;
                data[1] = email;


                PutData putData = new PutData("http://192.168.1.105/Onlineteaching/editstudentprofile.php", "POST", field, data);
                if (putData.startPut()) {
                    HttpsTrustManager.allowAllSSL();
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (result.equals("Profile Edited")) {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        }
                        //   Log.i("PutData", result);
                    }
                }
            }
        });
    }




  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && resultCode == RESULT_OK && data !=  null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                instPic.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
            UploadPicture(getId,getStringImage(bitmap));
        }
    }

    private void UploadPicture(final String email, final String picture) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading..");
        progressDialog.show();

        StringRequest stringRequest =  new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(StudentProfile.this,"Success", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(StudentProfile.this,"Try again!" + e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StudentProfile.this,"Try again!" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("picture",picture);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        return encodeImage;
    }

*/

}