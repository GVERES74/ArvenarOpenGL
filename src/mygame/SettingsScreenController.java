/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import static mygame.PlayGame.main;

/**
 *
 * @author TE332168
 */
public class SettingsScreenController extends BaseAppState implements ScreenController {
    
    private SimpleApplication app;

    private Nifty nifty;
    private Screen screen;
    private AppSettings settings = new AppSettings(true);
    private String mainScreen;
    private DropDown dropdownRes;
    private int width, height;
    
    @Override
    protected void initialize(Application app) {
        this .app = (SimpleApplication) app;
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
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;

        this.screen = screen;
        
        dropdownRes = screen.findNiftyControl("resolutions", DropDown.class);
        dropdownRes.addItem("1366x768");
        dropdownRes.addItem("1920x1080");
    }

    @Override
    public void onStartScreen() {
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
    public void popupApplySettings(){
        
        
    }
    
    public void backToMainMenu(String nextScreen){
        System.out.println("Apply Settings button pressed...");
        applySettings();
//        nifty.gotoScreen(mainScreen);
            
    }
    
    public void changeSettings(){
        
        int res = dropdownRes.getSelectedIndex();
        
        switch(res){
            case 0: width = 1366; height = 768;
            case 1: width = 1920; height = 1080;
            default: width = 1366; height = 768;
            
        }
    }
    
    public void applySettings(){
        changeSettings();
        settings.setResolution(width, height);
    }
   
    
}
