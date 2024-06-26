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
        nifty = PlayGame.nifty;
        app.getGuiViewPort().addProcessor(PlayGame.niftyDisplay);
        screenWidth = PlayGame.appsettings.getWidth();
        screenHeight = PlayGame.appsettings.getHeight();
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
        
        createSettingsGUI();
                  
    }
    
    @Override
    public void update(float tpf) {
        
    }
    
    
    @Override
    protected void cleanup(Application app) {
        
        System.out.println(this.nifty.getCurrentScreen().getScreenId()+" screen cleanup called.....");
    }

    @Override
    protected void onEnable() {
        
        nifty.gotoScreen("Screen_GameSettings");
        
        PlayGame.gameplayAppState.setEnabled(false);
        System.out.println(this.getClass().getName()+" enabled....."); 
       
    }
    
    @Override
    protected void onDisable() {
        
        nifty.removeScreen("Screen_GameSettings");
        
        nifty.gotoScreen("Screen_HUD");
        PlayGame.gameplayAppState.setEnabled(true);
    }
    
    public void createSettingsGUI(){
        
        
            
            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
        
            nifty.registerSound("btnclick", "Interface/sound/click.wav");
            
        
            nifty.addScreen("Screen_GameSettings", new ScreenBuilder("Game Settings"){{
                controller(new mygame.SettingsScreenController());
                defaultFocusElement("settings_Apply");
                
                layer(new LayerBuilder("Layer_Settings_Background"){{
                    childLayoutCenter();
                    
                    image(new ImageBuilder() {{
                        filename("Interface/Images/bkg_pirate_table.jpg");
                        height("100%");
                        width("100%");
                    }});
                }}); //end layer background    
                    
                layer(new LayerBuilder("Layer_Settings_Content"){{
                    childLayoutVertical();
                    
                    panel(new PanelBuilder("Panel_Settings_Title"){{
                        height("10%");
                        width("30%"); 
                        
                        childLayoutCenter();
                            text(new TextBuilder() {{
                                text("Game Settings");
                                font("Interface/Fonts/verdana-48-regular.fnt");
                                height("50%");
                                width("30%");
                                alignCenter();
                                valignCenter();
                                
                            }});
                    }}); //end panel settings title 
                                               
                    
                    panel(new PanelBuilder("Panel_Settings_Tabs"){{
                        childLayoutCenter();
                        width("100%");
                        height("70%");            
                    
                        control(new TabGroupBuilder("TabGroup_Settings"){{
                            width("500px");
                            height("500px");
                            alignCenter();
                            valignCenter();
                            backgroundColor("#ffc1");
                        
                            
                        control(new TabBuilder("tab_GameplaySettings", "Gameplay"){{ //GAMEPLAY SETTINGS
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
                        
                        control(new TabBuilder("tab_VideoSettings", "Video"){{  //DISPLAY SETTINGS
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
                            }}); //end Video settings
                        
                        control(new TabBuilder("tab_GraphicsSettings", "Graphics"){{ //GRAPHICS QUALITY SETTINGS
                                childLayoutAbsoluteInside();     
                                
                                    control(new LabelBuilder("label_Shadows") {{
                                        text("Shadows");   
                                        x("20px");
                                        y("60px");  
                                                                                
                                    }});
                                
                                    control(new CheckboxBuilder("cb_Shadows") {{

                                        x("150px");
                                        y("60px");   
                                    }});
                                    
                                    control(new LabelBuilder("label_Shadow_Quality") {{
                                        text("Shadow Quality");   
                                        x("20px");
                                        y("100px");    

                                    }});
//
                                    control(new DropDownBuilder("dropdown_Shadow_Quality") {{
                                        width("80px");
                                        x("150px");
                                        y("100px");

                                    }});
//                            
                                    control(new LabelBuilder("label_DOF") {{
                                        text("Depth of Field");   
                                        x("20px");
                                        y("140px");    

                                    }});
//
                                    control(new CheckboxBuilder("cb_DOF") {{
                                        x("150px");
                                        y("140px");   
                                    }});
                                    
                                     control(new LabelBuilder("label_DOF_Quality") {{
                                        text("Depth of Field Quality");   
                                        x("20px");
                                        y("180px");    

                                    }});
//
                                    control(new DropDownBuilder("dropdown_DOF_Quality") {{
                                        width("80px");
                                        x("150px");
                                        y("180px");

                                    }});
                                    
                                    
                                    control(new LabelBuilder("label_SSAO") {{
                                        text("SSAO");   
                                        x("20px");
                                        y("220px");    

                                    }});
//
                                    control(new CheckboxBuilder("cb_SSAO") {{
                                        x("150px");
                                        y("220px");   
                                    }});
                                    
                                    control(new LabelBuilder("label_GodRays") {{
                                        text("GodRays");   
                                        x("20px");
                                        y("260px");    
                                    }});
//
                                    control(new CheckboxBuilder("cb_GodRays") {{
                                        x("150px");
                                        y("260px");   
                                    }});
                                    

                                    control(new LabelBuilder("label_Bloom") {{
                                        text("Bloom");   
                                        x("20px");
                                        y("300px");    

                                    }});
//
                                    control(new CheckboxBuilder("cb_Bloom") {{
                                        x("150px");
                                        y("300px");   
                                    }});
                                    
                                     control(new LabelBuilder("label_Bloom_Quality") {{
                                        text("Bloom Quality");   
                                        x("20px");
                                        y("340px");    

                                    }});
//
                                    control(new DropDownBuilder("dropdown_Bloom_Quality") {{
                                        width("80px");
                                        x("150px");
                                        y("340px");

                                    }});
                                    
                            }}); //end Graphics settings
                        
                        control(new TabBuilder("tab_AudioSettings", "Audio"){{  //AUDIO (MUSIC / SOUND) SETTINGS
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
                        
                        control(new TabBuilder("tab_ControlSettings", "Controls"){{ //KEYBOARD / MOUSE SETTINGS
                            
                                    x("0px");  
                                    y("0px");
                                    height("300px"); width("300px");
                                    style("nifty-panel");                                
                        }});
                    
                    }});
                    }}); //end panel tabs
                   
                    panel(new PanelBuilder("Panel_Settings_ScreenButtons"){{
                        //backgroundColor("#ffc1");
                        height("20%");
                        width("20%");
                        paddingLeft("20px");
                        //alignCenter();
                        childLayoutVertical();                        
                        
                        image(new ImageBuilder("settings_Apply"){{
                            filename("Interface/Images/MenuUI/button_0_apply.png");
                            height("40px");
                            width("150px");  
                            interactOnClick("ApplySettings()"); 
                            interactOnMouseOver("buttonEffect()");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_apply.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_apply.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                          
                        }});
                        
                        image(new ImageBuilder("settings_BacktoGame"){{
                            filename("Interface/Images/MenuUI/button_0_back.png");
                            height("40px");
                            width("150px");    
                            interactOnClick("backToGame()");
                            interactOnMouseOver("buttonEffect()");
                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_back.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_back.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                                
                        }});
                        
                        image(new ImageBuilder("settings_BacktoMenu"){{
                            filename("Interface/Images/MenuUI/button_0_backtomenu.png");
                            height("40px");
                            width("150px");    
                            interactOnClick("backToMainMenu()"); 
                            interactOnMouseOver("buttonEffect()");
                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_backtomenu.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_backtomenu.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                                
                        }});
                        }});
                
                }});
                }}.build(nifty));
        
                
                nifty.gotoScreen("Screen_GameSettings");
    }
    
}
