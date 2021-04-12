/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Utils.MaterialCreator;
import Utils.WaterCreator;
import com.jme3.app.Application;
import com.jme3.app.LegacyApplication;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.audio.AudioNode;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.water.SimpleWaterProcessor;

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
    WaterCreator waterproc = new WaterCreator();
    BitmapText menuItemText, camPosInfoText;
        
    
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
        createSimpleWater(35, 25, -15f, -1f, -12f);
        createSimpleWater(10, 20, 22f, -0.5f, 5f);
        createPrecipitation();
        createFirePlace();
        createCreek();
        createWaterFall();
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
            
            createModel("Models/Crate/Crate-04.obj", "Models/Crate/wood_crate.j3m", 33f, 0.0f, -17f, 1f, 2f);
            createModel("Models/Crate/Crate-01.obj", "Models/Crate/wood_crate.j3m", 33f, 1.05f, -17f, 3f, 2f);
            createModel("Models/Crate/Crate-02.obj", "Models/Crate/wood_crate.j3m", 30f, 0.0f, -17f, 6f, 2f);
            createModel("Models/Crate/Crate-03.obj", "Models/Crate/wood_crate.j3m", 31f, 0.0f, -15f, 19f, 2f);
            createModel("Models/Crate/Crate-05.obj", "Models/Crate/wood_crate.j3m", 20f, 0.0f, -8f, 0f, 2f);
            createModel("Models/Crate/Crate-05.obj", "Models/Crate/wood_crate.j3m", 30f, 0.3f, -17f, 0f, 3f);
            
            createModel("Models/Cage/CageBed.j3o", "Models/Cage/cage.j3m", 25f, 0.0f, -24f, 2f, 1f);
//            createModel("Models/Trunk/trunk.j3o", "", 5f, 0f, 3f, 90f, 1f);
            createModel("Models/Barrel/mini_wood_barrel.obj", "Models/Barrel/wood_barrel.j3m", 20f, 0.0f, -19f, 2f, 0.02f);
            createModel("Models/Barrel/mini_wood_barrel.obj", "Models/Barrel/wood_barrel.j3m", 19f, 0.0f, -23f, 2f, 0.02f);
            
            createModel("Models/Stool/DwarfBeerBarrel.j3o", "Models/Stool/stool.j3m", 2f, 0f, -3f, 0f, 0.5f);
            createModel("Models/Tent/tent_detailedOpen.obj", "Models/Tent/tent.j3m", -5f, 0f, 3f, 90f, 6f);
            createModel("Models/Signpost/sign.obj", "Models/Signpost/signpost.j3m", 21f, -0.25f, -8f, -1f, 3f);
            

        }
    
        public void createModel(String modelfile, String custmatfile, float xpos, float ypos, float zpos, float yaw, float scale){
            Spatial model = this.app.getAssetManager().loadModel(modelfile);
                        
            if(custmatfile !=""){   
            model.setMaterial(this.app.getAssetManager().loadMaterial(custmatfile));
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
        
        public void createCreek(){
            ParticleEmitter creekemitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material waterMaterial = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
            waterMaterial.setTexture("Texture", this.app.getAssetManager().loadTexture("Effects/Explosion/flame.png"));
            creekemitter.setMaterial(waterMaterial);
            creekemitter.setImagesX(2);
            creekemitter.setImagesY(2);
            creekemitter.setEndColor(ColorRGBA.Blue);   
            creekemitter.setStartColor(ColorRGBA.Cyan); 
            creekemitter.setLowLife(10f);
            creekemitter.setHighLife(30f);
            creekemitter.setStartSize(1.5f);
            creekemitter.setEndSize(0.1f);
            creekemitter.setGravity(0, 0, 0);
            creekemitter.getParticleInfluencer().setInitialVelocity(new Vector3f(-1,-0.1f,-1));
            creekemitter.getParticleInfluencer().setVelocityVariation(0.1f);
            creekemitter.setLocalTranslation(32f, -0.7f, -2f);
            creekemitter.setNumParticles(300);
            creekemitter.setParticlesPerSec(50);
            creekemitter.setShape(new EmitterBoxShape(new Vector3f(-2f,-1f,-1f),new Vector3f(2f,1f,1f)));
            startRootNode.attachChild(creekemitter);
            
        }
        
        public void createWaterFall(){
            ParticleEmitter waterfallemitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material waterMaterial = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
            waterMaterial.setTexture("Texture", this.app.getAssetManager().loadTexture("Effects/Explosion/flame.png"));
            waterfallemitter.setMaterial(waterMaterial);
            waterfallemitter.setImagesX(2);
            waterfallemitter.setImagesY(2);
            waterfallemitter.setEndColor(ColorRGBA.Blue);   
            waterfallemitter.setStartColor(ColorRGBA.Cyan); 
            waterfallemitter.setLowLife(10f);
            waterfallemitter.setHighLife(15f);
            waterfallemitter.setStartSize(1.5f);
            waterfallemitter.setEndSize(0.1f);
            waterfallemitter.setGravity(0, 0, 0);
            waterfallemitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0f,-2f,0f));
            waterfallemitter.getParticleInfluencer().setVelocityVariation(0.1f);
            waterfallemitter.setLocalTranslation(32f, 10f, -2f);
            waterfallemitter.setNumParticles(300);
            waterfallemitter.setParticlesPerSec(50);
            waterfallemitter.setShape(new EmitterBoxShape(new Vector3f(-1f,-1f,-1f),new Vector3f(1f,1f,1f)));
            startRootNode.attachChild(waterfallemitter);
            
        }
        
        public void createSimpleWater(float width, float depth, float posx, float posy, float posz){
            
            SimpleWaterProcessor waterCreator = new SimpleWaterProcessor(this.app.getAssetManager());
                                 waterCreator.setReflectionScene(mainScene);
            
            Vector3f waterLocation = new Vector3f(0,-6,0);
            
            waterCreator.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
            this.app.getViewPort().addProcessor(waterCreator);
            waterCreator.setWaterDepth(40);
            waterCreator.setDistortionScale(0.05f);
            waterCreator.setWaveSpeed(0.05f);
                                    
            Geometry watergeom = waterCreator.createWaterGeometry(width, depth);
                     watergeom.setLocalTranslation(posx, posy, posz);
                     watergeom.setShadowMode(RenderQueue.ShadowMode.Receive);
                     watergeom.setMaterial(waterCreator.getMaterial());
                     startRootNode.attachChild(watergeom);
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
            float moveX = this.app.getCamera().getDirection().x/500;
            float moveZ = this.app.getCamera().getDirection().z/500;
            float camx = this.app.getCamera().getLocation().x;
            float camz = this.app.getCamera().getLocation().z;
            float camy = this.app.getCamera().getLocation().y;
              this.app.getCamera().setLocation(new Vector3f(camx-moveX, camy, camz-moveZ));

        }
             
             
             
        public void createMainMenu(){
            this.app.setDisplayStatView(false); this.app.setDisplayFps(false);

            createMenuText("Start New Game", 50, screenHeight-50);
            createMenuText("Game Options", 50, screenHeight-80);
            createMenuText("Credits", 50, screenHeight-110);
            createMenuText("Exit Game", 50, screenHeight-140);

            
        }
             
        public void createMenuText(String name, float posx, float posy){
            menuItemText = new BitmapText(this.app.getAssetManager().loadFont("Interface/Fonts/Antiqua.fnt"), false);
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
                    createMenuText("CamDir: "+app.getCamera().getLocation(), 50, screenHeight-200);
                    
                                    }
            }
        };
    
        
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        rotateCamera(1,tpf,Vector3f.UNIT_Y);
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
    
    
}
