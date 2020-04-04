package ru.katagarov.traningintuition;

import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class IntervalTimer {
    private int maxCounter;
    private int intervalSeconds;
    private int currentCounter = maxCounter;
    private Handler activityHandler;
    private ExecutorService intervalThreadPool;

    IntervalTimer(int intervalSeconds, int maxCounter, Handler activityHandler) {
        this.intervalSeconds = intervalSeconds;
        this.maxCounter = maxCounter;
        this.activityHandler = activityHandler;
        intervalThreadPool = Executors.newFixedThreadPool(1);
    }

    void start(Runnable uiNextTick, Runnable uiCancelTimer) {
        intervalThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                new Thread(nextTick(uiNextTick, uiCancelTimer)).start();
            }
        });
    }

    private Runnable nextTick(Runnable uiNextTick, Runnable uiCancelTimer) {
        return new Runnable() {
            @Override
            public void run() {
                if (currentCounter > 0) {
                    --currentCounter;
                    activityHandler.post(uiNextTick);
                } else {
                    activityHandler.post(uiCancelTimer);
                    resetCounter();
                }

                try {
                    Thread.sleep(1000 * intervalSeconds);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                intervalThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        start(uiNextTick, uiCancelTimer);
                    }
                });
            }
        };
    }

    int getCurrentCounter() {
        return currentCounter;
    }

    void resetCounter() {
        currentCounter = maxCounter;
    }
}
