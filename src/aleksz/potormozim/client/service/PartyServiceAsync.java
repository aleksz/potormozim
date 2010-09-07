package aleksz.potormozim.client.service;

import java.util.Set;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.utils.client.DateRange;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PartyServiceAsync {

  void addParty(String partyName, AsyncCallback<Party> callback);

  void updatePartyDate(String eventName, DateRange date, AsyncCallback<Void> callback);

  void updatePartyDescription(String partyName, String description, AsyncCallback<Void> callback);

  void getAllPartiesDirty(AsyncCallback<Set<Party>> callback);

  void getParticipants(String partyName, AsyncCallback<Set<Participant>> callback);

  void addParticipant(String partyName, String participantName, AsyncCallback<Participant> callback);

  void togglePayedStatus(String partyName, String participantName, AsyncCallback<Boolean> callback);

  void deleteParty(String id, AsyncCallback<Void> callback);

  void deleteParticipant(String partyName, String participantName, AsyncCallback<Void> callback);

  void keepAlive(AsyncCallback<Void> callback);

  void setPayedStatus(String partyName, String participantName, boolean payed,
      AsyncCallback<Void> callback);
}
