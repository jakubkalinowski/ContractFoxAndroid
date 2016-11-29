package com.example.jakubkalinowski.contractfoxandroid.Navigation_Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jakubkalinowski.contractfoxandroid.Model.ContractorDutySession;
import com.example.jakubkalinowski.contractfoxandroid.Model.ContractorSingleDaySchedule;
import com.example.jakubkalinowski.contractfoxandroid.R;
import com.example.jakubkalinowski.contractfoxandroid.helper_classes.ContractorOccupiedDaysDecorator;
import com.example.jakubkalinowski.contractfoxandroid.helper_classes.RecyclerViewItemTouchHelper;
import com.example.jakubkalinowski
        .contractfoxandroid.helper_classes.ContractorSingleSessionAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;


/**
 * Created by MD on 10/27/2016.
 */

public class ContractorScheduleFragment extends Fragment implements
        DatePickerDialog.OnDateSetListener, OnDateSelectedListener {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    private Button mButton;
    private MaterialCalendarView mContractorScheduleMonthView;
    private FloatingActionButton mNewEventFAB;

    //variables to recyclerview
    private RecyclerView mRecyclerViewDuties;
    private ContractorSingleSessionAdapter mAdapter;
    private List<ContractorDutySession> singleDayServicesList = new ArrayList<>();


    //Material Dialog
    private MaterialDialog.Builder mInputDescriptionDialog;
    private MaterialDialog.Builder mInputTimeAvailabilityDialog;
    private MaterialDialog.Builder mSingleContractSessionReadUpdateDeleteDialog;

    private FragmentActivity mParentActivityContext;
    private CharSequence mEventDescription;

    //Firebase database reference
    private DatabaseReference mDatabaseAllContractorScheduleReference;
    private DatabaseReference mDatabaseCurrentContractorScheduleReference;
    private DatabaseReference mDatabaseContractorSchedule;
    private String contractorIDKey;

    //This hashmap downloads all the content
    private Map<String, ContractorSingleDaySchedule> contractorScheduleMap = new HashMap<>();


    //This hashmap is whats being saved back into firebase after all logic is handled.
    //This is the local data structure
    private Map<String, ContractorSingleDaySchedule> threeWeeksAvailableMap = new HashMap<>();

    ArrayList<Integer> x = new ArrayList<Integer>();
    int counter = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.contractor_schedule_fragment_layout, parent, false);
    }


    @Override
    public void onAttach(Context context) {

        Activity a;
        if (context instanceof Activity) {
            a = (Activity) context;
            mParentActivityContext = (FragmentActivity) a;
        }
        super.onAttach(context);
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        mAdapter = new ContractorSingleSessionAdapter(singleDayServicesList);
        mContractorScheduleMonthView = (MaterialCalendarView) view.
                findViewById(R.id.schedule_month_view_contractor_schedule_fragment_layout);
        mNewEventFAB = (FloatingActionButton) view.findViewById
                (R.id.fab_new_duty_contractor_schedule_fragment_layout);
        mRecyclerViewDuties =
                (RecyclerView) view.findViewById
                        (R.id.events_recycler_view_contractor_schedule);
        mRecyclerViewDuties.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewDuties.setAdapter(mAdapter);
//        prepareDutiesDataForRecyclerView();
        mRecyclerViewDuties.setHasFixedSize(true);


        setCalendarMonthViewState();
        mContractorScheduleMonthView.setOnDateChangedListener(this);
//        mContractorScheduleMonthView.state().edit()
//                .setFirstDayOfWeek(Calendar.WEDNESDAY)
//                .setMinimumDate(CalendarDay.from(2016, 4, 3))
//                .setMaximumDate(CalendarDay.from(2016, 5, 12))
//                .setCalendarDisplayMode(CalendarMode.WEEKS)
//                .commit();


        mDatabaseAllContractorScheduleReference = FirebaseDatabase.getInstance().
                getReference().child("contractor_schedule");

        threeWeeksAvailableMap = getThreeWeeksAvailableHashMap();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //get currentcontractor's schedule in a map interface.
        if (user != null) {
            contractorIDKey = user.getUid().toString();
            mDatabaseCurrentContractorScheduleReference = mDatabaseAllContractorScheduleReference.
                    child(contractorIDKey);
            mDatabaseCurrentContractorScheduleReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    //this will add shit to the thing
                    String timeStampKey = dataSnapshot.getKey();
                    ContractorSingleDaySchedule allDayScheduleObjectValue = dataSnapshot.
                            getValue(ContractorSingleDaySchedule.class);
                    if (timeStampKey != null && allDayScheduleObjectValue != null) {
                        contractorScheduleMap.put(timeStampKey, allDayScheduleObjectValue);
                        threeWeeksAvailableMap.put(timeStampKey, allDayScheduleObjectValue);
                        new PopulateFullDayOccupied().executeOnExecutor
                                (Executors.newSingleThreadExecutor());
                        new PopulateHalfDayEventsAsync().executeOnExecutor
                                (Executors.newSingleThreadExecutor());
                        new UnpoppulateFullDayOccupied().executeOnExecutor
                                (Executors.newSingleThreadExecutor());

                    }
//                    x.add(counter++);
//                    System.out.println("child_added_to_hashmap-->" + counter);
//                    System.out.println();
//                    for (Map.Entry<String, ContractorSingleDaySchedule> item : threeWeeksAvailableMap.entrySet()) {
//                        // ...
//                        System.out.println(item.getKey() + "-->" + item.getValue().getAvailableThisDay());
//                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    //this will update an existing key-value pair
                    String timeStampKey = dataSnapshot.getKey();
                    ContractorSingleDaySchedule allDayScheduleObjectValue = dataSnapshot.
                            getValue(ContractorSingleDaySchedule.class);
                    if (timeStampKey != null && allDayScheduleObjectValue != null) {
                        contractorScheduleMap.put(timeStampKey, allDayScheduleObjectValue);
                        threeWeeksAvailableMap.put(timeStampKey, allDayScheduleObjectValue);
                        new PopulateFullDayOccupied().executeOnExecutor
                                (Executors.newSingleThreadExecutor());
                        new PopulateHalfDayEventsAsync().executeOnExecutor
                                (Executors.newSingleThreadExecutor());
                        new UnpoppulateFullDayOccupied().executeOnExecutor
                                (Executors.newSingleThreadExecutor());
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    //this will remove stuff upon an event upon deletion
                    //notice that we're not removing the map key's object but actually making a slot
                    //available
                    //hence we need to retrieve a key and point that key to an object looking like
                    //an available slot
                    // --> Step 1: retrieve the key
                    // --> Step 2: make it point to an available slot object
                    // --> Step 3: put it back into the hashmap and upload to firebase

                    String timeStampKey = dataSnapshot.getKey();
                    ContractorSingleDaySchedule allDayScheduleObjectValue = dataSnapshot.
                            getValue(ContractorSingleDaySchedule.class);
                    allDayScheduleObjectValue.setMorningSession(null);
                    allDayScheduleObjectValue.setEveningSession(null);
                    allDayScheduleObjectValue.setAvailableThisDay(true);

//                    Toast.makeText(getActivity(), "Child removed", Toast.LENGTH_SHORT).show();
                    if (timeStampKey != null && allDayScheduleObjectValue != null) {
                        contractorScheduleMap.put(timeStampKey, allDayScheduleObjectValue);
                        threeWeeksAvailableMap.put(timeStampKey, allDayScheduleObjectValue);
                        new PopulateFullDayOccupied().executeOnExecutor
                                (Executors.newSingleThreadExecutor());
                        new PopulateHalfDayEventsAsync().executeOnExecutor
                                (Executors.newSingleThreadExecutor());
                        new UnpoppulateFullDayOccupied().executeOnExecutor
                                (Executors.newSingleThreadExecutor());
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            //user is not signed in
        }


        mInputDescriptionDialog = new MaterialDialog.Builder(getActivity())
                .title("New contract")
                .content("set a new appointment")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .negativeText(R.string.cancel_button_dialog_editText)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.dismiss();
                    }
                }).positiveText("Set a date").input(getString(R.string.hint_dialog_editText), "",
                        new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // Do something
                                dialog.dismiss();
                                if (input == "") {
                                    //insert something
                                } else {
                                    mEventDescription = input;
                                    promptPickADate(mEventDescription);
                                }

                            }
                        }).canceledOnTouchOutside(false);

        new PopulateFullDayOccupied().executeOnExecutor(Executors.newSingleThreadExecutor());
        new PopulateHalfDayEventsAsync().executeOnExecutor(Executors.newSingleThreadExecutor());


        mNewEventFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInputDescriptionDialog.show();
            }
        });
        mRecyclerViewDuties.addOnItemTouchListener
                (new RecyclerViewItemTouchHelper(getActivity(),
                        new RecyclerViewItemTouchHelper.
                                OnItemClickListener() {

                            @Override
                            public void onClick(View view, int position) {

                                //Options, edit, view, delete

                            }

                            @Override
                            public void onLongClick(View view, int position) {
//                                Toast.makeText(getActivity(),
//                                        String.valueOf(singleDayServicesList.get(position).getSessionDate_Milliseconds_Key()),
//                                        Toast.LENGTH_SHORT).show();
                                promptEditDialogShow(position);
                            }
                        },
                        mRecyclerViewDuties));
    }

    public void promptEditDialogShow(final int i) {
        //Options, edit, view, delete
        //Step 1 --> Show dialog with three buttons
        //Step 2 --> View With Info
        //Step 3 --> Edit and delete option
        mSingleContractSessionReadUpdateDeleteDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_title_edit_contract_contractor).content
                        (singleDayServicesList.get(i).getDescription() + "\n" + singleDayServicesList.
                                get(i).getReadableSessionDate() + "\n" + singleDayServicesList.get(i).
                                getReadableAppointmentStartTime() + " - " + singleDayServicesList.get(i).
                                getReadableAppointmentEndTime()).
                        positiveText(R.string.edit_contract_option).
                        negativeText(R.string.delete_contract_option).
                        neutralText(R.string.dialog_ok_contract_edit).
                        onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //Edit description
                                final ContractorDutySession currentObject = singleDayServicesList.get(i);
                                if (currentObject != null && String.valueOf(currentObject.
                                        getSessionDate_Milliseconds_Key()) != null) {
//                                    mInputDescriptionDialog = new MaterialDialog.Builder(getActivity())
//                                            .title("New contract")
//                                            .content("set a new appointment")
//                                            .inputType(InputType.TYPE_CLASS_TEXT)
//                                            .negativeText(R.string.cancel_button_dialog_editText)
//                                            .onNegative(new MaterialDialog.SingleButtonCallback() {
//                                                @Override
//                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                                    // TODO
//                                                    dialog.dismiss();
//                                                }
//                                            }).positiveText("Set a date").input(getString(R.string.hint_dialog_editText), "",
//                                                    new MaterialDialog.InputCallback() {
//                                                        @Override
//                                                        public void onInput(MaterialDialog dialog, CharSequence input) {
//                                                            // Do something
//                                                            dialog.dismiss();
//                                                            if (input == "") {
//                                                                //insert something
//                                                            } else {
//                                                                mEventDescription = input;
//                                                                promptPickADate(mEventDescription);
//                                                            }
//
//                                                        }
//                                                    }).canceledOnTouchOutside(false);
                                    MaterialDialog.Builder editDescriptionDialogBuilder = new MaterialDialog.
                                            Builder(getActivity()).title(R.string.
                                            edit_description_dialog_title).negativeText
                                            (R.string.edit_description_dismiss_dialog_button_text)
                                            .positiveText
                                                    (R.string.edit_description_positive_button_text)
                                            .input(getString(R.string.hint_dialog_editText), "",
                                                    new MaterialDialog.InputCallback() {
                                                        @Override
                                                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                                            if (input == "") {

                                                            } else {
                                                                String new_description =
                                                                        input.toString();
                                                                String DayScheduleKey = String.
                                                                        valueOf(currentObject.getSessionDate_Milliseconds_Key());
                                                                //Step 1 -> know about if the current object is has morning session or
                                                                if (currentObject != null &&
                                                                        currentObject.getAppointmentSession().equals("morning")) {
                                                                    currentObject.setDescription(new_description);
                                                                    threeWeeksAvailableMap.get(DayScheduleKey).
                                                                            setMorningSession(currentObject);
                                                                    contractorScheduleMap.get(DayScheduleKey).
                                                                            setMorningSession(currentObject);

                                                                    mDatabaseAllContractorScheduleReference.
                                                                            child(contractorIDKey)
                                                                            .child(DayScheduleKey).
                                                                            child("morningSession").
                                                                            setValue(threeWeeksAvailableMap.
                                                                                    get(DayScheduleKey).getMorningSession());
                                                                    prepareDutiesDataForRecyclerView(threeWeeksAvailableMap.
                                                                            get(DayScheduleKey));

                                                                } else if (currentObject != null &&
                                                                        currentObject.getAppointmentSession().equals("afternoon")) {
                                                                    currentObject.setDescription(new_description);
                                                                    threeWeeksAvailableMap.get(DayScheduleKey).
                                                                            setEveningSession(currentObject);
                                                                    contractorScheduleMap.get(DayScheduleKey).
                                                                            setEveningSession(currentObject);

                                                                    mDatabaseAllContractorScheduleReference.
                                                                            child(contractorIDKey)
                                                                            .child(DayScheduleKey).
                                                                            child("eveningSession").
                                                                            setValue(threeWeeksAvailableMap.
                                                                                    get(DayScheduleKey).getEveningSession());
                                                                    prepareDutiesDataForRecyclerView(threeWeeksAvailableMap.
                                                                            get(DayScheduleKey));
                                                                }
                                                            }
                                                        }
                                                    });
                                    MaterialDialog editDescriptionBuiltDialog =
                                            editDescriptionDialogBuilder.build();
                                    editDescriptionBuiltDialog.show();
                                }
                            }
                        }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //delete contract
                        //Step 1--> take the milliseconds key from the database here, and delete it,
                        //then update the in-memory datastructure as well as
                        ContractorDutySession currentObject = singleDayServicesList.get(i);
                        if (currentObject != null &&
                                String.valueOf(currentObject.getSessionDate_Milliseconds_Key()) !=
                                        null) {
                            String DayScheduleKey = String.
                                    valueOf(currentObject.getSessionDate_Milliseconds_Key());
                            String sessionString = currentObject.getAppointmentSession();
                            if (sessionString.equalsIgnoreCase("afternoon")) {
                                //get ref and delete
                                threeWeeksAvailableMap.get(DayScheduleKey).setEveningSession(null);
                                contractorScheduleMap.get(DayScheduleKey).setEveningSession(null);
                                if (threeWeeksAvailableMap.get(DayScheduleKey) != null &&
                                        threeWeeksAvailableMap.get(DayScheduleKey).
                                                getEveningSession() == null &&
                                        threeWeeksAvailableMap.get(DayScheduleKey).
                                                getEveningSession() == null) {
                                    threeWeeksAvailableMap.get(DayScheduleKey).
                                            setAvailableThisDay(true);
                                    contractorScheduleMap.get(DayScheduleKey).
                                            setAvailableThisDay(true);
                                }
                                mDatabaseAllContractorScheduleReference.child(contractorIDKey).
                                        setValue(threeWeeksAvailableMap);
                                prepareDutiesDataForRecyclerView(threeWeeksAvailableMap.
                                        get(DayScheduleKey));
                            } else if (sessionString.equalsIgnoreCase("morning")) {
                                //get ref and delete
                                threeWeeksAvailableMap.get(DayScheduleKey).setMorningSession(null);
                                contractorScheduleMap.get(DayScheduleKey).setMorningSession(null);
                                if (threeWeeksAvailableMap.get(DayScheduleKey) != null &&
                                        ((threeWeeksAvailableMap.get(DayScheduleKey).
                                                getEveningSession() == null &&
                                                threeWeeksAvailableMap.get(DayScheduleKey).
                                                        getEveningSession() == null) || (threeWeeksAvailableMap.get(DayScheduleKey).
                                                getEveningSession() != null &&
                                                threeWeeksAvailableMap.get(DayScheduleKey).
                                                        getEveningSession() == null) || (threeWeeksAvailableMap.get(DayScheduleKey).
                                                getEveningSession() == null &&
                                                threeWeeksAvailableMap.get(DayScheduleKey).
                                                        getEveningSession() != null))) {
                                    threeWeeksAvailableMap.get(DayScheduleKey).
                                            setAvailableThisDay(true);
                                    contractorScheduleMap.get(DayScheduleKey).
                                            setAvailableThisDay(true);
                                }
                                mDatabaseAllContractorScheduleReference.child(contractorIDKey).
                                        setValue(threeWeeksAvailableMap);
                                prepareDutiesDataForRecyclerView(threeWeeksAvailableMap.get
                                        (DayScheduleKey));
                            } else {
                                //dont do an action here
                            }

                        }

                    }
                }).onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //let go
                        dialog.dismiss();
                    }
                }).canceledOnTouchOutside(false);

        MaterialDialog editContractDialog = mSingleContractSessionReadUpdateDeleteDialog.build();

        editContractDialog.show();
    }


    //all logic in here
    public void promptPickTimes(final long pickedDate, final CharSequence description) {
        //open show
        //setting up the builder here because it was causing issues earlier
        mInputTimeAvailabilityDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.dialogBox_pick_a_time_header)
                .items(R.array.time_availability_list)
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        /**
                         * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected check box to actually be selected.
                         * See the limited multi choice dialog example in the sample project for details.
                         **/


                        ContractorSingleDaySchedule singleDaySchedule =
                                contractorScheduleMap.get(String.valueOf(pickedDate));

                        //if user has already scheduled something
                        if (singleDaySchedule == null) {
                            System.out.println("schedule object not found, created new schedule object");
                            singleDaySchedule = new
                                    ContractorSingleDaySchedule(null, null, true,
                                    getReadableDate(pickedDate), pickedDate);
                        }


                        if (which.length == 0) {
                            //nothing is selected, dont save anything to firebase

                            System.out.println("Nothing is selected");
                        } else if (which[0] == 0 && which.length == 1) {
                            // morning time range selected
                            //converting description to string for firebase
                            ContractorDutySession morningSession =
                                    new ContractorDutySession(description.toString(), "morning",
                                            getMorningStartTime(pickedDate),
                                            getMorningEndTime(pickedDate),
                                            getReadableMorningStartTime(pickedDate),
                                            getReadableMorningEndTime(pickedDate), false,
                                            getReadableDate(pickedDate), pickedDate
                                    );
                            singleDaySchedule.setMorningSession(morningSession);
                            threeWeeksAvailableMap.put(String.valueOf(pickedDate), singleDaySchedule);
                            if (singleDaySchedule.getMorningSession() != null &&
                                    singleDaySchedule.getEveningSession() != null &&
                                    singleDaySchedule.getMorningSession().
                                            getAvailableDuringSession() == false &&
                                    singleDaySchedule.getEveningSession().
                                            getAvailableDuringSession() == false) {
                                singleDaySchedule.setAvailableThisDay(false);
                                threeWeeksAvailableMap.put(String.valueOf(pickedDate), singleDaySchedule);
                            }
//                            mDatabaseAllContractorScheduleReference.child(contractorIDKey).
//                                    child(String.valueOf(singleDaySchedule.
//                                            getTimeInMilliseconds())).setValue(singleDaySchedule);
                            mDatabaseAllContractorScheduleReference.child(contractorIDKey).
                                    setValue(threeWeeksAvailableMap);


                        } else if (which[0] == 1 && which.length == 1) {
                            // afternoon time range selected

                            ContractorDutySession afternoonSession = new ContractorDutySession
                                    (description.toString(),
                                            "afternoon", getAfternoonStartTime(pickedDate),
                                            getAfternoonEndTime(pickedDate),
                                            getReadableAfternoonStartTime(pickedDate),
                                            getReadableAfternoonEndTime(pickedDate), false,
                                            getReadableDate(pickedDate), pickedDate);
                            singleDaySchedule.setEveningSession(afternoonSession);
                            threeWeeksAvailableMap.put(String.valueOf(pickedDate), singleDaySchedule);
                            if (singleDaySchedule.getMorningSession() != null &&
                                    singleDaySchedule.getEveningSession() != null &&
                                    singleDaySchedule.getMorningSession().
                                            getAvailableDuringSession() == false &&
                                    singleDaySchedule.getEveningSession().
                                            getAvailableDuringSession() == false) {
                                singleDaySchedule.setAvailableThisDay(false);
                                threeWeeksAvailableMap.put(String.valueOf(pickedDate), singleDaySchedule);
                            }
//                            mDatabaseAllContractorScheduleReference.child(contractorIDKey).
//                                    child(String.valueOf(singleDaySchedule.getTimeInMilliseconds()))
//                                    .setValue(singleDaySchedule);
                            mDatabaseAllContractorScheduleReference.child(contractorIDKey).
                                    setValue(threeWeeksAvailableMap);

                        } else if (which.length == 2) {
                            // both is selected
                            ContractorDutySession morningSession =
                                    new ContractorDutySession(description.toString(), "morning",
                                            getMorningStartTime(pickedDate),
                                            getMorningEndTime(pickedDate),
                                            getReadableMorningStartTime(pickedDate),
                                            getReadableMorningEndTime(pickedDate), false,
                                            getReadableDate(pickedDate), pickedDate);
                            ContractorDutySession afternoonSession = new ContractorDutySession
                                    (description.toString(),
                                            "afternoon", getAfternoonStartTime(pickedDate),
                                            getAfternoonEndTime(pickedDate),
                                            getReadableAfternoonStartTime(pickedDate),
                                            getReadableAfternoonEndTime(pickedDate), false,
                                            getReadableDate(pickedDate), pickedDate);
                            singleDaySchedule.setEveningSession(afternoonSession);
                            singleDaySchedule.setMorningSession(morningSession);
                            threeWeeksAvailableMap.put(String.valueOf(pickedDate), singleDaySchedule);
                            if (singleDaySchedule.getMorningSession() != null &&
                                    singleDaySchedule.getEveningSession() != null &&
                                    singleDaySchedule.getMorningSession().
                                            getAvailableDuringSession() == false &&
                                    singleDaySchedule.getEveningSession().
                                            getAvailableDuringSession() == false) {
                                singleDaySchedule.setAvailableThisDay(false);
                                threeWeeksAvailableMap.put(String.valueOf(pickedDate), singleDaySchedule);
                            }
                            mDatabaseAllContractorScheduleReference.child(contractorIDKey).
                                    setValue(threeWeeksAvailableMap);
                        }
                        return true;
                    }
                })
                .positiveText(R.string.saveBookingTimeText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.dismiss();
                    }
                }).canceledOnTouchOutside(false);

        MaterialDialog timeInputDialog = mInputTimeAvailabilityDialog.build();

        timeInputDialog.show();

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        //call timepicker attribute here

        Calendar pickedDate = Calendar.getInstance();
        pickedDate.set(Calendar.MONTH, monthOfYear);
        pickedDate.set(Calendar.YEAR, year);
        pickedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        pickedDate.set(Calendar.HOUR_OF_DAY, 0);
        pickedDate.set(Calendar.MINUTE, 0);
        pickedDate.set(Calendar.SECOND, 0);
        pickedDate.set(Calendar.MILLISECOND, 0);
        long pickedDateInMillis = pickedDate.getTimeInMillis();
        view.dismiss();
        //send the date through here


        promptPickTimes(pickedDateInMillis, mEventDescription);
    }


    /**
     * This method is necessary as it helps to create empty slots for a 3 week timeframe
     */
    public Map<String, ContractorSingleDaySchedule> getThreeWeeksAvailableHashMap() {
        int dayCounter = 1;
        Map<String, ContractorSingleDaySchedule> threeWeeksAvailableMap = new HashMap<>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        int day_range = 21;
        for (int i = 0; i < day_range; i++) {

            System.out.println(c.getTime() + "---" + c.getTimeInMillis());
            threeWeeksAvailableMap.put(String.valueOf(c.getTimeInMillis()),
                    new ContractorSingleDaySchedule(null, null, true,
                            getReadableDate(c.getTimeInMillis()), c.getTimeInMillis()));
            c.add(Calendar.DAY_OF_YEAR, 1);
        }
        return threeWeeksAvailableMap;

    }


    public void setCalendarMonthViewState() {
        int maxNumberOfDaysAhead = 20;
        Calendar now = Calendar.getInstance();
        Calendar threeWeeksFromNow = Calendar.getInstance();
        threeWeeksFromNow.add(Calendar.DAY_OF_YEAR, maxNumberOfDaysAhead);
        mContractorScheduleMonthView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS).commit();


//        .setMinimumDate(now).setMaximumDate(threeWeeksFromNow)


//        mContractorScheduleMonthView.state().edit()
//                .setFirstDayOfWeek(Calendar.WEDNESDAY)
//                .setMinimumDate(CalendarDay.from(2016, 4, 3))
//                .setMaximumDate(CalendarDay.from(2016, 5, 12))
//                .setCalendarDisplayMode(CalendarMode.WEEKS)
//                .commit();

    }

    /**
     * This method shows the dialog and limits user entry to 3 weeks starting today
     *
     * @param description
     */
    public void promptPickADate(CharSequence description) {
        int maxNumberOfDaysAhead = 20;
        mEventDescription = description;
        Calendar now = Calendar.getInstance();

        Calendar threeWeeksFromNow = Calendar.getInstance();
        threeWeeksFromNow.add(Calendar.DAY_OF_YEAR, maxNumberOfDaysAhead);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ContractorScheduleFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMaxDate(threeWeeksFromNow);
        dpd.setMinDate(now);
        dpd.setOkText("pick availability");
        dpd.show(mParentActivityContext.getFragmentManager(), "Datepickerdialog");

    }

    public long getMorningStartTime(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }

    /**
     * Return time based on morning or afternoon session
     *
     * @param dateInMilliseconds
     */
    public String getReadableMorningStartTime(long dateInMilliseconds) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        c.setTimeInMillis(dateInMilliseconds);
        c.set(Calendar.HOUR_OF_DAY, 9);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return sdf.format(c.getTime());
    }


    /**
     * Return time based on morning or afternoon session
     *
     * @param dateInMilliseconds
     */
    public String getReadableMorningEndTime(long dateInMilliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 14);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return sdf.format(c.getTime());
    }

    public long getMorningEndTime(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.HOUR, 14);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }


    /**
     * Return time based on morning or afternoon session
     *
     * @param dateInMilliseconds
     */
    public String getReadableAfternoonStartTime(long dateInMilliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 15);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return sdf.format(c.getTime());

    }

    public long getAfternoonStartTime(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.HOUR_OF_DAY, 15);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();
    }

    /**
     * Return time based on morning or afternoon session
     *
     * @param dateInMilliseconds
     */
    public String getReadableAfternoonEndTime(long dateInMilliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 20);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return sdf.format(c.getTime());

    }

    public long getAfternoonEndTime(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.HOUR_OF_DAY, 20);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTimeInMillis();

    }


    public String getReadableDate(long dateInMilliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dateInMilliseconds);
        return getDayOfWeek(dateInMilliseconds) + " " + formatter.format(c.getTime());
    }


    public String getDayOfWeek(long dateInMilliseconds) {
        String[] days = {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dateInMilliseconds);
        return days[c.get(Calendar.DAY_OF_WEEK)];
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @Nullable CalendarDay date,
                               boolean selected) {
//        System.out.println(date.toString() + "SELECTED" + date.getCalendar().getTimeInMillis());
//        System.out.println(threeWeeksAvailableMap.get(String.valueOf(date.getCalendar().getTimeInMillis())));
//        Toast.makeText(getActivity(), "DATEPRESSED", Toast.LENGTH_SHORT).show();
        ContractorSingleDaySchedule touchedObject =
                threeWeeksAvailableMap.get(String.valueOf(date.getCalendar().getTimeInMillis()));
        prepareDutiesDataForRecyclerView(touchedObject);
    }


    /**
     * Async Task to populate half day events
     */
    private class PopulateHalfDayEventsAsync extends AsyncTask<Void, Void, List<CalendarDay>> {
        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (Map.Entry<String, ContractorSingleDaySchedule>
                    entry : threeWeeksAvailableMap.entrySet()) {
                String key = entry.getKey();
                ContractorSingleDaySchedule cSDS = entry.getValue();
                if ((cSDS.getEveningSession() != null &&
                        cSDS.getMorningSession() == null) || (cSDS.getEveningSession() == null &&
                        cSDS.getMorningSession() != null)) {
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(cSDS.getTimeInMilliseconds());
                    CalendarDay day = CalendarDay.from(c);
                    dates.add(day);
                }
            }
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isRemoving()) {
                return;
            }
            mContractorScheduleMonthView.addDecorator(new ContractorOccupiedDaysDecorator(Color.CYAN,
                    calendarDays));
        }
    }


    /**
     * ASYNC TASK for populating unavailable days
     */
    private class PopulateFullDayOccupied extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (Map.Entry<String, ContractorSingleDaySchedule>
                    entry : threeWeeksAvailableMap.entrySet()) {
                String key = entry.getKey();
                ContractorSingleDaySchedule cSDS = entry.getValue();

                if (cSDS.getEveningSession() != null &&
                        cSDS.getMorningSession() != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(cSDS.getTimeInMilliseconds());
                    CalendarDay day = CalendarDay.from(c);
                    dates.add(day);
                }
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isRemoving()) {
                return;
            }
            mContractorScheduleMonthView.addDecorator(new ContractorOccupiedDaysDecorator(Color.RED,
                    calendarDays));
        }
    }

    private class UnpoppulateFullDayOccupied extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (Map.Entry<String, ContractorSingleDaySchedule>
                    entry : threeWeeksAvailableMap.entrySet()) {
                String key = entry.getKey();
                ContractorSingleDaySchedule cSDS = entry.getValue();
                if (cSDS.getEveningSession() == null &&
                        cSDS.getMorningSession() == null) {
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(cSDS.getTimeInMilliseconds());
                    CalendarDay day = CalendarDay.from(c);
                    dates.add(day);
                }
            }
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);
            if (isRemoving()) {
                return;
            }
            mContractorScheduleMonthView.addDecorator(new ContractorOccupiedDaysDecorator
                    (Color.WHITE, calendarDays));

        }
    }


    /**
     *
     * @param daySchedule
     * This takes care when each touched object is changed on the UI to update RecyclerView
     * accordingly
     */
    public void prepareDutiesDataForRecyclerView(ContractorSingleDaySchedule daySchedule) {
        //Step 1 --> clear the array list backing datasi
        singleDayServicesList.clear();

        //Step 2 --> Iterate throught the keys and find that shit that has either morning or evening duties
        if (daySchedule != null) {
            if (daySchedule.getMorningSession() == null && daySchedule.getEveningSession() == null) {
                //dont add shit
            } else if (daySchedule.getMorningSession() != null &&
                    daySchedule.getEveningSession() == null) {
                singleDayServicesList.add(daySchedule.getMorningSession());
                mAdapter.notifyDataSetChanged();
            } else if (daySchedule.getMorningSession() == null &&
                    daySchedule.getEveningSession() != null) {
                singleDayServicesList.add(daySchedule.getEveningSession());
                mAdapter.notifyDataSetChanged();
            } else if (daySchedule.getMorningSession() != null &&
                    daySchedule.getEveningSession() != null) {
                singleDayServicesList.add(daySchedule.getEveningSession());
                singleDayServicesList.add(daySchedule.getMorningSession());
                mAdapter.notifyDataSetChanged();
            } else {
                //dont add to array
            }

        }
        mAdapter.notifyDataSetChanged();
    }


}




