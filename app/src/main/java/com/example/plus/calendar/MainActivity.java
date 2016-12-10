package com.example.plus.calendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button button;
    AutoCompleteTextView tvID;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button= (Button) findViewById(R.id.loginButton);
        tvID = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_id);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = tvID.getText().toString().trim();
                if(TextUtils.isEmpty(userID)){
                    Toast.makeText(getApplicationContext(),"Please, write your name and surname", Toast.LENGTH_LONG).show();
                }
                else if(!userID.matches("^[a-zA-Z0-9_ ]+$")){
                    Toast.makeText(getApplicationContext(),
                            "Username contains only characters in the adapterList, a-z, A-Z, 0-9,underscore and space",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    intent = new Intent(getApplicationContext(), EventActivity.class);
                    intent.putExtra("USER_ID",userID);
                    startActivity(intent);
                }

            }
        });



    }
}
