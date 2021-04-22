/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Menu;
import de.lessvoid.nifty.controls.MenuItemActivatedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;

/**
 *
 * @author TE332168
 */
public class MainMenuScreenController extends BaseAppState implements ScreenController{
    
    
    private PlayGame app;
    private Nifty nifty;
    private Screen screen;
    
    private Element popup;
            
    @Override
    public void initialize(Application app) {
       
       this.app = (PlayGame)app;
        
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
    
    public void startGame(String nextScreen){
        System.out.println("Play Game button pressed...");
        //nifty.gotoScreen(nextScreen);
        
    }
        
        
    public void settingsGame(String nextScreen){
        
        //nifty.gotoScreen(nextScreen);
        System.out.println("Game Settings button pressed...");
        
       
    }
    
    public void creditsGame(String nextScreen){
        System.out.println("Play Game button pressed...");
        //nifty.gotoScreen(nextScreen);
        
    }
    
    public void quitGame(){
        System.out.println("Game left...");
        //app.stop();
        System.exit(0);
        
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
