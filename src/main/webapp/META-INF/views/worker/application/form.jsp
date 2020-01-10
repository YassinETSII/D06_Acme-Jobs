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

<acme:form>
	<jstl:if test="${command == 'show'}">
		<acme:form-textbox code="worker.application.form.label.reference" path="reference" placeholder="EEEE-JJJJ:WWWW" readonly="true"/>
		<acme:form-moment code="worker.application.form.label.moment" path="moment" readonly="true"/>
		<acme:form-textbox code="worker.application.form.label.status" path="status" readonly="true"/>
		<acme:form-textarea code="worker.application.form.label.statement" path="statement" readonly="true"/>
		<acme:form-textarea code="worker.application.form.label.skills" path="skills" readonly="true"/>
		<acme:form-textarea code="worker.application.form.label.qualifications" path="qualifications" readonly="true"/>
		<acme:form-textbox code="worker.application.form.label.job.reference" path="job.reference" readonly="true"/>	
	
	<jstl:if test="${status != 'pending'}">
		<acme:form-textarea code="worker.application.form.label.justification" path="justification"/>
	</jstl:if>
	
		<acme:form-submit code="worker.application.form.label.job" action="/authenticated/job/show?id=${idJob}" method="get" />
	</jstl:if>
	
	<jstl:if test="${command == 'create'}">
		<acme:form-textbox code="worker.application.form.label.reference" path="reference" placeholder="EEEE-JJJJ:WWWW"/>
		<acme:form-textarea code="worker.application.form.label.statement" path="statement"/>
		<acme:form-textarea code="worker.application.form.label.skills" path="skills"/>
		<acme:form-textarea code="worker.application.form.label.qualifications" path="qualifications"/>
		<acme:form-submit code="worker.application.form.button.create" action="/worker/application/create?idJob=${idJob}" method="post"/>
	</jstl:if>
		
		<acme:form-return code="worker.application.form.button.return"/>
</acme:form>