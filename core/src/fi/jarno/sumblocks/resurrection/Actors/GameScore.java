package fi.jarno.sumblocks.resurrection.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by Jarno on 17-Nov-17.
 */

public class GameScore extends DepthActor {
    private final int UPDATE_INTERVAL = 15;
    private GlyphLayout _textSize = new GlyphLayout();
    private ShaderProgram _fontShader;
    private BitmapFont _font;
    private int _currentScore = 0;
    private int _targetScore = 0;

    private double lastUpdate;
    public GameScore(float posX, float posY, int width, int height){
        setBounds(posX, posY, width, height);
        setOrigin(width / 2, height / 2);
        initFont();
        _fontShader = new ShaderProgram(Gdx.files.internal("shaders/font.vert"), Gdx.files.internal("shaders/font.frag"));
        if (!_fontShader.isCompiled()) {
            Gdx.app.error("fontShader", "compilation failed:\n" + _fontShader.getLog());
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(_currentScore < _targetScore){
            double time = System.currentTimeMillis();
            if(time - lastUpdate > UPDATE_INTERVAL){
                _currentScore++;
                lastUpdate = time;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setShader(_fontShader);
        _font.getData().setScale(getScaleX());
        _textSize.setText(_font, Integer.toString(_currentScore));
        _font.draw(batch, Integer.toString(_currentScore), (getX() + getOriginX()) - (_textSize.width / 2), (getY() + getOriginY()) - (_textSize.height / 2));
        batch.setShader(null);

    }

    public void updateScore(int newScore){
        _targetScore = newScore;
    }

    private void initFont() {
        Texture texture = new Texture(Gdx.files.internal("fonts/score.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        _font = new BitmapFont(Gdx.files.internal("fonts/score.fnt"), new TextureRegion(texture), true);
    }
}
