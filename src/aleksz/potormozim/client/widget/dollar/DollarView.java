package aleksz.potormozim.client.widget.dollar;

import aleksz.potormozim.client.App;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author aleksz
 *
 */
public class DollarView extends Composite implements Dollar.View {

  private Image img;

  public DollarView() {
    initWidget(getImg());
  }

  @Override
  public Image getImg() {

    if (img != null) {
      return img;
    }

    img = new Image();
    img.setStyleName("dollarImg");

    return img;
  }

  @Override
  public void setImgTitle(boolean positive) {
    getImg().setTitle(
        positive ? App.msgs.dollarPositiveStatus() : App.msgs.dollarNegativeStatus());
  }

  @Override
  public void setImgUrl(String url) {
    getImg().setUrl(url);
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public void showSuccesfulStatusChangeMessage(boolean positive) {
    App.systemMessaging.success(
        positive ? App.msgs.dollarStatusChangedPositive() : App.msgs.dollarStatusChangedNegative());
  }
}
