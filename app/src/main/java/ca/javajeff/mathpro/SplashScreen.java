package ca.javajeff.mathpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import static android.R.id.list;

/**
 * Created by Саддам on 29.07.2017.
 */

public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    new CountDownTimer(5000, 250) {
                        int k=0;
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            k++;
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("k", k);
                            startActivity(intent);
                        }
                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
