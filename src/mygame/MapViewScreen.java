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
import de.lessvoid.nifty.screen.Screen;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class MapViewScreen extends BaseAppState {
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
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
        
        //ScrollPanelBuilder scrollpanel = new ScrollPanelBuilder("scroll");
        
        createMapViewScreen();
        
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
        
        showMapViewScreen();
        PlayGame.gameplayState.setEnabled(false);
        
    }
    
    @Override
    protected void onDisable() {
        
        hideMapViewScreen();
            //Called when the state was previously enabled but is now disabled         
        //either because setEnabled(false) was called or the state is being         
        //cleaned up.    
    }
    
    @Override
    public void update(float tpf) {
        
        //TODO: implement behavior during runtime    
    }
    
    public void createMapViewScreen(){
        
        app.getFlyByCamera().setDragToRotate(true);
        
        nifty = PlayGame.getNiftyDisplay().getNifty();
            app.getGuiViewPort().addProcessor(PlayGame.getNiftyDisplay());
            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
        
            nifty.registerSound("btnclick", "Interface/sound/click.wav");
            nifty.registerSound("openmap", "Interface/sound/book_flip_2.ogg");
                   
            nifty.addScreen("Screen_MapScreen", new ScreenBuilder("MapView"){{
                controller(new mygame.MapViewScreenController());
                defaultFocusElement("settings_CloseMap");
                
                layer(new LayerBuilder("Layer_MapView"){{
                    childLayoutCenter();
                    
                    
                    onStartScreenEffect(new EffectBuilder("playSound") {{
                        effectParameter("sound", "openmap");
                    }}); 
                    
                    onEndScreenEffect(new EffectBuilder("playSound") {{
                        effectParameter("sound", "openmap");
                    }}); 
                        
                    
                    image(new ImageBuilder() {{
                            filename("Interface/Images/Map/map_background.png");
                            height("100%");
                            width("100%");
                    }});
                        
                               
                    panel(new PanelBuilder("Panel_MapView_Buttons"){{
                        alignLeft();
                        valignCenter();
                        height("80%");
                        width("30%"); 
                        padding("20px");
                        childLayoutVertical();
                        backgroundColor("#ffc1");
                                                                     
                    
                        panel(new PanelBuilder("Panel_MapView_SelectMap"){{
                        height("20%");
                        width("100%"); 
                        childLayoutVertical();
                                 
                            image(new ImageBuilder("btn_mapview_LocalMap"){{
                                filename("Interface/Images/MenuUI/button_0_localmap.png");
                                height("37px");
                                width("147px");  

                                interactOnClick("showLocalMap()");
                                onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                    effectParameter("active", "Interface/Images/MenuUI/button_1_localmap.png"); neverStopRendering(true);
                                    effectParameter("inactive", "Interface/Images/MenuUI/button_0_localmap.png"); neverStopRendering(true);}});
                                    onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                                onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});

                                }});
                        
                                image(new ImageBuilder("btn_mapview_WorldMap"){{
                                filename("Interface/Images/MenuUI/button_0_worldmap.png");
                                height("37px");
                                width("147px");  

                                interactOnClick("showWorldMap()");
                                onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                    effectParameter("active", "Interface/Images/MenuUI/button_1_worldmap.png"); neverStopRendering(true);
                                    effectParameter("inactive", "Interface/Images/MenuUI/button_0_worldmap.png"); neverStopRendering(true);}});
                                    onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                                onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});

                                }});
                        }});  //end SelectMap panel  
                            
                    panel(new PanelBuilder("Panel_MapView_ZoomButtons"){{
                        alignLeft();
                        height("20%");
                        width("100%"); 
                        childLayoutVertical();
                                                    
                            image(new ImageBuilder("btn_mapview_ZoomIn"){{
                            filename("Interface/Images/Map/button_0_zoomin.png");
                            height("37px");
                            width("147px");  
                            
                            interactOnClick("zoomInMap()");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/Map/button_1_zoomin.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/Map/button_0_zoomin.png"); neverStopRendering(true);}});
                                onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                            
                            }});
                            
                            image(new ImageBuilder("btn_mapview_ZoomOut"){{
                            filename("Interface/Images/Map/button_0_zoomout.png");
                            height("37px");
                            width("147px");  
                            
                            interactOnClick("zoomOutMap()");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/Map/button_1_zoomout.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/Map/button_0_zoomout.png"); neverStopRendering(true);}});
                                onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                            
                            }});
                                                
                        }});
                    
                    
                    //map directions
                        panel(new PanelBuilder("Panel_MapView_DirectionButtons"){{
                        alignLeft();
                        
                        height("30%");
                        width("100%"); 
                        childLayoutAbsoluteInside();
                                                    
                            image(new ImageBuilder("btn_mapview_MoveUp"){{
                            filename("Interface/Images/Map/btn_mapup_0.png");
                            x("75px");
                            y("0px");
                            height("40px");
                            width("40px");  
                            
                            interactOnClick("mapUp()");
                            
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/Map/btn_mapup_1.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/Map/btn_mapup_0.png"); neverStopRendering(true);}});
                                
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                            
                            }});
                            
                            image(new ImageBuilder("btn_mapview_MoveDown"){{
                            filename("Interface/Images/Map/btn_mapdown_0.png");
                            x("75px");
                            y("75px");
                            height("40px");
                            width("40px");  
                            
                            interactOnClick("mapDown()");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/Map/btn_mapdown_1.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/Map/btn_mapdown_0.png"); neverStopRendering(true);}});
                                
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                            
                            }});
                            
                            image(new ImageBuilder("btn_mapview_MoveLeft"){{
                            filename("Interface/Images/Map/btn_mapleft_0.png");
                            x("35px");
                            y("35px");
                            height("40px");
                            width("40px");  
                            
                            interactOnClick("mapLeft()");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/Map/btn_mapleft_1.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/Map/btn_mapleft_0.png"); neverStopRendering(true);}});
                                
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                            
                            }});
                            
                             image(new ImageBuilder("btn_mapview_MoveRight"){{
                            filename("Interface/Images/Map/btn_mapright_0.png");
                            x("110px");
                            y("35px");
                            height("40px");
                            width("40px");  
                            
                            interactOnClick("mapRight()");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/Map/btn_mapright_1.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/Map/btn_mapright_0.png"); neverStopRendering(true);}});
                                
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                            
                            }});
                                                
                        }});
                        
                        panel(new PanelBuilder("Panel_MapView_ScreenButtons"){{
                        height("20%");
                        width("100%"); 
                        childLayoutVertical();
                        
                        image(new ImageBuilder("settings_Back"){{
                            filename("Interface/Images/MenuUI/button_0_back.png");
                            height("40px");
                            width("150px");    
                            interactOnClick("backToGame()");
                            interactOnMouseOver("buttonEffect()");

                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_back.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_back.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                              
                        }});
                      
                    }}); 
                        
                    }});    
                        
//                        control(new ScrollPanelBuilder("ScrollPanel_MapView_MapHolder"){{
//                        x("200px");
//                        y("50px");
//                        width("80%");
//                        height("80%");
//                        set("horizontal", "false");
//                        set("vertical", "false");
//                        //childLayoutOverlay();
//                        //backgroundColor("#0009");
                                                
                        panel(new PanelBuilder("Panel_MapView_MapImage"){{
                        height("80%"); 
                        width("70%");
                        alignCenter();
                        childLayoutCenter();
                        interactOnMouseWheel("zoomCurrentMap()");
                        
                            image(new ImageBuilder("img_LocalMap") {{
                                filename("Interface/Images/Map/map_local.png");
                                height("100%");
                                width("100%");
                                }});    
                            image(new ImageBuilder("img_WorldMap") {{
                                filename("Interface/Images/Map/caribbean.png");
                                height("100%");
                                width("100%");
                                }});
                            
                    }});    
//                    }}); //ScrollPanel end
                        
                                   
                }});
                }}.build(nifty));
                        
                nifty.gotoScreen("Screen_MapScreen");
    }
    
    public void showMapViewScreen(){
        app.getFlyByCamera().setDragToRotate(true);
        nifty.gotoScreen("Screen_MapScreen");
    }
    
    public void hideMapViewScreen(){
        app.getFlyByCamera().setDragToRotate(false);
        nifty.removeScreen("Screen_MapScreen");
        PlayGame.getNiftyDisplay().getNifty().gotoScreen("Screen_HUD");        
    }
       
}
