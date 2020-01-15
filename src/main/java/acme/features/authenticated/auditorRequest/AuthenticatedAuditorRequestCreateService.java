/*
 * AuthenticatedAuditorCreateService.java
 *
 * Copyright (c) 2019 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.auditorRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.auditorRequests.AuditorRequest;
import acme.framework.components.Errors;
import acme.framework.components.HttpMethod;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.components.Response;
import acme.framework.entities.Authenticated;
import acme.framework.entities.Principal;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractCreateService;

@Service
public class AuthenticatedAuditorRequestCreateService implements AbstractCreateService<Authenticated, AuditorRequest> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedAuditorRequestRepository repository;

	// AbstractCreateService<Authenticated, AuditorRequest> ---------------------------


	@Override
	public boolean authorise(final Request<AuditorRequest> request) {
		assert request != null;

		return true;
	}

	@Override
	public void validate(final Request<AuditorRequest> request, final AuditorRequest entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
	}

	@Override
	public void bind(final Request<AuditorRequest> request, final AuditorRequest entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<AuditorRequest> request, final AuditorRequest entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "firm", "responsibilityStatement");

		int userId;
		userId = request.getPrincipal().getActiveRoleId();
		AuditorRequest auditorRequest;
		auditorRequest = this.repository.findOneByUserId(userId);
		boolean isPending = false;
		boolean isAccepted = false;
		boolean isRejected = false;
		if (auditorRequest != null) {
			if (auditorRequest.getStatus().equals("pending")) {
				isPending = true;
			} else if (auditorRequest.getStatus().equals("accepted")) {
				isAccepted = true;
			} else if (auditorRequest.getStatus().equals("rejected")) {
				isRejected = true;
			}
		}
		model.setAttribute("isPending", isPending);
		model.setAttribute("isAccepted", isAccepted);
		model.setAttribute("isRejected", isRejected);
	}

	@Override
	public AuditorRequest instantiate(final Request<AuditorRequest> request) {
		assert request != null;

		AuditorRequest result;
		Principal principal;
		int userId;
		Authenticated user;

		principal = request.getPrincipal();
		userId = principal.getActiveRoleId();
		user = this.repository.findOneUserById(userId);

		result = new AuditorRequest();
		result.setUser(user);
		result.setStatus("pending");

		return result;
	}

	@Override
	public void create(final Request<AuditorRequest> request, final AuditorRequest entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}

	@Override
	public void onSuccess(final Request<AuditorRequest> request, final Response<AuditorRequest> response) {
		assert request != null;
		assert response != null;

		if (request.isMethod(HttpMethod.GET)) {
			PrincipalHelper.handleUpdate();
		}
	}

}
