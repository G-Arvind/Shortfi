package com.example.arvind.shortfi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoProgressCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImageView;
import android.widget.VideoView;
import android.widget.MediaController;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoProgressCallback;

public class Add_videos extends AppCompatActivity implements EasyVideoCallback{

    Button addvid;
    EditText vidname, viddesc;
    public static int GALLERY_REQUEST = 1;
    private StorageReference sref;
    private Uri viduri = null;
    FirebaseAuth mAuth;
    private DatabaseReference mref;
    ProgressDialog progress;
    Button choosevid;
    VideoView video;
    private int cnf = -1;
    private EasyVideoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_videos);
        addvid = (Button) findViewById(R.id.addvid);
        vidname = (EditText) findViewById(R.id.vidname);
        viddesc = (EditText) findViewById(R.id.viddesc);
        vidname.requestFocus();
       // video = (VideoView) findViewById(R.id.video);
        player=(EasyVideoPlayer)findViewById(R.id.player);
        player.setCallback(this);
        player.seekTo( player.getCurrentPosition());
        player.hideControls();
        //MediaController mc = new MediaController(this);
        //mc.setAnchorView(video);
        //video.setMediaController(mc);
        progress = new ProgressDialog(this);
        progress.setMessage("Please wait");
        sref = FirebaseStorage.getInstance().getReference().child("Videos");
        mref = FirebaseDatabase.getInstance().getReference().child("Uploads");
        mAuth = FirebaseAuth.getInstance();
        choosevid = (Button) findViewById(R.id.choosevid);
        choosevid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });
        addvid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cnf == 0) {
                    progress.show();
                    addvideo();
                } else
                    Toast.makeText(Add_videos.this, "Please choose video", Toast.LENGTH_SHORT).show();
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {
            progress.show();
            viduri = data.getData();
            StorageReference riversRef = sref.child("files/" + viduri.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(viduri);
           // video.setVideoURI(viduri);
            player.setSource(viduri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progress.dismiss();
                    cnf = -1;
                    Toast.makeText(Add_videos.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progress.dismiss();
                    Toast.makeText(Add_videos.this, "Upload Success", Toast.LENGTH_SHORT).show();
                    cnf = 0;
                }
            });
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {}

    @Override
    public void onPaused(EasyVideoPlayer player) {}

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {
        Log.d("EVP-Sample", "onPrepared()");
    }

    @Override
    public void onBuffering(int percent) {
        Log.d("EVP-Sample", "onBuffering(): " + percent + "%");
    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {
        Log.d("EVP-Sample", "onError(): " + e.getMessage());

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {
        Log.d("EVP-Sample", "onCompletion()");
    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {
        Toast.makeText(this, "Retry", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {
        Toast.makeText(this, "Submit", Toast.LENGTH_SHORT).show();
    }





    void addvideo() {
       final String n = vidname.getText().toString();
       final String d = viddesc.getText().toString();
        if (vidname.getText().toString().isEmpty()) {
            vidname.setError("email is required");
            Toast.makeText(Add_videos.this, "Please enter video name", Toast.LENGTH_SHORT).show();
        } else {
            if (n != null) {
                final String uid = mAuth.getCurrentUser().getUid();
                StorageReference filepath = sref.child(viduri.getLastPathSegment());
                filepath.putFile(viduri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String duri = taskSnapshot.getDownloadUrl().toString();
                        DatabaseReference temp= mref.child(uid);
                        temp.child("video").setValue(duri);
                        temp.child("Name").setValue(n);
                        temp.child("Desciption").setValue(d);
                        progress.dismiss();
                        Intent i = new Intent(Add_videos.this, LoggedIn.class);
                        startActivity(i);
                        finish();
                    }
                });
            } else {
                Toast.makeText(Add_videos.this, "Please enter video name", Toast.LENGTH_SHORT).show();
            }
        }
    }
    }

