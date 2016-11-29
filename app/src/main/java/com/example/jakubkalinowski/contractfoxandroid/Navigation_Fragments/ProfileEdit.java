package com.example.jakubkalinowski.contractfoxandroid.Navigation_Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.jakubkalinowski.contractfoxandroid.Contractor;
import com.example.jakubkalinowski.contractfoxandroid.Homeowner;
import com.example.jakubkalinowski.contractfoxandroid.Member;
import com.example.jakubkalinowski.contractfoxandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileEdit.OnFragmentInteractionListener} interfaces
 * to handle interaction events.
 * Use the {@link ProfileEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileEdit extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String param = "kj";
    private static final String TAG = "Firebase_TAG!!" ;
    //[Firebase_variable]**
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //signed_in state listener object

    private static DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance()
            .getReference();

    private OnFragmentInteractionListener mListener;
    private Member m;

    //UI component variables [start]
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mAddressEditText;
    private EditText mCityEditText;
    private EditText mPostalCodeEditText;
    private EditText mPhoneNumberEditText;
    private EditText mCompanyNameEditText;
    private EditText mWebsiteURLEditText;

    //UI components [END]
    public ProfileEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileEdit.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileEdit newInstance(String param1, String param2) {
        ProfileEdit fragment = new ProfileEdit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    mFirebaseDatabaseReference
                            .child("usersInChat").child(user.getUid().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("contractorOption").getValue().equals(true)){


                                        //need null handlers here
                                        Contractor m = dataSnapshot.getValue(Contractor.class);
                                        mFirstNameEditText.setText(m.getFirstName());
                                        mLastNameEditText.setText(m.getLastName());
                                        mAddressEditText.setText(m.getAddress().toString());
                                        mCityEditText.setText(m.getAddress().getCity());
                                        mPostalCodeEditText.setText(m.getAddress().getZipCode());
                                        mPhoneNumberEditText.setText(m.getPhoneNo());
                                        mCompanyNameEditText.setText(m.getBusinessWebsiteURL());
                                        mWebsiteURLEditText.setText(m.getEmailAddress());
                                    }
                                    else{
                                        Homeowner m = (Homeowner)dataSnapshot.getValue(Homeowner.class);

                                        mFirstNameEditText.setText(m.getFirstName());
                                        mLastNameEditText.setText(m.getLastName());
                                        mAddressEditText.setText(m.getAddress().toString());
                                        mCityEditText.setText(m.getAddress().getCity());
                                        mPostalCodeEditText.setText(m.getAddress().getZipCode());
                                        mPhoneNumberEditText.setText(m.getPhoneNo());
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
                // ...
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //these fields are declared as global variables to be used in callback functions
        //for firebase data retrievl. If possible, find a way for data retrieval to happen
        // before oncreateVIew.
        View root = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        //mFirstNameEditText.setText(mFirstName_Textbox_Value);
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

//    public void setFirstName(String uid){
//
//
//        //there is a callback function inside here. To use outside variables inside the callback
//        //functions, the variable inside should be either final or global variable.
//        //Still trying to figure out the best way to handle callback functions.
//        mFirebaseDatabaseReference
//                .child("contractors").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Member m = dataSnapshot.getValue(Contractor.class);
//                firstName.setText(m.getEmailAddress());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirstNameEditText = (EditText) view.
                findViewById(R.id.firstName_editProfile_Fragment);
        mLastNameEditText = (EditText) view.findViewById(R.id.lasttName_editProfile_Fragment);
        mAddressEditText = (EditText) view.findViewById(R.id.address_editProfile_Fragment);
        mCityEditText = (EditText) view.findViewById(R.id.city_editProfile_Fragment);
        mPostalCodeEditText = (EditText)
                view.findViewById(R.id.postal_code_editProfile_fragment);
        mPhoneNumberEditText = (EditText)
                view.findViewById(R.id.phone_edit_profile_fragment);

        mCompanyNameEditText = (EditText)
                view.findViewById(R.id.company_name_editText_editProfile_fragment);
        mWebsiteURLEditText = (EditText)
                view.findViewById(R.id.website_url_editText_editProfile_fragment);

    }

}

