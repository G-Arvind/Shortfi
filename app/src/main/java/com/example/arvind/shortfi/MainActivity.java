package com.example.arvind.shortfi;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.support.v4.view.ViewPager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.ProgressDialog;


import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends AppCompatActivity {

   public boolean connected;
    Button login,reg,register,skip;
    EditText user,pass;
    FirebaseAuth mAuth;
    ProgressDialog progress;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mref;
    private Firebase fref;
    private static final String TAG="MainActivity";
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=(Button)findViewById(R.id.login);
        reg=(Button)findViewById(R.id.reg);
        register=(Button)findViewById(R.id.register);
        user=(EditText)findViewById(R.id.user);
        pass=(EditText)findViewById(R.id.pass);
        user.requestFocus();
        skip=(Button) findViewById(R.id.skip);
        mAuth=FirebaseAuth.getInstance();
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

        authStateListener=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    Intent i = new Intent(MainActivity.this, Home.class);
                    startActivity(i);
                    finish();
                }

            }
        };


        reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,Register.class);
                    startActivity(intent);
                }
            });


            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(connected==false)
                        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    else
                    startSignIn();
                }

            });

        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progress!=null&&progress.isShowing())
        {
            progress.cancel();
        }
    }

    void startSignIn() {
        if (user.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Empty", Toast.LENGTH_SHORT).show();
            user.setError("email is required!");
        }else if (pass.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Empty", Toast.LENGTH_SHORT).show();
            pass.setError("password is required!");
        } else {

            String u = user.getText().toString();
            String p = pass.getText().toString();
            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(MainActivity.this, "Empty", Toast.LENGTH_SHORT).show();
            } else {
                progress.show();
                mAuth.signInWithEmailAndPassword(u, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid=mAuth.getCurrentUser().getUid();
                            Log.d(TAG,uid);
                            String url="https://shortfi-5926b.firebaseio.com/Users/"+uid+"/image";
                            fref=new Firebase(url);
                            fref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String value=dataSnapshot.getValue(String.class);
                                    Log.d(TAG,value);
                                    if(value.equals("default"))
                                    {
                                        progress.dismiss();
                                        Intent i = new Intent(MainActivity.this, Image_choose.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    else {
                                        progress.dismiss();
                                        //Intent i = new Intent(MainActivity.this, Home.class);
                                        //startActivity(i);
                                        //finish();
                                    }

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });

                        } else {
                            progress.dismiss();
                            Toast.makeText(MainActivity.this, "Username/password doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                );
            }
        }
    }

}
