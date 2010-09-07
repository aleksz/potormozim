package aleksz.potormozim.client.widget;

import aleksz.potormozim.client.widget.stack.MyStackPanel;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Label;



public class MyStackPanelTest extends GWTTestCase {

  private MyStackPanel stack;

  @Override
  public String getModuleName() {
    return "aleksz.potormozim.App";
  }

  @Override
  protected void gwtSetUp() throws Exception {
    super.gwtSetUp();
    stack = new MyStackPanel();
  }

  public void testGetWidgetCount() {
    stack.add(new Label("one"));
    stack.add(new Label("two"));
    assertEquals(2, stack.getWidgetCount());

    stack.add(new Label("three"), "three");
    stack.add(new Label("four"), "four");
    assertEquals(4, stack.getWidgetCount());

    stack.add(new Label("three"), "three", true);
    stack.add(new Label("four"), "four", true);
    assertEquals(6, stack.getWidgetCount());

    stack.add(new Label("three"), new Label("three"), "key");
    stack.add(new Label("four"), new Label("four"), "key");
    assertEquals(8, stack.getWidgetCount());
  }

  public void testAddWithStringInHeader() {
    stack.add(new Label("content"), "header");
    assertEquals(1, stack.getWidgetCount());
  }

  public void testAddWithWidgetInHeader() {
    stack.add(new Label("content"), new Label("header"), "key");
    assertEquals(1, stack.getWidgetCount());
  }

  public void testClearAfterAddWithWidgetInHeader() {
    stack.add(new Label("content"), new Label("header"), "key");
    stack.clear();
    assertEquals(0, stack.getWidgetCount());
  }
}
