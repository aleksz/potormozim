package aleksz.potormozim.client.widget.add;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Image;


public class AddButton extends Image implements HasClickHandlers {

  private static final String ADD_IMG = "/images/add.gif";

  private static final String ADD_GREY_IMG = "/images/add_grey.gif";

  private static final String CSS_STYLE = "addImg";

  private static final String CSS_STYLE_ENABLED = "enabled";

  public AddButton() {
    setStylePrimaryName(CSS_STYLE);
    disable("");
  }

  public void enable(String titleText) {
    setUrl(ADD_IMG);
    setTitle(titleText);
    addStyleDependentName(CSS_STYLE_ENABLED);
  }

  public void disable(String titleText) {
    setUrl(ADD_GREY_IMG);
    setTitle(titleText);
    removeStyleDependentName(CSS_STYLE_ENABLED);
  }
}