package aleksz.potormozim.server.mobile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import aleksz.potormozim.client.domain.Participant;
import aleksz.potormozim.client.domain.Party;
import aleksz.potormozim.server.dao.PartyDao;
import aleksz.utils.client.Date;
import aleksz.utils.client.DateRange;

@Controller
@RequestMapping({"/mob/*", "/mob"})
public class MobileController {

  private static final String DATE_FORMAT_PATTERN = "dd.MM.yyyy";

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);

  @Autowired
  private PartyDao partyDao;

  @RequestMapping("/mob")
  public String home() {
    return "forward:/mob/partyList.do";
  }

  @RequestMapping(value="partyList.do", method=RequestMethod.GET)
  @ModelAttribute("partyList")
  public Set<Party> partyList() {
    return partyDao.getAllParties();
  }

  @RequestMapping(value="partyList.do", method=RequestMethod.POST)
  public ModelMap updatePartyList(
      @RequestParam("newParty") String newParty,
      @RequestParam(value="delete", required=false) Set<String> partyNamesToDelete,
      ModelMap model) {

    Set<String> errors = new HashSet<String>();
    model.addAttribute("errors", errors);
    Set<Party> allParties = partyDao.getAllParties();
    model.addAttribute("partyList", allParties);

    addNewParty(newParty, allParties, errors);
    deleteParties(partyNamesToDelete, allParties);

    return model;
  }

  @RequestMapping(value="party.do", method=RequestMethod.GET)
  public ModelMap party(@RequestParam("party") String partyName, ModelMap model) {

    Party party = partyDao.getParty(partyName);
    model.addAttribute("party", party);
    model.addAttribute("dateFormat", DATE_FORMAT_PATTERN);
    model.addAttribute("participantsSize", party.getParticipants().size());

    return model;
  }

  @RequestMapping(value="party.do", method=RequestMethod.POST)
  public ModelMap updateParty(
      @RequestParam("party") String partyName,
      @RequestParam("dateFrom") String dateFrom,
      @RequestParam("dateTo") String dateTo,
      @RequestParam("description") String description,
      @RequestParam(value="delete", required=false) String[] participantNamesToDelete,
      @RequestParam(value="payed", required=false) Set<String> payedParticipantNames,
      @RequestParam(value="newParticipant", required=false) String newParticipant,
      ModelMap model) {

    Set<String> errors = new HashSet<String>();
    model.addAttribute("errors", errors);
    Party party = partyDao.getParty(partyName);

    updateDate(dateFrom, dateTo, party, errors);
    updateDescription(description, party);
    togglePayedStatus(payedParticipantNames, party);
    deleteParticipants(participantNamesToDelete, party);
    addNewParticipant(newParticipant, party, errors);

    return party(partyName, model);
  }

  private void addNewParty(String newPartyName, Set<Party> allParties, Set<String> errors) {

    if (!StringUtils.hasText(newPartyName)) {
      return;
    }

    for (Party p : allParties) {
      if (p.getName().equalsIgnoreCase(newPartyName)) {
        errors.add("Party already exists");
        return;
      }
    }

    Party party = new Party(newPartyName);
    partyDao.saveParty(party);
    allParties.add(party);
  }

  private void addNewParticipant(String newParticipant, Party party, Set<String> errors) {

    if (!StringUtils.hasText(newParticipant)) {
      return;
    }

    for (Participant p : party.getParticipants()) {
      if (p.getName().equalsIgnoreCase(newParticipant)) {
        errors.add("New participant is already in party");
        return;
      }
    }

    party.addParticipant(newParticipant);
    partyDao.saveParty(party);
  }

  private void updateDescription(String description, Party party) {

    if (!StringUtils.hasText(description)) {
      return;
    }

    if (description.equals(party.getDescription())) {
      return;
    }

    party.setDescription(description);
    partyDao.saveParty(party);
  }

  private void updateDate(String dateFrom, String dateTo, Party party, Set<String> errors) {

    Date parsedDateFrom = null;
    Date parsedDateTo = null;

    if (StringUtils.hasText(dateFrom)) {
      try {
        parsedDateFrom = new Date(DATE_FORMAT.parse(dateFrom));
      } catch (ParseException e) {
        errors.add("Expected date format is " + DATE_FORMAT.format(new Date()));
        return;
      }
    }

    if (StringUtils.hasText(dateTo)) {
      try {
        parsedDateTo = new Date(DATE_FORMAT.parse(dateTo));
      } catch (ParseException e) {
        errors.add("Expected date format is " + DATE_FORMAT.format(new Date()));
        return;
      }
    }

    if (parsedDateFrom == null) { return; }
    if (parsedDateTo == null) { parsedDateTo = parsedDateFrom; }

    DateRange newRange = new DateRange(parsedDateFrom, parsedDateTo);

    if (newRange.equals(party.getDate())) {
      return;
    }

    party.setDate(newRange);
    partyDao.saveParty(party);
  }

  private void togglePayedStatus(Set<String> payedParticipantNames, Party party) {

    for (Participant p : party.getParticipants()) {
      boolean shouldBePayed = payedParticipantNames != null;
      shouldBePayed = shouldBePayed && payedParticipantNames.contains(p.getName());
      if (!p.isPayed() && shouldBePayed || p.isPayed() && !shouldBePayed) {
        p.setPayed(!p.isPayed());
        partyDao.saveParty(party);
      }
    }
  }

  private void deleteParticipants(String[] participantNamesToDelete, Party party) {

    if (participantNamesToDelete == null) {
      return;
    }

    for (String participantName : participantNamesToDelete) {
      party.removeParticipant(participantName);
    }

    partyDao.saveParty(party);
  }

  private void deleteParties(Set<String> partiesToDelete, Set<Party> allParties) {

    if (partiesToDelete == null) {
      return;
    }

    Set<Party> copyOfAllParties = new HashSet<Party>(allParties);

    for (Party p : copyOfAllParties) {
      if (partiesToDelete.contains(p.getName())) {
        partyDao.deleteParty(p.getName());
        allParties.remove(p);
      }
    }
  }
}
