
package acme.features.administrator.chart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.forms.chart.Chart;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Administrator;
import acme.framework.services.AbstractShowService;

@Service
public class AdministratorChartShowService implements AbstractShowService<Administrator, Chart> {

	// Internal state ---------------------------------------------------------

	@Autowired
	AdministratorChartRepository repository;


	// AbstractShowService<Administrator, Chart> interface --------------

	@Override
	public boolean authorise(final Request<Chart> request) {
		assert request != null;

		return true;
	}

	@Override
	public void unbind(final Request<Chart> request, final Chart entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "companySector", "companyNumber", "investorSector", "investorNumber", "finalMode", "ratioOfJobs", "applicationStatus", "ratioOfApplications", "momentPendingApplications", "momentAcceptedApplications",
			"momentRejectedApplications", "countPendingApplications", "countAcceptedApplications", "countRejectedApplications");
	}

	@Override
	public Chart findOne(final Request<Chart> request) {
		assert request != null;
		Chart result = new Chart();

		Calendar fourWeekAgoCalendar;
		fourWeekAgoCalendar = new GregorianCalendar();
		fourWeekAgoCalendar.add(Calendar.WEEK_OF_MONTH, -4);
		Date fourWeekAgo = fourWeekAgoCalendar.getTime();

		Calendar todayCalendar;
		todayCalendar = new GregorianCalendar();
		Date today = todayCalendar.getTime();

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		List<Long> companyNumber = new LinkedList<>();
		List<String> companySector = new LinkedList<>();
		List<Long> investorNumber = new LinkedList<>();
		List<String> investorSector = new LinkedList<>();
		List<Double> ratioOfJobs = new LinkedList<>();
		List<String> finalMode = new LinkedList<>();
		List<String> applicationStatus = new LinkedList<>();
		List<Double> ratioOfApplications = new LinkedList<>();
		List<Date> momentPendingApplications = new LinkedList<>();
		List<Date> momentAcceptedApplications = new LinkedList<>();
		List<Date> momentRejectedApplications = new LinkedList<>();
		List<Long> countPendingApplications = new LinkedList<>();
		List<Long> countAcceptedApplications = new LinkedList<>();
		List<Long> countRejectedApplications = new LinkedList<>();

		//--------------------------------------------------companies
		Collection<Object[]> companies = this.repository.numCompaniesBySector();

		companies.stream().forEach(c -> companyNumber.add((Long) c[0]));
		companies.stream().forEach(c -> companySector.add((String) c[1]));

		result.setCompanyNumber(companyNumber);
		result.setCompanySector(companySector);
		//--------------------------------------------------investors
		Collection<Object[]> investors = this.repository.numInvestorsBySector();

		investors.stream().forEach(i -> investorNumber.add((Long) i[0]));
		investors.stream().forEach(i -> investorSector.add((String) i[1]));

		result.setInvestorNumber(investorNumber);
		result.setInvestorSector(investorSector);
		//--------------------------------------------------jobs
		Collection<Object[]> jobs = this.repository.ratioOfJobsGroupedByStatus();

		jobs.stream().forEach(j -> ratioOfJobs.add((Double) j[0]));

		for (Object[] j : jobs) {
			if ((Boolean) j[1] == true) {
				finalMode.add("Final mode");
			} else {
				finalMode.add("Draft mode");
			}
		}

		result.setRatioOfJobs(ratioOfJobs);
		result.setFinalMode(finalMode);
		//--------------------------------------------------applications
		Collection<Object[]> applications = this.repository.ratioOfApplicationsGroupedByStatus();

		applications.stream().forEach(a -> ratioOfApplications.add((Double) a[0]));
		applications.stream().forEach(a -> applicationStatus.add((String) a[1]));

		result.setRatioOfApplications(ratioOfApplications);
		result.setApplicationStatus(applicationStatus);
		//--------------------------------------------------pending applications
		Date currentPending = fourWeekAgo;
		while (currentPending.before(today) || currentPending.equals(today)) {
			countPendingApplications.add(this.repository.numPendingApplicationsPerDays(currentPending));
			momentPendingApplications.add(currentPending);

			Calendar calendarPending = Calendar.getInstance();
			calendarPending.setTime(currentPending);
			calendarPending.add(Calendar.DATE, 1);
			currentPending = calendarPending.getTime();
		}

		result.setCountPendingApplications(countPendingApplications);
		result.setMomentPendingApplications(momentPendingApplications.stream().map(m -> formatter.format(m)).collect(Collectors.toList()));

		//--------------------------------------------------accepted applications
		Date currentAccepted = fourWeekAgo;
		while (currentAccepted.before(today) || currentAccepted.equals(today)) {
			countAcceptedApplications.add(this.repository.numAcceptedApplicationsPerDays(currentAccepted));
			momentAcceptedApplications.add(currentAccepted);

			Calendar calendarAccepted = Calendar.getInstance();
			calendarAccepted.setTime(currentAccepted);
			calendarAccepted.add(Calendar.DATE, 1);
			currentAccepted = calendarAccepted.getTime();
		}

		result.setCountAcceptedApplications(countAcceptedApplications);
		result.setMomentAcceptedApplications(momentAcceptedApplications.stream().map(m -> formatter.format(m)).collect(Collectors.toList()));
		//--------------------------------------------------rejected applications
		Date currentRejected = fourWeekAgo;
		while (currentRejected.before(today) || currentRejected.equals(today)) {
			countRejectedApplications.add(this.repository.numRejectedApplicationsPerDays(currentRejected));
			momentRejectedApplications.add(currentRejected);

			Calendar calendarRejected = Calendar.getInstance();
			calendarRejected.setTime(currentRejected);
			calendarRejected.add(Calendar.DATE, 1);
			currentRejected = calendarRejected.getTime();
		}

		result.setCountRejectedApplications(countRejectedApplications);
		result.setMomentRejectedApplications(momentRejectedApplications.stream().map(m -> formatter.format(m)).collect(Collectors.toList()));
		return result;
	}

}
