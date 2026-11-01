package util;

import model.annotation.Positive;
import model.annotation.MaxLength;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationValidator {

    public static String[] validate(Object obj) {

        List<String> errors = new ArrayList<>();

        if (obj == null) {
            errors.add("Object cannot be null.");
            return errors.toArray(new String[0]);
        }

        // Check the object's class and its parent classes
        Class<?> currentClass = obj.getClass();

        while (currentClass != null && currentClass != Object.class) {

            Field[] fields = currentClass.getDeclaredFields();

            for (Field field : fields) {

                field.setAccessible(true);

                try {

                    // Check @Positive annotation
                    if (field.isAnnotationPresent(Positive.class)) {

                        Positive positive = field.getAnnotation(Positive.class);

                        Object value = field.get(obj);

                        if (value instanceof Number &&
                                ((Number) value).doubleValue() <= 0) {

                            errors.add(field.getName() + " " + positive.message());
                        }
                    }

                    // Check @MaxLength annotation
                    if (field.isAnnotationPresent(MaxLength.class)) {

                        MaxLength maxLength = field.getAnnotation(MaxLength.class);

                        Object value = field.get(obj);

                        if (value instanceof String &&
                                ((String) value).length() > maxLength.value()) {

                            errors.add(field.getName() +
                                    " length must not exceed " +
                                    maxLength.value() + " characters.");
                        }
                    }

                } catch (IllegalAccessException e) {
                    errors.add("Unable to access field: " + field.getName());
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}
