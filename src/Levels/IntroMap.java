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
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
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
import mygame.AudioManager;
import mygame.EffectsManager;
import mygame.ModelManager;
import mygame.ParticleManager;
import mygame.PlayGame;
import mygame.SkyBoxManager;
import static mygame.ModelManager.destroyableNode;
import static mygame.ModelManager.shootableNode;
import static mygame.ModelManager.staticNode;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class IntroMap extends BaseAppState{

    
    private SimpleApplication app;
    
    private Node              rootNode, aniModel;
    
    private AssetManager      assetManager;
    private AppStateManager   stateManager;
    private InputManager      inputManager;
    private RenderManager     renderManager;
    
    private ModelManager modelManager;
    private EffectsManager effectManager;
    private ParticleManager particleManager;
    private SkyBoxManager skyBoxCreate;
    
    private ViewPort          viewPort;
    private Camera camera;
    
    
    ParticleEmitter butterflyEmitter, bugsEmitter;
    
    SimpleWaterProcessor waterCreator;
    //for Post process water effectprocessor
    
    private DirectionalLight sunPosition;
            
    private TerrainQuad terrainIntroMap;
    private Material matTerrain;
    private AbstractHeightMap heightmap;
    private Texture heightMapImage;
    private final int TERRAIN_SIZE = 2049;
    private TerrainLodControl lodControl;
    
    private float timer = 0.0f;
    private int randomNumber = 0;
    private final int GRASS_COUNT = 5;
    private final int FLOWER_COUNT = 5;
    private final int TREE_COUNT = 5;
    private final int BUSH_COUNT = 5;
    private final int PLANT_COUNT = 5;
    private final int OTHER_COUNT = 5;
    
        
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
        
        rootNode.attachChild(shootableNode);
        rootNode.attachChild(destroyableNode);
        rootNode.attachChild(staticNode);
                
                
        skyBoxCreate.loadBasicSkyBox("rocky_mountain");
        //skyBoxCreate.loadCubeMapSky("skybox.dds");
        //skyBoxCreate.loadSphereMapSky("mountains-lake.jpg");
        
        generateLandScape();
        generateHeightMap();
        generateTerrain();
        setCollision();
        
        createSun();
        
        loadSceneModels();
        createAdvancedWater();
        initStaticParticles(); //e.g. leaves, fireplace, candle flame that do not change location
        initDynamicParticles(); //e.g. bugs, butterflies, rain, snow that change location through update, e.g. follow player        
                
        loadAmbientSound();
       
        initGraphicsEffects();
        
    }

    @Override
    protected void cleanup(Application app) {
        
          rootNode.detachAllChildren();
        //TODO: clean up what you initialized in the initialize method,        
        //e.g. remove all spatials from rootNode    
    }

    //onEnable()/onDisable() can be used for managing things that should     
    //only exist while the state is enabled. Prime examples would be scene     
    //graph attachment or input listener attachment.    
    @Override
    protected void onEnable() {
    
        //effectManager.fadeScreen(true,10);
               
        System.out.println(this.getClass().getName()+" enabled....."); 
        
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
        //continuous updates
        modelManager.getAniModel("WindMillWheel-objnode").rotate(0f,0f,tpf*0.2f); //child names used to get the required model :)
        
        //randomized updates
        timer += tpf;
        
        if (timer > 59f){ randomNumber = FastMath.nextRandomInt(30,90); }
        
            if(randomNumber <= 60){butterflyEmitter.setLocalTranslation(camera.getLocation().x-30, camera.getLocation().y, camera.getLocation().z+20);}
            if(randomNumber > 60){bugsEmitter.setLocalTranslation(camera.getLocation().x-10, camera.getLocation().y, camera.getLocation().z+20);}
                
        if (timer > randomNumber){
            timer = 0.0f;
            randomNumber = FastMath.nextRandomInt(30,90);
            
        }
    }
    
     public void loadSceneModels(){
                     
            //works only with basic models - modelManager.loadAnimatedModel("Models/Structures/Windmill/Windwheel.j3o", 1f, 940f, 35f, -1180f, "WindMillHead256Action");
            
            modelManager.createUpdateAnimatedModel("Models/Structures/Windmill/WindMillWheel.obj", 3f, 800.5f, 87.5f, -1439f);
            
            modelManager.createStaticModel("Models/Structures/Windmill/WindMillStand.obj", ModelManager.staticNode, 800f, -2f, -1440f, 0f, 0f, 3f);         
            modelManager.createStaticModel("Models/Structures/OldShack/OldShack_blend.obj", ModelManager.staticNode, 2930f, 0f, -2520f, 1.6f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/Well/Well1_blend.obj", ModelManager.staticNode, 950f, 0f, -1143f, 1.6f, 0f, 1f);
            modelManager.createStaticModel("Models/Structures/MedievalShack/medievalshackwood.j3o", ModelManager.staticNode, 908f, 0f, -1195f, 1f, 0f, 0.06f);
            modelManager.createStaticModel("Models/Props/Lantern/CandleLantern_blend.obj", ModelManager.destroyableNode, 905.6f, 12.8f, -1190.5f, 1f, 0f, 2.0f);
            modelManager.createStaticModel("Models/Weapons/HuntingRifle/HuntingRifle.j3o", ModelManager.destroyableNode, 899f, 5f, -1194f, 1f, 0f, 2.0f);
            modelManager.createStaticModel("Models/Props/Painting/Painting_Death.obj", ModelManager.destroyableNode, 901.5f, 5.5f, -1198f, 1f, 0f, 2.0f);
            modelManager.createStaticModel("Models/Props/Painting/Painting_rev.obj", ModelManager.destroyableNode, 905f, 5.5f, -1181.5f, 1f, 0f, 2.0f);
            modelManager.createStaticModel("Models/Structures/WoodenFence/WoodenFence_blend.obj", ModelManager.staticNode, 842f, 0f, -1190f, 1f, 0f, 0.7f);
            modelManager.createStaticModel("Models/Structures/Ruins/Ruins_blend.obj", ModelManager.staticNode, -290f, 0.1f, -1650f, 0f, 0f, 2.0f);
            modelManager.createStaticModel("Models/Structures/Ruins/Abbey_wallruins_blend.obj", ModelManager.staticNode, -155f, 0.1f, -1795f, 0f, 0f, 1.0f);
            modelManager.createStaticModel("Models/Buildings/Keep/Keep_blend.obj", ModelManager.staticNode, 1830f, -1.8f, 3355f, 0f, 0f, 2.5f);
            modelManager.createStaticModel("Models/Props/Torch/Torch_blend.obj", ModelManager.destroyableNode, 1818.3f, 5f, 3341f, 1.5f, 0f, 2.0f);
            modelManager.createStaticModel("Models/Others/Campfire/Campfire.j3o", ModelManager.staticNode, 1100f, 0f, -970f, 0f, 0, 4f);
            modelManager.createStaticModel("Models/Structures/Jetty02/Jetty02_blend.obj", ModelManager.staticNode, 1125f, 0.5f, -978f, 0f, 0f, 2f);
            modelManager.createStaticModel("Models/Furnishments/Bed/Bed_blend.obj", ModelManager.destroyableNode, 910f, 0f, -1202f, 1f, 0f, 0.8f);
            modelManager.createStaticModel("Models/Props/CartWheel/CartWheel_blend.obj", ModelManager.destroyableNode, 913f, 0.5f, -1208f, 1.1f, 0.8f, 0.025f);
            modelManager.createStaticModel("Models/Props/Ladder/Ladder_blend.obj", ModelManager.destroyableNode, 920f, -1f, -1212f, 2.6f, -0.48f, 0.04f);
            modelManager.createStaticModel("Models/Props/Ladder0/WoodenLadder_blend.obj", ModelManager.destroyableNode, 882f, -1f, -1228f, 2f, 0f, 0.04f);
            modelManager.createStaticModel("Models/NPC/Mother_blend.obj", ModelManager.staticNode, 900, 0f, -1230f, 2f, 0f, 4f);
            modelManager.createStaticModel("Models/Props/OldWagon/OldWagon_blend.obj", ModelManager.destroyableNode, 996f, 0f, -1206f, 1f, 0f, 3f);
            modelManager.createStaticModel("Models/Props/WoodenCart/WoodenCart_blend.obj", ModelManager.destroyableNode, 980f, 0, -1218f, 1f, 0f, 3f);
            modelManager.createStaticModel("Models/Props/Bench/BeerBench_blend.obj", ModelManager.destroyableNode, 902f, 0, -1190f, 1.0f, 0f, 1f);
            modelManager.createStaticModel("Models/Props/DinnerSet/DinnerSet_blend.obj", ModelManager.destroyableNode, 902f, 3.2f, -1189f, 1.0f, 0f, 1f);
            modelManager.createStaticModel("Models/Props/Bench/Bench_blend.obj", ModelManager.destroyableNode, 919f, 0, -1195f, 1f, 0f, 3f);
            modelManager.createStaticModel("Models/Props/Hatchet/Hatchet_blend.obj", ModelManager.destroyableNode, 911f, 1.4f, -1212.5f, 1f, 2f, 1.5f);

            modelManager.createStaticModel("Models/Vegetation/Trees/Platane/Platane_blend.obj", ModelManager.staticNode, 880f, 0f, -1230f, 2f, 0f, 3f);
            modelManager.createStaticModel("Models/Vegetation/Trees/Stump/Stump_blend.obj", ModelManager.staticNode, 912f, 0f, -1212f, 0f, 0f, 0.5f);
            
            modelManager.createStaticModel("Models/Vegetation/Plants/Pumpkin/Pumpkin_blend.obj", ModelManager.staticNode, 960f, 0f, -1142f, 0f, 0.0f, 0.1f);
            modelManager.createStaticModel("Models/Vegetation/Plants/Pumpkin/Pumpkin_blend.obj", ModelManager.staticNode, 957f, 0f, -1155f, 0f, 0.0f, 0.1f);
            modelManager.createStaticModel("Models/Vegetation/Plants/Pumpkin/Pumpkin_blend.obj", ModelManager.staticNode, 955f, 0f, -1160f, 0f, 0.0f, 0.1f);
            modelManager.createStaticModel("Models/Vegetation/Plants/Pumpkin/Pumpkin_blend.obj", ModelManager.staticNode, 950f, 0f, -1165f, 0f, 0.0f, 0.1f);
            modelManager.createStaticModel("Models/Vegetation/Plants/Pumpkin/Pumpkin_blend.obj", ModelManager.staticNode, 945f, 0f, -1172f, 0f, 0.0f, 0.1f);
            
            modelManager.createStaticModel("Models/Vegetation/Plants/Vine/Vine_blend.obj", ModelManager.staticNode, 900, 0f, -1143, 0f, 0.0f, 0.1f);
            modelManager.createStaticModel("Models/Vegetation/Plants/Vine/Vine_blend.obj", ModelManager.staticNode, 910, 0f, -1140, 0f, 0.0f, 0.1f);                    
            modelManager.createStaticModel("Models/Vegetation/Plants/Vine/Vine_blend.obj", ModelManager.staticNode, 920, 0f, -1138, 0f, 0.0f, 0.1f);                    
            modelManager.createStaticModel("Models/Vegetation/Plants/Vine/Vine_blend.obj", ModelManager.staticNode, 930, 0f, -1137, 0f, 0.0f, 0.1f);                    
            modelManager.createStaticModel("Models/Vegetation/Plants/Vine/Vine_blend.obj", ModelManager.staticNode, 940, 0f, -1136, 0f, 0.0f, 0.1f);                    
            modelManager.createStaticModel("Models/Vegetation/Grasses/GrassPatch/MariaTach/Maria_blend.obj", ModelManager.staticNode, 790, 0f, -1435, 2f, 0.0f, 0.1f);                    
            modelManager.createStaticModel("Models/Vegetation/Grasses/GrassPatch/MariaTach/Maria_blend.obj", ModelManager.staticNode, 810, 0f, -1445, 2f, 0.0f, 0.08f);                              
            
            modelManager.createStaticModel("Models/Animals/Fish/Szardinia_blend.obj", ModelManager.shootableNode, 1151f, 1f, -998f, 2f, 0f, 0.3f);
            modelManager.createStaticModel("Models/Animals/Rabbit/Rabbit0_blend.obj", ModelManager.shootableNode, 920f, 0f, -1275f, 0f, 0f, 0.1f);
            modelManager.createStaticModel("Models/Animals/Deer/Doe_anim.obj", ModelManager.shootableNode, 906f, 0f, -1265f, 2f, 0f, 1f);
            modelManager.createStaticModel("Models/Animals/Deer/Doe_idle.obj", ModelManager.shootableNode, 926f, 0f, -1265f, 0f, 0f, 1f);
            modelManager.createStaticModel("Models/Animals/Cat/Husa_blend.obj", ModelManager.shootableNode, 901.5f, 1.8f, -1228f, 3.0f, 0f, 0.15f);
            modelManager.createStaticModel("Models/Animals/Cat/Cat_blend.obj", ModelManager.shootableNode, 900f, 1.6f, -1232f, 0.0f, 0f, 1.3f);
            modelManager.createStaticModel("Models/Animals/Dog/Dog_blend.obj", ModelManager.shootableNode, 920f, 0f, -1223f, 1f, 0f, 1.2f);
            modelManager.createStaticModel("Models/Animals/Dog/benny.obj", ModelManager.shootableNode, 900f, 0f, -1235f, 0.0f, 0f, 1.2f);
 
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence01_blend.obj", ModelManager.staticNode, 1000f, -1f, -1245f, -0.2f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence01_blend.obj", ModelManager.staticNode, 851f, -1f, -1227f, 2f, 0f, 3f); //right end
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence01_blend.obj", ModelManager.staticNode, 853f, -1f, -1146f, 2f, 0f, 3f); //left end
            
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 877f, -1f, -1142f, 3f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 902f, -1f, -1138f, 3f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 927f, -1f, -1134f, 3f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 952f, -1f, -1130f, 3f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 977f, -1f, -1126f, 3f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 986f, -1f, -1149f, -2f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 996f, -1f, -1172f, -2f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 1006f, -1f, -1195f, -2f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 1016f, -1f, -1218f, -2f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 1026f, -1f, -1241f, -2f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 940f, -1f, -1250f, 0f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 915f, -1f, -1250f, 0f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 890f, -1f, -1250f, 0f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 865f, -1f, -1250f, 0f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/BarbedWireFences/WireFence02_blend.obj", ModelManager.staticNode, 840f, -1f, -1250f, 0f, 0f, 3f);
            modelManager.createStaticModel("Models/Structures/RopeFence/RopeFence_blend.obj", ModelManager.staticNode, 979f, -1f, -1247f, 0f, 0f, 2f);
            
            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassPatch/GrassMeadow/GrassGroup_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 0.4f, 0.6f, false);  
            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassPatch/GrassMeadow/GrassPatch_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 0.1f, 0.5f, false);  
            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassPatch/GrassPack/GreenYellow_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 3f, 5f, false);            

            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassSingle/Cucumber/Cucumber_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 3f, 5f, false);    
            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassSingle/BeachGrass/BeachGrass_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 5f, 8f, false);
            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassSingle/FieldGrass/FieldGrass_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 1f, 3f, false);
            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassSingle/LongGrass/LongGrass_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 1f, 3f, false);
            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassSingle/AfricaGrass_01/AfricaGrass_01_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 1f, 3f, false);
            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassSingle/AfricaGrass_02/AfricaGrass_02_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 1f, 3f, false);
            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassSingle/BeachGrass/WillowGrass_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 8f, 10f, false);
            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassSingle/BeachGrass/Weeds_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 8f, 10f, false);
            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassSingle/BeachGrass/JGrass_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 8f, 10f, false);
            modelManager.createRandomizedModel("Models/Vegetation/Grasses/GrassSingle/BeachGrass/FoxTail_blend.obj", ModelManager.staticNode, GRASS_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 8f, 10f, false);
        
            modelManager.createRandomizedModel("Models/Vegetation/Flowers/YelloPrarieFlower/YellowPrarie_blend.obj", ModelManager.staticNode, FLOWER_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 1f, 3f, false);
            modelManager.createRandomizedModel("Models/Vegetation/Flowers/Lily/Lily_blend.obj", ModelManager.staticNode, FLOWER_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 0.1f, 0.2f, false);
            modelManager.createRandomizedModel("Models/Vegetation/Flowers/Flax/Flax_blend.obj", ModelManager.staticNode, FLOWER_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 0.1f, 0.5f, false);
            
            modelManager.createRandomizedModel("Models/Vegetation/Bushes/Bush01/Bush01_blend.obj", ModelManager.staticNode, BUSH_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 0.5f, 1f, true);                    
            modelManager.createRandomizedModel("Models/Vegetation/Bushes/Bush03/Bush03_blend.obj", ModelManager.staticNode, BUSH_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 1f, 2f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Bushes/Hedge/SmallRound/HedgeRound_blend.obj", ModelManager.staticNode, BUSH_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 0.1f, 0.5f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Bushes/Laurel/Laurel_blend.obj", ModelManager.staticNode, BUSH_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 1f, 2f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Bushes/Birch/Birch_blend.obj", ModelManager.staticNode, BUSH_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 5f, 7f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Bushes/Skatter/Skatter_blend.obj", ModelManager.staticNode, BUSH_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 1f, 2f, true);
            
                        
            modelManager.createRandomizedModel("Models/Vegetation/Trees/PineTree/PineC/Pine_Snow_None.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 4f, 6f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/WhiteBirch/WhiteBirch01_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 3f, 5f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/genericTree_01/genericTree_01_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 4f, 6f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/umbrellaAcacia_01/umbrellaAcacia_01_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 3f, 5f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/acaciaTree_01/acaciaTree_01_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 3f, 5f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/Jasmin/Jasmin.j3o", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 0.5f, 1f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/Platane/Platane_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 4f, 5f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/Maple/Maple_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 3f, 5f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/Cactus/Cactus_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 1f, 3f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/Oak/Oak_large.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 4f, 6f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/Oak/Oak_medium.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 4f, 6f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/Oak/Oak_small.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 4f, 6f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/RedLeaved/RedLeaved_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 6f, 8f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/HillTop/HillTop_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 4f, 6f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/Apple/AppleTree_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 6f, 8f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/JapanMaple/JapanMaple_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 4f, 6f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/Poplar/Poplar_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 4f, 6f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/PineTree/PineA/Pine_b.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 1f, 2f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Trees/PineTree/PineD/FirTree_b.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 3f, 5f, true);
            
            modelManager.createRandomizedModel("Models/Vegetation/Shrubs/Little/LittleTrees_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 0.5f, 2f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Shrubs/Little/Little1_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 0.1f, 0.5f, true);
            modelManager.createRandomizedModel("Models/Vegetation/Shrubs/Little/Little2_blend.obj", ModelManager.staticNode, TREE_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 0.1f, 0.5f, true);
                                 
            modelManager.createRandomizedModel("Models/Vegetation/Plants/ForestPlant01/ForestPlant01_blend.obj", ModelManager.staticNode, PLANT_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 0.5f, 1f, false);
                        
            modelManager.createRandomizedModel("Models/Naturals/SmallRock01/SmallRock_blend.obj", ModelManager.staticNode, OTHER_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 2f, 10f, true);            
            modelManager.createRandomizedModel("Models/Naturals/SticksandStones_01/Sticks_blend.obj", ModelManager.staticNode, OTHER_COUNT, -2000, 2000, -2000, 2000, 5, 0f, 2f, 5f, true);            
     }        
    
     
     
     public void particleButterfly(){
            butterflyEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material butterfly = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                butterfly.setTexture("Texture", assetManager.loadTexture("Effects/Particles/Butterfly/butterflies.png"));
                                
            butterflyEmitter.setMaterial(butterfly);
            butterflyEmitter.setImagesX(3);
            butterflyEmitter.setImagesY(3);
            
            butterflyEmitter.setLowLife(50f);
            butterflyEmitter.setHighLife(60f);
            butterflyEmitter.setStartSize(0.2f);
            butterflyEmitter.setEndSize(0.1f);
            butterflyEmitter.setStartColor(new ColorRGBA(1f,1f,1f,1f));
            butterflyEmitter.setEndColor(new ColorRGBA(1f,1f,1f,1f));
            butterflyEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(3,0,-5));
            butterflyEmitter.getParticleInfluencer().setVelocityVariation(1.0f); //1.0f = random 360° directions //0.5f = 180 grade
            //butterflyEmitter.setRotateSpeed(1f);
            butterflyEmitter.setLocalTranslation(950f, 10f, -1100f);
            butterflyEmitter.setNumParticles(20);
            butterflyEmitter.setParticlesPerSec(5);
            butterflyEmitter.setSelectRandomImage(true);
            butterflyEmitter.setRandomAngle(true);
            butterflyEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO,10f));
            //butterflyEmitter.setShape(new EmitterBoxShape(new Vector3f(-50f,-50f,-50f),new Vector3f(50f,50f,50f)));
            rootNode.attachChild(butterflyEmitter);
            
        }
     
        public void particleBugs(){
            bugsEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material bugs = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
                bugs.setTexture("Texture", assetManager.loadTexture("Effects/Particles/Bugs/bugs.png"));
                                
            bugsEmitter.setMaterial(bugs);
            bugsEmitter.setImagesX(3);
            bugsEmitter.setImagesY(3);
            
            bugsEmitter.setLowLife(50f);
            bugsEmitter.setHighLife(60f);
            bugsEmitter.setStartSize(0.2f);
            bugsEmitter.setEndSize(0.1f);
            bugsEmitter.setStartColor(new ColorRGBA(1f,1f,1f,1f));
            bugsEmitter.setEndColor(new ColorRGBA(1f,1f,1f,1f));
            bugsEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(-3,0,3));
            bugsEmitter.getParticleInfluencer().setVelocityVariation(1.0f); //1.0f = random 360° directions //0.5f = 180 grade
            //bugsEmitter.setRotateSpeed(1f);
            bugsEmitter.setLocalTranslation(950f, 10f, -1100f);
            bugsEmitter.setNumParticles(20);
            bugsEmitter.setParticlesPerSec(5);
            bugsEmitter.setSelectRandomImage(true);
            bugsEmitter.setRandomAngle(true);
            bugsEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO,10f));
            //bugsEmitter.setShape(new EmitterBoxShape(new Vector3f(-50f,-50f,-50f),new Vector3f(50f,50f,50f)));
            rootNode.attachChild(bugsEmitter);
            
        }
        
        public void createAdvancedWater(){
            //post process water
        
        effectManager.waterFilter = new WaterFilter(rootNode, sunPosition.getDirection());
        effectManager.waterFilter.setSunScale(1f);
        effectManager.waterFilter.setWaterHeight(10.0f); //height from terrain base
        effectManager.waterFilter.setWindDirection(new Vector2f(-0.2f,-0.2f)); 
        effectManager.waterFilter.setUseFoam(true);
        effectManager.waterFilter.setFoamHardness(0.1f);
        effectManager.waterFilter.setFoamExistence(new Vector3f(0.1f,-0.5f,0.1f));
        effectManager.waterFilter.setUseRipples(true); //false = calm lake flat water surface
        effectManager.waterFilter.setDeepWaterColor(ColorRGBA.Brown);
        effectManager.waterFilter.setWaterColor(ColorRGBA.Brown.mult(2.0f));
        effectManager.waterFilter.setWaterTransparency(0.2f);
        effectManager.waterFilter.setMaxAmplitude(0.1f);
        effectManager.waterFilter.setWaveScale(0.008f);
        effectManager.waterFilter.setSpeed(0.2f); //> 0 = for rivers
        effectManager.waterFilter.setShoreHardness(0.3f);
        effectManager.waterFilter.setRefractionConstant(0.2f);
        effectManager.waterFilter.setShininess(0.5f);
        effectManager.waterFilter.setColorExtinction(new Vector3f(10.0f, 20.0f, 30.0f));
        effectManager.filterPostProc.addFilter(effectManager.waterFilter);
                
        }
        
        public void createSun(){
            
            sunPosition = new DirectionalLight();
            sunPosition.setDirection(new Vector3f(-3f, -2.5f, 6f).normalizeLocal()); //higher negative y value makes the shadow distance shorter
            sunPosition.setColor(ColorRGBA.White.clone().multLocal(2)); //also controls daytime - multiply = brighter (noon)
            
            rootNode.addLight(sunPosition);
                        
         }
        
                
        public void loadAmbientSound(){
            
            AudioManager.playSound("Sounds/Ambient/Water/AfricaRiver01.ogg", false, false, true, 50f, 0.5f, 1440f, 10f, -1050f);
            AudioManager.playSound("Sounds/Ambient/Environment/nature.ogg", false, false, true, 20f, 2.0f, 0f, 10f, 0f);
                        
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
            TextureKey hmKey = new TextureKey("Textures/Terrain/HeightMaps/HM_S1M0.png", false);
            heightMapImage = assetManager.loadTexture(hmKey);
            
                        
            // DiffuseMaps
            Texture forestGrass = assetManager.loadTexture("Textures/Terrain/Grass/forestgrass.jpg");
            forestGrass.setWrap(WrapMode.Repeat);
            matTerrain.setTexture("DiffuseMap", forestGrass);
            matTerrain.setFloat("DiffuseMap_0_scale", 512f);

            Texture grass110 = assetManager.loadTexture("Textures/Terrain/Grass/Grass110.jpg");
            grass110.setWrap(WrapMode.Repeat);
            matTerrain.setTexture("DiffuseMap_1", grass110);
            matTerrain.setFloat("DiffuseMap_1_scale", 640f);
            
            Texture grass112 = assetManager.loadTexture("Textures/Terrain/Grass/Grass112.jpg");
            grass112.setWrap(WrapMode.Repeat);
            matTerrain.setTexture("DiffuseMap_2", grass112);
            matTerrain.setFloat("DiffuseMap_2_scale", 512f);
            
            
            // NORMAL MAPS
                                    
            Texture normalMapGrass110 = assetManager.loadTexture("Textures/Terrain/Grass/Grass110_NRM.png");
            normalMapGrass110.setWrap(WrapMode.Repeat);
            matTerrain.setTexture("NormalMap", normalMapGrass110);
            
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
            terrainIntroMap = new TerrainQuad("terrain", 129, TERRAIN_SIZE, heightmap.getHeightMap());//, new LodPerspectiveCalculatorFactory(getCamera(), 4)); // add this in to see it use entropy for LOD calculations
            ModelManager.currentMap = terrainIntroMap;
            lodControl = new TerrainLodControl(terrainIntroMap, app.getCamera());
            lodControl.setLodCalculator( new DistanceLodCalculator(129, 2.7f) ); // patch size, and a multiplier
            terrainIntroMap.addControl(lodControl);
            terrainIntroMap.setMaterial(matTerrain);
            terrainIntroMap.setShadowMode(RenderQueue.ShadowMode.Receive); //only if ShadowRenderer used instead of ShadowFilter
            terrainIntroMap.setModelBound(new BoundingBox());
            terrainIntroMap.updateModelBound();
            terrainIntroMap.setLocalTranslation(0, 0, 0);
            terrainIntroMap.setLocalScale(5f,3f,5f);
            rootNode.attachChild(terrainIntroMap);
        }
        
        public void setCollision(){
            CollisionShape sceneLevel = CollisionShapeFactory.createMeshShape(terrainIntroMap); 
            RigidBodyControl levelRigidBody = new RigidBodyControl(sceneLevel,0);
            terrainIntroMap.addControl(levelRigidBody);
            PlayGame.bulletAppState.getPhysicsSpace().add(levelRigidBody);
        }
        
        public Vector3f setRandomPlayerSpawnPoint(){
            
            int x = FastMath.nextRandomInt(-4096, 4096);
            int z = FastMath.nextRandomInt(-4096, 4096);
            float y = terrainIntroMap.getHeight(new Vector2f(x,z))+20f;
            return new Vector3f(x,y,z);
        }
        
        public void initGraphicsEffects(){
            
            //effect.directionalLightShadowRenderer(sunPosition);
            effectManager.depthOfField(200, 20, 0.3f);
            effectManager.directionalLightShadowFilter(new DirectionalLight(new Vector3f(-3f, -2.5f, 6f).normalizeLocal()));
            effectManager.SSAO();
            effectManager.createGodRaysEffect(3500f, 3000f, -8000f, 0.3f);
        }
       
        public void initStaticParticles(){
            particleManager.particleFallingLeaves("leaf4.png", 880f, 80f, -1230f);
            particleManager.particleFallingLeaves("leaves.png", 1830f, 260f, 3355f);
            
            particleManager.createFirePlace("flame.png", 1100f, 12f, -970f);
            particleManager.createCandleFlame("flame.png", 905.6f, 45.8f, -1190.5f);
                    
        }
        
        public void initDynamicParticles(){
            particleButterfly(); //loacation varies through SimpleUpdate
            particleBugs(); //loacation varies through SimpleUpdate
        }
}
