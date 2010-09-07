package aleksz.potormozim.client.widget.participants;

import java.util.Set;

import aleksz.potormozim.client.ErrorHandlingAsyncCallback;
import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.PartyServiceAsync;


public abstract class AbstractParticipantsPanel<VIEW extends AbstractParticipantsPanel.View> {

  protected interface View {
    void updateHeader(int numberOfParticipants);
    void removeParticipant(Participant participant);
  }

  protected final Party model;
  protected VIEW view;
  protected PartyServiceAsync partyService;

  protected AbstractParticipantsPanel(Party party, VIEW view,
      PartyServiceAsync partyService) {
    model = party;
    this.view = view;
    this.partyService = partyService;
    bindDisplay();
  }

  protected void bindDisplay() {
    addAllParticipants(model.getParticipants());
  }

  protected void addAllParticipants(Set<Participant> participants) {
    for (Participant p : participants) {
      addParticipant(p);
    }
  }

  protected void addParticipant(Participant p) {
    model.getParticipants().add(p);
    updateHeaderInView();
  }

  protected void updateHeaderInView() {
    view.updateHeader(model.getParticipants().size());
  }

  protected void removeParticipant(Participant p) {
    model.getParticipants().remove(p);
    view.removeParticipant(p);
    updateHeaderInView();
  }

  public void loadAllParticipants() {

    partyService.getParticipants(model.getName(),
        new ErrorHandlingAsyncCallback<Set<Participant>>() {

          @Override
          public void onSuccess(Set<Participant> result) {
            clearView();
            addAllParticipants(result);
          }

        });
  }

  void clearView() {
    for (Participant p : model.getParticipants()) {
      removeParticipant(p);
    }
  }
}
