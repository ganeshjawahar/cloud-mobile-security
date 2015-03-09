package com.cloud.secure.sql;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.cloud.secure.AppGlobals;
import com.cloud.secure.pojo.CreateSessionRequest;
import com.cloud.secure.pojo.CreateSessionResponse;
import com.cloud.secure.pojo.FetchAvailableLocationsResponse;
import com.cloud.secure.pojo.FetchAvailableUsersResponse;
import com.cloud.secure.pojo.SearchRequest;
import com.cloud.secure.pojo.UserLoginRequest;
import com.cloud.secure.pojo.UserLoginResponse;
import com.cloud.secure.pojo.UserSignUpRequest;
import com.cloud.secure.pojo.searchresp.Image;
import com.cloud.secure.pojo.searchresp.ImageSet;
import com.cloud.secure.pojo.searchresp.SearchResponse;
import com.cloud.secure.pojo.searchresp.User;
import com.cloud.secure.s3.S3;

public class DbClient {

	public static void userSignUp(final UserSignUpRequest userInfo)
			throws SQLException, IOException, PropertyVetoException,
			ClassNotFoundException {
		if (isExistingUser(userInfo.getId()))
			throw new SQLException("User ID " + userInfo.getId()
					+ " already exist.");

		final String query = Query.INSERT_NEW_USER.toString();
		final Connection conn = DataSource.getInstance().getConnection();
		final PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, userInfo.getId());
		stmt.setString(2, userInfo.getName());
		stmt.setString(3, userInfo.getPassword());
		stmt.setDouble(4, Double.parseDouble(userInfo.getClickDelayInSeconds()));
		stmt.setDouble(5,
				Double.parseDouble(userInfo.getUploadIntervalInSeconds()));
		stmt.executeUpdate();
		conn.close();
	}

	public static UserLoginResponse userLogIn(final UserLoginRequest userInfo)
			throws SQLException, IOException, PropertyVetoException,
			ClassNotFoundException {
		final String query = Query.SELECT_USER_DETAILS.toString();
		final Connection conn = DataSource.getInstance().getConnection();
		final PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, userInfo.getUserId());

		ResultSet rs = stmt.executeQuery();
		if (!rs.next()) {
			conn.close();
			throw new SQLException("User ID " + userInfo.getUserId()
					+ " does not exist.");
		} else {
			if (!rs.getString("password").equals(userInfo.getPassword())) {
				conn.close();
				throw new SQLException("Authentication problem.");
			}
			UserLoginResponse resp = new UserLoginResponse("200",
					"Login Success.", userInfo.getUserId(),
					rs.getString("name"), rs.getString("password"),
					rs.getString("clickDelayInSeconds"),
					rs.getString("uploadIntervalInSeconds"));
			conn.close();
			return resp;
		}
	}

	public static CreateSessionResponse createSession(
			final CreateSessionRequest sessionInfo) throws SQLException,
			IOException, PropertyVetoException, ParseException,
			ClassNotFoundException {
		if (!isExistingUser(sessionInfo.getUserId())) {
			throw new SQLException("User ID " + sessionInfo.getUserId()
					+ "  does not exist.");
		}

		final String sessionId = DbUtil.getRandomId();
		final String query = Query.CREATE_SESSION.toString();
		final Connection conn = DataSource.getInstance().getConnection();
		final PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, sessionId);
		stmt.setString(2, sessionInfo.getUserId());
		stmt.setTimestamp(3,
				DbUtil.convertStringToSql(sessionInfo.getStartTime()));
		stmt.setTimestamp(4,
				DbUtil.convertStringToSql(sessionInfo.getEndTime()));
		stmt.setString(5, sessionInfo.getLocation());
		stmt.executeUpdate();
		conn.close();
		return new CreateSessionResponse("200",
				"Session created successfully.", sessionId);
	}

	public static boolean isExistingUser(final String userId)
			throws SQLException, IOException, PropertyVetoException,
			ClassNotFoundException {
		final String query = Query.CHECK_FOR_EXISTING_USER.toString();
		final Connection conn = DataSource.getInstance().getConnection();
		final PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, userId);
		final boolean ans = stmt.executeQuery().next();
		conn.close();
		return ans;
	}

	public static boolean isExistingSession(final String sessionId)
			throws SQLException, IOException, PropertyVetoException,
			ClassNotFoundException {
		final String query = Query.CHECK_FOR_EXISTING_SESSION.toString();
		final Connection conn = DataSource.getInstance().getConnection();
		final PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, sessionId);
		final boolean ans = stmt.executeQuery().next();
		conn.close();
		return ans;
	}

	public static SearchResponse fetchImagesBasedOnUserQuery(
			final SearchRequest req) throws SQLException, IOException,
			PropertyVetoException, ClassNotFoundException, ParseException {
		final String query = prepareFilters(req);
		final Connection conn = DataSource.getInstance().getConnection();
		final PreparedStatement stmt = conn.prepareStatement(query);
		int index = 1;

		final Timestamp startDate = DbUtil.convertStringToSql(req
				.getDateStart());
		final Timestamp endDate = DbUtil.convertStringToSql(req.getDateEnd());

		// Fill date filters
		stmt.setTimestamp(index++, startDate);
		stmt.setTimestamp(index++, endDate);
		//stmt.setTimestamp(index++, startDate);
		//stmt.setTimestamp(index++, endDate);

		// Fill user name filters
		if (req.getUserList() != null && req.getUserList().size() > 0) {
			for (final String user : req.getUserList())
				stmt.setString(index++, user);
		}

		// Fill location filters
		if (req.getLocationList() != null && req.getLocationList().size() > 0) {
			for (final String location : req.getLocationList())
				stmt.setString(index++, location);
		}

		System.out.println(stmt);
		ResultSet rs = stmt.executeQuery();
		if (!rs.next()) {
			conn.close();
			throw new SQLException("Returned empty set.");
		} else {
			final LinkedList<User> userList = new LinkedList<User>();
			List<Image> imgList = new ArrayList<Image>();
			List<ImageSet> imgSet = new ArrayList<ImageSet>();
			String lastSeenUser = null;
			Timestamp lastSeenDay = null;
			do {
				final String sessionId = rs.getString("id");
				final String userId = rs.getString("userId");
				final String location = rs.getString("location");
				final List<String> s3Imgs = S3.getImages(userId + "/"
						+ sessionId + "/");

				for (final String s3Image : s3Imgs) {
					final String[] keys = s3Image.split("_");
					final String imageId = keys[0];
					final Timestamp snapedAt = DbUtil.parseTimeStamp(keys[2],
							keys[3], keys[4], keys[5], keys[6], keys[7]);

					if (startDate.before(snapedAt) && snapedAt.before(endDate)) {

						if (lastSeenUser == null
								|| !userId.equals(lastSeenUser)) {
							if (lastSeenUser != null) {
								imgSet.add(new ImageSet(
										DbUtil.convertTimestampToStringWithoutTime(lastSeenDay),
										DbUtil.cloneImage(imgList), imgList
												.size()));
								userList.add(new User(lastSeenUser, DbUtil
										.cloneImageSet(imgSet), imgSet.size()));
								lastSeenDay = null;
							}
							lastSeenUser = userId;
							imgSet.clear();
						}

						if (lastSeenDay == null
								|| DbUtil.isDifferentDay(lastSeenDay, snapedAt)) {
							if (lastSeenDay != null)
								imgSet.add(new ImageSet(
										DbUtil.convertTimestampToStringWithoutTime(lastSeenDay),
										DbUtil.cloneImage(imgList), imgList
												.size()));
							lastSeenDay = snapedAt;
							imgList.clear();
						}

						// Get image properties.
						final Image image = new Image();
						image.setImageId(imageId);
						image.setSnapedAt(DbUtil
								.convertTimestampToString(snapedAt));
						image.setLocation(location);
						image.setResourcePath(AppGlobals.CLOUD_FRONT_PREFIX
								+ "/" + userId + "/" + sessionId + "/"
								+ s3Image);
						imgList.add(image);
					}

				}
			} while (rs.next());
			imgSet.add(new ImageSet(DbUtil
					.convertTimestampToStringWithoutTime(lastSeenDay), DbUtil
					.cloneImage(imgList), imgList.size()));
			userList.add(new User(lastSeenUser, imgSet, imgSet.size()));
			conn.close();
			return new SearchResponse("200", "Successfully fetched records.",
					userList);
		}

	}

	public static String prepareFilters(final SearchRequest req) {
		final StringBuilder builder = new StringBuilder(
				Query.FETCH_IMAGES.toString());

		// Add date filters
		builder.append(" where startedAt >= ? and endedAt <= ? ");

		if (req.getUserList() != null && req.getUserList().size() > 0) {
			// Add user name filters
			builder.append(" and userid in (");
			int count = req.getUserList().size();
			while (count-- > 0)
				builder.append("?,");
			builder.setLength(builder.length() - 1);
			builder.append(") ");
		}

		// Add location filters
		if (req.getLocationList() != null && req.getLocationList().size() > 0) {
			builder.append(" and location in (");
			int count = req.getLocationList().size();
			while (count-- > 0)
				builder.append("?,");
			builder.setLength(builder.length() - 1);
			builder.append(") ");
		}

		builder.append(" order by userid,startedAt;");

		return builder.toString();
	}

	public static FetchAvailableUsersResponse selectUsers()
			throws ClassNotFoundException, SQLException, IOException,
			PropertyVetoException {
		final String query = Query.FETCH_USERS.toString();
		final Connection conn = DataSource.getInstance().getConnection();
		final PreparedStatement stmt = conn.prepareStatement(query);
		final ResultSet rs = stmt.executeQuery();
		if (!rs.next()) {
			conn.close();
			throw new SQLException("Returned empty set.");
		} else {
			final List<String> userList = new ArrayList<String>();
			do {
				userList.add(rs.getString("id"));
			} while (rs.next());
			conn.close();
			return new FetchAvailableUsersResponse("200", "Success", userList);
		}
	}

	public static FetchAvailableLocationsResponse selectLocations()
			throws ClassNotFoundException, SQLException, IOException,
			PropertyVetoException {
		final String query = Query.FETCH_LOCATIONS.toString();
		final Connection conn = DataSource.getInstance().getConnection();
		final PreparedStatement stmt = conn.prepareStatement(query);
		final ResultSet rs = stmt.executeQuery();
		if (!rs.next()) {
			conn.close();
			throw new SQLException("Returned empty set.");
		} else {
			final List<String> locationList = new ArrayList<String>();
			do {
				locationList.add(rs.getString("location"));
			} while (rs.next());
			conn.close();
			return new FetchAvailableLocationsResponse("200", "Success",
					locationList);
		}
	}

}

enum Query {
	CHECK_FOR_EXISTING_USER("select 1 from user where id=?;"), CHECK_FOR_EXISTING_SESSION(
			"select 1 from session where id=?;"), INSERT_NEW_USER(
			"insert into user(id,name,password,clickDelayInSeconds,uploadIntervalInSeconds) values(?,?,?,?,?);"), SELECT_USER_DETAILS(
			"select name,password,clickDelayInSeconds,uploadIntervalInSeconds from user where id=?;"), CREATE_SESSION(
			"insert into session(id,userId,startedAt,endedAt,location) values(?,?,?,?,?);"), FETCH_IMAGES(
			"select id,userId,location from session"), FETCH_USERS(
			"select id from user;"), FETCH_LOCATIONS(
			"select distinct(location) from session;");

	String value;

	private Query(String value) {
		this.value = value;
	}

	public String toString() {
		return value;
	}
}