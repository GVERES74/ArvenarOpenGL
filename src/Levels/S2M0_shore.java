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
import com.jme3.asset.TextureKey;
import com.jme3.bounding.BoundingBox;
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
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.water.WaterFilter;
import mygame.AudioManager;
import mygame.EffectsManager;
import mygame.GameAppState;
import mygame.ModelManager;
import mygame.PlayGame;
import mygame.SkyBoxManager;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class S2M0_shore extends BaseAppState {

    
    private SimpleApplication app;
    
    private Node              rootNode;
    private AssetManager      assetManager;
    private AppStateManager   stateManager;
    private InputManager      inputManager;
    private RenderManager     renderManager;
    
    private ViewPort          viewPort;
    private Camera camera;
    
    private ModelManager modelManager;    
    private EffectsManager effect;    
    private SkyBoxManager skyBoxCreate;
        
    ParticleEmitter particle1;
    //for Post process water effectprocessor
    
    private DirectionalLight sunPosition;
    
    private float ppInitialWaterHeight = 2f; // choose a value for your scene
    private float waveTime = 0.0f;
    private float ppWaterHeight = 0.0f;
            
    private Vector3f playerSpawnPoint = new Vector3f(-3400f,20f,-1770f);
    
    
    private TerrainQuad terrainS2M0;
    private Material matTerrain;
    private AbstractHeightMap heightmap;
    private Texture heightMapImage;
    private final int TERRAIN_SIZE = 1025;
    private TerrainLodControl lodControl;
    private String heightMapFile = "Textures/Terrain/HeightMaps/HM_S2M0.png";
    
    private final int GRASS_COUNT = 1000;
    private final int FLOWER_COUNT = 500;
    private final int TREE_COUNT = 100;
    private final int BUSH_COUNT = 500;
    private final int PLANT_COUNT = 500;
    private final int OTHER_COUNT = 500;
    
    
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
        effect = new EffectsManager(); 
        skyBoxCreate = new SkyBoxManager();
        app.setPauseOnLostFocus(true);
        GameAppState.playerSpawnPoint.set(playerSpawnPoint); //sets spawnpoint for ENTER
                
        skyBoxCreate.loadBasicSkyBox("sunset");
        generateLandScape();
        generateHeightMap();
        generateTerrain();
        setCollision();
        
        loadSceneModels();
        sunShine();
        sunLight();
        createAdvancedWater();
        loadAudio();
        leafParticle();
        createFirePlace();
        
        //effect.fadeScreen(true, 10);
        
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
    AudioManager.musicPlayer.stop();
    stateManager.getState(GameAppState.class).firstPersonPlayer.setPhysicsLocation(setRandomPlayerSpawnPoint());
    System.out.println(this.getClass().getName()+" enabled....."); 
        //Called when the state is fully enabled, ie: is attached and         
        //isEnabled() is true or when the setEnabled() status changes after the         
        //state is attached.    
    }
    
    @Override
    protected void onDisable() {
        AudioManager.soundPlayer.stop();
        AudioManager.musicPlayer.stop();
    
        //Called when the state was previously enabled but is now disabled         
        //either because setEnabled(false) was called or the state is being         
        //cleaned up.    
    }
    
    @Override
    public void update(float tpf) {
        
        particle1.clone().setLocalTranslation(camera.getLocation().x, camera.getLocation().y+50f, camera.getLocation().z); //clouds follow player
        
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
            modelManager.createStaticModel("Models/Vehicles/Ship/Pirate/Pship.j3o", ModelManager.staticNode, -4000f, -10f, -2870f, 2f, 0f, 5f);
            modelManager.createStaticModel("Models/Vehicles/Boat/RowBoat2/Boat2.j3o", ModelManager.staticNode, -3900f, 0f, -2000f, -1f, -0.1f, 0.2f);
            modelManager.createStaticModel("Models/Vehicles/Boat/RowBoat1/Boat.j3o", ModelManager.staticNode, -3350f, 1f, -3485f, 3f, 0f, 5f);
            
            
            modelManager.createStaticModel("Models/Animals/Birds/Seagull1/Seagull1.j3o", ModelManager.shootableNode, 554f, 1f, -485f, 2f, 0f, 2f);
            modelManager.createStaticModel("Models/Animals/Birds/Seagull2/Seagull2.j3o", ModelManager.shootableNode, 526f, 1f, -489.5f, 2f, 0f, 2f);
            
            
            
    //schacks, huts, jettys
           
            modelManager.createStaticModel("Models/Structures/Bridge/Turn_Bridge/Turn_Bridge.j3o", ModelManager.staticNode, -3356f, 5f, -2115f, 0f, 0f, 1f);
            modelManager.createStaticModel("Models/Structures/Bridge/Turn_Bridge/Stairs.j3o", ModelManager.staticNode, -3356f, 1.5f, -2098f, 0f, 0f, 1f);
            modelManager.createStaticModel("Models/Structures/Bridge/Bridge_Dock/Bridge_Dock.j3o", ModelManager.staticNode, -3360f, 2f, -3480f, 0f, 0f, 1f);
            modelManager.createStaticModel("Models/Structures/JungleHut01/JungleHut01_blend.obj", ModelManager.staticNode, -3360f, 8f, -1730f, -2f, 0f, 2f);
            modelManager.createStaticModel("Models/Structures/AfricaDock01/AfricaDock01_blend.obj", ModelManager.staticNode, -3375f, 4.5f, -1735f, 2.7f, 0f, 2f);
            modelManager.createStaticModel("Models/Structures/JungleHut02/JungleHut02_blend.obj", ModelManager.staticNode, 320f, 2f, -140f, 0f, 0f, 2f);
            modelManager.createStaticModel("Models/Structures/AfricaDock01/AfricaDock01_blend.obj", ModelManager.staticNode, 324f, 0f, -160f, 1.5f, 0f, 2f);
            modelManager.createStaticModel("Models/Structures/Shack01/Shack01_blend.obj", ModelManager.staticNode, 2680f, 0f, 2074f, 3f, 0f, 3f);
            
            
            modelManager.createStaticModel("Models/Structures/Bench/BeerBench_blend.obj", ModelManager.destroyableNode, -3355f, 7f, -1733f, 1.2f, 0f, 0.8f);
            modelManager.createStaticModel("Models/Structures/Hammock/Hammock_blend.obj", ModelManager.destroyableNode, -3392f, 2f, -1700f, -0.1f, 0f, 6f);
            
            
            modelManager.createStaticModel("Models/Furnishments/Bottles/BoxedBottles1/Bottles.j3o", ModelManager.destroyableNode, -3355f, 10f, -1733f, 1f, 0f, 0.5f);
            modelManager.createStaticModel("Models/Furnishments/Bottles/BoxedBottles2/Bottles2.j3o", ModelManager.destroyableNode, -3326f, 1.2f, -1651f, 0f, 0f, 0.6f);
            modelManager.createStaticModel("Models/Furnishments/Bottles/ScrollBottle/ScrollBottle_blend.obj", ModelManager.destroyableNode, -3873f, 0f, -1980f, 0f, 1f, 0.8f);            
            modelManager.createStaticModel("Models/Furnishments/Bottles/Bottle1/Bottle1.j3o", ModelManager.destroyableNode, -3380f, 0f, -2061f, 2f, 0.2f, 12f);
            modelManager.createStaticModel("Models/Furnishments/Bottles/Bottle1/Bottle1.j3o", ModelManager.destroyableNode, -3400f, 0f, -2110f, 1f, -0.1f, 12f);
            
            modelManager.createStaticModel("Models/Furnishments/Candle/candle_model.obj", ModelManager.destroyableNode, -650f, 0f, -660f, 1f, 0f, 0.01f);
            modelManager.createStaticModel("Models/Furnishments/Candle/candle_model.obj", ModelManager.destroyableNode, -658f, 0f, -655f, 1f, 0f, 0.01f);
            
    //vegetation in fixed places (e.g. around structures to avoid overlapping each other)
                modelManager.createStaticModel("Models/Vegetation/Trees/Palm/Palm01_blender.obj", ModelManager.staticNode, -3400f, 0f, -1700f, 2f, 0f, 5f);
                modelManager.createStaticModel("Models/Vegetation/Trees/Palm/Palm02_blender.obj", ModelManager.staticNode, -3385f, 0f, -1705f, 2f, 0f, 5f);
                modelManager.createStaticModel("Models/Vegetation/Trees/Palm/Palm02_blender.obj", ModelManager.staticNode, -3200f, 0f, -1730f, 1f, 0f, 4f);
                modelManager.createStaticModel("Models/Vegetation/Trees/Palm/Palm03_blender.obj", ModelManager.staticNode, -3325f, 0f, -1735f, 0f, 0f, 5f);
                modelManager.createStaticModel("Models/Vegetation/Trees/Palm/Palm01_blender.obj", ModelManager.staticNode, -3310f, 0f, -1734f, 1.5f, 0f, 5f);
                modelManager.createStaticModel("Models/Vegetation/Trees/BeachPalm/BeachPalm.obj", ModelManager.staticNode, -3360f, 0f, -1760f, 1f, 0f, 5f);
                modelManager.createStaticModel("Models/Vegetation/Trees/QueensPalm/QueensPalm.obj", ModelManager.staticNode, -3300f, 0f, -1700f, 2f, 0f, 5f);
                modelManager.createStaticModel("Models/Vegetation/Trees/Banana/Banana01_blend.obj", ModelManager.staticNode, -3330f, 0f, -1720f, 0f, 0f, 5f);
                
                
    //randomly placed models (trees, bushes also with random scale)
                modelManager.createRandomizedModel("Models/Vegetation/Trees/Cecropia/Cecropia_blender.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 3f, 5f, true);
                modelManager.createRandomizedModel("Models/Vegetation/Trees/CiabaTree_01/CiabaTree_01_blender.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 3f, 5f, true);
                modelManager.createRandomizedModel("Models/Vegetation/Trees/CiabaTree_01/CiabaTree_02_blender.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 3f, 5f, true);  
                modelManager.createRandomizedModel("Models/Vegetation/Trees/CopalTree/CopalTree_blender.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 3f, 5f, true);
                
                modelManager.createRandomizedModel("Models/Vegetation/Trees/Palm/Palm01_blender.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 3f, 5f, true);
                modelManager.createRandomizedModel("Models/Vegetation/Trees/Palm/Palm02_blender.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 3f, 5f, true);
                modelManager.createRandomizedModel("Models/Vegetation/Trees/Palm/Palm03_blender.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 3f, 5f, true);
                modelManager.createRandomizedModel("Models/Vegetation/Trees/BeachPalm/BeachPalm.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 3f, 5f, true);
                modelManager.createRandomizedModel("Models/Vegetation/Trees/QueensPalm/QueensPalm.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 3f, 5f, true);
                modelManager.createRandomizedModel("Models/Vegetation/Trees/Banana/Banana01_blend.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 3f, 5f, true);
                
                modelManager.createRandomizedModel("Models/Vegetation/Trees/Jasmin/Jasmin_blend.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 0.5f, 1f, true);
                modelManager.createRandomizedModel("Models/Vegetation/Trees/Cactus/Cactus_blend.obj", ModelManager.staticNode, 100, -1000, 1000, -1000, 1000, 5, 0f, 0.04f, 0.06f, true);
                
                modelManager.createRandomizedModel("Models/Vegetation/Bushes/Bush01/Bush01_blend.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 0.5f, 1f, true);                    
                modelManager.createRandomizedModel("Models/Vegetation/Plants/TropicFern/TropicFern01_blend.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 2f, 4f, false);
                modelManager.createRandomizedModel("Models/Vegetation/Plants/BirdsNest/BirdsNest_blend.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 2f, 4f, false);                    
                modelManager.createRandomizedModel("Models/Vegetation/Plants/Phila01/Phila01_blend.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 2f, 4f, false);                    
                modelManager.createRandomizedModel("Models/Vegetation/Plants/ElephantEar/ElephantEar_blend.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 2f, 4f, false);
                modelManager.createRandomizedModel("Models/Vegetation/Plants/Bamboo01/Bamboo01_blender.obj", ModelManager.staticNode, 100, -3000, 3000, -3000, 3000, 5, 0f, 1f, 2f, false);                    
                
                
                modelManager.createRandomizedModel("Models/Vegetation/Plants/ForestPlant01/ForestPlant01_blend.obj", ModelManager.staticNode, 200, -3000, 3000, -3000, 3000, 5, 0f, 0.5f, 1f, false);
                                  
                modelManager.createRandomizedModel("Models/Vegetation/Flowers/Lily/Lily_blend.obj", ModelManager.staticNode, 200, -3000, 3000, -3000, 3000, 5, 0f, 0.1f, 0.2f, false);
                
                modelManager.createRandomizedModel("Models/Vegetation/Grasses/BeachGrass/BeachGrass_blend.obj", ModelManager.staticNode, 1000, -3000, 3000, -3000, 3000, 5, 0f, 8f, 10f, false);
                modelManager.createRandomizedModel("Models/Vegetation/Grasses/BeachGrass/WillowGrass_blend.obj", ModelManager.staticNode, 1000, -4096, 4096, -4096, 4096, 5, 0f, 8f, 10f, false);
                modelManager.createRandomizedModel("Models/Vegetation/Grasses/BeachGrass/Weeds_blend.obj", ModelManager.staticNode, 1000, -4096, 4096, -4096, 4096, 5, 0f, 8f, 10f, false);
                modelManager.createRandomizedModel("Models/Vegetation/Grasses/BeachGrass/JGrass_blend.obj", ModelManager.staticNode, 1000, -4096, 4096, -4096, 4096, 5, 0f, 8f, 10f, false);
                modelManager.createRandomizedModel("Models/Vegetation/Grasses/BeachGrass/FoxTail_blend.obj", ModelManager.staticNode, 1000, -4096, 4096, -4096, 4096, 5, 0f, 8f, 10f, false);
                
                
                modelManager.createRandomizedModel("Models/Vegetation/Grasses/Kelp_01/Kelp_blend.obj", ModelManager.staticNode, 20, -3700, -4000, -2200, -3500, 3, 0f, 1f, 2f, false);
                modelManager.createRandomizedModel("Models/Vegetation/Grasses/SeaPlant01/SeaPlant01_blend.obj", ModelManager.staticNode, 20, -3700, -4000, -2200, -3500, 3, 0f, 0.5f, 1f, false);
                modelManager.createRandomizedModel("Models/Vegetation/Grasses/SeaPlant_02/SeaPlant02_blend.obj", ModelManager.staticNode, 20, -3700, -4000, -2200, -3500, 3, 0f, 0.5f, 1f, false);
                modelManager.createRandomizedModel("Models/Vegetation/Grasses/waterReeds_01/WaterReeds_blend.obj", ModelManager.staticNode, 20, -3700, -4000, -2200, -3500, 3, 0f, 0.5f, 1f, false);
                
                
            
                modelManager.createRandomizedModel("Models/Naturals/StoneAndPlants/StonePlants_blend.obj", ModelManager.staticNode, 5, -3500, -3400, -2000, -1700, 5, 0f, 1f, 2f, true);
                modelManager.createRandomizedModel("Models/Others/Grave/cross_blend.obj", ModelManager.staticNode, 3, -660, -680, -645, -650, 2, 0.2f, 1f, 2f, true);
                
            //buildings
            
            //campfire
                modelManager.createStaticModel("Models/Others/Campfire/Campfire.j3o", ModelManager.staticNode, -3395f, 0f, -1778f, 0f, 0, 4f);
                
            
            //crates and barrels            
            modelManager.createStaticModel("Models/Others/Crate/Crate04_blend.obj", ModelManager.destroyableNode, -3320f, 0f, -1665f, 1f, 0f, 5f);
            modelManager.createStaticModel("Models/Others/Crate/Crate-01_blend.obj", ModelManager.destroyableNode, -3322f, 0f, -1660f, 0f, 0f, 5f);
            modelManager.createStaticModel("Models/Others/Crate/Crate02_blend.obj", ModelManager.destroyableNode, -3324f, 0f, -1659f, 1f, 0f, 5f);
            modelManager.createStaticModel("Models/Others/Crate/Crate03_blend.obj", ModelManager.destroyableNode, -3326f, 0f, -1651f, 1f, 0f, 5f);
            modelManager.createStaticModel("Models/Others/Crate/Crate05_blend.obj", ModelManager.destroyableNode, -3328f, 0f, -1657f, -1f, 0f, 5f);
            modelManager.createStaticModel("Models/Others/Crate/Crate05_blend.obj", ModelManager.destroyableNode, -3330f, 0f, -1655f, 1f, 0f, 6f);
            modelManager.createStaticModel("Models/Others/Crate/Crate03_blend.obj", ModelManager.destroyableNode, -3332f, 0f, -1652f, 0f, 0f, 4f);
                        
            modelManager.createStaticModel("Models/Others/Barrel/mini_wood_barrel.obj", ModelManager.destroyableNode, -3365f, 0f, -2100f, 1f, 0f, 0.05f);
            modelManager.createStaticModel("Models/Others/Barrel/mini_wood_barrel.obj", ModelManager.destroyableNode, -3340f, 0f, -2090f, 1f, 0f, 0.05f);
        }   
    
        
    
    public void leafParticle(){
        
            particle1 = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material precipitationMaterial = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
            precipitationMaterial.setTexture("Texture", this.app.getAssetManager().loadTexture("Effects/Particles/Leaves/palmleaf.png"));
            particle1.setMaterial(precipitationMaterial);
            particle1.setImagesX(1);
            particle1.setImagesY(1);
            particle1.getParticleInfluencer().setInitialVelocity(new Vector3f(0,-1,-5));
            particle1.getParticleInfluencer().setVelocityVariation(0.5f);
            particle1.setLocalTranslation(-3321, 20, -1730);
            particle1.setStartSize(0.5f);
            particle1.setEndSize(0.5f);
            //particle1.setGravity(1,1,1);
            particle1.setLowLife(10f);
            particle1.setHighLife(100f);
            particle1.setNumParticles(200);
            particle1.setParticlesPerSec(10);
            particle1.setRotateSpeed(2f);
            particle1.setRandomAngle(true);
            particle1.setSelectRandomImage(true); 
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
            pemitter.setStartColor(new ColorRGBA(1f, 0f, 0.0f, 0.8f));   // red
            pemitter.setEndColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
            pemitter.setLowLife(1.5f);
            pemitter.setHighLife(3.5f);
            pemitter.setStartSize(0.5f);
            pemitter.setEndSize(0.1f);
            pemitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0,1,0));
            pemitter.getParticleInfluencer().setVelocityVariation(0.3f);
            pemitter.setLocalTranslation(-3395f, 12f, -1778f);
            pemitter.setNumParticles(200);
            pemitter.setParticlesPerSec(20);
            this.app.getRootNode().attachChild(pemitter);
            
        }
        
        public void sunShine(){
            
            sunPosition = new DirectionalLight();
            sunPosition.setDirection(new Vector3f(-4.9f, -1.3f, 5.9f).normalizeLocal());
            sunPosition.setColor(ColorRGBA.White.clone().multLocal(2));
            this.app.getRootNode().addLight(sunPosition);
         }
        
        public void sunLight(){
            effect.sunGodRays = new LightScatteringFilter(new Vector3f(3000f, 1500f, -8000f));
            
            effect.sunGodRays.setLightDensity(1f);
            effect.filterPostProc.addFilter(effect.sunGodRays);
        }
        
        
        public void createAdvancedWater(){
            //post process water
        
        effect.waterFilter = new WaterFilter(rootNode, sunPosition.getDirection());
        effect.waterFilter.setSunScale(1f);
        effect.waterFilter.setLightColor(ColorRGBA.LightGray);
        effect.waterFilter.setWaterHeight(ppInitialWaterHeight);
        effect.waterFilter.setMaxAmplitude(1f);
        effect.waterFilter.setWaveScale(0.005f);
        effect.waterFilter.setWindDirection(new Vector2f(0.2f,-0.8f));     
        effect.waterFilter.setNormalScale(0.5f);
        effect.waterFilter.setFoamHardness(1.0f);
        effect.waterFilter.setFoamExistence(new Vector3f(0.5f,-2f,1f));
        effect.waterFilter.setUseRipples(true);
        effect.waterFilter.setRefractionConstant(0.2f);
        effect.waterFilter.setShininess(0.3f);
        effect.filterPostProc.addFilter(effect.waterFilter);
        
        
        }
        
        public void updateAdvancedWater(float tpf){
            
            waveTime += tpf;
            ppWaterHeight = (float) Math.cos(((waveTime * 0.5f) % FastMath.TWO_PI)) * 1.0f;
            effect.waterFilter.setWaterHeight(ppInitialWaterHeight + ppWaterHeight);
                        
        }
        
        private void loadAudio(){
            
            AudioManager.loadMusic("Music/Soundtracks/Peaceful_Place.ogg", false, true);
            
            AudioManager.playSound("Sounds/Ambient/Animals/ocean_seagull_mono.ogg", false, true, true, 100f, 3, -3000f, 5f, -2000f);
            AudioManager.playSound("Sounds/Ambient/Fire/torchBurning.ogg", false, true, true, 1000f, 2, -3395f, 13f, -1778f);
            AudioManager.playSound("Sounds/Ambient/Environment/JungleAmbient01.ogg", false, true, true, 100f, 3, 320f, 40f, -140f);
            
        }
    
    public void generateLandScape(){
        
            matTerrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
            matTerrain.setBoolean("useTriPlanarMapping", false);
            matTerrain.setFloat("Shininess", 0.0f);

            // ALPHA map (for splat textures)
            matTerrain.setTexture("AlphaMap", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));
            matTerrain.setTexture("AlphaMap_1", assetManager.loadTexture("Textures/Terrain/splat/alpha1.png"));
            matTerrain.setTexture("AlphaMap_2", assetManager.loadTexture("Textures/Terrain/splat/alpha2.png"));
            

            // HEIGHTMAP image (for the terrain heightmap)
            TextureKey hmKey = new TextureKey(heightMapFile, false);
            heightMapImage = assetManager.loadTexture(hmKey);
                                    
            // DiffuseMaps
            Texture sandDry = assetManager.loadTexture("Textures/Terrain/Sand/Beach110.jpg");
            sandDry.setWrap(Texture.WrapMode.Repeat);
            matTerrain.setTexture("DiffuseMap_2", sandDry);
            matTerrain.setFloat("DiffuseMap_2_scale", 512f);

            
            Texture grass110 = assetManager.loadTexture("Textures/Terrain/Grass/Grass110.jpg");
            grass110.setWrap(Texture.WrapMode.Repeat);
            matTerrain.setTexture("DiffuseMap_1", grass110);
            matTerrain.setFloat("DiffuseMap_1_scale", 640f);
            
            Texture grass112 = assetManager.loadTexture("Textures/Terrain/Grass/Grass112.jpg");
            grass112.setWrap(Texture.WrapMode.Repeat);
            matTerrain.setTexture("DiffuseMap", grass112);
            matTerrain.setFloat("DiffuseMap_0_scale", 512f);
            
            // NORMAL MAPS
            
            Texture normalMapSand = assetManager.loadTexture("Textures/Terrain/Sand/Beach110_NRM.png");
            normalMapSand.setWrap(Texture.WrapMode.Repeat);
            matTerrain.setTexture("NormalMap_2", normalMapSand);
            
            Texture normalMapGrass110 = assetManager.loadTexture("Textures/Terrain/Grass/Grass110_NRM.png");
            normalMapGrass110.setWrap(Texture.WrapMode.Repeat);
            matTerrain.setTexture("NormalMap_1", normalMapGrass110);
            
            Texture normalMapGrass112 = assetManager.loadTexture("Textures/Terrain/Grass/Grass112_NRM.png");
            normalMapGrass112.setWrap(Texture.WrapMode.Repeat);
            matTerrain.setTexture("NormalMap", normalMapGrass112);
            
        }
        
        public void generateHeightMap(){
        
            heightmap = null;
            try {
                heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.3f);
                //heightmap = new HillHeightMap(513, 5, 200, 300, (byte) 3);
                heightmap.load();
                heightmap.smooth(0.9f, 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        public void generateTerrain(){
            terrainS2M0 = new TerrainQuad("terrain", 65, TERRAIN_SIZE, heightmap.getHeightMap());//, new LodPerspectiveCalculatorFactory(getCamera(), 4)); // add this in to see it use entropy for LOD calculations
            ModelManager.currentMap = terrainS2M0;
            lodControl = new TerrainLodControl(terrainS2M0, app.getCamera());
            lodControl.setLodCalculator( new DistanceLodCalculator(65, 2.7f) ); // patch size, and a multiplier
            terrainS2M0.addControl(lodControl);
            terrainS2M0.setMaterial(matTerrain);
            terrainS2M0.setModelBound(new BoundingBox());
            terrainS2M0.updateModelBound();
            terrainS2M0.setLocalTranslation(0, 0, 0);
            terrainS2M0.setLocalScale(10f, 5f, 10f);
            rootNode.attachChild(terrainS2M0);
                        
        }
        
        
        
        public void setCollision(){
            CollisionShape sceneLevel = CollisionShapeFactory.createMeshShape(terrainS2M0); 
            RigidBodyControl levelRigidBody = new RigidBodyControl(sceneLevel,0);
            terrainS2M0.addControl(levelRigidBody);
            PlayGame.bulletAppState.getPhysicsSpace().add(levelRigidBody);
        }
        
        public Vector3f setRandomPlayerSpawnPoint(){
            
            int x = FastMath.nextRandomInt(-4096, 4096);
            int z = FastMath.nextRandomInt(-4096, 4096);
            float y = terrainS2M0.getHeight(new Vector2f(x,z))+20f;
            return new Vector3f(x,y,z);
        }
       
    
    
       
}
