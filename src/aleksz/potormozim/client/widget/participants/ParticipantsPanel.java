package aleksz.potormozim.client.widget.participants;


import java.util.Set;

import aleksz.potormozim.client.ErrorHandlingAsyncCallback;
import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.PartyServiceAsync;
import aleksz.potormozim.client.widget.dollar.Dollar;
import aleksz.utils.client.StringUtils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

/**
 *
 * @author aleksz
 *
 */
public class ParticipantsPanel extends AbstractParticipantsPanel<ParticipantsPanel.View>{

  public interface View extends AbstractParticipantsPanel.View {
    void setNewParticipantInputValue(String text);
    String getNewParticipantInputValue();
    void disableAddButton();
    void enableAddButton();
    HasKeyUpHandlers getNewParticipantInputAsEventSource();
    HasClickHandlers getAddButton();
    HasClickHandlers getDeleteButton(Participant participant);
    Dollar.View addParticipant(Participant participant);
    void showNewParticipantAddedMessage();
    void showParticipantDeletedMessage();
  }

  public ParticipantsPanel(Party party, PartyServiceAsync partyService, View view) {
    super(party, view, partyService);
  }

  @Override
  protected void bindDisplay() {
    view.getNewParticipantInputAsEventSource().addKeyUpHandler(new KeyUpHandler() {

      @Override
      public void onKeyUp(KeyUpEvent event) {
        String newName = view.getNewParticipantInputValue();
        if (!validateNewParticipant(newName)) {
          view.disableAddButton();
          return;
        }

        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          submitNewParticipant(newName);
          return;
        }

        view.enableAddButton();
      }
    });

    view.getAddButton().addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        String newName = view.getNewParticipantInputValue();

        if (validateNewParticipant(newName)) {
          submitNewParticipant(newName);
        }
      }

    });

    super.bindDisplay();
  }

  public void submitNewParticipant(String newName) {

    final Participant addedParticipant = new Participant(newName, model);
    addParticipant(addedParticipant);
    addDeleteButtonHandler(addedParticipant);

    partyService.addParticipant(model.getName(), newName,
        new ErrorHandlingAsyncCallback<Participant>() {

          @Override
          public void onSuccess(Participant newParticipant) {
            view.showNewParticipantAddedMessage();
          }

          @Override
          public void onFailure(Throwable caught) {
            super.onFailure(caught);
            removeParticipant(addedParticipant);
          }

        });
  }

  private boolean validateNewParticipant(String newName) {

    if (StringUtils.isEmpty(newName)) {
      return false;
    }

    for (Participant p : model.getParticipants()) {
      if (p.getName().equals(newName)) {
        return false;
      }
    }

    return true;
  }

  @Override
  protected void addAllParticipants(Set<Participant> participants) {
    if (participants.isEmpty()) {
      updateHeaderInView();
      cleanInput();
    }

    super.addAllParticipants(participants);
  }

  private void cleanInput() {
    view.disableAddButton();
    view.setNewParticipantInputValue(null);
  }

  @Override
  protected void addParticipant(Participant p) {
    super.addParticipant(p);
    Dollar.View dollarView = view.addParticipant(p);

    new Dollar(p, dollarView, partyService);
    addDeleteButtonHandler(p);
    cleanInput();
  }

  @Override
  protected void removeParticipant(Participant p) {
    super.removeParticipant(p);
    cleanInput();
  }

  private void addDeleteButtonHandler(final Participant p) {
    view.getDeleteButton(p).addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {

        removeParticipant(p);

        partyService.deleteParticipant(p.getParty().getName(), p.getName(), new ErrorHandlingAsyncCallback<Void>() {

          @Override
          public void onSuccess(Void result) {
            view.showParticipantDeletedMessage();
          }

          @Override
          public void onFailure(Throwable caught) {
            super.onFailure(caught);
            addParticipant(p);
          }
        });
      }
    });
  }
}
