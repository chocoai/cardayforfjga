package com.cmdt.carrental.mobile.gateway.api.inf;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.cmdt.carrental.mobile.gateway.model.LoginDto;

@Produces(MediaType.APPLICATION_JSON)
public interface ILoginService {
	public Response login(@Valid @NotNull LoginDto login);
}
