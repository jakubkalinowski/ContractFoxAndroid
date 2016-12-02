package com.example.jakubkalinowski.contractfoxandroid.Navigation_Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jakubkalinowski.contractfoxandroid.Contractor;
import com.example.jakubkalinowski.contractfoxandroid.DrawerActivity;
import com.example.jakubkalinowski.contractfoxandroid.Homeowner;
import com.example.jakubkalinowski.contractfoxandroid.PicGalleryActivity;
import com.example.jakubkalinowski.contractfoxandroid.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interfaces
 * to handle interaction events.
 * Use the {@link MyProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    //    String param = "kj";
    private static final String TAG = "Firebase_TAG!!" ;
    //[Firebase_variable]**
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //signed_in state listener object

    private DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance()
            .getReference();

    private OnFragmentInteractionListener mListener;
//    private Member m;

    //UI component variables

    private TextView address, phoneNumber, companyName, website, emailAddress, fullName, miles;
    private LinearLayout callButton, directionsButton, websiteButton, skillsButton, reviewsButton,
            galleryButton;

    private Button mAddBeforeImageToGallery, mAddAfterImageToGallery;

    // picture gallery storage reference
//    private StorageReference mStorageReference;
    private StorageReference mProfilePicPath;
    private StorageReference mLogoImagesPath;

    //progress dialog
    private ProgressDialog mProgressDialog;

    private ImageView mImageView1, mImageView2, mImageView3, mImageView4;

    private ImageView profilePicture, logoPicture;

    // integer for request code
    private static final int GALLERY_INTENT = 2;

    //imageView
    private CircleImageView mCircleProfileImageView;
    private Bitmap mProfileImageBitmap;

    Boolean isContractor;


    private String contractorID = DrawerActivity.currentUserId;

    private int i=0;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference galleryImg;
    //    private StorageReference pathReference = storageRef.child("Before&AfterPictureGallery/1.jpg");
    private StorageReference filePath;
    private Boolean before ;
    private String webInput;
    public MyProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfile newInstance(String param1, String param2) {
        MyProfile fragment = new MyProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReferenceFromUrl("gs://contract-fox.appspot.com ");
//        galleryImg = storageRef.child("Before&AfterPictureGallery/"+contractorID);

        isContractor = true;

//        mAuth = FirebaseAuth.getInstance();
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                contractorID = user.getUid().toString();
//
//                if (user != null) {
//                    // User is signed in
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
////                    storage = FirebaseStorage.getInstance();
////                    storageRef = storage.getReferenceFromUrl("gs://contract-fox.appspot.com ");
//
//
//                    galleryImg = storage.getReference("Before&AfterPictureGallery/"+contractorID);


//                    // Download profile picture
//                    mProfilePicPath = FirebaseStorage.getInstance().getReference("ProfilePictures/"+contractorID+"/profilepic.jpeg");
//                    mLogoImagesPath = FirebaseStorage.getInstance().getReference("LogoImages/"+contractorID+"/logoimg.jpeg");
////                    mProfilePicPath = FirebaseStorage.getInstance().getReference("users/"+contractorID);
////                    mProfilePicPath = FirebaseStorage.getInstance().getReference("users/"+contractorID+"/profilePicture.jpeg");
////                    String profilePic = mProfilePicPath.getDownloadUrl().toString();
////                    Picasso.with(getActivity()).load(profilePic).into(profilePicture);     TESTING
//                    //Profo --->
//                    Glide.with(getActivity())
//                            .using(new FirebaseImageLoader())
//                            .load(mProfilePicPath)
//                            .into(profilePicture);
//                    //logo ---->
//                    Glide.with(getActivity())
//                            .using(new FirebaseImageLoader())
//                            .load(mLogoImagesPath)
//                            .into(logoPicture);


        // Download logo picture
//                    mLogoImagesPath = FirebaseStorage.getInstance().getReference("logoImages/"+contractorID);
////                    mLogoImagesPath = FirebaseStorage.getInstance().getReference("logoImages/"+contractorID+"/logo.jpeg");
//                    String logo = mLogoImagesPath.getDownloadUrl().toString();
//                    Picasso.with(getActivity()).load((logo)).into(logoPicture);

        // Download picture gallery
//                    mStorageReference = FirebaseStorage.getInstance().getReference("Before&AfterPictureGallery");

//                    // Download profile info
//                    mFirebaseDatabaseReference
//                            .child("users").child(user.getUid().toString())
//                            .addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.child("contractorOption").getValue().equals(true)){
//
//                                        //need null handlers here
//                                        Contractor m = dataSnapshot.getValue(Contractor.class);
//
//                                        address.setText(m.getAddress().toString());
//                                        phoneNumber.setText(m.getPhoneNo());
//                                        companyName.setText(m.getCompanyName());
//                                        website.setText(m.getBusinessWebsiteURL());
//                                        //miles.setText();
//                                        isContractor = true;
//
//                                    }
//                                    else{
//                                        Homeowner m = dataSnapshot.getValue(Homeowner.class);
//
//                                        address.setText(m.getAddress().toString());
//                                        phoneNumber.setText(m.getPhoneNo());
//                                        //fullName.setText(m.getFullName().toString());
//
//                                        isContractor = false;
//                                    }
//                                }
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                } else {
//                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                }
//                // ...
//            }
//        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root;

//        if (isContractor == true) {

        root = inflater.inflate(R.layout.fragment_contractor_profile, container, false);
//        }else {
//            root = inflater.inflate(R.layout.fragment_homeowner_profile, container, false);
//        }
//        View root = inflater.inflate(R.layout.activity_contractor_profile, container, false);
        //TODO: fetch contractorOption from DB to set the if statement
//        View root;
//        if(m.getContractorOption().equals(true)) {
//            root = inflater.inflate(R.layout.fragment_contractor_profile, container, false);
//        } else {
//            root = inflater.inflate(R.layout.fragment_homeowner_profile, container, false);
//        }
        // return inflater.inflate(R.layout.fragment_my_profile, container, false);
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interfaces must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profilePicture = (ImageView)view.findViewById(R.id.profile_fragment_picture);
        logoPicture = (ImageView)view.findViewById(R.id.logo_fragment_picture);

        address = (TextView) view.findViewById(R.id.address_string);
        companyName = (TextView) view.findViewById(R.id.company_name);
        phoneNumber = (TextView) view.findViewById(R.id.call_text);
        website = (TextView) view.findViewById(R.id.website_url);
        fullName = (TextView) view.findViewById(R.id.full_name);

        mAddBeforeImageToGallery = (Button)view.findViewById(R.id.add_before_image_to_gallery_button);
        mAddAfterImageToGallery = (Button)view.findViewById(R.id.add_after_image_to_gallery_button);
        websiteButton = (LinearLayout) view.findViewById(R.id.website_button);
        skillsButton = (LinearLayout) view.findViewById(R.id.skills_button);
        reviewsButton = (LinearLayout) view.findViewById(R.id.reviews_button);
        galleryButton = (LinearLayout) view.findViewById(R.id.pic_gallery_button);


//        mImageView1 = (ImageView) view.findViewById(R.id.gallery_image1);
//        mImageView2 = (ImageView) view.findViewById(R.id.gallery_image2);
//        mImageView3 = (ImageView) view.findViewById(R.id.gallery_image3);
//        mImageView4 = (ImageView) view.findViewById(R.id.gallery_image4);

        mProgressDialog = new ProgressDialog(getActivity());

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PicGalleryActivity.class);
                i.putExtra("id", contractorID);
                startActivity(i);
            }
        });

        mAddBeforeImageToGallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent i = new Intent(Intent.ACTION_PICK);
                before = true;
                i.setType("image/*");
//                i.putExtra("before", before);
                startActivityForResult(i, GALLERY_INTENT);
            }
        });

        mAddAfterImageToGallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                Intent i = new Intent(Intent.ACTION_PICK);
                before = false;
                i.setType("image/*");
//                i.putExtra("before", before);
                startActivityForResult(i, GALLERY_INTENT);
            }
        });

        websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!webInput.startsWith("http://") && !webInput.startsWith("https://")) {
                    webInput = "http://" + webInput;
                }

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(webInput));
                startActivity(i);
            }
        });

        storage = FirebaseStorage.getInstance();
//        mAuth = FirebaseAuth.getInstance();
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                contractorID = user.getUid().toString();
//
//                if (user != null) {
//                    // User is signed in
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
////                    storage = FirebaseStorage.getInstance();
////                    storageRef = storage.getReferenceFromUrl("gs://contract-fox.appspot.com ");
//


        galleryImg = storage.getReference("Before&AfterPictureGallery/"+contractorID);

        // Download profile picture
        mProfilePicPath = FirebaseStorage.getInstance().getReference("ProfilePictures/"+contractorID+"/profilepic.jpeg");
        mLogoImagesPath = FirebaseStorage.getInstance().getReference("LogoImages/"+contractorID+"/logoimg.jpeg");
//                    mProfilePicPath = FirebaseStorage.getInstance().getReference("users/"+contractorID);
//                    mProfilePicPath = FirebaseStorage.getInstance().getReference("users/"+contractorID+"/profilePicture.jpeg");
//                    String profilePic = mProfilePicPath.getDownloadUrl().toString();
//                    Picasso.with(getActivity()).load(profilePic).into(profilePicture);     TESTING
        //Profo --->
        Glide.with(getActivity())
                .using(new FirebaseImageLoader())
                .load(mProfilePicPath)
                .into(profilePicture);
        //logo ---->
        Glide.with(getActivity())
                .using(new FirebaseImageLoader())
                .load(mLogoImagesPath)
                .into(logoPicture);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

//                contractorID = user.getUid().toString();

                contractorID = DrawerActivity.currentUserId;

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
        // Download profile info
        mFirebaseDatabaseReference
                .child("users").child(contractorID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("contractorOption").getValue().equals(true)){

                            //need null handlers here
                            Contractor m = dataSnapshot.getValue(Contractor.class);

                            address.setText(m.getAddress().toString());
                            phoneNumber.setText(m.getPhoneNo());
                            companyName.setText(m.getCompanyName());
                            website.setText(m.getBusinessWebsiteURL());
                            //miles.setText();
                            webInput = dataSnapshot.child("businessWebsiteURL").getValue().toString();
                            website.setText(webInput);

                        }
                        else{
                            Homeowner m = dataSnapshot.getValue(Homeowner.class);

                            address.setText(m.getAddress().toString());
                            phoneNumber.setText(m.getPhoneNo());
                            //fullName.setText(m.getFullName().toString());
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

//            Bundle b = data.getExtras();
//            before = b.getBoolean("before");



            mProgressDialog = ProgressDialog.show(getActivity(), "Uploading ...", "Please wait...", true);
            mProgressDialog.show();

            final Uri uri = data.getData();

            if (before == true){
                filePath = galleryImg.child("img");
            } else {
                filePath = galleryImg.child("img2");
            }


            //TODO: add random name instead of last path .child(uri.getLastPathSegment())
//            final StorageReference filePath = mStorageReference.child("Before&AfterPictureGallery")
//                    .child(contractorID).child(uri.getLastPathSegment());
//            StorageReference filePath = galleryImg.child(contractorID);

            //TODO: add picture to the list not on top of another
//             i=0;
//                filePath = galleryImg.child("img");

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                    i++;
                    mProgressDialog.dismiss();

                    Intent i = new Intent(getActivity().getApplicationContext(), PicGalleryActivity.class);
                    i.putExtra("id", contractorID);
                    i.putExtra("before", before);
//                    i.putExtra("uri", uri.toString());
                    startActivity(i);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }
}
