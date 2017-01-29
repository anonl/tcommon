package nl.weeaboo.entity;

public interface ISignal {

    /**
     * Returns {@code true} if this signal was previously marked as handled.
     * @see #setHandled()
     */
    boolean isHandled();

    /**
     * Marks this signal as handled. Further signal handlers should ignore this signal in most cases.
     */
    void setHandled();

}
