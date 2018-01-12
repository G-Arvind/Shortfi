package com.example.arvind.shortfi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.view.View;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity {
    Button register;
    EditText user,pass,name;
    FirebaseAuth mAuth;
    ProgressDialog progress;
    Boolean connected;
    private DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = (Button) findViewById(R.id.register);
        mAuth=FirebaseAuth.getInstance();
        mref=FirebaseDatabase.getInstance().getReference().child("Users");
        user=(EditText)findViewById(R.id.user);
        pass=(EditText)findViewById(R.id.pass);
        name=(EditText)findViewById(R.id.name);
        name.requestFocus();
        progress=new ProgressDialog(this);
        progress.setMessage("please wait");


        connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else
            connected = false;
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connected==false)
                    Toast.makeText(Register.this, "No Active Internet Connection", Toast.LENGTH_LONG).show();
                else
                    startSignUp();
            }
        });



    }
    void startSignUp() {
        if (name.getText().toString().isEmpty()) {
            Toast.makeText(Register.this, "Empty", Toast.LENGTH_SHORT).show();
            name.setError("name is required!");
        } else if (user.getText().toString().isEmpty()) {
            Toast.makeText(Register.this, "Empty", Toast.LENGTH_SHORT).show();
            user.setError("email is required!");
        } else if (pass.getText().toString().isEmpty()) {
            Toast.makeText(Register.this, "Empty", Toast.LENGTH_SHORT).show();
            pass.setError("password is required!");
        } else {
            progress.show();
            final String u = user.getText().toString();
            String p = pass.getText().toString();
            String n = name.getText().toString();
            if (u.isEmpty() || p.isEmpty() ||n.isEmpty()) {
                Toast.makeText(Register.this, "Empty", Toast.LENGTH_SHORT).show();
            } else {

                mAuth.createUserWithEmailAndPassword(u, p)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    String n = name.getText().toString();
                                    String uid=mAuth.getCurrentUser().getUid();
                                    DatabaseReference user_db=mref.child(uid);
                                    user_db.child("name").setValue(n);
                                    user_db.child("image").setValue("default");

                                    progress.dismiss();
                                    Intent intent = new Intent(Register.this, Image_choose.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    progress.dismiss();
                                    Toast.makeText(Register.this, "Already registered",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        }
    }

    }

