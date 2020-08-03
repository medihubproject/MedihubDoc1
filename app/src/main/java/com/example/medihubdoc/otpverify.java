package com.example.medihubdoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class otpverify extends AppCompatActivity {

    Button otp;
    EditText otpByUser;
    String codeSent;

    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);
        mFirebaseAuth=FirebaseAuth.getInstance();

        otp=findViewById(R.id.otpVerify);            //button
        otpByUser=findViewById(R.id.phoneOtp);       //editText
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

        final Bundle bundle=getIntent().getExtras();
        final String uEmail=bundle.getString("email");
        final String uPwd=bundle.getString("pwd1");
        String phN =bundle.getString("phoneNo");

        sendVerificationCode(phN);             //sendVerificationCodeToUser


        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressBar = new ProgressDialog(otpverify.this);
                progressBar.setCancelable(false);
                progressBar.setMessage("Registering...");
                progressBar.show();

                final String code=otpByUser.getText().toString();

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Doctors");


                String name=bundle.getString("name");
                String email=bundle.getString("email");
                String phoneNo=bundle.getString("phoneNo");
                String pwd1=bundle.getString("pwd1");
                String spec=bundle.getString("spec");
                String dest=bundle.getString("dest");
                String status="0";
                String veri="0";

                final UserHelperClass helperClass = new UserHelperClass(name, email, phoneNo, pwd1, spec, dest, status, veri);


                if(code.isEmpty() || code.length() < 6){
                    otpByUser.setError("Wrong OTP...");
                    otpByUser.requestFocus();
                    progressBar.dismiss();
                    return;
                }
                else if(!code.isEmpty())
                {
                    mFirebaseAuth.createUserWithEmailAndPassword(uEmail,uPwd).addOnCompleteListener(otpverify.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                builder.setMessage("SignUp Unsuccessful, Please try Login")
                                        .setCancelable(false)
                                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                startActivity(new Intent(otpverify.this,signup.class));
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();
                                                progressBar.dismiss();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.setTitle("Warning");
                                alert.show();
                            }else{
                                verifyCode(code);
                                reference.child(mFirebaseAuth.getUid()).setValue(helperClass);
                                Toast.makeText(otpverify.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),loginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });


    }

    private void sendVerificationCode(String phN){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phN,        // Phone number to verify
                45,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code=phoneAuthCredential.getSmsCode();
            if(code!=null){

            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(otpverify.this,"incorrect otp",Toast.LENGTH_SHORT).show();
        }

    };

    private void verifyCode(String codeByUser){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent,codeByUser);
        signIn(credential);
    }

    private void signIn(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(otpverify.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(otpverify.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(otpverify.this,loginActivity.class));
    }

}