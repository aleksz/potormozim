package aleksz.potormozim.client.widget.participants;

import java.util.Set;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.PartyServiceAsync;


public class ParticipantsReadOnlyPanel extends AbstractParticipantsPanel<ParticipantsReadOnlyPanel.View>{

  public interface View extends AbstractParticipantsPanel.View {
    void addParticipant(Participant participant);
  }

  public ParticipantsReadOnlyPanel(Party party, View view,
      PartyServiceAsync partyService) {
    super(party, view, partyService);
  }

  protected void addAllParticipants(Set<Participant> participants) {

    if (participants.isEmpty()) {
      updateHeaderInView();
    }

    super.addAllParticipants(participants);
  }

  protected void addParticipant(Participant p) {
    super.addParticipant(p);
    view.addParticipant(p);
  }
}
