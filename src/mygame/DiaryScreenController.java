/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.InputManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class DiaryScreenController extends BaseAppState implements ScreenController{

    private SimpleApplication app;
    private InputManager inputManager;
    private Nifty nifty;
    private Screen screen;
    
    
    @Override
    protected void initialize(Application app) {
    
       this.inputManager = this.app.getInputManager();
       //inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT); //delete ESC key quit app function
       
    }

    @Override
    protected void cleanup(Application app) {
    
    
    }
    
    @Override
    protected void onEnable() {
    

     
    }
    
    @Override
    protected void onDisable() {
        
       
    }
    
    @Override
    public void update(float tpf) {
    
    
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        PlayGame.playMusic("Music/Soundtracks/RPG - The Great Collapse.ogg");
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
    public void settingsGame(){
        PlayGame.attachAppState(PlayGame.settings_screen);
        PlayGame.detachAppState(PlayGame.diary_screen);
            
        System.out.println("Game Settings button pressed...");
        //PlayGame.musicPlayer.stop();
        
    }
    
    public void showMap(){
        
        PlayGame.attachAppState(PlayGame.mapview_screen);
        PlayGame.detachAppState(PlayGame.paused_screen);
        
    }
    
    
    public String getLeftContentText(){
        String content1 = 
                "<< Abandoned Shore - Day 1 >>\n"
                + "Dear Valentine,\n"
                + "By the time you read these rows,\n"
                + "i likely am already dead.\n"
                + "Ten years ago i suddenly\n"
                + "disappeared from our house,\n"
                + "i can not even remember what \n"
                + "actually happened, i only \n"
                + "remember pain, and despair, \n"
                + "and you weren't anywhere.\n"
                + "I found myself here,\n"
                + "in a deserted beach,\n"
                + "i have to get up and walk,\n"
                + "looking for shelter and food...";
        return content1;
    }
    
    public String getRightContentText(){
        String content2 = 
                "<< Abandoned Shore - Day 2 >>\n"
                + "I found a shack near the shore.\n"
                + "It looks abandoned and weathered,\n "
                + "but will do the job. I'm still\n"
                + "very tired, as i didn't sleep\n"
                + "last night at all.\n"
                + "What could have happened to me??\n"
                + "There is some bread and roasted fish \n"
                + "on the table. I hope i won't die \n"
                + "from poisoned food.\n"
                + "Later i will look around in the shack, \n"
                + "but first of all i have to sleep a bit...";
        return content2;
    }
    
    public void backToGame(){
        System.out.println("Back to game button pressed...");
        PlayGame.detachAppState(PlayGame.diary_screen);
                
        //also calls screen's onDisable() method
                
    }
    
    public void backToMainMenu(){
        System.out.println("Back to Mainmenu button pressed...");
        PlayGame.detachAppState(PlayGame.gameplayState);
        System.exit(0);
        
    }
    

}