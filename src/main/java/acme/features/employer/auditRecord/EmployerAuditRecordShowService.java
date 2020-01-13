
package acme.features.employer.auditRecord;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.auditRecords.AuditRecord;
import acme.entities.roles.Employer;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractShowService;

@Service
public class EmployerAuditRecordShowService implements AbstractShowService<Employer, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	EmployerAuditRecordRepository repository;

	// AbstractShowService<Employer, AuditRecord> interface --------------


	//An employer can not access a not final mode audit record. Neither an audit record of an elapsed job
	//unless it's the creator.
	@Override
	public boolean authorise(final Request<AuditRecord> request) {
		assert request != null;

		boolean result;
		int auditRecordId;
		AuditRecord auditRecord;
		Principal principal = request.getPrincipal();

		Calendar c = new GregorianCalendar();
		Date d = c.getTime();

		auditRecordId = request.getModel().getInteger("id");
		auditRecord = this.repository.findOneAuditRecordById(auditRecordId);

		boolean elapsedDeadline = auditRecord.getJob().getDeadline().before(d);
		boolean isCreator = this.repository.findOneEmployerByAuditRecordId(auditRecordId).getUserAccount().getId() == principal.getAccountId();
		result = auditRecord.isFinalMode() && !elapsedDeadline || auditRecord.isFinalMode() && elapsedDeadline && isCreator;

		return result;
	}

	@Override
	public void unbind(final Request<AuditRecord> request, final AuditRecord entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "moment", "body", "job.reference");

	}

	@Override
	public AuditRecord findOne(final Request<AuditRecord> request) {
		assert request != null;

		AuditRecord result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findOneAuditRecordById(id);

		return result;
	}

}
