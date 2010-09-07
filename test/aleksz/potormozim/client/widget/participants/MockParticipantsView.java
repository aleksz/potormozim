package aleksz.potormozim.client.widget.participants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.widget.dollar.Dollar;
import aleksz.potormozim.client.widget.dollar.MockDollarView;
import aleksz.utils.mock.MockHasClickHandlers;
import aleksz.utils.mock.MockHasKeyUpHandlers;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;


public class MockParticipantsView implements ParticipantsPanel.View {

  public Set<Participant> renderedParticipants = new HashSet<Participant>();
  public MockHasKeyUpHandlers newParticipantInput = new MockHasKeyUpHandlers();
  public String newParticipantInputText;
  public MockHasClickHandlers addButton = new MockHasClickHandlers();
  public Map<Participant, MockHasClickHandlers> delButtons = new HashMap<Participant, MockHasClickHandlers>();
  public boolean addButtonEnabled;
  public boolean participantAddedMessageShowed;
  public boolean participantDeletedMessageShowed;
  public int nrOfParticipantsInHeader;

  @Override
  public Dollar.View addParticipant(Participant participant) {
    renderedParticipants.add(participant);
    delButtons.put(participant, new MockHasClickHandlers());

    return new MockDollarView();
  }

  @Override
  public void updateHeader(int nrOfParticipants) {
    this.nrOfParticipantsInHeader = nrOfParticipants;
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
    return delButtons.get(participant);
  }

  @Override
  public void removeParticipant(Participant participant) {
    renderedParticipants.remove(participant);
  }

  @Override
  public String getNewParticipantInputValue() {
    return newParticipantInputText;
  }

  @Override
  public void setNewParticipantInputValue(String text) {
    newParticipantInputText = text;
  }

  @Override
  public void disableAddButton() {
    addButtonEnabled = false;
  }

  @Override
  public void enableAddButton() {
    addButtonEnabled = true;
  }

  @Override
  public void showNewParticipantAddedMessage() {
    participantAddedMessageShowed = true;
  }

  @Override
  public void showParticipantDeletedMessage() {
    participantDeletedMessageShowed = true;
  }
}