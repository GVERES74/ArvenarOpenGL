/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;


import Levels.IntroMap;
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
    
    private Node startRootNode = new Node("Main Menu RootNode");
    private Node startGUINode = new Node("Main Menu GUINode");
    
    float screenHeight, screenWidth, timer;
    boolean animated = true; //Switch ON / OFF fly camera moving
    
    BitmapText menuItemText, camPosInfoText;
    
    IntroMap menuScene;

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
        
        
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
        camera.setLocation(new Vector3f(960f,42f,-1225f)); //flycam initposition
        camera.setRotation(new Quaternion().fromAngleNormalAxis(0.1f, Vector3f.UNIT_X)); //flycam looks down a bit
        this.app.getFlyByCamera().setEnabled(false); //disable camera movement with keyboard / mouse
        
        menuScene = new IntroMap();
        PlayGame.attachAppState(menuScene);
         
        initMenuControls();
        createAnimatedMainMenu();
//        createSimpleMainMenu();
        enableMainMenuScreen();
        
        AudioManager.loadRandomMusic();
           
    }
    
            
        protected void rotateCamera(float rotationSpeed, float value, float tpf
                            , Vector3f axis){
            
            Quaternion rotate = new Quaternion().fromAngleNormalAxis(rotationSpeed * value * tpf, axis);
            Quaternion q = rotate.mult(camera.getRotation());
            camera.setRotation(q);
              
        }
        
        
        protected void moveCamera(boolean enabled){
            if (enabled == true){
            Vector3f camDirection = camera.getDirection();
            Vector3f camLocation = camera.getLocation();
                      
            float moveX = camDirection.x/10;
            float moveZ = camDirection.z/10;
            float camx = camLocation.x;
            float camz = camLocation.z;
            float camy = camLocation.y;
                camera.setLocation(new Vector3f(camx+moveX, camy, camz+moveZ));
            }    
            
            else if (enabled == false){
                camera.setLocation(new Vector3f(923f,45f,-1246f));
                camera.setRotation(new Quaternion(0.05f, -0.3f, -0.02f, 0.8962425f));
            }

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
                        if (isPressed){
                        animated = false;
                        createSimpleMainMenu(); break;
                        }
                }
                }
                             
            };
            
        
    @Override
    public void update(float tpf) {
        timer +=tpf;
        //changeIntroCam();
        
        if (animated == true){
        rotateCamera(-0.1f, 1,tpf,Vector3f.UNIT_Y);
        moveCamera(true);
        }
        else if (animated == false){
            rotateCamera(0f,0f,0f,Vector3f.UNIT_Y);
            moveCamera(false);                                
        }
       
    }
    
    @Override
    public void cleanup(Application app) {
        System.out.println("MainMenuScreen cleanup called.....");
        inputManager.deleteMapping("SkipIntro");
                
        this.app.getFlyByCamera().setEnabled(true); //enable camera movement with keyboard / mouse       
    }    
          
   
    @Override
    protected void onEnable(){
        timer = 0;               
        System.out.println(this.getClass().getName()+" enabled....."); 
    }
    

    @Override
    protected void onDisable() {
        disableMainMenuScreen();
        //AudioManager.musicPlayer.stop();
    }
    
    public void createAnimatedMainMenu(){
        
        app.setDisplayStatView(false); 
        nifty = PlayGame.getNiftyDisplay().getNifty();
        app.getFlyByCamera().setDragToRotate(true);
        
        app.getGuiViewPort().addProcessor(PlayGame.getNiftyDisplay()); 
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        nifty.registerSound("btnclick", "Interface/sound/click.wav");
                
        nifty.addScreen("Screen_AnimatedMainMenu", new ScreenBuilder("Main Menu"){{
                controller(new mygame.MainMenuScreenController());
                defaultFocusElement("menuimg_Play");
                     

                    layer(new LayerBuilder("Layer_Menu_Intro"){{
                        childLayoutVertical();

                        panel(new PanelBuilder("Panel_Menu_Cat"){{    
                            childLayoutCenter();
                                height("30%");
                                width("100%");
                            image(new ImageBuilder("logo_Cat") {{
                                filename("Interface/Images/greetingcat.png");
                                height("100%");
                                width("20%");
                                                                                                
                                onActiveEffect(new EffectBuilder("fade") {{ //fade in and fade out effect :)
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
                        }}); //panel catlogo end        
                            
                        panel(new PanelBuilder("Panel_Menu_Texts"){{
                                childLayoutCenter();
                                height("50%");
                                width("100%");
                            
                            text(new TextBuilder() {{
                                text("Greeting Cat Game Studio presents\n"
                                        + "Arvenar - The Lost Traveller");
                                font("Interface/Fonts/Default.fnt");
                                color("#0009");
                                height("100%");
                                width("100%");
                                alignCenter();
                                
                                onActiveEffect(new EffectBuilder("fade") {{
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
                                color("#0009");
                                height("100%");
                                width("100%");
                                alignCenter();
                                
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
                    
                    }}); //panel texts end
                                                                               
                    panel(new PanelBuilder("Panel_Menu_SkipIntroText"){{
                            childLayoutCenter();
                            height("20%");
                            width("50%");
                                
                                text(new TextBuilder() {{
                                text("Press ESC to skip intro");
                                font("Interface/Fonts/Default.fnt");
                                color("#0009");
                                height("80%");
                                width("80%");
                                alignCenter();
                                                                                                
                                onActiveEffect(new EffectBuilder("colorPulsate") {{
                                    
                                    effectParameter("startColor", "#fff6");
                                    effectParameter("endColor", "#0009");
                                    effectParameter("pulsateType", "sin");
                                    effectParameter("period", "2000");

                                }});  
                           
                                
                        }}); 
                        }}); //panel skipintro end            
                        }}); //layer intro end
                     
                    layer(new LayerBuilder("Layer_Menu_Main"){{
                    childLayoutVertical();
                    
                    onActiveEffect(new EffectBuilder("fade") {{
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
                        childLayoutVertical();
                        alignLeft();
                        valignCenter();
                        padding("20px");
                        height("300px");
                        width("250px"); 
                                                
                        //backgroundColor("#eee3"); //last digit sets the alpha channel
//                        style("nifty-panel");
                                           
                        
                        
                        image(new ImageBuilder("menuimg_Play"){{
                            filename("Interface/Images/MenuUI/button_0_playgame.png");
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
                       

                }});
                    
                }}.build(nifty));
        
                
               nifty.gotoScreen("Screen_AnimatedMainMenu");
        
    }
    
    public void createSimpleMainMenu(){
                
        app.setDisplayStatView(false); 
        nifty = PlayGame.getNiftyDisplay().getNifty();
        app.getFlyByCamera().setDragToRotate(true);
        
        app.getGuiViewPort().addProcessor(PlayGame.getNiftyDisplay()); 
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        nifty.registerSound("btnclick", "Interface/sound/click.wav");
                
        nifty.addScreen("Screen_SimpleMainMenu", new ScreenBuilder("Main Menu"){{
                controller(new mygame.MainMenuScreenController());
                defaultFocusElement("menuimg_Play");
                
                //this is only for static menuscreen, i prefer moving the camera accross the landscape
                layer(new LayerBuilder("Layer_MainMenu_BackgroundImage"){{
                        
                        childLayoutCenter();
//                        image(new ImageBuilder("img_Loading_Background") {{
//                            filename("Interface/Images/bkg_pirate_table.jpg");
//                            height("100%");
//                            width("100%");
//                        }});
                }}); //end layer bkgimage
                
                layer(new LayerBuilder("Layer_Menu_Main"){{
                    childLayoutVertical();
                   
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
                        childLayoutVertical();
                        
                        padding("20px");
                        height("300px");
                        width("250px"); 
                                                
                        //backgroundColor("#eee3"); //last digit sets the alpha channel
//                        style("nifty-panel");
                        
                        image(new ImageBuilder("menuimg_Play"){{
                            filename("Interface/Images/MenuUI/button_0_playgame.png");
                            
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
                        
                }});

                   
                }}.build(nifty));
                nifty.gotoScreen("Screen_SimpleMainMenu");
    }
    
    
    public void enableMainMenuScreen(){
        nifty.gotoScreen("Screen_SimpleMainMenu");
        app.getFlyByCamera().setDragToRotate(true);
        
    }
    
    public void disableMainMenuScreen(){
        nifty.removeScreen("Screen_SimpleMainMenu");
        app.getFlyByCamera().setDragToRotate(false);
        
    }
    
    public void changeIntroCam(){
        if (timer > 500) camera.setLocation(new Vector3f(1150f,50f,-950f)); //camera position 1}
        if (timer > 1000) camera.setLocation(new Vector3f(800f,80f,-1400f)); //camera position 2}
        
    }
 }

                                  

