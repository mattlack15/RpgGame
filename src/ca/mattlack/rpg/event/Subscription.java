package ca.mattlack.rpg.event;

import java.util.*;

/**
 * This class is used to represent a subscription to an event by an object.
 */
public class Subscription
{

    private Object object;
    private Map<Class<?>, List<MethodCaller>> methodCallers = new HashMap<>();

    public Subscription(Object object)
    {
        this.object = object;
    }

    public Object getObject()
    {
        return object;
    }

    public Map<Class<?>, List<MethodCaller>> getMethodCallers()
    {
        return methodCallers;
    }

    public void addMethodCaller(Class<?> subscribedClass, MethodCaller methodCaller)
    {
        // Add a method caller associated with a class. The class will be some super class of the object stored in the object variable.
        methodCallers.computeIfAbsent(subscribedClass, k -> new ArrayList<>()).add(methodCaller);
    }

    public void removeMethodCaller(Class<?> subscribedClass, MethodCaller methodCaller)
    {
        // Remove all method callers that are associated with the class specified.
        methodCallers.computeIfPresent(subscribedClass, (k, v) ->
        {
            v.remove(methodCaller);
            return v.isEmpty() ? null : v; // Remove the list if it's empty, otherwise return the same list.
        });
    }

    public void removeSubscribedClass(Class<?> subscribedClass)
    {
        methodCallers.remove(subscribedClass);
    }

    public boolean isSubscribedClass(Class<?> subscribedClass)
    {
        return methodCallers.containsKey(subscribedClass);
    }
}
