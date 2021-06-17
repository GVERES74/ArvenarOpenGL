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
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.NiftyControl;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import javax.annotation.Nonnull;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class HUDScreenController extends BaseAppState implements ScreenController{

 private Nifty nifty;
 private Screen screen;
 private NiftyControl imghealthbarvalue;
 
  
 private SimpleApplication app;
 private AppStateManager stateManager;
 
    
    @Override
    protected void initialize(Application app) {
      this.app = (SimpleApplication) app;
      this.stateManager = this.app.getStateManager();
      
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
            
            //targetName = stateManager.getState(GameAppState.class).getTargetName();
            //popupDialogBox();
            
        //TODO: implement behavior during runtime    
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        
        imghealthbarvalue = screen.findNiftyControl("HUD_PlayerHealthValueBar", NiftyControl.class);
               
        
    }

    @Override
    public void onStartScreen() {
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
    @NiftyEventSubscriber(id="ListBox_Dialog")
    public void onListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent event) {
                       
        if (event.getSelectionIndices().isEmpty()) {
            return;
        }
        else {
        System.out.println(event.getSelection());
        nifty.getCurrentScreen().findElementById("Panel_Dialog_Container").setVisible(false);
        app.getFlyByCamera().setDragToRotate(false);
        }
        
    }    
    
}