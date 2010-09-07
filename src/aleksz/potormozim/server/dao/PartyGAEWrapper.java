package aleksz.potormozim.server.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Serialized;

import com.google.appengine.api.datastore.Text;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.utils.client.DateRange;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PartyGAEWrapper  implements Serializable {

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
  private String encodedKey;

  @Persistent
  @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
  private String name;

  @Persistent
  @Serialized
  private DateRange date;

  @Persistent
  private Text description;

  @Persistent(mappedBy = "party")
  private Set<ParticipantGAEWrapper> participants = new HashSet<ParticipantGAEWrapper>();

  @SuppressWarnings("unused")
  private PartyGAEWrapper() {
    //used by GAE
  }

  public PartyGAEWrapper(Party party) {
    this.name = party.getName();
    this.date = party.getDate();
    if (party.getDescription() != null) {
      this.description = new Text(party.getDescription());
    }
    for (Participant p : party.getParticipants()) {
      participants.add(new ParticipantGAEWrapper(p, this));
    }
  }

  public Party getParty() {
    Party party = new Party(name, date);
    if (description != null) {
      party.setDescription(description.getValue());
    }
    for(ParticipantGAEWrapper wrapper : participants) {
      party.addParticipant(wrapper.getParticipant(party));
    }
    return party;
  }
}