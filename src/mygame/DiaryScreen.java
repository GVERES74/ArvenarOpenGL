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
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.screen.Screen;



/**
 *
 * @author TE332168
 */
public class DiaryScreen extends BaseAppState {
    
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
        nifty = PlayGame.nifty;
        app.getGuiViewPort().addProcessor(PlayGame.niftyDisplay);
        screenWidth = PlayGame.appsettings.getWidth();
        screenHeight = PlayGame.appsettings.getHeight();
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
                
        createDiaryScreen();

    }
    
    @Override
    public void update(float tpf) {
        
    }
    
    
    @Override
    protected void cleanup(Application app) {
        System.out.println(this.nifty.getCurrentScreen().getScreenId()+" screen cleanup called.....");
        PlayGame.gameplayAppState.setEnabled(true);
        
    }

    @Override
    protected void onEnable() {
        showDiaryScreen();
        PlayGame.gameplayAppState.setEnabled(false);
        System.out.println(this.getClass().getName()+" enabled....."); 
        
    }
     
    @Override
    protected void onDisable() {

       hideDiaryScreen();
                        
    }
        
    public void createDiaryScreen(){
        
        app.getFlyByCamera().setDragToRotate(true);
        
        
            
            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
        
            nifty.registerSound("btnclick", "Interface/sound/click.wav");
            nifty.registerSound("openbook", "Interface/sound/bookOpen.ogg");
            nifty.registerSound("closebook", "Interface/sound/bookClose.ogg");
                   
            nifty.addScreen("Screen_DiaryBook", new ScreenBuilder("DiaryBook"){{
                controller(new mygame.DiaryScreenController());
                defaultFocusElement("settings_Back");
                
                layer(new LayerBuilder("Layer_Diary_Background"){{
                    childLayoutCenter();                    
                    onStartScreenEffect(new EffectBuilder("playSound") {{
                        effectParameter("sound", "openbook");
                    }}); 
                    
                    onEndScreenEffect(new EffectBuilder("playSound") {{
                        effectParameter("sound", "closebook");
                    }}); 
                    
                    image(new ImageBuilder() {{
                            filename("Interface/Images/background_book.png");
                            height("100%");
                            width("100%");
                    }});
                }}); //end layer background
                
                layer(new LayerBuilder("Layer_Diary_Content"){{
                    childLayoutVertical(); 
                    
                    panel(new PanelBuilder("Panel_Diary_Title"){{
                        height("10%");
                        width("30%"); 
                        //backgroundColor("#fff6");
                        childLayoutCenter();
                            text(new TextBuilder() {{
                                text("Mother's Diary");
                                font("Interface/Fonts/verdana-48-regular.fnt");
                                height("100%");
                                width("100%");
                                alignCenter();
                                                                
                            }});
                    }}); //end panel title 
                                        
                    panel(new PanelBuilder("Panel_Diary_Contents"){{
                        
                        height("80%");
                        width("100%");
                        childLayoutVertical();
                        
                                                
                        panel(new PanelBuilder("Panel_Diary_Sheets"){{
                        
                            height("100%");
                            width("100%");
                            childLayoutHorizontal();
                            alignCenter();
                                                                            
                        panel(new PanelBuilder("Panel_Diary_Content_SheetLeft"){{
                        
                            height("100%");
                            width("50%"); 
                            childLayoutVertical();
                            //backgroundColor("#00f1");
                            paddingLeft("150px");
                            paddingTop("20px");
                            alignCenter();
                            valignCenter();
                        
                        control(new LabelBuilder("Content_Text1"){{
                            
                            font("Interface/Fonts/Default.fnt");
                            color("#0009");
                            height("50%");
                            width("50%");
                            align(Align.Left);
                            valignCenter();
                        }});
                        
                        image(new ImageBuilder("Content_Image1"){{
                                
                                height("50%");
                                width("*"); 
                                alignCenter();
                                valignCenter();
                        }});    
                        
                    }}); //end panel Book Sheet Left 
                    
                    
                    panel(new PanelBuilder("Panel_Diary_Content_SheetRight"){{
                        
                        height("100%");
                        width("50%"); 
                        childLayoutVertical();
                        //backgroundColor("#55f1");
                        //paddingLeft("50px");
                        paddingTop("20px");
                        alignCenter();  
                        valignCenter();
                            image(new ImageBuilder("Content_Image2"){{
                                
                                height("50%");
                                width("50%"); 
                                alignCenter();
                                valignCenter();
                            }});
                            
                            control(new LabelBuilder("Content_Text2"){{
                                
                                font("Interface/Fonts/Default.fnt");
                                color("#0009");
                                height("40%");
                                width("*");
                                align(Align.Left);
                            }});    
                            
                            
                            
                     
                    }});//end panel Book Sheet Right 
                }});   //end panel Sheets
                   
                    panel(new PanelBuilder("Panel_Diary_ScreenButtons"){{
                        childLayoutHorizontal();
                        paddingLeft("150px");
                        //paddingTop("20px");
                        height("10%");
                        width("100%"); 
                        //backgroundColor("#0008");                        
                        
                        image(new ImageBuilder("pausedmenuimg_gamesettings"){{
                            filename("Interface/Images/MenuUI/button_0_pausedmenu_gamesettings.png");
                            height("40px");
                            width("200px");      
                            alignCenter();
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
                            alignCenter();
                            interactOnClick("backToGame()");
                            
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
                            alignCenter();
                            interactOnClick("backToMainMenu()");
                            
//                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_pausedmenu_quit.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_pausedmenu_quit.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                            }});
                                            
                    
                        image(new ImageBuilder("Diary_PrevPage"){{
                            filename("Interface/Images/MenuUI/button_0_book_prevpage.png");
                            height("40px");
                            width("100px");
                            alignCenter();
                            interactOnClick("prevPage()");
                                                        
//                          backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_0_book_prevpage.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_book_prevpage.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "-15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                            }});
                        
                        
                        image(new ImageBuilder("Diary_NextPage"){{
                            filename("Interface/Images/MenuUI/button_0_book_nextpage.png");
                            height("40px");
                            width("100px");
                            alignCenter();
                            interactOnClick("nextPage()");
                            
//                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_0_book_nextpage.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_book_nextpage.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                            }});
                        
                        }});
                      
                }}); //end panel contents
                }}); //end layer contents
                }}.build(nifty));
                        
                nifty.gotoScreen("Screen_DiaryBook");
    }
    
    public void showDiaryScreen(){
        nifty.gotoScreen("Screen_DiaryBook");
        app.getFlyByCamera().setDragToRotate(true);
        
    }
    
    public void hideDiaryScreen(){
        nifty.removeScreen("Screen_DiaryBook");
        app.getFlyByCamera().setDragToRotate(false);
            nifty.gotoScreen("Screen_HUD");
        
    }
}
