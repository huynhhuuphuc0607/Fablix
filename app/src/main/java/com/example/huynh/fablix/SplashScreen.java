package com.example.huynh.fablix;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {
    private String genres[] = new String[]{"Action", "Thriller","Comedy","See them all on Fablix"};
    private TextView splashTextView;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashTextView = findViewById(R.id.splashTextView);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Handler handler = new Handler();
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        splashTextView.setText(genres[i]);
                        splashTextView.setVisibility(View.VISIBLE);
                        splashTextView.startAnimation(AnimationUtils.loadAnimation(SplashScreen.this, R.anim.pop_up));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (i < genres.length-1) {
                                    splashTextView.startAnimation(AnimationUtils.loadAnimation(SplashScreen.this, R.anim.disappear));
                                    splashTextView.setVisibility(View.INVISIBLE);
                                    i++;
                                }else {
                                    timer.cancel();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                                            SplashScreen.this.finish();
                                        }
                                    },300);
                                }
                            }
                        }, 1000);
                    }
                });
            }
        }, 0, 1750);
    }
}
