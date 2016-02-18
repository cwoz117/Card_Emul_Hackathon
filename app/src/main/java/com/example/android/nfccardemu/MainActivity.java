package com.example.android.nfccardemu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        writeUserInfo(new User());
        setContentView(R.layout.activity_main);
    }

    /** grabs user input from the UI, parses it accordingly,
     * and writes it to a file. Fields are then cleared.
     * @param view - response to listener for button press
     */
    public void getUserInfo(View view){
        String userName, password;

        //get user input
        EditText user = (EditText) findViewById(R.id.UCID);
        EditText pass = (EditText) findViewById(R.id.Password);

        //convert to string and set user in card service
        userName = user.getText().toString();
        password = pass.getText().toString();

        //update button and textfield text
        Button donePressed = (Button) findViewById(R.id.done);
        donePressed.setText("Credentials saved. Restart app to update credentials");
        user.setText("");
        pass.setText("");

        writeUserInfo(new User(userName, password));
    }

    /** writes user credentions to file.
     * filename = "userCreds"
     * @param userInfo - user information
     */
    public void writeUserInfo(User userInfo){
        String filename = "userCreds";
        FileOutputStream outputStream;

        try{
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(userInfo.toString().getBytes());
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
