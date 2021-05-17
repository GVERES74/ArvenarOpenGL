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
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
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
    private int screenWidth, screenHeight;
    
    
    @Override
    protected void initialize(Application app) {
        
        this.app = (SimpleApplication) app;
        screenWidth = PlayGame.getPlayGameAppSettings().getWidth()-200;
        screenHeight = PlayGame.getPlayGameAppSettings().getHeight()/2;
        
        createHUDScreen();
        
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
        
           enableHUDScreen(); 
        //Called when the state is fully enabled, ie: is attached and         
        //isEnabled() is true or when the setEnabled() status changes after the         
        //state is attached.    
    }
    
    @Override
    protected void onDisable() {
       
        disableHUDScreen();
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
//                        popup(new PopupBuilder("popupExit") {{
//                            childLayoutCenter();
//                            backgroundColor("#000a");
//                        }}.registerPopup(nifty));
                            
                        
                
                }});
                }}.build(nifty));
                        
                nifty.gotoScreen("Screen_HUD");
                System.out.println(screenHeight);
    }
    
    public void enableHUDScreen(){
        nifty.gotoScreen("Screen_HUD");
        app.getFlyByCamera().setDragToRotate(false);
        
    }
    
    public void disableHUDScreen(){
        nifty.removeScreen("Screen_HUD");
        app.getFlyByCamera().setDragToRotate(true);
        
    }
    
    public void decreasePlayerHealthBar(){
        int healthpoints = nifty.getCurrentScreen().findElementById("HUD_PlayerHealthValueBar").getWidth();
                nifty.getCurrentScreen().findElementById("HUD_PlayerHealthValueBar").setWidth(healthpoints-10);
    }
}
