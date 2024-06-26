package mygame;


import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import Levels.S0M0_valley;
import Levels.S1M0_forest;
import Levels.S2M0_shore;
import Levels.S3M0_town;




/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */ 
   
public class PlayGame extends SimpleApplication{
       
    public static PlayGame app;
    static AppSettings appsettings;
    public static NiftyJmeDisplay niftyDisplay;
    public static Nifty nifty;
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
    public static boolean isNightTime;
    
                    
    public static void main(String[] args) {
                
        app = new PlayGame();
        appsettings = new AppSettings(true);
        appsettings.setResolution(1920, 1080);
        appsettings.setFullscreen(true);

        app.setSettings(appsettings);        
        appsettings.setTitle("Arvenar 3D - OpenGl");
        appsettings.setSettingsDialogImage("Interface/Images/splash.png");
        
        app.setShowSettings(false); //default jMonkey settings ON / OFF
        app.setPauseOnLostFocus(true);
        app.start();
        
        
       
    }

    
    @Override
    public void simpleInitApp() {
        
        niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
        nifty = niftyDisplay.getNifty();
        viewPort.addProcessor(niftyDisplay); 
        
        screenMainMenu = new MainMenuScreen(); 
        stateManager.attach(screenMainMenu); //THIS THE IDEAL WAY!! 
               
        bulletAppState = new BulletAppState(); 
        app.getStateManager().attach(bulletAppState);
        
        screenGameMode = new GameModeScreen(); 
        screenLoading = new LoadingScreen(); 
                     
        gameplayAppState = new GameAppState(); 
        screenSettings = new SettingsScreen();
        screenCredits = new CreditsScreen(); 
        screenExtras = new ExtrasScreen();  
        screenPauseMenu = new PausedScreen(); 
        screenInGameHUD = new HUDScreen();       
        screenMapView = new MapViewScreen();
        screenDiary = new DiaryScreen();
        levelS2M0 = new S2M0_shore();
        levelS0M0 = new S0M0_valley(); 
        levelS1M0 = new S1M0_forest();
        levelS3M0 = new S3M0_town();
       
       
        
        /*The beauty of AppStates and controls
            The ideal jMonkeyEngine application's simpleInitApp() method would have only two
            lines of code: one that creates a custom StartScreenAppState instance, and a second
            line that attaches it to the stateManager object of the SimpleApplication class.*/
        
           
           app.setDisplayFps(false); app.setDisplayStatView(false);
              
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
    
        

        
    
}
