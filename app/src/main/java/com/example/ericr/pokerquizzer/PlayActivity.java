package com.example.ericr.pokerquizzer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;

public class PlayActivity extends AppCompatActivity {
    private boolean isPaused=false;
    private int correctIndex=0;
    private long resumeFromMillis = 30000; //timer starts at 30 seconds
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //declare buttons and textView
        final TextView questionTextView = findViewById(R.id.questionTextView);
        final TextView scoreTextView = findViewById(R.id.scoreTextView);
        Button firstAnswerBtn = findViewById(R.id.firstAnswerBtn);
        Button secondAnswerBtn = findViewById(R.id.secondAnswerBtn);
        Button thirdAnswerBtn = findViewById(R.id.thirdAnswerBtn);
        Button fourthAnswerBtn = findViewById(R.id.fourthAnswerBtn);
        firstAnswerBtn.setBackgroundColor(Color.TRANSPARENT);
        secondAnswerBtn.setBackgroundColor(Color.TRANSPARENT);
        thirdAnswerBtn.setBackgroundColor(Color.TRANSPARENT);
        fourthAnswerBtn.setBackgroundColor(Color.TRANSPARENT);
        final Button[] answerButtons = {firstAnswerBtn,secondAnswerBtn,thirdAnswerBtn,fourthAnswerBtn};

        createTimer();//creates timer
        newQuestion(questionTextView,answerButtons,scoreTextView);//CREATES A NEW QUESTION, mutates correctIndex (0-3);

    }

    //===========================
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
    @SuppressLint("ResourceType")
    public void newQuestion(final TextView questionTextView, final Button[] answerButtons, final TextView scoreTextView){
        scoreTextView.setText("Score: "+score);
        Resources res = getResources();
        Random random = new Random();
        int randomQuestion = 0;//random.nextInt(3);//number is the number of questions and should probably not be hard coded

//declare question array and answer array
        TypedArray answerResources = res.obtainTypedArray(R.array.answers);
        int resId = answerResources.getResourceId(randomQuestion, -1);//gets the ID of the "randomquestion"th string array
        answerResources.recycle();//free
        TypedArray questionAnswers = res.obtainTypedArray(resId);
        Drawable[] drawableArray = {questionAnswers.getDrawable(0),questionAnswers.getDrawable(1),questionAnswers.getDrawable(2),questionAnswers.getDrawable(3)};
        questionAnswers.recycle();

        String[] questions = res.getStringArray(R.array.question_array);
        String[] whyArray = res.getStringArray(R.array.why_answers);
        final String whyAnswer=whyArray[randomQuestion];

        ImageView firstAnswerImg = findViewById(R.id.imageAnswer1);
        ImageView secondAnswerImg= findViewById(R.id.imageAnswer2);
        ImageView thirdAnswerImg = findViewById(R.id.imageAnswer3);
        ImageView fourthAnswerImg= findViewById(R.id.imageAnswer4);

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
            Drawable hold= drawableArray[i];//quick lil swapperoo
            drawableArray[i]=drawableArray[randomShuffle];
            drawableArray[randomShuffle]=hold;
        }

//set the question and the buttons to the randomQuestion
        questionTextView.setText(questions[randomQuestion]);

        firstAnswerImg .setImageDrawable(drawableArray[0]);
        secondAnswerImg.setImageDrawable(drawableArray[1]);
        thirdAnswerImg .setImageDrawable(drawableArray[2]);
        fourthAnswerImg.setImageDrawable(drawableArray[3]);

        answerButtons[0].setText(R.string.blank);
        answerButtons[1].setText(R.string.blank);
        answerButtons[2].setText(R.string.blank);
        answerButtons[3].setText(R.string.blank);

//click listseners
        for (int i = 0; i < 4; i++) {
            final int j = i;
            if (j != correctIndex) {
                answerButtons[j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isPaused=true;
                        questionDialog(questionTextView, answerButtons,scoreTextView,false,whyAnswer);
                    }
                });
            }
            else{
                answerButtons[j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isPaused=true;
                        questionDialog(questionTextView,answerButtons,scoreTextView,true, whyAnswer);
                    }
                });
            }

        }

    }
    public void questionDialog(final TextView questionTextView, final Button[] answerButtons, final TextView scoreTextView,boolean correct, final String whyAnswer){
        AlertDialog alertDialog = new AlertDialog.Builder(PlayActivity.this).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        if(correct){
            alertDialog.setMessage(getString(R.string.correct_answer));
            score++;
        }
        else{
            alertDialog.setMessage(getString(R.string.incorrect_answer));
        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Next Question",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newQuestion(questionTextView, answerButtons, scoreTextView);
                        dialog.dismiss();
                        isPaused=false;
                        createTimer();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Why?",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        whyDialog(questionTextView, answerButtons,scoreTextView,whyAnswer);
                    }
                });
        alertDialog.show();
    }
    public void createTimer(){
        //Timer initiation
        final TextView timerTextView = findViewById(R.id.timerTextView);
        new CountDownTimer(resumeFromMillis, 10) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Seconds Remaining: " + millisUntilFinished / 1000);
                if(isPaused){
                    resumeFromMillis=millisUntilFinished;
                    cancel();
                }
            }
            public void onFinish() {
                timerTextView.setText("Done!");
                cancel();
                //start gameOverActivity
                finish();
            }
        }.start();
    }
    public void whyDialog(final TextView questionTextView, final Button[] answerButtons, final TextView scoreTextView, final String whyAnswer){
        //dont forget to change isPaused=false and call creatTimer();
        //android.os.SystemClock.sleep(750);
        AlertDialog alertDialog = new AlertDialog.Builder(PlayActivity.this).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(whyAnswer);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Next Question",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newQuestion(questionTextView, answerButtons, scoreTextView);
                        dialog.dismiss();
                        isPaused=false;
                        createTimer();
                    }
                });
        alertDialog.show();
    }

}