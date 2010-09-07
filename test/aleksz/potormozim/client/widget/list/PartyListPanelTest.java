package aleksz.potormozim.client.widget.list;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.MockPartyService;
import aleksz.potormozim.client.widget.loading.LoadingPopup;
import aleksz.potormozim.client.widget.party.MockPartyReadOnlyView;
import aleksz.potormozim.client.widget.party.MockPartyView;
import aleksz.potormozim.client.widget.party.PartyDateChangedEvent;
import aleksz.potormozim.client.widget.party.PartyPanel;
import aleksz.potormozim.client.widget.party.PartyReadOnlyPanel.View;
import aleksz.utils.client.Date;
import aleksz.utils.client.DateRange;
import aleksz.utils.client.Key;
import aleksz.utils.mock.MockClickEvent;
import aleksz.utils.mock.MockHasClickHandlers;
import aleksz.utils.mock.MockHasKeyUpHandlers;
import aleksz.utils.mock.MockHasSelectionHandlers;
import aleksz.utils.mock.MockHasValueChangeHandlers;
import aleksz.utils.mock.MockKeyUpEvent;
import aleksz.utils.mock.MockSelectionEvent;
import aleksz.utils.mock.MockValueChangeEvent;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.shared.HandlerManager;



public class PartyListPanelTest {

  private MockView view;
  private MockPartyService partyService;
  private PartyListPanel panel;

  @Before
  public void init() {
    partyService = new MockPartyService();
    view = new MockView();
    panel = new PartyListPanel(view, partyService);
  }

  @Test
  public void eventUnmarkedOnDelete() {
    Party p1 = new Party("p1", new DateRange(new Date()));
    Party p2 = new Party("p2", new DateRange(new Date().nextDay()));

    partyService.expectAddParty(p1.getName()).andReturn(p1);
    partyService.expectAddParty(p2.getName()).andReturn(p2);
    panel.addParty(p1.getName());
    panel.addParty(p2.getName());

    partyService.expectDeleteParty(p2.getName());
    panel.deleteParty(p2);

    Map<String, DateRange> markedEvents = ((MockPartyView) panel.getPartyPanel(p1).getView()).markedEvents;

    assertFalse(markedEvents.toString(), markedEvents.containsKey(p2.getName()));
  }

  @Test
  public void allPartiesWithDatesMarkedOnInit() {
    Party p1 = new Party("p1", new DateRange(new Date()));
    Party p2 = new Party("p2", new DateRange(new Date().nextDay()));
    Party p3 = new Party("p3", new DateRange(new Date().nextMonth()));
    Party p4 = new Party("p4");
    Party p5 = new Party("p5", new DateRange(new Date().prevDay()));

    PartyListPanel plp = new PartyListPanel(view, partyService);

    partyService.expectGetAllPartiesDirty().andReturn(new HashSet<Party>(asList(p1, p2, p3, p4, p5)));
    partyService.expectGetParticipants(p5.getName()).andReturn(Collections.<Participant>emptySet());
    partyService.expectGetParticipants(p4.getName()).andReturn(Collections.<Participant>emptySet());
    partyService.expectGetParticipants(p3.getName()).andReturn(Collections.<Participant>emptySet());
    partyService.expectGetParticipants(p2.getName()).andReturn(Collections.<Participant>emptySet());
    partyService.expectGetParticipants(p1.getName()).andReturn(Collections.<Participant>emptySet());
    plp.loadAllParties();

    Map<String, DateRange> markedEvents = ((MockPartyView) plp.getPartyPanel(p1).getView()).markedEvents;

    assertTrue(markedEvents.toString(), markedEvents.containsKey(p2.getName()));
    assertTrue(markedEvents.toString(), markedEvents.containsKey(p3.getName()));
    assertTrue(markedEvents.toString(), markedEvents.containsKey(p5.getName()));
    assertEquals(3, markedEvents.size());

    markedEvents = ((MockPartyView) plp.getPartyPanel(p2).getView()).markedEvents;
    assertTrue(markedEvents.toString(), markedEvents.containsKey(p1.getName()));
    assertTrue(markedEvents.toString(), markedEvents.containsKey(p3.getName()));
    assertTrue(markedEvents.toString(), markedEvents.containsKey(p5.getName()));
    assertEquals(3, markedEvents.size());

    markedEvents = ((MockPartyView) plp.getPartyPanel(p3).getView()).markedEvents;
    assertTrue(markedEvents.toString(), markedEvents.containsKey(p1.getName()));
    assertTrue(markedEvents.toString(), markedEvents.containsKey(p2.getName()));
    assertTrue(markedEvents.toString(), markedEvents.containsKey(p5.getName()));
    assertEquals(3, markedEvents.size());
  }

  @Test
  public void testPartyPanelsWiredWithHandlersOnPartyDelete() {
    Party p1 = new Party("p1");
    Party p2 = new Party("p2");
    Party p3 = new Party("p3");
    Party p4 = new Party("p4");

    PartyListPanel plp = new PartyListPanel(view, partyService);

    partyService.expectGetAllPartiesDirty().andReturn(new HashSet<Party>(asList(p1, p2, p3, p4)));
    partyService.expectGetParticipants(p4.getName()).andReturn(Collections.<Participant>emptySet());
    partyService.expectGetParticipants(p3.getName()).andReturn(Collections.<Participant>emptySet());
    partyService.expectGetParticipants(p2.getName()).andReturn(Collections.<Participant>emptySet());
    partyService.expectGetParticipants(p1.getName()).andReturn(Collections.<Participant>emptySet());
    plp.loadAllParties();

    partyService.expectDeleteParty(p4.getName());
    plp.deleteParty(p4);

    HandlerManager handlers = ((MockPartyView) plp.getPartyPanel(p1).getView()).handlers;
    assertEquals(2, handlers.getHandlerCount(PartyDateChangedEvent.getType()));

    handlers = ((MockPartyView) plp.getPartyPanel(p2).getView()).handlers;
    assertEquals(2, handlers.getHandlerCount(PartyDateChangedEvent.getType()));

    handlers = ((MockPartyView) plp.getPartyPanel(p3).getView()).handlers;
    assertEquals(2, handlers.getHandlerCount(PartyDateChangedEvent.getType()));
  }

  @Test
  public void testPartyPanelsWiredWithHandlersOnPartyAdded() {
    Party p1 = new Party("p1");
    Party p2 = new Party("p2");
    Party p3 = new Party("p3");
    Party p4 = new Party("p4");

    PartyListPanel plp = new PartyListPanel(view, partyService);

    partyService.expectGetAllPartiesDirty().andReturn(new HashSet<Party>(asList(p1, p2, p3)));
    partyService.expectGetParticipants(p3.getName()).andReturn(Collections.<Participant>emptySet());
    partyService.expectGetParticipants(p2.getName()).andReturn(Collections.<Participant>emptySet());
    partyService.expectGetParticipants(p1.getName()).andReturn(Collections.<Participant>emptySet());
    plp.loadAllParties();
    partyService.expectAddParty(p4.getName()).andReturn(p4);
    plp.addParty(p4.getName());

    HandlerManager handlers = ((MockPartyView) plp.getPartyPanel(p1).getView()).handlers;
    assertEquals(3, handlers.getHandlerCount(PartyDateChangedEvent.getType()));

    handlers = ((MockPartyView) plp.getPartyPanel(p2).getView()).handlers;
    assertEquals(3, handlers.getHandlerCount(PartyDateChangedEvent.getType()));

    handlers = ((MockPartyView) plp.getPartyPanel(p3).getView()).handlers;
    assertEquals(3, handlers.getHandlerCount(PartyDateChangedEvent.getType()));

    handlers = ((MockPartyView) plp.getPartyPanel(p4).getView()).handlers;
    assertEquals(3, handlers.getHandlerCount(PartyDateChangedEvent.getType()));
  }

  @Test
  public void testPartyPanelsWiredWithHandlersOnLoad() {
    Party p1 = new Party("p1");
    Party p2 = new Party("p2");
    Party p3 = new Party("p3");
    Party p4 = new Party("p4");

    PartyListPanel plp = new PartyListPanel(view, partyService);

    partyService.expectGetAllPartiesDirty().andReturn(new HashSet<Party>(asList(p1, p2, p3, p4)));
    partyService.expectGetParticipants(p4.getName()).andReturn(Collections.<Participant>emptySet());
    partyService.expectGetParticipants(p3.getName()).andReturn(Collections.<Participant>emptySet());
    partyService.expectGetParticipants(p2.getName()).andReturn(Collections.<Participant>emptySet());
    partyService.expectGetParticipants(p1.getName()).andReturn(Collections.<Participant>emptySet());
    plp.loadAllParties();

    HandlerManager handlers = ((MockPartyView) plp.getPartyPanel(p1).getView()).handlers;
    assertEquals(3, handlers.getHandlerCount(PartyDateChangedEvent.getType()));

    handlers = ((MockPartyView) plp.getPartyPanel(p2).getView()).handlers;
    assertEquals(3, handlers.getHandlerCount(PartyDateChangedEvent.getType()));

    handlers = ((MockPartyView) plp.getPartyPanel(p3).getView()).handlers;
    assertEquals(3, handlers.getHandlerCount(PartyDateChangedEvent.getType()));

    handlers = ((MockPartyView) plp.getPartyPanel(p4).getView()).handlers;
    assertEquals(3, handlers.getHandlerCount(PartyDateChangedEvent.getType()));
  }

  @Test
  public void readOnlyParties() {
    Party party1 = new Party("party1");
    party1.setDate(new DateRange(new Date().prevDay()));
    Party party2 = new Party("party2");
    Participant participant1 = new Participant("par1", party1);
    Participant participant2 = new Participant("par2", party2);
    Set<Party> expected = new HashSet<Party>(asList(party1, party2));

    partyService.expectGetAllPartiesDirty().andReturn(expected);
    partyService.expectGetParticipants(party1.getName()).andReturn(singleton(participant1));
    partyService.expectGetParticipants(party2.getName()).andReturn(singleton(participant2));
    panel.loadAllParties();

    assertEquals(1, view.parties.size());
    assertEquals(1, view.readOnlyParties.size());
    assertTrue(view.parties.contains(party2));
    assertTrue(view.readOnlyParties.contains(party1));
    assertEquals(singleton(participant1), view.readOnlyPartyViews.get(0).participants.renderedParticipants);
    assertEquals(singleton(participant2), view.partyViews.get(0).participants.renderedParticipants);
    assertNull(view.shownParty);
  }

  @Test
  public void loadAllParties() {
    Party party1 = new Party("party1");
    Party party2 = new Party("party2");
    Participant participant1 = new Participant("par1", party1);
    Participant participant2 = new Participant("par2", party2);
    Set<Party> expected = new HashSet<Party>(asList(party1, party2));

    partyService.expectGetAllPartiesDirty().andReturn(expected);
    partyService.expectGetParticipants(party1.getName()).andReturn(singleton(participant1));
    partyService.expectGetParticipants(party2.getName()).andReturn(singleton(participant2));
    panel.loadAllParties();

    assertEquals(2, view.parties.size());
    assertEquals(expected, view.parties);
    assertEquals(singleton(participant1), view.partyViews.get(0).participants.renderedParticipants);
    assertEquals(singleton(participant2), view.partyViews.get(1).participants.renderedParticipants);
    assertNull(view.shownParty);
  }

  @Test
  public void loadAllPartiesShowsLoadingPopup() {
    partyService.expectGetAllPartiesDirty().andFail();
    panel.loadAllParties();
    assertTrue(view.showingPopup);
  }

  @Test
  public void loadAllPartiesHidesLoadingPopup() {
    partyService.expectGetAllPartiesDirty().andReturn(new HashSet<Party>());
    panel.loadAllParties();
    assertFalse(view.showingPopup);
  }

  @Test
  public void testEnterOnEmptyNewPartyInput() {
    view.setNewPartyInputValue("");
    view.newPartyInput.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.ENTER));

    assertTrue(view.parties.isEmpty());
    assertFalse(view.addButtonEnabled);
    assertNull(view.shownParty);
  }

  @Test
  public void testEnterOnNewPartyInputWithFirstDigit() {
    view.setNewPartyInputValue("2sdfdf");
    view.newPartyInput.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.ENTER));

    assertTrue(view.parties.isEmpty());
    assertFalse(view.addButtonEnabled);
    assertNull(view.shownParty);
  }

  @Test
  public void testEnterOnValidNewPartyInput() {
    Party expected = new Party("testParty");
    partyService.expectAddParty(expected.getName()).andReturn(expected);
    view.setNewPartyInputValue(expected.getName());
    view.newPartyInput.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.ENTER));

    assertEquals(singleton(expected), view.parties);
    assertFalse(StringUtils.hasText(view.getNewPartyInputValue()));
    assertFalse(view.addButtonEnabled);
    assertTrue(view.showedPartyAddedMessage);
    assertEquals(expected, view.shownParty);
  }

  @Test
  public void testAddButtonPressedWithInvalidName() {
    view.setNewPartyInputValue("");
    view.addButton.lastClickHandler.onClick(new MockClickEvent());

    assertTrue(view.parties.isEmpty());
    assertFalse(view.addButtonEnabled);
    assertNull(view.shownParty);
  }

  @Test
  public void testAddButtonPressedWithValidName() {
    Party expected = new Party("newTestParty");

    partyService.expectAddParty(expected.getName()).andReturn(expected);
    view.setNewPartyInputValue(expected.getName());
    view.addButton.lastClickHandler.onClick(new MockClickEvent());

    assertEquals(singleton(expected), view.parties);
    assertFalse(StringUtils.hasText(view.getNewPartyInputValue()));
    assertFalse(view.addButtonEnabled);
    assertTrue(view.showedPartyAddedMessage);
    assertEquals(expected, view.shownParty);
  }

  @Test
  public void testEnterOnPartyInputWithExistingName() {
    Party p1 = new Party("SameName");
    Party p2 = new Party("SameName");

    partyService.expectAddParty(p1.getName()).andReturn(p1);
    view.setNewPartyInputValue(p1.getName());
    view.newPartyInput.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.ENTER));

    view.setNewPartyInputValue(p2.getName());
    view.newPartyInput.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.ENTER));

    assertEquals(singleton(p1), view.parties);
    assertEquals(p2.getName(), view.getNewPartyInputValue());
    assertFalse(view.addButtonEnabled);
  }

  @Test
  public void testValidTextChangeOnNewParticipantInput() {
    String text = "nameAfterKeyPress";
    view.setNewPartyInputValue(text);
    view.newPartyInput.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.FOUR));

    assertEquals(0, view.parties.size());
    assertEquals(text, view.getNewPartyInputValue());
    assertTrue(view.addButtonEnabled);
    assertNull(view.shownParty);
  }

  @Test
  public void testInvalidTextChangeOnNewParticipantInput() {
    String text = "         ";
    view.setNewPartyInputValue(text);
    view.newPartyInput.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.FIVE));

    assertEquals(0, view.parties.size());
    assertEquals(text, view.getNewPartyInputValue());
    assertFalse(view.addButtonEnabled);
    assertNull(view.shownParty);
  }

  @Test
  public void testDelete() {
    Party p1 = new Party("p1");
    Party p2 = new Party("p2");


    partyService.expectAddParty(p1.getName()).andReturn(p1);
    partyService.expectAddParty(p2.getName()).andReturn(p2);
    panel.addParty(p1.getName());
    panel.addParty(p2.getName());

    partyService.expectDeleteParty(p2.getName());
    view.delButtons.get(p2).lastClickHandler.onClick(new MockClickEvent());

    assertEquals(singleton(p1), view.parties);
    assertFalse(view.addButtonEnabled);
    assertTrue(view.showedPartyDeletedMessage);
    assertNull(view.shownParty);
  }

  @Test
  public void selectPartySetsHistory() {
    Party p1 = new Party("p1");
    Party p2 = new Party("p2");

    partyService.expectAddParty(p1.getName()).andReturn(p1);
    partyService.expectAddParty(p2.getName()).andReturn(p2);
    panel.addParty(p1.getName());
    panel.addParty(p2.getName());

    view.stack.lastSelectHandler.onSelection(new MockSelectionEvent<String>(p1.getName()));

    assertEquals(p1.getName(), view.historyItem);
  }

  @Test
  public void selectNewPartySetsHistory() {
    Party p1 = new Party("p1");
    Party p2 = new Party("p2");

    partyService.expectAddParty(p1.getName()).andReturn(p1);
    partyService.expectAddParty(p2.getName()).andReturn(p2);
    panel.addParty(p1.getName());
    panel.addParty(p2.getName());

    view.stack.lastSelectHandler.onSelection(new MockSelectionEvent<String>("new"));
    assertEquals("new", view.historyItem);
  }

  @Test
  public void bookmarkNavigationToOtherParty() {
    Party p1 = new Party("p1");
    Party p2 = new Party("p2");

    partyService.expectAddParty(p1.getName()).andReturn(p1);
    partyService.expectAddParty(p2.getName()).andReturn(p2);
    panel.addParty(p1.getName());
    panel.addParty(p2.getName());

    assertEquals(p2, view.shownParty);

    view.history.lastValueChangeHandler.onValueChange(new MockValueChangeEvent<String>(p1.getName()));
    assertEquals(p1, view.shownParty);
  }

  @Test
  public void bookmarkNavigationToNewInput() {
    Party p1 = new Party("p1");
    Party p2 = new Party("p2");

    partyService.expectAddParty(p1.getName()).andReturn(p1);
    partyService.expectAddParty(p2.getName()).andReturn(p2);
    panel.addParty(p1.getName());
    panel.addParty(p2.getName());

    assertEquals(p2, view.shownParty);

    view.history.lastValueChangeHandler.onValueChange(new MockValueChangeEvent<String>("new"));
    assertNull(view.shownParty);
  }
}

class MockView implements PartyListPanel.View {

  public Set<Party> parties = new HashSet<Party>();
  public Set<Party> readOnlyParties = new HashSet<Party>();
  public List<MockPartyView> partyViews = new ArrayList<MockPartyView>();
  public List<MockPartyReadOnlyView> readOnlyPartyViews = new ArrayList<MockPartyReadOnlyView>();
  public MockHasClickHandlers addButton = new MockHasClickHandlers();
  public MockHasKeyUpHandlers newPartyInput = new MockHasKeyUpHandlers();
  public String newPartyInputText;
  public Map<Party, MockHasClickHandlers> delButtons = new HashMap<Party, MockHasClickHandlers>();
  public Party shownParty;
  public MockHasSelectionHandlers<String> stack = new MockHasSelectionHandlers<String>();
  public String historyItem;
  public MockHasValueChangeHandlers<String> history = new MockHasValueChangeHandlers<String>();
  public Boolean addButtonEnabled;
  public boolean showedPartyAddedMessage;
  public boolean showedPartyDeletedMessage;
  public boolean showingPopup;

  @Override
  public PartyPanel.View addParty(Party party) {
    parties.add(party);
    MockPartyView mockPartyView = new MockPartyView();
    partyViews.add(mockPartyView);
    delButtons.put(party, new MockHasClickHandlers());

    return mockPartyView;
  }

  @Override
  public HasClickHandlers getAddButton() {
    return addButton;
  }

  @Override
  public HasKeyUpHandlers getNewPartyInputAsEventSource() {
    return newPartyInput;
  }

  @Override
  public void showNewPartyInput() {
    shownParty = null;
  }

  @Override
  public void showParty(Party party) {
    shownParty = party;
  }

  @Override
  public HasClickHandlers getDeleteButton(Party party) {
    return delButtons.get(party);
  }

  @Override
  public void deleteParty(Party party) {
    parties.remove(party);
    delButtons.remove(party);
  }

  @Override
  public HasSelectionHandlers<String> getStackPanel() {
    return stack;
  }

  @Override
  public void addHistoryItem(String item) {
    historyItem = item;
  }

  @Override
  public HasValueChangeHandlers<String> getHistory() {
    return history;
  }

  @Override
  public LoadingPopup getLoadingPopup() {
    return new LoadingPopup() {

      @Override
      public void hide() {
        showingPopup = false;
      }

      @Override
      public void center() {
        showingPopup = true;
      }
    };
  }

  @Override
  public View addReadOnlyParty(Party party) {
    readOnlyParties.add(party);
    MockPartyReadOnlyView mockPartyView = new MockPartyReadOnlyView();
    readOnlyPartyViews.add(mockPartyView);
    delButtons.put(party, new MockHasClickHandlers());

    return mockPartyView;
  }

  @Override
  public String getNewPartyInputValue() {
    return newPartyInputText;
  }

  @Override
  public void setNewPartyInputValue(String value) {
    newPartyInputText = value;
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
  public void showNewPartySavedMessage() {
    showedPartyAddedMessage = true;
  }

  @Override
  public void showPartyDeletedMessage() {
    showedPartyDeletedMessage = true;
  }
}
