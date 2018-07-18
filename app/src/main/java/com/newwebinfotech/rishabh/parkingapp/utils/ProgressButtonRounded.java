package com.newwebinfotech.rishabh.parkingapp.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.newwebinfotech.rishabh.parkingapp.R;


/**
 * Created by Admin on 8/28/2017.
 */

public class ProgressButtonRounded {

    private final ProgressBar pBar;
    private final Button btnAction;
    private final Animation inAnim,outAnim;
    private final ImageView ivStatus;

    public static ProgressButtonRounded newInstance(Context context, ImageView ivStatus, ProgressBar pBar, Button btnMain) {
        ProgressButtonRounded btn = new ProgressButtonRounded(context, ivStatus, pBar,  btnMain);
        return btn;
    }

    public ProgressButtonRounded(Context context, ImageView ivStatus, ProgressBar pBar, Button btnMain) {
        this.ivStatus = ivStatus;
        this.pBar =pBar;
        this.btnAction = btnMain;
        inAnim = AnimationUtils.loadAnimation(context,
                android.R.anim.fade_in);
        outAnim = AnimationUtils.loadAnimation(context,
                android.R.anim.fade_out);

    }


    public void setOnClickListener(View.OnClickListener click) {
        if (btnAction != null)
            btnAction.setOnClickListener(click);

    }

    public void startAnimation() {
        hideButtonAction();

    }

    public void revertAnimation() {
        showButtonAction();
    }

    public void revertSuccessAnimation() {
        revertSuccessAnimation(false);
    }
    public void revertSuccessAnimation(Boolean btnVisibility) {
        hideProgressBar();
        if(btnVisibility) {
            showButtonAction();
        }else{
            showSuccessView();
        }
    }

    private void showSuccessView() {
        if(ivStatus!=null) {
            ivStatus.startAnimation(inAnim);
            ivStatus.setVisibility(View.VISIBLE);
        }
    }

    private void showButtonAction() {
        if (btnAction != null) {
            ObjectAnimator transAnimation2= ObjectAnimator.ofFloat(btnAction, "ScaleX", 0, 1);
            transAnimation2.setDuration(500);//set duration
            final Animator.AnimatorListener showListener=new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    hideProgressBar();
                    btnAction.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    btnAction.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            };
            hideProgressBar();
            btnAction.setVisibility(View.VISIBLE);
            transAnimation2.addListener(showListener);
            transAnimation2.start();//start animation
        }
    }

    private void hideButtonAction() {

        if (btnAction != null) {
            final Animator.AnimatorListener hideButtonListener=new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    showProgressBar();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    btnAction.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            };

            ObjectAnimator transAnimation1= ObjectAnimator.ofFloat(btnAction, "ScaleX", 1, 0);
            transAnimation1.setDuration(500);//set duration

            transAnimation1.addListener(hideButtonListener);

            transAnimation1.start();//start animation
        }
    }

    private void showProgressBar() {
        if (pBar != null) {
//            pBar.startAnimation(inAnim);
            pBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar() {
        if (pBar != null) {
//            pBar.startAnimation(outAnim);
            pBar.setVisibility(View.GONE);
        }
    }

    public void setText(String text) {
        if (btnAction != null) {
            btnAction.setText(text!=null?text:"");
        }
    }
}
