package aleksz.potormozim.client.widget.party;

import aleksz.utils.client.DateRange;

import com.google.gwt.event.shared.GwtEvent;

/**
 *
 * @author aleksz
 *
 */
public class PartyDateChangedEvent extends GwtEvent<PartyDateChangedHandler> {

  private static Type<PartyDateChangedHandler> TYPE;

  private String partyName;
  private DateRange prevDateRange;
  private DateRange newDateRange;

  public PartyDateChangedEvent(String partyName, DateRange prevDateRange, DateRange newDateRange) {
    this.partyName = partyName;
    this.prevDateRange = prevDateRange;
    this.newDateRange = newDateRange;
  }

  @Override
  protected void dispatch(PartyDateChangedHandler handler) {
    handler.onPartyDateChange(this);
  }

  public static Type<PartyDateChangedHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<PartyDateChangedHandler>();
    }
    return TYPE;
  }

  @Override
  public Type<PartyDateChangedHandler> getAssociatedType() {
    return TYPE;
  }

  public String getPartyName() {
    return partyName;
  }

  public DateRange getPrevDateRange() {
    return prevDateRange;
  }

  public DateRange getNewDateRange() {
    return newDateRange;
  }
}
