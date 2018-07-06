package com.example.ericr.pokerquizzer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playGameBtn = (Button) findViewById(R.id.playGameBtn);
        //playGameBtn.setBackgroundColor(Color.TRANSPARENT);//this is  a test

        playGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(MainActivity.this, PlayActivity.class);
                startActivity(playIntent);
                finish();
            }
        });
    }
    public void onBackPressed() {
        backButtonOverride();
        return;
    }
    public void backButtonOverride(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialogStyleNeutral).create();
        alertDialog.setTitle("Quit?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        alertDialog.show();

    }
}
