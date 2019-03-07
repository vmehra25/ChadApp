package com.example.chadapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText Email, Password;
    TextView signUp, ForgotPass,msg;
     FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView img = findViewById(R.id.Login_Ige);
        img.setScaleType(ImageView.ScaleType.FIT_XY);


            mAuth = FirebaseAuth.getInstance();


        Email = findViewById(R.id.Login_Email1);
        Password = findViewById(R.id.Login_Password);

        signUp = findViewById(R.id.Login_SignUp);
        ForgotPass = findViewById(R.id.Login_Forgot);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SIgnUpActivity.class));
            }
        });
    }

    public void Login(View view) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);


        String email = Email.getText().toString(),
                password = Password.getText().toString();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            progressDialog.show();
            try{
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    if(mAuth.getCurrentUser().isEmailVerified())
                    {
                        startMainActivity(mAuth.getCurrentUser().getUid());
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Please verify your email first", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                    progressDialog.dismiss();


                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Error: "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
            }
        });

    }
    catch (Exception e)
    {}}

    }
     void startMainActivity(final String userId)
    {

        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userId).exists())
                {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(LoginActivity.this,Account2Activity.class);
                   // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
