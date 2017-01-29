package nl.weeaboo.entity;

public interface IPartListener {

    /**
     * Called when a part is attached to an entity.
     */
    void onPartAttached(Entity e, IPart p);

    /**
     * Called when a part is detached to an entity.
     */
    void onPartDetached(Entity e, IPart p);

    /**
     * Called when a property of a part changes.
     */
    void onPartPropertyChanged(Entity e, IPart p, String propertyName, Object newValue);

}
