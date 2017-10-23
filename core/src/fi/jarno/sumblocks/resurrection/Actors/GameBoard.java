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

    private Vector2 _touch = new Vector2();
    private Vector2 _drag = new Vector2();
    private Vector2 _delta = new Vector2();
    private Vector2 _srcGridPos = new Vector2();
    private Vector2 _dstGridPos = new Vector2();

    private Block _destination, _source;

    boolean moveChecked = true;

    @Override
    public void act(float delta) {
        if(!moveChecked && !childrenActionsRunning()){
            //check for matches
            int[][] colorSnapShot = getColorArraySnapshot();

            int match = 1;
            int lastColor = 10;

            int[][] hMatchMap = new int[_cols][_rows];
            int[][] vMatchMap = new int[_cols][_rows];

            // CHECK MATCHES HORIZONTIALLY
            for(int y = 0; y < _rows; y++){
                for(int x = 0; x < _cols; x++) {
                    int currentColor = colorSnapShot[x][y];
                    match = currentColor == lastColor ? match+1:1;
                    lastColor = currentColor;
                    if(match == 3){
                       //match found collect all previous x values
                        for(int i = (x - 2); i <= x; i++){
                            hMatchMap[i][y] = currentColor;
                        }
                    }if(match > 3){
                        hMatchMap[x][y] = currentColor;
                    }
                }
                lastColor = 10;
                match = 1;
            }

            // CHECK MATCHES VERTICALLY
            for(int x = 0; x < _cols; x++) {
                for(int y = 0; y < _rows; y++){
                    int currentColor = colorSnapShot[x][y];
                    match = currentColor == lastColor ? match+1:1;
                    lastColor = currentColor;
                    if(match == 3){
                        //match found collect all previous x values
                        for(int i = (y - 2); i <= y; i++){
                            vMatchMap[x][i] = currentColor;
                        }
                    }if(match > 3){
                        vMatchMap[x][y] = currentColor;
                    }
                }
                lastColor = 10;
                match = 1;
            }

            int[][] mergedMatchMap = mergeMatchMaps(hMatchMap, vMatchMap);

            if(!hasMatches(mergedMatchMap)){
                swapBlockSpots(_source, _destination);
            }

            // FIND A WAY TO CHECK CONNECTED MATCHES AND DECIDE THE BLOCK WHICH WILL GET ALL OTHER BLOCKS MERGED
            // CORNER BLOCK SHOULD ALWAYS BE THE ONE WHICH MERGES BECAUSE IT CAN ONLY HAPPEN BY USER INTERACTION

            moveChecked = true;
        }
        //repopulate
        //check repopulate
        super.act(delta);
    }

    private boolean hasMatches(int[][] mergedMatchMap) {
        for(int y = 0; y < mergedMatchMap[0].length; y++) {
            for(int x = 0; x < mergedMatchMap.length; x++) {
                if(mergedMatchMap[x][y] > 0){
                    return true;
                }
            }
        }
        return false;
    }

    private int[][] mergeMatchMaps(int[][] a, int[][] b) {
        for(int y = 0; y < _rows; y++) {
            for(int x = 0; x < _cols; x++) {
                if(a[x][y] == 0 && b[x][y] > 0){
                    a[x][y] = b[x][y];
                }
            }
        }
        return a;
    }

    private int[][] getColorArraySnapshot() {
        int[][] blockColors = new int[_cols][_rows];
        Block b;
        Vector2 gridPos;
        for (Actor a:getChildren()) {
            if(a instanceof Block){
                b = (Block)a;
                gridPos = b.getGridPos();
                blockColors[(int)gridPos.x][(int)gridPos.y] = b.getColorId();
            }
        }
        return blockColors;
    }

    private boolean childrenActionsRunning() {
        for (Actor a:getChildren()) {
            if(a.hasActions()){
                return true;
            }
        }
        return false;
    }

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
        initBoard();
    }

    private BitmapFont initBlockFont() {
        BitmapFont blockFont;
        Texture texture = new Texture(Gdx.files.internal("fonts/block_font.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        blockFont = new BitmapFont(Gdx.files.internal("fonts/block_font.fnt"), new TextureRegion(texture), true);
        return blockFont;
    }

    private void initBoard(){
        getChildren().clear();
        BitmapFont font = initBlockFont();
        ShaderProgram fontShader = new ShaderProgram(Gdx.files.internal("shaders/font.vert"), Gdx.files.internal("shaders/font.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
        }

        int[][] colorMap = initializeColorMap();

        // INIT THE BOARD WITH COLOR MAP
        float initDelay = 0;
        for(int y = 0; y < _rows; y++){
            for(int x = 0; x < _cols; x++) {
                Block block = new Block(
                        (x * _blockWidth) + BLOCK_OFFSET,
                        (y * _blockHeight) + BLOCK_OFFSET,
                        _blockWidth - BLOCK_OFFSET,
                        _blockHeight - BLOCK_OFFSET,
                        x,
                        y,
                        colorMap[x][y],
                        font,
                        fontShader);
                block.setScale(0);
                this.addActor(block);
                block.addAction(BlockActions.blockInit(initDelay));
                initDelay += BlockActions.BLOCK_INIT_DELAY;
            }
        }
    }

    private int[][] initializeColorMap() {
        boolean noMatches;

        int[][] colorMap = new int[_cols][_rows];

        do{
            noMatches = true;

            for(int y = 0; y < _rows; y++){
                for(int x = 0; x < _cols; x++) {
                    colorMap[x][y] = randomizeBlockColor();
                }
            }

            int match = 1;
            int lastColor = 10;

            // CHECK MATCHES HORIZONTIALLY
            for(int y = 0; y < _rows; y++){
                for(int x = 0; x < _cols; x++) {
                    int currentColor = colorMap[x][y];
                    match = currentColor == lastColor ? match+1:1;
                    lastColor = currentColor;
                    if(match > 2){
                        noMatches = false;
                    }
                }
                lastColor = 10;
                match = 1;
            }

            if(!noMatches){
                continue; // if we already got a match, no need to check vertically
            }

            // CHECK MATCHES VERTICALLY
            for(int x = 0; x < _cols; x++) {
                for(int y = 0; y < _rows; y++){
                    int currentColor = colorMap[x][y];
                    match = currentColor == lastColor ? match+1:1;
                    lastColor = currentColor;
                    if(match > 2){
                        noMatches = false;
                    }
                }
                lastColor = 10;
                match = 1;
            }

        }while(!noMatches);

        return colorMap;
    }

    private int randomizeBlockColor(){
        return new Random().nextInt(5) + 1;
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

        _srcGridPos.set(source.getGridPos());
        _dstGridPos.set(destination.getGridPos());

        source.setGridPos(_dstGridPos);
        destination.setGridPos(_srcGridPos);

        source.addAction(BlockActions.blockOverSwap(destination));
        destination.addAction(BlockActions.blockUnderSwap(source));
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
                moveChecked = false;
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
