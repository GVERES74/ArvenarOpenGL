package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;

import com.jme3.export.binary.BinaryImporter;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import java.util.prefs.BackingStoreException;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */ 
  //https://www.p8tech.com/jmonkey-game-logic-application-states/
 
public class PlayGame extends SimpleApplication{
       
    
    private MainMenuScreen mainMenu;
    
    private SettingsScreen settingsScreen;
    private CreditsScreen creditsScreen;
    static PlayGame app;
    static NiftyJmeDisplay niftyDisplay;
    private Nifty nifty;
            
    public static void main(String[] args) throws BackingStoreException {
                
        app = new PlayGame();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1366, 768);
        app.setSettings(settings);        
        settings.setTitle("Arvenar 3D - OpenGl");
        settings.setSettingsDialogImage("Interface/Images/splash.png");
        settings.save("ArvenarGL.cfg");
        app.setShowSettings(true); //default jMonkey settings OFF
        app.start();
        
        
    }

    @Override
    public void simpleInitApp() {
        
       niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
       nifty = niftyDisplay.getNifty();
        viewPort.addProcessor(niftyDisplay); 
        
        
        mainMenu = new MainMenuScreen(); stateManager.attach(mainMenu);
        //settingsScreen = new SettingsScreen(); stateManager.attach(settingsScreen);
        //creditsScreen = new CreditsScreen(); stateManager.attach(creditsScreen);
        



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
    
    
    public static NiftyJmeDisplay getNiftyDisplay() {
        return niftyDisplay;
    }

    public static PlayGame getPlayGameApp() {
        return app;
    }

    
   
}
