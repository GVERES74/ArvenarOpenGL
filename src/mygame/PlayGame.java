package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;

import com.jme3.export.binary.BinaryImporter;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.util.TangentBinormalGenerator;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class PlayGame extends SimpleApplication implements AnimEventListener{
       
    Node npcPlayer;
    Geometry sphereGeo;
    private AudioNode levelMusic;
    private AudioNode levelAmbientSound;
        
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
       

    public static void main(String[] args) {
                
        PlayGame app = new PlayGame();
        app.start();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Arvenar 3D OpenGl");
        app.setSettings(settings);
        
    }

    @Override
    public void simpleInitApp() {
        
         /** Load a Node from a .j3o file */
                  
         BinaryImporter importer = BinaryImporter.getInstance();
         importer.setAssetManager(assetManager);
//         File file = new File("assets/Scenes/mainScene.j3o");
//         try {
//           Node loadedNode = (Node)importer.load(file);
//           loadedNode.setName("loaded node");
//           rootNode.attachChild(loadedNode);
//         } catch (IOException ex) {
//           Logger.getLogger(PlayGame.class.getName()).log(Level.SEVERE, "No saved node loaded.", ex);
//                 } 

        appState = new BulletAppState();
        stateManager.attach(appState);
        flyCam.setMoveSpeed(50);
        
        
//      createSpatial();
        createRock();
        
        //registerScene("town.zip", "main.scene");
        loadScene("Scenes/town/main.j3o");
        setCollisionPhysics();
        initKeyEvent();
        addAmbientLight();
        createPlayer();
                
    }
    
    
    
    public void addAmbientLight(){
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.3f, -0.7f, -3.0f));
        rootNode.addLight(sun);
    }
    
    public void createPlayer(){
                /** Load a model. Uses model and texture from jme3-test-data library! */ 
        
        
        npcPlayer = (Node)assetManager.loadModel("Models/Oto/OtoOldAnim.j3o");
        npcPlayer.setLocalTranslation(-25, 5, -15);
        //npcPlayer.scale(1f);
        
        rootNode.attachChild(npcPlayer);
        control = npcPlayer.getControl(AnimControl.class);
        control.addListener(this);            
        
        npcWalkChannel = control.createChannel();
        npcWalkChannel.setAnim("Walk");
        
        firstPersonPlayer.setGravity(new Vector3f(0,-30f,0));
        firstPersonPlayer.setPhysicsLocation(new Vector3f(20,10,10));
        //npcGolem.setGravity(new Vector3f(0,-30f,0));
        //npcGolem.setPhysicsLocation(new Vector3f(10,10,20));
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
                
        sphereGeo.setMaterial((Material) assetManager.loadMaterial("Materials/ShinyMaterialCustomized.j3m") );
        sphereGeo.setLocalTranslation(4, 4, -3);
        rootNode.attachChild(sphereGeo);
        
    }

    public void createSpatial(){
//        Spatial model = assetManager.loadModel("Models/Teapot/Teapot.obj");
//        Material potMaterial = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
//        model.setMaterial(potMaterial);
//        model.setLocalTranslation(2,3,2);
//        rootNode.attachChild(model);
        
        Box brick = new Box(1,1,1);
        Geometry wall = new Geometry("Box", brick);
        Material brickMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");    
        brickMat.setTexture("ColorMap",
            assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        wall.setMaterial(brickMat);
        wall.setLocalTranslation(1, 2, 2);
        rootNode.attachChild(wall);
        
    }
    
    public void showGuiText(String text){
      guiNode.detachAllChildren();
      BitmapText title = new BitmapText(assetManager.loadFont("Interface/Fonts/Default.fnt"), false);
      title.setText(text);
      title.setSize(assetManager.loadFont("Interface/Fonts/Default.fnt").getCharSet().getRenderedSize());
      title.setLocalTranslation(500, 700, 0);
      guiNode.attachChild(title);
      
    }
    
//    public void registerScene(String scenePath, String gameLevel){
//        
//        assetManager.registerLocator(scenePath, ZipLocator.class);
//        Spatial level = assetManager.loadModel(gameLevel);
//        level.setLocalTranslation(0, -5.0f, 0);
//        level.setLocalScale(2);        
//        rootNode.attachChild(level);
//    }
    
    public void loadScene(String gameLevel){
        loadAudio();
        level = assetManager.loadModel(gameLevel);
        //level.setLocalTranslation(0, 0, 0);
        level.setLocalScale(2f);        
        rootNode.attachChild(level);
        
                
    }
    
    private void loadAudio(){
        
        levelAmbientSound = new AudioNode(assetManager, "Sound/Environment/Ocean Waves.ogg", DataType.Stream); 
        levelAmbientSound.setLooping(true);
        levelAmbientSound.setPositional(true);
        levelAmbientSound.setVolume(2);
        rootNode.attachChild(levelAmbientSound);
        levelAmbientSound.play();
        
        String levelMusicTitle = "Music/Soundtracks/forest.wav";
        levelMusic = new AudioNode(assetManager, levelMusicTitle, DataType.Stream); 
        levelMusic.setLooping(true);
        levelMusic.setPositional(false);
        levelMusic.setVolume(2);
        rootNode.attachChild(levelMusic);
        levelMusic.play();
        
        showGuiText("Now playing: "+levelMusicTitle);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
//        sphereGeo.rotate(0,tpf*2,tpf);

        camDir.set(cam.getDirection().multLocal(0.6f));
        camLeft.set(cam.getLeft().multLocal(0.4f));
        walkDirection.set(0, 0, 0);
        
        if (keyW){walkDirection.addLocal(camDir); }
        if (keyS){walkDirection.addLocal(camDir.negate());}
        if (keyA){walkDirection.addLocal(camLeft);}
        if (keyD){walkDirection.addLocal(camLeft.negate());}
        
        firstPersonPlayer.setWalkDirection(walkDirection);
        cam.setLocation(firstPersonPlayer.getPhysicsLocation());
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void initKeyEvent(){
        
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("StrafeRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        
        inputManager.addListener(actionListener, "Forward", "Backward", "StrafeLeft", "StrafeRight", "Jump");
    }
    
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName){
        
        //not used
    }
    
    private final AnalogListener analogListener = new AnalogListener(){
        @Override
        public void onAnalog(String keyBinding, float value, float tpf){
            
                      
        }
        };
        
        private ActionListener actionListener = new ActionListener(){
            public void onAction (String keyBinding, boolean keyPressed, float tpf){
                switch (keyBinding){
                    case "Forward": keyW = keyPressed; break;
                    case "Backward": keyS = keyPressed; break;
                    case "StrafeLeft": keyA = keyPressed; break;
                    case "StrafeRight": keyD = keyPressed; break;
                    case "Jump": if (keyPressed) {firstPersonPlayer.jump(new Vector3f(0,20f,0));}; break;
                }
            }
        };

    
    public void onAnimCycleDone(AnimControl control, AnimChannel npcWalkChannel, String animName) {
            
//To change body of generated methods, choose Tools | Templates.
    }

   
}
