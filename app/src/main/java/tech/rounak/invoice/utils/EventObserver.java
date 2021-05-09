package tech.rounak.invoice.utils;

import androidx.lifecycle.Observer;

/**
 * Created by Rounak
 * For more info visit https://rounak.tech
 **/
public class EventObserver<T> implements Observer<Event<? extends T>> {

    private EventUnhandledContent<T> content;

    public interface EventUnhandledContent<T> {
        void onEventUnhandledContent(T t);
    }


    public EventObserver(EventUnhandledContent<T> content) {
        this.content = content;
    }

    @Override
    public void onChanged(Event<? extends T> event) {
        if (event != null) {
            T result = event.getContentIfNotHandled();
            if (result != null && content != null) {
                content.onEventUnhandledContent(result);
            }
        }
    }
}
