package com.example.medihubdoc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {

    EditText emailId,Password;
    Button btnSignin;
    TextView tvSignup,forgotPass;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        emailId = findViewById(R.id.emailid);
        Password = findViewById(R.id.Password);
        btnSignin = findViewById(R.id.Signin);
        tvSignup = findViewById(R.id.tvSignUp);
        forgotPass=findViewById(R.id.forgotPassword);


        builder = new AlertDialog.Builder(this);


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
                String pwd = Password.getText().toString();
                builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                if (email.isEmpty()) {
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    Password.setError("Please enter your password");
                    Password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(loginActivity.this, "Fields Are Empty!", Toast.LENGTH_LONG).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    final ProgressDialog progressBar = new ProgressDialog(loginActivity.this);
                    progressBar.setCancelable(false);
                    progressBar.setMessage("Please wait, Validating...");
                    progressBar.show();
                    firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (progressBar != null) {
                                progressBar.dismiss();
                            }
                            if (!task.isSuccessful()) {
                                builder.setMessage("Email/password is Incorrect ")
                                        .setCancelable(false)
                                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                startActivity(new Intent(loginActivity.this,loginActivity.class));
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.setTitle("Warning");
                                alert.show();
                            }
                            else {
                                Intent inttoHome = new Intent(loginActivity.this, Status.class);
                                startActivity(inttoHome);
                            }
                        }
                    });
                } else {
                    Toast.makeText(loginActivity.this, "Error Occured!", Toast.LENGTH_LONG).show();
                }
            }

        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this,forgotpass.class));
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSignup = new Intent(loginActivity.this, signup.class);
                startActivity(intSignup);
                finish();
            }
        });
    }
}