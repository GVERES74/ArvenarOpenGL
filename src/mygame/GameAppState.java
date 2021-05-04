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
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;
import com.jme3.water.SimpleWaterProcessor;

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
        
        this.app.getFlyByCamera().setDragToRotate(false);
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
                        
        //createRock();
        loadScene("Scenes/S2_Summerdale/S2M0_shore.j3o");
        createSimpleWater(1600f, 1600f, -800, -6, 800);
        loadAudio();
        createPrecipitationParticleEffects();
        setCollisionPhysics();
        initKeyEvent();
        addAmbientLight();
        createPlayer();
        
    }
        
        public void addAmbientLight(){
        
            DirectionalLight sun = new DirectionalLight();
            sun.setDirection(new Vector3f(-0.3f, -0.7f, -3.0f));
            this.app.getRootNode().addLight(sun);
        }
    
        public void createPlayer(){
                /** Load a model. Uses model and texture from jme3-test-data library! */ 
        
        
            npcPlayer = (Node)this.app.getAssetManager().loadModel("Models/Oto/OtoOldAnim.j3o");
            npcPlayer.setLocalTranslation(0, 3, 0);
            //npcPlayer.scale(1f);

            this.app.getRootNode().attachChild(npcPlayer);
            control = npcPlayer.getControl(AnimControl.class);
            control.addListener(this);            

            npcWalkChannel = control.createChannel();
            npcWalkChannel.setAnim("Walk");

            firstPersonPlayer.setGravity(new Vector3f(0,-30f,0));
            firstPersonPlayer.setPhysicsLocation(new Vector3f(-100,5,0));
        
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
    
        public void createRock(){
        
            Sphere sphereMesh = new Sphere(30,30, 2f);
            sphereGeo = new Geometry("Shiny rock", sphereMesh);
            sphereMesh.setTextureMode(Sphere.TextureMode.Projected);
            TangentBinormalGenerator.generate(sphereMesh);

            sphereGeo.setMaterial((Material) this.app.getAssetManager().loadMaterial("Materials/ShinyMaterialCustomized.j3m") );
            sphereGeo.setLocalTranslation(4, 4, -3);
            this.app.getRootNode().attachChild(sphereGeo);

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
        
        
        public void createSimpleWater(float xwidth, float zdepth, float posx, float posy, float posz){
            
            SimpleWaterProcessor waterCreator = new SimpleWaterProcessor(assetManager);
                                 waterCreator.setReflectionScene(level);
            
            //Vector3f waterLocation = new Vector3f(-500,-6,-500);
            
            //waterCreator.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
            viewPort.addProcessor(waterCreator);
            waterCreator.setWaterDepth(40);
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
            this.app.getRootNode().attachChild(level);

            showGuiText("Level: "+gameLevel, 500, 650);
             
        }
    
        private void loadAudio(){
        
//            PlayGame.playMusic("Music/Soundtracks/Peaceful_Place.ogg");
            playSound("Sounds/Ambient/Animals/Seagull.wav", false, false, false, 0.5f, -1000f, 0f, 1000f);
            playSound("Sound/Environment/Ocean Waves.ogg", false, true, true, 0.5f, -1000f, 0f, 1000f);
        }
        
        public void playSound(String filepath, boolean directional, boolean positional, boolean looping, float volume, float xpos, float ypos, float zpos){
        soundPlayer = new AudioNode(assetManager, filepath, DataType.Stream);
        soundPlayer.setDirectional(directional);
        soundPlayer.setPositional(positional);
        soundPlayer.setLocalTranslation(xpos, ypos, zpos);
        soundPlayer.setLooping(looping);
        soundPlayer.setVolume(volume);
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
            this.app.getInputManager().addMapping("PauseGame", new KeyTrigger(KeyInput.KEY_ESCAPE));

            this.app.getInputManager().addListener(actionListener, "Forward", "Backward", "StrafeLeft", "StrafeRight", "Jump", "PauseGame");
            
        }
        
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        
        camDir.set(this.app.getCamera().getDirection().multLocal(0.6f));
        camLeft.set(this.app.getCamera().getLeft().multLocal(0.4f));
        walkDirection.set(0, 0, 0);
        
        if (keyW){walkDirection.addLocal(camDir); }
        if (keyS){walkDirection.addLocal(camDir.negate());}
        if (keyA){walkDirection.addLocal(camLeft);}
        if (keyD){walkDirection.addLocal(camLeft.negate());}
        
        firstPersonPlayer.setWalkDirection(walkDirection);
        this.app.getCamera().setLocation(firstPersonPlayer.getPhysicsLocation());
        
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
                    case "Jump": if (keyPressed) {firstPersonPlayer.jump(new Vector3f(0,20f,0));}; break;
                    case "PauseGame": onDisable(); break;
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
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
        }

    @Override
    public void onAnimCycleDone(AnimControl arg0, AnimChannel arg1, String arg2) {
        
    }

    @Override
    public void onAnimChange(AnimControl arg0, AnimChannel arg1, String arg2) {
        
    }

    @Override
    protected void onEnable() {
        
    }

    @Override
    protected void onDisable() {
        PlayGame.attachAppState(PlayGame.paused_screen);
    }
}
