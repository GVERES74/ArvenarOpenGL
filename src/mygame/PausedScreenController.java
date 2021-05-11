/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.InputManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class PausedScreenController extends BaseAppState implements ScreenController{

    private SimpleApplication app;
    private InputManager inputManager;
    private Nifty nifty;
    private Screen screen;
    
    
    @Override
    protected void initialize(Application app) {
    
       this.inputManager = this.app.getInputManager();
       //inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
       
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
    
    public void settingsGame(){
        
        
        System.out.println("Game Settings button pressed...");
        //PlayGame.musicPlayer.stop();
        
        PlayGame.attachAppState(PlayGame.settings_screen);
        
    }
    
    public void showMap(){
        PlayGame.attachAppState(PlayGame.mapview_screen);
        
    }
    
    public void backToGame(){
        System.out.println("back button pressed...");
        PlayGame.detachAppState(PlayGame.paused_screen);
        PlayGame.detachAppState(PlayGame.mapview_screen);
        
        //also calls screen's onDisable() method
                
    }
    
    public void backToMainMenu(){
        System.out.println("back button pressed...");
        PlayGame.detachAppState(PlayGame.paused_screen); //also calls screen's onDisable() method
        PlayGame.detachAppState(PlayGame.gameplayState);
        System.exit(0);
        
    }
    

}