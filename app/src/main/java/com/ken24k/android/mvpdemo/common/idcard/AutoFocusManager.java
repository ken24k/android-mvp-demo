package com.ken24k.android.mvpdemo.common.idcard;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.RejectedExecutionException;

/**
 * 自动对焦，定时对焦方式实现
 * Created by wangming on 2020-05-28
 */

public class AutoFocusManager implements Camera.AutoFocusCallback {

    private static final String TAG = AutoFocusManager.class.getSimpleName();

    private static final long AUTO_FOCUS_INTERVAL_MS = 2000L;
    private static final Collection<String> FOCUS_MODES_CALLING_AF;

    static {
        FOCUS_MODES_CALLING_AF = new ArrayList<>(2);
        FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_AUTO);
        FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_MACRO);
    }

    private final boolean useAutoFocus;
    private final Camera camera;
    private boolean stopped;
    private boolean focusing;
    private AsyncTask<?, ?, ?> outstandingTask;

    public AutoFocusManager(Camera camera) {
        this.camera = camera;
        String currentFocusMode = camera.getParameters().getFocusMode();
        useAutoFocus = FOCUS_MODES_CALLING_AF.contains(currentFocusMode);
        start();
    }

    @Override
    public synchronized void onAutoFocus(boolean success, Camera theCamera) {
        focusing = false;
        autoFocusAgainLater();
    }

    @SuppressLint("NewApi")
    private synchronized void autoFocusAgainLater() {
        if (!stopped && outstandingTask == null) {
            AutoFocusTask newTask = new AutoFocusTask();
            try {
                if (Build.VERSION.SDK_INT >= 11) {
                    newTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    newTask.execute();
                }
                outstandingTask = newTask;
            } catch (RejectedExecutionException ree) {
                Log.w(TAG, "Could not request auto focus", ree);
            }
        }
    }

    public synchronized void start() {
        if (useAutoFocus) {
            outstandingTask = null;
            if (!stopped && !focusing) {
                try {
                    camera.autoFocus(this);
                    Log.w(TAG, "自动对焦");
                    focusing = true;
                } catch (RuntimeException re) {
                    Log.w(TAG, "Unexpected exception while focusing", re);
                    autoFocusAgainLater();
                }
            }
        }
    }

    private synchronized void cancelOutstandingTask() {
        if (outstandingTask != null) {
            if (outstandingTask.getStatus() != AsyncTask.Status.FINISHED) {
                outstandingTask.cancel(true);
            }
            outstandingTask = null;
        }
    }

    public synchronized void stop() {
        stopped = true;
        if (useAutoFocus) {
            cancelOutstandingTask();
            try {
                camera.cancelAutoFocus();
            } catch (RuntimeException re) {
                Log.w(TAG, "Unexpected exception while cancelling focusing", re);
            }
        }
    }

    private final class AutoFocusTask extends AsyncTask<Object, Object, Object> {
        @Override
        protected Object doInBackground(Object... voids) {
            try {
                Thread.sleep(AUTO_FOCUS_INTERVAL_MS);
            } catch (InterruptedException e) {
            }
            start();
            return null;
        }
    }

}
