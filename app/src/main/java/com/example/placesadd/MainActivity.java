package com.example.placesadd;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public EditText nameText;
    public EditText phoneText;
    public EditText latText;
    public EditText lngText;
    public Button saveBtn;
    public Button readBtn;
    public Button dirBtn;
    public TextView viewText;

    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    private final String fileName = "places.txt";
    ArrayList<Places> placesArr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = (EditText) findViewById(R.id.nameText);
        phoneText = (EditText) findViewById(R.id.phoneText);
        latText = (EditText) findViewById(R.id.latText);
        lngText = (EditText) findViewById(R.id.lngText);

        saveBtn = (Button) findViewById(R.id.saveBtn);
        readBtn = (Button) findViewById(R.id.readBtn);
        dirBtn = (Button) findViewById(R.id.dirBtn);

        viewText = (TextView) findViewById(R.id.viewText);

        placesArr = new ArrayList<Places>();

        saveBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                askPermissionAndWriteFile();
            }
            

        });

        readBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                askPermissionAndReadFile();
            }

        });

        dirBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                listExternalStorages();
            }

        });

    }

    private void askPermissionAndWriteFile() {
        boolean canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //
        if (canWrite) {
            this.writeFile();
        }
    }

    private void askPermissionAndReadFile() {
        boolean canRead = this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        //
        if (canRead) {
            this.readFile();
        }
    }

    // With Android Level >= 23, you have to ask the user
    // for permission with device (For example read/write data on the device).
    private boolean askPermission(int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have permission
            int permission = ActivityCompat.checkSelfPermission(this, permissionName);


            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{permissionName},
                        requestId
                );
                return false;
            }
        }
        return true;
    }

    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        // Note: If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0) {
            switch (requestCode) {
                case REQUEST_ID_READ_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        readFile();
                    }
                }
                case REQUEST_ID_WRITE_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        writeFile();
                    }
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Permission Cancelled!", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeFile(){
        File extStore = Environment.getExternalStorageDirectory();
        // ==> /storage/emulated/0/note.txt
        String path = extStore.getAbsolutePath() + "/" + fileName;
        Log.i("ExternalStorageDemo", "Save to: " + path);

        String name = nameText.getText().toString();
        String phone = phoneText.getText().toString();
        String lat = latText.getText().toString();
        String lng = lngText.getText().toString();

        Places place = new Places(name,phone,lat,lng);
        placesArr.add(place);

        try {
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            for(int i = 0; i < placesArr.size(); i++)
            {
                String data = placesArr.get(i).getName() + "|"
                        + placesArr.get(i).getPhone() + "|"
                        + placesArr.get(i).getLat() + "|"
                        + placesArr.get(i).getLng() + "\n";

                osw.write(data);

            }
            osw.close();
            fos.close();

            nameText.setText("");
            phoneText.setText("");
            latText.setText("");
            lngText.setText("");

            Toast.makeText(getApplicationContext(), fileName + " saved", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readFile() {

        File extStore = Environment.getExternalStorageDirectory();
        // ==> /storage/emulated/0/note.txt
        String path = extStore.getAbsolutePath() + "/" + fileName;
        Log.i("ExternalStorageDemo", "Read file: " + path);

        String s = "";
        String fileContent = "";
        try {
            File myFile = new File(path);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));

            while ((s = myReader.readLine()) != null) {
                fileContent += s + "\n";
            }
            myReader.close();

            this.viewText.setText(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), fileContent, Toast.LENGTH_LONG).show();
    }

    private void listExternalStorages() {
        StringBuilder sb = new StringBuilder();

        sb.append("Data Directory: ").append("\n - ")
                .append(Environment.getDataDirectory().toString()).append("\n");

        sb.append("Download Cache Directory: ").append("\n - ")
                .append(Environment.getDownloadCacheDirectory().toString()).append("\n");

        sb.append("External Storage State: ").append("\n - ")
                .append(Environment.getExternalStorageState().toString()).append("\n");

        sb.append("External Storage Directory: ").append("\n - ")
                .append(Environment.getExternalStorageDirectory().toString()).append("\n");

        sb.append("Is External Storage Emulated?: ").append("\n - ")
                .append(Environment.isExternalStorageEmulated()).append("\n");

        sb.append("Is External Storage Removable?: ").append("\n - ")
                .append(Environment.isExternalStorageRemovable()).append("\n");

        sb.append("External Storage Public Directory (Music): ").append("\n - ")
                .append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString()).append("\n");

        sb.append("Download Cache Directory: ").append("\n - ")
                .append(Environment.getDownloadCacheDirectory().toString()).append("\n");

        sb.append("Root Directory: ").append("\n - ")
                .append(Environment.getRootDirectory().toString()).append("\n");

        Log.i("ExternalStorageDemo", sb.toString());
        this.viewText.setText(sb.toString());
    }



}
