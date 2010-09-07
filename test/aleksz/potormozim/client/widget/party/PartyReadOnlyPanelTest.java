package aleksz.potormozim.client.widget.party;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.MockPartyService;
import aleksz.potormozim.client.widget.participants.ParticipantsReadOnlyPanel;
import aleksz.utils.client.Date;
import aleksz.utils.client.DateRange;


/**
 *
 * @author aleksz
 *
 */
public class PartyReadOnlyPanelTest {

  private Party party;
  private MockPartyReadOnlyView view;
  private MockPartyService partyService;


  @Before
  public void init() {
    partyService = new MockPartyService();
    view = new MockPartyReadOnlyView();
    party = new Party("party");
  }

  @Test
  public void setsReadOnlyParticipants() {
    PartyReadOnlyPanel partyReadOnlyPanel = new PartyReadOnlyPanel(party, view, partyService);
    assertTrue(partyReadOnlyPanel.participantsPanel instanceof ParticipantsReadOnlyPanel);
  }

  @Test
  public void isInitializedFromModel() {
    DateRange expectedDate = new DateRange(new Date());
    String expectedDescription = "the description";
    party.setDescription(expectedDescription);
    party.setDate(expectedDate);
    new PartyReadOnlyPanel(party, view, partyService);

    assertEquals(expectedDate, view.date);
    assertEquals(expectedDescription, view.description);
  }
}