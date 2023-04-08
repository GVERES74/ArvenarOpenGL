/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
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
import com.jme3.texture.Texture.WrapMode;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;
import Managers.AudioManager;
import Managers.EffectsManager;
import mygame.GameAppState;
import Managers.ModelManager;
import mygame.PlayGame;
import Managers.SkyBoxManager;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class S3M0_town extends BaseAppState {

    
    private SimpleApplication app;
    
    private Node              rootNode;
    
    private AssetManager      assetManager;
    private AppStateManager   stateManager;
    private InputManager      inputManager;
    private RenderManager     renderManager;
    
    
    private ModelManager modelManager;
    private EffectsManager effectManager;
    private SkyBoxManager skyBoxCreate;
    
    private ViewPort          viewPort;
    private Camera camera;
        
        
    ParticleEmitter leafEmitter, fireEmitter, rainEmitter;
    SimpleWaterProcessor waterCreator;
    //for Post process water effectprocessor
    
    private DirectionalLight sunPosition;
    private PointLight plLantern;
    private SpotLight slLightHouse;
    private Vector3f playerSpawnPoint = new Vector3f(0f,100f,0f);
        
    private TerrainQuad terrainS3M0;
    private Material matTerrain;
    private AbstractHeightMap heightmap;
    private Texture heightMapImage;
    private final int TERRAIN_SIZE = 2049;
    private TerrainLodControl lodControl;
    
    private float timer = 0;
    
    private final int GRASS_COUNT = 1000;
    private final int FLOWER_COUNT = 500;
    private final int TREE_COUNT = 100;
    private final int BUSH_COUNT = 100;
    private final int PLANT_COUNT = 100;
    private final int OTHER_COUNT = 100;
        
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
        skyBoxCreate = new SkyBoxManager();
        
        GameAppState.playerSpawnPoint.set(playerSpawnPoint);
        
        skyBoxCreate.loadBasicSkyBox("cloudy");
        //skyBoxCreate.createDSkyBox("BrightSky.dds");
        
        generateLandScape();
        generateHeightMap();
        generateTerrain();
        setCollision();
        
        createSun();
        createAmbientLight();
        initPointLights();
        initSpotLights();        
        loadSceneModels();
        createAdvancedWater();
        createPrecipitation();        
        
        
        loadAmbientSound();
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
        
        effectManager.fadeScreen(true,10);
        AudioManager.musicPlayer.stop();
        stateManager.getState(GameAppState.class).firstPersonPlayer.setPhysicsLocation(setRandomPlayerSpawnPoint());
        System.out.println(setRandomPlayerSpawnPoint()); 
        System.out.println(this.getClass().getName()+" enabled....."); 
        System.out.println("CurrentScreen =" + PlayGame.nifty.getCurrentScreen().getScreenId());
        
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
        rainEmitter.setLocalTranslation(camera.getLocation().x, camera.getLocation().y+50f, camera.getLocation().z); //clouds follow player
        timer += tpf;
        
        if (timer > 30){
            loadRandomSound();
            System.out.println("30 sec passed..");
            timer = 0;
        }
        
        
        //updateAdvancedWater(tpf); //off = we want calm rivers and lakes
    }
    
     public void loadSceneModels(){
            
        modelManager.createStaticModel("Models/Structures/Harbor/Dock_01/Dock01_blend.obj", ModelManager.staticNode, -950f, 0f, -2320f, 0f, 0f, 1f);    
        modelManager.createStaticModel("Models/Structures/Harbor/IndustrialShack_01/IndustrialShack01_blend.obj", ModelManager.staticNode, -1040f, 0f, -2400f, 3f, 0f, 3f);    
        modelManager.createStaticModel("Models/Structures/Harbor/SplitRock_01/SplitRock_blend.obj", ModelManager.staticNode, 800f, 0f, 520f, 0f, 0f, 3f); 
        modelManager.createStaticModel("Models/Buildings/OldBusiness/OldHouse_blend.obj", ModelManager.staticNode, 175f, 0f, 673f, 0f, 0f, 3f); 
        
        modelManager.createStaticModel("Models/Structures/Harbor/Warf_01/Warf01_blend.obj", ModelManager.staticNode, -960f, 0f, -2450f, 3f, 0f, 1f);    
        modelManager.createStaticModel("Models/Structures/Harbor/WarfStand_01/WarfStand01_blend.obj", ModelManager.staticNode, -960f, 0f, -2390f, 0f, 0f, 1f);    
        
        modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassSingle/BeachGrass/BeachGrass_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2500, 2500, -2500, 2500, 5, 0f, 5f, 8f, false);
    }        
     
    
     
     public void createPrecipitation(){
            rainEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material rainDrop = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                rainDrop.setTexture("Texture", assetManager.loadTexture("Effects/Smoke/Smoke.png"));
            rainEmitter.setMaterial(rainDrop);
            rainEmitter.setImagesX(15);
            rainEmitter.setImagesY(1);
            rainEmitter.setStartColor(ColorRGBA.DarkGray);
            rainEmitter.setEndColor(ColorRGBA.DarkGray);
            rainEmitter.setLowLife(10f);
            rainEmitter.setHighLife(50f);
            rainEmitter.setStartSize(0.05f);
            rainEmitter.setEndSize(0.1f);
            rainEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(-1,-50,-5));
            rainEmitter.getParticleInfluencer().setVelocityVariation(0.1f);
            //rainEmitter.setRotateSpeed(1f);
            rainEmitter.setLocalTranslation(0f, 100f, 0f);
            rainEmitter.setNumParticles(2000);
            rainEmitter.setParticlesPerSec(200);
            rainEmitter.setSelectRandomImage(true);
            //rainEmitter.setRandomAngle(true);
            rainEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO,100f));
            //preciEmitter.setShape(new EmitterBoxShape(new Vector3f(-50f,-50f,-50f),new Vector3f(50f,50f,50f)));
            rootNode.attachChild(rainEmitter);
            
        }
        
        public void createAdvancedWater(){
            //post process water
        
        effectManager.waterFilter = new WaterFilter(rootNode, new Vector3f(-1f, -1.3f, 5.9f));
        effectManager.waterFilter.setSunScale(2f);
        effectManager.waterFilter.setWaterHeight(5.0f); //height from terrain base
        effectManager.waterFilter.setWindDirection(new Vector2f(1f,-0.8f)); 
        effectManager.waterFilter.setUseFoam(true);
        effectManager.waterFilter.setFoamHardness(0.5f);
        effectManager.waterFilter.setFoamExistence(new Vector3f(0.5f,-2f,1f));
        effectManager.waterFilter.setUseRipples(true); //false = calm lake flat water surface
        effectManager.waterFilter.setDeepWaterColor(ColorRGBA.Gray);
        effectManager.waterFilter.setWaterColor(ColorRGBA.Gray.mult(2.0f));
        effectManager.waterFilter.setWaterTransparency(0.2f);
        effectManager.waterFilter.setMaxAmplitude(0.5f);
        effectManager.waterFilter.setWaveScale(0.008f);
        effectManager.waterFilter.setSpeed(0.1f); //for rivers
        effectManager.waterFilter.setShoreHardness(0.5f);
        effectManager.waterFilter.setRefractionConstant(0.2f);
        effectManager.waterFilter.setShininess(0.5f);
        effectManager.waterFilter.setColorExtinction(new Vector3f(10.0f, 20.0f, 30.0f));
        effectManager.filterPostProc.addFilter(effectManager.waterFilter);
                
        }
        
        public void createSun(){
            
            sunPosition = new DirectionalLight();
            sunPosition.setDirection(new Vector3f(-0.7f, -2.5f, 5.9f).normalizeLocal());
            sunPosition.setColor(ColorRGBA.White);
            rootNode.addLight(sunPosition);
        }
        
        
        public void createAmbientLight(){
            
//            DirectionalLight ambient = new DirectionalLight();
//            ambient.setDirection(new Vector3f(4.9f, -1.3f, -5.9f).normalizeLocal()); //higher negative y value makes the shadow distance shorter
//            ambient.setColor(ColorRGBA.White.clone()); 
//            rootNode.addLight(ambient);
            AmbientLight al = new AmbientLight();
            al.setColor(ColorRGBA.White.multLocal(0.1f));
            rootNode.addLight(al);
                        
         }
        
        public void initPointLights(){
            
//            DirectionalLight ambient = new DirectionalLight();
//            ambient.setDirection(new Vector3f(3f, -3.0f, -6f).normalizeLocal()); //higher negative y value makes the shadow distance shorter
//            ambient.setColor(ColorRGBA.White.clone()); 
//            rootNode.addLight(ambient);
            plLantern = new PointLight();
            plLantern.setColor(ColorRGBA.Yellow.mult(2f));
            plLantern.setRadius(20f);
            plLantern.setPosition(new Vector3f(800f, 210f, 527f));
            plLantern.setEnabled(true);
            rootNode.addLight(plLantern);
                       
         }
        
        public void initSpotLights(){
            slLightHouse = new SpotLight();
            slLightHouse.setSpotRange(1000f);
            slLightHouse.setSpotOuterAngle(25f*FastMath.DEG_TO_RAD);
            slLightHouse.setSpotInnerAngle(15f*FastMath.DEG_TO_RAD);
            slLightHouse.setColor(ColorRGBA.Yellow.mult(2.0f));
            slLightHouse.setDirection(new Vector3f(1f, 0f, 2f));
            slLightHouse.setPosition(new Vector3f(799f, 210f, 527f));
            rootNode.addLight(slLightHouse);
        }
        
        public void loadAmbientSound(){

         AudioManager.playSound("Sounds/Ambient/Animals/shore_seagull.ogg", false, false, true, 50f, 3, -950f, 5f, -2500f);               
                        
        }
        
        public void loadRandomSound(){
           
            int rnum = FastMath.nextRandomInt(1, 10);
            System.out.println("Rnum= "+rnum);
            
            switch (rnum){
                
                
                case 1: AudioManager.playSound("Sounds/Ambient/Environment/Rainy/thunder_2_far.wav", false, false, false, 1000f, 0.5f, 0f, 10f, 0f); //everywhere
                break;  
                
                case 2: AudioManager.playSound("Sounds/Ambient/Environment/Rainy/thunder_3_far.wav", false, false, false, 1000f, 0.5f, 0f, 10f, 0f); //everywhere
                break;
                
                case 3: AudioManager.playSound("Sounds/Ambient/Environment/Rainy/thunder_4_far.wav", false, false, false, 1000f, 0.5f, 0f, 10f, 0f); //everywhere
                break;
                
                case 4: AudioManager.playSound("Sounds/Ambient/Environment/Rainy/thunder_5_far.wav", false, false, false, 1000f, 0.5f, 0f, 10f, 0f); //everywhere
                break;
                
                case 5: AudioManager.playSound("Sounds/Ambient/Environment/Rainy/thunder_6_far.wav", false, false, false, 1000f, 0.5f, 0f, 10f, 0f); //everywhere
                break;
                
                case 6: AudioManager.playSound("Sounds/Ambient/Environment/ambience.ogg", false, false, false, 50f, 0.5f, 0f, 10f, 0f); //everywhere
                break;
                
            }    
                
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
            TextureKey hmKey = new TextureKey("Textures/Terrain/HeightMaps/HM_S3M0.png", false);
            heightMapImage = assetManager.loadTexture(hmKey);
            
                        
            // DiffuseMaps
            Texture grassyRock = assetManager.loadTexture("Textures/Terrain/Rock/Rock_Grass.jpg");
            grassyRock.setWrap(WrapMode.Repeat);
            matTerrain.setTexture("DiffuseMap", grassyRock);
            matTerrain.setFloat("DiffuseMap_0_scale", 512f);

            Texture grass112 = assetManager.loadTexture("Textures/Terrain/Grass/Grass112.jpg");
            grass112.setWrap(WrapMode.Repeat);
            matTerrain.setTexture("DiffuseMap_1", grass112);
            matTerrain.setFloat("DiffuseMap_1_scale", 480f);
            
            
             // NORMAL MAPS
            
            Texture normalMapRock = assetManager.loadTexture("Textures/Terrain/Rock/Rock_Grass_NRM.png");
            normalMapRock.setWrap(WrapMode.Repeat);
            matTerrain.setTexture("NormalMap", normalMapRock);
            
            Texture normalMapGrass112 = assetManager.loadTexture("Textures/Terrain/Grass/Grass112_NRM.png");
            normalMapGrass112.setWrap(WrapMode.Repeat);
            matTerrain.setTexture("NormalMap_1", normalMapGrass112);

        }
        
        public void generateHeightMap(){
        
            heightmap = null;
            try {
                heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.3f);
                //heightmap = new HillHeightMap(1025, 500, 50, 100, (byte) 3);
                heightmap.load();
                heightmap.smooth(0.9f, 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        public void generateTerrain(){
            terrainS3M0 = new TerrainQuad("terrain", 65, TERRAIN_SIZE, heightmap.getHeightMap());//, new LodPerspectiveCalculatorFactory(getCamera(), 4)); // add this in to see it use entropy for LOD calculations
            ModelManager.currentMap = terrainS3M0;
            lodControl = new TerrainLodControl(terrainS3M0, app.getCamera());
            lodControl.setLodCalculator( new DistanceLodCalculator(65, 2.7f) ); // patch size, and a multiplier
            terrainS3M0.addControl(lodControl);
            terrainS3M0.setMaterial(matTerrain);
            terrainS3M0.setModelBound(new BoundingBox());
            terrainS3M0.updateModelBound();
            terrainS3M0.setLocalTranslation(0, 0, 0);
            terrainS3M0.setLocalScale(3f,2f,3f);
            rootNode.attachChild(terrainS3M0);
                        
        }
        
        public void setCollision(){
            CollisionShape sceneLevel = CollisionShapeFactory.createMeshShape(terrainS3M0); 
            RigidBodyControl levelRigidBody = new RigidBodyControl(sceneLevel,0);
            terrainS3M0.addControl(levelRigidBody);
            PlayGame.bulletAppState.getPhysicsSpace().add(levelRigidBody);
        }
        
        public Vector3f setRandomPlayerSpawnPoint(){
            
            int x = FastMath.nextRandomInt(-2048, 2048);
            int z = FastMath.nextRandomInt(-2048, 2048);
            float y = terrainS3M0.getHeight(new Vector2f(x,z))+20f;
            return new Vector3f(x,y,z);
        }
        
        
        public void initGraphicsEffects(){
            
            //effectManager.directionalLightShadowRenderer(sunPosition);
            effectManager.depthOfField(100, 20, 0.3f);
            effectManager.directionalLightShadowFilter(sunPosition);
            effectManager.SSAO();
            effectManager.createGodRaysEffect(700f, 5000f, -8000f, 0.3f);
            effectManager.bloom(55, 1f);
        }
       
        
}
