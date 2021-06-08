/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.texture.Image;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.NiftyControl;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private Element diaryimagepage1, diaryimagepage2;
    private NiftyImage diaryniftyimagepage1, diaryniftyimagepage2;
    
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
        
        diaryimagepage1 = screen.findElementById("Content_Image1");
        diaryimagepage2 = screen.findElementById("Content_Image2");
        
        diaryniftyimagepage1 = nifty.createImage(screen,"Interface/Images/Diary/theshore.png", true);
        diaryniftyimagepage2 = nifty.createImage(screen,"Interface/Images/Diary/beachhut.png", true);
        
        //diaryniftyimagepage1 = nifty.createImage(screen,level.getDiaryImage1, true);
        //diaryniftyimagepage2 = nifty.createImage(screen,level.getDiaryImage2, true);
    }

    @Override
    public void onStartScreen() {
        PlayGame.musicPlayer.stop();
        PlayGame.playMusic("Music/Soundtracks/RPG - The Great Collapse.ogg");
        initDiaryPages(); //starting content showing
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
    
    
    public String getContentText1() throws FileNotFoundException, IOException{
        
        String content = "";
        String readstr;
        File text = new File("src/Levels/S2M0_shore_Day1.txt");
        BufferedReader br = new BufferedReader(new FileReader(text));
        
        while((readstr = br.readLine()) != null){
                    
                content = content + "\n"+ readstr;
        }            
        //        
        return content;
    }
    
    public String getContentText2() throws FileNotFoundException, IOException{
        String content = "";
        String readstr;
        File text = new File("src/Levels/S2M0_shore_Day2.txt");
        BufferedReader br = new BufferedReader(new FileReader(text));
        
        while((readstr = br.readLine()) != null){
                    
                content = content + "\n"+ readstr;
        }            
        //        
        return content;
    }
    
    
    
    public void nextPage() throws IOException{
        PlayGame.playSoundInstance("Interface/sound/book_flip_3.ogg");
        
        screen.findNiftyControl("Content_Text1", Label.class).setText(getContentText1());
        screen.findNiftyControl("Content_Text2", Label.class).setText(getContentText2());
        diaryimagepage1.getRenderer(ImageRenderer.class).setImage(diaryniftyimagepage1);
        diaryimagepage2.getRenderer(ImageRenderer.class).setImage(diaryniftyimagepage2);
        
    }
    
    public void prevPage(){
       PlayGame.playSoundInstance("Interface/sound/book_flip_3.ogg"); 
    }
    
    public void initDiaryPages(){
        try {
            screen.findNiftyControl("Content_Text1", Label.class).setText(getContentText1());
            screen.findNiftyControl("Content_Text2", Label.class).setText(getContentText2());
        } catch (IOException ex) {
            Logger.getLogger(DiaryScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        diaryimagepage1.getRenderer(ImageRenderer.class).setImage(diaryniftyimagepage1);
        diaryimagepage2.getRenderer(ImageRenderer.class).setImage(diaryniftyimagepage2);
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