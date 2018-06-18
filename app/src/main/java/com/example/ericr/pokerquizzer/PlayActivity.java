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
        String[] questions = res.getStringArray(R.array.question_array);
        String[] questionOneAnswers = res.getStringArray(R.array.questionOneAnswers);
        String[] questionTwoAnswers = res.getStringArray(R.array.questionTwoAnswers);
        String[] questionThreeAnswers = res.getStringArray(R.array.questionThreeAnswers);

        TextView questionTextView = findViewById(R.id.questionTextView);
        Button firstAnswerBtn = (Button)findViewById(R.id.firstAnswerBtn);
        Button secondAnswerBtn = (Button)findViewById(R.id.secondAnswerBtn);
        Button thirdAnswerBtn = (Button) findViewById(R.id.thirdAnswerBtn);
        Button fourthAnswerBtn = (Button) findViewById(R.id.fourthAnswerBtn);

        questionTextView.setText(questions[randomQuestion]);
        if(randomQuestion==1){
            firstAnswerBtn.setText(questionOneAnswers[0]);
            secondAnswerBtn.setText(questionOneAnswers[2]);
            thirdAnswerBtn.setText(questionOneAnswers[3]);
            fourthAnswerBtn.setText(questionOneAnswers[4]);
        }
        if(randomQuestion==2){
            firstAnswerBtn.setText(questionTwoAnswers[0]);
            secondAnswerBtn.setText(questionTwoAnswers[2]);
            thirdAnswerBtn.setText(questionTwoAnswers[3]);
            fourthAnswerBtn.setText(questionTwoAnswers[4]);
        }
        if(randomQuestion==3){
            firstAnswerBtn.setText(questionThreeAnswers[0]);
            secondAnswerBtn.setText(questionThreeAnswers[2]);
            thirdAnswerBtn.setText(questionThreeAnswers[3]);
            fourthAnswerBtn.setText(questionThreeAnswers[4]);
        }


    }
}
