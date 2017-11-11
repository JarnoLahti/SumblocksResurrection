package fi.jarno.sumblocks.resurrection.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fi.jarno.sumblocks.resurrection.Resources.BlockColors;

/**
 * Created by Jarno on 08-Jul-17.
 */

public class Block extends Actor{
    private ShapeRenderer _sh = new ShapeRenderer();
    private Color _color = new Color();
    private GlyphLayout _textSize = new GlyphLayout();

    private Vector2 _gridPos;
    private int _colorID;
    private BitmapFont _font;
    private int _value = 0;
    private ShaderProgram _fontShader;

    public Block(float x, float y, float width, float height, int column, int row, int colorID, BitmapFont font, ShaderProgram fontShader){
        super();
        setBounds(x, y, width, height);
        setOrigin(width / 2, height / 2);
        updateColor(colorID);
        _gridPos = new Vector2(column, row);
        _font = font;
        _value = 1;
        setZIndex(1);
        _fontShader = fontShader;
    }

    public Vector2 getGridPos(){
        return _gridPos;
    }

    public int getColorId(){
        return _colorID;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        _sh.setProjectionMatrix(batch.getProjectionMatrix());
        _sh.setTransformMatrix(batch.getTransformMatrix());

        _sh.begin(ShapeRenderer.ShapeType.Filled);
        _sh.setColor(_color);
        _sh.rect(getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        _sh.end();

        batch.begin();
        if(getScaleX() == 0 || getScaleY() == 0){
            return;
        }
        batch.setShader(_fontShader);
        _font.getData().setScale(getScaleX());
        _textSize.setText(_font, Integer.toString(_value));
        _font.draw(batch, Integer.toString(_value), (getX() + getOriginX()) - (_textSize.width / 2), (getY() + getOriginY()) - (_textSize.height / 2));
        batch.setShader(null);
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

    private void updateColor(int colorID){
        switch (colorID){
            case BlockColors.RED_ID:
                _color.set(BlockColors.RED);
                break;
            case BlockColors.GREEN_ID:
                _color.set(BlockColors.GREEN);
                break;
            case BlockColors.BLUE_ID:
                _color.set(BlockColors.BLUE);
                break;
            case BlockColors.ORANGE_ID:
                _color.set(BlockColors.ORANGE);
                break;
            case BlockColors.PURPLE_ID:
                _color.set(BlockColors.PURPLE);
                break;
            default:
                return;
        }
        _colorID = colorID;
    }

    public void dispose(){
        _font = null;
        _fontShader = null;
        _color = null;
    }
}
