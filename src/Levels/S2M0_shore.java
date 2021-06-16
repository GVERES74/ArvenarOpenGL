package Levels;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.input.InputManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.water.WaterFilter;
import mygame.GameAppState;
import mygame.PlayGame;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class S2M0_shore extends BaseAppState {

    public Spatial level_S2M0;
    private SimpleApplication app;
    
    private Node              rootNode;
    private AssetManager      assetManager;
    private AppStateManager   stateManager;
    private InputManager      inputManager;
    private RenderManager     renderManager;
    
    private ViewPort          viewPort;
    private Camera camera;
    
    private AudioNode soundPlayer;
    private AudioNode playsoundonce;
    private AudioRenderer audioRenderer;
    
    private BulletAppState bulletAppState;
        
    private RigidBodyControl modelRigidBody;
    
    ParticleEmitter particle1;
    //for Post process water effectprocessor
    private FilterPostProcessor ppFilter;
    private WaterFilter ppWaterFilter;
    private DirectionalLight sun;
    
    private float ppInitialWaterHeight = 0.5f; // choose a value for your scene
    private float waveTime = 0.0f;
    private float ppWaterHeight = 0.0f;
            
    private Vector3f initPlayerPosition = new Vector3f(0f,15f,250f);
    
    @Override
    protected void initialize(Application app) {
    this.app = (SimpleApplication) app;   
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
        this.camera       = this.app.getCamera();
        
        bulletAppState = new BulletAppState();
                
        app.getStateManager().attach(bulletAppState);
    
    //It is technically safe to do all initialization and cleanup in the         
        //onEnable()/onDisable() methods. Choosing to use initialize() and         
        //cleanup() for this is a matter of performance specifics for the         
        //implementor.        
        //TODO: initialize your AppState, e.g. attach spatials to rootNode    
        
        loadSceneModels();
        addScenePhysics();
        sunShine();
        createAdvancedWater();
        loadAudio();
        createPrecipitationParticleEffects();
        createFirePlace();
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
    

    
        //Called when the state is fully enabled, ie: is attached and         
        //isEnabled() is true or when the setEnabled() status changes after the         
        //state is attached.    
    }
    
    @Override
    protected void onDisable() {
    

    
        //Called when the state was previously enabled but is now disabled         
        //either because setEnabled(false) was called or the state is being         
        //cleaned up.    
    }
    
    @Override
    public void update(float tpf) {
        
        updateAdvancedWater(tpf);
        
        if (camera.getLocation().x < -600f){
                 PlayGame.musicPlayer.play();
        
        }
//        particle1.setLocalTranslation(
//                new Vector3f(
//                        maingameappstate.firstPersonPlayer.getPhysicsLocation().x, 
//                        50, 
//                        maingameappstate.firstPersonPlayer.getPhysicsLocation().z)
//        );
        //TODO: implement behavior during runtime    
    }
    
     public void loadSceneModels(){
            //boat and ship
            createModel("Models/Boat/boat_crashed.j3o", "", 300f, 0f, 900f, -1f, 0f, 30f);
            createModel("Models/Boat/boat_small.obj", "", 30f, 2f, 320f, 0f, 0.1f, 1f);
            //schacks, huts, jettys
            createModel("Models/Jetty02/Tropical_Jetty02.j3o", "Models/Jetty02/Tropical_Jetty02.j3m", -600f, 1f, 400f, 0f, 0f, 2f);
            createModel("Models/JungleHut01/JungleHut01.j3o", "Models/JungleHut01/JungleHut01.j3m", -650f, 15f, 250f, 1f, 0f, 2f);
            createModel("Models/JungleHut01/AfricaDock01.j3o", "Models/JungleHut01/AfricaDock01.j3m", -640f, 11f, 256f, -0.5f, 0f, 2f);
            createModel("Models/JungleHut02/JungleHut02.j3o", "Models/JungleHut02/JungleHut02.j3m", -100f, 13f, -600f, 0f, 0f, 2f);
            createModel("Models/JungleHut01/AfricaDock01.j3o", "Models/JungleHut01/AfricaDock01.j3m", -97f, 11f, -620f, 1.5f, 0f, 2f);
            
            //trees
            createModel("Models/Tree/StrangeCoconutTreeYoung.j3o", "", -650f, 5f, 220f, 1f, 0f, 15f);
            createModel("Models/Tree/StrangePalm.j3o", "", -650f, 5f, 200f, 1f, 0f, 15f);
            createModel("Models/Tree/StrangePalmOld.j3o", "", -650f, 5f, 270f, 1f, 0f, 1f);
            createModel("Models/Tree/StrangePalmCurved.j3o", "", -630f, 5f, 300f, 1f, 0f, 2f);
            createModel("Models/Tree/StrangeCoconutTreeCurved.j3o", "", -630f, 5f, 320f, 1f, 0f, 5f);
            createModel("Models/Tree/Palm/Palm01.j3o", "Models/Tree/Palm/Palm01.j3m", 0f, 5f, 250f, 1f, 0f, 5f);
            createModel("Models/Tree/Palm/Palm02.j3o", "Models/Tree/Palm/Palm02.j3m", 400f, 3f, 250f, 1f, 0f, 5f);
            createModel("Models/Tree/Palm/Palm03.j3o", "Models/Tree/Palm/Palm03.j3m", -630f, 5f, 400f, 1f, 0f, 5f);
            createModel("Models/Tree/Cecropia/CecropiaTree01.j3o", "Models/Tree/Cecropia/Cecropia.j3m", -120f, 10f, -620f, 1f, 0f, 5f);
            
            //buildings
            
            //campfire
            createModel("Models/Campfire/campfire_logs.obj", "Models/Campfire/campfire_logs.j3m", -620f, 7f, 250f, 1f, 0f, 5f);
            createModel("Models/Campfire/campfire_stones.obj", "Models/Campfire/campfire_stones.j3m", -620f, 7f, 250f, 1f, 0, 5f);
            //crates and barrels            
            createModel("Models/Crate/Crate-04.obj", "Models/Crate/wood_crate.j3m", -640f, 7f, 250f, 1f, 0f, 5f);
            createModel("Models/Crate/Crate-01.obj", "Models/Crate/wood_crate.j3m", -640f, 11f, 237f, 0f, 0f, 5f);
            createModel("Models/Crate/Crate-02.obj", "Models/Crate/wood_crate.j3m", -640f, 7f, 275f, 1f, 0f, 5f);
            createModel("Models/Crate/Crate-03.obj", "Models/Crate/wood_crate.j3m", -640f, 7f, 280f, 1f, 0f, 5f);
            createModel("Models/Crate/Crate-05.obj", "Models/Crate/wood_crate.j3m", -595f, 4f, 401f, -1f, 0f, 5f);
            createModel("Models/Crate/Crate-05.obj", "Models/Crate/wood_crate.j3m", -620f, 3f, 390f, 1f, 0f, 6f);
            createModel("Models/Crate/Crate_jetty_01.j3o", "Models/Crate/Crate_jetty_01.j3m", -595f, 4.1f, 398f, 0f, 0f, 2f);
            
            
            createModel("Models/Cage/CageBed.j3o", "Models/Cage/cage.j3m", -650f, 7f, 300f, -2f, 0f, 3f);
                        
            createModel("Models/Barrel/mini_wood_barrel.obj", "Models/Barrel/wood_barrel.j3m", -645f, 7f, 237f, 1f, 0f, 0.05f);
            createModel("Models/Barrel/mini_wood_barrel.obj", "Models/Barrel/wood_barrel.j3m", -640f, 7f, 237f, 1f, 0f, 0.05f);
        }   
    
        public void createModel(String modelfile, String custmatfile, float xpos, float ypos, float zpos, float yaw, float pitch, float scale){
            Spatial model = assetManager.loadModel(modelfile);
                        
            if(custmatfile !=""){   
            model.setMaterial(assetManager.loadMaterial(custmatfile));
            }                    
            model.setLocalTranslation(xpos, ypos, zpos);
            model.rotate(pitch, yaw, 0);
            model.setLocalScale(scale);
            
            CollisionShape sceneModel = CollisionShapeFactory.createMeshShape(model);
            modelRigidBody = new RigidBodyControl(sceneModel,0);
            model.addControl(modelRigidBody);
            
            bulletAppState.getPhysicsSpace().add(modelRigidBody);
            rootNode.attachChild(model);
        }
    
    public void createPrecipitationParticleEffects(){
        
            particle1 = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material precipitationMaterial = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
            precipitationMaterial.setTexture("Texture", this.app.getAssetManager().loadTexture("Effects/Explosion/flame.png"));
            particle1.setMaterial(precipitationMaterial);
            particle1.setImagesX(2);
            particle1.setImagesY(2);
            particle1.getParticleInfluencer().setInitialVelocity(new Vector3f(-1,-1,0));
            particle1.getParticleInfluencer().setVelocityVariation(1.0f);
            particle1.setLocalTranslation(-650, 50, 250);
            particle1.setStartSize(0.5f);
            particle1.setEndSize(1.0f);
            //particle1.setGravity(1,1,1);
            particle1.setLowLife(10f);
            particle1.setHighLife(30f);
            particle1.setNumParticles(200);
            particle1.setParticlesPerSec(10);

            particle1.setShape(new EmitterBoxShape(new Vector3f(-50f,-50f,-50f),new Vector3f(50f,50f,50f)));
           
            this.app.getRootNode().attachChild(particle1);

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
            pemitter.setLowLife(1.5f);
            pemitter.setHighLife(4.5f);
            pemitter.setStartSize(0.5f);
            pemitter.setEndSize(0.1f);
            pemitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0,1,0));
            pemitter.getParticleInfluencer().setVelocityVariation(0.3f);
            pemitter.setLocalTranslation(-620f, 7.5f, 250f);
            
            pemitter.setNumParticles(200);
            pemitter.setParticlesPerSec(20);
            this.app.getRootNode().attachChild(pemitter);
            
        }
        
        public void sunShine(){
            
            sun = new DirectionalLight();
            //sun.setColor(ColorRGBA.White);
            sun.setDirection(new Vector3f(6.0f, -1.5f, -4.0f).normalizeLocal());
            this.app.getRootNode().addLight(sun);
            
        }
        
        
        public void createAdvancedWater(){
            //post process water
        ppFilter = new FilterPostProcessor(assetManager);
        ppWaterFilter = new WaterFilter(rootNode, sun.getDirection());
        ppWaterFilter.setSunScale(1f);
        ppWaterFilter.setWaterHeight(ppInitialWaterHeight);
        ppWaterFilter.setWindDirection(new Vector2f(0.0f,1.0f));     
        ppWaterFilter.setNormalScale(0.5f);
        ppWaterFilter.setFoamHardness(0.5f);
        ppWaterFilter.setFoamExistence(new Vector3f(0.5f,1f,1f));
        ppFilter.addFilter(ppWaterFilter);
        viewPort.addProcessor(ppFilter);
        
        }
        
        public void updateAdvancedWater(float tpf){
            
            waveTime += tpf;
            ppWaterHeight = (float) Math.cos(((waveTime * 0.6f) % FastMath.TWO_PI)) * 1.0f;
            ppWaterFilter.setWaterHeight(ppInitialWaterHeight + ppWaterHeight);
        }
        
        private void loadAudio(){
            
            PlayGame.loadMusic("Music/Soundtracks/Peaceful_Place.ogg", false, true);
            
            playSound("Sounds/Ambient/Animals/ocean_seagull_mono.ogg", false, true, true, 1000f, 3, 0f, 5f, 500f);
            playSound("Sounds/Ambient/Fire/torchBurning.ogg", false, true, true, 1000f, 2, -620f, 7f, 250f);
            playSound("Sounds/Ambient/Environment/JungleAmbient01.ogg", false, true, true, 1000f, 3, -100f, 5f, -630f);
            
        }
        
        //playsound must be instantiated (more sounds must be played, needs always a new AudioNode)
        public void playSound(String filepath, boolean directional, boolean positional, boolean looping, float maxdist, float volume, float xpos, float ypos, float zpos){
        soundPlayer = new AudioNode(assetManager, filepath, AudioData.DataType.Stream);
        soundPlayer.setDirectional(directional);
        soundPlayer.setPositional(positional);
        soundPlayer.setLocalTranslation(xpos, ypos, zpos);
        soundPlayer.setLooping(looping);
        soundPlayer.setVolume(volume);
        soundPlayer.setRefDistance(5f);
        soundPlayer.setMaxDistance(maxdist);
        rootNode.attachChild(soundPlayer);
        soundPlayer.play();
        //audioRenderer.playSource(soundPlayer);
        }
        
        public void playSoundInstance(String file){
            playsoundonce = new AudioNode(assetManager, file, AudioData.DataType.Buffer);
            playsoundonce.setPositional(false);
            playsoundonce.setLooping(false);
            playsoundonce.setVolume(0.5f);
            rootNode.attachChild(playsoundonce);
            playsoundonce.playInstance();
            //audioRenderer.playSourceInstance(playsoundonce);
        }

    public Spatial getLevel() {
        return level_S2M0;
    }
    
    public void addScenePhysics(){
         
            bulletAppState.getPhysicsSpace().add(app.getStateManager().getState(GameAppState.class).levelRigidBody);
            bulletAppState.getPhysicsSpace().add(app.getStateManager().getState(GameAppState.class).firstPersonPlayer);
    }

    public Vector3f getInitPlayerPosition() {
        return initPlayerPosition;
    }
            
}
