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

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    
    int meetingDuration = (int) request.getDuration();
    Collection<String> attendees = request.getAttendees();
    
    // Edge Cases
    // 1. when requested duration exceeds one day 
    // 2. there are no attendees requested to the meeting
    if (meetingDuration > (60*24)) {
        return Arrays.asList();
    } else if (attendees.size() == 0) {
        return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    List<TimeRange> timeTable = constructTimeTable(events, attendees);

    // when there are no timeslots where attendees need to attend events
    if (timeTable.size() == 0) {
        return Arrays.asList(TimeRange.WHOLE_DAY);
    }
    
    List<TimeRange> meetings= findMeetingSlots(timeTable, meetingDuration);

    return meetings;
  }
  
  // aggregates a timetable based on the schedule of attendees involved 
  private static List<TimeRange> constructTimeTable(Collection<Event> events, Collection<String> attendees) {
    
    List<TimeRange> timeTable = new ArrayList<TimeRange>();

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

    return timeTable;
  }

  // find available meeting timeslots based on the timetable of attendees
  private static List<TimeRange> findMeetingSlots(List<TimeRange> timeTable, int meetingDuration) {
    
    List<TimeRange> meetings = new ArrayList<TimeRange>();
    int firstStartTime = timeTable.get(0).start();

    if (firstStartTime != 0) {
        int start = TimeRange.START_OF_DAY;
        int end = firstStartTime;
        addMeetingSlot(meetings, start, end, false, meetingDuration);
    }
    if (timeTable.size() > 1) {
        for (int i=0; i < timeTable.size()-1; i++) {
            int start = timeTable.get(i).end();
            int end = timeTable.get(i+1).start();
            addMeetingSlot(meetings, start, end, false, meetingDuration);
        }
    }
    int lastEndTime = timeTable.get(timeTable.size()-1).end();
    if (lastEndTime != 60*24) {
        int start = lastEndTime;
        int end = TimeRange.END_OF_DAY;
        addMeetingSlot(meetings, start, end, true, meetingDuration);
    }
    return meetings;
  }

  // adds a meeting slot if the timeslot meets requirements
  private static void addMeetingSlot(List<TimeRange> meetings, int start, int end, boolean inclusive, int meetingDuration) {
    if (end - start >= meetingDuration) {
        TimeRange timeslot = TimeRange.fromStartEnd(start, end, inclusive);
            meetings.add(timeslot); 
    }
    return;
  }
}
