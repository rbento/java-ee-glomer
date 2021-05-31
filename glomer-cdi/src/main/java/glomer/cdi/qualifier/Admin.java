package glomer.cdi.qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apache.deltaspike.security.api.authorization.SecurityBindingType;

@Retention(RUNTIME)
@Target({TYPE, METHOD})
@Documented
@SecurityBindingType
public @interface Admin {
}
