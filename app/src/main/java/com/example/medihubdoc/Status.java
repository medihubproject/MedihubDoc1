package com.example.medihubdoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Status extends AppCompatActivity {

    Switch online;
    TextView text,hint;
    Button call;
    public FirebaseAuth mFirebaseAuth;
    public FirebaseDatabase reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mFirebaseAuth = FirebaseAuth.getInstance();

        online = findViewById(R.id.onlineDoc);
        text = findViewById(R.id.textVIEW);
        call = findViewById(R.id.connectWithPatient);
        call.setVisibility(View.INVISIBLE);
        hint = findViewById(R.id.textView6);
        hint.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance();
        DatabaseReference ref = reference.getInstance().getReference().child("Doctors");
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(online.isChecked()){
                    text.setVisibility(View.VISIBLE);
                    text.setText(online.getTextOn());
                    String veri = "1";
                    HashMap hashMap= new HashMap();
                    hashMap.put("veri",veri);
                    ref.child(mFirebaseAuth.getUid()).updateChildren(hashMap);


                    call.setVisibility(View.VISIBLE);
                    hint.setVisibility(View.INVISIBLE);
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent inttoCall = new Intent(Status.this, MainActivity.class);
                            startActivity(inttoCall);
                        }
                    });

                }
                else{
                    text.setVisibility(View.INVISIBLE);
                    call.setVisibility(View.INVISIBLE);
                    hint.setVisibility(View.VISIBLE);
                    String veri = "0";
                    HashMap hashMap= new HashMap();
                    hashMap.put("veri",veri);
                    ref.child(mFirebaseAuth.getUid()).updateChildren(hashMap);
                }
            }
        });
    }
}