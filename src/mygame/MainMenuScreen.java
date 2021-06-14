/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;


import Levels.S0M0_valley;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
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

    


/**
 *
 * @author TE332168
 */
public class MainMenuScreen extends BaseAppState {
    
    private SimpleApplication app;
    private Nifty nifty;
    
    private Node              rootNode;
    private AssetManager      assetManager;
    private AppStateManager   stateManager;
    private InputManager      inputManager;
    private RenderManager     renderManager;
    private AudioRenderer     audioRenderer;
    private ViewPort          viewPort;
    private Camera camera;
    private AudioNode soundPlayer;
    
    public S0M0_valley levelS0M0;
    private Spatial level;
    
    private Node startRootNode = new Node("Main Menu RootNode");
    private Node startGUINode = new Node("Main Menu GUINode");
    
    float screenHeight, screenWidth;
    
    BitmapText menuItemText, camPosInfoText;

//    public MainMenuScreen() {
//        
//    }
        
    
    @Override
    public void initialize(Application app) {
        
        this.app = (SimpleApplication) app;   
        
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
            
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
        this.camera       = this.app.getCamera();
        
        screenHeight = app.getCamera().getHeight();
        screenWidth = app.getCamera().getWidth();
        
        rootNode.attachChild(startRootNode);
       // rootNode.attachChild(startGUINode);
        
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
        
        levelS0M0 = new S0M0_valley();
        
        setLevel("Scenes/S0_Snowenar/S0M0_walley.j3o", levelS0M0);
        
        camera.setLocation(levelS0M0.getInitPlayerPosition());
       
        //this.app.getFlyByCamera().setEnabled(false);
        
        initMenuControls();
        createMainMenu();
        
        
    //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }
    
            
        protected void rotateCamera(float rotationSpeed, float value, float tpf
                            , Vector3f axis){
            
            Quaternion rotate = new Quaternion().fromAngleNormalAxis(rotationSpeed * value * tpf, axis);
            Quaternion q = rotate.mult(camera.getRotation());
            camera.setRotation(q);
              
        }
        
        
        protected void moveCamera(){
            Vector3f camDirection = camera.getDirection();
            Vector3f camLocation = camera.getLocation();
                      
            float moveX = camDirection.x/500;
            float moveZ = camDirection.z/300;
            float camx = camLocation.x;
            float camz = camLocation.z;
            float camy = camLocation.y;
                camera.setLocation(new Vector3f(camx+moveX, camy, camz+moveZ));

        }
        
        
                        
        public void playSound(String filepath, boolean directional, boolean positional, boolean looping, float volume, float xpos, float ypos, float zpos){
        soundPlayer = new AudioNode(assetManager, filepath);
        soundPlayer.setDirectional(directional);
        soundPlayer.setPositional(positional);
        soundPlayer.setLooping(looping);
        soundPlayer.setVolume(volume);
        startRootNode.attachChild(soundPlayer);
        soundPlayer.play();
    }     
                
        public void initMenuControls(){
        
            
            inputManager.addMapping("SkipIntro", new KeyTrigger(KeyInput.KEY_ESCAPE));
            
            inputManager.addListener(actionListener, "SkipIntro");
        
        
        }
    
        private final ActionListener actionListener = new ActionListener() {
            @Override
            public void onAction(String mappedName, boolean isPressed, float tpf) {
                switch (mappedName) {
                    case "SkipIntro": 
                        
                        //stateManager.attach(settingsScreen);
                        
                }
                }
                             
            };
            
        
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        rotateCamera(-0.1f, 1,tpf,Vector3f.UNIT_Y);
        moveCamera();
       
    }
    
    @Override
    public void cleanup(Application app) {
        System.out.println("MainMenuScreen cleanup called.....");
       // startRootNode.detachAllChildren();
       // rootNode.detachChild(mainScene);

    }    
        
        
   
    @Override
    protected void onEnable(){
        enableMainMenuScreen();       
    }


    @Override
    protected void onDisable() {
        disableMainMenuScreen();
        
    }
    
    public void createMainMenu(){
        
        app.setDisplayStatView(false); 
        nifty = PlayGame.getNiftyDisplay().getNifty();
        app.getFlyByCamera().setDragToRotate(true);
        
        app.getGuiViewPort().addProcessor(PlayGame.getNiftyDisplay()); 
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        nifty.registerSound("btnclick", "Interface/sound/click.wav");
//        nifty.registerMusic("maintheme", "Music/Soundtracks/RPG_Ambient_2.ogg");
                
        nifty.addScreen("Screen_MainMenu", new ScreenBuilder("Main Menu"){{
                controller(new mygame.MainMenuScreenController());
                defaultFocusElement("menuimg_Play");
                
                layer(new LayerBuilder("Layer_Menu_Main"){{
                    childLayoutVertical();
//                    onStartScreenEffect(new EffectBuilder("playSound") {{
//                            effectParameter("sound", "maintheme");
//                            }});    
                    
                    onStartScreenEffect(new EffectBuilder("fade") {{
                            startDelay(22000);
                                length(3000);    
                                effectParameter("start", "#00");
                                effectParameter("end", "#ff");
                                neverStopRendering(true);
                            }});    
                    
                    
                    panel(new PanelBuilder("Panel_Menu_Title"){{
                        childLayoutVertical();
                        alignLeft();
                        valignCenter();
                        height("150px");
                        width("400px");
                        //backgroundColor("#cff5");
                        
                            image(new ImageBuilder("imagelogo_Title") {{
                                filename("Interface/Images/MenuUI/mainlogo1.png");
                                height("180px");
                                width("380px"); 
                                alignCenter();
                                valignCenter();
                                
                            }});
                    }});        
                    
                    panel(new PanelBuilder("Panel_Menu_ForButtons"){{
//                        childLayoutVertical();
                        childLayoutAbsoluteInside();
                        alignLeft();
                        valignCenter();
                        height("300px");
                        width("250px"); 
                                                
                        //backgroundColor("#eee3"); //last digit sets the alpha channel
//                        style("nifty-panel");
                                           
                        
                        
                        image(new ImageBuilder("menuimg_Play"){{
                            filename("Interface/Images/MenuUI/button_0_playgame.png");
                            x("20px");
                            y("20px");
                            height("45px");
                            width("150px");  
                            
                            interactOnClick("startGame()");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_playgame.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_playgame.png"); neverStopRendering(true);}});
                                onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                            
                        }});
                        
                        image(new ImageBuilder("menuimg_Settings"){{
                            filename("Interface/Images/MenuUI/button_0_settings.png");
                            x("20px");
                            y("70px");
                            height("45px");
                            width("150px");    
                            interactOnClick("settingsGame()");
                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_settings.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_settings.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                        }});
                        
                        image(new ImageBuilder("menuimg_Credits"){{
                            filename("Interface/Images/MenuUI/button_0_credits.png");
                            x("20px");
                            y("120px");
                            height("45px");
                            width("150px");   
                            interactOnClick("creditsGame()");
                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_credits.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_credits.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                        }});

                        image(new ImageBuilder("menuimg_Extras"){{
                            filename("Interface/Images/MenuUI/button_0_extras.png");
                            x("20px");
                            y("170px");
                            height("45px");
                            width("150px");    
                            interactOnClick("gameExtras()");
                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_extras.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_extras.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                        }});
                        
                        image(new ImageBuilder("menuimg_Quit"){{
                            filename("Interface/Images/MenuUI/button_0_quit.png");
                            x("20px");
                            y("220px");
                            height("45px");
                            width("150px");    
                            interactOnClick("quitGame()");
                            backgroundColor("#0c01");
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_quit.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_quit.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+10");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                        }});
                        
                    }});    
                        
//                        popup(new PopupBuilder("popupExit") {{
//                            childLayoutCenter();
//                            backgroundColor("#000a");
//                        }}.registerPopup(nifty));
                          
                        
         }});

                    layer(new LayerBuilder("Layer_Menu_Logo"){{
                        childLayoutAbsoluteInside();

                            image(new ImageBuilder("logo_Cat") {{
                                filename("Interface/Images/greetingcat.png");
                                
                                height("180px");
                                width("180px");
                                x("600px");
                                y("100px");
                                
                                onStartScreenEffect(new EffectBuilder("fade") {{ //fade in and fade out effect :)
                                startDelay(1000);
                                //length(8000);    
                                effectValue("time", "3000", "value", "0.0");
                                effectValue("time", "5000", "value", "1.0");
                                effectValue("time", "10000", "value", "1.0");
                                effectValue("time", "12000", "value", "0.0");
                                post(false);
                                neverStopRendering(true);
                                }});
                            }});
                            
                            text(new TextBuilder() {{
                                text("Greeting Cat Game Studio presents\n"
                                        + "Arvenar - The Lost Traveller");
                                font("Interface/Fonts/Default.fnt");
                                height("100%");
                                width("100%");
                                x("500px");
                                y("300px");
                                onStartScreenEffect(new EffectBuilder("fade") {{
                                startDelay(1000);
                                //length(8000);    
                                effectValue("time", "3000", "value", "0.0");
                                effectValue("time", "5000", "value", "1.0");
                                effectValue("time", "10000", "value", "1.0");
                                effectValue("time", "12000", "value", "0.0");
                                post(false);
                                neverStopRendering(true);
                                }});
                            }});    
                             
                             text(new TextBuilder() {{
                                text("A Greeting Cat Production\n"
                                        + "Design and programming by Gabor Veres\n"
                                        + "Powered by jMonkeyEngine3.0\n"
                                        + "Powered by NiftyGUI\n"
                                        + "Powered by OpenGameArt.org\n"
                                        + "https://opengameart.org");
                                 font("Interface/Fonts/Default.fnt");
                                height("100%");
                                width("100%");
                                x("500px");
                                y("300px");
                                onStartScreenEffect(new EffectBuilder("fade") {{
                                startDelay(12000);
                                //length(8000);    
                                effectValue("time", "3000", "value", "0.0");
                                effectValue("time", "5000", "value", "1.0");
                                effectValue("time", "10000", "value", "1.0");
                                effectValue("time", "12000", "value", "0.0");
                                post(false);
                                neverStopRendering(true);
                                }});
                            }});
                    
                     
                }}); //layer logo end
                }}.build(nifty));
        
                
               nifty.gotoScreen("Screen_MainMenu");
        
        
    }
    
    public void enableMainMenuScreen(){
        nifty.gotoScreen("Screen_MainMenu");
        app.getFlyByCamera().setDragToRotate(true);
    }
    
    public void disableMainMenuScreen(){
        nifty.removeScreen("Screen_MainMenu");
        app.getFlyByCamera().setDragToRotate(false);
    }
    
    
    public void setLevel(String levelname, BaseAppState levelid) {
        
        level = this.app.getAssetManager().loadModel(levelname);
        level.setLocalTranslation(0f, 0f, 0f);
        this.app.getRootNode().attachChild(level);
        app.getStateManager().attach(levelid);
        
    }

    public Spatial getLevel() {
        return level;
    }
    
    
 }

                                  

