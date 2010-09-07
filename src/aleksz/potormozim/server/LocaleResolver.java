package aleksz.potormozim.server;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import aleksz.potormozim.client.i18n.AppMessages;

@Component("localeResolver")
public class LocaleResolver implements org.springframework.web.servlet.LocaleResolver {

  private Set<String> supportedLocales = new HashSet<String>();

  {
    for (Locale l : Locale.getAvailableLocales()) {

      String name = "/" + AppMessages.class.getName().replaceAll("\\.", "/");
      name += "_" + l + ".properties";

      if (getClass().getResourceAsStream(name) != null) {
        supportedLocales.add(l.toString());
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Locale resolveLocale(HttpServletRequest request) {

    Enumeration<Locale> locales = request.getLocales();

    while (locales.hasMoreElements()) {
      Locale l = locales.nextElement();
      if (supportedLocales.contains(l.toString())) {
        return l;
      }
    }

    return request.getLocale();
  }

  @Override
  public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    throw new UnsupportedOperationException(
      "Cannot change HTTP accept header - use a different locale resolution strategy");
  }
}
