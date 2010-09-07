package aleksz.potormozim.client.widget.dollar;

import aleksz.potormozim.client.ErrorHandlingAsyncCallback;
import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.service.PartyServiceAsync;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author aleksz
 *
 */
public class Dollar {

  public static final String DOLLAR_GREY_IMG = "/images/dollar_grey.png";
  public static final String DOLLAR_IMG = "/images/dollar.png";

  public interface View {
    HasClickHandlers getImg();
    void setImgUrl(String url);
    void setImgTitle(boolean positive);
    void showSuccesfulStatusChangeMessage(boolean positive);
    Widget asWidget();
  }

  private final PartyServiceAsync partyService;
  private View view;
  private final Participant model;

  public Dollar(Participant participant, View view, PartyServiceAsync partyService) {
    this.model = participant;
    this.partyService = partyService;
    bindDisplay(view);
    updateDisplay();
  }

  private void bindDisplay(View view) {
    this.view = view;
    this.view.getImg().addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        toggleState();
      }
    });
  }

  public void toggleState() {

    final boolean newState = !model.isPayed();
    model.setPayed(newState);
    updateDisplay();

    partyService.setPayedStatus(model.getParty().getName(), model.getName(), newState,
        new ErrorHandlingAsyncCallback<Void>() {

          @Override
          public void onSuccess(Void result) {
            view.showSuccesfulStatusChangeMessage(newState);
          }

          @Override
          public void onFailure(Throwable caught) {
            model.setPayed(!newState);
            updateDisplay();
            super.onFailure(caught);
          }

        });
  }

  private void updateDisplay() {
    view.setImgUrl(model.isPayed() ? DOLLAR_IMG : DOLLAR_GREY_IMG);
    view.setImgTitle(model.isPayed());
  }
}