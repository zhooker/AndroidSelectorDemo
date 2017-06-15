package com.example.zhuangsj.myapplication;

import android.animation.ObjectAnimator;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


/*        final RotateDrawable rotateDrawable = (RotateDrawable)findViewById(R.id.image).getBackground();
        ObjectAnimator.ofInt(rotateDrawable, "level", 0, 10000).start();*/
    }


}

