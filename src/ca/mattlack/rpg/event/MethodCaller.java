package ca.mattlack.rpg.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class is used to call methods on an object with some event as the parameter.
 */
public class MethodCaller
{
    private final Method method;
    private final Class<?> eventType;

    public MethodCaller(Method method, Class<?> eventType) {
        this.method = method;
        this.eventType = eventType;
        method.setAccessible(true);
    }

    public Method getMethod()
    {
        return method;
    }

    public Class<?> getEventType()
    {
        return eventType;
    }

    public void call(Object object, Object event) throws InvocationTargetException, IllegalAccessException
    {
        // Check if the method is accessible yet. Accessible is false initially if the method is private.
        // If the method is private, we need to make it accessible before we can call it.
        // We check if it's accessible first because making it accessible is an expensive operation.
        if (!method.canAccess(object)) {
            method.setAccessible(true);
        }

        // Call the method.
        method.invoke(object, event);
    }
}
