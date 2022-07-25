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
import com.jme3.input.InputManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author TE332168
 */
public class MainMenuScreenController extends BaseAppState implements ScreenController{
    
    private Nifty nifty;
    private Screen screen;
    private SimpleApplication app;
    private AppStateManager stateManager;
    private InputManager    inputManager;
            
    private Element popup;
    
    @Override
    public void initialize(Application app) {
       
       this.app = (SimpleApplication) app;
       this.stateManager = this.app.getStateManager();
       this.inputManager = this.app.getInputManager();
    }  
        
    @Override
    public void update(float tpf) {
        
        
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
    
    public void startGame(){
        System.out.println("Play Game button pressed...");
        
        PlayGame.detachAppState(PlayGame.screenMainMenu);
        PlayGame.attachAppState(PlayGame.screenGameMode);
        
        //Optional
        //AudioManager.musicPlayer.stop();
                              
    }
        
        
    public void settingsGame(){
        
        System.out.println("Game Settings button pressed...");
                
        /*THIS THE IDEAL WAY!! 
        When the user clicks on the options button, the StartScreenAppState method
        attaches an OptionsScreenAppState object and detaches itself.
        */
        PlayGame.attachAppState(PlayGame.screenSettings);
        PlayGame.detachAppState(PlayGame.screenMainMenu);
        
    }
    
    public void creditsGame(){
        System.out.println("Game Credits button pressed...");
        
        
        PlayGame.attachAppState(PlayGame.screenCredits);
        AudioManager.musicPlayer.stop();
        AudioManager.soundPlayer.stop(); 
        
    }
    
    public void gameExtras(){
        System.out.println("Game Extras button pressed...");
        
        PlayGame.attachAppState(PlayGame.screenExtras);
        //AudioManager.musicPlayer.stop();
        AudioManager.soundPlayer.stop();       
        
    }
    
    public void quitGame(){
        System.out.println("Game left...");
        PlayGame.getPlayGameApp().stop();
                //System.exit(0);
        
    }
   
    
    public void showExitPopUp(){
        
    }

    @Override
    protected void cleanup(Application app) {
      
    }

    @Override
    protected void onEnable() {
        
    }

    @Override
    protected void onDisable() {
        
    }

    
}
