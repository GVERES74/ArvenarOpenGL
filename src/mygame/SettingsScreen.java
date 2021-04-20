/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.slider.builder.SliderBuilder;
import de.lessvoid.nifty.controls.tabs.builder.TabBuilder;
import de.lessvoid.nifty.controls.tabs.builder.TabGroupBuilder;
import de.lessvoid.nifty.effects.Effect;
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
        
        
//         mainScene = assetManager.loadModel("Scenes/shore/mainScene.j3o");
//            settingsRootNode.attachChild(mainScene);
//            
//            rootNode.attachChild(settingsRootNode);
//            rootNode.attachChild(settingsGUINode);
            NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
            nifty = niftyDisplay.getNifty();
            app.getGuiViewPort().addProcessor(niftyDisplay);
            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
        
            nifty.registerSound("btnclick", "Interface/sound/ButtonClick.ogg");
             nifty.registerMusic("credits", "Interface/music/RPG_Village_1.ogg");
                        
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
    
    
    @Override
    protected void cleanup(Application arg0) {
        
    }

     @Override
    protected void onEnable() {
        
        app.getFlyByCamera().setDragToRotate(true);
        
        
        nifty.addScreen("Screen_GameSettings", new ScreenBuilder("Game Settings"){{
                controller(new mygame.SettingsScreenController());
                defaultFocusElement("Button_Apply");
                
                layer(new LayerBuilder("Layer_Settings_AllItems"){{
                    childLayoutAbsoluteInside();
                    
                    onStartScreenEffect(new EffectBuilder("playSound") {{
                        effectParameter("sound", "credits");
                    }}); 
                
                    image(new ImageBuilder() {{
                        filename("Interface/background-new.png");
                    }});
                    
                     panel(new PanelBuilder("Panel_Settings_Title"){{
                        childLayoutAbsoluteInside(); //!! please remember if you want to set child x / y positions manually
                        padding("10px");
                        x("20px");
                        y("20px");

                        height("100px");
                        width("250px"); 
                        
                            text(new TextBuilder() {{
                                text("Game Settings");
                                font("Interface/Fonts/Antiqua.fnt");
                                height("100%");
                                width("100%");
                                padding("10px");
                                x("20px");
                                y("20px");
                                
                            }});
                    }});        
                    
                                    
                    control(new TabGroupBuilder("TabGroup_Settings"){{
                            width("500px");
                            height("500px");
                            x("400px");
                            y("100px");
                            backgroundColor("#ffc1");
                        
                            
                        control(new TabBuilder("tab_GameplaySettings", "Gameplay"){{
                            
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
                            }});
                        
                        control(new TabBuilder("tab_AudioSettings", "Audio"){{
                                childLayoutAbsoluteInside();    
                                    control(new LabelBuilder("label_Volume") {{
                                        text("Volume");   
                                        x("20px");
                                        y("60px");  
                                                                                
                                    }});
                                    
                                    control(new SliderBuilder("slider_Volume", false){{
                                        width("200px");
                                        x("120px");
                                        y("60px");
                                    }});
                                    
                                    control(new LabelBuilder("label_Slider_Volume") {{
                                        text("%");   
                                        x("340px");
                                        y("60px");  
                                                                                
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
//                        childLayoutVertical();
                        childLayoutAbsoluteInside();
                        padding("10px");
                        x("50px");
                        y("520px");
                        height("200px");
                        width("300px"); 
                                                
                        
                        control(new ButtonBuilder("Button_Apply", "Apply Changes"){{

                            x("20px");
                            y("20px");
                            height("50px");
                            width("220px");  
//                            interactOnClick("popupApplySettings()");
                              interactOnClick("backToMainMenu()");  
                            onStartHoverEffect(new HoverEffectBuilder("fade"){{
                                  length(100);
                                  effectParameter("start", "#0");
                                  effectParameter("end", "#f");
                                  neverStopRendering(true);
                                          
                                 }});
                            
                            onStartHoverEffect(new HoverEffectBuilder("move"){{      
                                  effectParameter("mode", "toOffset");
                                  effectParameter("offsetX", "+15");
                                    
                                }});
                            
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{
                                  effectParameter("sound", "btnclick");
                                    
                                }});
                          
                        }});
                        
                        control(new ButtonBuilder("Button_Cancel", "Cancel Changes"){{
                            x("20px");
                            y("80px");
                            height("50px");
                            width("220px");    
                            interactOnClick("popupCancelSettings()");
                            interactOnMouseOver("buttonEffect()");
                            backgroundColor("#0c01");
                                onStartHoverEffect(new HoverEffectBuilder("playSound"){{
                                  effectParameter("sound", "btnclick");
                                    
                                }});

                             onStartHoverEffect(new HoverEffectBuilder("fade"){{
                                  length(100);
                                  effectParameter("start", "#0");
                                  effectParameter("end", "#f");
                                  neverStopRendering(true);
                                          
                                 }});
                            
                            onStartHoverEffect(new HoverEffectBuilder("move"){{      
                                  effectParameter("mode", "toOffset");
                                  effectParameter("offsetX", "+15");
                                    
                                }});     
                                      
                                        
                                    
                                
                        }});
                        }});
                    
                    
                        
//                        popup(new PopupBuilder("popupExit") {{
//                            childLayoutCenter();
//                            backgroundColor("#000a");
//                        }}.registerPopup(nifty));
                            
                        
                
                }});
                }}.build(nifty));
        
                
                nifty.gotoScreen("Screen_GameSettings");
    }
    
   


    @Override
    protected void onDisable() {
        
    }
    
}
