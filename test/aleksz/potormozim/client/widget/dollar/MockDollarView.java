package aleksz.potormozim.client.widget.dollar;

import aleksz.utils.mock.MockHasClickHandlers;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;


public class MockDollarView implements Dollar.View {

  public MockHasClickHandlers img = new MockHasClickHandlers();
  public Boolean positiveStatus;
  public Boolean positiveStatusChangeMessage;
  public String url;

  @Override
  public HasClickHandlers getImg() {
    return img;
  }

  @Override
  public void setImgTitle(boolean positiveStatus) {
    this.positiveStatus = positiveStatus;
  }

  @Override
  public void setImgUrl(String url) {
    this.url = url;
  }

  @Override
  public Widget asWidget() {
    return null;
  }

  @Override
  public void showSuccesfulStatusChangeMessage(boolean positive) {
    positiveStatusChangeMessage = positive;
  }
}
