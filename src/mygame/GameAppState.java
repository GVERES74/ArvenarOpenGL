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
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
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
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;

/**
 *
 * @author TE332168
 */
public class GameAppState extends BaseAppState implements AnimEventListener{
    
    private SimpleApplication app;
    
    Node npcPlayer;
    Geometry sphereGeo;
    
    private Node              rootNode;
    private AssetManager      assetManager;
    private AppStateManager   stateManager;
    private InputManager      inputManager;
    private RenderManager     renderManager;
    private AudioRenderer     audioRenderer;
    private ViewPort          viewPort;
    private Camera camera;
    private AudioNode soundPlayer;
    private AudioNode playsoundonce;
        
    private Spatial level;
    private BulletAppState bulletAppState;
    private RigidBodyControl landScape;
            
    private CharacterControl firstPersonPlayer;
    private Vector3f walkDirection = new Vector3f();
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private boolean keyA = false, keyD = false, keyW = false, keyS = false;
    
    private AnimChannel npcWalkChannel;
    private AnimControl control;
    
    ParticleEmitter particle1;
    //for Post process water effectprocessor
    private FilterPostProcessor ppFilter;
    private WaterFilter ppWaterFilter;
    private Vector3f ppLightDir = new Vector3f(7.0f, -1.0f, -4.0f); // same as light source
    private float ppInitialWaterHeight = 0.5f; // choose a value for your scene
    private float waveTime = 0.0f;
    private float ppWaterHeight = 0.0f;
    
    private int playerhp = 100;
    
    
    @Override
    public void initialize(Application app) {
        
        this.app = (SimpleApplication) app;   
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
        this.camera       = this.app.getCamera();
        bulletAppState = new BulletAppState();
        app.getStateManager().attach(bulletAppState);
                
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
                
        
        loadScene("Scenes/S2_Summerdale/S2M0_shore.j3o");
        loadSceneModels();
        //createSimpleWater(1600f, 1600f, -800, -6, 800);
        createAdvancedWater();
        loadAudio();
        createPrecipitationParticleEffects();
        createFirePlace();
        setCollisionPhysics();
        initKeyEvent();
        addAmbientLight();
        createPlayer();
        
    }
        
        public void loadSceneModels(){
            createModel("Models/Boat/boat_crashed.j3o", "", 300f, 0f, 900f, -1f, 0f, 30f);
            createModel("Models/Boat/boat_small.obj", "", 30f, 2f, 320f, 0f, 0.1f, 1f);
            createModel("Models/Shack/small_shack.FBX", "Models/Shack/small_shack.j3m", -650f, 7f, 250f, 1f, -1.55f, 3f);
            createModel("Models/Tree/StrangeCoconutTreeYoung.j3o", "", -650f, 5f, 220f, 1f, 0f, 15f);
            createModel("Models/Tree/StrangePalm.j3o", "", -650f, 5f, 200f, 1f, 0f, 15f);
            createModel("Models/Tree/StrangePalmOld.j3o", "", -650f, 5f, 270f, 1f, 0f, 1f);
            createModel("Models/Tree/StrangePalmCurved.j3o", "", -630f, 5f, 300f, 1f, 0f, 2f);
            createModel("Models/Tree/StrangeCoconutTreeCurved.j3o", "", -630f, 5f, 320f, 1f, 0f, 5f);
            createModel("Models/House/MVK_Houses_01.j3o", "Models/House/MVK_Houses_01.j3m", 0f, 7f, -600f, -1f, 0f, 10f);
            
            createModel("Models/Campfire/campfire_logs.obj", "Models/Campfire/campfire_logs.j3m", -620f, 7f, 250f, 1f, 0f, 5f);
            createModel("Models/Campfire/campfire_stones.obj", "Models/Campfire/campfire_stones.j3m", -620f, 7f, 250f, 1f, 0, 5f);
                        
            createModel("Models/Crate/Crate-04.obj", "Models/Crate/wood_crate.j3m", -640f, 7f, 260f, 1f, 0f, 5f);
            createModel("Models/Crate/Crate-01.obj", "Models/Crate/wood_crate.j3m", -640f, 7f, 270f, 1f, 0f, 5f);
            createModel("Models/Crate/Crate-02.obj", "Models/Crate/wood_crate.j3m", -640f, 7f, 275f, 1f, 0f, 5f);
            createModel("Models/Crate/Crate-03.obj", "Models/Crate/wood_crate.j3m", -640f, 7f, 280f, 1f, 0f, 5f);
            createModel("Models/Crate/Crate-05.obj", "Models/Crate/wood_crate.j3m", -640f, 7f, 265f, 1f, 0f, 5f);
            createModel("Models/Crate/Crate-05.obj", "Models/Crate/wood_crate.j3m", -635f, 7f, 255f, 1f, 0f, 5f);
            
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
            rootNode.attachChild(model);
        }
    
    
        public void addAmbientLight(){
        
            DirectionalLight sun = new DirectionalLight();
            sun.setDirection(new Vector3f(-0.5f, -1f, -3.0f));
            this.app.getRootNode().addLight(sun);
        }
    
        public void createPlayer(){
                /** Load a model. Uses model and texture from jme3-test-data library! */ 
        
        
            npcPlayer = (Node)this.app.getAssetManager().loadModel("Models/Oto/OtoOldAnim.j3o");
            npcPlayer.setLocalTranslation(0, 11, 0);
            //npcPlayer.scale(1f);

            this.app.getRootNode().attachChild(npcPlayer);
            control = npcPlayer.getControl(AnimControl.class);
            control.addListener(this);            

            npcWalkChannel = control.createChannel();
            npcWalkChannel.setAnim("Walk");

            firstPersonPlayer.setGravity(new Vector3f(0,-30f,0));
            firstPersonPlayer.setPhysicsLocation(new Vector3f(0,10,250));
        
        }
    
        public void setCollisionPhysics(){
        
            CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(level);
            landScape = new RigidBodyControl(sceneShape,0);
            level.addControl(landScape);


            CapsuleCollisionShape capsulePlayer = new CapsuleCollisionShape(1.5f,6f,1);
                firstPersonPlayer = new CharacterControl(capsulePlayer, 0.05f);
                firstPersonPlayer.setJumpSpeed(20);
                firstPersonPlayer.setFallSpeed(30);
                bulletAppState.getPhysicsSpace().add(landScape);
                bulletAppState.getPhysicsSpace().add(firstPersonPlayer);

        }
    
        

    
        public void showGuiText(String text, int posx, int posy){
      
          BitmapText title = new BitmapText(this.app.getAssetManager().loadFont("Interface/Fonts/Default.fnt"), false);
          title.setText(text);
          title.setSize(this.app.getAssetManager().loadFont("Interface/Fonts/Default.fnt").getCharSet().getRenderedSize());
          title.setLocalTranslation(posx, posy, 0);
          this.app.getGuiNode().attachChild(title);

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
            particle1.setLocalTranslation(0, 50, 0);
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
        
        public void createAdvancedWater(){
            //post process water
        ppFilter = new FilterPostProcessor(assetManager);
        ppWaterFilter = new WaterFilter(rootNode, ppLightDir);
        ppWaterFilter.setSunScale(2f);
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
        
        public void createSimpleWater(float xwidth, float zdepth, float posx, float posy, float posz){
            
            SimpleWaterProcessor waterCreator = new SimpleWaterProcessor(assetManager);
                                 waterCreator.setReflectionScene(level);
            
            //Vector3f waterLocation = new Vector3f(-500,-6,-500);
            
            //waterCreator.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
            viewPort.addProcessor(waterCreator);
            waterCreator.setWaterDepth(50);
            waterCreator.setDistortionScale(0.1f);
            waterCreator.setWaveSpeed(-0.01f);
                                    
            Geometry watergeom = waterCreator.createWaterGeometry(xwidth, zdepth);
                     watergeom.setLocalTranslation(posx, posy, posz);
                     watergeom.setShadowMode(RenderQueue.ShadowMode.Receive);
                     watergeom.setMaterial(waterCreator.getMaterial());
                     rootNode.attachChild(watergeom);
    }                 
    
        public void loadScene(String gameLevel){
        
            level = this.app.getAssetManager().loadModel(gameLevel);
            level.setLocalScale(2f);
            level.setLocalTranslation(0f, 3f, 0f);
            
            this.app.getRootNode().attachChild(level);

            //showGuiText("Level: "+gameLevel, 500, 650);
             
        }
    
        private void loadAudio(){
        
            PlayGame.playMusic("Music/Soundtracks/Peaceful_Place.ogg");
            playSound("Sounds/Ambient/Animals/ocean_seagull_mono.ogg", false, true, true, 1.5f, 0f, 0f, 500f);
            playSound("Sounds/Ambient/Fire/torchBurning.ogg", false, true, true, 1.5f, -620f, 7f, 250f); 
        }
        
        public void playSound(String filepath, boolean directional, boolean positional, boolean looping, float volume, float xpos, float ypos, float zpos){
        soundPlayer = new AudioNode(assetManager, filepath, DataType.Stream);
        soundPlayer.setDirectional(directional);
        soundPlayer.setPositional(positional);
        soundPlayer.setLocalTranslation(xpos, ypos, zpos);
        soundPlayer.setLooping(looping);
        soundPlayer.setVolume(volume);
//        soundPlayer.setMaxDistance(300f);
        rootNode.attachChild(soundPlayer);
        soundPlayer.play();
        }
        
        public void playSoundInstance(String file){
            playsoundonce = new AudioNode(assetManager, file, DataType.Buffer);
            playsoundonce.setPositional(false);
            playsoundonce.setLooping(false);
            playsoundonce.setVolume(0.5f);
            rootNode.attachChild(playsoundonce);
            playsoundonce.playInstance();
        }
        
        private void initKeyEvent(){
        
            this.app.getInputManager().addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
            this.app.getInputManager().addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
            this.app.getInputManager().addMapping("StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
            this.app.getInputManager().addMapping("StrafeRight", new KeyTrigger(KeyInput.KEY_D));
            this.app.getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
            this.app.getInputManager().addMapping("Crouch", new KeyTrigger(KeyInput.KEY_LCONTROL));
            this.app.getInputManager().addMapping("PauseGame", new KeyTrigger(KeyInput.KEY_ESCAPE));
            this.app.getInputManager().addMapping("MapView", new KeyTrigger(KeyInput.KEY_M));
            

            this.app.getInputManager().addListener(actionListener, "Forward", "Backward", "StrafeLeft", "StrafeRight", "Jump", "Crouch", "PauseGame", "MapView");
            
        }
        
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        if (playerhp < 2) showGuiText("You are dead..", 500, 650); ;
        
        updateAdvancedWater(tpf);
        
        camDir.set(this.app.getCamera().getDirection().multLocal(0.6f));
        camLeft.set(this.app.getCamera().getLeft().multLocal(0.4f));
        walkDirection.set(0, 0, 0);
        
        if (keyW){walkDirection.addLocal(camDir); }
        if (keyS){walkDirection.addLocal(camDir.negate());}
        if (keyA){walkDirection.addLocal(camLeft);}
        if (keyD){walkDirection.addLocal(camLeft.negate());}
        
        firstPersonPlayer.setWalkDirection(walkDirection);
        this.app.getCamera().setLocation(firstPersonPlayer.getPhysicsLocation());
        
        npcPlayer.move(0.01f, 0, 0.01f);
        
        particle1.setLocalTranslation(
                new Vector3f(
                        firstPersonPlayer.getPhysicsLocation().x, 
                        50, 
                        firstPersonPlayer.getPhysicsLocation().z)
        );
    }
    
        private ActionListener actionListener = new ActionListener(){
            public void onAction (String keyBinding, boolean keyPressed, float tpf){
                switch (keyBinding){
                    
                    case "Forward": keyW = keyPressed; break;
                    case "Backward": keyS = keyPressed; break;
                    case "StrafeLeft": keyA = keyPressed; break;
                    case "StrafeRight": keyD = keyPressed; break;
                    case "Jump": if (keyPressed) {firstPersonPlayer.jump(new Vector3f(0,20f,0));
                                 decreasePlayerHealth(); 
                                  }; break;
                    case "Crouch": if (keyPressed) {camera.setLocation(new Vector3f(camera.getLocation().x,camera.getLocation().y-2, camera.getLocation().z));}
                                    break;
                                                     
                    case "PauseGame": if (!PlayGame.getPlayGameApp().getStateManager().hasState(PlayGame.paused_screen) &&!keyPressed){ 
                                           PlayGame.attachAppState(PlayGame.paused_screen);
                                        } else if (!keyPressed){
                                          PlayGame.detachAppState(PlayGame.paused_screen);} break;  
                                        
                    case "MapView": if (!PlayGame.getPlayGameApp().getStateManager().hasState(PlayGame.mapview_screen) &&!keyPressed){
                                        PlayGame.attachAppState(PlayGame.mapview_screen);}
                                    else if (!keyPressed){
                                        PlayGame.detachAppState(PlayGame.mapview_screen);} break;
                }
                
                if (keyPressed) {   
                    playSoundInstance("Sounds/Human/footstep_onsnow1.wav");
                }
                else if (!keyPressed){
                    
                }
                
            }
        };
         
         
        private AnalogListener analogListener = new AnalogListener(){
        
        @Override
            public void onAnalog(String keyBinding, float value, float tpf) {
            playSoundInstance("Sounds/Human/footstep_onsnow1.wav");
        }
        };
        
    
    
    @Override
    public void cleanup(Application app) {
                
        this.app.getRootNode().detachAllChildren();
        System.out.println("GameAppState cleanup called.....");
        
        }

    @Override
    public void onAnimCycleDone(AnimControl arg0, AnimChannel arg1, String arg2) {
        
    }

    @Override
    public void onAnimChange(AnimControl arg0, AnimChannel arg1, String arg2) {
        
    }

    @Override
    protected void onEnable() {
        PlayGame.attachAppState(PlayGame.ingameHud);
        
        System.out.print("GameAppState onEnable() called......");
        
    }

    @Override
    protected void onDisable() {
               
        this.app.getFlyByCamera().setDragToRotate(true);
        
        System.out.print("GameAppState onDisable() called......");
        
    }
    
    public void decreasePlayerHealth(){
        PlayGame.ingameHud.decreasePlayerHealthBar();
        playerhp -=10;
    }
}
