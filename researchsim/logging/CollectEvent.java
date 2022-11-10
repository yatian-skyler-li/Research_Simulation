package researchsim.logging;

import researchsim.entities.Entity;
import researchsim.entities.User;

import java.util.StringJoiner;

/**
 * The collection of an entity that implemented {@link researchsim.util.Collectable} by a
 * {@link User}.
 *
 * @ass2
 */
public class CollectEvent extends Event {
    /** The target entity that is being collected */
    private Entity target;

    /**
     * Creates a new collect event, this is an event where a user collects research on an entity.
     *
     * @param user the user collecting a target
     * @param target the target entity that is being collected
     */
    public CollectEvent(User user, Entity target) {
        super(user, target.getCoordinate());
        this.target = target;
    }

    /**
     * Returns the target that was collected.
     *
     * @return event target
     */
    public Entity getTarget() {
        return this.target;
    }

    /**
     * Returns the string representation of the collect event.
     * The format of the string to return is:
     *
     *  user
     *  COLLECTED
     *  entity
     *  -----
     * Where:
     * user is the toString() of the user that collected the entity
     * entity is the toString() of the collected entity
     * IMPORTANT: The coordinate in the user string should be the coordinate that the user was
     * in when the event occurred and NOT its current coordinate
     * ADDITIONALLY: The coordinate in the entity string should be the coordinate that the entity
     * was in when the event occurred and NOT its current coordinate
     *
     * Each entry should be separated by a system-dependent line separator.
     *
     * For example:
     *
     *  Dave [User] at (12,12)
     *  COLLECTED
     *  Dog [Fauna] at (11,12) [LAND]
     *  -----
     *
     * Note that there is no trailing newline.
     *
     * @return human-readable string representation of this collect event.
     */
    public String toString() {
        StringJoiner collect = new StringJoiner(System.lineSeparator());
        collect.add(this.getEntity().toString());
        collect.add("COLLECTED");
        collect.add(this.getTarget().toString());
        collect.add("-----");
        return collect.toString();
    }
}
