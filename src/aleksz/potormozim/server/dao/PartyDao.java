package aleksz.potormozim.server.dao;

import java.util.Set;

import aleksz.potormozim.client.domain.Party;

/**
 *
 * @author aleksz
 *
 */
public interface PartyDao {

  Party getParty(String name);

  void saveParty(Party party);

  Set<Party> getAllParties();

  void deleteParty(String id);

  void deleteParticipant(String partyName, String participantName);
}
