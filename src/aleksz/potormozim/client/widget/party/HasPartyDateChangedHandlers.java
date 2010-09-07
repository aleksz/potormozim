package aleksz.potormozim.client.widget.party;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 *
 * @author aleksz
 *
 */
public interface HasPartyDateChangedHandlers extends HasHandlers {

  HandlerRegistration addPartyDateChangeHandler(PartyDateChangedHandler handler);
}
