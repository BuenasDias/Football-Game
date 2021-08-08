package com.example.footballgame.collection;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.footballgame.collection.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int teamOneScore = 0;
    private int teamTwoScore = 0;
    private boolean isStavka = false;
    private boolean isMinusBalans = false;
    private int userStavka = 0;
    private Animation animShake;
//    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initStartBalansText();

        binding.btnClub1.setOnClickListener(view -> {

            minusBalans();

            userStavka = 1;

            isStavka = true;
            showStavkaTeamOne();
            hideErrorText();

        });

        binding.btnClub2.setOnClickListener(view -> {

            minusBalans();

            userStavka = 2;

            isStavka = true;
            showStavkaTeamTwo();
            hideErrorText();

        });

        binding.btnStartGame.setOnClickListener(v -> {

            if (isStavka && isMinusBalans) {
                blockBtnStart();
                blockButtonsStavka();

                new CountDownTimer(8000, 1000) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTick(long l) {

                        int temp = getGoalTeam();

                        if (temp == 1) {
                            teamOneScore++;
                        } else if (temp == 2) {
                            teamTwoScore++;
                        }

                        binding.txtScore.setText(teamOneScore + " : " + teamTwoScore);
                    }

                    @Override
                    public void onFinish() {

                        unblockBtnStart();
                        unblockButtonsStavka();

                        if (teamOneScore > teamTwoScore) {

                            if (userStavka == 1) {
                                Common.BALANS += 50;
                                showErrorText("ПОБЕДИЛ REAL MADRID\nВЫ ВЫЙГРАЛИ!!!");
                                showBalans();
                                getVibration();
                            } else {
                                showErrorText("ПОБЕДИЛ REAL MADRID\nВЫ ПРОИГРАЛИ");
                            }

                        } else if (teamOneScore < teamTwoScore) {

                            if (userStavka == 2) {
                                Common.BALANS += 50;
                                showErrorText("ПОБЕДИЛА BARSELONA\nВЫ ВЫЙГРАЛИ!!!");
                                showBalans();
                                getVibration();
                            } else {
                                showErrorText("ПОБЕДИЛА BARSELONA\nВЫ ПРОИГРАЛИ");
                            }

                        } else {
                            showErrorText("НИЧЬЯ");
                        }

                        teamOneScore = 0;
                        teamTwoScore = 0;

                        isStavka = false;
                        isMinusBalans = false;
                    }
                }.start();
            } else {
                showErrorText("СДЕЛАЙТЕ СТАВКУ");
            }


        });
    }

    @SuppressLint("SetTextI18n")
    private void initStartBalansText() {
        showBalans();
    }

    private void getVibration() {
        long mills = 1000L;
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if (vibrator.hasVibrator()) {
            vibrator.vibrate(mills);
        }
    }

//    private void startMatch(){
//        mp = MediaPlayer.create(this, R.raw.music);
//        mp.start();
//    }

    private void showErrorText(String message) {
        binding.txtError.setVisibility(View.VISIBLE);
        binding.txtError.setText(message);
    }

    private void minusBalans() {
        if (!isMinusBalans) {
            if (Common.BALANS == 0) {
                showErrorText("GAME OVER");

                // Тут нужно сделать кнопку обновления игры

            } else {
                Common.BALANS -= 10;
                isMinusBalans = true;
            }
        }
        showBalans();
    }

    @SuppressLint("SetTextI18n")
    private void showBalans() {
        binding.txtBalans.setText("БАЛАНС " + Common.BALANS);
    }

    private void hideErrorText() {
        binding.txtError.setVisibility(View.GONE);
    }

    private void blockButtonsStavka() {
        binding.btnClub1.setEnabled(false);
        binding.btnClub1.setClickable(false);

        binding.btnClub2.setEnabled(false);
        binding.btnClub2.setClickable(false);
    }

    private void unblockButtonsStavka() {
        binding.btnClub1.setEnabled(true);
        binding.btnClub1.setClickable(true);

        binding.btnClub2.setEnabled(true);
        binding.btnClub2.setClickable(true);
    }

    private void initViewAnim() {
        animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
    }


    private void showStavkaTeamOne() {
        initViewAnim();

        binding.txtStavkaTeamOne.startAnimation(animShake);
        binding.txtStavkaTeamOne.setVisibility(View.VISIBLE);
        binding.txtStavkaTeamTwo.clearAnimation();
        binding.txtStavkaTeamTwo.setVisibility(View.GONE);
    }

    private void showStavkaTeamTwo() {
        initViewAnim();

        binding.txtStavkaTeamTwo.startAnimation(animShake);
        binding.txtStavkaTeamTwo.setVisibility(View.VISIBLE);
        binding.txtStavkaTeamOne.clearAnimation();
        binding.txtStavkaTeamOne.setVisibility(View.GONE);
    }


    private int getGoalTeam() {
        Random random = new Random();
        return random.nextInt(3);
    }

    private void blockBtnStart() {
        binding.btnStartGame.setEnabled(false);
        binding.btnStartGame.setClickable(false);
    }

    private void unblockBtnStart() {
        binding.btnStartGame.setEnabled(true);
        binding.btnStartGame.setClickable(true);
    }
}