package fi.jarno.sumblocks.resurrection.Actors.ui;

import fi.jarno.sumblocks.resurrection.Actors.CustomActor;
import fi.jarno.sumblocks.resurrection.Resources.BlockActions;

/**
 * Created by Jarno on 19-Nov-17.
 */

public class ButtonActor extends CustomActor {
    @Override
    public void initAnimation(float delay) {
        addAction(BlockActions.showBlock(delay, this));
    }


}
