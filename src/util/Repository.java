package util;
import model.annotation.Id;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class Repository<T> {

    // Store objects using their ID as the key
    private final HashMap<String, T> storage = new HashMap<>();

    public T save(T entity) {

        try {
            Class<?> clazz = entity.getClass();

            // Search for the field marked with @Id
            while (clazz != null) {

                for (Field field : clazz.getDeclaredFields()) {

                    if (field.isAnnotationPresent(Id.class)) {

                        field.setAccessible(true);

                        // Read the ID value
                        Object idValue = field.get(entity);

                        if (idValue == null) {
                            throw new IllegalArgumentException(
                                    "ID cannot be null."
                            );
                        }

                        // Store the object using its ID
                        String id = idValue.toString();

                        storage.put(id, entity);

                        return entity;
                    }
                }

                clazz = clazz.getSuperclass();
            }

            throw new IllegalArgumentException(
                    "No field marked with @Id found."
            );

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to access ID field.", e);
        }
    }

    public T findById(String id) {
        return storage.get(id);
    }
    public ArrayList<T> findAll() {
        return new ArrayList<>(storage.values());
    }
    public boolean delete(String id) {
        return storage.remove(id) != null;
    }

    @SafeVarargs
    public final void saveAll(T... items) {

        for (T item : items) {
            save(item);
        }

        System.out.println("All objects saved successfully.");
    }

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            return 0.0;
        }

        double sum = 0;

        for (N value : values) {
            sum += value.doubleValue();
        }

        return sum / values.length;
    }


}
