package com.example.ericr.pokerquizzer;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;
import java.util.Collections;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Random random = new Random();
        int randomQuestion = random.nextInt(3);
        Resources res = getResources();

//declare question array and answer array
        TypedArray answerResources = res.obtainTypedArray(R.array.answers);
        int resId = answerResources.getResourceId(randomQuestion, -1);//gets the ID of the nth string array
        answerResources.recycle();//free
        //if (resId < 0) {QUESTION DOES NOT EXIST.  CHECK strings.xml OR RNG}
        String [] questionAnswers = res.getStringArray(resId);
        String[] questions = res.getStringArray(R.array.question_array);

//declare buttons and textview
        TextView questionTextView = findViewById(R.id.questionTextView);
        Button firstAnswerBtn = findViewById(R.id.firstAnswerBtn);
        Button secondAnswerBtn = findViewById(R.id.secondAnswerBtn);
        Button thirdAnswerBtn = findViewById(R.id.thirdAnswerBtn);
        Button fourthAnswerBtn = findViewById(R.id.fourthAnswerBtn);

//set the question and the buttons to the randomQuestion
        questionTextView.setText(questions[randomQuestion]);
        firstAnswerBtn.setText(questionAnswers[0]);
        secondAnswerBtn.setText(questionAnswers[1]);
        thirdAnswerBtn.setText(questionAnswers[2]);
        fourthAnswerBtn.setText(questionAnswers[3]);

    }
}
