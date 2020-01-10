
package acme.features.auditor.auditRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.auditRecords.AuditRecord;
import acme.entities.jobs.Job;
import acme.entities.roles.Auditor;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuditorAuditRecordRepository extends AbstractRepository {

	@Query("select a from AuditRecord a where a.id = ?1")
	AuditRecord findOneAuditRecordById(int id);

	@Query("select a from AuditRecord a where a.finalMode != true and a.job.id = ?1 and a.auditor.id = ?2")
	Collection<AuditRecord> findManyNotFinalModeAuditRecordsByJobIdAndAuditorId(int id1, int id2);

	@Query("select a from AuditRecord a where a.finalMode = true and a.job.id = ?1")
	Collection<AuditRecord> findManyFinalModeAuditRecordsByJobId(int id1);

	@Query("select a from Auditor a where a.id = ?1")
	Auditor findOneAuditorById(int id);

	@Query("select j from Job j where j.id = ?1")
	Job findOneJobById(int id);
}
