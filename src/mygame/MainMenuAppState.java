/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.audio.AudioNode;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

/**
 *
 * @author TE332168
 */
public class MainMenuAppState extends BaseAppState{
    
    
    private SimpleApplication app;
    
    private Spatial mainScene;
    private Node startRootNode = new Node("Main Menu RootNode");
    private Node startGUINode = new Node("Main Menu GUINode");
    private AudioNode mainMenuThemePlayer;
    float screenHeight, screenWidth;
        
    
    @Override
    public void initialize(Application app) {
        
        this.app = (SimpleApplication) app;
        screenHeight = this.app.getCamera().getHeight();
        screenWidth = this.app.getCamera().getWidth();
        
        this.app.getRootNode().attachChild(startRootNode);
        this.app.getGuiNode().attachChild(startGUINode);
        //this.app.getFlyByCamera().setEnabled(false);
        
        
        loadMainScene();
        createWorldLight();
        loadSceneModels();
        createPrecipitation();
        createFirePlace();
        loadMenuMusic();
        
        createMainMenu();
        initMenuControls();
        
        
    //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }
    
        
        public void loadMainScene(){
            mainScene = this.app.getAssetManager().loadModel("Scenes/MainMenu/mainMenuScene.j3o");
            this.app.getRootNode().attachChild(mainScene);
            this.app.getCamera().setLocation(new Vector3f(10f, 3f, 5f));
            this.app.getCamera().setRotation(new Quaternion().fromAngleAxis(0, Vector3f.UNIT_Y)); //initial camera direction
            
        }
    
        public void loadMenuMusic(){

            mainMenuThemePlayer = new AudioNode(this.app.getAssetManager(),"Scenes/MainMenu/RPG_Ambient_2.ogg");
            mainMenuThemePlayer.setLooping(true);
            mainMenuThemePlayer.setPositional(false);
            startRootNode.attachChild(mainMenuThemePlayer);
            mainMenuThemePlayer.play();

        }
    
        public void loadSceneModels(){

            createModel("stump_roundDetailed.glb", 17f, 0.1f, -15f, 0f, 4f);
            createModel("tree_pineTallD_detailed.glb", 30f, 0.1f, -25, 0f, 8f);
            createModel("grass.glb", 20f, 0.3f, -15f, 0f, 3f);
            createModel("grass_large.glb", 17f, 0.3f, -10f, 0f, 3f);
            createModel("campfire_logs.glb", 0f, 0.1f, 0, 0f, 2f);
            createModel("campfire_stones.glb", -0.2f, 0.1f, 0, 0f, 3f);
            createModel("bed_floor.glb", 1f, 0.1f, 3f, 0f, 3f);
            createModel("Crate-04.j3o", 25f, 0.1f, -15f, 1f, 2f);
            createModel("mini_wood_barrel.obj", 26f, 0.1f, -14f, 2f, 0.02f);
            createModel("tree_pineDefaultA.glb", -4f, 0.0f, -5f, 0f, 6f);
            createModel("tent_detailedOpen.glb", -5f, 0.3f, 3f, 80f, 6f);
            createModel("sign.glb", 25f, 0.1f, -10f, 90f, 5f);
            

        }
    
        public void createModel(String modelName, float xpos, float ypos, float zpos, float yaw, float scale){
            Spatial model = this.app.getAssetManager().loadModel("Models/"+modelName);
            
            model.setMaterial((Material) this.app.getAssetManager().loadMaterial("Materials/wood.j3m"));
            
            model.setLocalTranslation(xpos, ypos, zpos);
            model.rotate(0, yaw, 0);
            model.setLocalScale(scale);
            startRootNode.attachChild(model);
        }
    
        public void createWorldLight(){
            DirectionalLight sunLight = new DirectionalLight();
            sunLight.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
            startRootNode.addLight(sunLight);
        }
    
        public void createPrecipitation(){
            ParticleEmitter pemitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material dropMaterial = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
                dropMaterial.setTexture("Texture", this.app.getAssetManager().loadTexture("Effects/Explosion/flame.png"));
            pemitter.setMaterial(dropMaterial);
            pemitter.setImagesX(2);
            pemitter.setImagesY(2);
            pemitter.setLowLife(20f);
            pemitter.setHighLife(30f);
            pemitter.setStartSize(0.5f);
            pemitter.setEndSize(1.0f);
            pemitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0,-20,0));
            pemitter.getParticleInfluencer().setVelocityVariation(0.5f);
            pemitter.setLocalTranslation(25f, 50f, 15f);
            pemitter.setNumParticles(500);
            pemitter.setParticlesPerSec(20);
            pemitter.setShape(new EmitterSphereShape(Vector3f.ZERO,100f));
            startRootNode.attachChild(pemitter);
            
        }

        public void createFirePlace(){
            ParticleEmitter pemitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material dropMaterial = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
            dropMaterial.setTexture("Texture", this.app.getAssetManager().loadTexture("Effects/Explosion/flame.png"));
            pemitter.setMaterial(dropMaterial);
            pemitter.setImagesX(2);
            pemitter.setImagesY(2);
            pemitter.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
            pemitter.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
            pemitter.setLowLife(0.5f);
            pemitter.setHighLife(1.5f);
            pemitter.setStartSize(0.4f);
            pemitter.setEndSize(0.1f);
            pemitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0,1,0));
            pemitter.getParticleInfluencer().setVelocityVariation(0.3f);
            pemitter.setLocalTranslation(0f, 0.3f, 0f);
            pemitter.setNumParticles(200);
            pemitter.setParticlesPerSec(20);
            startRootNode.attachChild(pemitter);
            
        }
            
            
        protected void rotateCamera(float value, float tpf
                            , Vector3f axis){
            float rotationSpeed = -0.1f;
            Quaternion rotate = new Quaternion().fromAngleNormalAxis(rotationSpeed * value * tpf, axis);
            Quaternion q = rotate.mult(this.app.getCamera().getRotation());
            this.app.getCamera().setRotation(q);

              if (this.app.getCamera().getDirection().z == -0.9f){
                  rotationSpeed = -0.05f;
              }

              if (this.app.getCamera().getDirection().z == -0.2f){
                  rotationSpeed = -0.1f;
              }


        }
        
        
        protected void moveCamera(){
            float moveSpeed = 0.001f;
            float camx = this.app.getCamera().getLocation().x;
            float camz = this.app.getCamera().getLocation().z;
            float camy = this.app.getCamera().getLocation().y;
              this.app.getCamera().setLocation(new Vector3f(camx*moveSpeed, camy, camz*moveSpeed));

        }
             
             
             
        public void createMainMenu(){
            this.app.setDisplayStatView(false); this.app.setDisplayFps(false);

            createMenuText("Start New Game", 50, screenHeight-50);
            createMenuText("Game Options", 50, screenHeight-80);
            createMenuText("Credits", 50, screenHeight-110);
            createMenuText("Exit Game", 50, screenHeight-140);

            createMenuText("CamDir: "+this.app.getCamera().getDirection(), 50, screenHeight-200);
        }
             
        public void createMenuText(String name, float posx, float posy){
            BitmapText menuItemText = new BitmapText(this.app.getAssetManager().loadFont("Interface/Fonts/Antiqua.fnt"), false);
            menuItemText.setText(name);
            menuItemText.setSize(24);
            menuItemText.setLocalTranslation(posx, posy, 0);
            menuItemText.setColor(ColorRGBA.White);

            startGUINode.attachChild(menuItemText);
        }
        
        
        public void initMenuControls(){
        
            this.app.getInputManager().addMapping("MBLeft", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
            this.app.getInputManager().addMapping("MenuUp", new KeyTrigger(KeyInput.KEY_UP));
            this.app.getInputManager().addMapping("MenuDown", new KeyTrigger(KeyInput.KEY_DOWN));

            this.app.getInputManager().addListener(actionListener, "MBLeft", "MenuUp", "MenuDown");
        
        
        }
    
        private final ActionListener actionListener = new ActionListener() {
            @Override
            public void onAction(String name, boolean keyPressed, float tpf) {
                            if (name.equals("MBLeft")) {

                    createMenuText("CamDir: "+app.getCamera().getDirection(), 100, 200);

                }
            }
        };
    
        
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        rotateCamera(1,tpf,Vector3f.UNIT_Y);
        //moveCamera();
       
    }
    
    @Override
    public void cleanup(Application app) {
        
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }    
        
        
    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
    
    
}
