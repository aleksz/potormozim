package aleksz.potormozim.client.widget.participants;

import static junit.framework.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.MockPartyService;




public class ParticipantsReadOnlyPanelTest {

  private MockParticipantsReadOnlyView view;
  private ParticipantsReadOnlyPanel panel;
  private Party party;
  private MockPartyService partyService;

  @Before
  public void init() {
    party = new Party("testName");
    partyService = new MockPartyService();
    view = new MockParticipantsReadOnlyView();
    panel = new ParticipantsReadOnlyPanel(party, view, partyService);
  }

  @Test
  public void loadAllParticipants() {
    Set<Participant> expectedParticipants = new HashSet<Participant>(
        Arrays.asList(new Participant("p1", party), new Participant("p2", party)));
    partyService.expectGetParticipants(party.getName()).andReturn(expectedParticipants);
    panel.loadAllParticipants();

    assertEquals(expectedParticipants, view.renderedParticipants);
    assertEquals(2, view.nrOfParticipantsInHeader);
  }

  @Test
  public void testWorksWithoutLoading() {
    Set<Participant> participants = new HashSet<Participant>(Arrays.asList(
        new Participant("p1", party),
        new Participant("p2", party)));
    party.setParticipants(participants);
    panel = new ParticipantsReadOnlyPanel(party, view, partyService);

    assertEquals(2, view.renderedParticipants.size());
    assertEquals(2, view.nrOfParticipantsInHeader);
  }
}