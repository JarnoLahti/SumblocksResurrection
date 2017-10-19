package fi.jarno.sumblocks.resurrection.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.Random;

import fi.jarno.sumblocks.resurrection.Resources.BlockActions;
import fi.jarno.sumblocks.resurrection.Resources.SwipeDirection;

/**
 * Created by Jarno on 04-Jul-17.
 */

public class GameBoard extends Group{
    private final int BLOCK_OFFSET = 3;

    private float _boardWidth;
    private float _boardHeight;
    private int _cols;
    private int _rows;
    private float _blockWidth;
    private float _blockHeight;

    private Vector2 _touch = new Vector2(),
                    _drag = new Vector2(),
                    _delta = new Vector2();

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
        ShaderProgram fontShader = new ShaderProgram(Gdx.files.internal("shaders/font.vert"), Gdx.files.internal("shaders/font.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
        }
        initBoard(font, fontShader);
    }

    private BitmapFont initBlockFont() {
        BitmapFont blockFont;
        Texture texture = new Texture(Gdx.files.internal("fonts/block_font.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        blockFont = new BitmapFont(Gdx.files.internal("fonts/block_font.fnt"), new TextureRegion(texture), true);
        return blockFont;
    }

    private void initBoard(BitmapFont font, ShaderProgram fontShader){
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
                        font,
                        fontShader);
                this.addActor(block);
            }
        }
    }

    private int randomizeBlockColor(){
        return new Random().nextInt(5);
    }

    private void handleInput(final Vector2 touchPos, final SwipeDirection direction){
        Actor a = hit(touchPos.x, touchPos.y, false);

        if(!(a instanceof Block)){
            return;
        }

        _source = (Block)a;

        if(_source == null) {
            return;
        }

        if(_source.hasActions()){
            return;
        }

        switch (direction){
            case left:
                _destination = getBlockFromGridPos((int)_source.getGridPos().x - 1, (int)_source.getGridPos().y);
                break;
            case right:
                _destination = getBlockFromGridPos((int)_source.getGridPos().x + 1, (int)_source.getGridPos().y);
                break;
            case up:
                _destination = getBlockFromGridPos((int)_source.getGridPos().x, (int)_source.getGridPos().y - 1);
                break;
            case down:
                _destination = getBlockFromGridPos((int)_source.getGridPos().x, (int)_source.getGridPos().y + 1);
                break;
            default:
                return;
        }

        if(_destination != null){
            swapBlockSpots(_source, _destination);
        }
    }

    private Block getBlockFromGridPos(int col, int row){
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
        if(source == null && destination == null){
            return;
        }

        source.setZIndex(2);
        destination.setZIndex(1);

        final Vector2 srcGridPos = new Vector2().set(source.getGridPos());
        final Vector2 dstGridPos = new Vector2().set(destination.getGridPos());

        source.addAction(BlockActions.blockOverSwap(source, destination, dstGridPos));
        destination.addAction(BlockActions.blockUnderSwap(source, destination, srcGridPos));
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
                    return true;
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
