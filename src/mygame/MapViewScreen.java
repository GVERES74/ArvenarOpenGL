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
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

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
    private int screenHeight;
    
    @Override
    protected void initialize(Application app) {
    
        this.app = (SimpleApplication) app; // can cast Application to something more specific
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
        
        screenHeight = PlayGame.getPlayGameAppSettings().getHeight()-100;
    }

    @Override
    protected void cleanup(Application app) {
        
        //TODO: clean up what you initialized in the initialize method,        
        //e.g. remove all spatials from rootNode    
    }

    //onEnable()/onDisable() can be used for managing things that should     
    //only exist while the state is enabled. Prime examples would be scene     
    //graph attachment or input listener attachment.    
    @Override
    protected void onEnable() {
        createMapViewScreen();
        //Called when the state is fully enabled, ie: is attached and         
        //isEnabled() is true or when the setEnabled() status changes after the         
        //state is attached.    
    }
    
    @Override
    protected void onDisable() {
    
        nifty.removeScreen("Screen_MapScreen");
        app.getFlyByCamera().setDragToRotate(false);
        PlayGame.ingameHud.enableHud();
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
        
            nifty.registerSound("btnclick", "Interface/sound/metalClick.ogg");
                   
            nifty.addScreen("Screen_MapScreen", new ScreenBuilder("View Map"){{
                controller(new mygame.MapViewScreenController());
                defaultFocusElement("settings_CloseMap");
                
                layer(new LayerBuilder("Layer_MapView"){{
                    childLayoutAbsoluteInside();
                    
//                    onStartScreenEffect(new EffectBuilder("playSound") {{
//                        effectParameter("sound", "settingstheme");
//                    }}); 
                    image(new ImageBuilder("img_MapBackground") {{
                            filename("Interface/Images/bkg_pirate_table.jpg");
                            height("100%");
                            width("100%");
                            }});
                        
                    panel(new PanelBuilder("Panel_MapView_Title"){{
//                       backgroundColor("#ff02");  
                        x("20px");
                        y("20px");
                        height("100px");
                        width("300px");
                        childLayoutCenter();
                                                
                            text(new TextBuilder() {{
                                text("Map of Arvenar");
                                font("Interface/Fonts/verdana-48-regular.fnt");
                                height("100%");
                                width("100%");
                                alignCenter();
                                valignCenter();
                                                                                                
                            }});
                    }});
                     
                    panel(new PanelBuilder("Panel_MapView_MapHolder"){{
                      padding("20px");
                        x("300px");
                        y("20px");
                        height("90%");
                        width("65%");
                        childLayoutCenter();
                            image(new ImageBuilder("img_WorldMap") {{
                            filename("Interface/Images/Map/caribbean.jpg");
                            height("100%");
                            width("100%");
                            }});
                            image(new ImageBuilder("img_LocalMap") {{
                            filename("Interface/Images/Map/localmap.png");
                            height("100%");
                            width("100%");
                            }});
                        
                    }});
                               
                    panel(new PanelBuilder("Panel_MapView_ViewButtons"){{
                        x("50px");
                        y("120px");
                        height("500px");
                        width("500px"); 
                        childLayoutAbsoluteInside();
                                                                     
                            image(new ImageBuilder("btn_mapview_LocalMap"){{
                            filename("Interface/Images/MenuUI/button_0_localmap.png");
                            x("20px");
                            y("20px");
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
                            x("20px");
                            y("70px");
                            height("37px");
                            width("147px");  
                            
                            interactOnClick("showWorldMap()");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_worldmap.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_worldmap.png"); neverStopRendering(true);}});
                                onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                            
                            }});
                        }});    
                            
                    panel(new PanelBuilder("Panel_MapView_ZoomButtons"){{
                        x("500px");
                        y(SizeValue.px(screenHeight));
                        height("100px");
                        width("300px"); 
                        childLayoutAbsoluteInside();
                            
                            image(new ImageBuilder("btn_mapview_ZoomIn"){{
                            filename("Interface/Images/Map/button_0_zoomin.png");
                            x("5px");
                            y("5px");
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
                            x("180px");
                            y("5px");
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
                   
                   
                    panel(new PanelBuilder("Panel_Paused_ScreenButtons"){{
//                      
                        childLayoutAbsoluteInside();
                        padding("10px");
                        x("50px");
                        y("520px");
                        height("200px");
                        width("300px"); 
                        
                        image(new ImageBuilder("settings_Back"){{
                            filename("Interface/Images/MenuUI/button_0_back.png");
                            x("20px");
                            y("70px");
                            height("40px");
                            width("150px");    
                            interactOnClick("backToGame()");
                            interactOnMouseOver("buttonEffect()");
//                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_back.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_back.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                              
                        }});
                      
                    }});  
                
                }});
                }}.build(nifty));
                        
                nifty.gotoScreen("Screen_MapScreen");
    }
    
    public void enableMapView(){
        nifty.gotoScreen("Screen_MapScreen");
    }
       
}
