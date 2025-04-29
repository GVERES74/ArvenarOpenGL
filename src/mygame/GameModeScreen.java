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
import de.lessvoid.nifty.controls.radiobutton.builder.RadioButtonBuilder;
import de.lessvoid.nifty.controls.radiobutton.builder.RadioGroupBuilder;
import de.lessvoid.nifty.screen.Screen;
import static mygame.GameModeScreenController.startSingleLevel;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class GameModeScreen extends BaseAppState {
    private SimpleApplication app;
    
    private Nifty nifty;
    
    public boolean load = false;
    public int frameCount = 0;  
    
    @Override
    protected void initialize(Application app) {
    
        this.app = (SimpleApplication) app; // can cast Application to something more specific
        this.app.getRootNode();
        this.app.getAssetManager();
        this.app.getStateManager();
        this.app.getInputManager();
        this.app.getViewPort();
        nifty = PlayGame.nifty;
        
        
        createGameModeScreen();
        
    }

    @Override
    protected void cleanup(Application app) {
        PlayGame.gameplayAppState.setEnabled(true); //required
                
        System.out.println(this.nifty.getCurrentScreen().getScreenId()+" screen cleanup called.....");
        
                
    }

    //onEnable()/onDisable() can be used for managing things that should     
    //only exist while the state is enabled. Prime examples would be scene     
    //graph attachment or input listener attachment.    
    @Override
    protected void onEnable() {
        
        
        nifty.gotoScreen("Screen_GameMode");
                
                
        PlayGame.gameplayAppState.setEnabled(false);
        
        
        System.out.println(this.getClass().getName()+" enabled....."); 
                
    }
    
    @Override
    protected void onDisable() {
        
        
        nifty.removeScreen("Screen_GameMode");
        
            //Called when the state was previously enabled but is now disabled         
        //either because setEnabled(false) was called or the state is being         
        //cleaned up.    
    }
    
    @Override
    public void update(float tpf) {       
        //TODO: implement behavior during runtime    
         if (load){
            frameCount++;
       
        if(frameCount == 300){
                
            startSingleLevel();
//            load = false;
        }
//        System.out.println(this.getClass().getName()+" FrameCount: "+frameCount);
         
       } 
    }
    
    public void createGameModeScreen(){
        
               
        
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
                        
                    
                    image(new ImageBuilder("img_Background") {{
                            filename("Interface/Images/background_book.png");
                            height("*");
                            width("*");
                    }});
                }}); //end layer background
                
                layer(new LayerBuilder("Layer_GameMode_Main"){{
                    childLayoutVertical();
                               
                    panel(new PanelBuilder("Panel_GameMode_Title"){{
                        height("15%");
                        width("100%"); 
                        //backgroundColor("#ccc3");
                        
                        childLayoutHorizontal();
                            text(new TextBuilder("static_title") {{
                                text("Select Game Mode");
                                font("Interface/Fonts/verdana-48-regular.fnt");
                                height("50%");
                                width("50%");
                                alignCenter();
                                valignBottom();
                            }});
                            
                            control(new LabelBuilder("text_GameMode") {{
                                text("Single Player - Story Mode");
                                font("Interface/Fonts/verdana-48-regular.fnt");
                                height("50%");
                                width("50%");
                                alignCenter();
                                valignBottom();
                            }});
                    }}); //end panel title 
                    
                    
                    panel(new PanelBuilder("Panel_GameMode_Content"){{ //consists of 2 panels (right and left)
                        height("70%");
                        width("100%"); 
                        //backgroundColor("#fff6");
                        childLayoutHorizontal();
                    
                    panel(new PanelBuilder("Panel_GameMode_Left"){{ //for the menuitems and settings
                        alignCenter();
                        valignCenter();
                        height("100%");
                        width("50%"); 
                        padding("10px");
                        childLayoutVertical();
                        //backgroundColor("#fcc1");
                                                                     
                    
                        panel(new PanelBuilder("Panel_GameMode_SelectMode"){{
                        height("40%");
                        width("100%"); 
                        childLayoutVertical();
                                 
                            image(new ImageBuilder("btn_GameMode_SP"){{
                                filename("Interface/Images/MenuUI/button_0_gm_sp.png");
                                height("70px");
                                width("350px");  
                                alignCenter();    
                                interactOnClick("showSPMenu()");
                                onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                    effectParameter("active", "Interface/Images/MenuUI/button_1_gm_sp.png"); 
                                    effectParameter("inactive", "Interface/Images/MenuUI/button_0_gm_sp.png"); }});
                                
                                onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});

                            }});
                        
                            image(new ImageBuilder("btn_GameMode_MP"){{
                                filename("Interface/Images/MenuUI/button_0_gm_mp.png");
                                height("70px");
                                width("350px");  
                                alignCenter();    
                                interactOnClick("showMPMenu()");
                                onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                    effectParameter("active", "Interface/Images/MenuUI/button_1_gm_mp.png"); 
                                    effectParameter("inactive", "Interface/Images/MenuUI/button_0_gm_mp.png"); }});
                                
                                onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});

                            }});
                            
                            image(new ImageBuilder("btn_GameMode_SL"){{
                                filename("Interface/Images/MenuUI/button_0_gm_sl.png");
                                height("70px");
                                width("350px");  
                                alignCenter();    
                                interactOnClick("showSLMenu()");
                                onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                    effectParameter("active", "Interface/Images/MenuUI/button_1_gm_sl.png"); 
                                    effectParameter("inactive", "Interface/Images/MenuUI/button_0_gm_sl.png"); }});
                                
                                onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
//                                onClickEffect(new EffectBuilder("gradient") {{
//                                  effectValue("offset", "0%", "color", "#66666fff"); 
//                                  effectValue("offset", "85%", "color", "#000f"); 
//                                  effectValue("offset", "100%", "color", "#44444fff"); 
//                                  neverStopRendering(false);
//                                }}); 
                                
                            }});
                            

                                
                        }});  //end Select Game Mode panel  
                            
                        panel(new PanelBuilder("Panel_GameMode_Settings"){{ //bottom panel if you add controls to set gameplay
                        alignCenter();
                        height("60%");
                        width("80%"); 
                        backgroundColor("#ffc3");
                        childLayoutAbsoluteInside();  
                        
                            control(new LabelBuilder("text_Settings"){{
                                text("Level settings");    
                                font("Interface/Fonts/verdana-48-regular.fnt");
                                color("#00f9");
                                height("30%");
                                width("100%"); 
                                x("20px");
                                y("0px");  
                                
                            }});
                            
                            
                            control(new RadioGroupBuilder("RadioGroup_SceneTime"));
                            
                            control(new LabelBuilder("label_DayTime") {{
                                        text("DayTime");   
                                        x("20px");
                                        y("80px");    

                            }});

                            control(new RadioButtonBuilder("rb_DayTime") {{
                                        x("100px");
                                        y("80px");
                                        group("RadioGroup_SceneTime");
                                        
                            }});
                            
                            control(new LabelBuilder("label_NightTime") {{
                                        text("NightTime");   
                                        x("20px");
                                        y("120px");    

                            }});

                            control(new RadioButtonBuilder("rb_NightTime") {{
                                        x("100px");
                                        y("120px");
                                        group("RadioGroup_SceneTime");
                            }});
                          
                            
                                                
                           //add some more controls, checkboxes, etc.
                                                
                        }});
                                              
                    }});    
                                               
                                                
                        panel(new PanelBuilder("Panel_GameMode_Right"){{
                        height("100%"); 
                        width("50%");
                        alignCenter();
                        childLayoutCenter();
                        //backgroundColor("#ccc8");
                            
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
                                                height("110%");

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
                        
                                    control(new LabelBuilder("label_MultiPlayer"){{
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
                                                width("*");
                                                height("110%");

                                    }});
                                }});   
                            
                                panel(new PanelBuilder("Panel_GameMode_SL_Image"){{
                                height("70%"); 
                                width("90%");
                                alignCenter();
                                childLayoutCenter();
                                //backgroundColor("#ff01");
                                
                                    image(new ImageBuilder("img_SingleLevel") {{
                                    filename("Interface/Images/gm_tour.png");
                                    height("100%");
                                    width("100%");
                                    }});    
                                }}); 
                                
                                panel(new PanelBuilder("Panel_GameMode_SL_Text"){{
                                height("20%"); 
                                width("100%");
                                alignCenter();
                                valignCenter();
                                childLayoutCenter();
                                                        
                                    control(new LabelBuilder("label_SingleLevel"){{
                                    text("Take a free tour on a \n"
                                            + "selected map");    
                                    font("Interface/Fonts/Default.fnt");
                                    color("#0009");
                                    height("100%");
                                    width("100%");
                                    align(Align.Center);
                                    valignCenter();
                                    }});
                                }});    
                                                        
                            }}); //gamemode sl panel end
                            
                         }});  //right panel end   
                                        
                        }}); //panel content end
                        
                       panel(new PanelBuilder("Panel_GameMode_ScreenButtons"){{
                            height("15%");
                            width("50%");
                            //backgroundColor("#ccc8");
                            alignRight();
                            childLayoutHorizontal();
                            
                                panel(new PanelBuilder("Panel_ScreenButtons_PlayGame"){{
                                height("*");
                                width("50%");
                                alignCenter();
                                childLayoutCenter();
                            
                                image(new ImageBuilder("gamemode_PlayGame"){{
                                filename("Interface/Images/MenuUI/button_0_playgame.png");
                                height("40px");
                                width("150px");
                                alignCenter();
                                valignTop();
                                padding("10px");
                                interactOnClick("playGame()");
                                interactOnMouseOver("buttonEffect()");

                                onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                    effectParameter("active", "Interface/Images/MenuUI/button_1_playgame.png"); neverStopRendering(true);
                                    effectParameter("inactive", "Interface/Images/MenuUI/button_0_playgame.png"); neverStopRendering(true);}});
                                onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "-15");}});
                                onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});

                                }});
                                }}); //panel for playgame button
                        
                                
                                panel(new PanelBuilder("Panel_ScreenButtons_Back"){{
                                height("*");
                                width("50%");
                                alignCenter();
                                childLayoutCenter();
                                image(new ImageBuilder("gamemode_Back"){{
                                filename("Interface/Images/MenuUI/button_0_back.png");
                                height("40px");
                                width("150px"); 
                                alignCenter();
                                valignTop();
                                padding("10px");
                                interactOnClick("backToMainMenu()");
                                interactOnMouseOver("buttonEffect()");

                                onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                    effectParameter("active", "Interface/Images/MenuUI/button_1_back.png"); neverStopRendering(true);
                                    effectParameter("inactive", "Interface/Images/MenuUI/button_0_back.png"); neverStopRendering(true);}});
                                onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                                onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});

                                }});
                                }}); //panel for back button
                      
                        }}); //end panel screenbuttons 
                        
                                   
                }}); //layer main end
                }}.build(nifty));
                        
                nifty.gotoScreen("Screen_GameMode");
    }
    
}