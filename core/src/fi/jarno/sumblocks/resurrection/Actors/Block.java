package fi.jarno.sumblocks.resurrection.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Jarno on 08-Jul-17.
 */

public class Block extends Actor{
    private ShapeRenderer _sh;
    public  Color _color;
    private Vector2 _gridPos;

    public Block(float x, float y, float width, float height, Vector3 color, int row, int column){
        super();
        setBounds(x, y, width, height);
        _sh = new ShapeRenderer();
        _color = new Color(color.x / 255, color.y / 255, color.z / 255, 0);
        setZIndex(1);
        _gridPos = new Vector2(column, row);
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
        _sh.rect(getX(),getY(), getWidth(), getHeight());
        _sh.end();

        _sh.begin(ShapeRenderer.ShapeType.Line);
        _sh.setColor(Color.BLACK);
        _sh.rect(getX(),getY(), getWidth(), getHeight());
        _sh.end();

        batch.begin();
    }
}
