package aleksz.potormozim.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public abstract class GWTController extends RemoteServiceServlet
    implements Controller, ServletContextAware {

  private static final long serialVersionUID = 1L;

  private ServletContext servletContext;

  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  public ServletContext getServletContext() {
    return servletContext;
  }

  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    super.doPost(request, response);
    return null;
  }
}
