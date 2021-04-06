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
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

/**
 *
 * @author TE332168
 */
public class GameAppState extends AbstractAppState implements AnimEventListener{
    
    private SimpleApplication app;
    
    Node npcPlayer;
    Geometry sphereGeo;
    private AudioNode levelMusic;
    private AudioNode levelAmbientSound;
    private AudioNode levelMiscSound;
    public String ambsound, levelmusic, levelmisc; 
    
        
    private Spatial level;
    private BulletAppState appState;
    private RigidBodyControl landScape;
    
    private CharacterControl firstPersonPlayer;
    private Vector3f walkDirection = new Vector3f();
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private boolean keyA = false, keyD = false, keyW = false, keyS = false;
    
    private AnimChannel npcWalkChannel;
    private AnimControl control;
    
    ParticleEmitter precipitationParticleEmitter;
    
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
         appState = new BulletAppState();
        stateManager.attach(appState);
               
                        
        //createRock();
        loadScene("Scenes/Taiga/sceneTaiga.j3o");
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
            npcPlayer.setLocalTranslation(-25, 5, -15);
            //npcPlayer.scale(1f);

            this.app.getRootNode().attachChild(npcPlayer);
            control = npcPlayer.getControl(AnimControl.class);
            control.addListener(this);            

            npcWalkChannel = control.createChannel();
            npcWalkChannel.setAnim("Walk");

            firstPersonPlayer.setGravity(new Vector3f(0,-30f,0));
            firstPersonPlayer.setPhysicsLocation(new Vector3f(20,10,10));
        
        }
    
        public void setCollisionPhysics(){
        
            CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(level);
            landScape = new RigidBodyControl(sceneShape,0);
            level.addControl(landScape);


            CapsuleCollisionShape capsulePlayer = new CapsuleCollisionShape(1.5f,6f,1);
                firstPersonPlayer = new CharacterControl(capsulePlayer, 0.05f);
                firstPersonPlayer.setJumpSpeed(20);
                firstPersonPlayer.setFallSpeed(30);
                appState.getPhysicsSpace().add(landScape);
                appState.getPhysicsSpace().add(firstPersonPlayer);

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
        
            precipitationParticleEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            Material precipitationMaterial = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
            precipitationMaterial.setTexture("Texture", this.app.getAssetManager().loadTexture("Effects/Explosion/flame.png"));
            precipitationParticleEmitter.setMaterial(precipitationMaterial);
            precipitationParticleEmitter.setImagesX(2);
            precipitationParticleEmitter.setImagesY(2);
            precipitationParticleEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0,-30,0));
            precipitationParticleEmitter.setLocalTranslation(0, 50, 0);
            precipitationParticleEmitter.setStartSize(0.5f);
            precipitationParticleEmitter.setEndSize(1.0f);
            precipitationParticleEmitter.setGravity(0,1,0);
            precipitationParticleEmitter.setLowLife(10f);
            precipitationParticleEmitter.setHighLife(30f);
            precipitationParticleEmitter.setNumParticles(1500);
            precipitationParticleEmitter.setParticlesPerSec(50);

            precipitationParticleEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO,100f));

            precipitationParticleEmitter.getParticleInfluencer().setVelocityVariation(0.5f);
            this.app.getRootNode().attachChild(precipitationParticleEmitter);

        }
    
        public void loadScene(String gameLevel){
        
            if (gameLevel.contains("Taiga")){ 
                ambsound = "";
                levelmusic = "Scenes/Taiga/ambient_snow1.ogg";
                levelmisc = "Scenes/Taiga/footstep_onsnow1.wav";
            }        

            level = this.app.getAssetManager().loadModel(gameLevel);

            level.setLocalScale(2f);        
            this.app.getRootNode().attachChild(level);

            showGuiText("Level: "+gameLevel, 500, 650);
             
        }
    
        private void loadAudio(){
        
            if (ambsound !=""){
            levelAmbientSound = new AudioNode(this.app.getAssetManager(), ambsound, AudioData.DataType.Stream); 
            levelAmbientSound.setLooping(true);
            levelAmbientSound.setPositional(true);
            levelAmbientSound.setVolume(2);
            this.app.getRootNode().attachChild(levelAmbientSound);
            levelAmbientSound.play();
            }

            levelMusic = new AudioNode(this.app.getAssetManager(), levelmusic, AudioData.DataType.Stream); 
            levelMusic.setLooping(false);
            levelMusic.setPositional(false);
            levelMusic.setVolume(2);
            this.app.getRootNode().attachChild(levelMusic);
            levelMusic.play();

            levelMiscSound = new AudioNode(this.app.getAssetManager(), levelmisc, AudioData.DataType.Buffer); 
            levelMiscSound.setLooping(true);
            levelMiscSound.setPositional(false);
            levelMiscSound.setVolume(2);
            this.app.getRootNode().attachChild(levelMiscSound);

            showGuiText("Now playing: "+levelmusic, 650, 700);
        }
        
        private void initKeyEvent(){
        
            this.app.getInputManager().addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
            this.app.getInputManager().addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
            this.app.getInputManager().addMapping("StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
            this.app.getInputManager().addMapping("StrafeRight", new KeyTrigger(KeyInput.KEY_D));
            this.app.getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));

            this.app.getInputManager().addListener(actionListener, "Forward", "Backward", "StrafeLeft", "StrafeRight", "Jump");
            
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
        
        precipitationParticleEmitter.setLocalTranslation(
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
                }
                
                if (keyPressed) {   
                    levelMiscSound.play();}
                else if (!keyPressed){
                    levelMiscSound.stop();
                }
                
            }
        };
         
         
        private AnalogListener analogListener = new AnalogListener(){
        
        @Override
            public void onAnalog(String keyBinding, float value, float tpf) {
            
        }
        };
        
    
    
    @Override
    public void cleanup() {
        super.cleanup();
        
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
}
