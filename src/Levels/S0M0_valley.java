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
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.input.InputManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.water.SimpleWaterProcessor;
import mygame.AudioManager;
import mygame.ModelManager;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class S0M0_valley extends BaseAppState {

    public Spatial level_S0M0;
    private SimpleApplication app;
    
    private Node              rootNode;
    private AssetManager      assetManager;
    private AppStateManager   stateManager;
    private InputManager      inputManager;
    private RenderManager     renderManager;
    
    private ModelManager modelManager; 
    
    private ViewPort          viewPort;
    private Camera camera;
        
        
    ParticleEmitter particle1;
    SimpleWaterProcessor waterCreator;
    //for Post process water effectprocessor
    
    private DirectionalLight sun;
    private Vector3f initPlayerPosition = new Vector3f(0f,5f,-50f);
        
    @Override
    protected void initialize(Application app) {
    this.app = (SimpleApplication) app;   
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
        this.camera       = this.app.getCamera();
        
        modelManager = new ModelManager(); 
                
        loadLevel();
        createSun();
        loadSceneModels();
                
        createPrecipitation();
        createFirePlace();
        createWaterStream();
        createWaterFall();
        loadAmbientSound();
        createSimpleWater(100, 100, -50f, 2.5f, -30f);
        createSimpleWater(50, 120, 55f, 2.2f, 12f);
        
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
        AudioManager.musicPlayer.stop();
        AudioManager.soundPlayer.stop();
        

    
        //Called when the state was previously enabled but is now disabled         
        //either because setEnabled(false) was called or the state is being         
        //cleaned up.    
    }
    
    @Override
    public void update(float tpf) {

    }
    
     public void loadSceneModels(){
            
            
            modelManager.createModel("Models/Vegetation/Trees/Pine/Pine_Snow_Full.obj", "", 90f, 0.5f, -75f, 2f, 0f, 3f);
            modelManager.createModel("Models/Vegetation/Trees/Pine/Pine_Snow_Half.obj", "", -55f, 0.5f, 22f, 2f, 0f, 3f);
            modelManager.createModel("Models/Vegetation/Trees/Pine/Pine_Snow_None.obj", "", -40f, 0.5f, -3f, 2f, 0f, 2f);
                        
            modelManager.createModel("Models/Structures/Jetty02/Jetty02_blend.obj", "", -2f, 2f, -35f, -1.5f, 0f, 2f);
            modelManager.createModel("Models/Structures/Bridge/Wooden_Bridge/WoodenBridge_blend.obj", "", 60f, 4.3f, -40f, -1f, 0f, 7f);
            modelManager.createModel("Models/Vegetation/Grasses/BeachGrass/BeachGrass_blend.obj", "", 20f, 3f, -15f, 0f, 0f, 5f);
            modelManager.createModel("Models/Vegetation/Grasses/BeachGrass/BeachGrass_blend.obj", "", 22f, 3f, -11f, 1f, 0f, 6f);
            modelManager.createModel("Models/Vegetation/Grasses/BeachGrass/BeachGrass_blend.obj", "", 19f, 3f, -18f, 2f, 0f, 4f);
            modelManager.createModel("Models/Naturals/Stones/stone_largeA.obj", "", 56f, 1.5f, -44f, -3f, 0f, 2f);
            
            modelManager.createModel("Models/Others/Campfire/campfire_logs.j3o", "", -1.5f, 3f, 11f, 0f, 0f, 6f);
            modelManager.createModel("Models/Others/Campfire/campfire_stones.j3o", "", -1.5f, 3f, 11f, 0f, 0f, 6f);
            modelManager.createModel("Models/Others/Floorbed/bed_floor.obj", "Models/Others/Floorbed/bed_floor.j3m", 8f, 3f, 14f, 1f, 0f, 6f);
            
            modelManager.createModel("Models/Others/Crate/Crate04_blend.obj", "", 70f, 3f, -60f, 1f, 0f, 5f);
            modelManager.createModel("Models/Others/Crate/Crate-01_blend.obj", "", 80f, 3f, -55f, 3f, 0f, 5f);
            modelManager.createModel("Models/Others/Crate/Crate02_blend.obj", "", 70f, 3f, -75f, 6f, 0f, 5f);
            modelManager.createModel("Models/Others/Crate/Crate03_blend.obj", "", 85f, 3f, -65f, 19f, 0f, 5f);
            modelManager.createModel("Models/Others/Crate/Crate05_blend.obj", "", 20f, 3f, -8f, 0f, 0f, 5f);
            modelManager.createModel("Models/Others/Crate/Crate05_blend.obj", "", 60f, 3f, -30f, 0f, 0f, 4f);
            
            modelManager.createModel("Models/Others/Cage/CageBed.j3o", "", 65f, 3f, -80f, 2f, 0f, 2f);

            modelManager.createModel("Models/Others/Barrel/mini_wood_barrel.obj", "Models/Others/Barrel/wood_barrel.j3m", 57f, 3f, -61f, 2f, 0f, 0.05f);
            modelManager.createModel("Models/Others/Barrel/mini_wood_barrel.obj", "Models/Others/Barrel/wood_barrel.j3m", 60f, 3f, -70f, 2f, 0f, 0.05f);
            
            modelManager.createModel("Models/Others/Stool/DwarfBeerBarrel.j3o", "Models/Others/Stool/stool.j3m", 2f, 3f, -3f, 0f, 0f, 1f);
            modelManager.createModel("Models/Others/Tent/tent_detailedOpen.j3o", "", -20f, 3f, 10f, 90f, 0f, 18f);
            modelManager.createModel("Models/Others/Signpost/SignPost_blend.obj", "", 57f, 3f, -25f, -1f, 0f, 6f);
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
            pemitter.setStartSize(0.1f);
            pemitter.setEndSize(0.5f);
            pemitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0,-10,0));
            pemitter.getParticleInfluencer().setVelocityVariation(0.5f);
            pemitter.setLocalTranslation(0f, 50f, 0f);
            pemitter.setNumParticles(500);
            pemitter.setParticlesPerSec(20);
            //pemitter.setShape(new EmitterSphereShape(Vector3f.ZERO,100f));
            pemitter.setShape(new EmitterBoxShape(new Vector3f(-50f,-50f,-50f),new Vector3f(50f,50f,50f)));
            rootNode.attachChild(pemitter);
            
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
            pemitter.setLocalTranslation(-1.5f, 3f, 11f);
            pemitter.setNumParticles(200);
            pemitter.setParticlesPerSec(20);
            rootNode.attachChild(pemitter);
            
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
            wstreamEmitter.setHighLife(40f);
            wstreamEmitter.setStartSize(1.0f);
            wstreamEmitter.setEndSize(0.1f);
            wstreamEmitter.setGravity(0, 0, 0);
            wstreamEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(-1f,-0.1f,-1));
            wstreamEmitter.getParticleInfluencer().setVelocityVariation(0.1f);
            wstreamEmitter.setLocalTranslation(90f, 3f, -5f);
            wstreamEmitter.setNumParticles(300);
            wstreamEmitter.setParticlesPerSec(100);
            wstreamEmitter.setShape(new EmitterBoxShape(new Vector3f(-1f,-1f,-1f),new Vector3f(1f,1f,1f)));
            rootNode.attachChild(wstreamEmitter);
            
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
            waterfallemitter.setLocalTranslation(102f, 20f, -5f);
            waterfallemitter.setNumParticles(300);
            waterfallemitter.setParticlesPerSec(50);
            waterfallemitter.setShape(new EmitterBoxShape(new Vector3f(-1f,-1f,-1f),new Vector3f(1f,1f,1f)));
            rootNode.attachChild(waterfallemitter);
            
        }
        
        public void createSimpleWater(float width, float depth, float posx, float posy, float posz){
            
                                waterCreator = new SimpleWaterProcessor(assetManager);
                                waterCreator.setReflectionScene(level_S0M0);
            
            Vector3f waterLocation = new Vector3f(0,3,0);
            
            waterCreator.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
            viewPort.addProcessor(waterCreator);
            waterCreator.setWaterDepth(40);
            waterCreator.setDistortionScale(0.05f);
            waterCreator.setWaveSpeed(0.05f);
                                    
            Geometry watergeom = waterCreator.createWaterGeometry(width, depth);
                     watergeom.setLocalTranslation(posx, posy, posz);
                     watergeom.setShadowMode(RenderQueue.ShadowMode.Receive);
                     watergeom.setMaterial(waterCreator.getMaterial());
                     rootNode.attachChild(watergeom);
    }                  
         
        
        public void createSun(){
            
            sun = new DirectionalLight();
            //sun.setColor(ColorRGBA.White);
            sun.setDirection(new Vector3f(6.0f, -1.5f, -4.0f).normalizeLocal());
            this.app.getRootNode().addLight(sun);
            
        }
        
        public void loadAmbientSound(){

            AudioManager.playSound("Sounds/Ambient/Water/waterstream.ogg", false, true, true, 1000f, 0.5f, 23f, -0.5f, -11f);
            AudioManager.playSound("Sounds/Ambient/Water/waterfall_01.ogg", false, false, true, 1000f, 0.5f, 32f, 2f, -2f); 
            AudioManager.playSound("Sounds/Ambient/Fire/torchBurning.ogg", false, true, true, 1000f, 1.0f, 0f, 0f, 0f); 
        }
        
        public void loadLevel() {
        
        level_S0M0 = this.app.getAssetManager().loadModel("Scenes/S0_Snowenar/S0M0_walley.j3o");
        level_S0M0.setLocalTranslation(0f, 3f, 0f);
        this.app.getRootNode().attachChild(level_S0M0);
                
        }
       
        
        public Spatial getLevel() {
            return level_S0M0;
        }

        public Vector3f getInitPlayerPosition() {
            return initPlayerPosition;        
        }

        
}
