package aleksz.potormozim.client.widget.party;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.MockPartyService;
import aleksz.utils.client.Date;
import aleksz.utils.client.DateRange;
import aleksz.utils.client.Key;
import aleksz.utils.mock.MockKeyUpEvent;
import aleksz.utils.mock.MockValueChangeEvent;


/**
 *
 * @author aleksz
 *
 */
public class PartyPanelTest {

  private PartyPanel panel;
  private MockPartyService partyService;
  private Party party;
  private MockPartyView view;

  @Before
  public void init() {
    partyService = new MockPartyService();
    view = new MockPartyView();
    party = new Party("party");
    panel = new PartyPanel(party, view, partyService);
  }

  @Test
  public void destroy() {
    PartyPanel anotherPanel = new PartyPanel(party, view, partyService);
    panel.addAsDateChangeHandlerTo(anotherPanel);
    panel.destroy();

    MockPartyView mockPartyView = (MockPartyView) anotherPanel.getView();
    assertEquals(0, mockPartyView.handlers.getHandlerCount(PartyDateChangedEvent.getType()));
  }

  @Test
  public void addAsDateChangeHandler() {
    PartyPanel anotherPanel = new PartyPanel(party, view, partyService);
    panel.addAsDateChangeHandlerTo(anotherPanel);

    MockPartyView mockPartyView = (MockPartyView) anotherPanel.getView();
    assertEquals(1, mockPartyView.handlers.getHandlerCount(PartyDateChangedEvent.getType()));
  }

  @Test
  public void handlesPartyDateChangedEvents() {
    String otherPartyName = "otherParty";
    DateRange prevValue = new DateRange(new Date());
    DateRange newValue = new DateRange(new Date().nextDay());
    view.markedEvents.put(otherPartyName, prevValue);
    panel.onPartyDateChange(new PartyDateChangedEvent(otherPartyName, prevValue, newValue));

    assertEquals(newValue, view.markedEvents.get(otherPartyName));
  }

  @Test
  public void handlesPartyDateChangedEventsWhenPrevDateIsNull() {
    String otherPartyName = "otherParty";
    DateRange newValue = new DateRange(new Date().nextDay());
    panel.onPartyDateChange(new PartyDateChangedEvent(otherPartyName, null, newValue));

    assertEquals(newValue, view.markedEvents.get(otherPartyName));
  }

  @Test
  public void firesEventToHandlersOnDateChange() {
    DateRange prevValue = new DateRange(new Date());
    DateRange newValue = new DateRange(new Date().nextDay());
    party.setDate(prevValue);

    MockPartyDateChangeHandler handler = new MockPartyDateChangeHandler();
    panel.addPartyDateChangeHandler(handler);

    partyService.expectUpdatePartyDate(party.getName(), newValue);
    view.dateEvents.lastValueChangeHandler.onValueChange(
        new MockValueChangeEvent<DateRange>(newValue));

    assertEquals(party.getName(), handler.partyName);
    assertEquals(prevValue, handler.prevValue);
    assertEquals(newValue, handler.newValue);
  }

  @Test
  public void isInitializedFromModel() {
    DateRange expectedDate = new DateRange(new Date());
    String expectedDescription = "the description";
    party.setDescription(expectedDescription);
    party.setDate(expectedDate);
    panel = new PartyPanel(party, view, partyService);

    assertEquals(expectedDate, view.date);
    assertEquals(expectedDescription, view.getDescription());
  }

  @Test
  public void changeDateToPastNotAllowed() {
    DateRange prevValue = new DateRange(new Date());
    party.setDate(prevValue);
    view.dateEvents.lastValueChangeHandler.onValueChange(new MockValueChangeEvent<DateRange>(new DateRange(new Date().prevDay())));
    assertEquals(prevValue, party.getDate());
    assertEquals(prevValue, view.date);
  }

  @Test
  public void changeDate() {
    DateRange newDate = new DateRange(new Date());

    partyService.expectUpdatePartyDate(party.getName(), newDate);
    view.dateEvents.lastValueChangeHandler.onValueChange(new MockValueChangeEvent<DateRange>(newDate));

    assertTrue(view.showedDateChangedMessage);
  }

  @Test
  public void changeDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    partyService.expectUpdatePartyDescription(party.getName(), textAfterKeyUp);
    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.ZERO));

    assertTrue(view.showedDescriptionChangedMessage);
  }

  @Test
  public void escOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.ESCAPE));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void f2OnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.F2));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void f4OnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.F4));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void f7OnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.F7));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void f8OnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.F8));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void f9OnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.F9));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void f10OnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.F10));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void f11OnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.F11));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void f12OnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.F12));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void insertOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.INSERT));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void homeOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.HOME));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void endOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.END));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void pgUpOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.PAGEUP));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void pgDnOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.PAGEDOWN));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void numLockOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.NUMLOCK));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void capsLockOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.CAPSLOCK));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void shiftOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.SHIFT));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void winOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.WIN));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void altOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.ALT));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void ctrlOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.CTRL));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void leftOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.LEFT));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void rightOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.RIGHT));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void upOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.UP));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void downOnDescription() {
    String textAfterKeyUp = "dfshdfgdgrt";

    view.description = textAfterKeyUp;
    view.descriptionEvents.lastKeyUpHandler.onKeyUp(new MockKeyUpEvent(Key.DOWN));

    assertFalse(view.showedDescriptionChangedMessage);
  }

  @Test
  public void loadAllParticipants() {
    Set<Participant> expectedParticipants = new HashSet<Participant>(
        Arrays.asList(new Participant("p1", party), new Participant("p2", party)));
    partyService.expectGetParticipants(party.getName()).andReturn(expectedParticipants);
    panel.loadAllParticipants();

    assertEquals(expectedParticipants, view.participants.renderedParticipants);
  }
}

class MockPartyDateChangeHandler implements PartyDateChangedHandler {

  public String partyName;
  public DateRange prevValue;
  public DateRange newValue;

  @Override
  public void onPartyDateChange(PartyDateChangedEvent event) {
    partyName = event.getPartyName();
    prevValue = event.getPrevDateRange();
    newValue = event.getNewDateRange();
  }
}