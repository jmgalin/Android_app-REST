package com.dam.musclegym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class StartWorkAct extends AppCompatActivity {

    TextView intropage, subintropage, fitonetitle, fitonedesc, timerValue, btnexercise, btnexercise2;
    View divpage, bgprogress, bgprogress2;
    LinearLayout fitone;
    ImageView imgTimer;

    private static final long START_TIME_IN_MILLIS = 51000;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    Animation btthree, bttfour, ttbone, ttbtwo, alphago;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_work);

        //Carga las animaciones
        btthree = AnimationUtils.loadAnimation(this, R.anim.btthree);
        bttfour = AnimationUtils.loadAnimation(this, R.anim.bttfour);
        ttbone = AnimationUtils.loadAnimation(this, R.anim.ttbone);
        ttbtwo = AnimationUtils.loadAnimation(this, R.anim.ttbtwo);
        alphago = AnimationUtils.loadAnimation(this, R.anim.alphago);

        intropage = (TextView) findViewById(R.id.intropage);
        subintropage = (TextView) findViewById(R.id.subintropage);
        fitonetitle = (TextView) findViewById(R.id.fitonetitle);
        fitonedesc = (TextView) findViewById(R.id.fitonedesc);
        timerValue = (TextView) findViewById(R.id.timerValue);
        btnexercise = (TextView) findViewById(R.id.btnexercise);
        btnexercise2 = (TextView) findViewById(R.id.btnexercise2);

        divpage = (View) findViewById(R.id.divpage);
        bgprogress = (View) findViewById(R.id.bgprogress);
        bgprogress2 = (View) findViewById(R.id.bgprogress2);

        fitone = (LinearLayout) findViewById(R.id.fitone);

        imgTimer = (ImageView) findViewById(R.id.imgtimer);

        //Asigna las animaciones
        btnexercise.startAnimation(bttfour);
        btnexercise2.startAnimation(bttfour);
        bgprogress.startAnimation(btthree);
        bgprogress2.startAnimation(btthree);
        fitone.startAnimation(ttbone);
        intropage.startAnimation(ttbtwo);
        subintropage.startAnimation(ttbtwo);
        divpage.startAnimation(ttbtwo);
        timerValue.startAnimation(alphago);
        imgTimer.startAnimation(alphago);

        btnexercise = (TextView) findViewById(R.id.btnexercise);
        btnexercise.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartWorkAct.this,WorkoutAct.class));
            }
        });

        startTimer();

        btnexercise2 = (TextView) findViewById(R.id.btnexercise2);
        btnexercise2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                mTimerRunning = false;
                mTimeLeftInMillis = START_TIME_IN_MILLIS;
                updateCountDownText();
                startTimer();
            }
        });


    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                startTimer();
            }
        }.start();
        mTimerRunning = true;
    }


    private void updateCountDownText(){
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeft = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds) ;
        timerValue.setText(timeLeft);
    }

}