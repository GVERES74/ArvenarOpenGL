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
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;

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
        
        
        
         mainScene = assetManager.loadModel("Scenes/shore/mainScene.j3o");
            settingsRootNode.attachChild(mainScene);
            
            rootNode.attachChild(settingsRootNode);
            rootNode.attachChild(settingsGUINode);
            
                        
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
        NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
        nifty = niftyDisplay.getNifty();
        app.getFlyByCamera().setDragToRotate(true);
        
        app.getGuiViewPort().addProcessor(niftyDisplay); 
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        
        nifty.addScreen("Screen_GameSettings", new ScreenBuilder("Game Settings"){{
                controller(new mygame.SettingsScreenController());
                //defaultFocusElement("Button_Play");
                
                layer(new LayerBuilder("Layer_Settings_AllItems"){{
                    childLayoutVertical();
                                        
                    panel(new PanelBuilder("Panel_Settings_ForTitle"){{
                        childLayoutVertical();
                        alignLeft();
                        valignCenter();
                        height("100px");
                        width("250px"); 
                        
                            text(new TextBuilder() {{
                                text("Game Settings");
                                font("Interface/Fonts/Antiqua.fnt");
                                height("100%");
                                width("100%");
                                alignLeft();
                                valignTop();
                                
                            }});
                    }});        
                    
                    panel(new PanelBuilder("Panel_Settings_For_ScreenButtons"){{
//                        childLayoutVertical();
                        childLayoutAbsoluteInside();
                        alignLeft();
                        valignCenter();
                        height("200px");
                        width("300px"); 
                        padding("10px");
                        
                        backgroundColor("#eee1"); //last digit sets the alpha channel
//                        style("nifty-panel");
                        
                        control(new ButtonBuilder("Button_Apply", "Apply Changes"){{
//                            alignLeft();
//                            valignBottom();
                            x("20px");
                            y("20px");
                            height("50px");
                            width("220px");  
                            
                            interactOnClick("popupApplySettings()");
                            interactOnMouseOver("buttonEffect()");
                            
                        }});
                        
                        control(new ButtonBuilder("Button_Cancel", "Cancel Changes"){{
                            x("20px");
                            y("80px");
                            height("50px");
                            width("220px");    
                            interactOnClick("popupCancelSettings()");
                            interactOnMouseOver("buttonEffect()");
                            backgroundColor("#0c01");
                        }});
                        
                        
//                        popup(new PopupBuilder("popupExit") {{
//                            childLayoutCenter();
//                            backgroundColor("#000a");
//                        }}.registerPopup(nifty));
                            
                        
                }});
                }});
                }}.build(nifty));
        
                
                nifty.gotoScreen("Screen_GameSettings");
    }


    @Override
    protected void onDisable() {
        
    }
    
}
