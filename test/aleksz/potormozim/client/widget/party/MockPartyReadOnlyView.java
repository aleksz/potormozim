package aleksz.potormozim.client.widget.party;

import aleksz.potormozim.client.widget.participants.MockParticipantsReadOnlyView;
import aleksz.potormozim.client.widget.participants.ParticipantsReadOnlyPanel;
import aleksz.utils.client.DateRange;

import com.google.gwt.user.client.ui.Widget;


public class MockPartyReadOnlyView implements PartyReadOnlyPanel.View {

  public MockParticipantsReadOnlyView participants = new MockParticipantsReadOnlyView();
  public String description;
  public DateRange date;

  @Override
  public ParticipantsReadOnlyPanel.View getParticipantsView() {
    return participants;
  }

  @Override
  public Widget asWidget() {
    return null;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public void setDate(DateRange date) {
    this.date = date;
  }
}
