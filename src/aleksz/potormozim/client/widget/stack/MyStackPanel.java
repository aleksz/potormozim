package aleksz.potormozim.client.widget.stack;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.Widget;


public class MyStackPanel extends DecoratedStackPanel implements HasSelectionHandlers<String> {

  private Map<Widget, String> widgetsToKeys = new HashMap<Widget, String>();

  public void add(Widget content, String header, String key) {
    super.add(content, header);
    widgetsToKeys.put(content, key);
  }

  public void add(Widget content, Widget header, String key) {
    add(content);

    Element table = getElement();
    Element body = DOM.getFirstChild(table);

    int index = getWidgetCount() - 1;
    Element tdWrapper = DOM.getChild(DOM.getChild(body, index * 2), 0);
    Element headerElem = DOM.getFirstChild(tdWrapper);

    header.removeFromParent();
    DOM.appendChild(getHeaderElem(headerElem), header.getElement());
    adopt(header);

    widgetsToKeys.put(content, key);
  }

  @Override
  public void onBrowserEvent(Event event) {

    super.onBrowserEvent(event);

    if (DOM.eventGetType(event) == Event.ONCLICK) {
      Element target = DOM.eventGetTarget(event);
      int index = findDividerIndex(target);
      if (index != -1) {
        SelectionEvent.fire(this, widgetsToKeys.get(getWidget(index)));
      }
    }
  }

  private Element getHeaderElem(Element headerElem) {
    Element tbody = DOM.getFirstChild(headerElem);
    Element tr = DOM.getChild(tbody, 1);
    Element td = DOM.getChild(tr, 1);
    return DOM.getFirstChild(td);
  }

  private int findDividerIndex(Element elem) {
    while (elem != getElement()) {
      String expando = DOM.getElementProperty(elem, "__index");
      if (expando != null) {
        // Make sure it belongs to me!
        int ownerHash = DOM.getElementPropertyInt(elem, "__owner");
        if (ownerHash == hashCode()) {
          // Yes, it's mine.
          return Integer.parseInt(expando);
        } else {
          // It must belong to some nested StackPanel.
          return -1;
        }
      }
      elem = DOM.getParent(elem);
    }
    return -1;
  }

  @Override
  public HandlerRegistration addSelectionHandler(SelectionHandler<String> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }
}
