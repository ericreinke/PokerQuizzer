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
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;

import static android.view.View.GONE;

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
        final Button[] answerButtons = {firstAnswerBtn,secondAnswerBtn,thirdAnswerBtn,fourthAnswerBtn};
        for(int i=0; i<4; i++){
            answerButtons[i].setBackgroundColor(Color.TRANSPARENT);//from #D7D7D7
        }


        createTimer();//creates timer
        newQuestion(questionTextView,answerButtons,scoreTextView);//CREATES A NEW QUESTION, mutates correctIndex (0-3);

    }

    //===========================
    public void onBackPressed() {
        backButtonOverride();
    }
    public void backButtonOverride(){
        AlertDialog alertDialog = new AlertDialog.Builder(PlayActivity.this).create();
        alertDialog.setTitle("Return to Main Menu?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent start = new Intent(PlayActivity.this, MainActivity.class);
                        startActivity(start);
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
    public void newQuestion(final TextView questionTextView, final Button[] answerButtons, final TextView scoreTextView){
        for(int i=0; i<4; i++){
            answerButtons[i].setBackgroundColor(Color.TRANSPARENT);
        }

        scoreTextView.setText("Score: "+score);
        Resources res = getResources();
        Random random = new Random();
        int randomQuestion = random.nextInt(3);//number is the number of questions and should probably not be hard coded

        ImageView hero1Img = findViewById(R.id.hero1);
        ImageView hero2Img = findViewById(R.id.hero2);
        ImageView villain1Img = findViewById(R.id.villain1);
        ImageView villain2Img = findViewById(R.id.villain2);
        ImageView flop1Img = findViewById(R.id.firstFlop);
        ImageView flop2Img = findViewById(R.id.secondFlop);
        ImageView flop3Img = findViewById(R.id.thirdFlop);
        ImageView turnImg = findViewById(R.id.turn);
        ImageView riverImg = findViewById(R.id.river);
        final ImageView[] community = {hero1Img, hero2Img, villain1Img, villain2Img, flop1Img, flop2Img, flop3Img, turnImg, riverImg};

        ImageView firstAnswerImg = findViewById(R.id.imageAnswer1);
        ImageView secondAnswerImg= findViewById(R.id.imageAnswer2);
        ImageView thirdAnswerImg = findViewById(R.id.imageAnswer3);
        ImageView fourthAnswerImg= findViewById(R.id.imageAnswer4);
        final ImageView[] imageAnswers={firstAnswerImg,secondAnswerImg,thirdAnswerImg,fourthAnswerImg};

        ImageView answerImg11 = findViewById(R.id.imageAnswer11);
        ImageView answerImg12 = findViewById(R.id.imageAnswer12);
        ImageView answerImg21 = findViewById(R.id.imageAnswer21);
        ImageView answerImg22 = findViewById(R.id.imageAnswer22);
        ImageView answerImg31 = findViewById(R.id.imageAnswer31);
        ImageView answerImg32 = findViewById(R.id.imageAnswer32);
        ImageView answerImg41 = findViewById(R.id.imageAnswer41);
        ImageView answerImg42 = findViewById(R.id.imageAnswer42);
        final ImageView[] imageAnswers2 = {answerImg11,answerImg21, answerImg31, answerImg41, answerImg12, answerImg22, answerImg32, answerImg42};

        for(int i=0; i<4; i++){
            imageAnswers[i].setVisibility(View.VISIBLE);
        }
        for(int i=0; i<8; i++){
            imageAnswers2[i].setVisibility(View.VISIBLE);
        }


//declare question array and answer array
        int questionType=res.getIntArray(R.array.answerType)[randomQuestion];
        Drawable[] drawableArray = new Drawable[4];
        Drawable[] drawableArray2= new Drawable[4];
        String [] buttonText = new String[4];
        Drawable[] drawableCommunity = retrieveCommunity(randomQuestion);

        if(questionType==1){
            drawableArray=retrieveTypeOne(randomQuestion);
            buttonText = returnBlankStrings();
        }
        else if(questionType==2){
            Drawable[]temp= retrieveTypeTwo(randomQuestion);
            buttonText = returnBlankStrings();
            for(int i=0;i<8;i+=2){
                drawableArray[i/2]=temp[i];
                drawableArray2[i/2]=temp[i+1];

            }
        }
        else if(questionType==3){
            for(int i=0; i<4; i++){
                answerButtons[i].setBackgroundColor(Color.parseColor("#D7D7D7"));
            }
            buttonText=retrieveTypeThree(randomQuestion);
        }

        String[] questions = res.getStringArray(R.array.question_array);
        final String whyAnswer=res.getStringArray(R.array.why_answers)[randomQuestion];


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
            Drawable hold= drawableArray[i]; // shuffle the first array
            drawableArray[i]=drawableArray[randomShuffle];
            drawableArray[randomShuffle]=hold;

            Drawable hold2 = drawableArray2[i]; //shuffle the second array
            drawableArray2[i]=drawableArray2[randomShuffle];
            drawableArray2[randomShuffle]=hold2;

            String holdS = buttonText[i];//shuffle the button text too
            buttonText[i]=buttonText[randomShuffle];
            buttonText[randomShuffle]=holdS;
        }

//set the question and the buttons to the randomQuestion
        questionTextView.setText(questions[randomQuestion]);//+" correct index is: "+correctIndex);

        for(int i=0; i<4; i++){
            imageAnswers[i].setImageDrawable(drawableArray[i]);
            imageAnswers2[i].setImageDrawable(drawableArray[i]);
            imageAnswers2[i+4].setImageDrawable(drawableArray2[i]);
            if(questionType!=1){
                imageAnswers[i].setVisibility(GONE);
            }
            if(questionType!=2){
                imageAnswers2[i].setVisibility(GONE);
                imageAnswers2[i+4].setVisibility(GONE);
            }
            answerButtons[i].setText(buttonText[i]);
        }
        //draw the community cards
        for(int i=0; i<9; i++){
            community[i].setImageDrawable(drawableCommunity[i]);
        }

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
        AlertDialog alertDialog;
        if(correct){
            alertDialog = new AlertDialog.Builder(PlayActivity.this,R.style.AlertDialogStyleCorrect).create();
            alertDialog.setMessage(getString(R.string.correct_answer));
            score++;
        }
        else{
            alertDialog = new AlertDialog.Builder(PlayActivity.this,R.style.AlertDialogStyleIncorrect).create();
            alertDialog.setMessage(getString(R.string.incorrect_answer));
        }
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
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
    public Drawable[] retrieveTypeOne(int randomQuestion){
        Resources res= getResources();
        TypedArray answerResources = res.obtainTypedArray(R.array.answers);
        int resId = answerResources.getResourceId(randomQuestion, -1);//gets the ID of the "randomquestion"th string array
        answerResources.recycle();//free
        TypedArray questionAnswers = res.obtainTypedArray(resId);//this is for a specific answer
        Drawable[] drawableArray = new Drawable[4];

        for(int i=0; i<4; i++){
            drawableArray[i]=questionAnswers.getDrawable(i);
        }
        questionAnswers.recycle();//free
        return drawableArray;
    }
    public Drawable[] retrieveTypeTwo(int randomQuestion){//returns an array with 8 drawables.  These drawables are split up after this fucktion call
        Resources res = getResources();
        TypedArray answerResources = res.obtainTypedArray(R.array.answers);
        int resId = answerResources.getResourceId(randomQuestion, -1);//gets the ID of the "randomquestion"th string array
        answerResources.recycle();//free
        TypedArray questionAnswers = res.obtainTypedArray(resId);//this is for a specific answer
        Drawable[] drawableArray = new Drawable[8];

        for(int i=0; i<8; i++){
            drawableArray[i]=questionAnswers.getDrawable(i);
        }

        questionAnswers.recycle();//free
        return drawableArray;
    }
    public String[] retrieveTypeThree (int randomQuestion){

        Resources res = getResources();
        TypedArray answerResources = res.obtainTypedArray(R.array.answers);
        int resId = answerResources.getResourceId(randomQuestion, -1);//gets the ID of the "randomquestion"th string array
        answerResources.recycle();//free
        String[] buttonText = res.getStringArray(resId);//this is for a specific answer
        return buttonText;
    }
    public Drawable [] retrieveCommunity (int randomQuestion){
        Resources res = getResources();
        TypedArray communityResources = res.obtainTypedArray(R.array.community);
        int resId = communityResources.getResourceId(randomQuestion, -1);//gets the ID of the "randomquestion"th string array
        communityResources.recycle();//free
        TypedArray communityCards = res.obtainTypedArray(resId);//this is for a specific answer
        Drawable[] drawableArray = new Drawable[9];

        for(int i=0; i<9; i++){
            drawableArray[i]=communityCards.getDrawable(i);
        }

        communityCards.recycle();//free

        return drawableArray;
    }
    public String[] returnBlankStrings(){
        String[] blanks=new String[4];
        for(int i=0; i<4; i++){
            blanks[i]=getResources().getString(R.string.blank);
        }
        return blanks;
    }
}