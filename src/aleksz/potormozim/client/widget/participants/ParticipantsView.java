package aleksz.potormozim.client.widget.participants;

import java.util.HashMap;
import java.util.Map;

import aleksz.potormozim.client.App;
import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.widget.add.AddButton;
import aleksz.potormozim.client.widget.delete.DeleteButton;
import aleksz.potormozim.client.widget.dollar.Dollar;
import aleksz.potormozim.client.widget.dollar.DollarView;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


public class ParticipantsView extends Composite implements ParticipantsPanel.View {

  private DisclosurePanel panel;
  private Grid grid = new Grid();
  private VerticalPanel content;
  private TextBox newParticipantInput = new TextBox();
  private AddButton addButton = new AddButton();
  private Map<Participant, HasClickHandlers> deleteButtons = new HashMap<Participant, HasClickHandlers>();
  private Map<Participant, Integer> participantsToRows = new HashMap<Participant, Integer>();

  public ParticipantsView() {
    initWidget(getPane());
  }

  @Override
  public HasKeyUpHandlers getNewParticipantInputAsEventSource() {
    return newParticipantInput;
  }

  @Override
  public HasClickHandlers getAddButton() {
    return addButton;
  }

  @Override
  public HasClickHandlers getDeleteButton(Participant participant) {
    return deleteButtons.get(participant);
  }

  @Override
  public Dollar.View addParticipant(Participant participant) {
    int rowNr = grid.getRowCount();
    grid.resize(rowNr + 1, 3);
    grid.setText(rowNr, 0, participant.getName());
    Dollar.View dollarView = new DollarView();
    grid.setWidget(rowNr, 1, dollarView.asWidget());
    DeleteButton del = new DeleteButton();
    deleteButtons.put(participant, del);
    grid.setWidget(rowNr, 2, del);
    participantsToRows.put(participant, rowNr);

    return dollarView;
  }

  @Override
  public void removeParticipant(Participant participant) {
    int row = participantsToRows.remove(participant);
    grid.removeRow(row);

    for (Map.Entry<Participant, Integer> e : participantsToRows.entrySet()) {
      if (e.getValue() > row) {
        e.setValue(e.getValue() - 1);
      }
    }
  }

  private DisclosurePanel getPane() {

    if (panel != null) {
      return panel;
    }

    panel = new DisclosurePanel("", false);
    panel.setContent(getContent());

    return panel;
  }

  private VerticalPanel getContent() {

    if (content != null) {
      return content;
    }

    content = new VerticalPanel();
    content.add(grid);
    FlowPanel newParticipantGroup = new FlowPanel();
    newParticipantGroup.setStyleName("newParticipantGroup");
    newParticipantGroup.add(newParticipantInput);
    newParticipantGroup.add(addButton);
    content.add(newParticipantGroup);

    return content;
  }

  @Override
  public String getNewParticipantInputValue() {
    return newParticipantInput.getText();
  }

  @Override
  public void setNewParticipantInputValue(String text) {
    newParticipantInput.setText(text);
  }

  @Override
  public void disableAddButton() {
    addButton.disable(App.msgs.participantsPanelDisabledAddButtonTitle());
  }

  @Override
  public void enableAddButton() {
    addButton.enable(App.msgs.participantsPanelEnabledAddButtonTitle());
  }

  @Override
  public void showNewParticipantAddedMessage() {
    App.systemMessaging.success(App.msgs.participantAdded());
  }

  @Override
  public void showParticipantDeletedMessage() {
    App.systemMessaging.success(App.msgs.participantDeleted());
  }

  @Override
  public void updateHeader(int nrOfParticipants) {
    getPane().getHeaderTextAccessor().setText(App.msgs.participantsHeader(nrOfParticipants));
  }
}
