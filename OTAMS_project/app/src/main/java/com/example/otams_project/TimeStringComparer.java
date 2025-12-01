package com.example.otams_project;

import android.util.Log;

import java.util.Calendar;

public class TimeStringComparer {
    public static boolean isNumHoursAhead(String eventDate, String eventTime,int hours) {
        String[] sessionDate = eventDate.split("-");
        int eventYear = Integer.parseInt(sessionDate[0]);
        int eventMonth = Integer.parseInt(sessionDate[1]);
        int eventDay = Integer.parseInt(sessionDate[2]);

        String[] sessionStartTime = eventTime.split(":");
        int eventHour = Integer.parseInt(sessionStartTime[0]);
        int eventMinute = Integer.parseInt(sessionStartTime[1]);

        Calendar sessionCalendar = Calendar.getInstance();
        sessionCalendar.set(eventYear, eventMonth - 1, eventDay, eventHour, eventMinute);
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.add(Calendar.HOUR_OF_DAY, hours);


        return todayCalendar.before(sessionCalendar);
    }

    public static boolean timeOverlap(String date1, String date2, String start1, String end1, String start2, String end2) {
        if (!date1.equals(date2)) {
            Log.d("overlap", "No overlap, different dates");
            return false;
        }

        String[] startTime1 = start1.split(":");
        int startTime1Hour = Integer.parseInt(startTime1[0]);
        int startTime1Minute = Integer.parseInt(startTime1[1]);

        String[] endTime1 = end1.split(":");
        int endTime1Hour = Integer.parseInt(endTime1[0]);
        int endTime1Minute = Integer.parseInt(endTime1[1]);

        String[] startTime2 = start2.split(":");
        int startTime2Hour = Integer.parseInt(startTime2[0]);
        int startTime2Minute = Integer.parseInt(startTime2[1]);

        String[] endTime2 = end2.split(":");
        int endTime2Hour = Integer.parseInt(endTime2[0]);
        int endTime2Minute = Integer.parseInt(endTime2[1]);

        int startTime1TotalMinute = startTime1Hour * 60 + startTime1Minute;
        int startTime2TotalMinute = startTime2Hour * 60 + startTime2Minute;
        int endTime1TotalMinute = endTime1Hour * 60 + endTime1Minute;
        int endTime2TotalMinute = endTime2Hour * 60 + endTime2Minute;

        return startTime1TotalMinute < endTime2TotalMinute && startTime2TotalMinute < endTime1TotalMinute;
    }
}
