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
import com.jme3.math.FastMath;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class PausedScreenController extends BaseAppState implements ScreenController{

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
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
    public void showMap(){
        
        //PlayGame.detachAppState(PlayGame.paused_screen);
        PlayGame.attachAppState(PlayGame.mapview_screen);
                
    }
    
    public void openDiary(){
               
        //PlayGame.detachAppState(PlayGame.paused_screen);
        PlayGame.attachAppState(PlayGame.diary_screen);
        
    }
    
    
    public String getMenuItemHintText(){
        String[] content = {
        
                "<< Abandoned Shore >>\n"
                + "You woke up dizzy\n"
                + "and feeling sick\n"
                + "on an abandoned shore.\n"
                + "The only thing you found\n"
                + "in the sand, was a diary,\n"
                + "written by your mother,\n"
                + "who had died long ago.\n"
                + "You felt strange and\n"
                + "were afraid of something\n"
                + "was not right here...",
                
                "<< HotKey bindings >>\n\r"
                + "F1 = Open Settings Screen\n\r"
                + "ESC = Open Paused Menu\n\r"
                + "M = Open Land Map\n\r"
                + "L = Open Diary",
                
                "<< About the Project >>\n\r"
                + "This project was originally\n\r"
                + "planned to be developed in\n\r"
                + "JavaFX. Due to the fact, that\n\r"
                + "the developer has been working\n\r"
                + "on the project alone, came the idea\n\r"
                + "to look for a game engine that not widely\n\r"
                + "used nowadays, but is easy to learn, and\n\r"
                + "still capable of create a good software\n\r"
                + "from the scratch.",
                
                "<< About the Game >>\n\r"
                + "It is always closer to a true gamer\n\r"
                + "to see and play an RPG, that is not\n\r"
                + "about cutting edge menus, that are very\n\r"
                + "beautyful for the eyes, and you can easily\n\r"
                + "lost yourself in, forgetting what was your\n\r"
                + "progress or game status 2 minutes ago.....\n\r"
                + "No. In real life you draw out a map, open a diary\n\r"
                + "and flipping its pages, and die in the world\n\r"
                + "from starving or dehydrating.",
                
                "<< Game Extras >>\n\r"
                + "There is an ogg music player\n\r"
                + "under the Game Extras menu. Go and try it."
                
        };
        
        int r = FastMath.nextRandomInt(0, content.length-1);
              
        
        return content[r];
    }
    
    public void settingsGame(){
        //PlayGame.detachAppState(PlayGame.paused_screen);
        PlayGame.attachAppState(PlayGame.settings_screen);
                
        System.out.println("Game Settings button pressed...");
        //PlayGame.musicPlayer.stop();
        
    }
    
    
    public void backToGame(){
        System.out.println("Back to game button pressed...");
        PlayGame.detachAppState(PlayGame.paused_screen);
                
        //also calls screen's onDisable() method
                
    }
    
    public void backToMainMenu(){
        System.out.println("Back to Mainmenu button pressed...");
        PlayGame.detachAppState(PlayGame.gameplayState);
        System.exit(0);
        
    }
    

}