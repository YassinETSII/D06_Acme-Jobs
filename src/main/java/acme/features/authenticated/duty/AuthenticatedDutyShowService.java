
package acme.features.authenticated.duty;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.duties.Duty;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractShowService;

@Service
public class AuthenticatedDutyShowService implements AbstractShowService<Authenticated, Duty> {

	// Internal state ---------------------------------------------------------

	@Autowired
	AuthenticatedDutyRepository repository;

	// AbstractShowService<Authenticated, Duty> interface --------------


	//An authenticated principal can not have access to a duty from a not finalMode job. Neither to elapsed ones.
	@Override
	public boolean authorise(final Request<Duty> request) {
		assert request != null;

		boolean result;
		int idJob;
		Duty duty;

		Calendar c = new GregorianCalendar();
		Date d = c.getTime();

		idJob = request.getModel().getInteger("id");
		duty = this.repository.findOneDutyById(idJob);
		boolean elapsedDeadline = duty.getJob().getDeadline().before(d);

		result = duty.getJob().isFinalMode() == true && !elapsedDeadline;

		return result;
	}

	@Override
	public void unbind(final Request<Duty> request, final Duty entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "description", "timePercentage");

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
