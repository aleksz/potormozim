package aleksz.potormozim.client.widget.delete;

import aleksz.potormozim.client.App;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;


public class DeleteButton extends Image {

  private static final String DELETE_IMG = "/images/delete.gif";

  private static final String DELETE_GREY_IMG = "/images/delete_grey.gif";

  public DeleteButton() {
    setUrl(DELETE_GREY_IMG);
    setTitle(App.msgs.delete());
    setStyleName("deleteImg");
    registerHoverHandler();
    sinkEvents(Event.MOUSEEVENTS);
  }

  private void registerHoverHandler() {
    addMouseOverHandler(new MouseOverHandler() {

      @Override
      public void onMouseOver(MouseOverEvent event) {
        setUrl(DELETE_IMG);
      }

    });

    addMouseOutHandler(new MouseOutHandler() {

      @Override
      public void onMouseOut(MouseOutEvent event) {
        setUrl(DELETE_GREY_IMG);
      }

    });
  }
}
