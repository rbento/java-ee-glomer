package glomer.cdi.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.deltaspike.security.api.authorization.SecurityParameterBinding;

@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
@SecurityParameterBinding
public @interface LoggedIn {
}
