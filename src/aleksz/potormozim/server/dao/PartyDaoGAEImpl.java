package aleksz.potormozim.server.dao;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.springframework.stereotype.Repository;

import aleksz.potormozim.client.domain.Party;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 *
 * @author aleksz
 *
 */
@Repository
public class PartyDaoGAEImpl extends AbstractDao implements PartyDao {

  private static final Logger LOG = Logger.getLogger(PartyDaoGAEImpl.class.getName());

  @Override
  public void deleteParty(String name) {

    PersistenceManager pm = getPM();

    try {

      PartyGAEWrapper p = pm.getObjectById(PartyGAEWrapper.class, name);
      pm.deletePersistent(p);

      LOG.info("Party " + name + " deleted");

    } finally {
      pm.close();
    }
  }


  @Override
  public void deleteParticipant(String partyName, String participantName) {

    PersistenceManager pm = getPM();

    try {

      KeyFactory.Builder builder =
        new KeyFactory.Builder(PartyGAEWrapper.class.getSimpleName(), partyName);
      builder.addChild(ParticipantGAEWrapper.class.getSimpleName(), participantName);
      Key key = builder.getKey();

      ParticipantGAEWrapper p = pm.getObjectById(ParticipantGAEWrapper.class, key);
      pm.deletePersistent(p);

      LOG.info("Participant " + participantName + " deleted from " + partyName);

    } finally {
      pm.close();
    }

  }

  @Override
  public Party getParty(String name) {
    PersistenceManager pm = getPM();

    try {
      PartyGAEWrapper o = pm.getObjectById(PartyGAEWrapper.class, name);
      return o.getParty();
    } finally {
      pm.close();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Set<Party> getAllParties() {
    PersistenceManager pm = getPM();

    TreeSet<Party> res = new TreeSet<Party>();

    try {
      Collection<PartyGAEWrapper> searchRes =
        (Collection<PartyGAEWrapper>) pm.newQuery(PartyGAEWrapper.class).execute();

      for (PartyGAEWrapper p : searchRes) {
        res.add(p.getParty());
      }

    } finally {
      pm.close();
    }

    return res;
  }

  @Override
  public void saveParty(Party party) {
    PersistenceManager pm = getPM();

    try {
      pm.makePersistent(new PartyGAEWrapper(party));
    } finally {
      pm.close();
    }
  }
}
