package fi.jarno.sumblocks.resurrection.Screens;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fi.jarno.sumblocks.resurrection.Actors.ui.Logo;
import fi.jarno.sumblocks.resurrection.Globals;
import fi.jarno.sumblocks.resurrection.Resources.StageScreen;

/**
 * Created by Jarno on 18-Nov-17.
 */

public class MainMenuScreen extends StageScreen {

    private Logo _logo;

    public MainMenuScreen(){
        _logo = new Logo((Globals.VIRTUAL_WIDTH / 2) - (Globals.LOGO_WIDTH / 2), 60, Globals.LOGO_WIDTH, Globals.LOGO_HEIGHT);
        _logo.setScale(0);
        addActor(_logo);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
