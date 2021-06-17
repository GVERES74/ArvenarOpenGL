/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.FastMath;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class HUDScreen extends BaseAppState {

    private SimpleApplication app;
    private Nifty nifty;
    private Screen hudscreen;
    private ListBox dialogListBox;    
    private int screenWidth, screenHeight;
    
    
    @Override
    protected void initialize(Application app) {
        
        this.app = (SimpleApplication) app;
        screenWidth = PlayGame.getPlayGameAppSettings().getWidth();
        screenHeight = PlayGame.getPlayGameAppSettings().getHeight();
        
        createHUDScreen();
      
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
       
        //TODO: implement behavior during runtime    
    }
    
       
    
    public void createHUDScreen(){
        
        //app.getFlyByCamera().setDragToRotate(true);
        
        nifty = PlayGame.getNiftyDisplay().getNifty();
            app.getGuiViewPort().addProcessor(PlayGame.getNiftyDisplay());
            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
        
            //nifty.registerSound("btnclick", "Interface/sound/metalClick.ogg");
                                    
                   
            nifty.addScreen("Screen_HUD", new ScreenBuilder("Ingame HUD"){{
                controller(new mygame.HUDScreenController());
                //defaultFocusElement("settings_Apply");
                
                layer(new LayerBuilder("Layer_HUD"){{
                    childLayoutHorizontal();
                    
                                      
                    
                    panel(new PanelBuilder("Panel_HUD_PlayerStats"){{
                        //backgroundColor("#ee02");  
                        alignCenter();
                        
                        height("100%");
                        width("33%");
                        childLayoutVertical();
                        
                            image(new ImageBuilder("HUD_PlayerImg"){{
                            filename("Interface/Images/Hud/player.png");
                            alignLeft();
                            valignTop();
                            height("96px");
                            width("70px");                          
                            }});
                            
                            image(new ImageBuilder("HUD_PlayerHealthIcon"){{
                            filename("Interface/Images/Hud/heart.png");
                            
                            height("24px");
                            width("24px");                          
                            }});
                            
                            image(new ImageBuilder("HUD_PlayerHealthBar"){{
                            filename("Interface/Images/Hud/healthbar.png");
                            
                            height("10px");
                            width("200px");                          
                            }});
                            
                            image(new ImageBuilder("HUD_PlayerHealthValueBar"){{
                            filename("Interface/Images/Hud/healthvaluebar.png");
                            
                            height("9px");
                            width("200px");                          
                            }});
                           
                   
                                           
                            image(new ImageBuilder("HUD_MinimapImg"){{
                            filename("Interface/Images/Hud/minimap_base.png");
                            alignLeft();
                            valignBottom();
                            height("250px");
                            width("250px");                          
                            }}); 
                    }});
                    
                    panel(new PanelBuilder("Panel_HUD_Dialog"){{
                            
                            height("100%");
                            width("33%");
                            
                            childLayoutCenter();
                            
                            image(new ImageBuilder("img_crosshair"){{
                                filename("Interface/Images/Hud/crosshair.png");
                                alignCenter();
                                valignCenter();
                                height("26px");
                                width("26px");
                            }});
                            
                            control(new LabelBuilder("dialogText"){{
                                text("");
                                font("Interface/Fonts/Default.fnt");
                                height("100%");
                                width("100%");
                                visible(false);
                                alignCenter();
                                valignCenter();
                            }});
                            
                                  
                    
                    panel(new PanelBuilder("Panel_Dialog_Container"){{
                        backgroundColor("#6602");  
                       
                        height("30%");
                        width("100%");
                        childLayoutVertical();
                        
                        panel(new PanelBuilder("Panel_Dialog_NPCDialogText"){{
                        backgroundColor("#ee02");  
                        alignCenter();
                        valignCenter();
                        height("50%");
                        width("100%");
                        childLayoutCenter();
                        }});    
                        
                        panel(new PanelBuilder("Panel_Dialog_PlayerDialogs"){{
                        //backgroundColor("#ee02");  
                        alignCenter();
                        valignCenter();
                        height("50%");
                        width("100%");
                        padding("10px");
                        
                        childLayoutHorizontal();
                        
                            image(new ImageBuilder("Image_Dialog_CharacterImage"){{
                            filename("Interface/Images/Hud/player.png");
                            
                            
                            }});
                        
                        
                            control(new ListBoxBuilder("ListBox_Dialog"){{
                            displayItems(4);
                            selectionModeSingle();
                            hideVerticalScrollbar();
                            hideHorizontalScrollbar();
                            height("100%");
                            width("80%");
                            
                            
                            }}); 
                    }});
                                       
                    }});    
                    
                    }});    
                    }}); 
                
                }}.build(nifty));
                        
                nifty.gotoScreen("Screen_HUD");
                                    
    }    

    
    public void showHUDScreen(){
        nifty.gotoScreen("Screen_HUD");
        app.getFlyByCamera().setDragToRotate(false);
        
    }
    
    public void hideHUDScreen(){
        nifty.removeScreen("Screen_HUD");
        app.getFlyByCamera().setDragToRotate(true);
        
    }
    
    public void decreasePlayerHealthBar(){
        int healthpoints = nifty.getCurrentScreen().findElementById("HUD_PlayerHealthValueBar").getWidth();
            nifty.getCurrentScreen().findElementById("HUD_PlayerHealthValueBar").setWidth(healthpoints-10);
    }
    
    public void showLookAtDialog(Boolean enabled, String text){ //not used at the moment - alternative solution for dialog
       
        Picture pic = new Picture("DialogPanel");
            pic.setImage(app.getAssetManager(), "Interface/Images/Hud/dialogbox.png", true);
            pic.setWidth(600);
            pic.setHeight(100);
            pic.setPosition(screenWidth/2-300, screenHeight-200);
            
            
        
        BitmapFont dialogFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText dialogText = new BitmapText(dialogFont, false);
        dialogText.setText(text);
        dialogText.setLocalTranslation(screenWidth/2-100, screenHeight-200, 0); // position
        
            if (enabled){
                app.getGuiNode().attachChild(pic);
                app.getGuiNode().attachChild(dialogText);
            }
            else if (!enabled){
                app.getGuiNode().getChildren().clear();

            }
        
    }//not used right now - alternative solution for dialog
    
    public void createDialogPanel(Boolean enabled, String text){
                
        String[] comment = {"This is just a ", "This should be a ", "This looks to be a ", "Hmm, I would say it's a ", "I'm wondering if it's not a "};
        int r = FastMath.nextRandomInt(0, comment.length-1);
        if (enabled){
            nifty.getCurrentScreen().findElementById("dialogText").setVisible(true);
            nifty.getCurrentScreen().findElementById("Panel_HUD_Dialog").setVisible(true);
            nifty.getCurrentScreen().findNiftyControl("dialogText", Label.class).setText(comment[r]+text);
        
        }
        
        if (!enabled){
            nifty.getCurrentScreen().findElementById("dialogText").setVisible(false);
            nifty.getCurrentScreen().findElementById("Panel_HUD_Dialog").setVisible(false);
        }
    }
    
    public void showCharacterDialog(){
                
            app.getFlyByCamera().setDragToRotate(true);
            nifty.getCurrentScreen().findElementById("Panel_Dialog_Container").setVisible(true);
            dialogListBox = nifty.getCurrentScreen().findNiftyControl("ListBox_Dialog", ListBox.class);
            dialogListBox.clear();
            dialogListBox.addItem("I'm looking for my mother");
            dialogListBox.addItem("I'm lost, please tell me where I am");
            dialogListBox.addItem("Not now. (end conversation)");
            
        
    }
    
    
    

}//end class
