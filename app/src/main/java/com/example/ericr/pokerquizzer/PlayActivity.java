package com.example.ericr.pokerquizzer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;
import java.util.Timer;

public class PlayActivity extends AppCompatActivity {
    private boolean isPaused=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Random random = new Random();
        int randomQuestion = random.nextInt(3);//number is the number of questions and should probably not be hard coded

        Resources res = getResources();

    //Timer initiation
        final TextView timerTextView = findViewById(R.id.timerTextView);
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("seconds remaining: " + millisUntilFinished / 1000);
                if(isPaused) {
                    //transfer timer info to next Activity
                    cancel();
                    finish();
                }
            }
            public void onFinish() {
               timerTextView.setText("Done!");
               //start gameOverActivity
            }
        }.start();

    //declare question array and answer array
        TypedArray answerResources = res.obtainTypedArray(R.array.answers);
        int resId = answerResources.getResourceId(randomQuestion, -1);//gets the ID of the nth string array
        answerResources.recycle();//free
        //if (resId < 0) {QUESTION DOES NOT EXIST.  CHECK strings.xml OR RNG}
        String [] questionAnswers = res.getStringArray(resId);
        String[] questions = res.getStringArray(R.array.question_array);

    //declare buttons and textView
        TextView questionTextView = findViewById(R.id.questionTextView);
        Button firstAnswerBtn = findViewById(R.id.firstAnswerBtn);
        Button secondAnswerBtn = findViewById(R.id.secondAnswerBtn);
        Button thirdAnswerBtn = findViewById(R.id.thirdAnswerBtn);
        Button fourthAnswerBtn = findViewById(R.id.fourthAnswerBtn);
        Button[] answerButtons = {firstAnswerBtn,secondAnswerBtn,thirdAnswerBtn,fourthAnswerBtn};

    //shuffle the four answers:
        int correctIndex=0;
        for(int i=0; i<4; i++){
            int randomShuffle = random.nextInt(4);
            if(i==correctIndex){//keep track of the correct answer (which always starts at 0)
                correctIndex=randomShuffle;
            }
            else if(randomShuffle==correctIndex){
                correctIndex=i;
            }
            String hold=questionAnswers[i];//quick lil swapperoo
            questionAnswers[i]=questionAnswers[randomShuffle];
            questionAnswers[randomShuffle]=hold;
        }

    //set1  the question and the buttons to the randomQuestion
        questionTextView.setText(questions[randomQuestion]);
        answerButtons[0].setText(questionAnswers[0]);
        answerButtons[1].setText(questionAnswers[1]);
        answerButtons[2].setText(questionAnswers[2]);
        answerButtons[3].setText(questionAnswers[3]);


        answerButtons[correctIndex].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playIntent = new Intent(PlayActivity.this, PlayActivity.class);
                startActivity(playIntent);
                isPaused=true;
                finish();
            }
        });

    }

    public void onBackPressed() {
        backButtonOverride();
    }

    public void backButtonOverride(){
        AlertDialog alertDialog = new AlertDialog.Builder(PlayActivity.this).create();
        alertDialog.setTitle("!");
        alertDialog.setMessage("Return to Main Menu?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent start = new Intent(PlayActivity.this, MainActivity.class);
                        startActivity(start);
                        finish();
                    }
                });
        alertDialog.show();

    }

}