
package acme.features.auditor.auditRecord;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.auditRecords.AuditRecord;
import acme.entities.roles.Auditor;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.services.AbstractCreateService;

@Service
public class AuditorAuditRecordCreateService implements AbstractCreateService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	AuditorAuditRecordRepository repository;

	// AbstractCreateService<Auditor, AuditRecord> interface --------------


	@Override
	public boolean authorise(final Request<AuditRecord> request) {
		assert request != null;

		return true;
	}

	@Override
	public void unbind(final Request<AuditRecord> request, final AuditRecord entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "body", "finalMode", "job.reference");
		int idJob = request.getModel().getInteger("idJob");
		model.setAttribute("idJob", idJob);

		Calendar c = new GregorianCalendar();
		Date d = c.getTime();

		boolean elapsedJob = entity.getJob().getDeadline().before(d);
		model.setAttribute("elapsedJob", elapsedJob);

	}

	@Override
	public void bind(final Request<AuditRecord> request, final AuditRecord entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "moment", "job.reference");

	}

	@Override
	public AuditRecord instantiate(final Request<AuditRecord> request) {
		assert request != null;
		AuditRecord result;
		result = new AuditRecord();
		Auditor auditor = this.repository.findOneAuditorById(request.getPrincipal().getActiveRoleId());
		result.setAuditor(auditor);
		result.setJob(this.repository.findOneJobById(request.getModel().getInteger("idJob")));
		return result;
	}

	@Override
	public void validate(final Request<AuditRecord> request, final AuditRecord entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

	}

	@Override
	public void create(final Request<AuditRecord> request, final AuditRecord entity) {
		assert request != null;
		assert entity != null;

		Date moment;
		moment = new Date(System.currentTimeMillis() - 1);
		entity.setMoment(moment);
		this.repository.save(entity);

	}

}
