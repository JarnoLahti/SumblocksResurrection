package fi.jarno.sumblocks.resurrection.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import fi.jarno.sumblocks.resurrection.Resources.BlockActions;
import fi.jarno.sumblocks.resurrection.Resources.BlockColors;

/**
 * Created by Jarno on 16-Nov-17.
 */

public class Background extends Group {
    private final int BLOCK_SIZE = 20;
    private final int BLOCK_OFFSET = 3;

    private Texture _texture;
    private Color _bgColor;
    private int _cols;
    private int _rows;
    private Random _random;

    float initTime;
    double initStartTime;
    boolean initDone = true;

    public Background(int posX, int posY, int width, int height){
        _random = new Random();
        _rows = height / BLOCK_SIZE;
        _cols = width / BLOCK_SIZE;
        _texture = new Texture(Gdx.files.internal("textures/block_gray.9.png"));
        _bgColor = new Color((66 / 255f), (66 / 255f), (66 / 255f), 1);
        setBounds(posX, posY, width, height);
        setOrigin(width / 2, height / 2);
        initBlocks();
    }

    @Override
    public void act(float delta) {
        for (Actor a:getChildren()) {
            if(a.hasActions()){
                continue;
            }
            if(MathUtils.randomBoolean(.0004f)){
                float time = _random.nextFloat() * (1f - .5f) + .5f;
                int length = 2 + (int)(Math.random() * 3);
                Color temp = null;
                switch (_random.nextInt(5) + 1){
                    case BlockColors.RED_ID:
                        temp = BlockColors.RED;
                        break;
                    case BlockColors.GREEN_ID:
                        temp = BlockColors.GREEN;
                        break;
                    case BlockColors.BLUE_ID:
                        temp = BlockColors.BLUE;
                        break;
                    case BlockColors.ORANGE_ID:
                        temp = BlockColors.ORANGE;
                        break;
                    case BlockColors.PURPLE_ID:
                        temp = BlockColors.PURPLE;
                        break;
                }

                if(temp != null){
                    int currentIdx = getChildren().indexOf(a, true);
                    float delay = 0;
                    for(int i = 0; i < length;i++){
                        if(currentIdx < getChildren().size) {
                            Actor actr = getChildren().get(currentIdx);
                            actr.addAction(BlockActions.pulseColor(delay, temp, a.getColor(), time));
                            currentIdx = getBlockBelowIndex(actr.getX(), actr.getY());
                            delay += time / 2;
                        }
                    }
                }
            }
        }
        super.act(delta);
    }

    private void initBlocks(){
        for(int y = 0; y < _rows; y++){
            for(int x = 0; x < _cols; x++){
                int bX = (x * BLOCK_SIZE) + BLOCK_OFFSET;
                int bY = (y * BLOCK_SIZE) + BLOCK_OFFSET;
                int bS = (BLOCK_SIZE - BLOCK_OFFSET);
                addActor(new Block(bX, bY, bS, bS, _bgColor, _texture));
            }
        }
    }

    private int getBlockBelowIndex(float posX, float posY){
        Actor[] actors = getChildren().toArray();
        posY += BLOCK_SIZE;
        for(int i = 0;i < actors.length; i++){
            Actor a = actors[i];
            if(a.getX() == posX && a.getY() == posY){
                return i;
            }
        }
        return getChildren().size + 1;
    }

    public void updateTiles(Array<Actor> actors){
        float hideDelay = 0;
        float showDelay = 0;
        for (Actor c:getChildren()) {
            for (Actor a:actors){
                if(((a.getX() < (c.getX() + c.getWidth())) && ((a.getX() + a.getWidth()) > c.getX())) && ((a.getY() < (c.getY() + c.getHeight())) && ((a.getY() + a.getHeight()) > c.getY()))){
                    c.addAction(BlockActions.hideBlock(hideDelay, c));
                    hideDelay += BlockActions.BACKGROUND_BLOCK_DELAY / 2;
                    continue;
                }else if(!a.isVisible()){
                    c.addAction(BlockActions.showBlock(showDelay, c));
                    showDelay += BlockActions.BACKGROUND_BLOCK_DELAY / 2;
                }
            }
        }
        initDone = false;
        initTime = ((showDelay > hideDelay ? showDelay:hideDelay) * 1000) / 2;
        initStartTime = System.currentTimeMillis();
    }

    public boolean canInit(){
        if(initDone){
            return false;
        }
        if(System.currentTimeMillis() - initStartTime > initTime){
            initDone = true;
            return true;
        }
        return false;
    }
}
