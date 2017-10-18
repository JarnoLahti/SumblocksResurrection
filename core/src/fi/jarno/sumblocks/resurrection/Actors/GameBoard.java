package fi.jarno.sumblocks.resurrection.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.Random;

import fi.jarno.sumblocks.resurrection.Resources.SwipeDirection;

/**
 * Created by Jarno on 04-Jul-17.
 */

public class GameBoard extends Group{
    private final float SWAP_SPEED = 0.275f;
    private final int BLOCK_OFFSET = 3;
    private final float BLOCK_SWAP_SCALE_SOURCE = 1.05f;
    private final float BLOCK_SWAP_SCALE_DESTINATION = .9f;

    private float _boardWidth;
    private float _boardHeight;
    private int _cols;
    private int _rows;
    private float _blockWidth;
    private float _blockHeight;

    private Vector2 _touch = new Vector2(),
                    _drag = new Vector2(),
                    _delta = new Vector2(),
                    _srcGridPos = new Vector2(),
                    _dstGridPos = new Vector2();

    private Block _destination, _source;

    public GameBoard(int posX, int posY, int width, int height, int cols, int rows){
        super();
        _cols = cols;
        _rows = rows;
        _boardWidth = width;
        _boardHeight = height;
        _blockWidth = _boardWidth / _cols;
        _blockHeight = _boardHeight / _rows;
        setBounds(posX, posY, width, height);
        setOrigin(width / 2, height / 2);
        BitmapFont font = initBlockFont();
        initBoard(font);
    }

    private BitmapFont initBlockFont() {
        BitmapFont blockFont;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = new Color(1,1,1,1);
        parameter.size = 30;
        parameter.flip = true;
        blockFont = generator.generateFont(parameter);
        generator.dispose();
        return blockFont;
    }

    private void initBoard(BitmapFont font){
        for(int y = 0; y < _rows; y++){
            for(int x = 0; x < _cols; x++) {
                Block block = new Block(
                        (x * _blockWidth) + BLOCK_OFFSET,
                        (y * _blockHeight) + BLOCK_OFFSET,
                        _blockWidth - BLOCK_OFFSET,
                        _blockHeight - BLOCK_OFFSET,
                        x,
                        y,
                        randomizeBlockColor(),
                        font);
                this.addActor(block);
            }
        }
    }

    private int randomizeBlockColor(){
        return new Random().nextInt(5);
    }

    private void handleInput(final Vector2 touchPos, final SwipeDirection direction){
        Actor a = hit(touchPos.x, touchPos.y, false);

        _source = (Block)a;

        if(_source == null) {
            return;
        }

        if(_source.hasActions()){
            return;
        }
        switch (direction){
            case left:
                swapBlockSpots(_source, getActorFromGridPos((int)_source.getGridPos().x - 1, (int)_source.getGridPos().y));
                break;
            case right:
                swapBlockSpots(_source, getActorFromGridPos((int)_source.getGridPos().x + 1, (int)_source.getGridPos().y));
                break;
            case up:
                swapBlockSpots(_source, getActorFromGridPos((int)_source.getGridPos().x, (int)_source.getGridPos().y - 1));
                break;
            case down:
                swapBlockSpots(_source, getActorFromGridPos((int)_source.getGridPos().x, (int)_source.getGridPos().y + 1));
                break;
            default:
                return;
        }
    }

    private Block getActorFromGridPos(int col, int row){
        for(Actor a : getChildren()){
            if(a instanceof Block){
                _destination = (Block)a;
                if(_destination.getGridPos().x == col && _destination.getGridPos().y == row){
                    return _destination;
                }
            }
        }
        return null;
    }

    private void swapBlockSpots(final Block source, final Block destination){
        if(source != null && destination != null){
            source.setZIndex(2);
            destination.setZIndex(1);

            _srcGridPos.set(source.getGridPos());
            _dstGridPos.set(destination.getGridPos());

            source.addAction(Actions.sequence(
                    Actions.parallel(Actions.moveTo(destination.getX(), destination.getY(), SWAP_SPEED, Interpolation.exp5Out),
                    Actions.sequence(
                            Actions.scaleTo(BLOCK_SWAP_SCALE_SOURCE, BLOCK_SWAP_SCALE_SOURCE, SWAP_SPEED / 2),
                            Actions.scaleTo(1f, 1f, SWAP_SPEED / 2))
                    ), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            source.setGridPos(_dstGridPos);
                        }
                    })
            ));

            destination.addAction(Actions.sequence(
                    Actions.parallel(Actions.moveTo(source.getX(), source.getY(), SWAP_SPEED, Interpolation.exp5Out),
                    Actions.sequence(
                            Actions.scaleTo(BLOCK_SWAP_SCALE_DESTINATION, BLOCK_SWAP_SCALE_DESTINATION, SWAP_SPEED / 2),
                            Actions.scaleTo(1f, 1f, SWAP_SPEED / 2))
                    ), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            destination.setGridPos(_srcGridPos);
                        }
                    })
            ));
        }
    }

    public InputAdapter getInputAdapter(){
        return new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                getStage().getViewport().unproject(_touch.set(screenX, screenY));
                stageToLocalCoordinates(_touch);
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                getStage().getViewport().unproject(_drag.set(screenX, screenY));
                stageToLocalCoordinates(_drag);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if(_drag.isZero()){
                    return false;
                }

                _delta = _drag.sub(_touch);

                if(Math.abs(_delta.x) > Math.abs(_delta.y)){
                    handleInput(_touch, _delta.x > 0 ? SwipeDirection.right:SwipeDirection.left);
                } else {
                    handleInput(_touch, _delta.y > 0 ? SwipeDirection.down:SwipeDirection.up);
                }

                _touch.set(0,0);
                _drag.set(0,0);
                _delta.set(0,0);

                return true;
            }
        };
    }
}
