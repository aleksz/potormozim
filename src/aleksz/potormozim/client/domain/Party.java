package aleksz.potormozim.client.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import aleksz.utils.client.Date;
import aleksz.utils.client.DateRange;



/**
 * @author aleksz
 */
public class Party implements Comparable<Party>, Serializable {

  private static final long serialVersionUID = 1L;

  protected String name;

  private DateRange date;

  private String description;

  private Set<Participant> participants = new HashSet<Participant>();

  @SuppressWarnings("unused")
  private Party() {
    //used by GWT serializer
  }

  public Party(String name) {
    this(name, null);
  }

  public Party(String name, DateRange date) {
    this(name, date, null);
  }

  public Party(String name, DateRange date, String description) {
    this.name = name;
    this.date = date;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public DateRange getDate() {
    return date;
  }

  public void setDate(DateRange date) {
    this.date = date;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Set<Participant> getParticipants() {
    return participants;
  }

  public void setParticipants(Set<Participant> participants) {
    this.participants = participants != null ? participants : new HashSet<Participant>();
  }

  public Participant addParticipant(String name) {
    Participant p = new Participant(name, this);
    this.participants.add(p);
    return p;
  }

  public void removeParticipant(String name) {
    Participant participantToRemove = null;

    for (Participant p : participants) {
      if (p.getName().equals(name)) {
        participantToRemove = p;
        break;
      }
    }

    if (participantToRemove != null) {
      participants.remove(participantToRemove);
    }
  }

  public void addParticipant(Participant participant) {
    this.participants.add(participant);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (obj == null) { return false; }
    if (getClass() != obj.getClass()) { return false; }

    return name.equals(((Party) obj).name);
  }

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public int compareTo(Party other) {
    if (this == other) { return 0; }
    if (other == null) { return 1; }

    Date today = new Date();

    if (this.date == null && other.getDate() == null) {
      return this.name.compareTo(other.getName());
    }

    if (this.date == null && other.getDate() != null) {
      boolean otherInFutureOrToday = !other.getDate().getFrom().before(today);
      return otherInFutureOrToday ? 1 : -1;
    }

    if (this.date != null && other.getDate() == null) {
      boolean thisInFutureOrToday = !this.date.getFrom().before(today);
      return thisInFutureOrToday ? -1 : 1;
    }

    boolean thisInFutureOrToday = !this.date.getFrom().before(today);
    boolean otherInFutureOrToday = !other.getDate().getFrom().before(today);

    if (thisInFutureOrToday && otherInFutureOrToday) {
      return this.date.getFrom().compareTo(other.getDate().getFrom());
    }

    if (thisInFutureOrToday && !otherInFutureOrToday) {
      return -1;
    }

    if (!thisInFutureOrToday && otherInFutureOrToday) {
      return 1;
    }

    return -1 * this.date.getFrom().compareTo(other.getDate().getFrom());
  }
}
