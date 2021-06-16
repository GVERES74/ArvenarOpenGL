/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import Levels.S2M0_shore;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

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
   
        
    private Spatial level;
    private BulletAppState bulletAppState;
        
    public RigidBodyControl levelRigidBody;
    CapsuleCollisionShape capsulePlayer;        
    public CharacterControl firstPersonPlayer;
    
    
    private Vector3f walkDirection = new Vector3f();
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private boolean keyA = false, keyD = false, keyW = false, keyS = false;
    
    private AnimChannel npcWalkChannel;
    private AnimControl control;
    
    S2M0_shore levelS2M0; 
    
    private float playerHeight;
    private int playerhp = 100;
    public String target = "Valami";
    
    
    @Override
    public void initialize(Application app) {
        
        this.app = (SimpleApplication) app;   
        this.rootNode     = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.stateManager = this.app.getStateManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort     = this.app.getViewPort();
        this.camera       = this.app.getCamera();
                
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
        
        bulletAppState = new BulletAppState();
                
        app.getStateManager().attach(bulletAppState);
        
        //init levels
        levelS2M0 = new S2M0_shore();
        
        //load level
        setLevel("Scenes/S2_Summerdale/S2M0_shore.j3o", levelS2M0);
                
        setCollisionPhysics();
        initKeyEvent();

        createPlayer();
        
    }
    
        public void createPlayer(){
                /** Load a model. Uses model and texture from jme3-test-data library! */ 
        
            npcPlayer = (Node)this.app.getAssetManager().loadModel("Models/Oto/OtoOldAnim.j3o");
            npcPlayer.setLocalTranslation(0, 10, 0);
            //npcPlayer.scale(1f);

            this.app.getRootNode().attachChild(npcPlayer);
            control = npcPlayer.getControl(AnimControl.class);
            control.addListener(this);            

            npcWalkChannel = control.createChannel();
            npcWalkChannel.setAnim("Walk");

            firstPersonPlayer.setGravity(new Vector3f(0,-20f,0)); //fallspeed
            firstPersonPlayer.setPhysicsLocation(levelS2M0.getInitPlayerPosition());
            
            firstPersonPlayer.setJumpSpeed(20);
            firstPersonPlayer.setFallSpeed(30);
                    
        }
    
        public void setCollisionPhysics(){
            playerHeight = 6f;
            capsulePlayer = new CapsuleCollisionShape(1f,playerHeight,1);
                firstPersonPlayer = new CharacterControl(capsulePlayer, 0.05f);
                              
                
            CollisionShape sceneLevel = CollisionShapeFactory.createMeshShape(level); 
            levelRigidBody = new RigidBodyControl(sceneLevel,0);
                level.addControl(levelRigidBody);

        }
    
    
        public void showGuiText(String text, int posx, int posy){
      
          BitmapText title = new BitmapText(this.app.getAssetManager().loadFont("Interface/Fonts/Default.fnt"), false);
          title.setText(text);
          title.setSize(this.app.getAssetManager().loadFont("Interface/Fonts/Default.fnt").getCharSet().getRenderedSize());
          title.setLocalTranslation(posx, posy, 0);
          this.app.getGuiNode().attachChild(title);

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
            this.app.getInputManager().addMapping("OpenDiary", new KeyTrigger(KeyInput.KEY_L));
            this.app.getInputManager().addMapping("Settings", new KeyTrigger(KeyInput.KEY_F1));
            
            this.app.getInputManager().addMapping("lookat_target", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
            

            //let's separate movement, open screen and action listeners
            this.app.getInputManager().addListener(actionListener, "Forward", "Backward", "StrafeLeft", "StrafeRight", "Jump", "Crouch");
            this.app.getInputManager().addListener(actionListener, "PauseGame", "MapView", "OpenDiary", "Settings");
            this.app.getInputManager().addListener(actionListener, "lookat_target");
             
            //this.app.getInputManager().addListener(analogListener, "lookat_target");
            
        }
        
    
    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        if (playerhp < 2) showGuiText("You are dead..", 500, 650); ;
        
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
                    case "Crouch": if (keyPressed) {playerHeight = 3f;} else if (!keyPressed){playerHeight = 6f;} break;
                                                     
                    case "PauseGame":   hotKeyPressed(PlayGame.paused_screen, keyPressed); break;  
                                        
                    case "MapView":     hotKeyPressed(PlayGame.mapview_screen, keyPressed); break;  
                                        
                    case "OpenDiary":   hotKeyPressed(PlayGame.diary_screen, keyPressed); break; 
                    
                    case "Settings":   hotKeyPressed(PlayGame.settings_screen, keyPressed); break; 
                                        
                    case "lookat_target": if ((keyPressed) && PlayGame.gameplayState.isEnabled()){
                        
                        //PlayGame.ingameHud.showLookAtDialog(true,getTarget());
                        PlayGame.ingameHud.createDialogPanel(true,getTarget());
                        
                    } 
                    
                    else if (!(keyPressed) && PlayGame.gameplayState.isEnabled()){
                        PlayGame.ingameHud.createDialogPanel(false,"");
                        
                    }
                        break;                    
                }
                
                if (keyPressed) {   
                    PlayGame.playSoundInstance("Sounds/Human/walkDry.ogg");
                }
                else if (!keyPressed){
                    
                }
                
            }
        };
         
         
        private AnalogListener analogListener = new AnalogListener(){
            @Override
            public void onAnalog(String keyBinding, float value, float tpf) {
             
        }
        };
        
    
    
    @Override
    public void cleanup(Application app) {
                
        this.app.getRootNode().detachAllChildren();
        this.app.getInputManager().deleteMapping("PauseGame");
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

    public void setLevel(String levelname, BaseAppState levelid) {
        
        level = this.app.getAssetManager().loadModel(levelname);
        level.setLocalTranslation(0f, 3f, 0f);
        this.app.getRootNode().attachChild(level);
        app.getStateManager().attach(levelid);
        
    }

    public Spatial getLevel() {
        return level;
    }
        
    
    public String getTarget(){
        CollisionResults results = new CollisionResults();
            Ray ray = new Ray(camera.getLocation(), camera.getDirection());
            rootNode.collideWith(ray, results);
                if (results.size() > 0){
                    target = results.getClosestCollision().getGeometry().getName();
                }
                return target;
    }    
    
    public void hotKeyPressed(AppState appStateName, Boolean keyPressed){
        
        if (!keyPressed && !PlayGame.getPlayGameApp().getStateManager().hasState(appStateName)){
            PlayGame.attachAppState(appStateName);
            }
        else if (!keyPressed){
            PlayGame.detachAppState(appStateName);}       
    }
                
        
    
}
