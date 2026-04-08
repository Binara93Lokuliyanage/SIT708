package com.example.planmate.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.planmate.data.Event;
import com.example.planmate.data.EventDao;
import com.example.planmate.data.EventDatabase;

import java.util.List;
import java.util.concurrent.Executors;

public class EventRepository {

    private EventDao eventDao;
    private LiveData<List<Event>> allEvents;

    public EventRepository(Application application) {
        EventDatabase db = EventDatabase.getDatabase(application);
        eventDao = db.eventDao();
        allEvents = eventDao.getAllEvents();
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    public void insert(Event event) {
        Executors.newSingleThreadExecutor().execute(() -> eventDao.insert(event));
    }

    public void update(Event event) {
        Executors.newSingleThreadExecutor().execute(() -> eventDao.update(event));
    }

    public void delete(Event event) {
        Executors.newSingleThreadExecutor().execute(() -> eventDao.delete(event));
    }
}
