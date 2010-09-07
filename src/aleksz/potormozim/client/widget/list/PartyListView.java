package aleksz.potormozim.client.widget.list;

import java.util.HashMap;
import java.util.Map;

import aleksz.potormozim.client.App;
import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.widget.add.AddButton;
import aleksz.potormozim.client.widget.delete.DeleteButton;
import aleksz.potormozim.client.widget.loading.LoadingPopup;
import aleksz.potormozim.client.widget.loading.LoadingPopupWidget;
import aleksz.potormozim.client.widget.party.PartyPanel;
import aleksz.potormozim.client.widget.party.PartyReadOnlyPanel;
import aleksz.potormozim.client.widget.party.PartyReadOnlyView;
import aleksz.potormozim.client.widget.party.PartyView;
import aleksz.potormozim.client.widget.stack.MyStackPanel;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author aleksz
 */
public class PartyListView extends Composite implements PartyListPanel.View {

  private MyStackPanel stackPanel;
  private AddButton addButton = new AddButton();
  private TextBox newPartyInput;
  private Map<Party, Integer> partyToStackIndex = new HashMap<Party, Integer>();
  private Map<Party, DeleteButton> partyToDelete = new HashMap<Party, DeleteButton>();
  private LoadingPopupWidget loadingPopup = new LoadingPopupWidget();

  public PartyListView() {
    initWidget(getStackPanel());
    getElement().setId("partyEventListPanel");
  }

  @Override
  public PartyReadOnlyPanel.View addReadOnlyParty(Party party) {
    Grid g = addPartyGrid(party);
    g.addStyleDependentName("readonly");

    PartyReadOnlyPanel.View partyView = new PartyReadOnlyView();
    getStackPanel().add(partyView.asWidget(), g, party.getName());

    return partyView;
  }

  @Override
  public PartyPanel.View addParty(Party party) {
    Grid g = addPartyGrid(party);

    PartyPanel.View partyView = new PartyView();
    getStackPanel().add(partyView.asWidget(), g, party.getName());

    return partyView;
  }

  private Grid addPartyGrid(Party party) {
    Grid g = new Grid();
    g.resize(1, 2);
    g.setStyleName("stackHeader");

    Label text = new Label(party.getName());
    text.setStyleName("stackItemMiddleCenter");
    g.setWidget(0, 0, text);
    g.getCellFormatter().setHorizontalAlignment(0, 0, HorizontalPanel.ALIGN_LEFT);

    DeleteButton delButton = new DeleteButton();
    partyToDelete.put(party, delButton);
    g.setWidget(0, 1, delButton);
    g.getCellFormatter().setHorizontalAlignment(0, 1, HorizontalPanel.ALIGN_RIGHT);

    partyToStackIndex.put(party, partyToStackIndex.size() + 1);
    return g;
  }

  @Override
  public AddButton getAddButton() {
    return addButton;
  }

  @Override
  public HasKeyUpHandlers getNewPartyInputAsEventSource() {
    return getNewPartyInput();
  }

  @Override
  public MyStackPanel getStackPanel() {

    if (stackPanel != null) {
      return stackPanel;
    }

    stackPanel = new MyStackPanel();
    FlowPanel addGroup = new FlowPanel();

    addGroup.add(getNewPartyInput());
    addGroup.add(addButton);
    stackPanel.add(addGroup, App.msgs.newPartyTitle(), "new");

    return stackPanel;
  }

  private TextBox getNewPartyInput() {

    if (newPartyInput != null) {
      return newPartyInput;
    }

    newPartyInput = new TextBox();
    newPartyInput.setStyleName("newEventNameInput");

    return newPartyInput;
  }

  @Override
  public void showNewPartyInput() {
    getStackPanel().showStack(0);
    History.newItem("new");
  }

  @Override
  public void showParty(Party party) {
    getStackPanel().showStack(partyToStackIndex.get(party));
    History.newItem(party.getName());
  }

  @Override
  public HasClickHandlers getDeleteButton(Party party) {
    return partyToDelete.get(party);
  }

  @Override
  public void deleteParty(Party party) {
    partyToDelete.remove(party);
    getStackPanel().remove(partyToStackIndex.remove(party));
  }

  @Override
  public void addHistoryItem(String item) {
    History.newItem(item);
  }

  @Override
  public HasValueChangeHandlers<String> getHistory() {
    return new HasValueChangeHandlers<String>() {

      @Override
      public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return History.addValueChangeHandler(handler);
      }

      @Override
      public void fireEvent(GwtEvent<?> event) {}
    };
  }

  @Override
  public LoadingPopup getLoadingPopup() {
    return loadingPopup;
  }

  @Override
  public String getNewPartyInputValue() {
    return newPartyInput.getText();
  }

  @Override
  public void setNewPartyInputValue(String value) {
    newPartyInput.setText(value);
  }

  @Override
  public void disableAddButton() {
    addButton.disable(App.msgs.partyListPanelDisabledAddButtonTitle());
  }

  @Override
  public void enableAddButton() {
    addButton.enable(App.msgs.partyListPanelEnabledAddButtonTitle());
  }

  @Override
  public void showNewPartySavedMessage() {
    App.systemMessaging.success(App.msgs.partySaved());
  }

  @Override
  public void showPartyDeletedMessage() {
    App.systemMessaging.success(App.msgs.partyDeleted());
  }
}
