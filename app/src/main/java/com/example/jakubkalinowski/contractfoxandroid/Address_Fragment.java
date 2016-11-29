package com.example.jakubkalinowski.contractfoxandroid;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jakubkalinowski.contractfoxandroid.googleMapsApi.PlaceAutocompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


// Packages required for importing google maps API
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
//


/**
 * A simple {@link Fragment} subclass.
 */
public class Address_Fragment extends Fragment implements OnConnectionFailedListener {

    //textWrappers
    private TextInputLayout mStreetAddressWrapper;
    private TextInputLayout mUnitAptWrapper;
    private TextInputLayout mCityWrapper;
    private TextInputLayout mStateWrapper;
    private TextInputLayout mZipWrapper;

    //inputTexts
    private EditText mStreetAddressEditText;
    private EditText mUnitAptEditText;
    private EditText mCityEditText;
    private EditText mStateEditText;
    private EditText mZipEditText;

    //google maps required variables
    private AutoCompleteTextView mAutoCompleteTextView;
    private PlaceAutocompleteAdapter mAdapter;
    private static final LatLngBounds BOUNDS_USA = new LatLngBounds(
            new LatLng(32.6393, -117.004304), new LatLng(44.901184, -67.32254));


    //registerButton
    private Button mRegisterButton;

    //Strings from previous framgents--> both kind of usersInChat have this
    private String mEmailValueFromPrevious;
    private String mPasswordValueFromPrevious;
    private String mFirstNameValueFromPrevious;
    private String mLastNameValueFromPrevious;
    private String mPhoneValueFromPrevious;
    private byte[] mBytesArrayFromPrevious;
    private Boolean mContractorBooleanValueFromPrevious;  //true for contractor\false for otherwise


    //Strings from previous fragments--> only contractor fragment is passing this
    private String mContractorCompanyValueFromPrevious;
    private String mContractorWebsiteValueFromPrevious;
    private String mContractorDescriptionValueFromPrevious;
    private ArrayList<String> mContractorSkillsetValueFromPrevious;


    //Strings from currentFragment
    private String mStreetValue;
    private String mUnitAptValue;
    private String mCityValue;
    private String mStateValue;
    private String mZipValue;

    private String TAG = "FirebaseTag";


    //googleAPI client instance
    private GoogleApiClient mGoogleApiClient;

    //FIREBASE ESSENTIALS to login
    //auth instance variable
    private FirebaseAuth mAuth;

    //listener to listen to authState Changes
    private FirebaseAuth.AuthStateListener mAuthListener;

    //reference to realtime database
    private DatabaseReference mDatabase;

    //reference to storage database
    private FirebaseStorage mStorage;
    //refference to profilepicture storage
    private StorageReference mProfileImageStorageRef;

    public Address_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //get them bundle values here.
        ((registerActivity) getActivity()).setTopToolBar("Enter Address");
        //Contractor & Homeowner
        mEmailValueFromPrevious = getArguments().getString("emailAddress");
        mPasswordValueFromPrevious = getArguments().getString("password");
        mFirstNameValueFromPrevious = getArguments().getString("firstname");
        mLastNameValueFromPrevious = getArguments().getString("lastname");
        mPhoneValueFromPrevious = getArguments().getString("phone");
        mBytesArrayFromPrevious = getArguments().getByteArray("profileImageData");
        mContractorBooleanValueFromPrevious = getArguments().getBoolean("typeBoolean");

        if (mContractorBooleanValueFromPrevious == true) {
            mContractorCompanyValueFromPrevious = getArguments().getString("companyName");
            mContractorWebsiteValueFromPrevious = getArguments().getString("websiteURL");
            mContractorDescriptionValueFromPrevious = getArguments().getString("companyDescription");
            mContractorSkillsetValueFromPrevious = getArguments().
                    getStringArrayList("contractor_skillset");
        }

        return inflater.inflate(R.layout.fragment_address_layout, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStreetAddressWrapper = (TextInputLayout)
                view.findViewById(R.id.street_address_wrapper_fragment_address_homeowner);
        mUnitAptWrapper = (TextInputLayout)
                view.findViewById(R.id.unit_apt_wrapper_register_address_homeowner_fragment);
        mCityWrapper = (TextInputLayout)
                view.findViewById(R.id.city_wrapper_fragment_homeownerRegisterAddress);
        mStateWrapper = (TextInputLayout)
                view.findViewById(R.id.state_wrapper_fragment_homeownerRegisterAddress);
        mZipWrapper = (TextInputLayout)
                view.findViewById(R.id.zipCode_wrapper_fragment_homeownerRegisterAddress);
        mAutoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.
                autocomplete_places_registration_address_fragment);


        mStreetAddressWrapper.setHint("Street Address");
        mUnitAptWrapper.setHint("Unit/Apt #");
        mStateWrapper.setHint("State");
        mCityWrapper.setHint("City");
        mZipWrapper.setHint("Zip code");

        mRegisterButton = (Button)
                view.findViewById(R.id.sign_up_button_registerHomeownerAddressButton);

        mStreetAddressEditText = (EditText)
                view.findViewById(R.id.street_address_edit_text_fragment_homeowner_register_address);
        mUnitAptEditText = (EditText)
                view.findViewById(R.id.unit_apt_editText_register_address_homeowner_fragment);
        mCityEditText = (EditText)
                view.findViewById(R.id.city_edittextfield_fragment_homeOwnerAddress_fragment);
        mStateEditText = (EditText)
                view.findViewById(R.id.state_edittextfield_fragment_homeOwnerAddress_fragment);
        mZipEditText = (EditText)
                view.findViewById(R.id.zipCode_edittextfield_fragment_homeOwnerRegister_fragment);


        //--GOOGLE MAPS AUTO COMPLETE --[START]
        mAutoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient,
                BOUNDS_USA, new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build());

        mAutoCompleteTextView.setAdapter(mAdapter);

        //--GOOGLE MAPS AUTO COMPLETE --[END]

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                registerNewUserAfterAddressValidation();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //gets firebase current authorization instance, can use this to get current user

        mAuth = FirebaseAuth.getInstance();

        //Firebase realtime Database Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Firebase storage reference
        mStorage = FirebaseStorage.getInstance();

        //
        mProfileImageStorageRef = mStorage.getReferenceFromUrl
                ("gs://contract-fox.appspot.com/usersInChat/");


        //setting the authlister always listening for changes
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in after first time creation
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Address address = new Address(mStreetValue, mCityValue,
                            mStateValue, mZipValue, mUnitAptValue);

                    String signedIn_userID_key = user.getUid().toString();

                    if (mContractorBooleanValueFromPrevious == false) {
                        Member new_homeOwner_member = new Homeowner(mFirstNameValueFromPrevious,
                                mLastNameValueFromPrevious, mEmailValueFromPrevious,
                                mPhoneValueFromPrevious, mContractorBooleanValueFromPrevious, address);
                        mDatabase.child("usersInChat").child(signedIn_userID_key).
                                setValue(new_homeOwner_member);
                        mDatabase.child("user_addresses").child(signedIn_userID_key).setValue(address);
                    } else {
                        Map<String, Boolean> skillset = new HashMap<>();
                        if(!mContractorSkillsetValueFromPrevious.isEmpty()){
                            for(String skill : mContractorSkillsetValueFromPrevious){
                                skillset.put(skill, true);
                            }
                        }
                        Member new_contractor_member = new Contractor(mFirstNameValueFromPrevious,
                                mLastNameValueFromPrevious, mEmailValueFromPrevious,
                                mPhoneValueFromPrevious, mContractorBooleanValueFromPrevious, address,
                                mContractorDescriptionValueFromPrevious,
                                skillset,
                                mContractorWebsiteValueFromPrevious);
                        mDatabase.child("usersInChat").child(signedIn_userID_key).
                                setValue(new_contractor_member);

                        mDatabase.child("user_addresses").child(signedIn_userID_key).setValue(address);
                    }


                    //This section of code uploads picture to authorized user--[START]//This uploads
                    //The picture requested, this section of code is from firebase website
                    //This piece of code below applies to both contractor and homeOwner
                    mProfileImageStorageRef = mStorage.getReferenceFromUrl
                            ("gs://contract-fox.appspot.com/usersInChat/" + signedIn_userID_key +
                                    "/profilePicture.jpg");
                    //2 lines of code below essentially uploads image to firebase.
                    UploadTask uploadTask = mProfileImageStorageRef.
                            putBytes(mBytesArrayFromPrevious);
                    //Attaching callback methods listening for failures and successes
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    });

                    //This section of code uploads pictures to authorized user--[END]


                    //signs out user and finishes all fragments/activities

                    signOutAndFinishFragments();


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                    //as soon as user state is changed, this is called.
                }
                // ...
            }
        };

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

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


    public void signOutAndFinishFragments() {
        mAuth.signOut();

        //this line of code el
//        FragmentManager Fm = getActivity().getSupportFragmentManager().beginTransaction().
//                remove(Address_Fragment.this).commit();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.popBackStack("HomeownerRegisterProfileFragment",
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        manager.popBackStack("ContractorRegisterProfileFragment",
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().getSupportFragmentManager().beginTransaction().
                remove(Address_Fragment.this).commit();

        ((registerActivity) getActivity()).finish();
        Activity loginActivity = getActivity();
        if (loginActivity instanceof LoginActivity) {
            ((LoginActivity) loginActivity).showProfileCreatedSuccessMessage();
        }

    }


    /**
     * Have to come back to this later
     */
    public void recieveData() {
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mContractorBooleanValueFromPrevious == true) {
            ((RegisterContractorFragment) getParentFragment()).setTopToolBar();
        } else {
            ((RegisterHomeownerFragment) getParentFragment()).setTopToolBar();
        }

    }


    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */

            Toast.makeText(getActivity(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };


    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(getActivity(),
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }


    //----FORM VALIDATION SECTION ---//---[START]
    public void registerNewUserAfterAddressValidation() {
        if (!validateStreetAddress()) {
            return;
        }
//        if (!validateAPTUNITinteger()) {
//            return;
//        }
        if (!validateCity()) {
            return;
        }
        if (!validateState()) {
            return;
        }

        if (!validateZip()) {
            return;
        }

        //public Address(String streetAddress, String city, String state, String zipCode, String unit_Apt_no)
        mStreetValue = mStreetAddressEditText.getText().toString();
        mUnitAptValue = mUnitAptEditText.getText().toString();
        mCityValue = mCityEditText.getText().toString();
        mStateValue = mStateEditText.getText().toString();
        mZipValue = mZipEditText.getText().toString();


        //this is just checking what happens when a ull value is stored, because unit is optional
        if (mUnitAptValue.equals("")) {
            mUnitAptValue = null;
        }

        //needs a null handler here


        mAuth.createUserWithEmailAndPassword(mEmailValueFromPrevious, mPasswordValueFromPrevious)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //add an action here once user is created,
                            // you can choose to finish the activity here if you want

                        }

                        // ...
                    }
                });
    }
    //----FORM VALIDATION SECTION ---//---[END]

    public boolean validateStreetAddress() {
        String streetAddress = mStreetAddressEditText.getText().toString().trim();
        if (streetAddress.isEmpty() || streetAddress.equals("")) {
            mStreetAddressWrapper.setError(getString(R.string.error_street_address));
            requestFocus(mStreetAddressEditText);
            return false;
        } else {
            mStreetAddressWrapper.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().
                    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public boolean validateAPTUNITinteger() {
        String unitAPT = mUnitAptEditText.getText().toString().trim();
        if (unitAPT.isEmpty() || unitAPT.equals("")) {
            mUnitAptWrapper.setError(getString(R.string.error_unit_apt));
            requestFocus(mUnitAptEditText);
            return false;
        } else {
            mUnitAptWrapper.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validateCity() {
        String cityString = mCityEditText.getText().toString().trim();
        if (cityString.isEmpty() || cityString.equals("")) {
            mCityWrapper.setError(getString(R.string.error_city));
            requestFocus(mCityEditText);
            return false;
        } else {
            mCityWrapper.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validateState() {
        String stateString = mStateEditText.getText().toString().trim();
        if (stateString.isEmpty() || stateString.equals("")) {
            mStateWrapper.setError(getString(R.string.error_state));
            requestFocus(mStateEditText);
            return false;
        } else {
            mStateWrapper.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validateZip() {
        String zipString = mZipEditText.getText().toString().trim();
        if (zipString.isEmpty() || zipString.equals("")) {
            mStateWrapper.setError(getString(R.string.error_zip_code));
            requestFocus(mZipEditText);
            return false;
        } else {
            mZipWrapper.setErrorEnabled(false);
        }
        return true;
    }
}

