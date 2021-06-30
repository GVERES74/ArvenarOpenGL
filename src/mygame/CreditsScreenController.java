/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author TE332168
 */
public class CreditsScreenController extends BaseAppState implements ScreenController {
    private SimpleApplication app;
    private AppSettings appSettings;
    private Nifty nifty;
    private AppStateManager stateManager;
    private Screen screen;
    
    @Override
    public void initialize(Application app) {
        this.app = (SimpleApplication) app;
        this.stateManager = PlayGame.getPlayGameApp().getStateManager();
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup(Application app) {
        
        
    }

    @Override
    protected void onEnable() {
        
    }

    @Override
    protected void onDisable() {
        
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
    public void backToMainMenu(){
        System.out.println("back button pressed...");
        
        PlayGame.attachAppState(PlayGame.mainMenu_screen);
        PlayGame.detachAppState(PlayGame.credits_screen);
        Audioxerver.musicPlayer.stop();
        
            
    }
    
    
}
