package aleksz.potormozim.client.widget.loading;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;


public class LoadingPopupWidget extends PopupPanel implements LoadingPopup {

  private final String IMG_PATH = "/images/loading.gif";

  private Image loading;

  public LoadingPopupWidget() {
    super(false, true);
    loading = new Image(IMG_PATH);
    setWidget(loading);
    setAnimationEnabled(false);
    setWidth("30px");
    setHeight("30px");
    setStyleName("loadingPopup");
  }
}
