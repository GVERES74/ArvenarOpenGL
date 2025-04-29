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
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.FastMath;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class HUDScreenController extends BaseAppState implements ScreenController{

 private Nifty nifty;
 private Screen screen;
    
 private SimpleApplication app;
 private AppStateManager stateManager;
 private int screenWidth, screenHeight;
 private ListBox dialogListBox; 
 
 
    
    @Override
    protected void initialize(Application app) {
      this.app = (SimpleApplication) app;
      this.stateManager = this.app.getStateManager();
      
      screenWidth = PlayGame.appsettings.getWidth();
      screenHeight = PlayGame.appsettings.getHeight();
      
      
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
    
    
    showHUDScreen();
    System.out.println(this.getClass().getName()+" enabled....."); 

        //Called when the state is fully enabled, ie: is attached and         
        //isEnabled() is true or when the setEnabled() status changes after the         
        //state is attached.    
    }
    
    @Override
    protected void onDisable() {
    
     hideHUDScreen();    
    
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
                
    }

    @Override
    public void onStartScreen() {
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
    public void showHUDScreen(){
        nifty.gotoScreen("Screen_HUD");
        app.getFlyByCamera().setDragToRotate(false);
        
    }
    
    public void hideHUDScreen(){
        nifty.removeScreen("Screen_HUD");
        app.getFlyByCamera().setDragToRotate(true);
        
    }
    
     
     
    public void showLookAtDialog(Boolean enabled, String text){ //not used at the moment - alternative solution for dialog
       
        Picture pic = new Picture("DialogPanel");
            pic.setImage(PlayGame.app.getAssetManager(), "Interface/Images/Hud/dialogbox.png", true);
            pic.setWidth(600);
            pic.setHeight(100);
            pic.setPosition(screenWidth/2-300, screenHeight-200);
            
            
        
        BitmapFont dialogFont = PlayGame.app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText dialogText = new BitmapText(dialogFont, false);
        dialogText.setText(text);
        dialogText.setLocalTranslation(screenWidth/2-100, screenHeight-200, 0); // position
        
            if (enabled){
                PlayGame.app.getGuiNode().attachChild(pic);
                PlayGame.app.getGuiNode().attachChild(dialogText);
            }
            else if (!enabled){
                PlayGame.app.getGuiNode().getChildren().clear();

    }
        
    }//not used right now - alternative solution for dialog
    
     
    public void createAssetInfoPanel(Boolean enabled, String text){
                
        String[] comment = {"This is just a ", "This should be a ", "This looks to be a ", "Hmm, I would say it's a ", "I'm wondering if it's not a "};
        int r = FastMath.nextRandomInt(0, comment.length-1);
        
            
                PlayGame.nifty.getCurrentScreen().findElementById("dialogText").setVisible(enabled);
                PlayGame.nifty.getCurrentScreen().findElementById("Panel_HUD_Dialog").setVisible(enabled);
                PlayGame.nifty.getCurrentScreen().findNiftyControl("dialogText", Label.class).setText(comment[r]+text);
            
            if (text.contains("Oto")){
                showCharacterDialog();
            }
        
    }
    
    public void showCharacterDialog(){
            createDialogPanel();    
            PlayGame.app.getFlyByCamera().setDragToRotate(true);
            PlayGame.nifty.getCurrentScreen().findElementById("Panel_Dialog_Container").setVisible(true);
            PlayGame.nifty.getCurrentScreen().findElementById("Text_npcDialogText").setVisible(true);
                     
        
    }
    
    public void createDialogPanel(){
        dialogListBox = PlayGame.nifty.getCurrentScreen().findNiftyControl("ListBox_Dialog", ListBox.class);
        PlayGame.nifty.getCurrentScreen().findNiftyControl("Text_npcDialogText", Label.class).setText(PlayGame.gameplayAppState.getTarget()+": \n"
                + "Hello Stranger,\n"
                + "I'm "+ PlayGame.gameplayAppState.getTarget()+"\n"
                + "How can I help you?"
                + "This place is long forgotten and abandoned\n"
                + "You won't find here anything, but ruins.\n"
                );
            dialogListBox.clear();
            dialogListBox.addItem("I'm looking for my mother");
            dialogListBox.addItem("I'm lost, please tell me where I am");
            dialogListBox.addItem("Not now. (end conversation)");
    }
 
       
    
    
    @NiftyEventSubscriber(id="ListBox_Dialog")
    public void onListBoxSelectionChanged(final String id, final ListBoxSelectionChangedEvent event) {
            
        if (event.getSelectionIndices().isEmpty()) {
            return;
        }
        else {
        System.out.println(event.getSelection());
        PlayGame.nifty.getCurrentScreen().findElementById("Panel_Dialog_Container").setVisible(false);

        
        }
        
    }    
    
}