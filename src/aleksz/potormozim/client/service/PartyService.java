package aleksz.potormozim.client.service;

import java.util.Set;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.utils.client.DateRange;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("gwt/party.rpc")
public interface PartyService extends RemoteService {

  Party addParty(String name);

  void updatePartyDate(String eventName, DateRange date);

  void updatePartyDescription(String eventName, String description);

  Set<Party> getAllPartiesDirty();

  Set<Participant> getParticipants(String partyName);

  Participant addParticipant(String partyName, String participantName);

  boolean togglePayedStatus(String partyName, String participantName);

  void setPayedStatus(String partyName, String participantName, boolean payed);

  void deleteParty(String id);

  void deleteParticipant(String partyName, String participantName);

  void keepAlive();
}
