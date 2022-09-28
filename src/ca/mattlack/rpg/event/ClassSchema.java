package ca.mattlack.rpg.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to hold information about a class and the methods
 * that need to be called for an object of the class' type when an event is fired.
 */
public class ClassSchema
{

    private Class<?> clazz;
    private final Map<Class<?>, List<MethodCaller>> methodCallers = new HashMap<>();

    public ClassSchema(Class<?> clazz, Class<? extends Annotation> annotation) {
        this.clazz = clazz;
        map(annotation);
    }

    public List<MethodCaller> getMethodCallers(Class<?> event) {
        return methodCallers.get(event);
    }

    public Map<Class<?>, List<MethodCaller>> getMethodCallers()
    {
        return methodCallers;
    }

    /**
     * Maps the methods of the class that are annotated with the given annotation.
     */
    private void map(Class<? extends Annotation> annotation) {
        for (Method declaredMethod : clazz.getDeclaredMethods())
        {

            if (!declaredMethod.isAnnotationPresent(annotation)) continue; // If the method is not annotated with the given annotation, skip it.
            if (declaredMethod.getParameterCount() != 1) continue; // If the method has more than one parameter, skip it as we won't be able to call it.

            Class<?> eventType = declaredMethod.getParameterTypes()[0]; // Get the type of the event that the method is listening for.
            MethodCaller caller = new MethodCaller(declaredMethod, eventType); // Create a new MethodCaller for the method.
            methodCallers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(caller); // Add the MethodCaller to the list of MethodCallers for the event type.
        }
    }
}
