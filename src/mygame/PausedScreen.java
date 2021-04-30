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
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.slider.builder.SliderBuilder;
import de.lessvoid.nifty.controls.tabs.builder.TabBuilder;
import de.lessvoid.nifty.controls.tabs.builder.TabGroupBuilder;
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
    
    private Node pausedRootNode = new Node("Paused Screen RootNode");
    
    
    @Override
    public void initialize(Application app) {
        
        this.app = (SimpleApplication) app; // can cast Application to something more specific
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
        screenWidth = PlayGame.getPlayGameAppSettings().getWidth();
        
                                                    
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
                
        app.setDisplayStatView(false); app.setDisplayFps(false);
        nifty = PlayGame.getNiftyDisplay().getNifty();
            app.getGuiViewPort().addProcessor(PlayGame.getNiftyDisplay());
            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
        
            nifty.registerSound("btnclick", "Interface/sound/metalClick.ogg");
            app.getFlyByCamera().setDragToRotate(true);
        
            nifty.addScreen("Screen_PausedMenu", new ScreenBuilder("Game Paused"){{
                controller(new mygame.PausedScreenController());
                defaultFocusElement("settings_Apply");
                
                layer(new LayerBuilder("Layer_Settings_AllItems"){{
                    childLayoutAbsoluteInside();
                    
//                    onStartScreenEffect(new EffectBuilder("playSound") {{
//                        effectParameter("sound", "settingstheme");
//                    }}); 
                
//                    image(new ImageBuilder() {{
//                        filename("Interface/Images/background_settings.png");
//                        height("100%");
//                        width("100%");
//                    }});
                    
                     panel(new PanelBuilder("Panel_Paused_Title"){{
                       backgroundColor("#ff02");  
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
                      
                      backgroundColor("#0f02");
                        
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
                            
                            interactOnClick("");
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
                            interactOnClick("");
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
                            interactOnClick("backToMainMenu()");
                            interactOnMouseOver("buttonEffect()");
                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_back.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_back.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                                      
                                        
                                    
                                
                        }});
                        }});
                    
                    
                        
//                        popup(new PopupBuilder("popupExit") {{
//                            childLayoutCenter();
//                            backgroundColor("#000a");
//                        }}.registerPopup(nifty));
                            
                        
                
                }});
                }}.build(nifty));
        
                
                nifty.gotoScreen("Screen_PausedMenu");
    }
    
   


    @Override
    protected void onDisable() {
        
    }
    
    
    
}
