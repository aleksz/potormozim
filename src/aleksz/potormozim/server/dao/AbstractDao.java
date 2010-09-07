package aleksz.potormozim.server.dao;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.springframework.stereotype.Component;

@Component
public abstract class AbstractDao {

  private static final PersistenceManagerFactory pmfInstance = JDOHelper
  .getPersistenceManagerFactory("transactions-optional");

  protected PersistenceManager getPM() {
    return pmfInstance.getPersistenceManager();
  }
}
