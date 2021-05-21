/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.Tab;
import de.lessvoid.nifty.controls.TabGroup;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class ExtrasScreenController extends BaseAppState implements ScreenController{

    private SimpleApplication app;
    private Nifty nifty;
    private AppStateManager stateManager;
    private Screen screen;
        
    private TabGroup tabgroup;
    private Tab focusedtab;
    private DropDown dropdownSelectMusic;
    
    private Label labelSelectMusic;
    private String musicFilesPath = "Music/Soundtracks/";
    private Label nowPlayingTitle;    
    
    @Override
    protected void initialize(Application app) {
    
        this.app = (SimpleApplication) app;
        this.stateManager = PlayGame.getPlayGameApp().getStateManager();
        //It is technically safe to do all initialization and cleanup in the         
        //onEnable()/onDisable() methods. Choosing to use initialize() and         
        //cleanup() for this is a matter of performance specifics for the         
        //implementor.        
        //TODO: initialize your AppState, e.g. attach spatials to rootNode 
        
        
    }

    @Override
    protected void cleanup(Application app) {
    
    
    
        //e.g. remove all spatials from rootNode    
    }

    //onEnable()/onDisable() can be used for managing things that should     
    //only exist while the state is enabled. Prime examples would be scene     
    //graph attachment or input listener attachment.    
    
    @Override
    public void update(float tpf) {
    
        
    
        //TODO: implement behavior during runtime    
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
       
        initExtrasScreenControls();
        initTabGroup();
    }

    @Override
    public void onStartScreen() { //itt kell inicializálni a screen-t!! Különben pl. üres ListBox-ot kapsz.
        loadMusicFiles();
        System.out.println("Init Extras done.....");
                
    }

    @Override
    public void onEndScreen() {
        
    }
    
    private void initExtrasScreenControls(){
        dropdownSelectMusic = screen.findNiftyControl("dropdown_MusicTheme", DropDown.class);
        nowPlayingTitle = screen.findNiftyControl("text_PlayingTitle", Label.class);
    }
    
    public void initTabGroup(){
        
        //start screen with video settings tab
        tabgroup = screen.findNiftyControl("TabGroup_Extras", TabGroup.class);
        focusedtab = screen.findNiftyControl("tab_Extras_MPlayer", Tab.class);
        tabgroup.setSelectedTab(focusedtab);
        
    }
    
    private void loadMusicFiles(){
        System.out.println("Music list loaded.....");
        String[] oggfiles = {
                "RPG_Ambient_4.ogg",
                "RPG - Mabels Crystal.ogg",
                "RPG - The Great Collapse.ogg",
                "RPG - The Path to Agartha (FadeOut).ogg",
                "RPG - The Path to Agartha (Loopable).ogg",
                "The Last Sylph.ogg",
                "RPG_-_Misty_Mountains.ogg",
                "RPG_Ambient_3.ogg",
                "RPG - The Mysterious Companion.ogg",
                "RPG_Ambient_4_The_Dark_Wood_.ogg",
                "RPG_Title_1.ogg",
                "RPG_Never_Go_Full_Bard.ogg",
                "RPG - A Long Way From Home.ogg",
                "Audience.ogg",
                "Loop_Kings_Feast.wav",
                "RPG - The Secret Within The Silent Woods.ogg",
                "Peaceful_Place.ogg",
                "RPG_Village_1.ogg",
                "RPG_Ambient_2.ogg",
                "RPG_For_Wenches_Ale_and_LOOT.ogg",
                "RPG_The_Lost_Town.ogg",
                "ambient_snow1.ogg",
                "forest.wav"};
        
        for (String s: oggfiles){
        dropdownSelectMusic.addItem(s);}
        
        dropdownSelectMusic.selectItemByIndex(0);
        
    }
    
    public void playMusic(){
        
        PlayGame.musicPlayer.stop();
        PlayGame.playMusic(musicFilesPath+dropdownSelectMusic.getSelection());
        nowPlayingTitle.setText(dropdownSelectMusic.getSelection().toString());
        System.out.println("Playmusic clicked");
    }
    
    public void stopMusic(){
        
        PlayGame.musicPlayer.stop();
        System.out.println("Stopmusic clicked");
        
    }
    
    
     public void backToMainMenu(){
        System.out.println("Back button pressed...");
        
        PlayGame.detachAppState(PlayGame.extras_screen);
        PlayGame.attachAppState(PlayGame.mainMenu_screen);
        
        
        //PlayGame.musicPlayer.stop();
            
    }
     
    

    @Override
    protected void onEnable() {
        
    }

    @Override
    protected void onDisable() {
        
    }
}
