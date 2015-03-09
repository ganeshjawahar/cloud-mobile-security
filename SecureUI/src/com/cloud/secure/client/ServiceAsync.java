package com.cloud.secure.client;

import java.util.HashMap;
import java.util.List;

import com.cloud.secure.client.pojo.GenericResponse;
import com.cloud.secure.client.pojo.SearchRequest;
import com.cloud.secure.client.pojo.User;
import com.cloud.secure.client.pojo.UserLoginRequest;
import com.cloud.secure.client.pojo.UserLoginResponse;
import com.cloud.secure.client.pojo.UserSignUpRequest;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ServiceAsync {
	void getAvailableUsers(AsyncCallback<List<String>> callback);

	void getAvailableLocations(AsyncCallback<List<String>> callback);

	void answerUserQuery(SearchRequest request,
			AsyncCallback<List<User>> callback);

	void login(UserLoginRequest request,
			AsyncCallback<UserLoginResponse> callback);

	void signup(UserSignUpRequest request,
			AsyncCallback<GenericResponse> callback);
	
}
