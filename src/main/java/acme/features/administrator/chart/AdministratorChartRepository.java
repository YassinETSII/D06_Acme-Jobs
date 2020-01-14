
package acme.features.administrator.chart;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.repositories.AbstractRepository;

@Repository
public interface AdministratorChartRepository extends AbstractRepository {

	@Query("select count(c), c.sector from CompanyRecord c group by c.sector")
	Collection<Object[]> numCompaniesBySector();

	@Query("select count(i), i.sector from InvestorRecord i group by i.sector")
	Collection<Object[]> numInvestorsBySector();

	@Query("select 1.0 * count(a) / (select count(b) from Job b), a.finalMode from Job a group by a.finalMode")
	Collection<Object[]> ratioOfJobsGroupedByStatus();

	@Query("select 1.0 * count(a) / (select count(b) from Application b), a.status from Application a group by a.status")
	Collection<Object[]> ratioOfApplicationsGroupedByStatus();

	@Query("select count(a) from Application a where a.status = 'pending' and cast(a.moment as date) = ?1")
	Long numPendingApplicationsPerDays(Date date);

	@Query("select count(a) from Application a where a.status = 'accepted' and cast(a.updateMoment as date) = ?1")
	Long numAcceptedApplicationsPerDays(Date date);

	@Query("select count(a) from Application a where a.status = 'rejected' and cast(a.updateMoment as date) = ?1")
	Long numRejectedApplicationsPerDays(Date date);

}
