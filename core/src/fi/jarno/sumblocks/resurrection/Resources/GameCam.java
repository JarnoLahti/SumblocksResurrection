package fi.jarno.sumblocks.resurrection.Resources;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by Jarno on 18-Nov-17.
 */

public class GameCam extends OrthographicCamera {
    private static final GameCam ourInstance = new GameCam();

    public static GameCam getInstance() {
        return ourInstance;
    }

    private GameCam() {
        super();
        setToOrtho(true);
    }
}
