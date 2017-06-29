package br.com.sms.springSms.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.sms.springSms.models.PaginatedList;
import br.com.sms.springSms.models.Sms;

@Repository
public class SmsDao {

	@PersistenceContext
	private EntityManager manager;

	public List<Sms> all() {
		return manager.createQuery("select s from Sms s", Sms.class).getResultList();
	}

	@Transactional
	public void save(Sms sms) {

		try {
			TypedQuery<Sms> query = manager.createQuery("select s from Sms s WHERE s.number = :number AND s.text = :text", Sms.class);
			query.setParameter("number", sms.getNumber());
			query.setParameter("text", sms.getText());
			if (!(query.getResultList().size() > 0)) {
				manager.persist(sms);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Sms findById(Integer id) {
		return manager.find(Sms.class, id);
	}

	public void remove(Sms sms) {
		manager.remove(sms);
	}

	public void update(Sms sms) {
		manager.merge(sms);
	}

	public PaginatedList paginated(int page, int max) {
		return new PaginatorQueryHelper().list(manager, Sms.class, page, max);
	}

}
