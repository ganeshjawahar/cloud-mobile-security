package com.cloud.secure.client;

import java.util.Date;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class BusyShowAsyncCallback<T> implements AsyncCallback<T> {
	AsyncCallback<T> callback;
	Boolean disableAll = false;
	final protected long operationStartTime;

	public BusyShowAsyncCallback(AsyncCallback<T> cb) {
		callback = cb;
		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
		this.operationStartTime = new Date().getTime();
		this.disableAll = true;
		showBusy();
	}

	public void onFailure(Throwable caught) {
		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
		callback.onFailure(caught);

		if (this.disableAll == true)
			popupPanel.hide();
	}

	public void onSuccess(T result) {
		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
		callback.onSuccess(result);
		if (this.disableAll == true)
			popupPanel.hide();
	}

	static private PopupPanel popupPanel = null;

	public void showBusy() {
		if (popupPanel == null) {
			popupPanel = new PopupPanel();
			popupPanel.getElement().getStyle().setZIndex(99);
			popupPanel.getElement().getStyle().setBorderWidth(0, Unit.PX);

			popupPanel.setWidth("30px");
			popupPanel.setGlassEnabled(true);
			popupPanel.setAnimationEnabled(true);
			popupPanel.setModal(true);
			VerticalPanel vp = new VerticalPanel();
			vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			vp.add(new Image("img/splash.gif"));
			popupPanel.add(vp);
		}
		popupPanel.show();
		popupPanel.center();
	}
}
