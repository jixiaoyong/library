package cf.android666.diywidget;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ToggleButton;

import cf.android666.applibrary.Logger;
import cf.android666.applibrary.ProgressButton;

/**
 * Created by jixiaoyong on 2018/2/18.
 */

public class MainActivity extends Activity {

    private ToggleButton toggleButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressButton progressButton = findViewById(R.id.progress);

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(10000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progressButton.setProgress((Float) animation.getAnimatedValue());
            }
        });
        animator.start();

        Log.d((Logger.generateTag()), "" + Logger.isLog());

    }
}
