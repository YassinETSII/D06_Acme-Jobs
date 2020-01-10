
package acme.features.authenticated.challenge;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.challenges.Challenge;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractListService;

@Service
public class AuthenticatedChallengeListService implements AbstractListService<Authenticated, Challenge> {

	// Internal state ---------------------------------------------------------

	@Autowired
	AuthenticatedChallengeRepository repository;


	// AbstractListService<Authenticated, Challenge> interface --------------

	@Override
	public boolean authorise(final Request<Challenge> request) {
		assert request != null;

		return true;
	}

	@Override
	public void unbind(final Request<Challenge> request, final Challenge entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "deadline", "title");
	}

	@Override
	public Collection<Challenge> findMany(final Request<Challenge> request) {
		assert request != null;

		Calendar c = new GregorianCalendar();
		Date d = c.getTime();

		Collection<Challenge> result;
		result = this.repository.findManyAll(d);

		return result;

	}

}
