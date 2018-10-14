package fi.jarno.sumblocks.resurrection;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import fi.jarno.sumblocks.resurrection.Actors.Background;
import fi.jarno.sumblocks.resurrection.Resources.GameCam;
import fi.jarno.sumblocks.resurrection.Resources.StageScreen;
import fi.jarno.sumblocks.resurrection.Screens.GameScreen;
import fi.jarno.sumblocks.resurrection.Screens.MainMenuScreen;
import fi.jarno.sumblocks.resurrection.Screens.ScreenStack;

public class SumblocksResurrection extends Game{
	private Background _bg;
	private SpriteBatch _batch;

	@Override
	public void create() {
		_bg = new Background(
				0,
				0,
				Globals.BACKGROUND_WIDTH,
				Globals.BACKGROUND_HEIGHT);
		ScreenStack.getInstance().PushScreen(new GameScreen());
		_batch = new SpriteBatch();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor((48 / 255f), (48 / 255f), (48 / 255f), 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(_bg.canInit() && !((StageScreen)getScreen()).initDone()){
			((StageScreen)getScreen()).initScreen();
			((StageScreen)getScreen()).setInitDone(true);
		}
		_bg.act(Gdx.graphics.getDeltaTime());
		_batch.setProjectionMatrix(GameCam.getInstance().combined);
		_batch.begin();
		_bg.draw(_batch, 1);
		super.render();
		_batch.end();
	}

	public void updateBackgroundTiles(Array<Actor> actors){
		_bg.updateTiles(actors);
	}
}
