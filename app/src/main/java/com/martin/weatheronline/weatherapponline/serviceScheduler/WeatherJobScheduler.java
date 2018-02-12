package com.martin.weatheronline.weatherapponline.serviceScheduler;

import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import static com.martin.weatheronline.weatherapponline.serviceScheduler.DownloadWeatherService.SERVICE_TAG;

public class WeatherJobScheduler {
    private final int SCHEDULE_PERIOD = 3 * 60 * 60;
    private final int INTERVAL = 60 * 60;
    Context context;
    FirebaseJobDispatcher dispatcher;

    public WeatherJobScheduler(Context context) {
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
    }

    public void startSchedule() {
        Job job = createJob(dispatcher);
        dispatcher.schedule(job);
        Log.d(SERVICE_TAG, "startSchedule");
    }

    private Job createJob(FirebaseJobDispatcher dispatcher) {
        Job job = dispatcher.newJobBuilder()
                .setLifetime(Lifetime.FOREVER)
                .setService(DownloadWeatherService.class)
                .setTag("dispatcher_unique_tag")
                .setReplaceCurrent(false)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SCHEDULE_PERIOD, SCHEDULE_PERIOD + INTERVAL))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build();
        Log.d(SERVICE_TAG, "createJob");
        return job;

    }
}
