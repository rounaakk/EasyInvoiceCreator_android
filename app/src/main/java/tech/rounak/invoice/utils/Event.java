package tech.rounak.invoice.utils;

/**
 * Created by Rounak
 * For more info visit https://rounak.tech
 **/
public class Event<T> {

   private boolean hasBeenHandled = false;
    private T content;

    public Event(T content) {
        this.content = content;
    }

    /**
     * Returns the content and prevents its use again.
     */
    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }

    /**
     * Returns if the content is handled or not handled.
     */
    public boolean isHandled() {
        return hasBeenHandled;
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    public T peekContent() {
        return content;
    }
}

