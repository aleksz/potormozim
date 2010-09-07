package aleksz.potormozim.client.widget.party;

import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.PartyServiceAsync;
import aleksz.potormozim.client.widget.participants.ParticipantsReadOnlyPanel;


public class PartyReadOnlyPanel extends AbstractPartyPanel<PartyReadOnlyPanel.View> {

  public interface View extends AbstractPartyPanel.View {
    ParticipantsReadOnlyPanel.View getParticipantsView();
  }

  public PartyReadOnlyPanel(Party party, PartyReadOnlyPanel.View view, PartyServiceAsync partyService) {
    super(view, party, partyService,
        new ParticipantsReadOnlyPanel(party, view.getParticipantsView(), partyService));
  }
}
