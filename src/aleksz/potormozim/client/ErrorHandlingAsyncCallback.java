package aleksz.potormozim.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class ErrorHandlingAsyncCallback<T> implements AsyncCallback<T> {

  @Override
  public void onFailure(Throwable caught) {
    if (GWT.isClient()) {
      App.systemMessaging.error(App.msgs.serverError());
    }
  }
}
