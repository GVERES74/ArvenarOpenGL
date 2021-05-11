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
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.screen.Screen;



/**
 *
 * @author TE332168
 */
public class PausedScreen extends BaseAppState {
    
    private SimpleApplication app;
    private Node              rootNode;
    private AssetManager      assetManager;
    private AppStateManager   stateManager;
    private InputManager      inputManager;
    private RenderManager     renderManager;
    private AudioRenderer     audioRenderer;
    private ViewPort          viewPort;
    
    private Nifty nifty;
    private Screen screen;
                
    int screenWidth;
    
    @Override
    public void initialize(Application app) {
        
        this.app = (SimpleApplication) app; // can cast Application to something more specific
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
        screenWidth = PlayGame.getPlayGameAppSettings().getWidth();
                
        createPausedScreen();

    }
    
    @Override
    public void update(float tpf) {
        
    }
    
    
    @Override
    protected void cleanup(Application app) {
        System.out.println("PausedScreen cleanup called.....");
    }

     @Override
    protected void onEnable() {
        
    }
      


    @Override
    protected void onDisable() {

       nifty.removeScreen("Screen_PausedMenu");
       PlayGame.ingameHud.enableHud(); //ez csak itt van, máshol nem kell meghívni
       
       app.getFlyByCamera().setDragToRotate(false); //mouse freelook
                        
    }
    
    
    public void createPausedScreen(){
        
        app.getFlyByCamera().setDragToRotate(true);
        
        nifty = PlayGame.getNiftyDisplay().getNifty();
            app.getGuiViewPort().addProcessor(PlayGame.getNiftyDisplay());
            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
        
            nifty.registerSound("btnclick", "Interface/sound/metalClick.ogg");
                   
            nifty.addScreen("Screen_PausedMenu", new ScreenBuilder("Game Paused"){{
                controller(new mygame.PausedScreenController());
                defaultFocusElement("settings_Apply");
                
                layer(new LayerBuilder("Layer_Settings_AllItems"){{
                    childLayoutAbsoluteInside();
                    
//                    onStartScreenEffect(new EffectBuilder("playSound") {{
//                        effectParameter("sound", "settingstheme");
//                    }}); 
                
                   image(new ImageBuilder("img_MapBackground") {{
                            filename("Interface/Images/bkg_pirate_table.jpg");
                            height("100%");
                            width("100%");
                            }});
                    
                    panel(new PanelBuilder("Panel_Paused_Background"){{
                        childLayoutAbsoluteInside();
                        height("80%");
                        width("80%");
                        x("50px");
                        y("50px");
                        
                        
                     panel(new PanelBuilder("Panel_Paused_Title"){{
//                       
                        x("50px");
                        y("20px");
                        height("100px");
                        width("250px");
                        childLayoutCenter();
                                                
                            text(new TextBuilder() {{
                                text("Game Paused");
                                font("Interface/Fonts/verdana-48-regular.fnt");
                                height("100%");
                                width("100%");
                                alignCenter();
                                valignCenter();
                                                                                                
                            }});
                    }});        
                    
                               
                    panel(new PanelBuilder("Panel_Paused_MenuButtons"){{
                      
                        x("50px");
                        y("120px");
                        height("500px");
                        width("500px"); 
                        childLayoutAbsoluteInside();
                                                                     
                            image(new ImageBuilder("pausedmenuimg_gamesettings"){{
                            filename("Interface/Images/MenuUI/button_0_pausedmenu_gamesettings.png");
                            x("20px");
                            y("20px");
                            height("37px");
                            width("147px");  
                            
                            interactOnClick("settingsGame()");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_pausedmenu_gamesettings.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_pausedmenu_gamesettings.png"); neverStopRendering(true);}});
                                onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                            
                        }});
                        
                        image(new ImageBuilder("pausedmenuimg_Inventory"){{
                            filename("Interface/Images/MenuUI/button_0_pausedmenu_inventory.png");
                            x("20px");
                            y("70px");
                            height("37px");
                            width("147px");    
                            interactOnClick("");
                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_pausedmenu_inventory.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_pausedmenu_inventory.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                        }});
                        
                        image(new ImageBuilder("pausedmenuimg_Character"){{
                            filename("Interface/Images/MenuUI/button_0_pausedmenu_character.png");
                            x("20px");
                            y("120px");
                            height("37px");
                            width("147px");   
                            interactOnClick("");
                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_pausedmenu_character.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_pausedmenu_character.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                        }});

                        image(new ImageBuilder("pausedmenuimg_Mission"){{
                            filename("Interface/Images/MenuUI/button_0_pausedmenu_mission.png");
                            x("20px");
                            y("170px");
                            height("37px");
                            width("147px");     
                            interactOnClick("");
                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_pausedmenu_mission.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_pausedmenu_mission.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                        }});
                        
                        image(new ImageBuilder("pausedmenuimg_Worldmap"){{
                            filename("Interface/Images/MenuUI/button_0_pausedmenu_worldmap.png");
                            x("20px");
                            y("220px");
                            height("37px");
                            width("147px");     
                            interactOnClick("showMap()");
                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_pausedmenu_worldmap.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_pausedmenu_worldmap.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                        }});
                        
                            }});
                   
                   
                    panel(new PanelBuilder("Panel_Paused_ScreenButtons"){{
//                      
                        childLayoutAbsoluteInside();
                        padding("10px");
                        x("50px");
                        y("520px");
                        height("200px");
                        width("300px"); 
                                                
                        
                        image(new ImageBuilder("settings_Apply"){{
                            filename("Interface/Images/MenuUI/button_0_apply.png");
                            x("20px");
                            y("20px");
                            height("40px");
                            width("150px");  
                            interactOnClick("popupApplySettings()");  
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_apply.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_apply.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                          
                        }});
                        
                        image(new ImageBuilder("settings_Back"){{
                            filename("Interface/Images/MenuUI/button_0_back.png");
                            x("20px");
                            y("70px");
                            height("40px");
                            width("150px");    
                            interactOnClick("backToGame()");
                            interactOnMouseOver("buttonEffect()");
//                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_back.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_back.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                              
                        }});
                        
                        image(new ImageBuilder("settings_Exit"){{
                            filename("Interface/Images/MenuUI/button_0_quit.png");
                            x("20px");
                            y("120px");
                            height("40px");
                            width("150px");    
                            interactOnClick("backToMainMenu()");
                            interactOnMouseOver("buttonEffect()");
//                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_quit.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_quit.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                            }});
                        }});
                    
                        
//                        popup(new PopupBuilder("popupExit") {{
//                            childLayoutCenter();
//                            backgroundColor("#000a");
//                        }}.registerPopup(nifty));
                            
                      }});  
                
                }});
                }}.build(nifty));
                        
                nifty.gotoScreen("Screen_PausedMenu");
    }
    
    public void enablePausedMenu(){
        
       nifty.gotoScreen("Screen_PausedMenu");
    }
    
    
}
