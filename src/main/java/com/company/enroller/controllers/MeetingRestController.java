package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {
    @Autowired
    MeetingService meetingService;
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings(){
        Collection<Meeting> meetings=meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }
    @RequestMapping(value = "/{id}", method =RequestMethod.GET)
    public ResponseEntity<?>getMeeting(@PathVariable("id")long id){
        Collection<Meeting> meeting=meetingService.getById(id);
        if (meeting == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<Meeting>>(meeting, HttpStatus.OK);
    }
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> addMeeting(@RequestBody Meeting meeting) {
        meetingService.addMeeting(meeting);
        return new ResponseEntity<>(meeting.getTitle() + " created successfully.", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
        Meeting meeting = (Meeting) meetingService.getById(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        meetingService.removeMeeting(meeting);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting meeting) {
        Meeting existingMeeting = (Meeting) meetingService.getById(id);
        if (existingMeeting == null) {
            return new ResponseEntity<>("Meeting with id " + id + " not found.", HttpStatus.NOT_FOUND);
        }

        existingMeeting.setTitle(meeting.getTitle());
        existingMeeting.setDescription(meeting.getDescription());
        existingMeeting.setDate(meeting.getDate());

        meetingService.updateMeeting(existingMeeting);
        return new ResponseEntity<>(meeting.getTitle() + " updated successfully.", HttpStatus.OK);
    }
}

