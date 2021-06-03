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
import com.jme3.texture.Image;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.NiftyControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.render.NiftyImage;
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
    private Element diarytextpage1, diarytextpage2;
    private NiftyImage diaryimagepage1, diaryimagepage2;
    
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
        PlayGame.musicPlayer.stop();
        PlayGame.playMusic("Music/Soundtracks/RPG - The Great Collapse.ogg");
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
    
    
    public String getContentText1(){
        String content1 = 
                "<< Abandoned Shore - Day 1 >>\n"
                + "Dear Valentine,\n"
                + "By the time you read these rows, i'm likely already far away.\n"
                + "Ten years ago i suddenly disappeared from our house,\n"
                + "i can not even remember what actually happened, i only \n"
                + "remember pain, and despair, looking for you and your brother,\n"
                + "but....but you were nowhere to be found.\n"
                + "I found myself here, on a deserted beach, alone and very tired.\n"
                + "But i have to get up and walk, looking for shelter and food...\n"
                + "Storm is coming over the ocean, and nightfall is almost here.";
        return content1;
    }
    
    public String getContentText2(){
        String content2 = 
                "<< Abandoned Shore - Day 2 >>\n"
                + "I found a shack near the shore. It looks abandoned and weathered,\n "
                + "but will do the job. I'm still very tired, as i didn't sleep last night at all.\n"
                + "What could have happened to me?? Where am I? What is this place?\n"
                + "There is some bread and roasted fish on the table. I hope i won't die \n"
                + "from poisoned food. Later i will look around in the shack, but first of all\n"
                + "i have to sleep a bit...and get myself together, tomorrow i MUST go for\n"
                + "a walk on the beach.";
        return content2;
    }
    
    public void nextPage(){
        screen.findNiftyControl("Content_Text1", Label.class).setText(getContentText1());
        screen.findNiftyControl("Content_Text2", Label.class).setText(getContentText2());
        
    }
    
    public void prevPage(){
        
    }
    
    public void settingsGame(){
        PlayGame.attachAppState(PlayGame.settings_screen);
        PlayGame.detachAppState(PlayGame.diary_screen);
            
        System.out.println("Game Settings button pressed...");
        //PlayGame.musicPlayer.stop();
        
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