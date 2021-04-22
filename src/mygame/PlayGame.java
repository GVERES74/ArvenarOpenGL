package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;

import com.jme3.export.binary.BinaryImporter;
import com.jme3.input.controls.AnalogListener;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class PlayGame extends SimpleApplication implements AnimEventListener{
       
    private Nifty nifty;
    private static NiftyJmeDisplay niftyDisplay;
        
    public static void main(String[] args) {
                
        PlayGame app = new PlayGame();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1366, 768);
        app.setSettings(settings);        
        settings.setTitle("Arvenar 3D OpenGl");
        app.start();
        
        
    }

    @Override
    public void simpleInitApp() {
        
        niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
        nifty = niftyDisplay.getNifty();
        viewPort.addProcessor(niftyDisplay); 
        
        stateManager.attach(new MainMenuScreen());
//        stateManager.attach(new GameAppState());
//        stateManager.attach(new SettingsScreen());
//        stateManager.attach(new CreditsScreen());


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
    
    
    
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName){
        
        //not used
    }
    
    private final AnalogListener analogListener = new AnalogListener(){
        @Override
        public void onAnalog(String keyBinding, float value, float tpf){
            
                      
        }
        };
        
       

    
    public void onAnimCycleDone(AnimControl control, AnimChannel npcWalkChannel, String animName) {
            
//To change body of generated methods, choose Tools | Templates.
    }

    public static NiftyJmeDisplay getNiftyDisplay() {
        return niftyDisplay;
    }

   

   
     

   
}
