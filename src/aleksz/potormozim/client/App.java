package aleksz.potormozim.client;

import aleksz.potormozim.client.i18n.AppMessages;
import aleksz.potormozim.client.service.PartyService;
import aleksz.potormozim.client.service.PartyServiceAsync;
import aleksz.potormozim.client.widget.list.PartyListPanel;
import aleksz.potormozim.client.widget.list.PartyListView;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class App implements EntryPoint {

  private PartyServiceAsync partyService = GWT.create(PartyService.class);
  public static AppMessages msgs = GWT.create(AppMessages.class);
  public static SystemMessaging systemMessaging = new SystemMessagingWidget();

  public void onModuleLoad() {
    PartyListView mainView = new PartyListView();
    new PartyListPanel(mainView, partyService).loadAllParties();
    RootPanel.get("partyList").add(mainView);

    Timer timer = new Timer() {

      @Override
      public void run() {
        partyService.keepAlive(new AsyncCallback<Void>() {

          @Override
          public void onSuccess(Void result) {
            // TODO Auto-generated method stub

          }

          @Override
          public void onFailure(Throwable caught) {
            // TODO Auto-generated method stub

          }
        });
      }

    };

    timer.scheduleRepeating(2 * 60 * 1000);
  }
}
