package aleksz.potormozim.server;

import java.util.Set;

import org.gwtwidgets.server.spring.GWTSpringController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.PartyService;
import aleksz.potormozim.server.dao.PartyDao;
import aleksz.utils.client.DateRange;

/**
 * The server side implementation of the RPC service.
 */

@Controller
@RequestMapping("/app/gwt/party.rpc")
public class PartyServiceImpl extends GWTSpringController implements PartyService {

  private static final long serialVersionUID = 8329725839315748023L;

  @Autowired
  private PartyDao partyDao;

  @Override
  public void deleteParty(String name) {
    partyDao.deleteParty(name);
  }

  @Override
  public void deleteParticipant(String partyName, String participantName) {
    partyDao.deleteParticipant(partyName, participantName);
  }

  @Override
  public boolean togglePayedStatus(String partyName, String participantName) {
    Party party = partyDao.getParty(partyName);
    Boolean res = null;

    for (Participant p : party.getParticipants()) {
      if (p.getName().equals(participantName)) {
        res = !p.isPayed();
        p.setPayed(res);
        break;
      }
    }

    partyDao.saveParty(party);
    return res;
  }

  @Override
  public void setPayedStatus(String partyName, String participantName, boolean payed) {
    Party party = partyDao.getParty(partyName);

    for (Participant p : party.getParticipants()) {
      if (p.getName().equals(participantName)) {
        p.setPayed(payed);
        break;
      }
    }

    partyDao.saveParty(party);
  }

  @Override
  public Participant addParticipant(String partyName, String participantName) {
    Party party = partyDao.getParty(partyName);
    Participant participant = party.addParticipant(participantName);
    partyDao.saveParty(party);
    return participant;
  }

  @Override
  public Set<Participant> getParticipants(String partyName) {
    return partyDao.getParty(partyName).getParticipants();
  }

  @Override
  public Party addParty(String name) {
    Party party = new Party(name);
    partyDao.saveParty(party);
    return party;
  }

  @Override
  public Set<Party> getAllPartiesDirty() {
    Set<Party> allParties = partyDao.getAllParties();

    for (Party p : allParties) {
      p.setParticipants(null);
    }

    return allParties;
  }

  @Override
  public void updatePartyDate(String partyName, DateRange date) {
    Party party = partyDao.getParty(partyName);
    party.setDate(date);
    partyDao.saveParty(party);
  }

  @Override
  public void updatePartyDescription(String partyName, String description) {
    Party party = partyDao.getParty(partyName);
    party.setDescription(description);
    partyDao.saveParty(party);
  }

  void setPartyDao(PartyDao partyDao) {
    this.partyDao = partyDao;
  }

  @Override
  public void keepAlive() {
    // TODO Auto-generated method stub
  }
}