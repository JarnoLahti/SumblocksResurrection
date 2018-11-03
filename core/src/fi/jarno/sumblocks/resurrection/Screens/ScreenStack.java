package fi.jarno.sumblocks.resurrection.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.ArrayList;

import fi.jarno.sumblocks.resurrection.Resources.StageScreen;
import fi.jarno.sumblocks.resurrection.SumblocksResurrection;

/**
 * Created by Jarno on 04-Jul-17.
 */
public class ScreenStack {
    private static ScreenStack ourInstance = new ScreenStack();

    public static ScreenStack getInstance() {
        return ourInstance;
    }

    private SumblocksResurrection _game;

    private StageScreen _currentScreen;

    private ArrayList<StageScreen> _screens;

    private int _stackSize;

    private ScreenStack() {
        _game = new SumblocksResurrection();
        _screens = new ArrayList();
        _stackSize = 0;
        _currentScreen = null;
    }

    public Game getGame(){
        return _game;
    }

    public void PushScreen(StageScreen screen){
        _screens.add(screen);
        _stackSize = _screens.size() - 1;
        _currentScreen = screen;
        _game.updateBackgroundTiles(_currentScreen.getActors());
        _game.setScreen(_currentScreen);
    }

    public StageScreen getCurrentScreen(){
        return _currentScreen;
    }

    public void PopScreen(){
        _screens.remove(_stackSize);
        _stackSize = _screens.size() - 1;
        _currentScreen = _screens.get(_stackSize);
        _game.updateBackgroundTiles(_currentScreen.getActors());
        _game.setScreen(_currentScreen);
    }
}
