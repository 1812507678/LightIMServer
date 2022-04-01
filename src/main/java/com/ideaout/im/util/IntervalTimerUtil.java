package com.ideaout.im.util;

import java.util.Timer;
import java.util.TimerTask;

public class IntervalTimerUtil {

    public static void startTimer(long delay, long period, final OnTimeChangeListener onTimeChangeListener) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (onTimeChangeListener!=null){
                    onTimeChangeListener.onTomeChange();
                }
            }
        };
        Timer mTimer = new Timer();
        mTimer.schedule(task, delay, period);
    }

    public interface OnTimeChangeListener{
        void onTomeChange();
    }
}