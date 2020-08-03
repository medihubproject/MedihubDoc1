package com.example.medihubdoc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class signup extends AppCompatActivity {

    EditText emailId,Password,regName,regPhone,dest1;
    Button btnSignup;
    TextView tvSignin;
    FirebaseAuth mFirebaseAuth;
    AlertDialog.Builder builder;

    public static String userId;
    RadioButton rb;
    ImageView img;
    Spinner spinSpec;
    private String[] listBG ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mFirebaseAuth= FirebaseAuth.getInstance();
        img=(ImageView) findViewById(R.id.SignupBack);
        emailId=findViewById(R.id.emailId);
        Password=findViewById(R.id.Password);
        btnSignup=findViewById(R.id.SignUp);
        tvSignin=findViewById(R.id.tvSignUp);
        regName=findViewById(R.id.regName);
        regPhone=findViewById(R.id.regPhone);
        builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);
        dest1=findViewById(R.id.dest);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spinSpec=findViewById(R.id.spinspec);


        listBG=getResources().getStringArray(R.array.blood_group);
        ArrayAdapter adapter = new ArrayAdapter(signup.this,android.R.layout.simple_spinner_item,listBG);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSpec.setAdapter(adapter);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressBar = new ProgressDialog(signup.this);
                progressBar.setCancelable(false);
                progressBar.setMessage("Registering...");
                progressBar.show();

                String name= regName.getText().toString();
                String email = emailId.getText().toString();
                String phoneNo = regPhone.getText().toString();
                String pwd1 = Password.getText().toString();


                userId= regPhone.getText().toString();
                builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                String spec = spinSpec.getSelectedItem().toString();

                String dest = dest1.getText().toString();


                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                    progressBar.dismiss();
                }
                else if(pwd1.isEmpty()){
                    Password.setError("Please enter your password");
                    Password.requestFocus();
                    progressBar.dismiss();
                }
                else if(email.isEmpty() && pwd1.isEmpty()){
                    Toast.makeText(signup.this,"Fields Are Empty!",Toast.LENGTH_LONG).show();
                    progressBar.dismiss();
                }
                else if(!email.matches(emailPattern)){
                    emailId.setError("Please enter valid email id");
                    emailId.requestFocus();
                    progressBar.dismiss();
                }
                else if(pwd1.length() <=7){
                    Password.setError("password too short");
                    Password.requestFocus();
                    progressBar.dismiss();
                }
                else if(phoneNo.length() != 10){
                    regPhone.setError("Enter correct phone number");
                    regPhone.requestFocus();
                    progressBar.dismiss();
                }

                else if(!(email.isEmpty() && pwd1.isEmpty())){
                    if (progressBar != null) {
                        progressBar.dismiss();
                    }
                    startActivity(new Intent(signup.this,otpverify.class));
                    Intent intent = new Intent(getApplicationContext(),otpverify.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("phoneNo",phoneNo);
                    bundle.putString("email",email);
                    bundle.putString("pwd1",pwd1);
                    bundle.putString("name",name);
                    bundle.putString("spec",spec);
                    bundle.putString("dest",dest);

                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(signup.this,"Unexpected Error Occured!",Toast.LENGTH_LONG).show();
                }
            }
        });


        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(signup.this, loginActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,loginActivity.class));
        finish();
    }
}