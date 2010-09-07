package aleksz.potormozim.client.widget.party;

import aleksz.potormozim.client.widget.participants.ParticipantsReadOnlyPanel;
import aleksz.potormozim.client.widget.participants.ParticipantsReadOnlyView;
import aleksz.utils.client.DateRange;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PartyReadOnlyView extends Composite implements PartyReadOnlyPanel.View {

  private Label date = new Label();
  private Label description = new Label();
  private VerticalPanel pane;
  private ParticipantsReadOnlyView participantsView;

  public PartyReadOnlyView() {
    this.participantsView = new ParticipantsReadOnlyView();
    initWidget(getPane());
  }

  private VerticalPanel getPane() {

    if (pane != null) {
      return pane;
    }

    pane = new VerticalPanel();
    HorizontalPanel dateAndDescriptionPanel = new HorizontalPanel();
    dateAndDescriptionPanel.add(date);
    dateAndDescriptionPanel.add(description);
    pane.add(dateAndDescriptionPanel);
    pane.add(participantsView);
    pane.setStyleName("partyEventPanel");

    return pane;
  }

  @Override
  public ParticipantsReadOnlyPanel.View getParticipantsView() {
    return participantsView;
  }

  @Override
  public void setDate(DateRange date) {
    this.date.setText(date.toString());
  }

  @Override
  public void setDescription(String description) {
    this.description.setText(description);
  }

  @Override
  public Widget asWidget() {
    return this;
  }
}
