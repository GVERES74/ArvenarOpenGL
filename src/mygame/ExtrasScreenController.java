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
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.Tab;
import de.lessvoid.nifty.controls.TabGroup;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.File;

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
        nowPlayingTitle.setText(dropdownSelectMusic.getSelection().toString());
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
        
        File folder = new File("assets/Music/Soundtracks");
        
        File[] content = folder.listFiles();
        dropdownSelectMusic.clear(); //avoid duplication of items!!
        for (int i = 0; i < content.length; i++){
            //for (final File fileEntry : folder.listFiles()) {
                if (content[i].isFile()) {
                    dropdownSelectMusic.addItem(content[i].getName());
                    
                                        
                }
                               
        }    
        
        dropdownSelectMusic.selectItemByIndex(0);
        
    }
    
    public void playMusic(){
        
        PlayGame.musicPlayer.stop();
        PlayGame.loadMusic(musicFilesPath+dropdownSelectMusic.getSelection(), true);
        nowPlayingTitle.setText(dropdownSelectMusic.getSelection().toString());
        System.out.println("Playmusic clicked");
    }
    
    public void stopMusic(){
        
        PlayGame.musicPlayer.stop();
        System.out.println("Stopmusic clicked");
        
    }
    
    public void pauseMusic(){
        
        PlayGame.musicPlayer.pause();
        
        System.out.println("Music paused...");
        
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
    
    public void listFilesForFolder(File folder) {
    for (final File fileEntry : folder.listFiles()) {
        if (fileEntry.isFile()) {
            listFilesForFolder(fileEntry);
        } else {
            System.out.println(fileEntry.getName());
        }
    }
}
}
