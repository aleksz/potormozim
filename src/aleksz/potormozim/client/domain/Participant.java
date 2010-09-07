package aleksz.potormozim.client.domain;

import java.io.Serializable;

/**
 *
 * @author aleksz
 *
 */
public class Participant implements Serializable {

  private static final long serialVersionUID = 1L;

  protected String name;
  private boolean payed;
  protected Party party;

  protected Participant() {}

  public Participant(String name, Party party) {
    this.name = name;
    this.party = party;
  }

  public boolean isPayed() {
    return payed;
  }

  public void setPayed(boolean payed) {
    this.payed = payed;
  }

  public String getName() {
    return name;
  }

  public Party getParty() {
    return party;
  }

  @Override
  public String toString() {
    return this.name + " of " + party;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((party == null) ? 0 : party.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Participant other = (Participant) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (party == null) {
      if (other.party != null)
        return false;
    } else if (!party.equals(other.party))
      return false;
    return true;
  }
}
