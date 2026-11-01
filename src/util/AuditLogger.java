package util;

import model.annotation.Audit;
import java.lang.reflect.Method;

public class AuditLogger {

    public static void log(Class<?> serviceClass, String methodName) {

        for (Method method : serviceClass.getDeclaredMethods()) {

            if (method.getName().equals(methodName)
                    && method.isAnnotationPresent(Audit.class)) {

                Audit audit = method.getAnnotation(Audit.class);

                System.out.println(
                        "[AUDIT] Operation: " + audit.value()
                                + " | Method: " + method.getName()
                );

                return;
            }
        }

        System.out.println("[AUDIT] No audit annotation found.");
    }
}
