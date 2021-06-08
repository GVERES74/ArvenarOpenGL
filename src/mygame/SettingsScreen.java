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
import de.lessvoid.nifty.tools.SizeValue;


/**
 *
 * @author TE332168
 */
public class SettingsScreen extends BaseAppState {
    
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
    private Spatial mainScene;
    
    int screenWidth, screenHeight;
    
    private Node settingsRootNode = new Node("Game Settings RootNode");
    private Node settingsGUINode = new Node("Game Settings GUINode");

    
    @Override
    public void initialize(Application app) {
        
        this.app = (SimpleApplication) app; // can cast Application to something more specific
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
        screenWidth = PlayGame.getPlayGameAppSettings().getWidth();
        screenHeight = PlayGame.getPlayGameAppSettings().getHeight();
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
        
        createSettingsGUI();
                  
    }
    
    @Override
    public void update(float tpf) {
        
    }
    
    
    @Override
    protected void cleanup(Application app) {
        PlayGame.gameplayState.setEnabled(true);
        System.out.println(this.nifty.getCurrentScreen().getScreenId()+" screen cleanup called.....");
    }

    @Override
    protected void onEnable() {
        showSettingsScreen();
       PlayGame.gameplayState.setEnabled(false);
       
    }
    
    @Override
    protected void onDisable() {
        hideSettingsScreen();
        
    }
    
    public void createSettingsGUI(){
        
        app.getFlyByCamera().setDragToRotate(true);
        
        nifty = PlayGame.getNiftyDisplay().getNifty();
            app.getGuiViewPort().addProcessor(PlayGame.getNiftyDisplay());
            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
        
            nifty.registerSound("btnclick", "Interface/sound/click.wav");
//            nifty.registerMusic("settingstheme", "Music/Soundtracks/RPG_Ambient_4.ogg");
            
        
            nifty.addScreen("Screen_GameSettings", new ScreenBuilder("Game Settings"){{
                controller(new mygame.SettingsScreenController());
                defaultFocusElement("settings_Apply");
                
                layer(new LayerBuilder("Layer_Settings_AllItems"){{
                    childLayoutAbsoluteInside();
                        
                    
//                    onStartScreenEffect(new EffectBuilder("playSound") {{
//                        effectParameter("sound", "settingstheme");
//                    }}); 
                
                    image(new ImageBuilder() {{
                        filename("Interface/Images/bkg_pirate_table.jpg");
                        height("100%");
                        width("100%");
                    }});
                    
                    panel(new PanelBuilder("Panel_Settings_Title"){{
                        childLayoutCenter();
                        x("20px");
                        y("20px");

                        height("100px");
                        width("250px"); 
                        
                            text(new TextBuilder() {{
                                text("Game Settings");
                                font("Interface/Fonts/verdana-48-regular.fnt");
                                height("100%");
                                width("100%");
                                
                            }});
                    }});        
                    
                                    
                    control(new TabGroupBuilder("TabGroup_Settings"){{
                            width("500px");
                            height("500px");
                            x(String.valueOf(screenWidth/2-250));
                            y("100px");
                            
                            backgroundColor("#ffc1");
                        
                            
                        control(new TabBuilder("tab_GameplaySettings", "Gameplay"){{
                            childLayoutAbsoluteInside();     
                                
                                    control(new LabelBuilder("label_ShowFps") {{
                                        text("Show FPS");   
                                        x("20px");
                                        y("60px");  
                                                                                
                                    }});
                                
                                    control(new CheckboxBuilder("cb_ShowFps") {{

                                        x("100px");
                                        y("60px");   
                                    }});
                        }});
                        
                        control(new TabBuilder("tab_VideoSettings", "Video"){{
                                childLayoutAbsoluteInside();     
                                
                                    control(new LabelBuilder("label_Fullscreen") {{
                                        text("Fullscreen");   
                                        x("20px");
                                        y("60px");  
                                                                                
                                    }});
                                
                                    control(new CheckboxBuilder("cb_Fullscreen") {{

                                        x("100px");
                                        y("60px");   
                                    }});
//                            
                                    control(new LabelBuilder("label_Resolution") {{
                                        text("Display Resolution");   
                                        x("20px");
                                        y("100px");    

                                    }});
//
                                    control(new DropDownBuilder("dropdown_Resolution") {{
                                        width("200px");
                                        x("150px");
                                        y("100px");

                                    }});
                                    
                                    
                                    control(new LabelBuilder("label_BitDepth") {{
                                        text("Bit Depth");   
                                        x("20px");
                                        y("140px");    

                                    }});
//
                                    control(new DropDownBuilder("dropdown_BitDepth") {{
                                        width("80px");
                                        x("150px");
                                        y("140px");

                                    }});
                                    
                                    control(new LabelBuilder("label_RefreshRate") {{
                                        text("Screen refresh rate");   
                                        x("20px");
                                        y("180px");    

                                    }});
//
                                    control(new DropDownBuilder("dropdown_RefreshRate") {{
                                        width("80px");
                                        x("150px");
                                        y("180px");

                                    }});
                            }});
                        
                        control(new TabBuilder("tab_AudioSettings", "Audio"){{
                                childLayoutAbsoluteInside();    
                                    control(new LabelBuilder("label_MusicVolume") {{
                                        text("Music Volume");   
                                        x("20px");
                                        y("60px");  
                                                                                
                                    }});
                                    
                                    control(new SliderBuilder("slider_MusicVolume", false){{
                                        width("200px");
                                        x("120px");
                                        y("60px");
                                        min(0.0f);
                                        max(100f);
                                        initial(50.0f);
                                        stepSize(10f);
                                        buttonStepSize(10f);
                                    }});
                                    
                                    control(new LabelBuilder("label_Slider_MusicVolume") {{
                                        text("%");
                                        width("50px");
                                        x("340px");
                                        y("60px");  
                                                                                
                                    }});
                                    
                                    control(new LabelBuilder("label_SoundVolume") {{
                                        text("Sound Volume");   
                                        x("20px");
                                        y("100px");  
                                                                                
                                    }});
                                    
                                    control(new SliderBuilder("slider_SoundVolume", false){{
                                        width("200px");
                                        x("120px");
                                        y("100px");
                                        min(0.0f);
                                        max(100f);
                                        initial(50.0f);
                                        stepSize(10f);
                                        buttonStepSize(10f);
                                    }});
                                    
                                    control(new LabelBuilder("label_Slider_SoundVolume") {{
                                        text("%");
                                        width("50px");
                                        x("340px");
                                        y("100px");  
                                                                                
                                    }});
                                                                 
                        }});
                        
                        control(new TabBuilder("tab_ControlSettings", "Controls"){{
                            
                                    x("0px");  
                                    y("0px");
                                    height("300px"); width("300px");
                                    style("nifty-panel");                                
                        }});
                    
                }});    
                   
                    panel(new PanelBuilder("Panel_Settings_ScreenButtons"){{
                        x("50px");
                        y(SizeValue.px(screenHeight-300));
                        height("200px");
                        width("300px"); 
                        childLayoutVertical();                        
                        
                        image(new ImageBuilder("settings_Apply"){{
                            filename("Interface/Images/MenuUI/button_0_apply.png");
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
                
                }});
                }}.build(nifty));
        
                
                nifty.gotoScreen("Screen_GameSettings");
    }
    
    public void showSettingsScreen(){
         
        nifty.gotoScreen("Screen_GameSettings");
        app.getFlyByCamera().setDragToRotate(true);
    }
     
    public void hideSettingsScreen(){
         
        nifty.removeScreen("Screen_GameSettings");
        app.getFlyByCamera().setDragToRotate(false);
        PlayGame.getNiftyDisplay().getNifty().gotoScreen("Screen_HUD");
    }
     
    
}
