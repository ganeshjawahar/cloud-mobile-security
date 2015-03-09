package com.cloud.secure;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.cloud.secure.pojo.CreateSessionRequest;
import com.cloud.secure.pojo.GenericResponse;
import com.cloud.secure.pojo.PutImageRequest;
import com.cloud.secure.pojo.SearchRequest;
import com.cloud.secure.pojo.UserLoginRequest;
import com.cloud.secure.pojo.UserSignUpRequest;
import com.cloud.secure.s3.S3;
import com.cloud.secure.sql.DbClient;
import com.cloud.secure.sql.DbUtil;

@Path("/post/")
public class PostService {

	@POST
	@Path("loginInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public GenericResponse authenticateUser(UserLoginRequest req) {
		/*
		 * Gets the user credentials. Outputs the user data, if the validation
		 * is successful.
		 */
		if (req.getUserId() == null || req.getPassword() == null)
			return new GenericResponse("400",
					"Please check whether all the arguments are entered correctly.");

		try {
			return DbClient.userLogIn(req);
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse("400", e.getMessage());
		}
	}

	@POST
	@Path("signUpInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public GenericResponse createUser(UserSignUpRequest req) {
		/*
		 * Gets the user signup data. Creates a new account, if the user id does
		 * not exist.
		 */
		if (req.getId() == null || req.getName() == null
				|| req.getPassword() == null
				|| req.getClickDelayInSeconds() == null
				|| req.getUploadIntervalInSeconds() == null)
			return new GenericResponse("400",
					"Please check whether all the arguments are entered correctly.");

		try {
			DbClient.userSignUp(req);
			return new GenericResponse("200", "Signup success.");
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse("400", e.getMessage());
		}
	}

	@POST
	@Path("createSessionInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public GenericResponse createSession(CreateSessionRequest req) {
		/*
		 * Gets the session. Creates a new row in database for this session.
		 */
		if (req.getUserId() == null || req.getStartTime() == null
				|| req.getEndTime() == null || req.getLocation() == null)
			return new GenericResponse("400",
					"Please check whether all the arguments are entered correctly.");

		try {
			return DbClient.createSession(req);
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse("400", e.getMessage());
		}
	}

	@POST
	@Path("imageInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public GenericResponse putImage(PutImageRequest req) {
		/*
		 * Gets the image meta data. Create a new row in database to store meta
		 * data. Put the image into S3 bucket.
		 */
		if (req.getUserId() == null || req.getSessionId() == null
				|| req.getSnapedAt() == null || req.getData() == null)
			return new GenericResponse("400",
					"Please check whether all the arguments are entered correctly.");

		try {
			if (!DbClient.isExistingSession(req.getSessionId()))
				return new GenericResponse("400",
						"Please check the session id.");

			req.setImageId(DbUtil.getRandomId());
			S3.put(req);
			return new GenericResponse("200", "Image Upload success.");
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse("400", e.getMessage());
		}
	}

	@POST
	@Path("searchInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public GenericResponse searchQuery(SearchRequest req) {
		/*
		 * Serves the user query request.
		 */
		if (req.getDateStart() == null || req.getDateEnd() == null)
			return new GenericResponse("400",
					"Please check whether all the arguments are entered correctly.");

		try {
			return DbClient.fetchImagesBasedOnUserQuery(req);
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse("400", e.getMessage());
		}
	}

}