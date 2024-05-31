package com.company.enroller.controllers;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<Collection<Participant>> getParticipants(
			@RequestParam(value ="sortBy", defaultValue="no-sort") String sortBy,
			@RequestParam(value="sortOrder", defaultValue="ASC") String sortOrder) {

		Collection<Participant> participants = participantService.getAll();

		if (sortBy.equals("login")) {
			if (sortOrder.equalsIgnoreCase("DESC")) {
				participants = participants.stream()
						.sorted(Comparator.comparing(Participant::getLogin).reversed())
						.collect(Collectors.toList());
			} else {
				participants = participants.stream()
						.sorted(Comparator.comparing(Participant::getLogin))
						.collect(Collectors.toList());
			}
		}

		return new ResponseEntity<>(participants, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Participant> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<String> registerParticipant(@RequestBody Participant participant) {
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
		if (foundParticipant != null) {
			return new ResponseEntity<>("Unable to create. A participant with login " + participant.getLogin() + " already exists.", HttpStatus.CONFLICT);
		}
		participantService.addParticipant(participant);
		return new ResponseEntity<>(participant.getLogin() + " created successfully.", HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		participantService.removeParticipant(participant);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateParticipant(@PathVariable("id") String login, @RequestBody Participant participant) {
		Participant foundParticipant = participantService.findByLogin(login);
		if (foundParticipant == null) {
			return new ResponseEntity<>("Participant with login " + login + " not found.", HttpStatus.NOT_FOUND);
		}

		participantService.updateParticipant(participant);
		return new ResponseEntity<>(participant.getLogin() + " updated successfully.", HttpStatus.OK);
	}
}