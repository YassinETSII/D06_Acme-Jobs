
package acme.features.authenticated.auditRecord;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.auditRecords.AuditRecord;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractShowService;

@Service
public class AuthenticatedAuditRecordShowService implements AbstractShowService<Authenticated, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	AuthenticatedAuditRecordRepository repository;

	// AbstractShowService<Authenticated, AuditRecord> interface --------------


	//An authenticated can not access a not final mode audit record
	@Override
	public boolean authorise(final Request<AuditRecord> request) {
		assert request != null;

		boolean result;
		int auditRecordId;
		AuditRecord auditRecord;

		Calendar c = new GregorianCalendar();
		Date d = c.getTime();

		auditRecordId = request.getModel().getInteger("id");
		auditRecord = this.repository.findOneAuditRecordById(auditRecordId);
		boolean elapsedDeadline = auditRecord.getJob().getDeadline().before(d);
		result = auditRecord.isFinalMode() == true && !elapsedDeadline;
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
