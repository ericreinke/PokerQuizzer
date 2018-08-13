package com.example.ericr.pokerquizzer;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PracticeActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        //scale drawables down to size
        Drawable d1 = getResources().getDrawable(R.drawable.c2);
        Drawable d2 = getResources().getDrawable(R.drawable.d2);
        Bitmap bm1 = ((BitmapDrawable)d1).getBitmap();
        Bitmap bm2 = ((BitmapDrawable)d2).getBitmap();
        Drawable card1 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bm1, 140, 210, true));
        Drawable card2 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bm2, 140, 210, true));

        Drawable[] layers = new Drawable[]{card1, card2};
        LayerDrawable ld= new LayerDrawable(layers);
        ld.setLayerGravity(0, Gravity.LEFT);
        ld.setLayerGravity(1, Gravity.LEFT);
        ld.setLayerInset(0,0,0,0,0);
        ld.setLayerInset(1,50,0,0,0);

        ImageButton b= findViewById(R.id.imageButton);
        Bitmap bm=Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(48);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/aquariusno8cg.ttf");
        p.setTypeface(font);
        c.drawText("1e2e3e4e5e6e7e8e9e",0,224,p);
        Drawable testPaint = new BitmapDrawable(getResources(),bm);
        b.setImageDrawable(ld); //interchange between testpaint and ld


    }

}
