package fi.jarno.sumblocks.resurrection.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fi.jarno.sumblocks.resurrection.Globals;

/**
 * Created by Jarno on 08-Jul-17.
 */

public class Block extends Actor{
    private ShapeRenderer _sh = new ShapeRenderer();
    private  Color _color = new Color();
    private GlyphLayout _textSize = new GlyphLayout();

    private Vector2 _gridPos;
    private int _colorID;
    private BitmapFont _font;
    private int _value = 0;

    public Block(float x, float y, float width, float height, int column, int row, int colorID, BitmapFont font){
        super();
        setBounds(x, y, width, height);
        setOrigin(width / 2, height / 2);
        _gridPos = new Vector2(column, row);
        _colorID = colorID;
        _font = font;
        _value = colorID * 10;
        updateColor();
        setZIndex(1);
    }

    public Vector2 getGridPos(){
        return _gridPos;
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
        _textSize.setText(_font, Integer.toString(_value));
        _font.draw(batch, Integer.toString(_value), (getX() + getOriginX()) - (_textSize.width / 2), (getY() + getOriginY()) - (_textSize.height / 2));
    }

    public void setGridPos(Vector2 newPos){
        _gridPos.set(newPos);
    }

    private void updateColor(){
        switch (_colorID){
            case Globals.BLOCK_RED_ID:
                _color.set(toColorFloat(239),
                           toColorFloat(83),
                           toColorFloat(80),
                           0);
                break;
            case Globals.BLOCK_GREEN_ID:
                _color.set(toColorFloat(102),
                           toColorFloat(187),
                           toColorFloat(106),
                           0);
                break;
            case Globals.BLOCK_BLUE_ID:
                _color.set(toColorFloat(66),
                           toColorFloat(165),
                           toColorFloat(245),
                           0);
                break;
            case Globals.BLOCK_ORANGE_ID:
                _color.set(toColorFloat(255),
                           toColorFloat(167),
                           toColorFloat(38),
                           0f);
                break;
            case Globals.BLOCK_PURPLE_ID:
                _color.set(toColorFloat(126),
                           toColorFloat(87),
                           toColorFloat(194),
                           0);
                break;
        }
    }

    private float toColorFloat(int colorValue){
        if(colorValue <= 0 || colorValue >= 255){
            return colorValue <= 0 ? 0f:1f;
        }
        return (colorValue / 255f);
    }
}
