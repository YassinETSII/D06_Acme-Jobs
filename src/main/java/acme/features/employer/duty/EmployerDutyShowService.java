
package acme.features.employer.duty;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.duties.Duty;
import acme.entities.roles.Employer;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractShowService;

@Service
public class EmployerDutyShowService implements AbstractShowService<Employer, Duty> {

	// Internal state ---------------------------------------------------------

	@Autowired
	EmployerDutyRepository repository;

	// AbstractShowService<Employer, Duty> interface --------------


	//An employer principal can not have access to a duty of a not finalMode job from another employer
	@Override
	public boolean authorise(final Request<Duty> request) {
		assert request != null;

		boolean result;
		int idDuty;
		Duty dutyJob;
		Employer employer;
		Principal principal;

		idDuty = request.getModel().getInteger("id");
		dutyJob = this.repository.findOneDutyById(idDuty);
		employer = dutyJob.getJob().getEmployer();
		principal = request.getPrincipal();

		Calendar c = new GregorianCalendar();
		Date d = c.getTime();

		boolean elapsedDeadline = dutyJob.getJob().getDeadline().before(d);

		result = dutyJob.getJob().isFinalMode() && !elapsedDeadline || dutyJob.getJob().isFinalMode() && elapsedDeadline && employer.getUserAccount().getId() == principal.getAccountId()
			|| !dutyJob.getJob().isFinalMode() && employer.getUserAccount().getId() == principal.getAccountId();

		return result;
	}

	@Override
	public void unbind(final Request<Duty> request, final Duty entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "description", "timePercentage", "job");

		int idJob = entity.getJob().getId();
		model.setAttribute("idJob", idJob);
	}

	@Override
	public Duty findOne(final Request<Duty> request) {
		assert request != null;

		Duty result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findOneDutyById(id);

		return result;
	}

}
