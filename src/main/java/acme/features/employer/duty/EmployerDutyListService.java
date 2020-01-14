
package acme.features.employer.duty;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.duties.Duty;
import acme.entities.jobs.Job;
import acme.entities.roles.Employer;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractListService;

@Service
public class EmployerDutyListService implements AbstractListService<Employer, Duty> {

	// Internal state ---------------------------------------------------------

	@Autowired
	EmployerDutyRepository repository;

	// AbstractListService<Employer, Duty> interface --------------


	//An employer principal can not list the duties of a not finalMode job from another employer
	@Override
	public boolean authorise(final Request<Duty> request) {
		boolean result;
		int jobId;
		Job job;
		Employer employer;
		Principal principal;

		Calendar c = new GregorianCalendar();
		Date d = c.getTime();

		jobId = request.getModel().getInteger("idJob");
		job = this.repository.findOneJobById(jobId);
		boolean elapsedDeadline = job.getDeadline().before(d);
		employer = job.getEmployer();
		principal = request.getPrincipal();
		result = job.isFinalMode() && !elapsedDeadline || job.isFinalMode() && elapsedDeadline && employer.getUserAccount().getId() == principal.getAccountId() || !job.isFinalMode() && employer.getUserAccount().getId() == principal.getAccountId();
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

		int id = request.getModel().getInteger("idJob");
		result = this.repository.findManyDutiesByJobId(id);
		return result;

	}

}
