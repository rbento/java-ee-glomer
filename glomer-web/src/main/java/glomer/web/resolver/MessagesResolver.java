package glomer.web.resolver;

import glomer.cdi.qualifier.Eager;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;

@Eager
@Named
@ApplicationScoped
public class MessagesResolver {

    @Inject
    private Logger log;

    @PostConstruct
    public void init() {

        log.debug("Initializing MessagesResolver...");

        Messages.setResolver(new Messages.Resolver() {

            private final String[] bundles = {"messages", "ValidationMessages", "labels"};

            public String getMessage(String key, Object... params) {

                for (String bundle : bundles) {

                    ResourceBundle resourceBundle = ResourceBundle.getBundle(bundle, Faces.getLocale());

                    if (resourceBundle.containsKey(key)) {

                        return MessageFormat.format(resourceBundle.getString(key), params);
                    }
                }

                return key + ".notfound";
            }

        });
    }
}
