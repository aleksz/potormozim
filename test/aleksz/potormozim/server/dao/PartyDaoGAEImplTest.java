package aleksz.potormozim.server.dao;

import java.util.Iterator;
import java.util.Set;

import org.datanucleus.store.appengine.JDOTestCase;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.utils.client.Date;
import aleksz.utils.client.DateRange;




public class PartyDaoGAEImplTest extends JDOTestCase {

  private PartyDao dao;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    dao = new PartyDaoGAEImpl();
  }

  public void testSavePartySavesNewParty() {
    dao.saveParty(new Party("p1"));
    assertEquals(1, countForClass(PartyGAEWrapper.class));
  }

  public void testSavePartyUpdatesParty() {
    Party p = new Party("p1");
    makePersistentInTxn(new PartyGAEWrapper(p));
    dao.saveParty(p);
    assertEquals(1, countForClass(PartyGAEWrapper.class));
  }

  public void testSavePartyUpdatesParticipants() {
    Party party = new Party("testName1");
    party.addParticipant("p1");
    PartyGAEWrapper partyWrapper = new PartyGAEWrapper(party);
    makePersistentInTxn(partyWrapper);
    dao.saveParty(party);
    assertEquals(1, countForClass(ParticipantGAEWrapper.class));
  }

  public void testDeleteParticipant() {
    Party party = new Party("testName1");
    Participant participant = new Participant("p1", party);
    party.addParticipant(participant);
    PartyGAEWrapper partyWrapper = new PartyGAEWrapper(party);
    makePersistentInTxn(partyWrapper);
    dao.deleteParticipant(party.getName(), participant.getName());
    assertEquals(0, countForClass(ParticipantGAEWrapper.class));
  }

  public void testSavePartyAcceptsDescriptionLongerThan500Chars() {
    Party party = new Party("p1");
    for (int i = 0; i < 501; i++) {
      party.setDescription(party.getDescription() + "x");
    }
    dao.saveParty(party);
  }

  public void testGetParty() {
    Party party = new Party("testName");
    PartyGAEWrapper wrapper = new PartyGAEWrapper(party);
    makePersistentInTxn(wrapper);
    assertEquals(party, dao.getParty(party.getName()));
  }

  public void testGetPartyRetrievesPartyWithDate() {
    Party party = new Party("testName1", new DateRange(new Date()));
    PartyGAEWrapper wrapper = new PartyGAEWrapper(party);
    makePersistentInTxn(wrapper);
    Party res = dao.getParty(party.getName());
    assertEquals(party.getDate(), res.getDate());
  }

  public void testGetPartyRetrievesPartyWithParticipants() {
    Party party = new Party("testName1");
    party.addParticipant("p1");
    PartyGAEWrapper partyWrapper = new PartyGAEWrapper(party);
    makePersistentInTxn(partyWrapper);
    Party res = dao.getParty(party.getName());
    assertEquals(1, res.getParticipants().size());
  }

  public void testDeleteParty() {
    Party party = new Party("testName1");
    PartyGAEWrapper wrapper = new PartyGAEWrapper(party);
    makePersistentInTxn(wrapper);
    dao.deleteParty(party.getName());
    assertEquals(0, countForClass(PartyGAEWrapper.class));
  }

  public void testGetAllParties() {
    PartyGAEWrapper wrapper = new PartyGAEWrapper(new Party("testName1"));
    makePersistentInTxn(wrapper);
    Set<Party> allParties = dao.getAllParties();
    assertEquals(1, allParties.size());
  }

  public void testGetAllPartiesReturnsPartiesWithDates() {
    DateRange expectedDate = new DateRange(new Date());
    Party party = new Party("testName1", expectedDate);
    PartyGAEWrapper wrapper = new PartyGAEWrapper(party);
    makePersistentInTxn(wrapper);
    Set<Party> allParties = dao.getAllParties();
    assertEquals(expectedDate, allParties.iterator().next().getDate());
  }

  public void testGetAllPartiesReturnsSortedSet() {
    Date currentDate = new Date();
    DateRange today = new DateRange(currentDate);
    DateRange future = new DateRange(currentDate.nextDay());
    DateRange past = new DateRange(currentDate.prevDay());

    Party p1 = new Party("p1");
    Party p2 = new Party("p2", today);
    Party p3 = new Party("p3", future);
    Party p4 = new Party("p4", past);

    makePersistentInTxn(new PartyGAEWrapper(p1));
    makePersistentInTxn(new PartyGAEWrapper(p2));
    makePersistentInTxn(new PartyGAEWrapper(p3));
    makePersistentInTxn(new PartyGAEWrapper(p4));

    Set<Party> allParties = dao.getAllParties();
    Iterator<Party> iterator = allParties.iterator();

    assertEquals(p2, iterator.next());
    assertEquals(p3, iterator.next());
    assertEquals(p1, iterator.next());
    assertEquals(p4, iterator.next());
  }

//  public void testGetAllPartiesFetchesParticipants() {
//    PartyGAEWrapper party = new PartyGAEWrapper("testParty1");
//    Set<ParticipantGAEWrapper> participants = new HashSet<ParticipantGAEWrapper>();
//    participants.add(new ParticipantGAEWrapper("testParticipant1", party));
//    party.setParticipants(participants);
//    makePersistentInTxn(party);
//
//    Set<Party> allParties = dao.getAllParties();
//
//    assertEquals(1, allParties.iterator().next().getParticipants().size());
//  }
}
