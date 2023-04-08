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
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.DropDownSelectionChangedEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
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
    private BaseAppState selectedAppStateID;
    
    private Element visiblePanel, img_activeGameMode;
    private static DropDown dropDownSingleLevel, dropDownMultiPlayer;
    private NiftyImage niftyImg_Summer, niftyImg_Winter, niftyImg_Spring, niftyImg_Autumn;
    
    private SimpleApplication app;
    private AppStateManager stateManager;
    
    public boolean load = false;
    public int frameCount = 0;  
    
    
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
        
        
        niftyImg_Summer = nifty.createImage(screen,"Interface/Images/Levels/S2/sl_tropical.png", true);
        niftyImg_Winter = nifty.createImage(screen,"Interface/Images/Levels/S0/sl_snowy.png", true);
        niftyImg_Spring = nifty.createImage(screen,"Interface/Images/Levels/S1/sl_forest.png", true);
        niftyImg_Autumn = nifty.createImage(screen,"Interface/Images/Levels/S3/sl_town.png", true);
        img_activeGameMode = screen.findElementById("img_SingleLevel");
                        
    }

    @Override
    public void onStartScreen() {
        
        initDropDownSingleLevelList();
        initDropDownMultiPlayerLevelList();
        
                
    }

    @Override
    public void onEndScreen() {
                
    }
    
    public void showSLMenu(){
        visiblePanel.setVisible(false);
        screen.findElementById("Panel_GameMode_SL").setVisible(true);
        visiblePanel = screen.findElementById("Panel_GameMode_SL");
        screen.findNiftyControl("text_GameMode", Label.class).setText("Single Level - Tour Mode");
    }
    
    public void showSPMenu(){
        visiblePanel.setVisible(false);
        screen.findElementById("Panel_GameMode_SP").setVisible(true);
        visiblePanel = screen.findElementById("Panel_GameMode_SP");
        screen.findNiftyControl("text_GameMode", Label.class).setText("Single Player - Story Mode");
    }
            
    public void showMPMenu(){
        visiblePanel.setVisible(false);
        screen.findElementById("Panel_GameMode_MP").setVisible(true);
        visiblePanel = screen.findElementById("Panel_GameMode_MP");
        screen.findNiftyControl("text_GameMode", Label.class).setText("Multi Player - LAN / Internet");
    }
    
    
    public static void startStoryMode(){
        PlayGame.app.getStateManager().detach(PlayGame.screenGameMode);
        PlayGame.app.getStateManager().attach(PlayGame.levelS0M0);
        
    }
    
    
    public static void startSingleLevel(){    
        PlayGame.app.getStateManager().detach(PlayGame.screenGameMode);
        PlayGame.app.getStateManager().attach(PlayGame.gameplayAppState);
                
        switch (dropDownSingleLevel.getSelectedIndex()){
            case 0: 
                    PlayGame.app.getStateManager().attach(PlayGame.levelS2M0);
                    break;
            case 1: 
                    PlayGame.app.getStateManager().attach(PlayGame.levelS0M0);
                    break;
            case 2:                     
                    PlayGame.app.getStateManager().attach(PlayGame.levelS1M0);
                    break;
            case 3:                     
                    PlayGame.app.getStateManager().attach(PlayGame.levelS3M0);
                    break; 
        }    
        
        PlayGame.screenLoading.setLoadLevelName(dropDownSingleLevel.getSelection().toString());
        PlayGame.screenLoading.setAssetName("Static model");
    }    
       
    
                
    public static void startMultiPlayer(){ 
    
        switch (dropDownMultiPlayer.getSelectedIndex()){
            case 0: 
                    PlayGame.app.getStateManager().attach(PlayGame.levelS2M0);
                    break;
            case 1: 
                    PlayGame.app.getStateManager().attach(PlayGame.levelS0M0);
                    break;
            case 2:                     
                    PlayGame.app.getStateManager().attach(PlayGame.levelS1M0);
                    break;
            case 3:                     
                    PlayGame.app.getStateManager().attach(PlayGame.levelS3M0);
                    break; 
        }
        
        PlayGame.screenLoading.setLoadLevelName(dropDownMultiPlayer.getSelection().toString());
    }   
    
    public void playGame(){
        PlayGame.screenGameMode.load = true;
        PlayGame.app.getStateManager().attach(PlayGame.screenLoading);
        
        
        
    }
    
    
    public void backToMainMenu(){
        System.out.println("Back button pressed...");
        PlayGame.app.getStateManager().detach(PlayGame.screenGameMode);
        PlayGame.app.getStateManager().attach(PlayGame.screenMainMenu);
    }
    
    @NiftyEventSubscriber(id="dropDown_SLLevelList")
    public void onDropDownChanged(final String id, @Nonnull final DropDownSelectionChangedEvent dropDownChangedEvent) {
                       
                //System.out.println(dropDownSingleLevel.getSelectedIndex());
        
        
        switch (dropDownSingleLevel.getSelectedIndex()){
            case 0: img_activeGameMode.getRenderer(ImageRenderer.class).setImage(niftyImg_Summer); 
                    screen.findNiftyControl("label_SingleLevel", Label.class).setText("You wake up on a deserted beach.\n"
                            + "At least it looks deserted and abandoned.");
            break;
            
            case 1: img_activeGameMode.getRenderer(ImageRenderer.class).setImage(niftyImg_Winter); 
                    screen.findNiftyControl("label_SingleLevel", Label.class).setText("Lost in a snowy valley.\n"
                            + "This valley is very cold and snow covered,\n"
                            + "finding a shelter is vital.");
            break;
            
            case 2: img_activeGameMode.getRenderer(ImageRenderer.class).setImage(niftyImg_Spring); 
                    screen.findNiftyControl("label_SingleLevel", Label.class).setText("What is better than roam\n "
                            + "a green grassy plain\n"
                            + " and a deep forest at dawn.");
            break;

            case 3: img_activeGameMode.getRenderer(ImageRenderer.class).setImage(niftyImg_Autumn); 
                    screen.findNiftyControl("label_SingleLevel", Label.class).setText("Harbor Town\n "
                            + "is usually crowded with various folks.\n"
                            + "Watch out for thieves and drunken mobs.");
            break;
            
                    
        }
        
    }
    
    public void initDropDownSingleLevelList(){
        
        dropDownSingleLevel.addItem("Tropical Beach");
        dropDownSingleLevel.addItem("Snowy Walley");
        dropDownSingleLevel.addItem("Green Forest");
        dropDownSingleLevel.addItem("Harbor Town");
        
        dropDownSingleLevel.selectItemByIndex(0);
               
    }
    
    public void initDropDownMultiPlayerLevelList(){
        dropDownMultiPlayer.addItem("Abandoned Shore");
        dropDownMultiPlayer.addItem("Woodcutter's camp");
        dropDownMultiPlayer.addItem("Lost in the forest");
        dropDownMultiPlayer.addItem("Urban legends");
        
        dropDownMultiPlayer.selectItemByIndex(0);
    }
    
    
}
