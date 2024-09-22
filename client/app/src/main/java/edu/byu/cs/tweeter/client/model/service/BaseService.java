package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.backgroundTask.BackGroundTask;

public abstract class BaseService<T extends BackGroundTask> {

    protected void executeTask(T task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }

    public interface BaseObserver {
        void displayError(String message);
        void displayException(Exception ex);
    }
}

