package com.example.placesadd;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    public TextView viewText;
    public EditText index;
    public TextView instructtion;

    private final String fileName = "passKongPlaces.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        viewText = (TextView) findViewById(R.id.viewText);
        instructtion = (TextView) findViewById(R.id.instruction);
        index = (EditText) findViewById(R.id.index);

        viewText.setMovementMethod(new ScrollingMovementMethod());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setViewText();


    }

    private void setViewText() {
        //locate the file
        File directory = Environment.getExternalStorageDirectory();
        String path = directory.getAbsolutePath() + "/" + fileName;

        String s = "";
        String fileContent = "";
        try {
            File file = new File(path);
            FileInputStream fIn = new FileInputStream(file);
            BufferedReader bReader = new BufferedReader(
                    new InputStreamReader(fIn));

            for(int i = 0; (s = bReader.readLine()) != null; i++)
            {
                //add index so user can remove a file
                fileContent += String.format("%2d: ", i+1) + s + "\n";
            }

            bReader.close();

            this.viewText.setText(fileContent);
        } catch (IOException e) {
            e.printStackTrace();

        }


    }

    public void saveIndex(View view) {

        Intent intent = new Intent();
        String i = index.getText().toString();
        intent.putExtra("Data", i);
        setResult(RESULT_OK, intent);
        finish();

    }


}
