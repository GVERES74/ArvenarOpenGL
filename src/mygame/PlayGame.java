package mygame;

import Levels.S2M0_shore;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.audio.AudioNode;

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
   
public class PlayGame extends SimpleApplication{
       
    static PlayGame app;
    static AppSettings appsettings;
    static NiftyJmeDisplay niftyDisplay;
    private static Nifty nifty;
    
    public static GameAppState gameplayState;
    public S2M0_shore levelS1M0;
    
    public static MainMenuScreen mainMenu_screen;
    public static SettingsScreen settings_screen;
    public static CreditsScreen credits_screen;
    public static PausedScreen paused_screen;
    public static HUDScreen ingameHud;
    public static MapViewScreen mapview_screen;
    public static ExtrasScreen extras_screen;
        
    public static AudioNode musicPlayer, soundPlayer;
    
                
    public static void main(String[] args) throws BackingStoreException {
                
        app = new PlayGame();
        appsettings = new AppSettings(true);
        appsettings.setResolution(1366, 768);
        app.setSettings(appsettings);        
        appsettings.setTitle("Arvenar 3D - OpenGl");
        appsettings.setSettingsDialogImage("Interface/Images/splash.png");
        appsettings.save("ArvenarGL.cfg");
        app.setShowSettings(true); //default jMonkey settings OFF
        app.start();
        
        
    }

    @Override
    public void simpleInitApp() {
        
       
           app.setDisplayFps(false); app.setDisplayStatView(false);
           niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
           nifty = niftyDisplay.getNifty();
           viewPort.addProcessor(niftyDisplay); 
           
           mainMenu_screen = new MainMenuScreen(); //stateManager.attach(mainMenu_screen);
           settings_screen = new SettingsScreen();  //stateManager.attach(settings_screen);
           credits_screen = new CreditsScreen(); //stateManager.attach(credits_screen);
           extras_screen = new ExtrasScreen(); //stateManager.attach(extras_screen);
           paused_screen = new PausedScreen(); //stateManager.attach(paused_screen);
           ingameHud = new HUDScreen();          //stateManager.attach(ingameHud);
           mapview_screen = new MapViewScreen();
           gameplayState = new GameAppState(); stateManager.attach(gameplayState);
           
           levelS1M0 = new S2M0_shore(); //stateManager.attach(levelS1M0);


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

    public static AppSettings getPlayGameAppSettings() {
        return appsettings;
    }
    
    public static void playMusic(String filepath){
        musicPlayer = new AudioNode(app.getAssetManager(), filepath);
        musicPlayer.setDirectional(false);
        musicPlayer.setPositional(false);
        musicPlayer.setLooping(true);
        musicPlayer.stop();
        app.getRootNode().attachChild(musicPlayer);
        musicPlayer.play();
        }
    

    public static void playSound(String filepath, boolean directional, boolean positional, boolean looping, float volume, float xpos, float ypos, float zpos){
        soundPlayer = new AudioNode(app.getAssetManager(), filepath);
        soundPlayer.setDirectional(directional);
        soundPlayer.setPositional(positional);
        soundPlayer.setLooping(looping);
        soundPlayer.setVolume(volume);
        app.getRootNode().attachChild(soundPlayer);
        //audioRenderer.playSource(soundPlayer);
        soundPlayer.play();
    }
    
    public static void attachAppState(AppState appstate){
        app.getStateManager().attach(appstate);
    }
       
    public static void detachAppState(AppState appstate){
        app.getStateManager().detach(appstate);
    }
    
    public static void switchNiftyScreen(String removeid, String gotoid){
        
        nifty.removeScreen(removeid);
        nifty.gotoScreen(gotoid);
    }
    
       
}
