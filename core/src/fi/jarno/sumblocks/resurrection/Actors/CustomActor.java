package fi.jarno.sumblocks.resurrection.Actors;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Jarno on 16-Nov-17.
 */

public abstract class CustomActor extends Actor implements Comparable<Actor> {
    @Override
    public int compareTo(Actor a) {
        return getZIndex() < a.getZIndex() ? -1 : (getZIndex() > a.getZIndex() ? 1 : 0);
    }

    public abstract void initAnimation(float delay);
}
