package aleksz.potormozim.client.widget.party;

import aleksz.calendar.client.CalendarImpl;
import aleksz.potormozim.client.App;
import aleksz.potormozim.client.widget.participants.ParticipantsPanel;
import aleksz.potormozim.client.widget.participants.ParticipantsView;
import aleksz.utils.client.DateRange;

import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PartyView extends Composite implements PartyPanel.View {

  private CalendarImpl dateInput = new CalendarImpl();
  private VerticalPanel pane;
  private TextArea descriptionInput;
  private ParticipantsView participantsView;

  public PartyView() {
    this.participantsView = new ParticipantsView();
    initWidget(getPane());
  }

  @Override
  public HasValueChangeHandlers<DateRange> getDateAsEventSource() {
    return dateInput;
  }

  @Override
  public HasKeyUpHandlers getDescriptionAsEventSource() {
    return getDescriptionInput();
  }

  private TextArea getDescriptionInput() {

    if (descriptionInput != null) {
      return descriptionInput;
    }

    descriptionInput = new TextArea();
    descriptionInput.setStyleName("description");

    return descriptionInput;
  }

  private VerticalPanel getPane() {

    if (pane != null) {
      return pane;
    }

    pane = new VerticalPanel();
    HorizontalPanel dateAndDescriptionPanel = new HorizontalPanel();
    dateAndDescriptionPanel.add(dateInput.asWidget());
    dateAndDescriptionPanel.add(getDescriptionInput());
    pane.add(dateAndDescriptionPanel);
    pane.add(participantsView);
    pane.setStyleName("partyEventPanel");

    return pane;
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public ParticipantsPanel.View getParticipantsView() {
    return participantsView;
  }

  @Override
  public String getDescription() {
    return descriptionInput.getText();
  }

  @Override
  public void setDate(DateRange date) {
    dateInput.setValue(date);
  }

  @Override
  public void setDescription(String description) {
    descriptionInput.setText(description);
  }

  @Override
  public void markEvent(String label, DateRange range) {
    dateInput.markEvent(range, label);
  }

  @Override
  public void unmarkEvent(String label, DateRange range) {
    dateInput.unmarkEvent(range, label);
  }

  @Override
  public HandlerRegistration addPartyDateChangeHandler(PartyDateChangedHandler handler) {
    return addHandler(handler, PartyDateChangedEvent.getType());
  }

  @Override
  public void showDateInPastMessage() {
    App.systemMessaging.error(App.msgs.dateInPast());
  }

  @Override
  public void showDateUpdatedMessage() {
    App.systemMessaging.success(App.msgs.dateUpdated());
  }

  @Override
  public void showDescriptionUpdatedMessage() {
    App.systemMessaging.success(App.msgs.descriptionUpdated());
  }
}
