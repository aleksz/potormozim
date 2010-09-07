package aleksz.potormozim.client.widget.list;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import aleksz.potormozim.client.ErrorHandlingAsyncCallback;
import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.PartyServiceAsync;
import aleksz.potormozim.client.widget.loading.LoadingPopup;
import aleksz.potormozim.client.widget.party.AbstractPartyPanel;
import aleksz.potormozim.client.widget.party.PartyPanel;
import aleksz.potormozim.client.widget.party.PartyReadOnlyPanel;
import aleksz.utils.client.Date;
import aleksz.utils.client.StringUtils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

/**
 *
 * @author aleksz
 *
 */
public class PartyListPanel {

  interface View {
    PartyPanel.View addParty(Party name);
    PartyReadOnlyPanel.View addReadOnlyParty(Party party);
    void setNewPartyInputValue(String value);
    String getNewPartyInputValue();
    HasKeyUpHandlers getNewPartyInputAsEventSource();
    void showNewPartyInput();
    void showParty(Party party);
    void deleteParty(Party party);
    HasClickHandlers getAddButton();
    HasClickHandlers getDeleteButton(Party party);
    HasSelectionHandlers<String> getStackPanel();
    void addHistoryItem(String item);
    HasValueChangeHandlers<String> getHistory();
    LoadingPopup getLoadingPopup();
    void showNewPartySavedMessage();
    void showPartyDeletedMessage();
    void disableAddButton();
    void enableAddButton();
  }

  private final View view;
  private Model model = new Model();
  private PartyServiceAsync partyService;

  public PartyListPanel(View view, PartyServiceAsync partyService) {
    this.view = view;
    this.partyService = partyService;
    bindDisplay();
  }

  private void bindDisplay() {
    view.getNewPartyInputAsEventSource().addKeyUpHandler(new KeyUpHandler() {

      @Override
      public void onKeyUp(KeyUpEvent event) {
        String newName = view.getNewPartyInputValue();
        if (!validateNewParty(newName)) {
          view.disableAddButton();
          return;
        }

        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          addParty(newName);
          return;
        }

        view.enableAddButton();
      }
    });

    view.getAddButton().addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        String newName = view.getNewPartyInputValue();

        if (validateNewParty(newName)) {
          addParty(newName);
        }
      }

    });

    cleanInput();
    registerHistory();
  }

  public void loadAllParties() {

    view.getLoadingPopup().center();

    partyService.getAllPartiesDirty(new ErrorHandlingAsyncCallback<Set<Party>>() {

      @Override
      public void onSuccess(Set<Party> result) {
        for (Party p : result) {
          addParty(p).loadAllParticipants();
        }

        view.getLoadingPopup().hide();
      }

    });
  }

  public void addParty(final String partyName) {
    partyService.addParty(partyName, new ErrorHandlingAsyncCallback<Party>() {

      @Override
      public void onSuccess(Party result) {
        addParty(result);
        view.showParty(result);
        view.showNewPartySavedMessage();
      }

    });
  }

  public void deleteParty(final Party party) {
    partyService.deleteParty(party.getName(),
        new ErrorHandlingAsyncCallback<Void>() {

          @Override
          public void onSuccess(Void result) {
            model.deleteParty(party);
            if (party.getDate() != null) {
              for (PartyPanel pp : model.getPartyPanels()) {
                pp.unmarkEvent(party);
              }
            }
            view.showNewPartyInput();
            view.deleteParty(party);
            view.showPartyDeletedMessage();
          }
        });
  }

  private AbstractPartyPanel<?> addParty(final Party party) {
    cleanInput();
    boolean readonly = party.getDate() != null && party.getDate().getFrom().before(new Date());

    AbstractPartyPanel<?> result;

    if (readonly) {
      PartyReadOnlyPanel partyReadOnlyPanel = new PartyReadOnlyPanel(
          party, view.addReadOnlyParty(party), partyService);
      result = partyReadOnlyPanel;
    } else {
      PartyPanel partyPanel = new PartyPanel(party, view.addParty(party), partyService);
      linkPartyPanelWithOthers(partyPanel);
      result = partyPanel;
    }

    model.addPanel(result);
    bindDeleteButton(party);

    return result;
  }

  private void linkPartyPanelWithOthers(PartyPanel newPartyPanel) {
    for (PartyPanel pp : model.getPartyPanels()) {
      newPartyPanel.addAsDateChangeHandlerTo(pp);
      pp.addAsDateChangeHandlerTo(newPartyPanel);
      if (pp.getModel().getDate() != null) { newPartyPanel.markEvent(pp.getModel()); }
      if (newPartyPanel.getModel().getDate() != null) { pp.markEvent(newPartyPanel.getModel()); }
    }

    for (PartyReadOnlyPanel prop : model.getReadOnlyPartyPanels()) {
      newPartyPanel.markEvent(prop.getModel());
    }
  }

  private void bindDeleteButton(final Party party) {
    view.getDeleteButton(party).addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        deleteParty(party);
      }
    });
  }

  private void cleanInput() {
    view.disableAddButton();
    view.setNewPartyInputValue(null);
  }

  private boolean validateNewParty(String inputText) {

    if (StringUtils.isEmpty(inputText)) {
      return false;
    }

    if (StringUtils.startsWithNumber(inputText)) {
      return false;
    }

    for (Party e : model.getAllParties()) {
      if (e.getName().equals(inputText)) {
        return false;
      }
    }

    return true;
  }

  private void registerHistory() {

    view.getStackPanel().addSelectionHandler(new SelectionHandler<String>() {

      @Override
      public void onSelection(SelectionEvent<String> event) {

        String key = event.getSelectedItem();

        if ("new".equals(key)) {
          view.addHistoryItem("new");
          return;
        }
        view.addHistoryItem(key);
      }

    });

    view.getHistory().addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {

        String historyToken = event.getValue();

        if (historyToken.equals("new")) {
          view.showNewPartyInput();
          return;
        }

        for (Party p : model.getAllParties()) {
          if (p.getName().equals(historyToken)) {
            view.showParty(p);
          }
        }
      }

    });
  }

  protected AbstractPartyPanel<?> getPartyPanel(Party p) {
    return model.getPartyPanel(p);
  }
}

class Model {

  private Set<Party> allParties = new HashSet<Party>();
  private Map<Party, PartyPanel> partyPanels = new HashMap<Party, PartyPanel>();
  private Map<Party, PartyReadOnlyPanel> readOnlyPartyPanels = new HashMap<Party, PartyReadOnlyPanel>();

  void addPanel(AbstractPartyPanel<?> pp) {
    if (pp instanceof PartyPanel) {
      partyPanels.put(pp.getModel(), (PartyPanel) pp);
    } else {
      readOnlyPartyPanels.put(pp.getModel(), (PartyReadOnlyPanel) pp);
    }
    allParties.add(pp.getModel());
  }

  Collection<PartyPanel> getPartyPanels() {
    return partyPanels.values();
  }

  AbstractPartyPanel<?> getPartyPanel(Party p) {
    PartyPanel res = partyPanels.get(p);
    return res != null ? res : readOnlyPartyPanels.get(p);
  }

  void deleteParty(Party p) {
    allParties.remove(p);
    PartyReadOnlyPanel removed = readOnlyPartyPanels.remove(p);
    if (removed != null) { return; }
    partyPanels.remove(p).destroy();
  }

  Set<Party> getAllParties() {
    return allParties;
  }

  Collection<PartyReadOnlyPanel> getReadOnlyPartyPanels() {
    return readOnlyPartyPanels.values();
  }
}