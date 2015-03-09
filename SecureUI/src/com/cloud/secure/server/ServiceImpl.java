package com.cloud.secure.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cloud.secure.client.Service;
import com.cloud.secure.client.pojo.FetchAvailableLocationsResponse;
import com.cloud.secure.client.pojo.FetchAvailableUsersResponse;
import com.cloud.secure.client.pojo.GenericResponse;
import com.cloud.secure.client.pojo.SearchRequest;
import com.cloud.secure.client.pojo.SearchResponse;
import com.cloud.secure.client.pojo.User;
import com.cloud.secure.client.pojo.UserLoginRequest;
import com.cloud.secure.client.pojo.UserLoginResponse;
import com.cloud.secure.client.pojo.UserSignUpRequest;
import com.cloud.secure.shared.AppGlobals;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ServiceImpl extends RemoteServiceServlet implements Service {

	@Override
	public List<String> getAvailableUsers() {
		try {
			final URL url = new URL(AppGlobals.REST_BASE_URL + "get/users");
			final URLConnection conn = url.openConnection();
			conn.connect();
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			final FetchAvailableUsersResponse resp = gson.fromJson(reader,
					FetchAvailableUsersResponse.class);
			return resp.getCode().equals("200") ? resp.getUserList() : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> getAvailableLocations() {
		try {
			final URL url = new URL(AppGlobals.REST_BASE_URL + "get/locations");
			final URLConnection conn = url.openConnection();
			conn.connect();
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			final FetchAvailableLocationsResponse resp = gson.fromJson(reader,
					FetchAvailableLocationsResponse.class);
			return resp.getCode().equals("200") ? resp.getLocationList() : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<User> answerUserQuery(SearchRequest request) {
		Logger.getLogger("").log(Level.INFO, gson.toJson(request));
		try {
			final URL url = new URL(AppGlobals.REST_BASE_URL
					+ "post/searchInfo");
			final HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();

			// add request header
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			// Send post request
			conn.setDoOutput(true);
			final DataOutputStream wr = new DataOutputStream(
					conn.getOutputStream());
			wr.writeBytes(gson.toJson(request));
			wr.flush();
			wr.close();

			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			final SearchResponse resp = gson.fromJson(reader,
					SearchResponse.class);
			return resp.getCode().equals("200") ? resp.getUserList() : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	

	final static Gson gson = new Gson();

	@Override
	public UserLoginResponse login(UserLoginRequest request) {
		Logger.getLogger("").log(Level.INFO, gson.toJson(request));
		try {
			final URL url = new URL(AppGlobals.REST_BASE_URL
					+ "post/loginInfo");
			final HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();

			// add request header
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			// Send post request
			conn.setDoOutput(true);
			final DataOutputStream wr = new DataOutputStream(
					conn.getOutputStream());
			wr.writeBytes(gson.toJson(request));
			wr.flush();
			wr.close();

			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			final UserLoginResponse resp = gson.fromJson(reader,
					UserLoginResponse.class);		
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public GenericResponse signup(UserSignUpRequest request) {
		Logger.getLogger("").log(Level.INFO, gson.toJson(request));
		try {
			final URL url = new URL(AppGlobals.REST_BASE_URL
					+ "post/signUpInfo");
			final HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();

			// add request header
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			// Send post request
			conn.setDoOutput(true);
			final DataOutputStream wr = new DataOutputStream(
					conn.getOutputStream());
			wr.writeBytes(gson.toJson(request));
			wr.flush();
			wr.close();

			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			final GenericResponse resp = gson.fromJson(reader,
					GenericResponse.class);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
