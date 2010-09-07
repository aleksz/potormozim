package aleksz.potormozim.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Label;

/**
 *
 * @author aleksz
 *
 */
public class SystemMessagingWidget extends DecoratedPopupPanel implements SystemMessaging {

  private static final int TIMEOUT = 5000; // milliseconds

  private static final String ERROR_STYLE_NAME = "errorNotificationDecorator";

  private static final String SUCCESS_STYLE_NAME = "successNotificationDecorator";

  private static final String COMMON_STYLE_NAME = "notificationDecorator";

  private Timer timer;

  public SystemMessagingWidget() {
    super(true, false);
    setStyleName(COMMON_STYLE_NAME);

    timer = new Timer() {
      @Override
      public void run() {
        hide();
      }
    };
  }

  @Override
  public void error(final String text) {
    removeStyleName(SUCCESS_STYLE_NAME);
    addStyleName(ERROR_STYLE_NAME);
    message(text);
  }

  @Override
  public void success(final String text) {
    removeStyleName(ERROR_STYLE_NAME);
    addStyleName(SUCCESS_STYLE_NAME);
    message(text);
  }

  private void message(final String text) {
    setWidget(new Label(text));

    setPopupPositionAndShow(new PositionCallback() {

      @Override
      public void setPosition(int offsetWidth, int offsetHeight) {
        setPopupPosition(Window.getClientWidth() / 2 - offsetWidth / 2, Window.getScrollTop());
      }
    });
    show();

    timer.schedule(TIMEOUT);
  }
}