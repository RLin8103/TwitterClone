package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.BaseService;

public abstract class BackGroundHandler<T extends BaseService.BaseObserver> extends Handler {

    protected T observer;
    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";

    public BackGroundHandler(T observer) {
        super(Looper.getMainLooper());
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if (msg.getData().getBoolean(SUCCESS_KEY)) {
            handleSuccess(msg);
        } else if (msg.getData().containsKey(MESSAGE_KEY)) {
            String message = msg.getData().getString(MESSAGE_KEY);
            observer.displayError(getMessage(message));
        } else if (msg.getData().containsKey(EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(EXCEPTION_KEY);
            observer.displayException(ex);
        }
        finalizeHandling(msg);
    }

    // Template methods to be overridden by concrete handler classes
    protected abstract void handleSuccess(Message msg);

    protected abstract String getMessage(String message);

    protected abstract void finalizeHandling(Message msg);
}

