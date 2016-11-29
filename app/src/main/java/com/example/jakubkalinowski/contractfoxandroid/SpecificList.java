package com.example.jakubkalinowski.contractfoxandroid;

/**
 * Created by Jatinder Verma on 9/29/16
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SpecificList extends AppCompatActivity {

    ListView listView;
    TextView titleText;
    String titleString;

    List<String> specificlist = new ArrayList<String>();

    HashMap<String, List<String>> hashmap = new HashMap<>();

    // Interior Services
    List<String> atticList = Arrays.asList("Attic Insulation", "Attic Door Installation", "Attic Fan Installation",
            "Attic Stairs Installation", "Mold Prevention", "Structural Integrity", "Vent Cleaning");
    List<String> basementList = Arrays.asList("Basement Remodel & Renovation", "Custom Shelving & Bookcases",
            "Entertainment Center Assembly", "Handrail & Stairs Installation", "Sump Pump Replacement", "TV Wall Mount",
            "Water Heater Insulation");
    List<String> bathroomList = Arrays.asList("Bathroom Remodel and Renovation",
            "Bathroom Fan Installation and Repair", "Caulking", "Drywall Repairs and Finishing",
            "Faucet Repair & Installation", "Light Fixture Installation and Repair", "Minor Plumbing Leaks",
            "Shower Door Installation and Repair", "Shower Head Servicing", "Sink Installation",
            "Tile Installation and Repair", "Toilet Repair and Installation", "Vanity and Bathroom Mirror Installation");
    List<String> bedroomList = Arrays.asList("Bedroom Remodel and Renovation", "Closet Door Installation",
            "Custom Shelving and Bookcases", "Drywall Repairs and Finishing", "Furniture Assembly",
            "Light Fixture Installation and Repair");
    List<String> garageList = Arrays.asList("Garage Remodel and Renovation",
            "Exhaust and Ventilation Fan Installation", "Garage Door Opener Installation",
            "Garage Door Weather Stripping", "Garage Floor Coating", "Garage Organization and Storage",
            "Handrail and Stairs Installation", "Light Fixture Installation and Repair");
    List<String> kitchenList =  Arrays.asList("Kitchen Remodel and Renovation",
            "Kitchen Backsplash Installation", "Cabinet Installation and Repair", "Countertop Installation and Repair",
            "Custom Kitchen Islands", "Faucet Repair and Installation", "Light Fixture Installation and Repair",
            "Sink Installation and Repair", "Tile Installation and Repair");
    List<String> generalRoomList = Arrays.asList("Dining Room Remodel and Renovation", "Living Room Remodel and Renovation",
            "Baby Proofing", "Crown Molding Installation & Repair", "Custom Shelving & Bookcases", "Furniture Assembly",
            "Mantel Installation", "Picture Hanging", "Wainscoting Installation & Repair");

    // Exterior Services
    List<String> acList = Arrays.asList("Air Conditioning Services", "Heating Services", "Remodels", "New Construction",
            "Oil-to-Gas Conversions", "Humidifiers", "Indoor Air Quality", "Maintenance Programs", "Repair and Replacement");
    List<String> chimneyList = Arrays.asList("Chimney Rebuilds", "Chimney Caps/Chimney Covers", "Crown Coat",
            "Dryer Vent Sweeps", "Fireplace Chimney Sweeps", "Fireplace Chimney Liners", "Fireplace Dampers",
            "Masonry Waterproofing Chimney Saver Applications", "Tuckpointing");
    List<String> patioList = Arrays.asList("Deck and Patio Repair", "Deck and Patio Construction",
            "Deck Staining, Painting, and Finishing", "Power Washing Services");
    List<String> doorList = Arrays.asList("Door Installation and Repair", "Pet Door Installation",
            "Door Painting", "Automatic Door Closer Installation", "Shower Door Installation and Repair");
    List<String> foundationList = Arrays.asList("Beam Replacement", "Bell Bottom Piers", "Drilled Piers",
            "Joist Replacement", "Magnum Helical System", "Polyurethene Injections", "Safe Adjust Post Installation", "Simple Pressed Piling",
            "Steel Piling", "Wood Replacement");
    List<String> roofList = Arrays.asList("Roof Remodel & Repair", "Roof Maintenance", "Solar Installation");
    List<String> wallList = Arrays.asList("Crown Molding Installation and Repair",
            "Custom Shelving and Bookcases", "Drywall Repair and Finishing", "Crown Molding and Trim Painting",
            "Picture Hanging", "Winscoting Installation and Repair");
    List<String> windowList = Arrays.asList("Hanging Blinds, Curtains and Drapes",
            "Weatherproofing", "Window Frame Repair", "Window Installation and Repair", "Window Shutter Installation");

    // Backyard Services
    List<String> drivewayList = Arrays.asList("Driveway Installation", "Driveway Remodel & Repair",
            "Power Washing Services");
    List<String> fenceList = Arrays.asList("Fence Installation", "Fence Repair", "Fence Painting", "Power Washing Services");
    List<String> gazeboList = Arrays.asList("Complete Outdoor Gazebo Services", "Gazebo Assembly",
            "Gazebo Renovation", "Gazebo Removel");
    List<String> landscapingList = Arrays.asList("Design", "Lawn Irrigation", "Maintenance", "Planting");
    List<String> poolList = Arrays.asList("Pool & Jacuzzi Installation", "Pool Repair & Maintenance",
            "Filter Installation", "Gas Heater Installation", "Handrails and Slides Installation",
            "Pool Pumps", "Safety Covers", "Solar Covers", "Vinyl Liner");
    List<String> septictankList = Arrays.asList("Septic Tank Installation", "Septic Tank Repair",
            "Grease Interceptors", "Grease Traps", "Real Estate Inspecitons", "Sump Pumps", "Vacuum Cleaned");
    List<String> treeList = Arrays.asList("Tree Planting", "Tree Removel", "Tree Pruning and Maintenance");
    List<String> wellList = Arrays.asList("Well Repair & Rehabilitation", "Pump and Motor Extraction",
            "Electrical Component Testing & Replacement");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_list);
        savedInstanceState = getIntent().getExtras();
        titleString = savedInstanceState.getString("name");

        // Add Interior Services to hashmap
        hashmap.put("Attic", atticList);
        hashmap.put("Basement", basementList);
        hashmap.put("Bathroom", bathroomList);
        hashmap.put("Bedroom", bedroomList);
        hashmap.put("Garage", garageList);
        hashmap.put("Kitchen", kitchenList);
        hashmap.put("Living Room", generalRoomList);

        // Add Exterior Services to hashmap
        hashmap.put("AC", acList);
        hashmap.put("Chimney", chimneyList);
        hashmap.put("Patio", patioList);
        hashmap.put("Door", doorList);
        hashmap.put("Foundation", foundationList);
        hashmap.put("Roof", roofList);
        hashmap.put("Walls", wallList);
        hashmap.put("Window", windowList);

        // Add Backyard Services to hashmap
        hashmap.put("Driveway", drivewayList);
        hashmap.put("Fence", fenceList);
        hashmap.put("Gazebo", gazeboList);
        hashmap.put("Landscape", landscapingList);
        hashmap.put("Pool", poolList);
        hashmap.put("Septic", septictankList);
        hashmap.put("Tree", treeList);
        hashmap.put("Well", wellList);

        specificlist = hashmap.get(titleString);

        titleText = (TextView) findViewById(R.id.title_ID);
        titleText.setText(titleString);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, specificlist);

        listView = (ListView) findViewById(R.id.listview_ID);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent i = new Intent(SpecificList.this, SearchViewListActivity.class);
                i.putExtra(  "serachedItem" , titleString );
                i.putExtra("flag" , true);
                startActivity(i);
            }
        });
    }
}
