package fi.jarno.sumblocks.resurrection.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Jarno on 08-Jul-17.
 */

public class Block extends DepthActor{

    protected Sprite _sprite;

    public Block(float x, float y, float width, float height, Texture texture){
        super();
        _sprite = new Sprite(texture);
        setBounds(x, y, width, height);
        setOrigin(width / 2, height / 2);
        _sprite.setOrigin(getOriginX(), getOriginY());
    }

    public Block(float x, float y, float width, float height, Color color, Texture texture){
        super();
        _sprite = new Sprite(texture);
        setBounds(x, y, width, height);
        setOrigin(width / 2, height / 2);
        setColor(color);
        _sprite.setOrigin(getOriginX(), getOriginY());
        _sprite.setColor(color);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        _sprite.draw(batch);
    }

    @Override
    public void act(float delta) {
        _sprite.setColor(getColor());
        _sprite.setAlpha(getColor().a);
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

    public void dispose(){}
}
