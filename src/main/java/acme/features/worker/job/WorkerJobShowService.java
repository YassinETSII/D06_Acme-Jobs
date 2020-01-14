
package acme.features.worker.job;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.jobs.Job;
import acme.entities.roles.Worker;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.services.AbstractShowService;

@Service
public class WorkerJobShowService implements AbstractShowService<Worker, Job> {

	// Internal state ---------------------------------------------------------

	@Autowired
	WorkerJobRepository repository;

	// AbstractShowService<Worker, Job> interface --------------


	//An Worker principal can not have access to a not finalMode job
	@Override
	public boolean authorise(final Request<Job> request) {
		assert request != null;

		boolean result;
		int jobId;
		Job job;
		Calendar c = new GregorianCalendar();
		Date d = c.getTime();

		jobId = request.getModel().getInteger("id");
		job = this.repository.findOneJobById(jobId);
		boolean elapsedDeadline = job.getDeadline().before(d);

		result = job.isFinalMode() == true && !elapsedDeadline;

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
