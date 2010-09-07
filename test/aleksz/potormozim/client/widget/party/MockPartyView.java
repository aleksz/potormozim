package aleksz.potormozim.client.widget.party;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import aleksz.potormozim.client.widget.participants.MockParticipantsView;
import aleksz.potormozim.client.widget.participants.ParticipantsPanel;
import aleksz.utils.client.DateRange;
import aleksz.utils.mock.MockHasKeyUpHandlers;
import aleksz.utils.mock.MockHasValueChangeHandlers;

import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;


public class MockPartyView implements PartyPanel.View {

  public MockHasValueChangeHandlers<DateRange> dateEvents = new MockHasValueChangeHandlers<DateRange>();
  public MockHasKeyUpHandlers descriptionEvents = new MockHasKeyUpHandlers();
  public MockParticipantsView participants = new MockParticipantsView();
  public String description;
  public DateRange date;
  public Map<String, DateRange> markedEvents = new HashMap<String, DateRange>();
  public HandlerManager handlers = new HandlerManager(this);
  public boolean showedDateChangedMessage;
  public boolean showedDescriptionChangedMessage;
  public boolean showedDateInPastMessage;

  @Override
  public HasValueChangeHandlers<DateRange> getDateAsEventSource() {
    return dateEvents;
  }

  @Override
  public HasKeyUpHandlers getDescriptionAsEventSource() {
    return descriptionEvents;
  }

  @Override
  public ParticipantsPanel.View getParticipantsView() {
    return participants;
  }

  @Override
  public Widget asWidget() {
    return null;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDate(DateRange date) {
    this.date = date;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public void markEvent(String label, DateRange range) {
    markedEvents.put(label, range);
  }

  @Override
  public void unmarkEvent(String label, DateRange range) {
    assertNotNull("Unmark non-marked event", markedEvents.remove(label));
  }

  @Override
  public HandlerRegistration addPartyDateChangeHandler(PartyDateChangedHandler handler) {
    return handlers.addHandler(PartyDateChangedEvent.getType(), handler);
  }

  @Override
  public void fireEvent(GwtEvent<?> event) {
    handlers.fireEvent(event);
  }

  @Override
  public void showDateInPastMessage() {
    showedDateInPastMessage = true;
  }

  @Override
  public void showDateUpdatedMessage() {
    showedDateChangedMessage = true;
  }

  @Override
  public void showDescriptionUpdatedMessage() {
    showedDescriptionChangedMessage = true;
  }
}
