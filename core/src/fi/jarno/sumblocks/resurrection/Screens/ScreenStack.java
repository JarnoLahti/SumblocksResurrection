package fi.jarno.sumblocks.resurrection.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.ArrayList;

import fi.jarno.sumblocks.resurrection.SumblocksResurrection;

/**
 * Created by Jarno on 04-Jul-17.
 */
public class ScreenStack {
    private static ScreenStack ourInstance = new ScreenStack();

    public static ScreenStack getInstance() {
        return ourInstance;
    }

    private Game _game;

    private ArrayList<Screen> _screens;

    private int _stackSize;

    private ScreenStack() {
        _game = new SumblocksResurrection();
        _screens = new ArrayList();
        _stackSize = 0;
    }

    public Game getGame(){
        return _game;
    }

    public void PushScreen(Screen screen){
        _screens.add(screen);
        _stackSize = _screens.size() - 1;
        _game.setScreen(screen);
    }

    public void PopScreen(){
        _game.setScreen(_screens.remove(_stackSize));
        _stackSize = _screens.size() - 1;
    }
}
