
package acme.features.worker.job;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.jobs.Job;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface WorkerJobRepository extends AbstractRepository {

	@Query("select j from Job j where j.id = ?1")
	Job findOneJobById(int id);

	@Query("select j from Job j where j.finalMode = true and j.deadline > ?1 and j.id not in(select a.job.id from Application a where a.worker.id = ?2)")
	Collection<Job> findManyNotAppliedJobs(Date d, int id);

	@Query("select distinct j from Job j where j.finalMode = true and j.deadline > ?1 and j.id in(select a.job.id from Application a where a.worker.id = ?2)")
	Collection<Job> findManyAppliedJobs(Date d, int id);

}
