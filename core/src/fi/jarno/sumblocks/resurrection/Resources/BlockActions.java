package fi.jarno.sumblocks.resurrection.Resources;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fi.jarno.sumblocks.resurrection.Actors.Block;

/**
 * Created by Jarno on 19-Oct-17.
 */

public class BlockActions {
    public static final float BLOCK_INIT_DELAY = .02f;
    public static final float BLOCK_MERGE_DELAY = .01f;
    public static final float BLOCK_DROP_DELAY = .035f;

    private static final float BLOCK_SPAWN_TIME = .2f;


    private static final float BLOCK_INIT_OVERSCALE_TIME = .15f;
    private static final float BLOCK_INIT_SCALE_TIME = .1f;

    private static final float BLOCK_INIT_SCALE = 1.1f;
    private static final float SWAP_TIME = .2f;
    private static final float DROP_TIME = .2f;
    private static final float MERGE_TIME = .15f;

    public static final float BLOCK_SPAWN_DELAY = (BLOCK_INIT_OVERSCALE_TIME + BLOCK_INIT_SCALE_TIME + DROP_TIME) / 2;

    public static Action overSwap(final Block destination){
        return Actions.moveTo(destination.getX(), destination.getY(), SWAP_TIME, Interpolation.exp5Out);
    }

    public static Action underSwap(final Block source){
        return Actions.moveTo(source.getX(), source.getY(), SWAP_TIME, Interpolation.exp5Out);
    }

    public static Action init(float delay){
        return Actions.delay(delay, Actions.sequence(Actions.scaleTo(BLOCK_INIT_SCALE, BLOCK_INIT_SCALE, BLOCK_INIT_OVERSCALE_TIME), Actions.scaleTo(1f, 1f, BLOCK_INIT_SCALE_TIME)));
    }

    public static Action merge(final Block block, float posX, float posY, float delay){
     return Actions.sequence(Actions.delay(delay, Actions.moveTo(posX, posY, MERGE_TIME)), Actions.run(new Runnable() {
         @Override
         public void run() {
             block.dispose();
             block.remove();
         }
     }));
    }

    public static Action drop(Vector2 pos, float delay){
        return Actions.delay(delay, Actions.moveTo(pos.x, pos.y, DROP_TIME, Interpolation.exp5Out));
    }

    public static Action spawn(Vector2 pos, float delay){
        return Actions.delay(delay, Actions.sequence(
                Actions.scaleTo(1f, 1f, BLOCK_SPAWN_TIME)
                , drop(pos, 0f)));
    }

    public static Action pulseColor(float delay, Color cToPulse, final Color cToReturn, final float time){
        return Actions.delay(delay, Actions.sequence(Actions.color(cToPulse, time), Actions.color(cToReturn, time)));
    }
}
