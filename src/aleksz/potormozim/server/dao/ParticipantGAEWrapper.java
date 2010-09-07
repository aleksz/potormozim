package aleksz.potormozim.server.dao;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ParticipantGAEWrapper implements Serializable {

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
  private String encodedKey;

  @SuppressWarnings("unused") //used by GAE
  @Persistent
  private PartyGAEWrapper party;

  @Persistent
  @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
  private String name;

  @Persistent
  private boolean isPayed;

  @SuppressWarnings("unused")
  private ParticipantGAEWrapper() {
    //used by GAE
  }

  public ParticipantGAEWrapper(Participant participant, PartyGAEWrapper party) {
    this.name = participant.getName();
    this.isPayed = participant.isPayed();
    this.party = party;
  }

  public ParticipantGAEWrapper(Participant participant) {
    this(participant, new PartyGAEWrapper(participant.getParty()));
  }

  public Participant getParticipant(Party p) {
    Participant participant = new Participant(name, p);
    participant.setPayed(isPayed);
    return participant;
  }
}