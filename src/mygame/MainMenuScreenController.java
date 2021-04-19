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
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
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
    
    
    private SimpleApplication app;
    private Node              rootNode;
    private AssetManager      assetManager;
    private AppStateManager   stateManager;
    private InputManager      inputManager;
    private RenderManager     renderManager;
    private AudioRenderer     audioRenderer;
    private ViewPort          viewPort;
    private Nifty nifty;
    
        
    @Override
    public void initialize(Application app) {
        
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
        this.app = (SimpleApplication) app; // can cast Application to something more specific
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
        this.nifty = nifty;
        
        
        SettingsScreen settingsAppState = new SettingsScreen(); 
            stateManager.attach(settingsAppState);
        
        NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
        nifty = niftyDisplay.getNifty();
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
    
    public void startGame(String nextScreen){
        System.out.println("Play Game button pressed...");
        nifty.gotoScreen(nextScreen);
    }
        
    
    public void settingsGame(String screenID){
//        nifty.gotoScreen(screenID);
        System.out.println("Game Settings button pressed...");
       
    }
    
    public void creditsGame(String nextScreen){
        System.out.println("Play Game button pressed...");
        nifty.gotoScreen(nextScreen);
    }
    
    public void quitGame(){
        System.out.println("Game left...");
        //this.app.stop();
        System.exit(0);
        
    }
    
    public void buttonEffect(Element element, NiftyMouseInputEvent event){
                
        System.out.println("Mouse Over: "+element.getId());
        element.setWidth(250);
               
        
    }
    
    public void showExitPopUp(){
        
    }
}
