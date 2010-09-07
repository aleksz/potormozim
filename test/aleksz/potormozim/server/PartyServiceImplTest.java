package aleksz.potormozim.server;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.PartyService;
import aleksz.potormozim.server.dao.PartyDao;
import aleksz.utils.client.Date;
import aleksz.utils.client.DateRange;



public class PartyServiceImplTest {

  private PartyService service;
  private PartyDao partyDao;

  @Before
  public void init() {
    service = new PartyServiceImpl();
    partyDao = createMock(PartyDao.class);
    ((PartyServiceImpl) service).setPartyDao(partyDao);
  }

  @Test
  public void updatePartyDescription() {
    Party party = new Party("p1");
    String newDescription = "newDescription";
    expect(partyDao.getParty(party.getName())).andReturn(party);
    partyDao.saveParty(party);
    replay(partyDao);
    service.updatePartyDescription(party.getName(), newDescription);
    assertEquals(newDescription, party.getDescription());
  }

  @Test
  public void updatePartyDate() {
    Party party = new Party("p1");
    DateRange newDate = new DateRange(new Date());
    expect(partyDao.getParty(party.getName())).andReturn(party);
    partyDao.saveParty(party);
    replay(partyDao);
    service.updatePartyDate(party.getName(), newDate);
    assertEquals(newDate, party.getDate());
  }

  @Test
  public void getPartiesWithoutParticipants() {
    Party p1 = new Party("p1");
    p1.addParticipant("part1");
    Party p2 = new Party("p2");
    p2.addParticipant("part2");
    Set<Party> expected = new TreeSet<Party>();
    expected.add(p1);
    expected.add(p2);
    expect(partyDao.getAllParties()).andReturn(expected);
    replay(partyDao);
    Set<Party> res = service.getAllPartiesDirty();
    assertEquals(expected, res);
    Iterator<Party> iterator = res.iterator();
    assertEquals(0, iterator.next().getParticipants().size());
    assertEquals(0, iterator.next().getParticipants().size());
  }

  @Test
  public void addParty() {
    Party party = new Party("p1");
    partyDao.saveParty(party);
    replay(partyDao);
    Party res = service.addParty(party.getName());
    assertEquals(party, res);
  }

  @Test
  public void getParticipants() {
    Party party = new Party("p1");
    Participant participant = new Participant("part1", party);
    party.addParticipant(participant);
    expect(partyDao.getParty(party.getName())).andReturn(party);
    replay(partyDao);
    Set<Participant> res = service.getParticipants(party.getName());
    assertEquals(1, res.size());
    assertEquals(participant, res.iterator().next());
  }

  @Test
  public void addParticipant() {
    Party party = new Party("p1");
    String participantName = "part1";
    expect(partyDao.getParty(party.getName())).andReturn(party);
    partyDao.saveParty(party);
    replay(partyDao);
    service.addParticipant(party.getName(), participantName);
    assertEquals(1, party.getParticipants().size());
    assertEquals(participantName, party.getParticipants().iterator().next().getName());
  }

  @Test
  public void togglePayedStatusToTrue() {
    Party party = new Party("p1");
    Participant participant = new Participant("part1", party);
    participant.setPayed(false);
    party.addParticipant(participant);
    expect(partyDao.getParty(party.getName())).andReturn(party);
    partyDao.saveParty(party);
    replay(partyDao);
    assertTrue(service.togglePayedStatus(party.getName(), participant.getName()));
    assertTrue(participant.isPayed());
  }

  @Test
  public void setPayedStatus() {
    Party party = new Party("p1");
    Participant participant = new Participant("part1", party);
    participant.setPayed(false);
    party.addParticipant(participant);
    expect(partyDao.getParty(party.getName())).andReturn(party);
    partyDao.saveParty(party);
    replay(partyDao);
    service.setPayedStatus(party.getName(), participant.getName(), true);
    assertTrue(participant.isPayed());
  }

  @Test
  public void deleteParticipant() {
    Party party = new Party("p1");
    Participant participant = new Participant("part1", party);
    party.addParticipant(participant);
    partyDao.deleteParticipant(party.getName(), participant.getName());
    replay(partyDao);
    service.deleteParticipant(party.getName(), participant.getName());
  }

  @Test
  public void deleteParty() {
    String partyName = "p1";
    partyDao.deleteParty(partyName);
    replay(partyDao);
    service.deleteParty(partyName);
  }
}
