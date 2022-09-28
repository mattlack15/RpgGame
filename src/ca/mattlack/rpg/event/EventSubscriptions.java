package ca.mattlack.rpg.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * A class that manages subscriptions to events in a pub-sub system.
 */
public class EventSubscriptions {

    private static EventSubscriptions instance;

    public static EventSubscriptions getInstance() {
        if (instance == null) {
            instance = new EventSubscriptions(EventSubscription.class);
        }
        return instance;
    }

    // Map of the event class to the subscriptions.
    private final Map<Class<?>, List<Subscription>> subscriptionMap = new HashMap<>();

    // Class schema.
    private final Map<Class<?>, ClassSchema> schemaMap = new HashMap<>();

    private final Class<? extends Annotation> annotation;

    public EventSubscriptions(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public <T> void subscribe(T object, Class<?> subscriberClass) {

        ClassSchema schema = schemaMap.get(subscriberClass);
        if (schema == null) {
            schema = new ClassSchema(subscriberClass, annotation);
            schemaMap.put(subscriberClass, schema);
        }

        EVENT_TYPE_LOOP: // Loop through the event types and their methods.
        for (Map.Entry<Class<?>, List<MethodCaller>> entry : schema.getMethodCallers().entrySet()) {

            Class<?> eventClass = entry.getKey();
            List<MethodCaller> methodCallers = entry.getValue();

            // Get the subscriptions for the event type.
            List<Subscription> subscriptions = subscriptionMap.computeIfAbsent(eventClass, k -> new ArrayList<>());

            // Loop through the subscriptions.
            for (Subscription subscription : subscriptions) {

                // If the object is already subscribed.
                if (subscription.getObject() == object) {
                    if (!subscription.isSubscribedClass(subscriberClass)) {

                        // Add the subscriber class to the subscription.
                        for (MethodCaller methodCaller : methodCallers) {
                            subscription.addMethodCaller(subscriberClass, methodCaller);
                        }

                    }
                    continue EVENT_TYPE_LOOP;
                }
            }

            // Create a new subscription.
            Subscription subscription = new Subscription(object);
            for (MethodCaller methodCaller : methodCallers) {
                subscription.addMethodCaller(subscriberClass, methodCaller);
            }

            // Add the subscription to the list.
            subscriptions.add(subscription);

        }
    }

    public void unsubscribe(Object object, Class<?> subscriberClass) {

        // Get the schema for the subscriber class.
        ClassSchema schema = schemaMap.get(subscriberClass);
        if (schema == null) {
            return;
        }

        for (Map.Entry<Class<?>, List<MethodCaller>> entry : schema.getMethodCallers().entrySet()) {
            Class<?> eventClass = entry.getKey();

            // Get the subscriptions for the event class.
            List<Subscription> subscriptions = subscriptionMap.get(eventClass);
            if (subscriptions == null) { // No subscriptions for this event class, continue to the next event class.
                continue;
            }

            for (Iterator<Subscription> iterator = subscriptions.iterator(); iterator.hasNext(); ) {
                Subscription subscription = iterator.next();

                if (subscription.getObject() == object) {

                    // Remove the subscriber class from the subscription.
                    subscription.removeSubscribedClass(subscriberClass);

                    // Remove the subscription entirely if there are no more subscribed classes in this subscription.
                    if (subscription.getMethodCallers().size() == 0) {
                        iterator.remove();
                    }
                }

            }

        }
    }

    public void publish(Object event) {

        // Get the event class.
        Class<?> eventClass = event.getClass();

        // Get the subscriptions for this event class.
        List<Subscription> subscriptions = subscriptionMap.get(eventClass);
        if (subscriptions == null) { // If there are no subscriptions, return.
            return;
        }

        // For each subscription.
        for (Subscription subscription : new ArrayList<>(subscriptions)) {

            // For all the methods that need to be called.
            for (Map.Entry<Class<?>, List<MethodCaller>> entry : subscription.getMethodCallers().entrySet()) {

                // Get the method callers.
                List<MethodCaller> methodCallers = entry.getValue();

                // For each method caller.
                for (MethodCaller methodCaller : methodCallers) {

                    // Try to call the method.
                    try {
                        methodCaller.call(subscription.getObject(), event);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
