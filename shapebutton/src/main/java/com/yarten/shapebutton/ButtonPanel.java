package com.yarten.shapebutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

/**
 * Created by yfic on 2017/12/26.
 */

public class ButtonPanel extends ConstraintLayout
{
    //region 构造器
    ButtonPanel(Context context)
    {
        this(context, null);
    }

    ButtonPanel(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    ButtonPanel(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.button_panel, this);

        initView();
    }

    private ShapeButton left, right, top, bottom, middle;
    private ShapeButton leftTop, leftMiddle, leftBottom, rightTop, rightMiddle, rightBottom;
    private ShapeButton[] buttons;
    private FrameLayout triangles, circles;
    private AnimatorSet animator, antiAnimator;

    private void initView()
    {
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        top = findViewById(R.id.top);
        bottom = findViewById(R.id.bottom);
        middle = findViewById(R.id.middle_circle);

        leftTop = findViewById(R.id.left_top);
        leftMiddle = findViewById(R.id.left_middle);
        leftBottom = findViewById(R.id.left_bottom);
        rightTop = findViewById(R.id.right_top);
        rightMiddle = findViewById(R.id.right_middle);
        rightBottom = findViewById(R.id.right_bottom);

        buttons = new ShapeButton[]{
                middle, top, right, bottom, left, leftTop, rightTop, rightMiddle, rightBottom, leftBottom, leftMiddle
        };

        triangles = findViewById(R.id.four_triangles);
        circles = findViewById(R.id.six_circles);

        triangles.setVisibility(View.VISIBLE);
    //    triangles.setRotation(-90);
        circles.setVisibility(View.GONE);
    //    circles.setRotation(0);

        animator = new AnimatorSet();
        animator.setInterpolator(new OvershootInterpolator());
        animator.setDuration(1000);
        animator
                .play(ObjectAnimator.ofFloat(triangles, "rotation", -90, 0))
                .with(ObjectAnimator.ofFloat(circles, "rotation", 0, -90))
                .with(ObjectAnimator.ofFloat(triangles, "alpha", 0, 1))
                .with(ObjectAnimator.ofFloat(circles, "alpha", 1, 0));

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                circles.setVisibility(View.VISIBLE);
                triangles.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                circles.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        antiAnimator = new AnimatorSet();
        antiAnimator.setInterpolator(new OvershootInterpolator());
        antiAnimator.setDuration(1000);
        antiAnimator
                .play(ObjectAnimator.ofFloat(triangles, "rotation", 0, -90))
                .with(ObjectAnimator.ofFloat(circles, "rotation", -90, 0))
                .with(ObjectAnimator.ofFloat(triangles, "alpha", 1, 0))
                .with(ObjectAnimator.ofFloat(circles, "alpha", 0, 1));

        antiAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                circles.setVisibility(View.VISIBLE);
                triangles.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                triangles.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    //endregion

    private boolean isReverse = false;

    public void toggle()
    {
        if(isReverse)
        {
            animator.cancel();
            antiAnimator.start();
        }
        else
        {
            antiAnimator.cancel();
            animator.start();
        }

        isReverse = !isReverse;
    }

    public void toggle(boolean isReverse)
    {
        this.isReverse = isReverse;
    }

    public ShapeButton getLeftButton(){return left;}
    public ShapeButton getRightButton(){return right;}
    public ShapeButton getTopButton(){return top;}
    public ShapeButton getBottomButton(){return bottom;}
    public ShapeButton getLeftTopButton(){return leftTop;}
    public ShapeButton getLeftMiddleButton(){return leftMiddle;}
    public ShapeButton getLeftBottomButton(){return leftBottom;}
    public ShapeButton getRightTopButton(){return rightTop;}
    public ShapeButton getRightMiddleButton(){return rightMiddle;}
    public ShapeButton getRightBottomButton(){return rightBottom;}
    public ShapeButton getMiddleButton(){return middle;}
}
