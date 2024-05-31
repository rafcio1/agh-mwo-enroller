package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}
	public Participant findByLogin(String login) {
		return connector.getSession().get(Participant.class, login);

	}
	public void addParticipant(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();

	}

	public void removeParticipant(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();
	}

	public void updateParticipant(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().update(participant);
		transaction.commit();
	}

	public void sortParticipant(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().update(participant);
		transaction.commit();
	}
}
