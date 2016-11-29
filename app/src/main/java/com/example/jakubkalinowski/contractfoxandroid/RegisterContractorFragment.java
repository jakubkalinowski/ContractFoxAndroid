package com.example.jakubkalinowski.contractfoxandroid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jakubkalinowski.contractfoxandroid.interfaces.Communicator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterContractorFragment extends Fragment {

    //text wrappers
    private TextInputLayout mFirstNameWrapper;
    private TextInputLayout mLastNameWrapper;
    private TextInputLayout mPhoneWrapper;
    private TextInputLayout mCompanyWrapper;
    private TextInputLayout mWebsiteWrapper;
    private TextInputLayout mDescriptionWrapper;

    //EditTexts insideWrappers
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mPhoneEditText;
    private EditText mCompanyEditText;
    private EditText mWebsiteEditText;
    private EditText mDescriptionEditText;

    //imageView
    private CircleImageView mCircleProfileImageView;
    private Bitmap mProfileImageBitmap;

    //layout
    private LinearLayout mLinearLayoutContractorFragment;

    //values;
    private String mEmailValueFromPrevious;
    private String mPasswordValueFromPrevious;
    private Boolean mContractorBooleanValueFromPrevious;
    private String mFirstNameValue;
    private String mLastNameValue;
    private String mPhoneValue;
    private String mCompanyValue;
    private String mWebsiteValue;
    private String mDescriptionValue;

    //NEXT BUTTON to register/ address
    private Button mNextButtonToRegisterAddress;

    //intent set up for gallery pick
    private Intent mPickImageIntent;
    private static int IMG_RESULT = 1;

    // Storage Permissions variables--This is requird for android 6.0 Marshmallow
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    //Jatinder work here
    //handling the checkbox skills area
    //Interior Skills
    private CheckBox mAtticSkillCheck;
    private CheckBox mBasementSkillCheck;
    private CheckBox mBathroomSkillCheck;
    private CheckBox mGarageSkillCheck;
    private CheckBox mGeneralRoomSkillCheck;
    private CheckBox mKitchenSkillCheck;
    private CheckBox mUtilitySkillCheck;

    //Exterior Skills
    private CheckBox mAcHeatSkillCheck;
    private CheckBox mChimneySkillCheck;
    private CheckBox mDeckPatioSkillCheck;
    private CheckBox mDoorsSkillCheck;
    private CheckBox mFoundationSkillCheck;
    private CheckBox mRoofSkillCheck;
    private CheckBox mWallsSkillCheck;
    private CheckBox mWindowsSkillCheck;

    //Backyard Skills
    private CheckBox mDrivewaySkillCheck;
    private CheckBox mFenchSkillCheck;
    private CheckBox mGazeboSkillCheck;
    private CheckBox mLandscapeSkillCheck;
    private CheckBox mPoolJacuzziSkillCheck;
    private CheckBox mSepticSkillCheck;
    private CheckBox mTreeSkillCheck;
    private CheckBox mWellSkillCheck;

    private CompoundButton.OnCheckedChangeListener mCheckListener;
    //arraylist
    private ArrayList<String> skillset;

    //reference to the communicator interface
    Communicator mCommunicator;
    Fragment address_child_fragment;

    public RegisterContractorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((registerActivity) getActivity()).setTopToolBar("Contractor sign up");
        //lets set values coming from previous fragment to this fragment
        mEmailValueFromPrevious = getArguments().getString("emailAddress");
        mPasswordValueFromPrevious = getArguments().getString("password");
        mContractorBooleanValueFromPrevious = getArguments().getBoolean("typeBoolean");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_contractor, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        skillset = new ArrayList<String>();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLinearLayoutContractorFragment = (LinearLayout)
                view.findViewById(R.id.register_contractor_linear_layout_id);

        //This is for the images part
        mCircleProfileImageView = (CircleImageView) view.
                findViewById(R.id.profile_image_contractor_register_fragment);

        //6 wrappers
        mFirstNameWrapper = (TextInputLayout) view.
                findViewById(R.id.first_name_textInput_wrapper_contractor_register_fragment);
        mLastNameWrapper = (TextInputLayout) view.
                findViewById(R.id.last_name_textInput_wrapper_fragment_Contractor_Register);
        mPhoneWrapper = (TextInputLayout) view.
                findViewById(R.id.phone_Input_wrapper_fragment_contractor_Register);
        mCompanyWrapper = (TextInputLayout) view
                .findViewById(R.id.companyName_Input_wrapper_fragment_contractor_Register);
        mDescriptionWrapper = (TextInputLayout) view
                .findViewById(R.id.company_description_Input_wrapper_fragment_contractor_Register);
        mWebsiteWrapper = (TextInputLayout) view
                .findViewById(R.id.website_url_Input_wrapper_fragment_contractor_Register);

        //setting 6 hints
        mFirstNameWrapper.setHint("First name");
        mLastNameWrapper.setHint("Last name");
        mPhoneWrapper.setHint("Phone");
        mCompanyWrapper.setHint("Company");
        mDescriptionWrapper.setHint("Description");
        mWebsiteWrapper.setHint("Website");

        //6 editTexts
        mFirstNameEditText = (EditText)
                view.findViewById(R.id.first_name_edit_text_fragment_contractor_register);
        mLastNameEditText = (EditText)
                view.findViewById(R.id.
                        last_name_edittextfield_fragment_Contractor_Register_fragment);
        mPhoneEditText = (EditText)
                view.findViewById(R.id.phone_edittextfield_fragment_contractor_Register_fragment);
        mCompanyEditText = (EditText)
                view.findViewById(R.id.
                        companyName_edittextfield_fragment_contractor_Register_fragment);
        mDescriptionEditText = (EditText)
                view.findViewById(R.id.
                        company_description_edittextfield_fragment_contractor_Register_fragment);
        mWebsiteEditText = (EditText)
                view.findViewById
                        (R.id.website_url_edittextfield_fragment_contractor_Register_fragment);

        //[setting up the skills arraylist and it's listeners]--[START]
        //Jatinder work here
        //Interior
        mAtticSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_attic);
        mBasementSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_basement);
        mBathroomSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_bathroom);
        mGarageSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_garage);
        mGeneralRoomSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_generalroom);
        mKitchenSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_kitchen);
        mUtilitySkillCheck = (CheckBox) view.findViewById(R.id.checkbox_utility);

        //Exterior
        mAcHeatSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_ac_heat);
        mChimneySkillCheck = (CheckBox) view.findViewById(R.id.checkbox_chimney);
        mDeckPatioSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_deck_patio);
        mDoorsSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_doors);
        mFoundationSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_foundation);
        mRoofSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_roof);
        mWallsSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_walls);
        mWindowsSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_windows);

        //Backyard
        mDrivewaySkillCheck = (CheckBox) view.findViewById(R.id.checkbox_driveway);
        mFenchSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_fence);
        mGazeboSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_gazebo);
        mLandscapeSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_landscape);
        mPoolJacuzziSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_pool_jacuzzi);
        mSepticSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_septic_tank);
        mTreeSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_tree);
        mWellSkillCheck = (CheckBox) view.findViewById(R.id.checkbox_well);

        //Jatinder work here

        //This Checklistener fires everytime a check button is clicked.
        mCheckListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                // compoundButton is the checkbox
                // boolean is whether or not checkbox is checked
                // Check which checkbox was clicked

                //Jatinder work here
                switch (compoundButton.getId()) {
                    //Interior
                    case R.id.checkbox_attic:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("attic");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("attic");
                        }
                        break;
                    case R.id.checkbox_basement:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("basement");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("basement");
                        }
                        break;
                    case R.id.checkbox_bathroom:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("bathroom");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("bathroom");
                        }
                        break;
                    case R.id.checkbox_garage:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("garage");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("garage");
                        }
                        break;
                    case R.id.checkbox_generalroom:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("general");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("general");
                        }
                        break;
                    case R.id.checkbox_kitchen:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("kitchen");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("kitchen");
                        }
                        break;
                    case R.id.checkbox_utility:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("utility");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("utility");
                        }
                        break;

                    //Exterior
                    case R.id.checkbox_ac_heat:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("ac");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("ac");
                        }
                        break;
                    case R.id.checkbox_chimney:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("chimney");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("chimney");
                        }
                        break;
                    case R.id.checkbox_deck_patio:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("deck");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("deck");
                        }
                        break;
                    case R.id.checkbox_doors:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("doors");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("doors");
                        }
                        break;
                    case R.id.checkbox_foundation:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("foundation");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("foundation");
                        }
                        break;
                    case R.id.checkbox_roof:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("roof");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("roof");
                        }
                        break;
                    case R.id.checkbox_walls:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("walls");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("walls");
                        }
                        break;
                    case R.id.checkbox_windows:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("windows");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("windows");
                        }
                        break;

                    //BackYard
                    case R.id.checkbox_driveway:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("driveway");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("driveway");
                        }
                        break;
                    case R.id.checkbox_fence:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("fence");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("fence");
                        }
                        break;
                    case R.id.checkbox_gazebo:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("gazebo");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("gazebo");
                        }
                        break;
                    case R.id.checkbox_landscape:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("landscape");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("landscape");
                        }
                        break;
                    case R.id.checkbox_pool_jacuzzi:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("pool");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("pool");
                        }
                        break;
                    case R.id.checkbox_septic_tank:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("septic");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("septic");
                        }
                        break;
                    case R.id.checkbox_tree:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("tree");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("tree");
                        }
                        break;
                    case R.id.checkbox_well:
                        if (checked) {
                            //Add to arrayList
                            skillset.add("well");
                        } else {
                            //Remove From ArrayList
                            skillset.remove("well");
                        }
                        break;
                }

                //Jatinder work here
            }
        };

        setUpSkillsCheckbox();
        //[setting up the skills arraylist and it's listeners]--[START]

        //setting the action of next button
        mNextButtonToRegisterAddress = (Button) view.findViewById
                (R.id.next_button_fragment_contractor_register);
        mNextButtonToRegisterAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddressFragmentAfterValidation();
            }
        });

        //setting action to profile picture
        mCircleProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(getActivity());
                showEditProfilePictureDialog();
            }
        });
    }

    /**
     * Setting top bar using parent activity instance because top tool bar is in address_fragment
     */
    public void setTopToolBar(){
        ((registerActivity) getActivity()).setTopToolBar("Contractor sign up");
    }

    //Jatinder work here
    //[THIS PIECE OF CODE BELOW ONLY ATTACHES THE LISTERNER TO EACH INDIVIDUAL CHECKBOX]
    public void setUpSkillsCheckbox() {
        mAtticSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mBasementSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mBathroomSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mGarageSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mGeneralRoomSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mKitchenSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mUtilitySkillCheck.setOnCheckedChangeListener(mCheckListener);

        mAcHeatSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mChimneySkillCheck.setOnCheckedChangeListener(mCheckListener);
        mDeckPatioSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mDoorsSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mFoundationSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mRoofSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mWallsSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mWindowsSkillCheck.setOnCheckedChangeListener(mCheckListener);

        mDrivewaySkillCheck.setOnCheckedChangeListener(mCheckListener);
        mFenchSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mGazeboSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mLandscapeSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mPoolJacuzziSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mSepticSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mTreeSkillCheck.setOnCheckedChangeListener(mCheckListener);
        mWellSkillCheck.setOnCheckedChangeListener(mCheckListener);
        //Jatinder work here
    }

    //This piece of code gets ByteArrayData from whatever the image circle IS//still needs
    // refinement
    public byte[] getCircleImageViewByteData() {

        mCircleProfileImageView.setDrawingCacheEnabled(true);
        mCircleProfileImageView.buildDrawingCache();
        mProfileImageBitmap = mCircleProfileImageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mProfileImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        return data;
    }

    //This piece of code pops up the dialog window
    //Found from https://github.com/afollestad/material-dialogs
    public void showEditProfilePictureDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext())
                .title("Set Profile Picture").positiveText("CAMERA").neutralText("DEVICE STORAGE")
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore
                                .Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(mPickImageIntent, IMG_RESULT);
                    }
                });
        MaterialDialog md = builder.build();
        md.show();
    }

    //This piece of code gets access to gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == IMG_RESULT && resultCode == Activity.RESULT_OK && null != data) {
                Uri URI = data.getData();
                String[] FILE = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(URI,
                        FILE, null, null, null);

                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(FILE[0]);
                String imageDecode = cursor.getString(columnIndex);

                //compressing the image
                Bitmap imageToCompress = BitmapFactory.decodeFile(imageDecode);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                imageToCompress.compress(Bitmap.CompressFormat.JPEG, 100, out);
                cursor.close();

                //image is compressed and then set to imageView/
                mCircleProfileImageView.setImageBitmap(BitmapFactory.decodeStream(
                        new ByteArrayInputStream(out.toByteArray())));


            }
        } catch (Exception e) {
            Toast.makeText(getActivity().
                    getApplicationContext(), "Something went wrong, try again", Toast.LENGTH_SHORT).show();
        }
    }

    //this piece of code below is to make sure app has permissions to access device photos/contents
    //This is new in Android 6.0 Marshmallow. It's called Runtime permissions.
    //Specifying permissions in Android Manifest file is not enough.
    public static void verifyStoragePermissions(Activity activity) {
        //check if we have read or write permissions
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.
                WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.
                READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * @param context Initializing communicator interface variable to be initialized to an Activity's context/
     *                So that mCommunicator knows which activity's respond to make a call to.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
            mCommunicator = (Communicator) a;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((registerActivity) getActivity()).setTopToolBar("Sign Up");
    }

    //====FORM VALIDATION SECTION====// --[START]
    public void goToAddressFragmentAfterValidation() {
        //Required fields are first name, last name, company name, phone, description
        //other fields are website, description.

        if (!validateFirstName()) {
            return;
        }
        if (!validateLastName()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
        if (!validateCompanyName()) {
            return;
        }

        if (!validateDescription()) {
            return;
        }

        //makes it required for atleast one skillset to be checked.
        if(skillset.isEmpty()){
            Snackbar snackbar = Snackbar
                    .make( mLinearLayoutContractorFragment, "You need to check atleast one skill",
                            Snackbar.LENGTH_SHORT);

            snackbar.show();
            return;
        }

        byte[] profileImageBytedata = getCircleImageViewByteData();
        mFirstNameValue = mFirstNameEditText.getText().toString();
        mLastNameValue = mLastNameEditText.getText().toString();
        mPhoneValue = mPhoneEditText.getText().toString();
        mCompanyValue = mCompanyEditText.getText().toString();
        mDescriptionValue = mDescriptionEditText.getText().toString();
        mWebsiteValue = mWebsiteEditText.getText().toString();

        //[Setting up the bundle to pass to next RegisterAddress screen]-[START]
        Bundle bundleToPass = new Bundle();
        bundleToPass.putString("emailAddress", mEmailValueFromPrevious);
        bundleToPass.putString("password", mPasswordValueFromPrevious);
        bundleToPass.putBoolean("typeBoolean", mContractorBooleanValueFromPrevious);
        bundleToPass.putString("firstname", mFirstNameValue);
        bundleToPass.putString("lastname", mLastNameValue);
        bundleToPass.putString("phone", mPhoneValue);
        bundleToPass.putString("companyName", mCompanyValue);
        bundleToPass.putString("websiteURL", mWebsiteValue);
        bundleToPass.putString("companyDescription", mDescriptionValue);
        bundleToPass.putStringArrayList("contractor_skillset", skillset);
        //still need to handle the images part here
        bundleToPass.putByteArray("profileImageData", profileImageBytedata);
        System.out.println(getTag());

        mCommunicator.respond(bundleToPass, getTag(), getChildFragmentManager());
    }

    public boolean validateFirstName() {
        String firstName = mFirstNameEditText.getText().toString().trim();
        if (firstName.isEmpty() || firstName.equals("")) {
            mFirstNameWrapper.setError(getString(R.string
                    .register_contractor_fragment_firstName_error));
            requestFocus(mFirstNameEditText);
            return false;
        } else {
            mFirstNameWrapper.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validateLastName() {
        String lastName = mLastNameEditText.getText().toString().trim();
        if (lastName.isEmpty() || lastName.equals("")) {
            mLastNameWrapper.setError(getString(R.string
                    .register_contractor_fragment_lastName_error));
            requestFocus(mLastNameEditText);
            return false;
        } else {
            mLastNameWrapper.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validateCompanyName() {
        String companyName = mCompanyEditText.getText().toString().trim();
        if (companyName.isEmpty() || companyName.equals("")) {
            mCompanyWrapper.setError(getString(R.string
                    .register_contractor_fragment_lastName_error));
            requestFocus(mCompanyEditText);
            return false;
        } else {
            mCompanyWrapper.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validatePhone() {
        String phone = mPhoneEditText.getText().toString().trim();
        if (phone.isEmpty() || phone.equals("") ||
                !(android.util.Patterns.PHONE.matcher(mPhoneEditText.getText().toString()).matches())) {
            mPhoneWrapper.setError(getString(R.string
                    .register_contractor_fragment_phone_error));
            requestFocus(mPhoneEditText);
            return false;
        } else {
            mPhoneWrapper.setErrorEnabled(false);
        }
        return true;
//        return android.util.Patterns.PHONE.matcher(mPhoneEditText.getText().toString()).matches();
    }

    public boolean validateDescription() {
        String description = mDescriptionEditText.getText().toString().trim();
        if (description.isEmpty() || description.equals("")) {
            mDescriptionWrapper.setError(getString(R.string
                    .register_contractor_fragment_description_error));
            requestFocus(mDescriptionEditText);
            return false;
        } else {
            mDescriptionWrapper.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().
                    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.first_name_edit_text_fragment_contractor_register:
                    validateFirstName();
                    break;
                case R.id.last_name_edittextfield_fragment_Contractor_Register_fragment:
                    validateLastName();
                    break;
                case R.id.phone_edittextfield_fragment_contractor_Register_fragment:
                    validatePhone();
                    break;
                case R.id.companyName_edittextfield_fragment_contractor_Register_fragment:
                    validateCompanyName();
                    break;
                case R.id.description_editText_contractorProfileFragment:
                    validateDescription();
                    break;
            }
        }
    }
    //===FORM VALIDATION SECTION====// //--[END]
}