package aleksz.potormozim.client.widget.participants;

import java.util.HashSet;
import java.util.Set;

import aleksz.potormozim.client.domain.Participant;


public class MockParticipantsReadOnlyView implements ParticipantsReadOnlyPanel.View {

  public Set<Participant> renderedParticipants = new HashSet<Participant>();
  public String newParticipantInputText;
  public int nrOfParticipantsInHeader;

  @Override
  public void addParticipant(Participant participant) {
    renderedParticipants.add(participant);
  }

  @Override
  public void updateHeader(int nrOfParticipants) {
    nrOfParticipantsInHeader = nrOfParticipants;
  }

  @Override
  public void removeParticipant(Participant participant) {
    renderedParticipants.remove(participant);
  }
}