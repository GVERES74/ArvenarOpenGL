package mygame;

import Levels.S0M0_valley;
import Levels.S1M0_forest;
import Levels.S2M0_shore;
import Levels.S3M0_town;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.bullet.BulletAppState;

import com.jme3.export.binary.BinaryImporter;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.prefs.BackingStoreException;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */ 
   
public class PlayGame extends SimpleApplication{
       
    public static PlayGame app;
    static AppSettings appsettings;
    static NiftyJmeDisplay niftyDisplay;
    private static Nifty nifty;
        
    public static GameAppState gameplayAppState;
    public static BulletAppState bulletAppState;
    public static S2M0_shore levelS2M0; 
    public static S0M0_valley levelS0M0;
    public static S1M0_forest levelS1M0;
    public static S3M0_town levelS3M0;
       
        
    public static MainMenuScreen screenMainMenu;
    public static SettingsScreen screenSettings;
    public static CreditsScreen screenCredits;
    public static PausedScreen screenPauseMenu;
    public static HUDScreen screenInGameHUD;
    public static MapViewScreen screenMapView;
    public static ExtrasScreen screenExtras;
    public static DiaryScreen screenDiary;
    public static GameModeScreen screenGameMode;
    public static LoadingScreen screenLoading;
    
    public static boolean displayFpsEnabled;
    
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4);
    Future future = null; 
                
    public static void main(String[] args) throws BackingStoreException {
                
        app = new PlayGame();
        appsettings = new AppSettings(true);
        appsettings.setResolution(1366, 768);
        app.setSettings(appsettings);        
        appsettings.setTitle("Arvenar 3D - OpenGl");
        appsettings.setSettingsDialogImage("Interface/Images/splash.png");
        appsettings.save("ArvenarGL.cfg");
        app.setShowSettings(false); //default jMonkey settings ON / OFF
        app.setPauseOnLostFocus(true);
        app.start();
        
        loadLastSettings(); //load game settings, options, etc.
    }

    
    @Override
    public void simpleInitApp() {
        
        
        /*The beauty of AppStates and controls
            The ideal jMonkeyEngine application's simpleInitApp() method would have only two
            lines of code: one that creates a custom StartScreenAppState instance, and a second
            line that attaches it to the stateManager object of the SimpleApplication class.*/
        
           
           bulletAppState = new BulletAppState();
           app.getStateManager().attach(bulletAppState);
           
           app.setDisplayFps(false); app.setDisplayStatView(false);
           
           niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
           nifty = niftyDisplay.getNifty();
           viewPort.addProcessor(niftyDisplay); 
           
           screenMainMenu = new MainMenuScreen(); 
           stateManager.attach(screenMainMenu); //THIS THE IDEAL WAY!! 
           
           
           //THESE ARE ONLY FOR UNIT / MODUL TESTING
           screenGameMode = new GameModeScreen(); //stateManager.attach(gameMode_screen);
           screenLoading = new LoadingScreen(); //stateManager.attach(game_loadscreen);
                     
           gameplayAppState = new GameAppState(); //stateManager.attach(gameplayState);
           screenSettings = new SettingsScreen();//stateManager.attach(settings_screen);
           screenCredits = new CreditsScreen(); //stateManager.attach(credits_screen);
           screenExtras = new ExtrasScreen();  //stateManager.attach(extras_screen);
           screenPauseMenu = new PausedScreen(); //stateManager.attach(paused_screen);
           screenInGameHUD = new HUDScreen();       //stateManager.attach(ingameHud);
           
           screenMapView = new MapViewScreen();
           screenDiary = new DiaryScreen();    //stateManager.attach(diary_screen);
            
           initGameLevels();
                        
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
        
        /*The beauty of AppStates and controls
        The ideal jMonkeyEngine application has an empty simpleUpdate() method in its main
        class—all entity behavior would be neatly modularized and encapsulated in controls and
        AppState objects.*/   

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

    public static AppSettings getPlayGameAppSettings() {
        return appsettings;
    }
    
       
    public static void attachAppState(AppState appstate){
        app.getStateManager().attach(appstate);
    }
       
    public static void detachAppState(AppState appstate){
        app.getStateManager().detach(appstate);
    }
    
    public static void loadLastSettings() throws BackingStoreException{
                appsettings.load("com/foo/ArvenarGL");
    } 
    
    public void initGameLevels(){
        levelS2M0 = new S2M0_shore();
        levelS0M0 = new S0M0_valley(); 
        levelS1M0 = new S1M0_forest();
        levelS3M0 = new S3M0_town();
    }
}
