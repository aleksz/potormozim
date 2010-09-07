package aleksz.potormozim.client.domain;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import aleksz.utils.client.Date;
import aleksz.utils.client.DateRange;



public class PartyTest {

  @Test
  public void worksAsKeyInMap() {
    Party p1 = new Party("p1");
    Party p2 = new Party("p2");

    Map<Party, String> map = new HashMap<Party, String>();
    map.put(p1, "value1");
    map.put(p2, "value2");

    assertTrue(map.containsKey(p1));
    assertTrue(map.containsKey(p2));
  }

  @Test
  public void worksInSet() {
    Party p1 = new Party("p1");
    Party p2 = new Party("p2");
    Set<Party> set = new HashSet<Party>(Arrays.asList(p1, p2));

    assertTrue(set.contains(p1));
    assertTrue(set.contains(p2));
  }

  @Test
  public void compareTo() throws Exception {

    Date today = new Date();
    Date future = today.nextDay();
    Date veryFuture = today.nextMonth();
    Date past = today.prevDay();
    Date veryPast = today.prevMonth();
    Date toDate = veryFuture;

    System.out.println("today:" + today);
    System.out.println("future:" + future);
    System.out.println("very future:" + veryFuture);
    System.out.println("past:" + past);
    System.out.println("very past:" + veryPast);

    Party peToday = new Party("peToday", new DateRange(today, toDate));
    Party peInFuture = new Party("peInFuture", new DateRange(future, toDate));
    Party peInVeryFuture = new Party("peInVeryFuture", new DateRange(veryFuture, toDate));
    Party peUnscheduled1 = new Party("peUnscheduled1");
    Party peUnscheduled2 = new Party("peUnscheduled2");
    Party peInPast = new Party("peInPast", new DateRange(past, toDate));
    Party peInVeryPast = new Party("peInVeryPast", new DateRange(veryPast, toDate));

    Set<Party> setOfEvents = new TreeSet<Party>();
    setOfEvents.add(peToday);
    setOfEvents.add(peInFuture);
    setOfEvents.add(peInVeryFuture);
    setOfEvents.add(peUnscheduled1);
    setOfEvents.add(peUnscheduled2);
    setOfEvents.add(peInPast);
    setOfEvents.add(peInVeryPast);

    System.out.println(setOfEvents);

    Iterator<Party> iterator = setOfEvents.iterator();
    assertEquals(peToday, iterator.next());
    assertEquals(peInFuture, iterator.next());
    assertEquals(peInVeryFuture, iterator.next());
    assertEquals(peUnscheduled1, iterator.next());
    assertEquals(peUnscheduled2, iterator.next());
    assertEquals(peInPast, iterator.next());
    assertEquals(peInVeryPast, iterator.next());
  }
}
