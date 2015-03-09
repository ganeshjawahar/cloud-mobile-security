package com.cloud.secure;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.cloud.secure.pojo.GenericResponse;
import com.cloud.secure.sql.DbClient;

@Path("/get/")
public class GetService {

	@GET
	@Path("users")
	@Produces(MediaType.APPLICATION_JSON)
	public GenericResponse getUsers() {
		try {
			return DbClient.selectUsers();
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse("400", e.getMessage());
		}
	}

	@GET
	@Path("locations")
	@Produces(MediaType.APPLICATION_JSON)
	public GenericResponse getLocations() {
		try {
			return DbClient.selectLocations();
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse("400", e.getMessage());
		}

	}

}
