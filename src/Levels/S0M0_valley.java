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
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;
import Managers.AudioManager;
import Managers.EffectsManager;
import mygame.GameAppState;
import Managers.ModelManager;
import Managers.ParticleManager;
import mygame.PlayGame;
import Managers.SkyBoxManager;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class S0M0_valley extends BaseAppState {

    
    private SimpleApplication app;
    
    private Node              rootNode;
    private AssetManager      assetManager;
    private AppStateManager   stateManager;
    private InputManager      inputManager;
        
    private ModelManager modelManager;
    private EffectsManager effectManager;
    private ParticleManager particleManager;
    private SkyBoxManager skyBoxCreate;
    
    private ViewPort          viewPort;
    private Camera camera;
        
        
    ParticleEmitter snowEmitter, snowStormEmitter;
    SimpleWaterProcessor waterCreator;
    //for Post process water effectprocessor
    
    private DirectionalLight sun;
    private Vector3f playerSpawnPoint = new Vector3f(600f,500f,-1200f);
    
    private TerrainQuad terrainS0M0;
    private Material matTerrain;
    private AbstractHeightMap heightmap;
    private Texture heightMapImage;
    private final int TERRAIN_SIZE = 2049;
    private TerrainLodControl lodControl;
    
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
        effectManager = new EffectsManager();
        particleManager = new ParticleManager();
        skyBoxCreate = new SkyBoxManager();
        
        GameAppState.playerSpawnPoint.set(playerSpawnPoint);
        
        skyBoxCreate.loadBasicSkyBox("snowy_mountain");
              
        generateLandScape();
        generateHeightMap();
        generateTerrain();
        
        setCollision();
        
        createSun();
        createAmbientLight();
        
        loadSceneModels();
                
        initStaticParticles(); //e.g. leaves, fireplace, candle flame that do not change location
        initDynamicParticles(); //e.g. bugs, butterflies, rain, snow that change location through update, e.g. follow player
        
        
        //createWaterStream();
        //createWaterFall();
        loadAmbientSound();
        //createSimpleWater(6144, 6144, -3072f, 30f, 3072f);
        createAdvancedWater();
        initGraphicsEffects();
        
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
    effectManager.fadeScreen(true, 5);
    stateManager.getState(GameAppState.class).firstPersonPlayer.setPhysicsLocation(setRandomPlayerSpawnPoint());
    System.out.println(camera.getLocation());
    System.out.println(this.getClass().getName()+" enabled....."); 
    
    AudioManager.musicPlayer.stop();
    AudioManager.loadMusic("Music/Soundtracks/ambient_snow1.ogg", true, true);    
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
        snowEmitter.setLocalTranslation(camera.getLocation().x, camera.getLocation().y+50f, camera.getLocation().z); //snow clouds follow player
        snowStormEmitter.setLocalTranslation(camera.getLocation().x-50, camera.getLocation().y+10f, camera.getLocation().z+20); //storm follows player
    }
    
     public void loadSceneModels(){
            
            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassSingle/BeachGrass/BeachGrass_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2048, 2048, -2048, 2048, 5, 0f, 4f, 6f, false);
            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassSingle/FieldGrass/FieldGrass_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2048, 2048, -2048, 2048, 5, 0f, 1f, 3f, false);
            
            modelManager.createRandomizedModel("Models/Vegetation/Trees/PineTree/PineC/Pine_Snow_Full.obj", ModelManager.staticNode, TREE_COUNT, -2048, 2048, -2048, 2048, 5, 0f, 6f, 8f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/PineTree/PineC/Pine_Snow_Half.obj", ModelManager.staticNode, TREE_COUNT, -2048, 2048, -2048, 2048, 5, 0f, 6f, 8f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/PineTree/PineC/Pine_Snow_None.obj", ModelManager.staticNode, TREE_COUNT, -2048, 2048, -2048, 2048, 5, 0f, 6f, 8f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/PineTree/PineE/BarkPine_blend.obj", ModelManager.staticNode, TREE_COUNT, -2048, 2048, -2048, 2048, 5, 0f, 6f, 8f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/PineTree/PineA/Pine_b.obj", ModelManager.staticNode, TREE_COUNT, -2048, 2048, -2048, 2048, 5, 0f, 1f, 2f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/PineTree/PineD/FirTree_b.obj", ModelManager.staticNode, TREE_COUNT, -2048, 2048, -2048, 2048, 5, 0f, 3f, 5f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/TreeSets/TreeSetA/TreeSetA_blend.obj", ModelManager.staticNode, 5, -2048, 2048, -2048, 2048, 5, 0f, 1f, 3f, true);                        
            modelManager.createRandomizedModel("Models/Vegetation/Trees/Oak/Oak_large.obj", ModelManager.staticNode, TREE_COUNT, -2048, 2048, -2048, 2048, 5, 0f, 4f, 6f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/Oak/Oak_medium.obj", ModelManager.staticNode, TREE_COUNT, -2048, 2048, -2048, 2048, 5, 0f, 4f, 6f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/Oak/Oak_small.obj", ModelManager.staticNode, TREE_COUNT, -2048, 2048, -2048, 2048, 5, 0f, 4f, 6f, true);
                                    
            modelManager.createRandomizedModel("Models/Vegetation/Bushes/Bush01/Bush01_blend.obj", ModelManager.staticNode, BUSH_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 0.5f, 1f, true);                    
            modelManager.createRandomizedModel("Models/Vegetation/Bushes/Bush01/Bush01_snow_blend.obj", ModelManager.staticNode, BUSH_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 0.5f, 1f, true);                    
            
            modelManager.createStaticModel("Models/Structures/Jetty02/Jetty02_blend.obj", ModelManager.staticNode, -375f, 1f, 4090f, 0f, 0f, 2f);
                        
            modelManager.createStaticModel("Models/Others/Campfire/campfire_logs.j3o", ModelManager.staticNode, 560f, 0f, -1200f, 0f, 0f, 6f);
            modelManager.createStaticModel("Models/Others/Campfire/campfire_stones.j3o", ModelManager.staticNode, 560f, 0f, -1200f, 0f, 0f, 6f);
            modelManager.createStaticModel("Models/Others/Floorbed/bed_floor.obj", ModelManager.staticNode, 565f, 0f, -1205f, 1f, 0f, 6f);
            
            modelManager.createStaticModel("Models/Others/Crate/Crate04_blend.obj", ModelManager.destroyableNode, 568f, 0f, -1176f, 1f, 0f, 5f);
            modelManager.createStaticModel("Models/Others/Crate/Crate-01_blend.obj", ModelManager.destroyableNode, 552f, 0f, -1195f, 3f, 0f, 5f);
            modelManager.createStaticModel("Models/Others/Crate/Crate02_blend.obj", ModelManager.destroyableNode, 546f, 0f, -1200f, 6f, 0f, 5f);
            modelManager.createStaticModel("Models/Others/Crate/Crate03_blend.obj", ModelManager.destroyableNode, 554f, 0f, -1205f, 19f, 0f, 5f);
            modelManager.createStaticModel("Models/Others/Crate/Crate05_blend.obj", ModelManager.destroyableNode, 555, 0f, -1210, 0f, 0f, 5f);
                       
            
            modelManager.createStaticModel("Models/Others/Barrel/Barrel_blend.obj", ModelManager.destroyableNode, 570f, 0f, -1253f, 2f, 0f, 5f);
            modelManager.createStaticModel("Models/Others/Barrel/Barrel_blend.obj", ModelManager.destroyableNode, 575f, 0f, -1238f, 2f, 0f, 5f);
            modelManager.createStaticModel("Models/Others/Barrel/Barrel_blend.obj", ModelManager.destroyableNode, -345f, 0f, 4080f, 1f, 0f, 5f);
            modelManager.createStaticModel("Models/Others/Barrel/Barrel_blend.obj", ModelManager.destroyableNode, -355f, 0f, 4075f, 0f, 0f, 5f);
                        
            modelManager.createStaticModel("Models/Structures/MedievalShack/medievalshackwood.j3o", ModelManager.staticNode, 540f, 0f, -1234f, 1f, 0f, 0.06f);
            modelManager.createStaticModel("Models/Others/Signpost/SignPost.j3o", ModelManager.destroyableNode, 600f, 0f, -1185f, -1f, 0f, 0.8f);
            modelManager.createStaticModel("Models/Others/Signpost/SignPost.j3o", ModelManager.destroyableNode, -340f, 0f, 4080f, -1f, 0f, 0.8f);
        }   
    
        
    
     public void createPrecipitation(){
            snowEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material snowflake = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                snowflake.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
            snowEmitter.setMaterial(snowflake);
            snowEmitter.setImagesX(2);
            snowEmitter.setImagesY(2);
            snowEmitter.setStartColor(ColorRGBA.White);
            snowEmitter.setEndColor(ColorRGBA.White);
            snowEmitter.setLowLife(20f);
            snowEmitter.setHighLife(50f);
            snowEmitter.setStartSize(0.1f);
            snowEmitter.setEndSize(0.3f);
            snowEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(1,-5,-1));
            snowEmitter.getParticleInfluencer().setVelocityVariation(0.3f);
            snowEmitter.setRotateSpeed(1f);
            snowEmitter.setLocalTranslation(0f, 100f, 0f);
            snowEmitter.setNumParticles(500);
            snowEmitter.setParticlesPerSec(50);
            snowEmitter.setSelectRandomImage(true);
            snowEmitter.setRandomAngle(true);
            snowEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO,200f));
            //preciEmitter.setShape(new EmitterBoxShape(new Vector3f(-50f,-50f,-50f),new Vector3f(50f,50f,50f)));
            rootNode.attachChild(snowEmitter);
            
        }
     
     public void createExtremeWeather(){
            snowStormEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material snowflake = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                snowflake.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
            snowStormEmitter.setMaterial(snowflake);
            snowStormEmitter.setImagesX(2);
            snowStormEmitter.setImagesY(2);
            snowStormEmitter.setStartColor(ColorRGBA.White);
            snowStormEmitter.setEndColor(ColorRGBA.White);
            snowStormEmitter.setLowLife(5f);
            snowStormEmitter.setHighLife(10f);
            snowStormEmitter.setStartSize(0.1f);
            snowStormEmitter.setEndSize(0.3f);
            snowStormEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(25,-1,-5));
            snowStormEmitter.getParticleInfluencer().setVelocityVariation(0.3f);
            snowStormEmitter.setRotateSpeed(1f);
            snowStormEmitter.setLocalTranslation(0f, 100f, 0f);
            snowStormEmitter.setNumParticles(1000);
            snowStormEmitter.setParticlesPerSec(200);
            snowStormEmitter.setSelectRandomImage(true);
            snowStormEmitter.setRandomAngle(true);
            snowStormEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO,100f));
            //snowStormEmitter.setShape(new EmitterBoxShape(new Vector3f(-50f,-50f,-50f),new Vector3f(50f,50f,50f)));
            rootNode.attachChild(snowStormEmitter);
            
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
            wstreamEmitter.setLocalTranslation(90f, 0f, -5f);
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
                                waterCreator.setReflectionScene(terrainS0M0);
                                
            
            Vector3f waterLocation = new Vector3f(0,0,0);
            waterCreator.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
            viewPort.addProcessor(waterCreator);
            waterCreator.setWaterDepth(40);
            waterCreator.setDistortionScale(0.1f);
            waterCreator.setWaveSpeed(0.01f);
                                    
            Geometry watergeom = waterCreator.createWaterGeometry(width, depth);
                     watergeom.setLocalTranslation(posx, posy, posz);
                     watergeom.setShadowMode(RenderQueue.ShadowMode.Receive);
                     watergeom.setMaterial(waterCreator.getMaterial());
                     rootNode.attachChild(watergeom);
        }
        
        
        public void createAdvancedWater(){
            //post process water
        
        effectManager.waterFilter = new WaterFilter(rootNode, sun.getDirection());
        effectManager.waterFilter.setSunScale(1f);
        effectManager.waterFilter.setWaterHeight(250f);
        effectManager.waterFilter.setWindDirection(new Vector2f(0.2f,-0.2f));     
        effectManager.waterFilter.setNormalScale(0.5f);
        effectManager.waterFilter.setFoamHardness(0.5f);
        effectManager.waterFilter.setFoamExistence(new Vector3f(0.5f,-2f,1f));
        effectManager.waterFilter.setUseRipples(true);
        effectManager.waterFilter.setDeepWaterColor(ColorRGBA.Brown);
        effectManager.waterFilter.setWaterColor(ColorRGBA.Brown);
        effectManager.waterFilter.setWaterTransparency(0.2f);
        effectManager.waterFilter.setMaxAmplitude(0.2f);
        effectManager.waterFilter.setWaveScale(0.008f);
        effectManager.waterFilter.setSpeed(0.5f); //> 0 = for rivers
        effectManager.waterFilter.setShoreHardness(0.5f);
        effectManager.waterFilter.setRefractionConstant(0.2f);
        effectManager.waterFilter.setShininess(0.3f);
        effectManager.waterFilter.setColorExtinction(new Vector3f(10.0f, 20.0f, 30.0f));
        effectManager.filterPostProc.addFilter(effectManager.waterFilter);
                
        }
         
        
        public void createSun(){
            
            sun = new DirectionalLight();
            sun.setDirection(new Vector3f(-15.0f, -5.0f, -6.0f).normalizeLocal()); // positive y-value gives nighttime
            sun.setColor(ColorRGBA.White.multLocal(1f));
            rootNode.addLight(sun);
            
        }
        
        public void createAmbientLight(){
            
//            DirectionalLight ambient = new DirectionalLight();
//            ambient.setDirection(new Vector3f(10f, -2.5f, 5.9f).normalizeLocal()); //higher negative y value makes the shadow distance shorter
//            ambient.setColor(ColorRGBA.White.clone()); 
//            rootNode.addLight(ambient);
            AmbientLight al = new AmbientLight();
            al.setColor(ColorRGBA.White.multLocal(0.6f));
            rootNode.addLight(al);
                        
         }
        
                
        public void loadAmbientSound(){

            //AudioManager.playSound("Sounds/Ambient/Water/waterstream.ogg", false, true, true, 1000f, 0.5f, 23f, -0.5f, -11f);
            //AudioManager.playSound("Sounds/Ambient/Water/waterfall_01.ogg", false, false, true, 1000f, 0.5f, 32f, 2f, -2f); 
            AudioManager.playSound("Sounds/Ambient/Fire/torchBurning.ogg", false, true, true, 1000f, 1.0f, -148f, 24.5f, -185f); 
            AudioManager.playSound("Sounds/Ambient/Environment/forest_amb.ogg", false, false, true, 50f, 1.0f, 0f, 0f, 0f); 
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
            TextureKey hmKey = new TextureKey("Textures/Terrain/HeightMaps/HM_S0M0.png", false);
            heightMapImage = assetManager.loadTexture(hmKey);
            
            // DiffuseMaps, Diffuse textures 0 to 3 use the first AlphaMap
            
            Texture snow1 = assetManager.loadTexture("Textures/Terrain/Snow/snow0.jpg");
            snow1.setWrap(Texture.WrapMode.Repeat);
            matTerrain.setTexture("DiffuseMap", snow1);
            matTerrain.setFloat("DiffuseMap_0_scale", 256f);

            
            Texture snow2 = assetManager.loadTexture("Textures/Terrain/Snow/snow1.jpg");
            snow2.setWrap(Texture.WrapMode.Repeat);
            matTerrain.setTexture("DiffuseMap_1", snow2);
            matTerrain.setFloat("DiffuseMap_1_scale", 256f);

            Texture grass = assetManager.loadTexture("Textures/Terrain/Grass/Grass112.jpg");
            grass.setWrap(Texture.WrapMode.Repeat);
            matTerrain.setTexture("DiffuseMap_2", grass);
            matTerrain.setFloat("DiffuseMap_2_scale", 512f);

            // NORMAL MAPS
                        
            Texture normalMapGrass = assetManager.loadTexture("Textures/Terrain/Grass/Grass112_NRM.png");
            normalMapGrass.setWrap(Texture.WrapMode.Repeat);
            matTerrain.setTexture("NormalMap_2", normalMapGrass);
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
            terrainS0M0 = new TerrainQuad("terrain", 65, TERRAIN_SIZE, heightmap.getHeightMap());//, new LodPerspectiveCalculatorFactory(getCamera(), 4)); // add this in to see it use entropy for LOD calculations
            ModelManager.currentMap = terrainS0M0;
            lodControl = new TerrainLodControl(terrainS0M0, app.getCamera());
            lodControl.setLodCalculator( new DistanceLodCalculator(65, 2.7f) ); // patch size, and a multiplier
            terrainS0M0.addControl(lodControl);
            terrainS0M0.setMaterial(matTerrain);
            terrainS0M0.setShadowMode(RenderQueue.ShadowMode.Receive); //only if ShadowRenderer used instead of ShadowFilter
            terrainS0M0.setModelBound(new BoundingBox());
            terrainS0M0.updateModelBound();
            terrainS0M0.setLocalTranslation(0, 0, 0);
            terrainS0M0.setLocalScale(5f, 10f, 5f);
            rootNode.attachChild(terrainS0M0);
            
            
        }
        
        public void setCollision(){
            CollisionShape sceneLevel = CollisionShapeFactory.createMeshShape(terrainS0M0); 
            RigidBodyControl levelRigidBody = new RigidBodyControl(sceneLevel,0);
            terrainS0M0.addControl(levelRigidBody);
            PlayGame.bulletAppState.getPhysicsSpace().add(levelRigidBody);
        }
       
        public Vector3f setRandomPlayerSpawnPoint(){
            
            int x = FastMath.nextRandomInt(-3000, 3000);
            int z = FastMath.nextRandomInt(-3000, 3000);
            float y = terrainS0M0.getHeight(new Vector2f(x,z))+20f;
            return new Vector3f(x,y,z);
        }
        
        public void initGraphicsEffects(){
            
            //effect.directionalLightShadowRenderer(sun);
            effectManager.depthOfField(0, 100, 0.5f);
            effectManager.directionalLightShadowFilter(sun);
            effectManager.SSAO();
            effectManager.createGodRaysEffect(12000f, 9000f, 5000f, 0.3f);
            //effectManager.createGodRaysEffect(sun, -1000f, 0.3f);
            effectManager.createFog(150f, 1.0f);
            effectManager.bloom(40, 1.0f);
        }
        
        
        public void initStaticParticles(){
                        
            particleManager.createFirePlace("flame.png", 560f, 693f, -1200f);
            
                    
        }
        
        public void initDynamicParticles(){
            createPrecipitation(); //loacation varies through SimpleUpdate
            createExtremeWeather();
            
        }
}
