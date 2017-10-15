package fi.jarno.sumblocks.resurrection;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import fi.jarno.sumblocks.resurrection.Screens.ScreenStack;
import fi.jarno.sumblocks.resurrection.SumblocksResurrection;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(ScreenStack.getInstance().getGame(), config);
	}
}
