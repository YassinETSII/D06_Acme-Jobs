
package acme.features.employer.job;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.jobs.Job;
import acme.entities.roles.Employer;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractShowService;

@Service
public class EmployerJobShowService implements AbstractShowService<Employer, Job> {

	// Internal state ---------------------------------------------------------

	@Autowired
	EmployerJobRepository repository;

	// AbstractShowService<Employer, Job> interface --------------


	//An employer principal can not have access to a not finalMode job from another employer
	@Override
	public boolean authorise(final Request<Job> request) {
		assert request != null;

		boolean result;
		int idJob;
		Job job;
		Employer employer;
		Principal principal = request.getPrincipal();

		Calendar c = new GregorianCalendar();
		Date d = c.getTime();

		idJob = request.getModel().getInteger("id");
		job = this.repository.findOneJobById(idJob);
		boolean elapsedDeadline = job.getDeadline().before(d);

		employer = job.getEmployer();
		result = job.isFinalMode() && !elapsedDeadline || job.isFinalMode() && elapsedDeadline && employer.getUserAccount().getId() == principal.getAccountId() || !job.isFinalMode() && employer.getUserAccount().getId() == principal.getAccountId();

		return result;
	}

	@Override
	public void unbind(final Request<Job> request, final Job entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "reference", "title", "deadline");
		request.unbind(entity, model, "salary", "moreInfo", "description", "finalMode");

	}

	@Override
	public Job findOne(final Request<Job> request) {
		assert request != null;

		Job result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findOneJobById(id);

		return result;
	}

}
