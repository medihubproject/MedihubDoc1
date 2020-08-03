package com.example.medihubdoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpass extends AppCompatActivity {

    EditText passEmail;
    Button resetPass;
    ImageView img;
    private FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        img=(ImageView) findViewById(R.id.back2);

        passEmail=findViewById(R.id.emailToReset);
        resetPass=findViewById(R.id.resetPass);
        mFirebaseAuth=FirebaseAuth.getInstance();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail =passEmail.getText().toString().trim();

                if(userEmail.equals("")){
                    passEmail.setError("email required");
                    passEmail.requestFocus();
                }
                else{
                    mFirebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(forgotpass.this,"reset password link sent to mail Successfully",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(forgotpass.this,loginActivity.class));
                            }
                            else{
                                Toast.makeText(forgotpass.this,"user email not registered",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}