package com.example.placesadd;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    public EditText nameText;
    public EditText phoneText;
    public EditText latText;
    public EditText lngText;
    public Button saveBtn;
    public Button readBtn;
    public TextView intruction;
    public RadioButton motoRBtn;
    public RadioButton carRBtn;
    public RadioButton bothRBtn;

    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    private final String fileName = "passKongPlaces.txt";
    ArrayList<Places> placesArr;


    String formatStr = "%-25s %-13s %-13s %-13s %-4s\n";
    String instruction = "1. Enter name as Name,Lastname\n" +
            "2. Enter phone number\n" +
            "3. Click on MAP and long press the location you want\n" +
            "4. Click on SAVE to save place\n" +
            "5. Clock on READ to see and delete your data";
    String serviceType = "null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = (EditText) findViewById(R.id.nameText);
        phoneText = (EditText) findViewById(R.id.phoneText);
        latText = (EditText) findViewById(R.id.latText);
        lngText = (EditText) findViewById(R.id.lngText);

        motoRBtn = (RadioButton) findViewById(R.id.motoRBtn);
        carRBtn = (RadioButton) findViewById(R.id.carRBtn);
        bothRBtn = (RadioButton) findViewById(R.id.bothRBtn);

        saveBtn = (Button) findViewById(R.id.saveBtn);
        readBtn = (Button) findViewById(R.id.readBtn);



        intruction = (TextView) findViewById(R.id.instruction);
        intruction.setText(instruction);

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


        loadData();

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.motoRBtn:
                if (checked)
                    serviceType = "moto";
                    break;
            case R.id.carRBtn:
                if (checked)
                    serviceType = "car";
                    break;
            case R.id.bothRBtn:
                if (checked)
                    serviceType = "both";
                    break;
        }
    }



    private void askPermissionAndWriteFile() {
        boolean canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //
        if (canWrite) {
            this.addPlace();
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
                        addPlace();
                    }
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Permission Cancelled!", Toast.LENGTH_SHORT).show();
        }
    }








    //add a new
    private void addPlace(){


        String name = nameText.getText().toString();
        String phone = phoneText.getText().toString();
        String lat = latText.getText().toString();
        String lng = lngText.getText().toString();

        if(!(name.equals("") || phone.equals("") || lat.equals("") || lng.equals(""))) {
            Places place = new Places(name, phone, lat, lng, serviceType);
            placesArr.add(place);

            writeFile();
            Toast.makeText(getApplicationContext(),"A place has been saved", Toast.LENGTH_LONG).show();
            nameText.setText("");
            phoneText.setText("");
            latText.setText("");
            lngText.setText("");
            motoRBtn.setChecked(false);
            carRBtn.setChecked(false);
            bothRBtn.setChecked(false);

        }

        else{
            Toast.makeText(getApplicationContext(),"Please fill all the data box", Toast.LENGTH_LONG).show();
        }
    }


    //write whatever is in array to text places.txt
    private void writeFile(){
        //Find Directory for fileName to be created
        File directory = Environment.getExternalStorageDirectory();
        String path = directory.getAbsolutePath() + "/" + fileName;

        try {
            String data = null;
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            for(int i = 0; i < placesArr.size(); i++)
            {
                //put file in format 'formatStr'
                data = String.format(formatStr,
                        placesArr.get(i).getName(),
                        placesArr.get(i).getPhone(),
                        placesArr.get(i).getLat(),
                        placesArr.get(i).getLng(),
                        placesArr.get(i).getServiceType());
                osw.append(data);

            }

            osw.close();
            fos.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void readFile() {
        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
        startActivityForResult(intent, 1);
    }

    public void goToMap(View view) {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){

            if(!(data.getStringExtra("Data")).equals("")){

                int i = Integer.parseInt(data.getStringExtra("Data"));

                if(i <= placesArr.size()) {
                    placesArr.remove(i-1);
                    writeFile();
                    Toast.makeText(getApplicationContext(),"A place has been deleted", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "File could not be deleted, please enter a valid number", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "File could not be deleted, please enter a valid number", Toast.LENGTH_SHORT).show();
            }

        }

        if(requestCode == 2 && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            String lat = extras.getString("latitude").substring(0,10);
            String lng = extras.getString("longitude").substring(0,10);

            latText.setText(lat);
            lngText.setText(lng);


        }

    }


    public void loadData() {
        placesArr.clear();

        File directory = Environment.getExternalStorageDirectory();
        String path = directory.getAbsolutePath() + "/" + fileName;

        String lineFromFile;

        File file = new File(path);

        if(file.exists())
        {
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

                while((lineFromFile = reader.readLine()) != null)
                {
                    StringTokenizer token = new StringTokenizer(lineFromFile, " ");
                    Places place = new Places(token.nextToken(), token.nextToken(), token.nextToken() ,token.nextToken(), token.nextToken());
                    placesArr.add(place);
                }

                reader.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }






}
