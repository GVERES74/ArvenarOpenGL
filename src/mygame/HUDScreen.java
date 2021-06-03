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
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
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
                    childLayoutAbsoluteInside();
                    
                    image(new ImageBuilder("img_crosshair"){{
                        filename("Interface/Images/Hud/crosshair.png");
                        x(SizeValue.px(screenWidth/2-13));
                        y(SizeValue.px(screenHeight/2-13));
                        height("26px");
                        width("26px");
                    }});
                    
                    
                     panel(new PanelBuilder("Panel_HUD_PlayerStats"){{
                        //backgroundColor("#ee02");  
                        x("50px");
                        y("20px");
                        height("150px");
                        width("250px");
                        childLayoutAbsoluteInside();
                        
                            image(new ImageBuilder("HUD_PlayerImg"){{
                            filename("Interface/Images/Hud/player.png");
                            x("5px");
                            y("5px");
                            height("96px");
                            width("70px");                          
                            }});
                            
                            image(new ImageBuilder("HUD_PlayerHealthIcon"){{
                            filename("Interface/Images/Hud/heart.png");
                            x("5px");
                            y("105px");
                            height("24px");
                            width("24px");                          
                            }});
                            
                            image(new ImageBuilder("HUD_PlayerHealthBar"){{
                            filename("Interface/Images/Hud/healthbar.png");
                            x("35px");
                            y("110px");
                            height("10px");
                            width("200px");                          
                            }});
                            
                            image(new ImageBuilder("HUD_PlayerHealthValueBar"){{
                            filename("Interface/Images/Hud/healthvaluebar.png");
                            x("35px");
                            y("110px");
                            height("9px");
                            width("200px");                          
                            }});
                    }});        
                    
                   
                    panel(new PanelBuilder("Panel_HUD_Minimap"){{
                        //backgroundColor("#ee02");  
//                        y(SizeValue.px(screenHeight));
                        x("40px");
                        y("70%"); //minimap position as per screen resolution
                        height("255px");
                        width("255px");
                        childLayoutAbsoluteInside();
                        
                            image(new ImageBuilder("HUD_MinimapImg"){{
                            filename("Interface/Images/Hud/minimap_base.png");
                            x("5px");
                            y("5px");
                            height("250px");
                            width("250px");                          
                            }}); 
                    }});
                    
                    panel(new PanelBuilder("Panel_HUD_Dialog"){{
                            backgroundColor("#6665");  
                            height("100px");
                            width("600px");
                            x(SizeValue.px(screenWidth/2-300));
                            y(SizeValue.px(screenHeight/2+100));
                            visible(false);
                            childLayoutCenter();
                            
                            control(new LabelBuilder("dialogText"){{
                                text("");
                                font("Interface/Fonts/Default.fnt");
                                height("100%");
                                width("100%");
                                visible(false);
                                alignCenter();
                                valignCenter();
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
        
        if (enabled){
            nifty.getCurrentScreen().findElementById("dialogText").setVisible(true);
            nifty.getCurrentScreen().findElementById("Panel_HUD_Dialog").setVisible(true);
            nifty.getCurrentScreen().findNiftyControl("dialogText", Label.class).setText("This is just a "+text);
        
        }
        
        if (!enabled){
            nifty.getCurrentScreen().findElementById("dialogText").setVisible(false);
            nifty.getCurrentScreen().findElementById("Panel_HUD_Dialog").setVisible(false);
        }
    }

}//end class
