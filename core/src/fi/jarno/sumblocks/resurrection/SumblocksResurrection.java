package fi.jarno.sumblocks.resurrection;

import com.badlogic.gdx.Game;
import fi.jarno.sumblocks.resurrection.Screens.GameScreen;
import fi.jarno.sumblocks.resurrection.Screens.ScreenStack;

public class SumblocksResurrection extends Game{
	@Override
	public void create() {
		ScreenStack.getInstance().PushScreen(new GameScreen());
	}

}
