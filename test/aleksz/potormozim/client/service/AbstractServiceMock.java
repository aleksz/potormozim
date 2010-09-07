package aleksz.potormozim.client.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.springframework.util.StringUtils;

import com.google.gwt.user.client.rpc.AsyncCallback;


public class AbstractServiceMock {

  private Map<String, List<ServiceMethodRegistration<?>>> registrations = new HashMap<String, List<ServiceMethodRegistration<?>>>();

  protected <T> void execCallback(AsyncCallback<T> callback, Object...args) {
    ServiceMethodRegistration<T> registration = getRegistration();
    Assert.assertArrayEquals(registration.args, args);

    if (registration.fail) {
      callback.onFailure(new RuntimeException());
      return;
    }

    callback.onSuccess((T) registration.returnValue);
  }

  @SuppressWarnings("unchecked")
  private <T> ServiceMethodRegistration<T> getRegistration() {
    String key = new Throwable().fillInStackTrace().getStackTrace()[2].getMethodName();
    List<ServiceMethodRegistration<?>> l = registrations.get(key);
    return (ServiceMethodRegistration<T>) l.remove(0);
  }

  protected <T> ServiceMethodRegistration<T> register(Object...args) {
    String methodName = new Throwable().fillInStackTrace().getStackTrace()[1].getMethodName();
    String key = StringUtils.uncapitalize(methodName.substring(6));
    ServiceMethodRegistration<T> value = new ServiceMethodRegistration<T>(args);

    if (registrations.get(key) != null) {
      registrations.get(key).add(value);
      return value;
    }

    List<ServiceMethodRegistration<?>> l = new ArrayList<ServiceMethodRegistration<?>>();
    l.add(value);
    registrations.put(key,  l);

    return value;
  }

  public class ServiceMethodRegistration<T> {

    T returnValue;
    Object[] args;
    boolean fail;

    ServiceMethodRegistration(Object[] args) {
      this.args = args;
    }

    public void andReturn(T returnValue) {
      this.returnValue = returnValue;
    }

    public void andFail() {
      fail = true;
    }
  }
}
