package com.example.jakubkalinowski.contractfoxandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EstimateActivity extends AppCompatActivity {

    private final String TAG = "ladimmm";
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    String firstName = "";

    private DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance()
            .getReference().getRoot();


    CameraPhoto cameraPhoto;
    static String currentUserId ;
    GalleryPhoto galleryPhoto;

    final int CAMERA_REQUEST = 12345;
    final int GALLERY_REQUEST = 12345;

    LinearLayout layout_interior, layout_exterior, layout_backyard, layout_description;
    RadioButton radio_interior, radio_exterior, radio_backyard;
    EditText project_description;
    ImageView ivCamera, ivGallery, ivUpload, ivImage;
    Button send ;

    Switch switchButton1, switchButton2;
    String switchOn = "YES";
    String switchOff = "NO";
    TextView textView1, textView2;
    static String [] ContracoorIds = new String[5];

    DatabaseReference messageReferencesDatabaseReference;
    DatabaseReference allMessagesDatabaseReference;

    static String receiverName,CurrentUsername, sendersName ;

    @Override
    protected void onStart() {
        super.onStart();

        mFirebaseDatabaseReference
                .child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sendersName = dataSnapshot.child(currentUserId).child("firstName").getValue(String.class);
                Log.i("gibsonSnap", firstName);

                receiverName = dataSnapshot.child(ContracoorIds[0]).child("firstName").getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUserId = user.getUid();
//            Log.i("ladimmm" ,user.getDisplayName());
//            Log.i("ladimmm" ,currentUserId);
        } else {
            // No user is signed in
            Log.i("ladimmm" ,"not signed in !!");
        }
//
//        messageReferencesDatabaseReference = FirebaseDatabase.getInstance()
//                .getReference().child("messageReferences");
//
//        allMessagesDatabaseReference = FirebaseDatabase.getInstance()
//                .getReference().child("allMessages");


        savedInstanceState = getIntent().getExtras();
        ContracoorIds = savedInstanceState.getStringArray("id");
        Log.d("i-d-est", ContracoorIds[0]);










        layout_interior = (LinearLayout)findViewById(R.id.interior_fragment_content_layout);
        layout_exterior = (LinearLayout)findViewById(R.id.exterior_fragment_content_layout);
        layout_backyard = (LinearLayout)findViewById(R.id.backyard_fragment_content_layout);
        send = ( Button)findViewById(R.id.sendButton);

        send.setOnClickListener( sendListener );


        radio_interior = (RadioButton)findViewById(R.id.radio_interior);
        radio_exterior = (RadioButton)findViewById(R.id.radio_exterior);
        radio_backyard = (RadioButton) findViewById(R.id.radio_backyard);

        project_description = (EditText)findViewById(R.id.description_paragraph_step3);

        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        ivImage = (ImageView)findViewById(R.id.ivImage);
        ivCamera = (ImageView)findViewById(R.id.ivCamera);
        ivGallery = (ImageView)findViewById(R.id.ivGallery);
//        ivUpload = (ImageView)findViewById(R.id.ivUpload);

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Something wrong while taking photos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });

        //----- STEP 2 -----

        // Switch Button 1
        switchButton1 = (Switch)findViewById(R.id.switch_q1);
        textView1 = (TextView) findViewById(R.id.textView1);

        switchButton1.setChecked(false);
        switchButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    textView1.setText(switchOn);
                } else {
                    textView1.setText(switchOff);
                }
            }
        });

        if (switchButton1.isChecked()) {
            textView1.setText(switchOn);
        } else {
            textView1.setText(switchOff);
        }
        // END Switch Button 1

        // Switch Button 2
        switchButton2 = (Switch)findViewById(R.id.switch_q2);
        textView2 = (TextView) findViewById(R.id.textView2);

        switchButton2.setChecked(false);
        switchButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    textView2.setText(switchOn);
                } else {
                    textView2.setText(switchOff);
                }
            }
        });

        if (switchButton2.isChecked()) {
            textView2.setText(switchOn);
        } else {
            textView2.setText(switchOff);
        }

        //lets get the names here.




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == CAMERA_REQUEST) {
                String photoPath = cameraPhoto.getPhotoPath();
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                    ivImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
            }
            else if(requestCode == GALLERY_REQUEST) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                    ivImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_interior:
                if (checked) {
                    radio_exterior.setChecked(false);
                    radio_backyard.setChecked(false);
                    layout_interior.setVisibility(View.VISIBLE);
                    layout_exterior.setVisibility(View.GONE);
                    layout_backyard.setVisibility(View.GONE);
                }
                break;
            case R.id.radio_exterior:
                if (checked) {
                    radio_interior.setChecked(false);
                    radio_backyard.setChecked(false);
                    layout_interior.setVisibility(View.GONE);
                    layout_exterior.setVisibility(View.VISIBLE);
                    layout_backyard.setVisibility(View.GONE);
                }
                break;
            case R.id.radio_backyard:
                if (checked) {
                    radio_interior.setChecked(false);
                    radio_exterior.setChecked(false);
                    layout_interior.setVisibility(View.GONE);
                    layout_exterior.setVisibility(View.GONE);
                    layout_backyard.setVisibility(View.VISIBLE);
                }
                break;
//            case R.id.project_description_edit_text:
//                if (checked) {
//                    layout_description.setVisibility(View.VISIBLE);
//                }
        }


    }

    View.OnClickListener sendListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //get thenames first


            String description = project_description.getText().toString();

            //adding myMessages attribute under each user.
//            Map<String , Object> reciverMyMessages = new HashMap<>();
//            reciverMyMessages.put("myMessages",  mFirebaseDatabaseReference.child("users").child(ContracoorIds[0]).getKey());
//
//            Map<String , Object> senderMyMessages = new HashMap<>();
//            senderMyMessages.put("myMessages", currentUserId);
//            //these tages have to be unique for each user. could be done at sign up by adding a myMessaages field to each user.
//            mFirebaseDatabaseReference.child("users").child(ContracoorIds[0]).updateChildren(reciverMyMessages); //receiver updated with reference  --this worked
//            mFirebaseDatabaseReference.child("users").child(currentUserId).updateChildren(senderMyMessages); //senderMsgRef is updateded with reference -- this didnt. couldnt get the current User ID

//////////////////////this part is done. Each user is apporpiraitely updated ////////////////////////////////
            //messageReference update below:
            Map<String, Object > messageReference = new HashMap<>();
            messageReference.put(currentUserId, "");
            messageReference.put(ContracoorIds[0], "");
            mFirebaseDatabaseReference.child("messageReferences").updateChildren(messageReference);
//////////////////////this part is done. IDs are added as immidiate children of message references.  ////////////////////////////////


            Map<String , Object> reciverMesList = new HashMap<>();
            Map<String , Object> senderMesList = new HashMap<>();

            senderMesList.put(ContracoorIds[0]+"/"+sendersName , currentUserId); //this value ---


            senderMesList.put(currentUserId+"/"+receiverName , ContracoorIds[0]);//this value ---


            mFirebaseDatabaseReference.child("messageReferences").updateChildren(senderMesList);
          //  mFirebaseDatabaseReference.child("messageReferences").child(currentUserId).updateChildren(reciverMesList);

///////////////////////this part is done ////////////////////////////////////////////////////////////////
            Map<String, Object > allMessageMap = new HashMap<>();
            allMessageMap.put(currentUserId+ContracoorIds[0], "");//this value ---
            mFirebaseDatabaseReference.child("allMessages").updateChildren(allMessageMap);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());

            Map<String, Object > initialMessageMap = new HashMap<>();
            initialMessageMap.put(currentDateandTime , description);
            mFirebaseDatabaseReference.child("allMessages").child(currentUserId+ContracoorIds[0]).updateChildren(initialMessageMap);

        }
    };

//---------------------------------------------------------------------
//    public void updateMessageNamesList (String id ){
//        Map<String , Object> someMap = new HashMap<>();
//
//        mFirebaseDatabaseReference
//                .child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                firstName = dataSnapshot.child("firstName").getValue(String.class);
//                Log.i("gibsonSnap", firstName);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        someMap.put(firstName , id);
//        messageReferencesDatabaseReference.child(id).setValue(someMap);
//
//    }
}