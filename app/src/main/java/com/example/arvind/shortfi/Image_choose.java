package com.example.arvind.shortfi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Image_choose extends AppCompatActivity {

    ImageButton dp;
    Button submitdp;
    public static int GALLERY_REQUEST=1;
    private StorageReference sref;
    private Uri imageuri=null;
    FirebaseAuth mAuth;
    private DatabaseReference mref;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_choose);
        progress=new ProgressDialog(this);
        progress.setMessage("Please wait");
        dp=(ImageButton)findViewById(R.id.dp);
        submitdp=(Button)findViewById(R.id.submitdp);
        mAuth= FirebaseAuth.getInstance();
        sref= FirebaseStorage.getInstance().getReference().child("Profile_Images");
        mref= FirebaseDatabase.getInstance().getReference().child("Users");
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery_intent=new Intent();
                gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
                gallery_intent.setType("image/*");
                startActivityForResult(gallery_intent,GALLERY_REQUEST);
            }
        });
        submitdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageuri!=null) {
                    progress.show();
                    Setdp();
                }
                else
                    Toast.makeText(Image_choose.this,"Please choose image",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK)
        {
                   Uri imguri=data.getData();
            CropImage.activity(imguri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK)
            {
                 imageuri=result.getUri();
                 dp.setImageURI(imageuri);
            }else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error=result.getError();

            }

        }

    }
    void Setdp()
    {
        final String uid=mAuth.getCurrentUser().getUid();
        StorageReference filepath=sref.child(imageuri.getLastPathSegment());
        filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String duri=taskSnapshot.getDownloadUrl().toString();
                mref.child(uid).child("image").setValue(duri);
                progress.dismiss();
                Intent i=new Intent(Image_choose.this,LoggedIn.class);
                startActivity(i);
                finish();
            }
        });
    }


}


