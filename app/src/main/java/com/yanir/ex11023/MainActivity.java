package com.yanir.ex11023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 2;
    private final String FILENAME = "exttest.txt";

    EditText TextET;
    TextView TextTV;
    Intent si;
    boolean havePermission = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextET = (EditText) findViewById(R.id.TextET);
        TextTV = (TextView) findViewById(R.id.TextTV);

    }

    /**
     * this method add the text from the edit text element to the "inttest.txt" file
     * @param reset if true it will reset the file(delete everything in the file)
     */
    public void saveText(boolean reset){
        if(isExternalStorageAvailable() == false){
            return;
        }
        if(checkPermission() == false){
            requestPermission();
            if(havePermission == false){
                return;
            }
        }
        try {
            // gett the old text
            String oldText = getText();

            // Open the file.
            File externalDir = Environment.getExternalStorageDirectory();
            File file = new File(externalDir, FILENAME);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);

            // chack if reset is true and if it is write "" so the file will reset
            if(reset){
                writer.write("");
            }
            else {
                writer.write(oldText + String.valueOf(TextET.getText()));
            }

            // close the writer
            writer.close();

        } catch (IOException e) {
            System.out.println("error saving");
        }
    }



    /**
     * this method reads the text in "inttest.txt"
     * @return a String containing the text in "inttest.txt"
     */
    public String getText(){
        if(isExternalStorageAvailable() == false){
            return null;
        }
        if(checkPermission() == false){
            requestPermission();
            if(havePermission == false){
                return null;
            }
        }
        try {
            // Open the file.
            File externalDir = Environment.getExternalStorageDirectory();
            File file = new File(externalDir, FILENAME);
            file.getParentFile().mkdirs();
            FileReader reader = new FileReader(file);
            BufferedReader bR = new BufferedReader(reader);
            StringBuilder sB = new StringBuilder();

            // read the first line
            String line = bR.readLine();
            String newString;

            // check if the file is null
            if (line != null) {
                // if not go for all the lines
                while (line != null) {
                    sB.append(line + "\n");
                    line = bR.readLine();
                }
                // remove the /n at the end
                newString = sB.toString().substring(0, sB.length() - 1);
            }else{
                newString = sB.toString();
            }
            bR.close();
            return newString;
        } catch (IOException e) {
            System.out.println("error getting text");
        }
        return null;
    }


    /**
     * this method reads the text from the file and set the TextView to this text
     */
    public void updateText(){
        String Text = getText();
        TextTV.setText(Text);
    }

    /**
     * this method saves the text to the file and sets the TextView to this text
     * @param v
     */
    public void saveAndUpdate(View v){
        saveText(false);
        updateText();
    }

    /**
     * this method resets the file and also updates the TextView
     * @param v
     */
    public void resetText(View v){
        saveText(true);
        updateText();
    }

    /**
     * this method saves and updates the text and also exits the application
     * @param v
     */
    public void saveAndExit(View v) {
        saveAndUpdate(v);
        finishAndRemoveTask();
    }

    /**
     * This function presents the options menu for moving between activities.
     * @param menu The options menu in which you place your items.
     * @return true in order to show the menu, otherwise false.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.manu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        System.out.println(item.getTitle().toString());
        if (item.getTitle().toString().equals("credit")){
            si = new Intent(this, credit.class);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * this method checks if there is external storage available
     * @return true if there is external storage available, otherwise false
     */
    public boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * this method chacks if there is a premission to write to external storage
     * @return true if there is a premission to write to external storage, otherwise false
     */
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission to access external storage granted", Toast.LENGTH_LONG).show();
                havePermission = true;
            } else {
                Toast.makeText(this, "Permission to access external storage NOT granted", Toast.LENGTH_LONG).show();
                havePermission = false;
            }
        }
    }



}