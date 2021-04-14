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
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.niftygui.NiftyJmeDisplay;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.water.SimpleWaterProcessor;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;


/**
 *
 * @author TE332168
 */
public class MainMenuAppState extends BaseAppState implements ScreenController{
    
    
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
      
         
    private Spatial mainScene;
    private Node startRootNode = new Node("Main Menu RootNode");
    private Node startGUINode = new Node("Main Menu GUINode");
    private AudioNode mainMenuThemePlayer, ambSoundNode, plainSoundNode;
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
        
        rootNode.attachChild(startRootNode);
        rootNode.attachChild(startGUINode);
        
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
        inputManager.addMapping("TOGGLE_OPTIONS", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(actionListener, "TOGGLE_OPTIONS");
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/Controls/NiftyGui.xml", "start");
        app.getGuiViewPort().addProcessor(niftyDisplay);
        
        
        //this.app.getFlyByCamera().setEnabled(false);
                
        loadMainScene();
        createWorldLight();
        loadSceneModels();
        createSimpleWater(35, 25, -15f, -1f, -12f);
        createSimpleWater(10, 20, 22f, -0.5f, 5f);
        
        createPrecipitation();
        createFirePlace();
        createWaterStream();
        createWaterFall();
        loadMenuMusic();
        
        loadAmbientSound("Sounds/Ambient/Water/waterstream.ogg", true, true, 0.5f, 23f, -0.5f, -11f);
        loadAmbientSound("Sounds/Ambient/Water/waterfall_01.ogg", true, false, 0.5f, 32f, 2f, -2f);
        loadAmbientSound("Sounds/Ambient/Fire/torchBurning.ogg", true, true, 1.0f, 0f, 0f, 0f);
        
        createMainMenu();
        initMenuControls();
        
    //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }
    
        
        public void loadMainScene(){
            mainScene = this.app.getAssetManager().loadModel("Scenes/MainMenu/mainMenuScene.j3o");
            rootNode.attachChild(mainScene);
            app.getCamera().setLocation(new Vector3f(10f, 2f, 5f));
            app.getCamera().setRotation(new Quaternion().fromAngleAxis(0, Vector3f.UNIT_Y)); //initial camera direction
            
        }
    
        public void loadMenuMusic(){

            mainMenuThemePlayer = new AudioNode(assetManager,"Scenes/MainMenu/RPG_Ambient_2.ogg");
            mainMenuThemePlayer.setLooping(true);
            mainMenuThemePlayer.setPositional(false);
            startRootNode.attachChild(mainMenuThemePlayer);
            mainMenuThemePlayer.play();

        }
        
        public void loadAmbientSound(String file, Boolean looping, Boolean positional, float vol, float posx, float posy, float posz){

            ambSoundNode = new AudioNode(assetManager, file);
            ambSoundNode.setLooping(looping);
            ambSoundNode.setPositional(positional);
            ambSoundNode.setLocalTranslation(posx, posy, posz);
            ambSoundNode.setMaxDistance(5);
            ambSoundNode.setVolume(vol);
            
            startRootNode.attachChild(ambSoundNode);
            ambSoundNode.play();

        }
    
        public void loadSceneModels(){

//            createModel("stump_roundDetailed.glb", 17f, 0.1f, -15f, 0f, 4f);
            createModel("Models/Tree_Pine/snow_pine_tree.obj", "Models/Tree_Pine/pine_snow_full.j3m", 35f, 0.1f, -22, 0f, 0.1f);
            createModel("Models/Tree_Pine/snow_pine_tree.obj", "Models/Tree_Pine/pine_snow_half.j3m", -8f, 0.0f, -8f, 0f, 0.1f);
            createModel("Models/Tree_Pine/snow_pine_tree.obj", "Models/Tree_Pine/pine_snow_none.j3m", -20f, 0.0f, -3f, 0f, 0.1f);
            createModel("Models/Grass/grass.glb", "Models/Grass/grass.j3m", 19f, 0.0f, -18f, 0f, 3f);
            createModel("Models/Grass/grass.glb", "Models/Grass/grass.j3m", -5f, 0.0f, 6f, 0f, 3f);
            createModel("Models/Grass/grass.glb", "Models/Grass/grass.j3m", -5f, 0.0f, -3f, 0f, 3f);
            createModel("Models/Grass/grass_large.glb", "Models/Grass/grass.j3m", 16f, 0.0f, -10f, 0f, 3f);
            createModel("Models/Bush/bush_round.obj", "Models/Bush/bush_round.j3m", 28f, -0.2f, 5f, 0f, 0.1f);
            
            createModel("Models/Bridge/bridge_wood.obj", "Models/Bridge/bridge_wood.j3m", 21f, -0.4f, -13f, 0.9f, 4f);
            createModel("Models/Grass/grass_large.glb", "Models/Grass/grass.j3m", 20f, -0.5f, -15f, 0f, 3f);
            createModel("Models/Grass/grass_large.glb", "Models/Grass/grass.j3m", 22f, -0.2f, -11f, 0f, 3f);
            createModel("Models/Stones/stone_largeA.obj", "", 21.5f, -0.65f, -10.5f, -3f, 2f);
            
            createModel("Models/Campfire/campfire_logs.obj", "Models/Campfire/campfire_logs.j3m", 0f, -0.1f, 0f, 0f, 2f);
            createModel("Models/Campfire/campfire_stones.obj", "Models/Campfire/campfire_stones.j3m", 0f, -0.1f, 0f, 0f, 3f);
            createModel("Models/Floorbed/bed_floor.obj", "Models/Floorbed/bed_floor.j3m", 1f, 0.1f, 3f, 0f, 3f);
            
            createModel("Models/Crate/Crate-04.obj", "Models/Crate/wood_crate.j3m", 28f, 0.0f, -20f, 1f, 2f);
            createModel("Models/Crate/Crate-01.obj", "Models/Crate/wood_crate.j3m", 28f, 1.05f, -20f, 3f, 2f);
            createModel("Models/Crate/Crate-02.obj", "Models/Crate/wood_crate.j3m", 27f, 0.0f, -18f, 6f, 2f);
            createModel("Models/Crate/Crate-03.obj", "Models/Crate/wood_crate.j3m", 27f, 0.0f, -22f, 19f, 2f);
            createModel("Models/Crate/Crate-05.obj", "Models/Crate/wood_crate.j3m", 20f, 0.0f, -8f, 0f, 2f);
            createModel("Models/Crate/Crate-05.obj", "Models/Crate/wood_crate.j3m", 27f, 0.3f, -18f, 0f, 3f);
            
            createModel("Models/Cage/CageBed.j3o", "Models/Cage/cage.j3m", 25f, 0.0f, -24f, 2f, 1f);
//            createModel("Models/Trunk/trunk.j3o", "", 5f, 0f, 3f, 90f, 1f);
            createModel("Models/Barrel/mini_wood_barrel.obj", "Models/Barrel/wood_barrel.j3m", 20f, 0.0f, -19f, 2f, 0.02f);
            createModel("Models/Barrel/mini_wood_barrel.obj", "Models/Barrel/wood_barrel.j3m", 19f, 0.0f, -23f, 2f, 0.02f);
            
            createModel("Models/Stool/DwarfBeerBarrel.j3o", "Models/Stool/stool.j3m", 2f, 0f, -3f, 0f, 0.5f);
            createModel("Models/Tent/tent_detailedOpen.obj", "Models/Tent/tent.j3m", -5f, 0f, 3f, 90f, 6f);
            createModel("Models/Signpost/sign.obj", "Models/Signpost/signpost.j3m", 21f, -0.25f, -8f, -1f, 3f);
            
        }
    
        public void createModel(String modelfile, String custmatfile, float xpos, float ypos, float zpos, float yaw, float scale){
            Spatial model = assetManager.loadModel(modelfile);
                        
            if(custmatfile !=""){   
            model.setMaterial(assetManager.loadMaterial(custmatfile));
            }                    
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
            Material dropMaterial = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                dropMaterial.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
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
            Material dropMaterial = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
            dropMaterial.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
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
        
        public void createWaterStream(){
            ParticleEmitter wstreamEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material waterMaterial = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
            waterMaterial.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
            wstreamEmitter.setMaterial(waterMaterial);
            wstreamEmitter.setImagesX(2);
            wstreamEmitter.setImagesY(2);
            wstreamEmitter.setEndColor(ColorRGBA.Blue);   
            wstreamEmitter.setStartColor(ColorRGBA.Cyan); 
            wstreamEmitter.setLowLife(5f);
            wstreamEmitter.setHighLife(20f);
            wstreamEmitter.setStartSize(1.0f);
            wstreamEmitter.setEndSize(0.1f);
            wstreamEmitter.setGravity(0, 0, 0);
            wstreamEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(-1,-0.1f,-1));
            wstreamEmitter.getParticleInfluencer().setVelocityVariation(0.1f);
            wstreamEmitter.setLocalTranslation(32f, -0.7f, -2f);
            wstreamEmitter.setNumParticles(300);
            wstreamEmitter.setParticlesPerSec(50);
            wstreamEmitter.setShape(new EmitterBoxShape(new Vector3f(-1f,-0.5f,-1f),new Vector3f(1f,0.5f,1f)));
            startRootNode.attachChild(wstreamEmitter);
            
        }
        
        public void createWaterFall(){
            ParticleEmitter waterfallemitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material waterMaterial = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
            waterMaterial.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
            waterfallemitter.setMaterial(waterMaterial);
            waterfallemitter.setImagesX(2);
            waterfallemitter.setImagesY(2);
            waterfallemitter.setEndColor(ColorRGBA.Blue);   
            waterfallemitter.setStartColor(ColorRGBA.Cyan); 
            waterfallemitter.setLowLife(5f);
            waterfallemitter.setHighLife(10f);
            waterfallemitter.setStartSize(1.5f);
            waterfallemitter.setEndSize(0.5f);
            waterfallemitter.setGravity(0, 0, 0);
            waterfallemitter.getParticleInfluencer().setInitialVelocity(new Vector3f(-0.8f,-2f,0.0f));
            waterfallemitter.getParticleInfluencer().setVelocityVariation(0.1f);
            waterfallemitter.setLocalTranslation(35.5f, 10f, -3f);
            waterfallemitter.setNumParticles(300);
            waterfallemitter.setParticlesPerSec(50);
            waterfallemitter.setShape(new EmitterBoxShape(new Vector3f(-1f,-1f,-1f),new Vector3f(1f,1f,1f)));
            startRootNode.attachChild(waterfallemitter);
            
        }
        
        public void createSimpleWater(float width, float depth, float posx, float posy, float posz){
            
            SimpleWaterProcessor waterCreator = new SimpleWaterProcessor(assetManager);
                                 waterCreator.setReflectionScene(mainScene);
            
            Vector3f waterLocation = new Vector3f(0,-6,0);
            
            waterCreator.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
            viewPort.addProcessor(waterCreator);
            waterCreator.setWaterDepth(40);
            waterCreator.setDistortionScale(0.05f);
            waterCreator.setWaveSpeed(0.05f);
                                    
            Geometry watergeom = waterCreator.createWaterGeometry(width, depth);
                     watergeom.setLocalTranslation(posx, posy, posz);
                     watergeom.setShadowMode(RenderQueue.ShadowMode.Receive);
                     watergeom.setMaterial(waterCreator.getMaterial());
                     startRootNode.attachChild(watergeom);
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
            
            //float moveX = camDirection.x/1000;
            float moveX = camDirection.x/800;
            float moveZ = camDirection.z/300;
            float camx = camLocation.x;
            float camz = camLocation.z;
            float camy = camLocation.y;
                app.getCamera().setLocation(new Vector3f(camx+moveX, camy, camz+moveZ));

        }
        
        public void leaveAutoPlay(){
            
            mainMenuThemePlayer.stop();
            rotateCamera(0f, 1,0,Vector3f.UNIT_Y);
            app.getCamera().setLocation(new Vector3f(2f, 2f, 2f));
            
        }
                        
             
        public void createMainMenu(){
            app.setDisplayStatView(false); app.setDisplayFps(false);

            createMenuText("Start New Game", 50, screenHeight-50);
            createMenuText("Game Options", 50, screenHeight-80);
            createMenuText("Credits", 50, screenHeight-110);
            createMenuText("Exit Game", 50, screenHeight-140);
 
        }
             
        public void createMenuText(String name, float posx, float posy){
            menuItemText = new BitmapText(assetManager.loadFont("Interface/Fonts/Antiqua.fnt"), false);
            menuItemText.setText(name);
            menuItemText.setSize(24);
            menuItemText.setLocalTranslation(posx, posy, 0);
            menuItemText.setColor(ColorRGBA.White);
            startGUINode.attachChild(menuItemText);
        }
        
        
        public void initMenuControls(){
        
            inputManager.addMapping("MBLeft", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
            inputManager.addMapping("MenuUp", new KeyTrigger(KeyInput.KEY_UP));
            inputManager.addMapping("MenuDown", new KeyTrigger(KeyInput.KEY_DOWN));
            inputManager.addMapping("PlayGame", new KeyTrigger(KeyInput.KEY_BACK));
            

            inputManager.addListener(actionListener, "MBLeft", "MenuUp", "MenuDown", "PlayGame");
        
        
        }
    
        private final ActionListener actionListener = new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                switch (name) {
                    
                    case "MBLeft": createMenuText("CamDir: "+app.getCamera().getLocation(), 50, screenHeight-200); break;
                    case "PlayGame": leaveAutoPlay(); break;
//                    case "TOGGLE_OPTIONS": if (isPressed){
//                                            nifty.getCurrentScreen().
//                                            getScreenController().toggleOptionsMenu();
//                                            }
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
        startRootNode.detachAllChildren();
        startGUINode.detachAllChildren();
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

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        
    }

    @Override
    public void onStartScreen() {
        nifty.gotoScreen("start");
        nifty.getCurrentScreen().findElementByName("options");
    }

    @Override
    public void onEndScreen() {
        
    }
    
    
}
