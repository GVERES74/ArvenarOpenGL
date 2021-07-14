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
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.input.InputManager;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
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
import mygame.AudioManager;
import mygame.GameAppState;
import mygame.ModelManager;

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
    
    
    private BulletAppState bulletAppState;
    private ModelManager modelManager;    
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
        modelManager = new ModelManager();
                
        app.getStateManager().attach(bulletAppState);
        app.setPauseOnLostFocus(true);
    
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
        createGraveParticleEffects();
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
    
        AudioManager.musicPlayer.stop();
    
        //Called when the state was previously enabled but is now disabled         
        //either because setEnabled(false) was called or the state is being         
        //cleaned up.    
    }
    
    @Override
    public void update(float tpf) {
        
        updateAdvancedWater(tpf);
        
        if ((camera.getLocation().x < -600f) && (camera.getLocation().z > 200f)){
                 AudioManager.musicPlayer.play();
        }
        
        if ((camera.getLocation().x < -650f) && (camera.getLocation().z < -630f)){
                 AudioManager.musicPlayer.stop();
                 AudioManager.loadMusic("Music/Soundtracks/Audience.ogg", true, false);
               
        }

        //TODO: implement behavior during runtime    
    }
    
     public void loadSceneModels(){
    //boat and ship
            createModel("Models/Vehicles/Ship/Ship.obj", "", 150f, 6f, 900f, 0f, 0f, 10f);
            modelManager.createModel("Models/Vehicles/Boat/RowBoat2/Boat2_blend.obj", "", 30f, 2f, 320f, 3f, -  0.1f, 0.2f);
            
            createModel("Models/NPC/Mother_blend.obj", "", -675, 11f, -640f, 2f, 0f, 4f);
            
            
    //schacks, huts, jettys
            //createModel("Models/Structures/Jetty02/Jetty02_blend.obj", "", -600f, 1f, 400f, 0f, 0f, 2f);
            createModel("Models/Structures/Bridge/Turn_Bridge/Turn_Bridge_blend.obj", "", -590f, 3f, 400f, 2f, 0f, 1f);
            createModel("Models/Structures/JungleHut01/JungleHut01_blend.obj", "", -650f, 15f, 250f, 1f, 0f, 2f);
            createModel("Models/Structures/AfricaDock01/AfricaDock01_blend.obj", "", -640f, 11f, 256f, -0.5f, 0f, 2f);
            createModel("Models/Structures/JungleHut02/JungleHut02_blend.obj", "", -100f, 13f, -600f, 0f, 0f, 2f);
            createModel("Models/Structures/AfricaDock01/AfricaDock01_blend.obj", "", -97f, 11f, -620f, 1.5f, 0f, 2f);
            createModel("Models/Structures/Shack01/Shack01_blend.obj", "", 650f, 7f, 0f, 3f, 0f, 3f);
            createModel("Models/Structures/Bridge/Bridge_Dock/Bridge_Dock_blend.obj", "", 554f, 5f, 485f, 0f, 0f, 1f);
            createModel("Models/Structures/Bench/Bench_blend.obj", "", -675f, 11f, -640f, -1f, 0f, 5f);
            createModel("Models/Structures/Bench/BeerBench_blend.obj", "", -655f, 14f, 253f, 1f, 0f, 0.8f);
            createModel("Models/Structures/Hammock/Hammock_blend.obj", "", -644f, 9f, 209f, 1.5f, 0f, 6f);
            
            
            createModel("Models/Furnishments/Bottles/BoxedBottles1/Bottles.j3o", "", -655f, 17f, 253f, 1f, 0f, 0.5f);
            createModel("Models/Furnishments/Bottles/BoxedBottles2/Bottles2.j3o", "", -640f, 8.6f, 280f, 0f, 0f, 0.2f);
            createModel("Models/Furnishments/Bottles/ScrollBottle/ScrollBottle_blend.obj", "", 40f, 2.5f, 310f, 0f, 1f, 0.8f);            
            createModel("Models/Furnishments/Bottles/Bottle1/Bottle1.j3o", "", -623f, 3.2f, 400f, 2f, 0.2f, 12f);
            createModel("Models/Furnishments/Bottles/Bottle1/Bottle1.j3o", "", -625f, 3.2f, 390f, 1f, -0.1f, 12f);
            
            createModel("Models/Furnishments/Candle/candle_model.obj", "", -650f, 11f, -660f, 1f, 0f, 0.01f);
            createModel("Models/Furnishments/Candle/candle_model.obj", "", -658f, 11f, -655f, 1f, 0f, 0.01f);
            
    //vegetation in fixed places (e.g. around structures to avoid overlapping each other)
                createModel("Models/Vegetation/Trees/Palm/Palm01_blender.obj", "", -650, 5f, 350, 2f, 0f, 5f);
                createModel("Models/Vegetation/Trees/Palm/Palm02_blender.obj", "", -640, 5f, 190, 1f, 0f, 4f);
                createModel("Models/Vegetation/Trees/Palm/Palm03_blender.obj", "", -644, 5f, 220, 0f, 0f, 5f);
                createModel("Models/Vegetation/Trees/Palm/Palm01_blender.obj", "", -642, 5f, 201, 1.5f, 0f, 5f);
                createModel("Models/Vegetation/Trees/BeachPalm/BeachPalm.obj", "", -642, 5f, 380, 1f, 0f, 5f);
                createModel("Models/Vegetation/Trees/QueensPalm/QueensPalm.obj", "", -645, 5f, 400, 2f, 0f, 5f);
                createModel("Models/Vegetation/Trees/Banana/Banana01_blend.obj", "", -665, 5f, 420, 0f, 0f, 5f);
                
                
    //randomly placed models (trees, bushes also with random scale)
                createModelRandomized("Models/Vegetation/Trees/Cecropia/Cecropia_blender.obj", "", 20, -600, 700, 7f, -200, -650, 5, 0f, 3f, 5f);
                createModelRandomized("Models/Vegetation/Trees/CiabaTree_01/CiabaTree_01_blender.obj", "", 20, -600, 700, 7f, -200, -650, 5, 0f, 3f, 5f);
                createModelRandomized("Models/Vegetation/Trees/CiabaTree_01/CiabaTree_02_blender.obj", "", 20, -600, 700, 7f, -200, -650, 5, 0f, 3f, 5f);  
                createModelRandomized("Models/Vegetation/Trees/CopalTree/CopalTree_blender.obj", "", 20, -600, 700, 7f, -200, -650, 5, 0f, 3f, 5f);
                
                createModelRandomized("Models/Vegetation/Trees/Palm/Palm01_blender.obj", "", 20, -650, 700, 5f, 250, -200, 5, 0f, 3f, 5f);
                createModelRandomized("Models/Vegetation/Trees/Palm/Palm02_blender.obj", "", 20, -650, 700, 5f, 250, -200, 5, 0f, 3f, 5f);
                createModelRandomized("Models/Vegetation/Trees/Palm/Palm03_blender.obj", "", 20, -650, 700, 5f, 250, -200, 5, 0f, 3f, 5f);
                createModelRandomized("Models/Vegetation/Trees/BeachPalm/BeachPalm.obj", "", 20, -650, 700, 5f, 250, -200, 5, 0f, 3f, 5f);
                createModelRandomized("Models/Vegetation/Trees/QueensPalm/QueensPalm.obj", "", 20, -650, 700, 5f, 250, -200, 5, 0f, 3f, 5f);
                createModelRandomized("Models/Vegetation/Trees/Banana/Banana01_blend.obj", "", 20, -650, 700, 5f, 250, -200, 5, 0f, 3f, 5f);
                
//                createModelRandomized("Models/Vegetation/Trees/Monstera/monstera3.j3o", "Models/Vegetation/Trees/Monstera/Monstera.j3m", 10, -650, 700, 5f, 250, -200, 5, 0f, 1f, 3f);
                createModelRandomized("Models/Vegetation/Trees/Jasmin/Jasmin_blend.obj", "", 3, -650, -680, 10f, -650, -670, 5, 0f, 0.5f, 1f);
                createModelRandomized("Models/Vegetation/Trees/Cactus/cactus.obj", "Models/Vegetation/Trees/Cactus/cactus.j3m", 10, 400, 650, 7f, 50, -600, 5, 0f, 0.04f, 0.06f);
                
                createModelRandomized("Models/Vegetation/Bushes/Bush01/Bush01_blend.obj", "", 10, -650, 700, 7f, -200, -650, 5, 0f, 0.5f, 1f);                    
                createModelRandomized("Models/Vegetation/Plants/TropicFern/TropicFern01_blend.obj", "", 10, -650, 700, 7f, 250, -650, 5, 0f, 2f, 4f);
                createModelRandomized("Models/Vegetation/Plants/BirdsNest/BirdsNest_blend.obj", "", 10, -650, 700, 7f, 250, -650, 5, 0f, 2f, 4f);                    
                createModelRandomized("Models/Vegetation/Plants/Phila01/Phila01_blend.obj", "", 10, -650, 700, 7f, 250, -650, 5, 0f, 2f, 4f);                    
                createModelRandomized("Models/Vegetation/Plants/ElephantEar/ElephantEar_blend.obj", "", 10, -650, 700, 7f, 250, -650, 5, 0f, 2f, 4f);
                createModelRandomized("Models/Vegetation/Plants/Bamboo01/Bamboo01_blender.obj", "", 10, -650, 700, 6f, 250, -650, 5, 0f, 1f, 2f);                    
                
                createModelRandomized("Models/Vegetation/Plants/Chamomille/PlantsFBX.j3o", "Models/Vegetation/Plants/Chamomille/Chamomille.j3m", 5, -630, -680, 7f, 200, 300, 5, 0f, 4f, 6f);                    
                createModelRandomized("Models/Vegetation/Plants/ForestPlant01/Plant1.j3o", "Models/Vegetation/Plants/ForestPlant01/Plant1.j3m", 5, -630, -680, 7f, 200, 300, 5, 0f, 0.5f, 1f);
                createModelRandomized("Models/Vegetation/Plants/ForestPlant01/Plant1.j3o", "Models/Vegetation/Plants/ForestPlant01/Plant1.j3m", 10, 600, 670, 7f, 50, -50, 5, 0f, 0.5f, 1f);
                createModelRandomized("Models/Vegetation/Plants/Pumpkin/pumpkin_plant.obj", "Models/Vegetation/Plants/Pumpkin/pumpkin.j3m", 5, -660, -680, 7f, 270, 300, 5, 0f, 0.05f, 0.1f);
                createModelRandomized("Models/Vegetation/Plants/Vine/vine.obj", "Models/Vegetation/Plants/Vine/vine.j3m", 5, -660, -680, 7f, 200, 290, 5, 0f, 0.08f, 0.1f);                    
                  
                createModelRandomized("Models/Vegetation/Flowers/Lily/Lily_blend.obj", "", 5, -680, -600, 7f, 200, 300, 5, 0f, 0.1f, 0.2f);
                createModelRandomized("Models/Vegetation/Flowers/Lily/Lily_blend.obj", "", 5, 600, 670, 7f, 50, -50, 5, 0f, 0.1f, 0.2f);                    

                createModelRandomized("Models/Vegetation/Grasses/BeachGrass/BeachGrass_blend.obj", "", 100, -650, 620, 6f, 200, 260, 5, 0f, 8f, 10f);                    
                
            
                createModelRandomized("Models/Naturals/StoneAndPlants/StonePlants_blend.obj", "", 5, 600, 670, 7f, 100, -100, 5, 0f, 1f, 2f);
                createModelRandomized("Models/Others/Grave/cross_blend.obj", "", 3, -660, -680, 11f, -645, -650, 2, 0.2f, 1f, 2f);
                
            //buildings
            
            //campfire
                createModel("Models/Others/Campfire/Campfire.j3o", "", -620f, 7f, 250f, 0f, 0, 4f);
                
            
            //crates and barrels            
            createModel("Models/Others/Crate/Crate-04.obj", "Models/Others/Crate/wood_crate.j3m", -640f, 7f, 250f, 1f, 0f, 5f);
            createModel("Models/Others/Crate/Crate-01.obj", "Models/Others/Crate/wood_crate.j3m", -640f, 11f, 237f, 0f, 0f, 5f);
            createModel("Models/Others/Crate/Crate-02.obj", "Models/Others/Crate/wood_crate.j3m", -640f, 7f, 275f, 1f, 0f, 5f);
            createModel("Models/Others/Crate/Crate-03.obj", "Models/Others/Crate/wood_crate.j3m", -640f, 7f, 280f, 1f, 0f, 5f);
            createModel("Models/Others/Crate/Crate-05.obj", "Models/Others/Crate/wood_crate.j3m", -595f, 4f, 401f, -1f, 0f, 5f);
            createModel("Models/Others/Crate/Crate-05.obj", "Models/Others/Crate/wood_crate.j3m", -620f, 3f, 390f, 1f, 0f, 6f);
            createModel("Models/Others/Crate/Crate_jetty_01.j3o", "Models/Others/Crate/Crate_jetty_01.j3m", -595f, 4.1f, 398f, 0f, 0f, 2f);
            
            
            createModel("Models/Others/Cage/CageBed.j3o", "Models/Others/Cage/cage.j3m", -650f, 7f, 300f, -2f, 0f, 3f);
                        
            createModel("Models/Others/Barrel/mini_wood_barrel.obj", "Models/Others/Barrel/wood_barrel.j3m", -645f, 7f, 237f, 1f, 0f, 0.05f);
            createModel("Models/Others/Barrel/mini_wood_barrel.obj", "Models/Others/Barrel/wood_barrel.j3m", -640f, 7f, 237f, 1f, 0f, 0.05f);
        }   
    
        
        
        
    
    public void createModel(String modelfile, String matfile, float xpos, float ypos, float zpos, float yaw, float pitch, float scale){
            Spatial model = assetManager.loadModel(modelfile);
                        
            if(matfile !=""){   
            model.setMaterial(assetManager.loadMaterial(matfile));
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
        
        public void createModelRandomized(String modelfile, String matfile, int count, int minx, int maxx, float ypos, int minz, int maxz, int yaw, float pitch, float minscale, float maxscale){
                            
            
            for (int i = 0; i < count; i++){
                Spatial model = assetManager.loadModel(modelfile);
                       
                    if(matfile !=""){   
                    model.setMaterial(assetManager.loadMaterial(matfile));
                }    
                
                int x = FastMath.nextRandomInt(minx, maxx);
                int z = FastMath.nextRandomInt(minz, maxz);
                int ydeg = FastMath.nextRandomInt(0, yaw);
                float modelsize = (FastMath.nextRandomFloat()%(maxscale-minscale)+minscale);
                
                    model.setLocalTranslation(x, ypos, z);
                    model.rotate(pitch, ydeg, 0);
                    model.setLocalScale(modelsize);
                                
            
            CollisionShape sceneModel = CollisionShapeFactory.createMeshShape(model);
            modelRigidBody = new RigidBodyControl(sceneModel,0);
            model.addControl(modelRigidBody);
            
            bulletAppState.getPhysicsSpace().add(modelRigidBody);
            rootNode.attachChild(model);
            }
        }
        
        
    
    public void createGraveParticleEffects(){
        
            particle1 = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material precipitationMaterial = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
            precipitationMaterial.setTexture("Texture", this.app.getAssetManager().loadTexture("Effects/Explosion/flame.png"));
            particle1.setMaterial(precipitationMaterial);
            particle1.setImagesX(2);
            particle1.setImagesY(2);
            particle1.getParticleInfluencer().setInitialVelocity(new Vector3f(0,-1,0));
            particle1.getParticleInfluencer().setVelocityVariation(1.0f);
            particle1.setLocalTranslation(-674, 30, -651);
            particle1.setStartSize(0.5f);
            particle1.setEndSize(1.0f);
            //particle1.setGravity(1,1,1);
            particle1.setLowLife(10f);
            particle1.setHighLife(40f);
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
            sun.setDirection(new Vector3f(1.0f, -1.5f, -4.0f).normalizeLocal());
            this.app.getRootNode().addLight(sun);
         }
        
        public void lightModel(float xpos, float ypos, float zpos){
        PointLight model_light = new PointLight();
        model_light.setColor(ColorRGBA.Yellow);
        model_light.setRadius(4f);
        model_light.setPosition(new Vector3f(xpos, ypos, zpos));
        this.app.getRootNode().addLight(model_light);
        
        }
        
        public void createAdvancedWater(){
            //post process water
        ppFilter = new FilterPostProcessor(assetManager);
        ppWaterFilter = new WaterFilter(rootNode, sun.getDirection());
        ppWaterFilter.setSunScale(1f);
        ppWaterFilter.setWaterHeight(ppInitialWaterHeight);
        ppWaterFilter.setWindDirection(new Vector2f(0.2f,0.8f));     
        ppWaterFilter.setNormalScale(0.5f);
        ppWaterFilter.setFoamHardness(0.5f);
        ppWaterFilter.setFoamExistence(new Vector3f(0.5f,1f,1f));
        ppFilter.addFilter(ppWaterFilter);
        viewPort.addProcessor(ppFilter);
        
        }
        
        public void updateAdvancedWater(float tpf){
            
            waveTime += tpf;
            ppWaterHeight = (float) Math.cos(((waveTime * 0.5f) % FastMath.TWO_PI)) * 1.0f;
            ppWaterFilter.setWaterHeight(ppInitialWaterHeight + ppWaterHeight);
                        
        }
        
        private void loadAudio(){
            
            AudioManager.loadMusic("Music/Soundtracks/Peaceful_Place.ogg", false, true);
            
            AudioManager.playSound("Sounds/Ambient/Animals/ocean_seagull_mono.ogg", false, true, true, 1000f, 3, 0f, 5f, 500f);
            AudioManager.playSound("Sounds/Ambient/Fire/torchBurning.ogg", false, true, true, 1000f, 2, -620f, 7f, 250f);
            AudioManager.playSound("Sounds/Ambient/Environment/JungleAmbient01.ogg", false, true, true, 1000f, 3, -100f, 5f, -630f);
            
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
