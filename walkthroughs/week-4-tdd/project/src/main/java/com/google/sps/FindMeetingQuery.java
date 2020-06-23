// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.lang.Math;

public final class FindMeetingQuery {

  private ArrayList<TimeRange> meetings = new ArrayList<TimeRange>();
  private ArrayList<TimeRange> timeTable = new ArrayList<TimeRange>();
  private int meetingDuration;
  private Collection<String> attendees;

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    meetingDuration = (int) request.getDuration();
    attendees = request.getAttendees();
    
    // Edge Cases
    // 1. when requested duration exceeds one day 
    // 2. there are no attendees requested to the meeting
    if (request.getDuration() > (60*24)) {
        return Arrays.asList();
    } else if (request.getAttendees().size() == 0) {
        return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    constructTimeTable(events);

    // when there are no timeslots where attendees need to attend events
    if (timeTable.size() == 0) {
        return Arrays.asList(TimeRange.WHOLE_DAY);
    }
    
    findMeetingSlots(timeTable);

    return meetings;
  }
  
  // aggregates a timetable based on the schedule of attendees involved 
  public void constructTimeTable(Collection<Event> events) {

    // adds up event timeslots where requested attendees are involved in 
    // timeslots are saved in timetable
    for (Event event : events) {
        Collection<String> eventAttendees = event.getAttendees();
        if (!Collections.disjoint(attendees, eventAttendees)) {
            timeTable.add(event.getWhen());
        }
    }

    // overlapping event timeslots are combined to one slot
    Collections.sort(timeTable, TimeRange.ORDER_BY_START);
    int i = 0;
    while (i < (timeTable.size()-1)) {
        if (timeTable.get(i).overlaps(timeTable.get(i+1))) {
            int start = Math.min(timeTable.get(i).start(), timeTable.get(i+1).start());
            int end = Math.max(timeTable.get(i).end(), timeTable.get(i+1).end());
            TimeRange newTimeSlot = TimeRange.fromStartEnd(start, end, false);
            timeTable.remove(i+1);
            timeTable.remove(i);
            timeTable.add(i, newTimeSlot);
        };
        i++;
    }
  }

  // find available meeting timeslots based on the timetable of attendees
  public void findMeetingSlots(ArrayList<TimeRange> timeTable) {
    int firstStartTime = timeTable.get(0).start();
    if (firstStartTime != 0) {
        int start = TimeRange.START_OF_DAY;
        int end = firstStartTime;
        addMeetingSlot(start, end, false);
    }
    if (timeTable.size() > 1) {
        for (int i=0; i < timeTable.size()-1; i++) {
            int start = timeTable.get(i).end();
            int end = timeTable.get(i+1).start();
            addMeetingSlot(start, end, false);
        }
    }
    int lastEndTime = timeTable.get(timeTable.size()-1).end();
    if (lastEndTime != 60*24) {
        int start = lastEndTime;
        int end = TimeRange.END_OF_DAY;
        addMeetingSlot(start, end, true);
    }
    return;
  }

  // adds a meeting slot if the timeslot meets requirements
  public void addMeetingSlot(int start, int end, boolean inclusive) {
    if (end - start >= meetingDuration) {
        TimeRange timeslot = TimeRange.fromStartEnd(start, end, inclusive);
            meetings.add(timeslot); 
    }
    return;
  }
}
