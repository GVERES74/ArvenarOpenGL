/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.NiftyControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.File;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class MapViewScreenController extends BaseAppState implements ScreenController{

    private Nifty nifty;
    private Screen screen;
    private Element mapholder;
    private Element world_map, local_map; 
    
    
    @Override
    protected void initialize(Application app) {
    
        
        //It is technically safe to do all initialization and cleanup in the         
        //onEnable()/onDisable() methods. Choosing to use initialize() and         
        //cleanup() for this is a matter of performance specifics for the         
        //implementor.        
        //TODO: initialize your AppState, e.g. attach spatials to rootNode    
    }

    @Override
    protected void cleanup(Application app) {
    
    
        //TODO: clean up what you initialized in the initialize method,        
        //e.g. remove all spatials from rootNode    
    }

    //onEnable()/onDisable() can be used for managing things that should     
    //only exist while the state is enabled. Prime examples would be scene     
    //graph attachment or input listener attachment.    
    @Override
    protected void onEnable() {
    


        //Called when the state is fully enabled, ie: is attached and         
        //isEnabled() is true or when the setEnabled() status changes after the         
        //state is attached.    
    }
    
    @Override
    protected void onDisable() {
    


        //Called when the state was previously enabled but is now disabled         
        //either because setEnabled(false) was called or the state is being         
        //cleaned up.    
    }
    
    @Override
    public void update(float tpf) {
    
    
        //TODO: implement behavior during runtime    
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        
        local_map = screen.findElementById("img_LocalMap");
        world_map = screen.findElementById("img_WorldMap");
        mapholder = screen.findElementById("Panel_MapView_MapHolder");
    }

    @Override
    public void onStartScreen() {
        mapholder.addChild(local_map);
        mapholder.addChild(world_map);
    }

    @Override
    public void onEndScreen() {
        
    }
    
    public void showLocalMap(){
        local_map.setVisible(true);
        world_map.setVisible(false);
        System.out.println("Local Map");
    }
    
    public void showWorldMap(){
        local_map.setVisible(false);
        world_map.setVisible(true);
        System.out.println("World Map");
    }
    
    public void backToGame(){
        System.out.println("Back button pressed...");
        
        PlayGame.detachAppState(PlayGame.mapview_screen);
        
        //also calls screen's onDisable() method
                
    }
}
