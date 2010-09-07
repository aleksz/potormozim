package aleksz.potormozim.server;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import aleksz.potormozim.client.i18n.AppMessages;

/**
 *
 * @author aleksz
 *
 */
@Component("messageSource")
public class MessageSource extends ReloadableResourceBundleMessageSource {

  {
    String name = "classpath:/";
    name += AppMessages.class.getName().replaceAll("\\.", "/");
    setBasename(name);
    setDefaultEncoding("UTF-8");
  }
}
