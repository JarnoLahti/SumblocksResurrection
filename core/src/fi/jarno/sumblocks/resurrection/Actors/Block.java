package fi.jarno.sumblocks.resurrection.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fi.jarno.sumblocks.resurrection.Resources.BlockColors;

/**
 * Created by Jarno on 08-Jul-17.
 */

public class Block extends Actor{
    private Color _color = new Color();
    private GlyphLayout _textSize = new GlyphLayout();

    private Vector2 _gridPos;
    private BitmapFont _font;
    private ShaderProgram _fontShader;

    private int _value;
    private int _colorID;

    private Sprite _sprite;

    public Block(float x, float y, float width, float height, int column, int row, int colorID, BitmapFont font, ShaderProgram fontShader, Texture texture){
        super();
        _sprite = new Sprite(texture);
        setBounds(x, y, width, height);
        setOrigin(width / 2, height / 2);
        _sprite.setOrigin(getOriginX(), getOriginY());
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
        _sprite.draw(batch);
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
        _sprite.setColor(_color.r, _color.g, _color.b, 1);
    }

    @Override
    public void act(float delta) {
        _sprite.setScale(getScaleX(), getScaleY());
        super.act(delta);
    }

    @Override
    protected void positionChanged() {
        _sprite.setPosition(getX(), getY());
        super.positionChanged();
    }

    @Override
    protected void rotationChanged() {
        _sprite.setRotation(getRotation());
        super.rotationChanged();
    }

    @Override
    protected void sizeChanged() {
        _sprite.setSize(getWidth(), getHeight());
        super.sizeChanged();
    }

    public void dispose(){
        _font = null;
        _fontShader = null;
        _color = null;
    }


}
