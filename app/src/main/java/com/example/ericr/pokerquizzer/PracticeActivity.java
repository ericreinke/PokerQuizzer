package com.example.ericr.pokerquizzer;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

public class PracticeActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        ImageButton b= findViewById(R.id.imageButton);
        //scale drawables down to size
        Drawable d1 = getResources().getDrawable(R.drawable.c2s);
        Drawable d2 = getResources().getDrawable(R.drawable.d2s);
        Bitmap bm1 = ((BitmapDrawable)d1).getBitmap();
        Bitmap bm2 = ((BitmapDrawable)d2).getBitmap();
        Drawable card1 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bm1, 55, 78, true));
        Drawable card2 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bm2, 55, 78, true));

        Drawable[] layers = new Drawable[]{card1, card2};
        LayerDrawable ld= new LayerDrawable(layers);
        ld.setLayerInset(0,0,0,0,0);
        ld.setLayerInset(1,25,0,0,0);
        ld.setLayerInset(2,25,0,0,0);
        ld.setLayerInset(3,25,0,0,0);
        b.setImageDrawable(ld);
    }

}
