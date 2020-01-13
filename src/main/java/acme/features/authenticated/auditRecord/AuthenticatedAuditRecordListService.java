
package acme.features.authenticated.auditRecord;

import java.util.Collection;

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


	//An authenticated principal can not list the audit records of not finalMode jobs
	@Override
	public boolean authorise(final Request<AuditRecord> request) {
		boolean result;
		int idJob;
		Job job;

		idJob = request.getModel().getInteger("idJob");
		job = this.repository.findOneJobById(idJob);
		result = job.isFinalMode() == true;
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
