package fi.jarno.sumblocks.resurrection.Resources;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import fi.jarno.sumblocks.resurrection.Actors.CustomActor;
import fi.jarno.sumblocks.resurrection.Actors.CustomGroup;
import fi.jarno.sumblocks.resurrection.Globals;

/**
 * Created by Jarno on 18-Nov-17.
 */

public abstract class StageScreen extends Stage implements Screen {
    private boolean initDone = false;

    @Override
    public void show() {
        setViewport(new ExtendViewport(Globals.VIRTUAL_WIDTH, Globals.VIRTUAL_HEIGHT, GameCam.getInstance()));
    }

    public void initScreen(){
        for (Actor a: getActors()) {
            if(a instanceof CustomActor){
                ((CustomActor) a).initAnimation(0);
            }
            if(a instanceof CustomGroup){
                ((CustomGroup) a).initAnimation(0);
            }
        }
    };

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        getViewport().apply(false);
        getBatch().setProjectionMatrix(getCamera().combined);
        act(delta);
        draw();
    }

    public boolean initDone(){
        return initDone;
    }

    public void setInitDone(boolean value){
        initDone = value;
    }
}
