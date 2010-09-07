package aleksz.potormozim.client.widget.party;

import java.util.ArrayList;
import java.util.List;

import aleksz.potormozim.client.ErrorHandlingAsyncCallback;
import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.client.service.PartyServiceAsync;
import aleksz.potormozim.client.widget.participants.ParticipantsPanel;
import aleksz.utils.client.Date;
import aleksz.utils.client.DateRange;
import aleksz.utils.client.Key;

import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;


public class PartyPanel extends AbstractPartyPanel<PartyPanel.View>
    implements HasPartyDateChangedHandlers, PartyDateChangedHandler {

  public interface View extends AbstractPartyPanel.View {
    String getDescription();
    HasValueChangeHandlers<DateRange> getDateAsEventSource();
    HasKeyUpHandlers getDescriptionAsEventSource();
    ParticipantsPanel.View getParticipantsView();
    void markEvent(String label, DateRange range);
    void unmarkEvent(String label, DateRange range);
    HandlerRegistration addPartyDateChangeHandler(PartyDateChangedHandler handler);
    void fireEvent(GwtEvent<?> event);
    void showDateInPastMessage();
    void showDescriptionUpdatedMessage();
    void showDateUpdatedMessage();
  }

  private List<HandlerRegistration> registrations = new ArrayList<HandlerRegistration>();

  public PartyPanel(Party party, View view, PartyServiceAsync partyService) {
    super(view, party, partyService,
        new ParticipantsPanel(party, partyService, view.getParticipantsView()));
  }

  protected void bindDisplay() {

    view.getDateAsEventSource().addValueChangeHandler(new ValueChangeHandler<DateRange>() {

      @Override
      public void onValueChange(ValueChangeEvent<DateRange> event) {
        if(event.getValue().getFrom().before(new Date())) {
          view.setDate(model.getDate());
          view.showDateInPastMessage();
          return;
        }

        updateDate(event.getValue());
      }
    });

    view.getDescriptionAsEventSource().addKeyUpHandler(new KeyUpHandler() {

      @Override
      public void onKeyUp(KeyUpEvent event) {
        Key key  = Key.byCode(event.getNativeKeyCode());
        if(key == null || !key.isControl()) {
          updateDescription(view.getDescription());
        }
      }
    });

    super.bindDisplay();
  }

  private void updateDescription(final String description) {
    partyService.updatePartyDescription(model.getName(), description,
        new ErrorHandlingAsyncCallback<Void>() {

          @Override
          public void onSuccess(Void result) {
            model.setDescription(description);
            view.showDescriptionUpdatedMessage();
          }
        });
  }

  private void updateDate(final DateRange value) {
    partyService.updatePartyDate(model.getName(), value,
        new ErrorHandlingAsyncCallback<Void>() {

          @Override
          public void onSuccess(Void result) {
            fireEvent(new PartyDateChangedEvent(
                model.getName(), model.getDate(), value));
            model.setDate(value);
            view.showDateUpdatedMessage();
          }
        });
  }

  public void addAsDateChangeHandlerTo(HasPartyDateChangedHandlers other) {
    registrations.add(other.addPartyDateChangeHandler(this));
  }

  private void clearHandlers() {
    for (HandlerRegistration reg : registrations) {
      reg.removeHandler();
    }
  }

  @Override
  public HandlerRegistration addPartyDateChangeHandler(PartyDateChangedHandler handler) {
    return view.addPartyDateChangeHandler(handler);
  }

  @Override
  public void fireEvent(GwtEvent<?> event) {
    view.fireEvent(event);
  }

  @Override
  public void onPartyDateChange(PartyDateChangedEvent event) {
    if (event.getPrevDateRange() != null) {
      view.unmarkEvent(event.getPartyName(), event.getPrevDateRange());
    }
    view.markEvent(event.getPartyName(), event.getNewDateRange());
  }

  public void unmarkEvent(Party p) {
    view.unmarkEvent(p.getName(), p.getDate());
  }

  public void markEvent(Party p) {
    view.markEvent(p.getName(), p.getDate());
  }

  public void destroy() {
    clearHandlers();
    super.destroy();
  }
}
