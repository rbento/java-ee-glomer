package glomer.web.navigation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.omnifaces.util.Faces;

@Named("outcome")
@ApplicationScoped
public class Outcome {

    private static final String CONTEXT_PATH = Faces.getRequestContextPath();

    public static final String DASHBOARD = CONTEXT_PATH + "/modules/dashboard.xhtml";
    public static final String LOGIN = CONTEXT_PATH + "/login.xhtml";
}
