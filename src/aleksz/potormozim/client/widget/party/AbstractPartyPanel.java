package aleksz.potormozim.client.widget.party;

import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.PartyServiceAsync;
import aleksz.potormozim.client.widget.participants.AbstractParticipantsPanel;
import aleksz.utils.client.DateRange;

import com.google.gwt.user.client.ui.Widget;


public abstract class AbstractPartyPanel<VIEW extends AbstractPartyPanel.View> {

  protected interface View {
    void setDate(DateRange date);
    void setDescription(String description);
    Widget asWidget();
  }

  protected VIEW view;
  protected Party model;
  protected PartyServiceAsync partyService;
  protected AbstractParticipantsPanel<?> participantsPanel;

  protected AbstractPartyPanel(VIEW view, Party model, PartyServiceAsync partyService,
      AbstractParticipantsPanel<?> participantsPanel) {
    this.view = view;
    this.model = model;
    this.partyService = partyService;
    this.participantsPanel = participantsPanel;
    bindDisplay();
  }

  protected void bindDisplay() {
    view.setDescription(model.getDescription());
    view.setDate(model.getDate());
  }

  public void loadAllParticipants() {
    participantsPanel.loadAllParticipants();
  }

  public VIEW getView() {
    return view;
  }

  public Party getModel() {
    return model;
  }

  public void destroy() {
  }
}
