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
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;

/**
 *
 * @author TE332168
 */
public class Screen extends BaseAppState {
    
    private SimpleApplication app;
    
    private AssetManager      assetManager;
    private AppStateManager   stateManager;
    private InputManager      inputManager;
    private RenderManager     renderManager;
    private AudioRenderer     audioRenderer;
    private ViewPort          viewPort;
    private Nifty nifty;
    
    @Override
    public void initialize(Application app) {
        
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
    
    @Override
    public void cleanup(Application app) {
        
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
    
    @Override
    protected void onEnable() {
        NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
        nifty = niftyDisplay.getNifty();
        
        app.getGuiViewPort().addProcessor(niftyDisplay); 
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        
        nifty.addScreen("MainMenu", new ScreenBuilder("Main menu"){{
                controller(new mygame.StartScreen());
                
                layer(new LayerBuilder("MenuLayer"){{
                    childLayoutVertical();
                    
                panel(new PanelBuilder("MenuPanel"){{
                    childLayoutCenter();
                    alignLeft();
                    valignCenter();
                    height("25%");
                    width("25%"); 
//                    backgroundColor("#f00");
                    
                                  
                control(new ButtonBuilder("PlayButton", "Play Game"){{
                    alignLeft();
                    valignTop();
                    height("5%");
                    width("15%"); 
                    interactOnClick("startGame(MainMenu)");
                
                
                control(new ButtonBuilder("ExitButton", "Exit Game"){{
                    alignLeft();
                    valignCenter();
                    height("5%");
                    width("15%");
                    interactOnClick("quitGame()");
                }});
                }});
                }});
                }});
                
                layer(new LayerBuilder("foreground"){{
                childLayoutHorizontal();
//                backgroundColor("#0000");
                
                panel(new PanelBuilder("foreground"){{
                    childLayoutCenter();
                    alignLeft();
                    valignTop();
                    height("25%");
                    width("25%"); 
//                    backgroundColor("#f00");
                    
                    text(new TextBuilder() {{
                    text("Main Menu");
                    font("Interface/Fonts/Antiqua.fnt");
                    height("100%");
                    width("100%");
                    alignLeft();
                    valignTop();
                }});
                }});
                }});
                }}.build(nifty));
                
                nifty.gotoScreen("MainMenu");
    }

    @Override
    protected void onDisable() {
        
    }

    
    
}
