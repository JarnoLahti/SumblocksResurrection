package fi.jarno.sumblocks.resurrection.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Sort;

import fi.jarno.sumblocks.resurrection.Actors.Game.GameBoard;
import fi.jarno.sumblocks.resurrection.Actors.Game.GameScore;
import fi.jarno.sumblocks.resurrection.Globals;
import fi.jarno.sumblocks.resurrection.Resources.StageScreen;

/**
 * Created by Jarno on 04-Jul-17.
 */

public class GameScreen extends StageScreen{
    private GameBoard _board;
    private GameScore _gameScore;

    public GameScreen(){
        _board = new GameBoard(
                (Globals.VIRTUAL_WIDTH / 2) - (Globals.GAME_BOARD_WIDTH / 2),
                (Globals.VIRTUAL_HEIGHT / 2) - (Globals.GAME_BOARD_HEIGHT / 2) + Globals.GAME_BOARD_Y_OFFSET,
                Globals.GAME_BOARD_WIDTH,
                Globals.GAME_BOARD_HEIGHT,
                Globals.GAME_BOARD_COLUMNS,
                Globals.GAME_BOARD_ROWS);
        addActor(_board);

        _gameScore = new GameScore((Globals.VIRTUAL_WIDTH / 2) - (Globals.GAME_SCORE_WIDTH / 2), Globals.GAME_SCORE_Y, Globals.GAME_SCORE_WIDTH, Globals.GAME_SCORE_HEIGHT);
        _gameScore.setScale(0);
        addActor(_gameScore);

        _gameScore.setZIndex(0);
        _board.setZIndex(1);

        Sort.instance().sort(getActors());
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(_board.getInputAdapter());
    }

    @Override
    public void render(float delta) {
        if(_board.getUpdateScore()){
            _gameScore.updateScore(_board.getScore());
            _board.setUpdateScore(false);
        }
        super.render(delta);
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
