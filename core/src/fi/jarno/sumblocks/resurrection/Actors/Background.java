package fi.jarno.sumblocks.resurrection.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import fi.jarno.sumblocks.resurrection.Resources.BlockActions;
import fi.jarno.sumblocks.resurrection.Resources.BlockColors;

/**
 * Created by Jarno on 16-Nov-17.
 */

public class Background extends DepthGroup {
    private final int BLOCK_SIZE = 20;
    private final int BLOCK_OFFSET = 3;

    private Texture _texture;
    private Color _bgColor;
    private int _cols;
    private int _rows;
    private Random _random;

    public Background(int posX, int posY, int width, int height, Array<Actor> actors){
        _random = new Random();
        _rows = height / BLOCK_SIZE;
        _cols = width / BLOCK_SIZE;
        _texture = new Texture(Gdx.files.internal("textures/block_gray.png"));
        _bgColor = new Color((66 / 255f), (66 / 255f), (66 / 255f), 1);
        setBounds(posX, posY, width, height);
        setOrigin(width / 2, height / 2);
        initBlocks(actors);
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

    private void initBlocks(Array<Actor> actors){
        boolean init;
        for(int y = 0; y < _rows; y++){
            for(int x = 0; x < _cols; x++){
                int bX = (x * BLOCK_SIZE) + BLOCK_OFFSET;
                int bY = (y * BLOCK_SIZE) + BLOCK_OFFSET;
                int bS = (BLOCK_SIZE - BLOCK_OFFSET);
                init = true;

                //check if block would be behind and actor
                for (Actor a: actors){
                    if(((a.getX() < (bX + bS)) && ((a.getX() + a.getWidth()) > bX)) && ((a.getY() < (bY + bS)) && ((a.getY() + a.getHeight()) > bY))){
                        init = false;
                    }
                }

                //if something is in front of the block, we do not want to create it
                if(!init){
                    continue;
                }

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
}
