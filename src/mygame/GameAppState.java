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
import com.jme3.light.DirectionalLight;
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
    private RigidBodyControl landScape;
            
    public CharacterControl firstPersonPlayer;
    public HUDScreenController hud;
    
    private Vector3f walkDirection = new Vector3f();
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private boolean keyA = false, keyD = false, keyW = false, keyS = false;
    
    private AnimChannel npcWalkChannel;
    private AnimControl control;
    
    S2M0_shore levelS2M0; 
    
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
        hud = new HUDScreenController();
        app.getStateManager().attach(bulletAppState);
        
        //init levels
        levelS2M0 = new S2M0_shore();
        
        //load level
        setLevel("Scenes/S2_Summerdale/S2M0_shore.j3o", levelS2M0);
                
        setCollisionPhysics();
        initKeyEvent();
        addAmbientLight();
        createPlayer();
        
    }
        
       
        public void addAmbientLight(){
        
            DirectionalLight sun = new DirectionalLight();
            sun.setDirection(new Vector3f(-0.5f, -1f, -3.0f));
            this.app.getRootNode().addLight(sun);
        }
    
        public void createPlayer(){
                /** Load a model. Uses model and texture from jme3-test-data library! */ 
        
        
            npcPlayer = (Node)this.app.getAssetManager().loadModel("Models/Oto/OtoOldAnim.j3o");
            npcPlayer.setLocalTranslation(0, 7, 0);
            //npcPlayer.scale(1f);

            this.app.getRootNode().attachChild(npcPlayer);
            control = npcPlayer.getControl(AnimControl.class);
            control.addListener(this);            

            npcWalkChannel = control.createChannel();
            npcWalkChannel.setAnim("Walk");

            firstPersonPlayer.setGravity(new Vector3f(0,-20f,0)); //fallspeed
            firstPersonPlayer.setPhysicsLocation(new Vector3f(-100,15,250));
            
        
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

        private void initKeyEvent(){
        
            this.app.getInputManager().addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
            this.app.getInputManager().addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
            this.app.getInputManager().addMapping("StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
            this.app.getInputManager().addMapping("StrafeRight", new KeyTrigger(KeyInput.KEY_D));
            this.app.getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
            this.app.getInputManager().addMapping("Crouch", new KeyTrigger(KeyInput.KEY_LCONTROL));
            this.app.getInputManager().addMapping("PauseGame", new KeyTrigger(KeyInput.KEY_ESCAPE));
            this.app.getInputManager().addMapping("MapView", new KeyTrigger(KeyInput.KEY_M));
            
            this.app.getInputManager().addMapping("lookat_target", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
            

            this.app.getInputManager().addListener(actionListener, "Forward", "Backward", "StrafeLeft", "StrafeRight", "Jump", "Crouch", "PauseGame", "MapView");
            this.app.getInputManager().addListener(analogListener, "lookat_target");
            
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
                    case "Crouch": if (keyPressed) {}
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
                    //playSoundInstance("Sounds/Human/footstep_onsnow1.wav");
                }
                else if (!keyPressed){
                    
                }
                
            }
        };
         
         
        private AnalogListener analogListener = new AnalogListener(){
        
        @Override
            public void onAnalog(String keyBinding, float value, float tpf) {
                if(keyBinding.equals("lookat_target")){
                    CollisionResults results = new CollisionResults();
                    Ray ray = new Ray(camera.getLocation(), camera.getDirection());
                    rootNode.collideWith(ray, results);
                    for (int i = 0; i < results.size(); i++) {
                   // For each "hit", we know distance, impact point, geometry.
                    float dist = results.getCollision(i).getDistance();
                    Vector3f pt = results.getCollision(i).getContactPoint();
                    target = results.getCollision(i).getGeometry().getName();
                    System.out.println("It is just a " + target);
                    hud.popupDialogBox(target);
                    }
                    
                }
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

    public void setLevel(String levelname, BaseAppState levelid) {
        
        level = this.app.getAssetManager().loadModel(levelname);
        level.setLocalTranslation(0f, 3f, 0f);
        this.app.getRootNode().attachChild(level);
        app.getStateManager().attach(levelid);
        
    }
    
    public String getTargetName(){
        return "Kivan a lóláb";
    }
    
    
}
