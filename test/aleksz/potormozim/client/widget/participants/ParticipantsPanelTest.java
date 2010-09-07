package aleksz.potormozim.client.widget.participants;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.MockPartyService;
import aleksz.utils.client.Key;
import aleksz.utils.mock.MockClickEvent;
import aleksz.utils.mock.MockKeyUpEvent;




public class ParticipantsPanelTest {

  private MockParticipantsView view;
  private ParticipantsPanel panel;
  private Party party;
  private MockPartyService partyService;

  @Before
  public void init() {
    party = new Party("testName");
    partyService = new MockPartyService();
    view = new MockParticipantsView();
    panel = new ParticipantsPanel(party, partyService, view);
  }

  @Test
  public void loadAllParticipants() {
    Set<Participant> expectedParticipants = new HashSet<Participant>(
        Arrays.asList(new Participant("p1", party), new Participant("p2", party)));
    partyService.expectGetParticipants(party.getName()).andReturn(expectedParticipants);
    panel.loadAllParticipants();

    assertEquals(expectedParticipants, view.renderedParticipants);
    assertEquals(2, view.nrOfParticipantsInHeader);
    assertFalse(view.addButtonEnabled);
  }

  @Test
  public void testEnterOnEmptyNewParticipantInput() {
    view.setNewParticipantInputValue("");
    view.newParticipantInput.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.ENTER));

    assertTrue(view.renderedParticipants.isEmpty());
    assertFalse(view.addButtonEnabled);
    assertEquals(0, view.nrOfParticipantsInHeader);
  }

  @Test
  public void testEnterOnValidNewParticipantInput() {
    Participant expected = new Participant("newTestParticipant", party);

    partyService.expectAddParticipant(party.getName(), expected.getName()).andReturn(expected);
    view.setNewParticipantInputValue(expected.getName());
    view.newParticipantInput.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.ENTER));

    assertEquals(1, view.renderedParticipants.size());
    assertEquals(expected, view.renderedParticipants.iterator().next());
    assertFalse(StringUtils.hasText(view.getNewParticipantInputValue()));
    assertEquals(1, view.nrOfParticipantsInHeader);
    assertFalse(view.addButtonEnabled);
    assertTrue(view.participantAddedMessageShowed);
  }

  @Test
  public void participantNotAddedIfRequestFails() {
    Participant expected = new Participant("newTestParticipant", party);

    partyService.expectAddParticipant(party.getName(), expected.getName()).andFail();
    view.setNewParticipantInputValue(expected.getName());
    view.newParticipantInput.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.ENTER));

    assertEquals(0, view.renderedParticipants.size());
    assertEquals(0, view.nrOfParticipantsInHeader);
    assertFalse(view.participantAddedMessageShowed);
  }

  @Test
  public void testAddButtonPressedWithInvalidName() {
    view.setNewParticipantInputValue("");
    view.addButton.lastClickHandler.onClick(new MockClickEvent());

    assertTrue(view.renderedParticipants.isEmpty());
    assertFalse(view.addButtonEnabled);
    assertEquals(0, view.nrOfParticipantsInHeader);
  }

  @Test
  public void testAddButtonPressedWithValidName() {
    Participant expected = new Participant("newTestParticipant", party);

    partyService.expectAddParticipant(party.getName(), expected.getName()).andReturn(expected);
    view.setNewParticipantInputValue(expected.getName());
    view.addButton.lastClickHandler.onClick(new MockClickEvent());

    assertEquals(1, view.renderedParticipants.size());
    assertEquals(expected, view.renderedParticipants.iterator().next());
    assertFalse(StringUtils.hasText(view.getNewParticipantInputValue()));
    assertEquals(1, view.nrOfParticipantsInHeader);
    assertFalse(view.addButtonEnabled);
    assertTrue(view.participantAddedMessageShowed);
  }

  @Test
  public void testEnterOnParticipantInputWithExistingName() {
    Participant p1 = new Participant("SameName", party);
    Participant p2 = new Participant("SameName", party);

    partyService.expectAddParticipant(party.getName(), p1.getName()).andReturn(p1);
    view.setNewParticipantInputValue(p1.getName());
    view.newParticipantInput.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.ENTER));

    view.setNewParticipantInputValue(p2.getName());
    view.newParticipantInput.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.ENTER));

    assertEquals(1, view.renderedParticipants.size());
    assertEquals(p1, view.renderedParticipants.iterator().next());
    assertEquals(p2.getName(), view.getNewParticipantInputValue());
    assertEquals(1, view.nrOfParticipantsInHeader);
    assertFalse(view.addButtonEnabled);
  }

  @Test
  public void testValidTextChangeOnNewParticipantInput() {
    String text = "nameAfterKeyPress";
    view.setNewParticipantInputValue(text);
    view.newParticipantInput.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.ONE));

    assertEquals(0, view.renderedParticipants.size());
    assertEquals(text, view.getNewParticipantInputValue());
    assertTrue(view.addButtonEnabled);
  }

  @Test
  public void testInvalidTextChangeOnNewParticipantInput() {
    String text = "         ";
    view.setNewParticipantInputValue(text);
    view.newParticipantInput.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.TWO));

    assertEquals(0, view.renderedParticipants.size());
    assertEquals(text, view.getNewParticipantInputValue());
    assertFalse(view.addButtonEnabled);
  }

  @Test
  public void testWorksWithoutLoading() {
    Set<Participant> participants = new HashSet<Participant>(Arrays.asList(
        new Participant("p1", party),
        new Participant("p2", party)));
    party.setParticipants(participants);
    panel = new ParticipantsPanel(party, partyService, view);

    assertEquals(2, view.renderedParticipants.size());
    assertFalse(StringUtils.hasText(view.getNewParticipantInputValue()));
    assertEquals(2, view.nrOfParticipantsInHeader);
    assertFalse(view.addButtonEnabled);
  }

  @Test
  public void testDelete() {
    Participant p2 = new Participant("p2", party);
    Participant p1 = new Participant("p1", party);
    Set<Participant> participants = new HashSet<Participant>(Arrays.asList(p1, p2));
    party.setParticipants(participants);

    panel = new ParticipantsPanel(party, partyService, view);
    partyService.expectDeleteParticipant(party.getName(), p2.getName());
    view.delButtons.get(p2).lastClickHandler.onClick(new MockClickEvent());

    assertEquals(1, view.renderedParticipants.size());
    assertEquals(p1, view.renderedParticipants.iterator().next());
    assertEquals(1, view.nrOfParticipantsInHeader);
    assertFalse(view.addButtonEnabled);
    assertTrue(view.participantDeletedMessageShowed);
  }
}