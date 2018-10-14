package fi.jarno.sumblocks.resurrection.Actors.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import fi.jarno.sumblocks.resurrection.Actors.CustomActor;
import fi.jarno.sumblocks.resurrection.Resources.BlockActions;

/**
 * Created by Jarno on 23-Nov-17.
 */

public class Logo extends CustomActor {

    private Sprite _sprite;

    public Logo(float posX, float posY, int width, int height){
        setBounds(posX, posY, width, height);
        setOrigin(width / 2, height / 2);
        _sprite = new Sprite(new Texture(Gdx.files.internal("textures/logo.png")));
        _sprite.setPosition(posX, posY);
        _sprite.setOrigin(getOriginX(), getOriginY());
        _sprite.setAlpha(1);
        _sprite.flip(false, true);
    }

    @Override
    public void initAnimation(float delay) {
        addAction(BlockActions.init(delay));
    }

    @Override
    public void act(float delta) {
        _sprite.setColor(getColor());
        _sprite.setAlpha(getColor().a);
        _sprite.setScale(getScaleX(), getScaleY());
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        _sprite.draw(batch);
    }
}
