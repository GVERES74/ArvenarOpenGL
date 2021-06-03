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
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;



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
                
    int screenWidth, screenHeight;
    
    @Override
    public void initialize(Application app) {
    //If a detached AppState is attached then initialize() will be called on the following render pass.
    
        this.app = (SimpleApplication) app; // can cast Application to something more specific
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
        screenWidth = PlayGame.getPlayGameAppSettings().getWidth();
        screenHeight = PlayGame.getPlayGameAppSettings().getHeight();
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
                
        createPausedScreen();

    }
    
    @Override
    public void update(float tpf) {
        
    }
    
    
    @Override
    protected void cleanup(Application app) {
        //If an attached AppState is detached then cleanup() will be called on the following render pass.
        System.out.println(this.nifty.getCurrentScreen().getScreenId()+" screen cleanup called.....");
    }

     @Override
    protected void onEnable() {
        showPausedScreen();
    }
    

    @Override
    protected void onDisable() {

       hidePausedScreen();
                        
    }
    
    
    public void createPausedScreen(){
        
        app.getFlyByCamera().setDragToRotate(true);
        
        nifty = PlayGame.getNiftyDisplay().getNifty();
            app.getGuiViewPort().addProcessor(PlayGame.getNiftyDisplay());
            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
        
            nifty.registerSound("btnclick", "Interface/sound/click.wav");
            nifty.registerSound("openbook", "Interface/sound/BookFlip1.wav");
            nifty.registerSound("closebook", "Interface/sound/BookFlip10.wav");
            nifty.registerMouseCursor("crosshair", "Interface/Images/HUD/crosshair.png", 100, 100);
            nifty.getNiftyMouse().enableMouseCursor("crosshair");
                   
            nifty.addScreen("Screen_PausedMenu", new ScreenBuilder("PausedMenu"){{
                controller(new mygame.PausedScreenController());
                defaultFocusElement("settings_Apply");
                
                layer(new LayerBuilder("Layer_Settings_AllItems"){{
                    childLayoutAbsoluteInside();
                    
                    onStartScreenEffect(new EffectBuilder("playSound") {{
                        effectParameter("sound", "openbook");
                    }}); 
                    
                    onEndScreenEffect(new EffectBuilder("playSound") {{
                        effectParameter("sound", "closebook");
                    }}); 
                
                   image(new ImageBuilder() {{
                            filename("Interface/Images/book.png");
                            height("100%");
                            width("100%");
                            }});
                        
                    panel(new PanelBuilder("Panel_Paused_Title"){{
//                       
                        x("20px");
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
                      
                        x(SizeValue.px(screenWidth/9));
                        y("80px");
                        height("50%");
                        width("30%"); 
                        childLayoutVertical();
                        
                        text(new TextBuilder() {{
                                text("MOTHER'S BOOK");
                                font("Interface/Fonts/verdana-48-regular.fnt");
                                color("#0009");
                                height("20%");
                                width("100%");
                                alignCenter();
                                                                                                
                        }});
                        
                        image(new ImageBuilder("pausedmenuimg_Inventory"){{
                            filename("Interface/Images/MenuUI/button_0_pausedmenu_inventory.png");
                            height("60px");
                            width("300px");    
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
                            height("60px");
                            width("300px");      
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
                            height("60px");
                            width("300px");     
                            interactOnClick("openDiary()");
                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_pausedmenu_mission.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_pausedmenu_mission.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                        }});
                        
                        image(new ImageBuilder("pausedmenuimg_Worldmap"){{
                            filename("Interface/Images/MenuUI/button_0_pausedmenu_worldmap.png");
                            height("60px");
                            width("300px");       
                            interactOnClick("showMap()");
                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_pausedmenu_worldmap.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_pausedmenu_worldmap.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                        }});
                        
                        }}); //end panel menuitems
                    
                    
                    panel(new PanelBuilder("Panel_Paused_MenuItemHints"){{
                        x(SizeValue.px(screenWidth/2));
                        y("80px");
                        height("80%");
                        width("40%"); 
                        childLayoutCenter();
                        //backgroundColor("#00f1");
                        
                        text(new TextBuilder("title_hint"){{
                            text("${CALL.getMenuItemHintText()}");
                            font("Interface/Fonts/verdana-48-regular.fnt");
                            color("#00f9");
                            height("100%");
                            width("100%");
                            alignCenter();
                            valignCenter();
                            
                        }});
                    }}); //end panel hintcontent   
                   
                   
                    panel(new PanelBuilder("Panel_Paused_ScreenButtons"){{
                        childLayoutHorizontal();
                        x(SizeValue.px(screenWidth/9));
                        y(SizeValue.px(screenHeight-150));
                        height("70px");
                        width("600px"); 
                                                
                        
                        image(new ImageBuilder("pausedmenuimg_gamesettings"){{
                            filename("Interface/Images/MenuUI/button_0_pausedmenu_gamesettings.png");
                            height("40px");
                            width("200px");      
                            
                            interactOnClick("settingsGame()");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_pausedmenu_gamesettings.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_pausedmenu_gamesettings.png"); neverStopRendering(true);}});
                                onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                          
                        }});
                        
                        image(new ImageBuilder("pausedmenu_Back"){{
                            filename("Interface/Images/MenuUI/button_0_pausedmenu_back.png");
                            height("40px");
                            width("200px");    
                            interactOnClick("backToGame()");
                            interactOnMouseOver("buttonEffect()");
//                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_pausedmenu_back.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_pausedmenu_back.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                              
                        }});
                        
                        image(new ImageBuilder("pausedmenu_Exit"){{
                            filename("Interface/Images/MenuUI/button_0_pausedmenu_quit.png");
                            height("40px");
                            width("200px");    
                            interactOnClick("backToMainMenu()");
                            interactOnMouseOver("buttonEffect()");
//                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_pausedmenu_quit.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_pausedmenu_quit.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                            }});
                        }});
                      
                }});
                }}.build(nifty));
                        
                nifty.gotoScreen("Screen_PausedMenu");
    }
    
    public void showPausedScreen(){
        nifty.gotoScreen("Screen_PausedMenu");
        app.getFlyByCamera().setDragToRotate(true);
    }
    
    public void hidePausedScreen(){
        nifty.removeScreen("Screen_PausedMenu");
        app.getFlyByCamera().setDragToRotate(false);
        
        
    }
}
