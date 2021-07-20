/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Levels.S2M0_shore;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import javax.annotation.Nonnull;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class GameModeScreenController extends BaseAppState implements ScreenController{

    private Nifty nifty;
    private Screen screen;
    public static String selectedLevel;
    public static BaseAppState selectedAppStateID;
    private GameAppState gameAppState;
        
    
    private Element visiblePanel;
    private DropDown dropDownSingleLevel, dropDownMultiPlayer;
    
        
    
    @Override
    protected void initialize(Application app) {
        gameAppState = new GameAppState();
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
        
        visiblePanel = screen.findElementById("Panel_GameMode_SP");
        visiblePanel.setVisible(true);
        
        dropDownSingleLevel = screen.findNiftyControl("dropDown_SLLevelList", DropDown.class);
        dropDownMultiPlayer = screen.findNiftyControl("dropDown_MPLevelList", DropDown.class);
                
    }

    @Override
    public void onStartScreen() {
        
        dropDownSingleLevel.addItem("Tropical Beach");
        dropDownSingleLevel.addItem("Snowy Walley");
        
        dropDownSingleLevel.selectItemByIndex(0);
        
                
    }

    @Override
    public void onEndScreen() {
        
    }
    
    public void showSLMenu(){
        visiblePanel.setVisible(false);
        screen.findElementById("Panel_GameMode_SL").setVisible(true);
        visiblePanel = screen.findElementById("Panel_GameMode_SL");
    }
    
    public void showSPMenu(){
        visiblePanel.setVisible(false);
        screen.findElementById("Panel_GameMode_SP").setVisible(true);
        visiblePanel = screen.findElementById("Panel_GameMode_SP");
    }
            
    public void showMPMenu(){
        visiblePanel.setVisible(false);
        screen.findElementById("Panel_GameMode_MP").setVisible(true);
        visiblePanel = screen.findElementById("Panel_GameMode_MP");
    }
    
    public void playSelectedGameMode(){
        System.out.println("Play Game button pressed...");
        PlayGame.detachAppState(PlayGame.gameMode_screen);
        
        switch (dropDownSingleLevel.getSelectedIndex()){
            case 0: selectedLevel = "Scenes/S2_Summerdale/S2M0_shore.j3o"; selectedAppStateID = gameAppState.levelS2M0;
            case 1: selectedLevel = "Scenes/S0_Snowenar/S0M0_walley.j3o"; selectedAppStateID = gameAppState.levelS0M0;
        
        }
        
        PlayGame.attachAppState(PlayGame.gameplayState);
    }    
    
    public void backToMainMenu(){
        System.out.println("Back button pressed...");
        PlayGame.detachAppState(PlayGame.gameMode_screen);
    
    }
    
    @NiftyEventSubscriber(id="dropDown_SLLevelList")
    public void onDropDownChanged(final String id, @Nonnull final DropDownSelectionChangedEvent dropDownChangedEvent) {
                       
        System.out.println(dropDownSingleLevel.getSelectedIndex());
        
    }
    
       
        
}
