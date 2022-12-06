package com.group2.memory;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    //// try mac
    public RecyclerView recyclerView;
    public RecyclerView.Adapter recyclerViewAdapter;
    List<Bitmap> pics = new ArrayList<>();
    List<String> qr_codes = new ArrayList<>();
//////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton addButton = findViewById(R.id.fab);
        addButton.setOnClickListener(v -> {
            takeQrCodePicture();
        });
        recyclerView = (RecyclerView)findViewById(R.id.rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this /* the activity */, 2);
        recyclerViewAdapter = new RecyclerAdapter(pics, qr_codes);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);

        Button button = findViewById(R.id.sendLogButton);
        button.setOnClickListener(v -> {
            try {
                log();
            } catch (ActivityNotFoundException e){
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IntentIntegrator.REQUEST_CODE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            String path = extras.getString(Intents.Scan.RESULT_BARCODE_IMAGE_PATH);
            // Obtain a bitmap for display
            Bitmap bmp = BitmapFactory.decodeFile(path);
            String code = extras.getString(Intents.Scan.RESULT);
            TextView myAwesomeTextView = (TextView)findViewById(R.id.textView);
            pics.add(bmp);
            qr_codes.add(code);
            System.out.println(bmp+"     "+code);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }


    public void takeQrCodePicture(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(MyCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setOrientationLocked(false);
        integrator.addExtra(Intents.Scan.BARCODE_IMAGE_ENABLED,true);
        integrator.initiateScan();
    }

    /**
     * Crate a Logbook Format as JSON and send via LogBuch App Activity
     */
    private void log() {
        Intent intent = new Intent("ch.blockwoche.intent.LOG");
        // format depends on app, see logbook format guideline
        JSONObject log = new JSONObject();
        ArrayList<String> pair;
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        try {
            log.put("task", "Memory");
            for(int i = 0; i < qr_codes.size(); i+=2){
                pair = new ArrayList<>();
                pair.add(qr_codes.get(i));
                pair.add(qr_codes.get(i+1));
                list.add(pair);
            }
            log.put("solution", new JSONArray(list));
        } catch (JSONException j){
        }
        intent.putExtra("ch.blockwoche.logmessage", log.toString());
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e){
        }
    }


}
