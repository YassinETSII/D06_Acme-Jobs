<%--
- form.jsp
-
- Copyright (c) 2019 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<acme:form readonly="true">
		<acme:form-textbox code="worker.job.form.label.reference" path="reference" placeholder="EEEE-JJJJ"/>
		<acme:form-textbox code="worker.job.form.label.title" path="title"/>
		<acme:form-moment code="worker.job.form.label.deadline" path="deadline"/>
		<acme:form-money code="worker.job.form.label.salary" path="salary"/>
		<acme:form-url code="worker.job.form.label.moreInfo" path="moreInfo"/>
		<acme:form-textarea code="worker.job.form.label.description" path="description"/>
	
		<acme:form-submit code="worker.job.form.button.add-application" action="/worker/application/create?idJob=${id}" method="get" />
		<acme:form-submit code="worker.job.form.button.list-duties" action="/authenticated/duty/list?idJob=${id}" method="get" />
		<acme:form-submit code="worker.job.form.button.list-audit-records" action="/authenticated/audit-record/list?idJob=${id}" method="get" />
		
	<acme:form-return code="worker.job.form.button.return"/>	
	
</acme:form>