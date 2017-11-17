package fi.jarno.sumblocks.resurrection.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

import fi.jarno.sumblocks.resurrection.Resources.BlockColors;

/**
 * Created by Jarno on 12-Nov-17.
 */

public class GameBlock extends Block {
    private GlyphLayout _textSize = new GlyphLayout();
    private BitmapFont _font;
    private ShaderProgram _fontShader;
    private Vector2 _gridPos;
    private int _colorID;
    private int _value;

    public GameBlock(float x, float y, float width, float height, int column, int row, int colorID, BitmapFont font, ShaderProgram fontShader, Texture texture) {
        super(x, y, width, height, texture);
        setColor(colorID);
        _gridPos = new Vector2(column, row);
        _font = font;
        _value = 1;
        _fontShader = fontShader;
    }

    private void setColor(int colorID){
        switch (colorID){
            case BlockColors.RED_ID:
                setColor(BlockColors.RED);
                break;
            case BlockColors.GREEN_ID:
                setColor(BlockColors.GREEN);
                break;
            case BlockColors.BLUE_ID:
                setColor(BlockColors.BLUE);
                break;
            case BlockColors.ORANGE_ID:
                setColor(BlockColors.ORANGE);
                break;
            case BlockColors.PURPLE_ID:
                setColor(BlockColors.PURPLE);
                break;
            default:
                return;
        }
        _colorID = colorID;
        _sprite.setColor(getColor());
        _sprite.setAlpha(1);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(getScaleX() == 0 || getScaleY() == 0){
            return;
        }
        batch.setShader(_fontShader);
        _font.getData().setScale(getScaleX());
        _textSize.setText(_font, Integer.toString(_value));
        _font.draw(batch, Integer.toString(_value), (getX() + getOriginX()) - (_textSize.width / 2), (getY() + getOriginY()) - (_textSize.height / 2));
        batch.setShader(null);
    }

    public Vector2 getGridPos(){
        return _gridPos;
    }

    public void setGridPos(Vector2 newPos){
        _gridPos.set(newPos);
    }

    public void setGridY(float y){ _gridPos.set(_gridPos.x, y); }

    public int getValue(){
        return _value;
    }

    public void addToValue(int value){
        _value += value;
    }

    public int getColorId(){
        return _colorID;
    }
    @Override
    public void dispose(){
        _font = null;
        _fontShader = null;
    }
}
