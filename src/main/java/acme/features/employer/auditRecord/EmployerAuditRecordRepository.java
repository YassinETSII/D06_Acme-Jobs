
package acme.features.employer.auditRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.auditRecords.AuditRecord;
import acme.entities.jobs.Job;
import acme.entities.roles.Employer;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface EmployerAuditRecordRepository extends AbstractRepository {

	@Query("select a from AuditRecord a where a.id = ?1")
	AuditRecord findOneAuditRecordById(int id);

	@Query("select a from AuditRecord a where a.finalMode = true and a.job.id = ?1")
	Collection<AuditRecord> findManyAuditRecordsByJobId(int id);

	@Query("select j.employer from Job j where j.id = (select a.job.id from AuditRecord a where a.id = ?1)")
	Employer findOneEmployerByAuditRecordId(int id);

	@Query("select j from Job j where j.id = ?1")
	Job findOneJobById(int id);
}
