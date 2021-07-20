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
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.scrollpanel.builder.ScrollPanelBuilder;
import de.lessvoid.nifty.screen.Screen;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class GameModeScreen extends BaseAppState {
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
    private int screenHeight, screenWidth;
    
    @Override
    protected void initialize(Application app) {
    
        this.app = (SimpleApplication) app; // can cast Application to something more specific
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
        
        screenHeight = PlayGame.getPlayGameAppSettings().getHeight();
        screenWidth = PlayGame.getPlayGameAppSettings().getWidth();
                    
        
        createGameModeScreen();
        
    }

    @Override
    protected void cleanup(Application app) {
        PlayGame.gameplayState.setEnabled(true);
        System.out.println(this.nifty.getCurrentScreen().getScreenId()+" screen cleanup called....."); 
    }

    //onEnable()/onDisable() can be used for managing things that should     
    //only exist while the state is enabled. Prime examples would be scene     
    //graph attachment or input listener attachment.    
    @Override
    protected void onEnable() {
        
        showGameModeScreen();
        PlayGame.gameplayState.setEnabled(false);
        
    }
    
    @Override
    protected void onDisable() {
        
        hideGameModeScreen();
            //Called when the state was previously enabled but is now disabled         
        //either because setEnabled(false) was called or the state is being         
        //cleaned up.    
    }
    
    @Override
    public void update(float tpf) {
        
        //TODO: implement behavior during runtime    
    }
    
    public void createGameModeScreen(){
        
        app.getFlyByCamera().setDragToRotate(true);
        
        nifty = PlayGame.getNiftyDisplay().getNifty();
            app.getGuiViewPort().addProcessor(PlayGame.getNiftyDisplay());
            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
        
            nifty.registerSound("btnclick", "Interface/sound/click.wav");
            nifty.registerSound("openmap", "Interface/sound/book_flip_2.ogg");
                   
            nifty.addScreen("Screen_GameMode", new ScreenBuilder("Select_GameMode"){{
                controller(new mygame.GameModeScreenController());
                defaultFocusElement("Back");
                
                layer(new LayerBuilder("Layer_GameMode_Background"){{
                    childLayoutCenter();
                    
                    
                    onStartScreenEffect(new EffectBuilder("playSound") {{
                        effectParameter("sound", "openmap");
                    }}); 
                    
                    onEndScreenEffect(new EffectBuilder("playSound") {{
                        effectParameter("sound", "openmap");
                    }}); 
                        
                    
                    image(new ImageBuilder() {{
                            filename("Interface/Images/background_scroll.png");
                            height("100%");
                            width("100%");
                    }});
                }}); //end layer background
                
                layer(new LayerBuilder("Layer_GameMode_Main"){{
                    childLayoutVertical();
                               
                    panel(new PanelBuilder("Panel_GameMode_Title"){{
                        height("10%");
                        width("30%"); 
                        //backgroundColor("#fff6");
                        childLayoutCenter();
                            text(new TextBuilder() {{
                                text("Select Game Mode");
                                font("Interface/Fonts/verdana-48-regular.fnt");
                                height("50%");
                                width("100%");
                                alignCenter();
                                                                
                            }});
                    }}); //end panel title 
                    
                    
                    panel(new PanelBuilder("Panel_GameMode_Content"){{
                        height("80%");
                        width("100%"); 
                        //backgroundColor("#fff6");
                        childLayoutHorizontal();
                    
                    panel(new PanelBuilder("Panel_GameMode_Left"){{ //for the menuitems and settings
                        alignCenter();
                        valignCenter();
                        height("100%");
                        width("50%"); 
                        padding("20px");
                        childLayoutVertical();
                        //backgroundColor("#ffc1");
                                                                     
                    
                        panel(new PanelBuilder("Panel_GameMode_SelectMode"){{
                        height("20%");
                        width("100%"); 
                        childLayoutVertical();
                                 
                            image(new ImageBuilder("btn_GameMode_SP"){{
                                filename("Interface/Images/MenuUI/button_0_gm_sp.png");
                                height("70px");
                                width("350px");  
                                alignCenter();    
                                interactOnClick("showSPMenu()");
                                onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                    effectParameter("active", "Interface/Images/MenuUI/button_1_gm_sp.png"); neverStopRendering(true);
                                    effectParameter("inactive", "Interface/Images/MenuUI/button_0_gm_sp.png"); neverStopRendering(true);}});
                                    onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                                onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});

                            }});
                        
                            image(new ImageBuilder("btn_GameMode_MP"){{
                                filename("Interface/Images/MenuUI/button_0_gm_mp.png");
                                height("70px");
                                width("350px");  
                                alignCenter();    
                                interactOnClick("showMPMenu()");
                                onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                    effectParameter("active", "Interface/Images/MenuUI/button_1_gm_mp.png"); neverStopRendering(true);
                                    effectParameter("inactive", "Interface/Images/MenuUI/button_0_gm_mp.png"); neverStopRendering(true);}});
                                    onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                                onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});

                            }});
                            
                            image(new ImageBuilder("btn_GameMode_SL"){{
                                filename("Interface/Images/MenuUI/button_0_gm_sl.png");
                                height("70px");
                                width("350px");  
                                alignCenter();    
                                interactOnClick("showSLMenu()");
                                onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                    effectParameter("active", "Interface/Images/MenuUI/button_1_gm_sl.png"); neverStopRendering(true);
                                    effectParameter("inactive", "Interface/Images/MenuUI/button_0_gm_sl.png"); neverStopRendering(true);}});
                                    onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                                onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});

                            }});
                            

                                
                        }});  //end Select Game Mode panel  
                            
                        panel(new PanelBuilder("Panel_GameMode_Settings"){{ //bottom panel if you add controls to set gameplay
                        alignLeft();
                        height("80%");
                        width("100%"); 
                        childLayoutVertical();
                                                    
                           //add some controls, checkboxes, etc.
                                                
                        }});
                                              
                    }});    
                                               
                                                
                        panel(new PanelBuilder("Panel_GameMode_Right"){{
                        height("100%"); 
                        width("50%");
                        alignCenter();
                        childLayoutCenter();
                            
                            panel(new PanelBuilder("Panel_GameMode_SP"){{
                            height("100%"); 
                            width("70%");
                            alignCenter();
                            childLayoutVertical();
                            
                                panel(new PanelBuilder("Panel_GameMode_SP_Image"){{
                                height("70%"); 
                                width("100%");
                                alignCenter();
                                childLayoutCenter();
                        
                                    image(new ImageBuilder("img_SinglePlayer") {{
                                    filename("Interface/Images/gm_storymode.png");
                                    height("100%");
                                    width("100%");
                                    }});    
                                }}); 
                                
                                panel(new PanelBuilder("Panel_GameMode_SP_Text"){{
                                height("30%"); 
                                width("100%");
                                alignCenter();
                                childLayoutCenter();
                        
                                    control(new LabelBuilder("Content_Text1"){{
                                    text("Play Story Mode\n"
                                            + "");    
                                    font("Interface/Fonts/verdana-48-regular.fnt");
                                    color("#0009");
                                    height("50%");
                                    width("100%");
                                    align(Align.Center);
                                    valignCenter();
                                    }});
                                }});    
                                                        
                            }}); //gamemode sp panel end
                            
                            
                            panel(new PanelBuilder("Panel_GameMode_MP"){{
                            visible(false);
                            height("100%"); 
                            width("70%");
                            alignCenter();
                            childLayoutVertical();
                            
                                panel(new PanelBuilder("Panel_GameMode_MP_DropDown"){{
                                height("10%"); 
                                width("100%");
                                alignCenter();
                                childLayoutCenter();
                            
                            
                                    control(new DropDownBuilder("dropDown_MPLevelList") {{
                                                width("100%");
                                                height("100%");

                                    }});
                                }});    
                            
                                panel(new PanelBuilder("Panel_GameMode_MP_Image"){{
                                height("70%"); 
                                width("100%");
                                alignCenter();
                                childLayoutCenter();
                        
                                    image(new ImageBuilder("img_MultiPlayer") {{
                                    filename("Interface/Images/gm_multiplayer.png");
                                    height("100%");
                                    width("100%");
                                    }});    
                                }}); 
                                
                                panel(new PanelBuilder("Panel_GameMode_MP_Text"){{
                                height("30%"); 
                                width("100%");
                                alignCenter();
                                childLayoutCenter();
                        
                                    control(new LabelBuilder("Content_Text1"){{
                                    text("Play Multi Player\n"
                                            + "");    
                                    font("Interface/Fonts/verdana-48-regular.fnt");
                                    color("#0009");
                                    height("50%");
                                    width("100%");
                                    align(Align.Center);
                                    valignCenter();
                                    }});
                                }});    
                                                        
                            }}); //gamemode mp panel end 
                            
                            
                            panel(new PanelBuilder("Panel_GameMode_SL"){{
                            visible(false);
                            height("100%"); 
                            width("70%");
                            alignCenter();
                            childLayoutVertical();
                            
                                panel(new PanelBuilder("Panel_GameMode_SL_DropDown"){{
                                height("10%"); 
                                width("100%");
                                alignCenter();
                                childLayoutCenter();
                            
                            
                                    control(new DropDownBuilder("dropDown_SLLevelList") {{
                                                width("100%");
                                                height("100%");

                                    }});
                                }});   
                            
                                panel(new PanelBuilder("Panel_GameMode_SL_Image"){{
                                height("70%"); 
                                width("100%");
                                alignCenter();
                                childLayoutCenter();
                        
                                    image(new ImageBuilder("img_SingleLevel") {{
                                    filename("Interface/Images/gm_tour.png");
                                    height("100%");
                                    width("100%");
                                    }});    
                                }}); 
                                
                                panel(new PanelBuilder("Panel_GameMode_SL_Text"){{
                                height("30%"); 
                                width("100%");
                                alignCenter();
                                childLayoutCenter();
                        
                                    control(new LabelBuilder("Content_Text1"){{
                                    text("Take a free tour on a \n"
                                            + "selected map");    
                                    font("Interface/Fonts/verdana-48-regular.fnt");
                                    color("#0009");
                                    height("50%");
                                    width("100%");
                                    align(Align.Center);
                                    valignCenter();
                                    }});
                                }});    
                                                        
                            }}); //gamemode sl panel end
                            
                         }});  //right panel end   
                                        
                        }}); //panel content end
                        
                       panel(new PanelBuilder("Panel_GameMode_ScreenButtons"){{
                            height("10%");
                            width("20%");
                            alignCenter();
                            //paddingLeft("20px");
                            childLayoutHorizontal();
                            
                                image(new ImageBuilder("gamemode_PlayGame"){{
                                filename("Interface/Images/MenuUI/button_0_playgame.png");
                                height("40px");
                                width("150px");    
                                interactOnClick("playSelectedGameMode()");
                                interactOnMouseOver("buttonEffect()");

                                onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                    effectParameter("active", "Interface/Images/MenuUI/button_1_playgame.png"); neverStopRendering(true);
                                    effectParameter("inactive", "Interface/Images/MenuUI/button_0_playgame.png"); neverStopRendering(true);}});
                                onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                                onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});

                                }});
                        
                                image(new ImageBuilder("gamemode_Back"){{
                                filename("Interface/Images/MenuUI/button_0_back.png");
                                height("40px");
                                width("150px");    
                                interactOnClick("backToMainMenu()");
                                interactOnMouseOver("buttonEffect()");

                                onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                    effectParameter("active", "Interface/Images/MenuUI/button_1_back.png"); neverStopRendering(true);
                                    effectParameter("inactive", "Interface/Images/MenuUI/button_0_back.png"); neverStopRendering(true);}});
                                onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                                onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});

                                }});
                      
                        }}); //end panel screenbuttons 
                        
                                   
                }}); //layer main end
                }}.build(nifty));
                        
                nifty.gotoScreen("Screen_GameMode");
    }
    
    public void showGameModeScreen(){
        app.getFlyByCamera().setDragToRotate(true);
        nifty.gotoScreen("Screen_GameMode");
    }
    
    public void hideGameModeScreen(){
        app.getFlyByCamera().setDragToRotate(false);
        nifty.removeScreen("Screen_GameMode");
        PlayGame.getNiftyDisplay().getNifty().gotoScreen("Screen_HUD");        
    }
       
}