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
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
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
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;

    


/**
 *
 * @author TE332168
 */
public class CreditsScreen extends BaseAppState {
    
    
    private SimpleApplication app;
    private Node              rootNode;
    private AssetManager      assetManager;
    private AppStateManager   stateManager;
    private InputManager      inputManager;
    private RenderManager     renderManager;
    private AudioRenderer     audioRenderer;
    private ViewPort          viewPort;
    private Nifty nifty;
    
         
    private Spatial creditsScene;
    private Node creditsRootNode = new Node("Credits RootNode");
    private Node creditsGUINode = new Node("Credits GUINode");
    
    float screenHeight, screenWidth;
    
    BitmapText menuItemText, camPosInfoText;
    
    
    @Override
    public void initialize(Application app) {
        
        this.app = (SimpleApplication) app; // can cast Application to something more specific
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
                                        
        screenHeight = app.getCamera().getHeight();
        screenWidth = app.getCamera().getWidth();
        
        rootNode.attachChild(creditsRootNode);
       // rootNode.attachChild(creditsGUINode);
       
                          
//        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
       
        //this.app.getFlyByCamera().setEnabled(false);
                
        loadCreditsScene();
        createWorldLight();
        createPrecipitation();
        loadCreditsMusic();
        
    
    }
    
        
        public void loadCreditsScene(){
            creditsScene = this.app.getAssetManager().loadModel("Scenes/town/main.j3o");
            rootNode.attachChild(creditsScene);
            app.getCamera().setLocation(new Vector3f(50f, 5f, 0f));
            app.getCamera().setRotation(new Quaternion().fromAngleAxis(0, Vector3f.UNIT_Y)); //initial camera direction
            
        }
    
        public void loadCreditsMusic(){

            PlayGame.playMusic("Music/Soundtracks/RPG_Ambient_3.ogg");

        }
        
        
        public void createWorldLight(){
            DirectionalLight sunLight = new DirectionalLight();
            sunLight.setDirection(new Vector3f(0f, 0f, 0f).normalizeLocal());
            creditsRootNode.addLight(sunLight);
        }
    
        public void createPrecipitation(){
            ParticleEmitter pemitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material dropMaterial = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                dropMaterial.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
            pemitter.setMaterial(dropMaterial);
            pemitter.setImagesX(2);
            pemitter.setImagesY(2);
            pemitter.setLowLife(30f);
            pemitter.setHighLife(50f);
            pemitter.setGravity(0,0,0);
            pemitter.setStartColor(ColorRGBA.Black);
            pemitter.setEndColor(ColorRGBA.White);
            pemitter.setStartSize(0.1f);
            pemitter.setEndSize(0.5f);
            pemitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0f,-1f,0f));
            pemitter.getParticleInfluencer().setVelocityVariation(1.0f);
            pemitter.setLocalTranslation(app.getCamera().getLocation().x, 50f, app.getCamera().getLocation().z);
            pemitter.setNumParticles(500);
            pemitter.setParticlesPerSec(20);
            pemitter.setShape(new EmitterBoxShape(new Vector3f(-100f,-50f,-100f),new Vector3f(100f,50f,100f)));
            creditsRootNode.attachChild(pemitter);
            
        }

                   
            
        protected void rotateCamera(float rotationSpeed, float value, float tpf
                            , Vector3f axis){
            
            Quaternion rotate = new Quaternion().fromAngleNormalAxis(rotationSpeed * value * tpf, axis);
            Quaternion q = rotate.mult(app.getCamera().getRotation());
            app.getCamera().setRotation(q);
              
        }
        
        
        protected void moveCamera(){
            Vector3f camDirection = app.getCamera().getDirection();
            Vector3f camLocation = app.getCamera().getLocation();
            
            //bigger valu = slower cam movement & smaller radius
            float moveX = camDirection.x/500;
            float moveZ = camDirection.z/200;
            float camx = camLocation.x;
            float camz = camLocation.z;
            float camy = camLocation.y;
                app.getCamera().setLocation(new Vector3f(camx+moveX, camy, camz+moveZ));

        }
        
        @Override
        public void update(float tpf) {
            //TODO: implement behavior during runtime
            rotateCamera(-0.1f, 1,tpf,Vector3f.UNIT_Y);
            moveCamera();

        }
    
        @Override
        public void cleanup(Application app) {
            System.out.println("CreditsScreen cleanup called.....");
            creditsRootNode.detachAllChildren();

        }    
        
        
        @Override
        protected void onEnable() {
            app.setDisplayStatView(false); app.setDisplayFps(false);
            app.getFlyByCamera().setDragToRotate(true);
            nifty = PlayGame.getNiftyDisplay().getNifty();
            app.getGuiViewPort().addProcessor(PlayGame.getNiftyDisplay());

            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
            nifty.registerSound("btnclick", "Interface/sound/metalClick.ogg");
                
        
            nifty.addScreen("Screen_Credits", new ScreenBuilder("Game Credits"){{
                controller(new mygame.CreditsScreenController());
                defaultFocusElement("Button_Back");
                
                layer(new LayerBuilder("Layer_CreditsText"){{
                    childLayoutCenter();
                    
                    
                    panel(new PanelBuilder("Panel_CreditsTexts"){{
                        height("50%");
                        width("50%"); 
                        alignCenter();
                        valignCenter();
                        backgroundColor("#0f02");
                        childLayoutCenter();                       
                        
                        text(new TextBuilder() {{
                                text("Arvenar - The Lost Traveller");
                                font("Interface/Fonts/Default.fnt");
                                height("20px");
                                width("200px");
                                alignCenter();
                                valignCenter();
                                
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
                            }});  // text1 end  
                             
                             text(new TextBuilder() {{
                                text("A Greeting Cat Production");
                                font("Interface/Fonts/Default.fnt");
                                height("20px");
                                width("200px");
                                alignCenter();
                                valignCenter();
                                onStartScreenEffect(new EffectBuilder("fade") {{
                                startDelay(10000);
                                //length(8000);    
                                effectValue("time", "3000", "value", "0.0");
                                effectValue("time", "5000", "value", "1.0");
                                effectValue("time", "10000", "value", "1.0");
                                effectValue("time", "12000", "value", "0.0");
                                post(false);
                                neverStopRendering(true);
                                }});
                            }}); // text2 end    
                    }}); //panel end
                }}); //layer end  
                
                layer(new LayerBuilder("Layer_CreditsControls"){{
                    childLayoutAbsoluteInside();
                    
                    panel(new PanelBuilder("Panel_Credits_Title"){{
                        height("50px");
                        width("150px"); 
                        x("50px");
                        y("30px");
                        childLayoutVertical();
                            text(new TextBuilder() {{
                                text("Game Credits");
                                font("Interface/Fonts/verdana-48-regular.fnt");
                                height("100%");
                                width("100%");
                                alignLeft();
                                valignTop();
                                
                            }});
                    }}); 
                    
                    
                    panel(new PanelBuilder("Panel_Credits_Back"){{
                        x("50px");
                        y("500px");
                        height("50px");
                        width("200px"); 
                        
                        //backgroundColor("#eff6"); //last digit sets the alpha channel
//                      
                        childLayoutVertical();
                            image(new ImageBuilder() {{
                            filename("Interface/Images/MenuUI/button_0_back.png");
                            alignCenter();
                            valignCenter();
                            height("40px");
                            width("150px");    
                            interactOnClick("backToMainMenu()");
                            
                            onStartHoverEffect(new HoverEffectBuilder("changeImage"){{
                                effectParameter("active", "Interface/Images/MenuUI/button_1_back.png"); neverStopRendering(true);
                                effectParameter("inactive", "Interface/Images/MenuUI/button_0_back.png"); neverStopRendering(true);}});
                            onStartHoverEffect(new HoverEffectBuilder("move"){{effectParameter("mode", "toOffset"); effectParameter("offsetX", "+15");}});
                            onStartHoverEffect(new HoverEffectBuilder("playSound"){{effectParameter("sound", "btnclick");}});
                        }});
                        
//                                                  
                        
                }});
                }});
                }}.build(nifty));
        
                
               nifty.gotoScreen("Screen_Credits");
        

    }
    
    

    @Override
    protected void onDisable() {
        
    }

}