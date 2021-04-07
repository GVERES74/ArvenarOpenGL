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
import com.jme3.app.state.BaseAppState;
import com.jme3.audio.AudioNode;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

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
    
    
    @Override
    public void initialize(Application app) {
        
        this.app = (SimpleApplication) app;
        
        this.app.getRootNode().attachChild(startRootNode);
        this.app.getGuiNode().attachChild(startGUINode);
        this.app.getFlyByCamera().setEnabled(false);
        
        loadMainScene();
        createPrecipitation();
        createFirePlace();
        loadMenuMusic();
        
        createMainMenu();
        initMenuControls();
        
        
    //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //this is called on the OpenGL thread after the AppState has been attached
    }
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        rotateCamera(1,tpf,Vector3f.UNIT_Y);
        
    }
    
    @Override
    public void cleanup(Application app) {
        
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }
    
    public void loadMainScene(){
        mainScene = this.app.getAssetManager().loadModel("Scenes/MainMenu/mainMenuScene.j3o");
        this.app.getRootNode().attachChild(mainScene);
        this.app.getCamera().setLocation(new Vector3f(0,5,0));
    }
    
    public void loadMenuMusic(){
        
        mainMenuThemePlayer = new AudioNode(this.app.getAssetManager(),"Scenes/MainMenu/RPG_Ambient_2.ogg");
        mainMenuThemePlayer.setLooping(true);
        mainMenuThemePlayer.setPositional(false);
        startRootNode.attachChild(mainMenuThemePlayer);
        mainMenuThemePlayer.play();
        
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
            pemitter.setEndSize(1.5f);
            pemitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0,-20,0));
            pemitter.getParticleInfluencer().setVelocityVariation(0.5f);
            pemitter.setLocalTranslation(0, 50, 0);
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
                pemitter.setHighLife(1.0f);
                pemitter.setStartSize(0.5f);
                pemitter.setEndSize(0.1f);
                pemitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0,5,0));
                pemitter.getParticleInfluencer().setVelocityVariation(0.3f);
                pemitter.setLocalTranslation(0, 0, -15);
                pemitter.setNumParticles(200);
                pemitter.setParticlesPerSec(20);
                startRootNode.attachChild(pemitter);
            
    }
            
            
             protected void rotateCamera(float value, float tpf
			  	, Vector3f axis){
		 float rotationSpeed = -0.1f;
	          Quaternion rotate = new Quaternion().fromAngleNormalAxis(rotationSpeed * value * tpf, axis);
		  Quaternion q = rotate.mult(this.app.getCamera().getRotation());
		  this.app.getCamera().setRotation(q);
                  
            }
             
             
             public void createMainMenu(){
                this.app.setDisplayStatView(false); this.app.setDisplayFps(false);
                createMenuText("Start New Game", 50, 800);
                createMenuText("Game Options", 50, 700);
                createMenuText("Credits", 50, 600);
                createMenuText("Exit Game", 50, 500);
             }
             
             public void createMenuText(String name, float posx, float posy){
                BitmapText menuItemText = new BitmapText(this.app.getAssetManager().loadFont("Interface/Fonts/Antiqua.fnt"), false);
                menuItemText.setText(name);
                menuItemText.setSize(this.app.getAssetManager().loadFont("Interface/Fonts/Antiqua.fnt").getCharSet().getRenderedSize());
                menuItemText.setLocalTranslation(posx, posy, 0);
                menuItemText.setColor(ColorRGBA.White);
                
                startGUINode.attachChild(menuItemText);
             }
    
    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

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
                app.setDisplayStatView(true);
            }
        }
    };

   
    
}
