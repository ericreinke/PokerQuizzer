package com.example.ericr.pokerquizzer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
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
    private int[] history = {-1,-1,-1,-1,-1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //declare buttons and textView
        final TextView questionTextView = findViewById(R.id.questionTextView);
        final TextView scoreTextView = findViewById(R.id.scoreTextView);
        final TextView timerTextView = findViewById(R.id.timerTextView);
        timerTextView.setText(getString(R.string.seconds_remaing)+"  ");


        ImageButton firstAnswerBtn = findViewById(R.id.firstAnswerBtn);
        ImageButton secondAnswerBtn = findViewById(R.id.secondAnswerBtn);
        ImageButton thirdAnswerBtn = findViewById(R.id.thirdAnswerBtn);
        ImageButton fourthAnswerBtn = findViewById(R.id.fourthAnswerBtn);
        final ImageButton[] answerButtons = {firstAnswerBtn,secondAnswerBtn,thirdAnswerBtn,fourthAnswerBtn};



        newQuestion(questionTextView,answerButtons,scoreTextView);//CREATES A NEW QUESTION, mutates correctIndex (0-3);

    }
    //===========================
    public void onBackPressed() {
        backButtonOverride();
    }
    public void backButtonOverride(){
        AlertDialog alertDialog = new AlertDialog.Builder(PlayActivity.this).create();
        alertDialog.setTitle(getString(R.string.return_main));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent start = new Intent(PlayActivity.this, MainActivity.class);
                        startActivity(start);
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }
    public void newQuestion(final TextView questionTextView, final ImageButton[] answerButtons, final TextView scoreTextView){
        for(int i=0; i<4; i++){
            //answerButtons[i].setBackgroundColor(Color.parseColor("#D7D7D7"));//from #D7D7D7
        }
        scoreTextView.setText(getString(R.string.score)+score);

        Resources res = getResources();
        TypedArray answerResources = res.obtainTypedArray(R.array.question_array);

        Random random = new Random();
        int randomQuestion;
        //this loop simulates a stack with array "history"
        for(;;){
            randomQuestion = random.nextInt(answerResources.length());
            if(!inHistory(randomQuestion )){
                for(int i=0; i<4; i++){
                    history[i]=history[i+1];
                }
                history[4]=randomQuestion;
                break;
            }
        }
        answerResources.recycle();
        //randomQuestion=0;
        TextView heroTextView = findViewById(R.id.heroTextView);
        TextView villainTextView = findViewById(R.id.villainTextView);

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

        for(int i=0; i<9; i++){
            community[i].setVisibility(View.INVISIBLE);
        }
        heroTextView.setVisibility(View.INVISIBLE);
        villainTextView.setVisibility(View.INVISIBLE);


//declare question array and answer array   ;
        int questionType=res.getIntArray(R.array.answerType)[randomQuestion];
        Drawable[] drawableArray;
        Drawable[] drawableCommunity = retrieveCommunity(randomQuestion);

        if(true){//NOW IT DOESN'T MATTER WHAT QUESTION TYPE. eventually remove this if
            drawableArray=retrieveDrawableArray(questionType, randomQuestion);
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
        }

//set the question and the buttons to the randomQuestion
        setQuestionString(heroTextView, villainTextView,  randomQuestion,  questions,  questionTextView ,  community ,   drawableCommunity,
           drawableArray, answerButtons);



//click listseners
        for (int i = 0; i < 4; i++) {
            //final int j = i;
            if (i != correctIndex) {
                answerButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isPaused=true;
                        questionDialog(questionTextView, answerButtons,scoreTextView,false,whyAnswer);
                    }
                });
            }
            else{
                answerButtons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isPaused=true;
                        questionDialog(questionTextView,answerButtons,scoreTextView,true, whyAnswer);
                    }
                });
            }

        }


    }
    public void questionDialog(final TextView questionTextView, final ImageButton[] answerButtons, final TextView scoreTextView,boolean correct, final String whyAnswer){
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
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.next_question),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newQuestion(questionTextView, answerButtons, scoreTextView);
                        dialog.dismiss();
                        isPaused=false;

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.why),
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
                timerTextView.setText(getString(R.string.seconds_remaing) + millisUntilFinished / 1000);
                if(isPaused){
                    resumeFromMillis=millisUntilFinished;
                    cancel();
                }
            }
            public void onFinish() {
                timerTextView.setText(getString(R.string.timer_over));
                cancel();

                AlertDialog alertDialog = new AlertDialog.Builder(PlayActivity.this,R.style.AlertDialogStyleNeutral).create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.setTitle(getString(R.string.game_over));
                alertDialog.setMessage("You got "+score+" questions correct");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.main_menu),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent start = new Intent(PlayActivity.this, MainActivity.class);
                                startActivity(start);
                                finish();
                            }
                        });
                alertDialog.show();
                //start gameOverActivity
            }
        }.start();
    }
    public void whyDialog(final TextView questionTextView, final ImageButton[] answerButtons, final TextView scoreTextView, final String whyAnswer){
        //dont forget to change isPaused=false and call creatTimer();
        //android.os.SystemClock.sleep(750);
        AlertDialog alertDialog = new AlertDialog.Builder(PlayActivity.this,R.style.AlertDialogStyleNeutral).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(whyAnswer);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.next_question),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newQuestion(questionTextView, answerButtons, scoreTextView);
                        dialog.dismiss();
                        isPaused=false;

                    }
                });
        alertDialog.show();
    }
    public Drawable[] retrieveDrawableArray(int questionType,int randomQuestion){
        Resources res= getResources();
        TypedArray answerResources = res.obtainTypedArray(R.array.answers);
        int resId = answerResources.getResourceId(randomQuestion, -1);//gets the ID of the "randomquestion"th string array
        answerResources.recycle();//free
        TypedArray questionAnswers = res.obtainTypedArray(resId);//this is for a specific answer
        Drawable[] drawableArray = new Drawable[4];

        if(questionType==1){
            for(int i=0; i<4; i++){
                Drawable d1 = questionAnswers.getDrawable(i);
                Bitmap bm1 = ((BitmapDrawable)d1).getBitmap();
                Drawable card1 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bm1, dipToPixels(this,55), dipToPixels(this,78), true));
                drawableArray[i]=card1;
            }//ezpz
        }
        else if(questionType==2){
            for(int i=0; i<4; i++){
                Drawable d1 = questionAnswers.getDrawable(i*2);
                Drawable d2 = questionAnswers.getDrawable(i*2+1);
                Bitmap bm1 = ((BitmapDrawable)d1).getBitmap();
                Bitmap bm2 = ((BitmapDrawable)d2).getBitmap();

                Drawable card1 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bm1, dipToPixels(this,55), dipToPixels(this,78), true));
                Drawable card2 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bm2, dipToPixels(this,55), dipToPixels(this,78),true));

                Drawable[] layers = new Drawable[]{card1, card2};
                LayerDrawable ld= new LayerDrawable(layers);
                ld.setLayerGravity(0, Gravity.LEFT);
                ld.setLayerGravity(1, Gravity.LEFT);
                ld.setLayerInset(0,0,0,0,0);
                ld.setLayerInset(1,50,0,0,0);
                drawableArray[i]=ld;
            }

        }
        else if(questionType==3){
            //text canvas paint hell ya
        }

        //Drawable[] testArray = {getDrawable(R.drawable.c2s),getDrawable(R.drawable.c2s),getDrawable(R.drawable.c2s),getDrawable(R.drawable.c2s)};

        questionAnswers.recycle();//free
        return drawableArray;//testArray;
    }
    public Drawable[] retrieveTypeTwo(int randomQuestion){
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
    }//returns an array with 8 drawables.  These drawables are split up after this fucktion call
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
    public boolean inHistory(int random){
        boolean found =false;
        for(int i=0; i<5; i++){
            if(random==history[i]){
                found = true;
                break;
            }
        }
        return found;
    }

    public void setQuestionString(final TextView heroTextView, final TextView villainTextView, int randomQuestion, String[] questions, TextView questionTextView ,final ImageView[] community ,
                                  final Drawable[] drawableCommunity, final Drawable[] answerImages, final ImageButton[] answerButtons){
        questionTextView.setText(questions[randomQuestion]);//+" correct index is: "+correctIndex);

        AlphaAnimation anim = new AlphaAnimation(0.0f,1.0f);
        anim.setDuration(1000);
        questionTextView.startAnimation(anim);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                setCommunityCards(heroTextView,villainTextView, community ,  drawableCommunity,
                   answerImages,   answerButtons);
            }
        }, 2000);
    }   //calls setImageAnswers
    public void setCommunityCards(final TextView heroTextView, final TextView villainTextView,ImageView[] community , Drawable[] drawableCommunity,
                                  final Drawable[] answerImages, final ImageButton[] answerButtons){
        //draw the community cards


        AlphaAnimation anim = new AlphaAnimation(0.0f,1.0f);
        anim.setDuration(1000);

        heroTextView.setVisibility(View.VISIBLE);
        villainTextView.setVisibility(View.VISIBLE);
        for(int i=0; i<9; i++){
            community[i].setVisibility(View.VISIBLE);
        }
        heroTextView.startAnimation(anim);
        villainTextView.startAnimation(anim);
        for(int i=0; i<9; i++){
            community[i].setImageDrawable(drawableCommunity[i]);
            community[i].startAnimation(anim);
        }



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                setImageAnswers(answerImages, answerButtons);
            }
        }, 1500);
    }//calls setImageAnswer()
    public void setImageAnswers(Drawable[] answerImages, ImageButton[] answerButtons){
        AlphaAnimation anim = new AlphaAnimation(0.0f,1.0f);
        anim.setDuration(1000);
        createTimer();
        for(int i=0; i<4; i++){

            answerButtons[i].setImageDrawable(answerImages[i]);
            answerButtons[i].startAnimation(anim);
        }

    }
    public static int dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

}