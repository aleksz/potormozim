package aleksz.potormozim.client.widget.participants;

import java.util.HashMap;
import java.util.Map;

import aleksz.potormozim.client.App;
import aleksz.potormozim.client.domain.Participant;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


public class ParticipantsReadOnlyView extends Composite implements ParticipantsReadOnlyPanel.View {

  private DisclosurePanel panel;
  private VerticalPanel content = new VerticalPanel();
  private Map<Participant, Label> participantsToLabels = new HashMap<Participant, Label>();

  public ParticipantsReadOnlyView() {
    initWidget(getPane());
  }

  private DisclosurePanel getPane() {

    if (panel != null) {
      return panel;
    }

    panel = new DisclosurePanel("", false);
    panel.setContent(content);

    return panel;
  }

  @Override
  public void updateHeader(int nrOfParticipants) {
    getPane().getHeaderTextAccessor().setText(App.msgs.participantsHeader(nrOfParticipants));
  }

  @Override
  public void addParticipant(Participant participant) {
    Label label = new Label(participant.getName());
    participantsToLabels.put(participant, label);
    content.add(label);
  }

  @Override
  public void removeParticipant(Participant participant) {
    content.remove(participantsToLabels.remove(participant));
  }
}
