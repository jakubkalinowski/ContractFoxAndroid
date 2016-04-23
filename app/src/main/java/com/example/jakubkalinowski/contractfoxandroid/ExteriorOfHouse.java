package com.example.jakubkalinowski.contractfoxandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ExteriorOfHouse extends AppCompatActivity {

    Button garage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exterior_of_house);

        garage = (Button) findViewById(R.id.garage_ID);
        garage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ExteriorOfHouse.this, InsideGarage.class);
                startActivity(i);
                Log.d("garage", "button pushed");
            }
        });
    }
}
