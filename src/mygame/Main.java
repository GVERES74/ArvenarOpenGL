package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ZipLocator;

import com.jme3.export.binary.BinaryImporter;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
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
public class Main extends SimpleApplication {
       
    protected Spatial player;
    Geometry sphereGeo;
       

    public static void main(String[] args) {
                
        Main app = new Main();
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
         File file = new File("assets/Scenes/mainScene.j3o");
         try {
           Node loadedNode = (Node)importer.load(file);
           loadedNode.setName("loaded node");
           rootNode.attachChild(loadedNode);
         } catch (IOException ex) {
           Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "No saved node loaded.", ex);
                 } 

//         createSpatial();
//         createRock();
         showGuiText();
        //registerScene("town.zip", "main.scene");
        //oadScene("Scenes/town/main.j3o");
        initKeyEvent();
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
        Spatial model = assetManager.loadModel("Models/Teapot/Teapot.obj");
        Material potMaterial = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        model.setMaterial(potMaterial);
        model.setLocalTranslation(2,3,2);
        rootNode.attachChild(model);
        
        Box brick = new Box(1,1,1);
        Geometry wall = new Geometry("Box", brick);
        Material brickMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");    
        brickMat.setTexture("ColorMap",
            assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        wall.setMaterial(brickMat);
        wall.setLocalTranslation(1, 2, 2);
        rootNode.attachChild(wall);
        
        player = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
        player.setLocalTranslation(3, 2, 3);
        player.scale(0.01f);
        
        rootNode.attachChild(player);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.3f, -0.7f, -3.0f));
        rootNode.addLight(sun);
        
        
        
        
    }
    
    public void showGuiText(){
      guiNode.detachAllChildren();
      BitmapText title = new BitmapText(assetManager.loadFont("Interface/Fonts/Default.fnt"), false);
      title.setText("Welcome to jMonkeyGame");
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
        Spatial level = assetManager.loadModel(gameLevel);
        level.setLocalTranslation(0, -5.0f, 0);
        level.setLocalScale(2);        
        rootNode.attachChild(level);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
//        sphereGeo.rotate(0,tpf*2,tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void initKeyEvent(){
        
        inputManager.addMapping("ArrowLeft", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("ArrowRight", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("ArrowUp", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("ArrowDown", new KeyTrigger(KeyInput.KEY_K));
        
        inputManager.addListener(analogListener, "ArrowLeft", "ArrowRight","ArrowUp", "ArrowDown");
    }
    
        private final AnalogListener analogListener = new AnalogListener(){
        @Override
        public void onAnalog(String name, float value, float tpf){
            
            switch (name){
                case "ArrowLeft": player.rotate(0, -value*speed, 0); break;
                case "ArrowRight": player.rotate(0, value*speed, 0); break;
                case "ArrowUp": {Vector3f walk = player.getLocalTranslation();
                    player.setLocalTranslation(walk.x, walk.y, walk.z-value*speed); break;}
                    
                case "ArrowDown": {Vector3f walk = player.getLocalTranslation();
                    player.setLocalTranslation(walk.x, walk.y, walk.z+value*speed); break;}
            }
          
        }
        };
}
