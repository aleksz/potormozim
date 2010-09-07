package aleksz.potormozim.client.widget.dollar;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.MockPartyService;
import aleksz.utils.mock.MockClickEvent;



public class DollarTest {

  private MockDollarView view;
  private MockPartyService partyService;
  private Participant participant;
  private Dollar dollar;

  @Before
  public void init() {
    view = new MockDollarView();
    partyService = new MockPartyService();
    participant = new Participant("testParticipant", new Party("testParty"));
    dollar = new Dollar(participant, view, partyService);
  }

  @Test
  public void toggleFromFalse() {
    participant.setPayed(false);

    partyService.expectSetPayedStatus(
        participant.getParty().getName(),
        participant.getName(),
        true);

    view.img.lastClickHandler.onClick(new MockClickEvent());

    assertTrue(view.positiveStatus);
    assertEquals(Dollar.DOLLAR_IMG, view.url);
    assertTrue(view.positiveStatusChangeMessage);
  }

  @Test
  public void toggleFromFalseFails() {
    participant.setPayed(false);

    partyService.expectSetPayedStatus(
        participant.getParty().getName(),
        participant.getName(),
        true).andFail();

    view.img.lastClickHandler.onClick(new MockClickEvent());

    assertFalse(view.positiveStatus);
    assertEquals(Dollar.DOLLAR_GREY_IMG, view.url);
  }

  @Test
  public void toggleFromTrue() {
    participant.setPayed(true);

    partyService.expectSetPayedStatus(
        participant.getParty().getName(),
        participant.getName(),
        false);

    view.img.lastClickHandler.onClick(new MockClickEvent());

    assertFalse(view.positiveStatus);
    assertEquals(Dollar.DOLLAR_GREY_IMG, view.url);
    assertFalse(view.positiveStatusChangeMessage);
  }

  @Test
  public void toggleStateByOtherWidget() {
    partyService.expectSetPayedStatus(
        participant.getParty().getName(),
        participant.getName(),
        true);
    dollar.toggleState();

    assertTrue(view.positiveStatus);
    assertEquals(Dollar.DOLLAR_IMG, view.url);
    assertTrue(view.positiveStatus);
  }

  @Test
  public void viewInitializedOnConstructorCall() {
    assertFalse(view.positiveStatus);
    assertEquals(Dollar.DOLLAR_GREY_IMG, view.url);
  }
}