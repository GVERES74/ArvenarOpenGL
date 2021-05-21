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
 *  
 *
 * @author TE332168 
 */
public class ExtrasScreen extends BaseAppState {

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
        
        this.app = (SimpleApplication) app; // can cast Application to something more specific
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
        screenWidth = PlayGame.getPlayGameAppSettings().getWidth();
        screenHeight = PlayGame.getPlayGameAppSettings().getHeight();
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
        
        createExtrasScreen();
                  
    }
    
    @Override
    public void update(float tpf) {
        
    }
    
    
    @Override
    protected void cleanup(Application app) {
        System.out.println("Extras Screen cleanup called.....");
    }

     @Override
    protected void onEnable() {
        enableExtrasScreen();
        PlayGame.playMusic("Music/Soundtracks/RPG - The Great Collapse.ogg");
       
    }
    
    @Override
    protected void onDisable() {
        disableExtrasScreen();
        
    }
    
    public void createExtrasScreen(){
        
        app.getFlyByCamera().setDragToRotate(true);
        
        nifty = PlayGame.getNiftyDisplay().getNifty();
            app.getGuiViewPort().addProcessor(PlayGame.getNiftyDisplay());
            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
        
            nifty.registerSound("btnclick", "Interface/sound/click.wav");
//            nifty.registerMusic("settingstheme", "Music/Soundtracks/RPG_Ambient_4.ogg");
            
        
            nifty.addScreen("Screen_Extras", new ScreenBuilder("Game Extras"){{
                controller(new mygame.ExtrasScreenController());
                defaultFocusElement("settings_Back");
                
                layer(new LayerBuilder("Layer_Extras_Root"){{
                    childLayoutAbsoluteInside();
                        
                    
//                    onStartScreenEffect(new EffectBuilder("playSound") {{
//                        effectParameter("sound", "settingstheme");
//                    }}); 
                
                    image(new ImageBuilder() {{
                        filename("Interface/Images/bkg_pirate_table.jpg");
                        height("100%");
                        width("100%");
                    }});
                    
                     panel(new PanelBuilder("Panel_Extras_Title"){{
                        childLayoutCenter();
                        x("20px");
                        y("20px");

                        height("100px");
                        width("250px"); 
                        
                            text(new TextBuilder() {{
                                text("Game Extras");
                                font("Interface/Fonts/verdana-48-regular.fnt");
                                height("100%");
                                width("100%");
                                
                            }});
                    }});        
                    
                                    
                    control(new TabGroupBuilder("TabGroup_Extras"){{
                            width("500px");
                            height("500px");
                            x(String.valueOf(screenWidth/2-250));
                            y("100px");
                            backgroundColor("#ffc1");
                            
                            
                        control(new TabBuilder("tab_Extras_MPlayer", "Music Player"){{
                            childLayoutAbsoluteInside();
                            
                                 
                                    panel(new PanelBuilder("Panel_Extras_MusicThemeTitle"){{
                                    x("50px");
                                    y("20px");
                                    height("10%");
                                    width("80%"); 
                                    backgroundColor("#ccc1");
                                    childLayoutHorizontal();    
                                            control(new LabelBuilder("text_nowPlaying", "Now playing:") {{
                                                alignLeft();
                                                valignCenter();
                                                font("Interface/Fonts/Default.fnt");
                                                height("100%");
                                                width("30%");
                                               
                                            }});


                                            control(new LabelBuilder("text_PlayingTitle", "No music loaded..") {{
                                                alignLeft();
                                                valignCenter();
                                                font("Interface/Fonts/Default.fnt");
                                                height("100%");
                                                width("70%");

                                            }});
                                    
                                    }}); //panel for music title
                                    
                                    panel(new PanelBuilder("Panel_Extras_MusicPlayerControls"){{
                                    x("50px");
                                    y("100px");
                                    height("50%");
                                    width("80%"); 
                                    backgroundColor("#fff3");
                                    childLayoutAbsoluteInside();                                      
                                            control(new LabelBuilder("label_SelectMusic") {{
                                                text("Select Music");
                                                font("Interface/Fonts/Default.fnt");
                                                x("10px");
                                                y("30px");  

                                            }});

                                            control(new DropDownBuilder("dropdown_MusicTheme") {{
                                                width("200px");
                                                x("150px");
                                                y("30px");

                                            }});

                                            image(new ImageBuilder("img_PlayMusic"){{
                                            filename("Interface/Images/ExtrasUI/mplayer_play_0.png");
                                            height("50px");
                                            width("50px"); 
                                            x("100px");
                                            y("150px");
                                            visibleToMouse(true);
                                            interactOnClick("playMusic()");
                                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                            effectParameter("active", "Interface/Images/ExtrasUI/mplayer_play_1.png"); neverStopRendering(true);
                                            effectParameter("inactive", "Interface/Images/ExtrasUI/mplayer_play_0.png"); neverStopRendering(true);}});
                                            }});

                                            image(new ImageBuilder("img_stopMusic"){{
                                            filename("Interface/Images/ExtrasUI/mplayer_stop_0.png");
                                            height("50px");
                                            width("50px"); 
                                            x("150px");
                                            y("151px");
                                            visibleToMouse(true);
                                            interactOnClick("stopMusic()");
                                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                            effectParameter("active", "Interface/Images/ExtrasUI/mplayer_stop_1.png"); neverStopRendering(true);
                                            effectParameter("inactive", "Interface/Images/ExtrasUI/mplayer_stop_0.png"); neverStopRendering(true);}});
                                            }});
                                            
                                            image(new ImageBuilder("img_pauseMusic"){{
                                            filename("Interface/Images/ExtrasUI/mplayer_pause_0.png");
                                            height("50px");
                                            width("50px"); 
                                            x("200px");
                                            y("150px");
                                            visibleToMouse(true);
                                            interactOnClick("pauseMusic()");
                                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                            effectParameter("active", "Interface/Images/ExtrasUI/mplayer_pause_1.png"); neverStopRendering(true);
                                            effectParameter("inactive", "Interface/Images/ExtrasUI/mplayer_pause_0.png"); neverStopRendering(true);}});
                                            }});
                                }}); //panel for music player controls                
                        }});
                        
                        control(new TabBuilder("tab_Extras_StoryBook", "Story Book"){{
                                childLayoutAbsoluteInside();     
                                
                                    control(new LabelBuilder("label_Fullscreen") {{
                                        text("text");   
                                        x("20px");
                                        y("60px");  
                                                                                
                                    }});
                                
                                    control(new CheckboxBuilder("cb_Fullscreen") {{

                                        x("100px");
                                        y("60px");   
                                    }});
//                            
                                    control(new LabelBuilder("label_Resolution") {{
                                        text("text");   
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
                        
                        control(new TabBuilder("tab_AudioSettings", "Empty"){{
                                childLayoutAbsoluteInside();    
                                    control(new LabelBuilder("label_MusicVolume") {{
                                        text("text");   
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
                                                                 
                        }});
                        
                        control(new TabBuilder("tab_ControlSettings", "empty"){{
                            
                                    x("0px");  
                                    y("0px");
                                    height("300px"); width("300px");
                                    style("nifty-panel");                                
                        }});
                    
                }});    
                   
                    panel(new PanelBuilder("Panel_Extras_ScreenButtons"){{
                        x("50px");
                        y(SizeValue.px(screenHeight-300));
                        height("200px");
                        width("300px"); 
                        childLayoutVertical();                        
                        
                                               
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
        
                
                nifty.gotoScreen("Screen_Extras");
    }
    
     public void enableExtrasScreen(){
         
        nifty.gotoScreen("Screen_Extras");
        app.getFlyByCamera().setDragToRotate(true);
     }
     
     public void disableExtrasScreen(){
         
        nifty.removeScreen("Screen_Extras");
        app.getFlyByCamera().setDragToRotate(false);
        PlayGame.getNiftyDisplay().getNifty().gotoScreen("Screen_MainMenu");
     }
     
    
}
