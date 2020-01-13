
package acme.features.authenticated.auditRecord;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.auditRecords.AuditRecord;
import acme.entities.jobs.Job;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractListService;

@Service
public class AuthenticatedAuditRecordListService implements AbstractListService<Authenticated, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	AuthenticatedAuditRecordRepository repository;

	// AbstractListService<Authenticated, AuditRecord> interface --------------


	//An authenticated principal can not list the audit records of an elapsed job
	@Override
	public boolean authorise(final Request<AuditRecord> request) {
		assert request != null;

		boolean result;
		int idJob;
		Job job;

		Calendar c = new GregorianCalendar();
		Date d = c.getTime();

		idJob = request.getModel().getInteger("idJob");
		job = this.repository.findOneJobById(idJob);
		boolean elapsedDeadline = job.getDeadline().before(d);

		result = !elapsedDeadline;

		return result;
	}

	@Override
	public void unbind(final Request<AuditRecord> request, final AuditRecord entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "moment");
	}

	@Override
	public Collection<AuditRecord> findMany(final Request<AuditRecord> request) {
		assert request != null;

		Collection<AuditRecord> result;

		int idJob = request.getModel().getInteger("idJob");
		result = this.repository.findManyAuditRecordsByJobId(idJob);
		return result;

	}

}
