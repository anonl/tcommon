package nl.weeaboo.entity;

public interface IPartListener {

    public void onPartAttached(Entity e, IPart p);

    public void onPartDetached(Entity e, IPart p);

    public void onPartPropertyChanged(Entity e, IPart p, String propertyName, Object newValue);

}
