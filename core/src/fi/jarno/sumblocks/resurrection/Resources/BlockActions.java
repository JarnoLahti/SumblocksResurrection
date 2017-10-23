package fi.jarno.sumblocks.resurrection.Resources;


import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fi.jarno.sumblocks.resurrection.Actors.Block;

/**
 * Created by Jarno on 19-Oct-17.
 */

public class BlockActions {
    private static final float BLOCK_INIT_SCALE = 1.1f;
    public static final float BLOCK_INIT_DELAY = .02f;
    private static final float BLOCK_SWAP_SCALE_SOURCE = 1.1f;
    private static final float BLOCK_SWAP_SCALE_DESTINATION = .9f;
    private static final float SWAP_SPEED = .275f;

    public static Action blockOverSwap(final Block destination){
        return Actions.sequence(
                Actions.parallel(
                        Actions.moveTo(destination.getX(), destination.getY(), SWAP_SPEED, Interpolation.exp5Out),
                        Actions.sequence(
                                Actions.scaleTo(BLOCK_SWAP_SCALE_SOURCE, BLOCK_SWAP_SCALE_SOURCE, SWAP_SPEED / 2),
                                Actions.scaleTo(1f, 1f, SWAP_SPEED / 2))
                ));
    }

    public static Action blockUnderSwap(final Block source){
        return Actions.sequence(
                Actions.parallel(Actions.moveTo(source.getX(), source.getY(), SWAP_SPEED, Interpolation.exp5Out),
                        Actions.sequence(
                                Actions.scaleTo(BLOCK_SWAP_SCALE_DESTINATION, BLOCK_SWAP_SCALE_DESTINATION, SWAP_SPEED / 2),
                                Actions.scaleTo(1f, 1f, SWAP_SPEED / 2))
                ));
    }

    public static Action blockInit(float duration){
        return Actions.sequence(Actions.delay(duration), Actions.scaleTo(BLOCK_INIT_SCALE, BLOCK_INIT_SCALE, .3f), Actions.scaleTo(1f, 1f, .1f));
    }
}
