package com.cloud.secure.client;

import java.util.List;

import com.cloud.secure.client.pojo.GenericResponse;
import com.cloud.secure.client.pojo.SearchRequest;
import com.cloud.secure.client.pojo.User;
import com.cloud.secure.client.pojo.UserLoginRequest;
import com.cloud.secure.client.pojo.UserLoginResponse;
import com.cloud.secure.client.pojo.UserSignUpRequest;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface Service extends RemoteService {
	List<String> getAvailableUsers();
	List<String> getAvailableLocations();	
	List<User> answerUserQuery(SearchRequest request);
	UserLoginResponse login(UserLoginRequest request);
	GenericResponse signup(UserSignUpRequest request);
}
