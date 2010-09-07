package aleksz.potormozim.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 *
 *
 * @author aleksz
 *
 */
public interface AppMessages extends Messages {

  String newPartyTitle();

  String delete();

  String dollarStatusChangedPositive();

  String dollarStatusChangedNegative();

  String dollarPositiveStatus();

  String dollarNegativeStatus();

  String serverError();

  String participantsPanelDisabledAddButtonTitle();

  String participantsPanelEnabledAddButtonTitle();

  String participantAdded();

  String participantDeleted();

  String dateInPast();

  String dateUpdated();

  String descriptionUpdated();

  String partyDeleted();

  String partySaved();

  String partyListPanelDisabledAddButtonTitle();

  String partyListPanelEnabledAddButtonTitle();

  String participantsHeader(int nrOfParticipants);
}
