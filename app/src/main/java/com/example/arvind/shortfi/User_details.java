package com.example.arvind.shortfi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class User_details extends AppCompatActivity {
    FirebaseAuth mAuth;
    StorageReference sref;
    DatabaseReference mref;
    Firebase fref;
    TextView Uname;
    ImageView imageView;
    ProgressBar pbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        String[] t={"My videos","Switch account","Settings","Feedback","Sign out"};
        ListAdapter myadapter=new CustomAdapter(this,t);
        Uname=(TextView)findViewById(R.id.Uname);
        imageView=(ImageView)findViewById(R.id.imageView);
        mAuth=FirebaseAuth.getInstance();
        pbar=(ProgressBar)findViewById(R.id.pbar);
        pbar.setVisibility(imageView.VISIBLE);
        mref=FirebaseDatabase.getInstance().getReference().child("Users");
        sref= FirebaseStorage.getInstance().getReference().child("Profile_Images");
        ListView myview=(ListView)findViewById(R.id.list);
        myview.setAdapter(myadapter);
        if(mAuth.getCurrentUser()==null)
        {
            Uname.setText("Hello User!");
        }
        else {
            String uid = mAuth.getCurrentUser().getUid();
            String url = "https://shortfi-5926b.firebaseio.com/Users/" + uid + "/name";
            String url1="https://shortfi-5926b.firebaseio.com/Users/" + uid + "/image";
            fref = new Firebase(url);
            fref.addValueEventListener(new com.firebase.client.ValueEventListener() {
                @Override
                public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                    String n = dataSnapshot.getValue(String.class);
                    Uname.setText("Hello " + n+"!");
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            fref=new Firebase(url1);
            fref.addValueEventListener(new com.firebase.client.ValueEventListener() {
                @Override
                public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                    String durl = dataSnapshot.getValue(String.class);
                    Picasso.with(User_details.this).load(durl).fit().centerCrop().into(imageView);
                    pbar.setVisibility(imageView.GONE);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
        myview.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String val=String.valueOf(adapterView.getItemAtPosition(i));
                        Toast.makeText(User_details.this,val,Toast.LENGTH_SHORT).show();
                        switch (val)
                        {
                            case "Sign out":
                                if(mAuth.getCurrentUser()==null)
                                {
                                    Toast.makeText(User_details.this,"not signed in",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    mAuth.signOut();
                                    Intent intent=new Intent(User_details.this,LoggedIn.class);
                                    startActivity(intent);
                                    Home.close().finish();
                                    finish();

                                }
                                break;
                            case "Switch account":
                                if(mAuth.getCurrentUser()==null)
                                {
                                    Toast.makeText(User_details.this,"not signed in",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    mAuth.signOut();
                                    Intent intent=new Intent(User_details.this,MainActivity.class);
                                    startActivity(intent);
                                    Home.close().finish();
                                    finish();
                                }
                                break;

                        }
                    }
                }

        );

    }

}
