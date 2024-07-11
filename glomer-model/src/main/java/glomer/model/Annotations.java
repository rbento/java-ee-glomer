package glomer.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Annotations {

    private static final Logger log = LoggerFactory.getLogger(Annotations.class);

    public static <T extends Annotation> void requirePresence(final Class<T> annotation, final Class<T> clazz) {

        if (!clazz.isAnnotationPresent(annotation)) {

            throw new IllegalStateException("Class<" + clazz.getSimpleName() + "> requires the @" + annotation.getSimpleName() + " annotation.");
        }
    }

    public static <T extends Annotation> void requirePresence(final Class<T> annotation, final Object object) {

        if (!object.getClass().isAnnotationPresent(annotation)) {

            throw new IllegalStateException("Object requires the @" + annotation.getSimpleName() + " annotation.");
        }
    }

    @SuppressWarnings("rawtypes")
    public static String getValueAsString(final Class clazz, final Class annotation, final String attribute) {

        Object result = getValue(clazz, annotation, attribute);

        return (null != result) ? result.toString() : null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object getValue(final Class clazz, final Class annotation, final String attribute) {

        Annotation result = clazz.getAnnotation(annotation);

        if (null != result) {

            try {

                return result.annotationType().getMethod(attribute).invoke(result);

            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {

                log.error("Error getting annotation value.", ex);
            }
        }

        return null;
    }
}
