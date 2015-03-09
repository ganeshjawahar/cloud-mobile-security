package com.cloud.secure.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cloud.secure.client.pojo.GenericResponse;
import com.cloud.secure.client.pojo.Image;
import com.cloud.secure.client.pojo.ImageSet;
import com.cloud.secure.client.pojo.SearchRequest;
import com.cloud.secure.client.pojo.User;
import com.cloud.secure.client.pojo.UserLoginRequest;
import com.cloud.secure.client.pojo.UserLoginResponse;
import com.cloud.secure.client.pojo.UserSignUpRequest;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SecureUI implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final ServiceAsync proxyService = GWT.create(Service.class);

	final HorizontalPanel rootPanel = new HorizontalPanel();
	final VerticalPanel userInputPanel = new VerticalPanel();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		showLoginScreen();
	}

	public void processUI(final String userid) {
		final boolean isAdmin = userid.equals("admin");

		Anchor anchor = new Anchor("logged in as '" + userid + "'");
		anchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("You are logged out successfully.");
				Window.Location.reload();
			}
		});
		RootPanel.get().add(anchor);

		rootPanel.setTitle("View image based on some query.");
		rootPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		rootPanel.setSpacing(10);

		/*
		 * Add the user input controls
		 */
		userInputPanel.setTitle("Key in your input here.");
		userInputPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		userInputPanel.setSpacing(10);

		final VerticalPanel startDateInputPanel = new VerticalPanel();
		startDateInputPanel.setTitle("Key in date input here");
		startDateInputPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		final Label startDateLabel = new Label("Session Start date");
		startDateLabel.setTitle("Select the start date");
		startDateInputPanel.add(startDateLabel);
		final DateBox startDatePicker = new DateBox();
		startDatePicker.setWidth("135px");
		startDateInputPanel.add(startDatePicker);
		userInputPanel.add(startDateInputPanel);

		final VerticalPanel endDateInputPanel = new VerticalPanel();
		endDateInputPanel.setTitle("Key in date input here");
		endDateInputPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		final Label endDateLabel = new Label("Session End date");
		endDateLabel.setTitle("Select the end date");
		endDateInputPanel.add(endDateLabel);
		final DateBox endDatePicker = new DateBox();
		endDatePicker.setWidth("135px");
		endDateInputPanel.add(endDatePicker);
		userInputPanel.add(endDateInputPanel);

		final VerticalPanel deviceInputPanel = new VerticalPanel();
		deviceInputPanel.setTitle("Select the users here");
		deviceInputPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		final Label userLabel = new Label("Available User(s)");
		userLabel.setTitle("Select the users");
		deviceInputPanel.add(userLabel);
		final VerticalPanel userPanel = new VerticalPanel();
		proxyService.getAvailableUsers(new BusyShowAsyncCallback<List<String>>(
				new AsyncCallback<List<String>>() {

					public void onSuccess(List<String> result) {
						if (result == null) {
							final Label errorLabel = new Label("Users empty.");
							errorLabel
									.setTitle("Problem in retrieving the user list or the list is empty.");
							userPanel.add(errorLabel);
							return;
						}
						for (final String user : result) {
							if (!user.equalsIgnoreCase("admin")) {
								final CheckBox cbox = new CheckBox(user);
								cbox.setValue(true);
								userPanel.add(cbox);
							}
						}
					}

					public void onFailure(Throwable caught) {
					}
				}));

		userLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (userPanel.getWidget(0) instanceof Label)
					return;
				for (int i = 0; i < userPanel.getWidgetCount(); i++) {
					CheckBox cbox = (CheckBox) userPanel.getWidget(i);
					cbox.setValue(false);
				}
			}
		});

		final ScrollPanel userScroll = new ScrollPanel(userPanel);
		userScroll.setSize("140px", "140px");
		deviceInputPanel.add(userScroll);
		if (isAdmin)
			userInputPanel.add(deviceInputPanel);

		final VerticalPanel locationInputPanel = new VerticalPanel();
		locationInputPanel.setTitle("Select the location here");
		locationInputPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		final Label locationLabel = new Label("Available Location(s)");
		locationLabel.setTitle("Select the location");
		locationInputPanel.add(locationLabel);
		proxyService
				.getAvailableLocations(new BusyShowAsyncCallback<List<String>>(
						new AsyncCallback<List<String>>() {

							public void onSuccess(List<String> result) {
								if (result == null) {
									final Label errorLabel = new Label(
											"Locations empty.");
									errorLabel
											.setTitle("Problem in retrieving the location list or the list is empty.");
									locationInputPanel.add(errorLabel);
									return;
								}
								for (final String location : result) {
									final CheckBox cbox = new CheckBox(location);
									cbox.setValue(true);
									locationInputPanel.add(cbox);
								}
							}

							public void onFailure(Throwable caught) {
							}
						}));
		locationLabel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (locationInputPanel.getWidget(1) instanceof Label)
					return;
				for (int i = 1; i < userPanel.getWidgetCount(); i++) {
					CheckBox cbox = (CheckBox) locationInputPanel.getWidget(i);
					cbox.setValue(false);
				}
			}
		});
		if (isAdmin)
			userInputPanel.add(locationInputPanel);

		final Button searchButton = new Button("Search");
		searchButton.setTitle("This will search for images.");
		userInputPanel.add(searchButton);
		rootPanel.add(userInputPanel);

		final TextBox heightInputBox = new TextBox();
		heightInputBox.setTitle("Input the height in pixels");
		heightInputBox.setWidth("50px");
		userInputPanel.add(new Label("Height"));
		userInputPanel.add(heightInputBox);

		final TextBox widthInputBox = new TextBox();
		widthInputBox.setTitle("Input the width in pixels");
		widthInputBox.setWidth("50px");
		userInputPanel.add(new Label("Width"));
		userInputPanel.add(widthInputBox);

		final Tree selectTree = new Tree();
		selectTree.setAnimationEnabled(true);
		selectTree.setTitle("Select the period");
		rootPanel.add(selectTree);

		final VerticalPanel imageViewPanel = new VerticalPanel();
		imageViewPanel.setSpacing(10);
		imageViewPanel.setTitle("Display all the images matching your query.");
		rootPanel.add(imageViewPanel);

		searchButton.addClickHandler(new ClickHandler() {

			final DateTimeFormat sdf = DateTimeFormat
					.getFormat("yyyy-MM-dd hh:mm:ss");

			@Override
			public void onClick(ClickEvent event) {
				if (startDatePicker.getValue() == null
						|| endDatePicker.getValue() == null) {
					Window.alert("Please check the input.");
					return;
				}

				if (locationInputPanel.getWidget(1) instanceof Label) {
					Window.alert("No locations available.");
					return;
				}

				if (userPanel.getWidget(0) instanceof Label) {
					Window.alert("No users available.");
					return;
				}

				// Prepare the search request
				final SearchRequest req = new SearchRequest();
				req.setDateStart(sdf.format(startDatePicker.getValue()));
				req.setDateEnd(sdf.format(endDatePicker.getValue()));
				if (isAdmin)
					req.setUserList(getCheckedBoxes(userPanel, 0));
				else
					req.setUserList(Arrays.asList(new String[] { userid }));
				if (isAdmin)
					req.setLocationList(getCheckedBoxes(locationInputPanel, 1));

				proxyService.answerUserQuery(req,
						new BusyShowAsyncCallback<List<User>>(
								new AsyncCallback<List<User>>() {

									@Override
									public void onFailure(Throwable caught) {
									}

									@Override
									public void onSuccess(List<User> result) {
										if (result == null) {
											Window.alert("Empty set returned.");
											return;
										}

										imageViewPanel.clear();

										populateTree(result);

									}

									private Tree populateTree(List<User> result) {
										selectTree.clear();
										for (final User user : result) {
											final TreeItem userItem = selectTree
													.addTextItem(user
															.getUserid());
											String lastSeenYear = null, lastSeenMonth = null;
											TreeItem imgSetItem = null;
											for (final ImageSet imgSet : user
													.getImgSet()) {
												final String[] dateValues = imgSet
														.getDay().split("-");
												if (lastSeenMonth == null
														|| !dateValues[1]
																.equals(lastSeenMonth)
														|| !dateValues[0]
																.equals(lastSeenYear)) {
													lastSeenMonth = dateValues[1];
													lastSeenYear = dateValues[0];
													imgSetItem = userItem.addTextItem(month[Integer
															.parseInt(lastSeenMonth)]
															+ "-"
															+ lastSeenYear);
												}
												final Label itemLabel = new Label(
														"Day-"
																+ dateValues[2]
																+ "("
																+ imgSet.getCount()
																+ ")");
												itemLabel
														.addClickHandler(new ClickHandler() {
															@Override
															public void onClick(
																	ClickEvent event) {
																imageViewPanel
																		.clear();
																int index = 1;
																final FlowPanel viewFlow = new FlowPanel();
																viewFlow.setSize(
																		"600px",
																		"300px");
																viewFlow.setStyleName("flowPanel_inline");
																for (final Image image : imgSet
																		.getImgs()) {
																	final HorizontalPanel imagePanel = new HorizontalPanel();
																	imagePanel
																			.setSpacing(5);
																	imagePanel
																			.add(new Label(
																					""
																							+ index));
																	imagePanel
																			.setStyleName("flowPanel_inline");
																	final com.google.gwt.user.client.ui.Image img = new com.google.gwt.user.client.ui.Image(
																			image.getResourcePath());
																	img.setWidth(getValue(widthInputBox)
																			+ "px");
																	img.setHeight(getValue(heightInputBox)
																			+ "px");
																	img.setStyleName("flowPanel_inline");
																	imagePanel
																			.add(img);
																	final HTML metaLabel = new HTML(
																			"Device Id - <b>"
																					+ user.getUserid()
																					+ "</b></br>Tags - <i>"
																					+ image.getLocation()
																					+ "</i></br>Snaped at - <i><b>"
																					+ image.getSnapedAt()
																					+ "</i></b>");
																	metaLabel
																			.setStyleName("flowPanel_inline");
																	imagePanel
																			.add(metaLabel);
																	viewFlow.add(imagePanel);
																	index++;
																}
																imageViewPanel
																		.add(viewFlow);
															}

															public int getValue(
																	TextBox tbox) {
																if (tbox.getValue()
																		.trim()
																		.length() == 0)
																	return 300;
																try {
																	return Integer
																			.parseInt(tbox
																					.getValue());
																} catch (Exception e) {
																	return 300;
																}
															}

														});
												imgSetItem.addItem(itemLabel);
											}
										}
										return selectTree;
									}

									final String[] month = new String[] {
											"Jan", "Feb", "Mar", "Apr", "May",
											"Jun", "Jul", "Aug", "Sep", "Oct",
											"Nov", "Dec" };

								}));

			}

			public List<String> getCheckedBoxes(final VerticalPanel panel,
					int startIndex) {
				final List<String> elements = new ArrayList<String>();
				for (int widgetIndex = startIndex; widgetIndex < panel
						.getWidgetCount(); widgetIndex++) {
					final CheckBox cbox = (CheckBox) panel
							.getWidget(widgetIndex);
					if (cbox.getValue())
						elements.add(cbox.getText());
				}
				return elements;
			}
		});

		RootPanel.get().add(rootPanel);
	}

	private void showLoginScreen() {
		final DialogBox loginBox = new DialogBox();
		loginBox.setAnimationEnabled(true);
		loginBox.setGlassEnabled(true);
		loginBox.setTitle("This panel allows you to login to the system.");

		final DecoratedTabPanel tabPanel = new DecoratedTabPanel();
		tabPanel.setAnimationEnabled(true);

		final FlexTable loginFT = new FlexTable();
		loginFT.setTitle("This is the panel to key-in your login credentials.");
		loginFT.setCellPadding(5);
		loginFT.setCellSpacing(5);
		final Label userNameLabel = new Label("Your device id*");
		userNameLabel.setTitle("Enter your user name");
		loginFT.getCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		loginFT.setWidget(0, 0, userNameLabel);
		final TextBox loginUserNameTB = new TextBox();
		loginUserNameTB.setTitle("Enter your valid user name.");
		loginFT.setWidget(0, 1, loginUserNameTB);
		final Label loginUserPwdLabel = new Label("Password*");
		loginUserPwdLabel
				.setTitle("Enter your account password created from SignUp tab.");
		loginFT.getCellFormatter().setAlignment(1, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		loginFT.setWidget(1, 0, loginUserPwdLabel);
		final PasswordTextBox loginUserPwdTB = new PasswordTextBox();
		loginUserPwdTB
				.setTitle("Enter your password set while signing up a new account.");
		loginFT.getCellFormatter().setAlignment(1, 1,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		loginFT.setWidget(1, 1, loginUserPwdTB);
		final Button loginUserLoginButton = new Button("LOGIN");
		loginUserLoginButton
				.setTitle("Clicking this will take you directly to our system.");
		loginUserLoginButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (loginUserNameTB.getText().trim().length() == 0
						|| loginUserPwdTB.getText().trim().length() == 0) {
					Window.alert("Please check the input.");
					return;
				}

				proxyService.login(new UserLoginRequest(loginUserNameTB
						.getText().trim(), loginUserPwdTB.getText().trim()),
						new BusyShowAsyncCallback<UserLoginResponse>(
								new AsyncCallback<UserLoginResponse>() {

									public void onSuccess(
											UserLoginResponse result) {
										if (!result.getCode().equals("200")) {
											Window.alert(result.getMessage());
											return;
										}
										Window.alert("logged in successfully.");
										RootPanel.get().clear();
										processUI(result.getUserid());
										loginBox.hide();
									}

									public void onFailure(Throwable caught) {

									}
								}));

			}
		});
		loginFT.getFlexCellFormatter().setColSpan(2, 0, 2);
		loginFT.getCellFormatter().setAlignment(2, 0,
				HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);

		final HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.add(loginUserLoginButton);
		loginFT.setWidget(2, 0, buttonPanel);
		tabPanel.add(loginFT, "Login");

		final FlexTable signupFT = new FlexTable();
		signupFT.setTitle("This is the panel to sign-up for a new account.");
		signupFT.setCellPadding(5);
		signupFT.setCellSpacing(5);
		final Label desiredUsernameLabel = new Label("Device id*");
		desiredUsernameLabel.setTitle("Enter your user ID");
		signupFT.getCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		signupFT.setWidget(0, 0, desiredUsernameLabel);
		final TextBox userTB = new TextBox();
		userTB.setTitle("Enter a user name.");
		signupFT.setWidget(0, 1, userTB);

		final Label nameLabel = new Label("User name*");
		desiredUsernameLabel.setTitle("Enter your name");
		signupFT.getCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		signupFT.setWidget(1, 0, nameLabel);
		final TextBox nameTB = new TextBox();
		nameTB.setTitle("Enter a user name.");
		signupFT.setWidget(1, 1, nameTB);

		final Label clickDelayLabel = new Label("Click Delay (seconds)*");
		clickDelayLabel.setTitle("Click Delay in seconds");
		signupFT.getCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		signupFT.setWidget(2, 0, clickDelayLabel);
		final TextBox clickDelayTB = new TextBox();
		clickDelayTB.setTitle("Enter the click delay.");
		signupFT.setWidget(2, 1, clickDelayTB);

		final Label uploadIntervalLabel = new Label("Upload Delay (seconds)*");
		uploadIntervalLabel.setTitle("Upload Delay in seconds");
		signupFT.getCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		signupFT.setWidget(3, 0, uploadIntervalLabel);
		final TextBox uploadDelayTB = new TextBox();
		uploadDelayTB.setTitle("Enter the upload delay.");
		signupFT.setWidget(3, 1, uploadDelayTB);

		final Label desiredPwdLabel = new Label("Password*");
		desiredPwdLabel
				.setTitle("Enter the password for the system. (We don't provide provision to recover your password. )");
		signupFT.getCellFormatter().setAlignment(4, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		signupFT.setWidget(4, 0, desiredPwdLabel);
		final PasswordTextBox desiredPwdTB = new PasswordTextBox();
		desiredPwdTB
				.setTitle("Enter a password unpredictable to your colleagues. (We don't provide provision to recover your password. )");
		signupFT.getCellFormatter().setAlignment(4, 1,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		signupFT.setWidget(4, 1, desiredPwdTB);
		final Label confirmedPwdLabel = new Label("Confirm your Password*");
		confirmedPwdLabel
				.setTitle("Confirm the password you entered before. (We don't provide provision to recover your password.)");
		signupFT.getCellFormatter().setAlignment(5, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		signupFT.setWidget(5, 0, confirmedPwdLabel);
		final PasswordTextBox confirmPwdTB = new PasswordTextBox();
		confirmPwdTB
				.setTitle("Enter the same thing given in the above text box. (We don't provide provision to recover your password.)");
		signupFT.getCellFormatter().setAlignment(5, 1,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		signupFT.setWidget(5, 1, confirmPwdTB);

		final Button signupLoginButton = new Button("Sign Up & Login");
		signupLoginButton
				.setTitle("Clicking this will create an account for you and take you directly to our system.");
		signupFT.getFlexCellFormatter().setColSpan(6, 0, 2);
		signupFT.getCellFormatter().setAlignment(6, 0,
				HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		signupFT.setWidget(6, 0, signupLoginButton);
		signupLoginButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (userTB.getText().trim().length() == 0
						|| nameTB.getText().trim().length() == 0
						|| clickDelayTB.getText().trim().length() == 0
						|| uploadDelayTB.getText().trim().length() == 0
						|| desiredPwdTB.getText().trim().length() == 0
						|| confirmPwdTB.getText().trim().length() == 0) {
					Window.alert("Please check the input.");
					return;
				}

				if (!desiredPwdTB.getText().trim()
						.equals(confirmPwdTB.getText().trim())) {
					Window.alert("password mismatch.");
					return;
				}

				proxyService.signup(
						new UserSignUpRequest(userTB.getText().trim(), nameTB
								.getText().trim(), desiredPwdTB.getText(),
								clickDelayTB.getText().trim(), uploadDelayTB
										.getText().trim()),
						new BusyShowAsyncCallback<GenericResponse>(
								new AsyncCallback<GenericResponse>() {

									public void onSuccess(GenericResponse result) {
										if (!result.getCode().equals("200")) {
											Window.alert(result.getMessage());
											return;
										}
										Window.alert("account created successfully.");
										RootPanel.get().clear();
										processUI(userTB.getText().trim());
										loginBox.hide();
									}

									public void onFailure(Throwable caught) {

									}
								}));

			}
		});

		tabPanel.add(signupFT, "Signup");

		tabPanel.selectTab(0);
		loginBox.add(tabPanel);
		loginBox.center();
	}
}