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

import java.util.ArrayList;
import java.util.Random;

import fi.jarno.sumblocks.resurrection.Resources.BlockActions;
import fi.jarno.sumblocks.resurrection.Resources.MatchGroup;
import fi.jarno.sumblocks.resurrection.Resources.MergeMetadata;
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
    private BitmapFont font;
    private ShaderProgram fontShader;

    private Vector2 _touch = new Vector2();
    private Vector2 _drag = new Vector2();
    private Vector2 _delta = new Vector2();
    private Vector2 _srcGridPos = new Vector2();
    private Vector2 _dstGridPos = new Vector2();

    private Vector2[][] _blockPositions;

    private Block _destination, _source;

    boolean moveChecked = true;
    boolean blocksNeedToDrop = false;
    boolean repopulateChecked = true;
    ArrayList<Vector2> mergeBlockPositions = new ArrayList();

    public GameBoard(int posX, int posY, int width, int height, int cols, int rows){
        super();
        _cols = cols;
        _rows = rows;
        _boardWidth = width;
        _boardHeight = height;
        _blockWidth = _boardWidth / _cols;
        _blockHeight = _boardHeight / _rows;
        _blockPositions = new Vector2[_cols][_rows];
        font = initBlockFont();
        fontShader = new ShaderProgram(Gdx.files.internal("shaders/font.vert"), Gdx.files.internal("shaders/font.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
        }
        setBounds(posX, posY, width, height);
        setOrigin(width / 2, height / 2);
        initBoard();
    }

    @Override
    public void act(float delta) {
        if(!moveChecked && !childActionsRunning()){
            mergeBlockPositions.clear();
            //check for matches
            int[][] mergedMatchMap = getColorMatchMap(getColorArraySnapshot());

            if(!hasMatches(mergedMatchMap)){
                swapBlockSpots(_source, _destination);
                blocksNeedToDrop = false;
            }else{
                mergeBlockPositions.add(_source.getGridPos());
                mergeBlockPositions.add(_destination.getGridPos());
                mergeBlocks(getMatchGroups(mergeBlockPositions));
                blocksNeedToDrop = true;
            }
            _source = null;
            _destination = null;
            moveChecked = true;
        }

        if(blocksNeedToDrop && !childActionsRunning()){
            mergeBlockPositions.clear();

            float dropDelay = 0;
            float spawnDelay = 0;

            Block[][] blocks = getBlockArraySnapshot();

            int drop = 0;
            Block blc = null;

            for(int x = 0; x < _cols; x++){

                //drop blocks
                for(int y = _rows - 1; y >= 0; y--){
                    blc = blocks[x][y];
                    if(blc == null){
                        drop++;
                    }else if(drop > 0){
                        dropBlock(blc, y + drop, dropDelay);
                        mergeBlockPositions.add(blc.getGridPos());
                        dropDelay += BlockActions.BLOCK_DROP_DELAY;
                    }
                }

                //spawn new blocks
                for(int i = drop - 1; i >= 0; i--){
                    Vector2 pos = _blockPositions[x][0];
                    mergeBlockPositions.add(new Vector2(x, i));
                    Block block = new Block(
                            pos.x,
                            pos.y - _blockHeight - BLOCK_OFFSET,
                            _blockWidth - BLOCK_OFFSET,
                            _blockHeight - BLOCK_OFFSET,
                            x,
                            i,
                            randomizeBlockColor(),
                            font,
                            fontShader);
                    block.setScale(0);
                    block.setZIndex(i);
                    this.addActor(block);
                    spawnBlock(block, i, spawnDelay);
                    spawnDelay += BlockActions.BLOCK_SPAWN_DELAY;
                }
                spawnDelay = 0;
                dropDelay = 0;
                drop = 0;
            }
            blocksNeedToDrop = false;
            repopulateChecked = false;
        }

        if(!repopulateChecked && !childActionsRunning()){
            int[][] mergedMatchMap = getColorMatchMap(getColorArraySnapshot());

            if(!hasMatches(mergedMatchMap)){
                repopulateChecked = true;
                blocksNeedToDrop = false;
            }else{
                mergeBlocks(getMatchGroups(mergeBlockPositions));
                blocksNeedToDrop = true;
            }
        }

        super.act(delta);
    }

    private int[][] getColorMatchMap(int[][] colorSnapshot) {
        int[][] hMatchMap = new int[_cols][_rows];
        int[][] vMatchMap = new int[_cols][_rows];

        int match = 0;
        int lastColor = 0;

        // CHECK MATCHES HORIZONTIALLY
        for(int y = 0; y < _rows; y++){
            for(int x = 0; x < _cols; x++) {
                int currentColor = colorSnapshot[x][y];
                match = currentColor == lastColor ? match+1:0;
                lastColor = currentColor;
                if(match == 2){
                    //match found collect all previous x values
                    for(int i = (x - 2); i <= x; i++){
                        if(i >= 0) {
                            hMatchMap[i][y] = currentColor;
                        }
                    }
                }if(match > 2){
                    hMatchMap[x][y] = currentColor;
                }
            }
            lastColor = 0;
            match = 0;
        }

        // CHECK MATCHES VERTICALLY
        for(int x = 0; x < _cols; x++) {
            for(int y = 0; y < _rows; y++){
                int currentColor = colorSnapshot[x][y];
                match = currentColor == lastColor ? match+1:0;
                lastColor = currentColor;
                if(match == 2){
                    //match found collect all previous x values
                    for(int i = (y - 2); i <= y; i++){
                        if(i >= 0) {
                            vMatchMap[x][i] = currentColor;
                        }
                    }
                }if(match > 2){
                    vMatchMap[x][y] = currentColor;
                }
            }
            lastColor = 0;
            match = 0;
        }

        return mergeColorMatchMaps(hMatchMap, vMatchMap);
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

    private int[][] mergeColorMatchMaps(int[][] a, int[][] b) {
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

    private Block[][] getBlockArraySnapshot() {
        Block[][] blocks = new Block[_cols][_rows];
        Block b;
        Vector2 gridPos;
        for (Actor a:getChildren()) {
            if(a instanceof Block){
                b = (Block)a;
                gridPos = b.getGridPos();
                blocks[(int)gridPos.x][(int)gridPos.y] = b;
            }
        }
        return blocks;
    }

    private boolean childActionsRunning() {
        for (Actor a:getChildren()) {
            if(a.hasActions()){
                return true;
            }
        }
        return false;
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


        int[][] colorMap = initializeColorMap();

        // INIT THE BOARD WITH COLOR MAP
        float initDelay = 0;
        for(int y = 0; y < _rows; y++){
            for(int x = 0; x < _cols; x++) {
                float blockX = (x * _blockWidth) + BLOCK_OFFSET;
                float blockY = (y * _blockHeight) + BLOCK_OFFSET;

                Block block = new Block(
                        blockX,
                        blockY,
                        _blockWidth - BLOCK_OFFSET,
                        _blockHeight - BLOCK_OFFSET,
                        x,
                        y,
                        colorMap[x][y],
                        font,
                        fontShader);
                block.setScale(0);
                this.addActor(block);
                block.addAction(BlockActions.init(initDelay));
                initDelay += BlockActions.BLOCK_INIT_DELAY;
                _blockPositions[x][y] = new Vector2(blockX, blockY);
            }
        }
    }

    private int[][] initializeColorMap() {
        boolean noMatches;

        int[][] colorMap = new int[_cols][_rows];

        do{
            for(int y = 0; y < _rows; y++){
                for(int x = 0; x < _cols; x++) {
                    colorMap[x][y] = randomizeBlockColor();
                }
            }

            noMatches = !hasMatches(getColorMatchMap(colorMap));
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

        source.addAction(BlockActions.overSwap(destination));
        destination.addAction(BlockActions.underSwap(source));
    }

    private void dropBlock(Block block, int gridY, float delay){
        if(block == null){
            return;
        }
        block.setGridY(gridY);
        block.addAction(BlockActions.drop(_blockPositions[(int)block.getGridPos().x][gridY], delay));
    }

    private void spawnBlock(Block block, int gridY, float delay){
        if(block == null){
            return;
        }
        block.setGridY(gridY);
        block.addAction(BlockActions.spawn(_blockPositions[(int)block.getGridPos().x][gridY], delay));
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
                if(_drag.isZero() || childActionsRunning()){
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

    private ArrayList<MatchGroup> getMatchGroups(ArrayList<Vector2> mergeBlockPositions) {
        int[][] colorSnapshot = getColorArraySnapshot();
        int lastColor = 0;
        int match = 0;
        ArrayList<MatchGroup> hMatchGroups = new ArrayList();
        ArrayList<MatchGroup> vMatchGroups = new ArrayList();

        // CHECK MATCHES HORIZONTIALLY
        for(int y = 0; y < _rows; y++){
            for(int x = 0; x < _cols; x++) {
                int currentColor = colorSnapshot[x][y];
                match = currentColor == lastColor ? match+1:0;
                lastColor = currentColor;
                if(match == 2){
                    hMatchGroups.add(new MatchGroup(lastColor));
                    //match found collect all previous x values
                    for(int i = (x - 2); i <= x; i++){
                        if(i >= 0) {
                            hMatchGroups.get(hMatchGroups.size() - 1).addMetadata(new MergeMetadata(i, y));
                        }
                    }
                }if(match > 2){
                    hMatchGroups.get(hMatchGroups.size() - 1).addMetadata(new MergeMetadata(x, y));
                }
            }
            lastColor = 0;
            match = 0;
        }

        // CHECK MATCHES VERTICALLY
        for(int x = 0; x < _cols; x++) {
            for(int y = 0; y < _rows; y++){
                int currentColor = colorSnapshot[x][y];
                match = currentColor == lastColor ? match+1:0;
                lastColor = currentColor;
                if(match == 2){
                    vMatchGroups.add(new MatchGroup(lastColor));
                    //match found collect all previous x values
                    for(int i = (y - 2); i <= y; i++){
                        if(i >= 0) {
                            vMatchGroups.get(vMatchGroups.size() - 1).addMetadata(new MergeMetadata(x, i));
                        }
                    }
                }if(match > 2){
                    vMatchGroups.get(vMatchGroups.size() - 1).addMetadata(new MergeMetadata(x, y));
                }
            }
            lastColor = 0;
            match = 0;
        }

        ArrayList<MatchGroup> matchGroups = combineMatchGroups(hMatchGroups, vMatchGroups);

        for (Vector2 mbp:mergeBlockPositions) {
            for (MatchGroup mg:matchGroups) {
                mg.setMergeBlock((int)mbp.x, (int)mbp.y);
            }
        }

        return matchGroups;
    }

    private ArrayList<MatchGroup> combineMatchGroups(ArrayList<MatchGroup> hMatchGroups, ArrayList<MatchGroup> vMatchGroups) {
        boolean connectingGroupFound;

        ArrayList<MatchGroup> notConnectingGroups = new ArrayList();

        if(hMatchGroups.isEmpty()){
            return vMatchGroups;
        }

        for (MatchGroup hmg : hMatchGroups) {
            for (MatchGroup vmg: vMatchGroups) {
                connectingGroupFound = false;
                int i = 0;
                int removeIdx = 0;
                for (MergeMetadata md : vmg.getMetadata()) {
                    if(hmg.overlaps(vmg.getColor(), md.x, md.y)){
                        connectingGroupFound = true;
                        hmg.setMergeBlock(md.x, md.y);
                        removeIdx = i;
                    }
                    i++;
                }

                if(connectingGroupFound){
                    vmg.getMetadata().remove(removeIdx);
                    hmg.addMergeGroup(vmg);
                }else{
                    notConnectingGroups.add(vmg);
                }
            }
        }

        for (MatchGroup g: notConnectingGroups) {
            hMatchGroups.add(g);
        }

        return hMatchGroups;
    }

    private void mergeBlocks(ArrayList<MatchGroup> matchGroups){
        Block[][] blocks = getBlockArraySnapshot();
        float mergeDelayX = 0;
        float mergeDelayY = 0;
        for (MatchGroup mg : matchGroups) {
            MergeMetadata mergeBlock = mg.getMergeBlock();
            Block merge = blocks[mergeBlock.x][mergeBlock.y];
            merge.setZIndex(2);
            ArrayList<MergeMetadata> mergingBlocks = mg.getNonMergeBlocks();

            for (MergeMetadata merging:mergingBlocks) {
                Block b = blocks[merging.x][merging.y];
                merge.addToValue(b.getValue());
                b.setZIndex(0);
                if(mergeBlock.x == merging.x){
                    b.addAction(BlockActions.merge(b, merge.getX(), merge.getY(), mergeDelayY));
                    mergeDelayY += BlockActions.BLOCK_MERGE_DELAY;
                }else{
                    b.addAction(BlockActions.merge(b, merge.getX(), merge.getY(), mergeDelayX));
                    mergeDelayX += BlockActions.BLOCK_MERGE_DELAY;
                }

            }
        }
    }
}
