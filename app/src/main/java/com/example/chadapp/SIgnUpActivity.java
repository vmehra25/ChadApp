package com.example.chadapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SIgnUpActivity extends AppCompatActivity {

    EditText Email, Password, Confirm;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ImageView img = findViewById(R.id.SignUp_Ige);
        img.setScaleType(ImageView.ScaleType.FIT_XY);

        Email = findViewById(R.id.SignUp_Email1);
        Password = findViewById(R.id.SignUp_Password);
        Confirm = findViewById(R.id.SignUp_CofPass);
        mAuth = FirebaseAuth.getInstance();
    }

    public void Next(View view) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //progressDialog.getWindow().

        String email = Email.getText().toString(),
                password = Password.getText().toString(),
                confirm = Confirm.getText().toString();
                if(!(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm))&& Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    if(password.equals(confirm))
                    {
                        progressDialog.show();
                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful())
                                {
                                    progressDialog.dismiss();
                                    VerifyEmail();
                                    mAuth.signOut();
                                    startActivity(new Intent(SIgnUpActivity.this,LoginActivity.class));
                                }
                                else
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(SIgnUpActivity.this, "Error: "+task.getException().toString(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                    else
                        Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(this, "Please enter correct information", Toast.LENGTH_SHORT).show();
                }

    }
    void VerifyEmail()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SIgnUpActivity.this,
                            "We have sent you a mail to verify your email address please check",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(SIgnUpActivity.this, "Failed to send email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
