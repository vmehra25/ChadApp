package com.example.chadapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Account2Activity extends AppCompatActivity {
    //Account2Activity components
    static EditText Name;
    static TextView Dob;
    static ImageView Dp;
    static Uri imageUri;
    int Upl1 = 0;
    //Uploading image
    static String Url;
    static Uri backupUri = null;
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    //Date selecting
    DatePickerDialog.OnDateSetListener mDatesetListener;

    //Firebase use
    FirebaseUser user;
    Task<Uri> uriTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account2);
        InitializeFields();

        //Fit Image to full screen
        ImageView img = findViewById(R.id.Account2_Ige);
        img.setScaleType(ImageView.ScaleType.FIT_XY);

        //Already selected image
        if (Account2Activity.imageUri != null)
        {
                Dp.setImageURI(Account2Activity.imageUri);
        }
        //Downloaded image
      else if (ChatFragment.bitmap != null)
       {
           Dp.setImageBitmap(ChatFragment.bitmap);
       }

        //Setting Name and Dob
        if(!TextUtils.isEmpty(ChatFragment.NameUser) && !TextUtils.isEmpty(ChatFragment.DobUser))
       {
           Name.setText(ChatFragment.NameUser);
            Dob.setText(ChatFragment.DobUser);
       }

        //Setting up Date Picker
        Dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR),
                        month = cal.get(Calendar.MONTH),
                        day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Account2Activity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDatesetListener,year,month,day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        //Setting up DateSetListener and setting the date
        mDatesetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Dob.setText(dayOfMonth+"/"+(month+1)+"/"+year);

            }
        };
    }

    private void InitializeFields() {


        Name = findViewById(R.id.Account2_Name);
        Dob = findViewById(R.id.Account2_DOB);
        Dp = findViewById(R.id.Account2_DP);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void Next(View view) {


        String usrKey = user.getUid();
        //Creating Database referece
        database = FirebaseDatabase.getInstance().getReference().child("Users").child(usrKey);

        //HashMap to upload value
        final Map<String,Object> map= new HashMap<>();
        String name = Name.getText().toString(),
                dob = Dob.getText().toString();

        //Check for empty strings
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(dob))
        {
            //Progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please Wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            //Uploading Picture
            if(Account2Activity.imageUri != null)
            {
                Upl1 = 1;

                storage.child("display pictures").child(usrKey).putFile(Account2Activity.imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Url = uriTask.getResult().toString();

                       database.child("image").setValue(Url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    //Toast.makeText(Account2Activity.this, "Url Uploaded", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Account2Activity.this,MainActivity.class));
                                }
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // progressDialog.dismiss();
                        Toast.makeText(Account2Activity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });


               // map.put("image",Url);
            }

            map.put("name",name);
            map.put("dob",dob);


            //Uploading HashMap
            database.updateChildren((Map)map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        map.clear();
                        if(Upl1 == 0){
                            progressDialog.dismiss();
                     //  Toast.makeText(Account2Activity.this, "Details Uploaded", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Account2Activity.this,MainActivity.class));
                    }}
                    else
                    {
                        map.clear();
                        Toast.makeText(Account2Activity.this, "Error: "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            backupUri = Account2Activity.imageUri;
        }
        else
            Toast.makeText(this, "Name and dob are required", Toast.LENGTH_SHORT).show();
    }

    public void SelectImage(View view) {

        //Checking version of phone
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                selectImage();
            else
                //requesting permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},10);
        }
    }

    private void selectImage() {

        //Selecting image from gallery
        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(imageIntent,33);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Checking result
        if(requestCode== 33 && resultCode == RESULT_OK && data!=null)
        {
            //Getting Image
            Account2Activity.imageUri = data.getData();
            Dp.setImageURI(Account2Activity.imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            Toast.makeText(this, "Permission is necessary", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Account2Activity.imageUri = backupUri;
    }
}
