package researchsim.logging;

import researchsim.map.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * A detailed log that contains a record of {@link Event}s and contains some event statistics.
 *
 * @ass2
 */
public class Logger {
    /**
     * The number of entities that have been collected
     */
    private int entities;
    /**
     * The number of tiles that have been travelled
     */
    private int tiles;
    /**
     * The number of points that have been earned
     */
    private int points;
    /**
     * The list of events in the logger.
     */
    private List<Event> events;

    /**
     * Creates a new logger to maintain a list of events that occur in a scenario.
     * A logger keeps track of the following statistics (starting at 0):
     *
     * The number of entities that have been collected
     * The number of tiles that have been travelled
     * The number of points that have been earned
     */
    public Logger() {
        this.entities = 0;
        this.tiles = 0;
        this.points = 0;
        this.events = new ArrayList<>();
    }

    /**
     * Returns how many tiles have been traversed by entities.
     *
     * @return tiles traversed
     */
    public int getTilesTraversed() {
        return this.tiles;
    }

    /**
     * Returns how many entities have been collected by a user.
     *
     * @return entities collected
     */
    public int getEntitiesCollected() {
        return this.entities;
    }

    /**
     * Returns the number of points earned in a scenario.
     *
     * @return points earned
     */
    public int getPointsEarned() {
        return this.points;
    }

    /**
     * Returns all the events that have been logged.
     * Adding or removing elements from the returned list should not affect the original list.
     *
     * @return all events that have been logged
     */
    public List<Event> getEvents() {
        return new ArrayList<Event>(this.events);
    }

    /**
     * Adds an event to the log.
     * If an event is a CollectEvent then the number of entities collected should be incremented.
     * Additionally, the number of points for collecting that entity should be recorded.
     * If an event is a MoveEvent then the number of tiles traversed should be incremented
     * by the distance travelled in this event.
     *
     * @param event the new event
     */
    public void add(Event event) {
        if (event instanceof CollectEvent) {
            this.entities += 1;
            this.points += ((CollectEvent) event).getTarget().getSize().points;
        } else if (event instanceof MoveEvent) {
            int distanceX = event.getInitialCoordinate().distance(event.getCoordinate()).getAbsX();
            int distanceY = event.getInitialCoordinate().distance(event.getCoordinate()).getAbsY();
            this.tiles += distanceX + distanceY;
        }
        this.events.add(event);
    }

    /**
     * Returns the string representation of the event log. The format of the string to return is:
     *  logEntry
     *  logEntry
     *  ...
     * Where:
     * logEntry is the Event.toString() of an event in the log
     * IMPORTANT: The log entries should appear in the order in which they were added.
     * Additionally, each entry should be separated by a system-dependent line separator.
     * For example:
     *
     *  Dave [User] at (13,13)
     *  MOVED TO (12,12)
     *  -----
     *  Dave [User] at (12,12)
     *  COLLECTED
     *  Dog [Fauna] at (11,12) [LAND]
     *
     * @return human-readable string representation of log
     */
    @Override
    public String toString() {
        StringJoiner log = new StringJoiner(System.lineSeparator());
        for (Event event : this.getEvents()) {
            log.add(event.toString());
        }
        return log.toString();
    }
}
