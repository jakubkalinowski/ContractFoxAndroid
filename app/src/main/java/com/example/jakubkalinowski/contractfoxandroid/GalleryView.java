package com.example.jakubkalinowski.contractfoxandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GalleryView extends AppCompatActivity {

    private String contractorID;
    private StorageReference galleryImg;
    int size;
    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "Before", "After", "Before", "After",
            "Before", "After", "Before", "After",
    };

    String[] listviewImage = new String[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_listview);

        savedInstanceState = getIntent().getExtras();
        contractorID = savedInstanceState.getString("id");
//        contractorID = getIntent().getExtras().getString("id");
        Log.i("contID-:", contractorID);

        lastGalleryImg = FirebaseStorage.getInstance().getReference("Before&AfterPictureGallery/"+contractorID);

        galleryImg = FirebaseStorage.getInstance().getReference("Before&AfterPictureGallery/"+contractorID);

        size = 7;
        for(int i=0;i<size;i++){
            if (galleryImg != null) {
                listviewImage[i] = galleryImg.child("img"+i).toString();
            }
        }

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 8; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
//            hm.put("listview_discription", listviewShortDescription[i]);
            hm.put("listview_image", listviewImage[i]);
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title"};
        int[] to = {R.id.listview_image, R.id.listview_item_title};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.gallery_listview, from, to);
        ListView androidListView = (ListView) findViewById(R.id.list_view);
        androidListView.setAdapter(simpleAdapter);
    }
}