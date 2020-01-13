
package acme.features.authenticated.duty;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.duties.Duty;
import acme.entities.jobs.Job;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractListService;

@Service
public class AuthenticatedDutyListService implements AbstractListService<Authenticated, Duty> {

	// Internal state ---------------------------------------------------------

	@Autowired
	AuthenticatedDutyRepository repository;

	// AbstractListService<Authenticated, Duty> interface --------------


	//An authenticated principal can not list the duties of not finalMode jobs. Neither to elapsed ones.
	@Override
	public boolean authorise(final Request<Duty> request) {
		assert request != null;

		boolean result;
		int idJob;
		Job job;

		Calendar c = new GregorianCalendar();
		Date d = c.getTime();

		idJob = request.getModel().getInteger("idJob");
		job = this.repository.findOneJobById(idJob);
		boolean elapsedDeadline = job.getDeadline().before(d);
		result = job.isFinalMode() == true && !elapsedDeadline;
		return result;
	}

	@Override
	public void unbind(final Request<Duty> request, final Duty entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title");
	}

	@Override
	public Collection<Duty> findMany(final Request<Duty> request) {
		assert request != null;

		Collection<Duty> result;

		int idJob = request.getModel().getInteger("idJob");
		result = this.repository.findManyDutiesByJobId(idJob);
		return result;

	}

}
