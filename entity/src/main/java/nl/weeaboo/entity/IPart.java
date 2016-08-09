package nl.weeaboo.entity;

import java.io.Serializable;

public interface IPart extends Serializable {

    /**
     * @return A string representation of the part's state.
     */
    String toDetailedString();

    /**
     * This method is called whenever this part becomes attached to a world.
     * @param scene The scene this part is now attached to.
     */
    void onAttached(Scene scene);

    /**
     * This method is called whenever this part becomes detached to a world.
     * @param scene The scene this part is now detached from.
     */
    void onDetached(Scene scene);

    /**
     * @param signal The signal which is optionally handled by this part.
     */
    void handleSignal(ISignal signal);

}
