package fi.jarno.sumblocks.resurrection.Actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Created by Jarno on 16-Nov-17.
 */

public class DepthGroup extends Group implements Comparable<Actor> {
    @Override
    public int compareTo(Actor g) {
        return getZIndex() < g.getZIndex() ? -1 : (getZIndex() > g.getZIndex() ? 1 : 0);
    }
}
