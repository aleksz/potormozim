package aleksz.potormozim.server;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping({"/ria/*", "/ria"})
public class AppController {

  @RequestMapping("/ria")
  public String home() {
    return "forward:/ria/app.do";
  }

  @RequestMapping(value="app.do", method=RequestMethod.GET)
  @ModelAttribute("locale")
  public String app(Locale loc) {
    return loc.toString();
  }
}
