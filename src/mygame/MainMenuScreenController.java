/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author TE332168
 */
public class MainMenuScreenController extends BaseAppState implements ScreenController{
    
    SimpleApplication app;
    
    @Override
    public void initialize(Application app) {
        
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup(Application app) {
        
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }

    
    @Override
    protected void onEnable() {
        
    }

    @Override
    protected void onDisable() {
        
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        
    }

    @Override
    public void onStartScreen() {
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
    public void startGame(){
        System.out.println("Play Game button pressed...");
    }
    
    public void quitGame(){
        System.out.println("Game left...");
        //this.app.stop();
        //System.exit(0);
        
    }
    
    public void buttonEffect(Element element, NiftyMouseInputEvent event){
                
        System.out.println("Mouse Over: "+element.getId());
        element.setWidth(120);
               
        
    }
    
    public void showExitPopUp(){
        
    }
}
