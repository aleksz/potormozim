package aleksz.potormozim.client.service;

import java.util.Set;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.utils.client.DateRange;

import com.google.gwt.user.client.rpc.AsyncCallback;


public class MockPartyService extends AbstractServiceMock implements PartyServiceAsync {

  @Override
  public void addParticipant(String partyName, String participantName,
      AsyncCallback<Participant> callback) {
    execCallback(callback, partyName, participantName);
  }

  public ServiceMethodRegistration<Participant> expectAddParticipant(String partyName, String participantName) {
    return super.<Participant>register(partyName, participantName);
  }

  @Override
  public void addParty(String partyName, AsyncCallback<Party> callback) {
    execCallback(callback, partyName);
  }

  public ServiceMethodRegistration<Party> expectAddParty(String partyName) {
    return super.<Party>register(partyName);
  }

  @Override
  public void deleteParticipant(String partyName, String participantName,
      AsyncCallback<Void> callback) {
    execCallback(callback, partyName, participantName);
  }

  public ServiceMethodRegistration<Void> expectDeleteParticipant(String partyName,
      String participantName) {
    return super.<Void> register(partyName, participantName);
  }

  @Override
  public void deleteParty(String id, AsyncCallback<Void> callback) {
    execCallback(callback, id);
  }

  public ServiceMethodRegistration<Void> expectDeleteParty(String id) {
    return super.<Void>register(id);
  }

  @Override
  public void getAllPartiesDirty(AsyncCallback<Set<Party>> callback) {
    execCallback(callback);
  }

  public ServiceMethodRegistration<Set<Party>> expectGetAllPartiesDirty() {
    return super.<Set<Party>>register();
  }

  @Override
  public void getParticipants(String partyName, AsyncCallback<Set<Participant>> callback) {
    execCallback(callback, partyName);
  }

  public ServiceMethodRegistration<Set<Participant>> expectGetParticipants(String partyName) {
    return super.<Set<Participant>>register(partyName);
  }

  @Override
  public void togglePayedStatus(String partyName, String participantName,
      AsyncCallback<Boolean> callback) {
    execCallback(callback, partyName, participantName);
  }

  public ServiceMethodRegistration<Boolean> expectTogglePayedStatus(String partyName,
      String participantName) {
    return super.<Boolean> register(partyName, participantName);
  }

  @Override
  public void setPayedStatus(String partyName, String participantName, boolean payed,
      AsyncCallback<Void> callback) {
    execCallback(callback, partyName, participantName, payed);
  }

  public ServiceMethodRegistration<Void> expectSetPayedStatus(String partyName,
      String participantName, boolean payed) {
    return super.<Void> register(partyName, participantName, payed);
  }

  @Override
  public void updatePartyDate(String eventName, DateRange date, AsyncCallback<Void> callback) {
    execCallback(callback, eventName, date);
  }

  public ServiceMethodRegistration<Void> expectUpdatePartyDate(String eventName, DateRange date) {
    return super.<Void>register(eventName, date);
  }

  @Override
  public void updatePartyDescription(String partyName, String description,
      AsyncCallback<Void> callback) {
    execCallback(callback, partyName, description);
  }

  public ServiceMethodRegistration<Void> expectUpdatePartyDescription(String partyName, String description) {
    return super.<Void>register(partyName, description);
  }

  @Override
  public void keepAlive(AsyncCallback<Void> callback) {
    // TODO Auto-generated method stub

  }
}