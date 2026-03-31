package com.example.eecs4443project;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReminderWorker extends Worker {

    public ReminderWorker(@NonNull Context context,
                          @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String title = getInputData().getString("title");
        String message = getInputData().getString("message");

        int notificationId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);

        NotificationHelper.showNotification(
                getApplicationContext(),
                title,
                message,
                notificationId
        );

        return Result.success();
    }
}