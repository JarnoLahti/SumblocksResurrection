package fi.jarno.sumblocks.resurrection.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import fi.jarno.sumblocks.resurrection.Actors.GameBoard;
import fi.jarno.sumblocks.resurrection.Globals;
import fi.jarno.sumblocks.resurrection.Resources.GameCamera;

/**
 * Created by Jarno on 04-Jul-17.
 */

public class GameScreen extends Stage implements Screen {
    private GameCamera _camera;
    private GameBoard _board;

    @Override
    public void show() {
        _camera = new GameCamera();
        _camera.setToOrtho(true);
        setViewport(new ExtendViewport(Globals.VIRTUAL_WIDTH, Globals.VIRTUAL_HEIGHT, _camera));
        _board = new GameBoard(
                (Globals.VIRTUAL_WIDTH / 2) - (Globals.GAME_BOARD_WIDTH / 2),
                (Globals.VIRTUAL_HEIGHT / 2) - (Globals.GAME_BOARD_HEIGHT / 2),
                Globals.GAME_BOARD_WIDTH,
                Globals.GAME_BOARD_HEIGHT,
                Globals.GAME_BOARD_COLUMNS,
                Globals.GAME_BOARD_ROWS);

        addActor(_board);
        Gdx.input.setInputProcessor(_board.getInputAdapter());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor((66 / 255f), (66 / 255f), (66 / 255f), 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getViewport().apply(false);
        getBatch().setProjectionMatrix(getCamera().combined);
        act(delta);
        draw();
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {}

}
