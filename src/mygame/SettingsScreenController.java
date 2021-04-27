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
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;


/**
 *
 * @author TE332168
 */
public class SettingsScreenController extends BaseAppState implements ScreenController {
    
    private SimpleApplication app;
    private AppSettings appSettings;
    private Nifty nifty;
    private Screen screen;
    
    private String mainScreen;
    private DropDown dropdownRes;
    private CheckBox checkboxFullscreen;
    private Slider sliderVol;
    private Label labelSliderVol;
    private int width, height;
    
    @Override
    protected void initialize(Application app) {
        this.app = (SimpleApplication) app;
                
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
          DisplayMode[] modes = device.getDisplayModes();
          int i=0; // note: there are usually several, let's pick the first
          PlayGame.getPlayGameAppSettings().setResolution(modes[i].getWidth(),modes[i].getHeight());
          PlayGame.getPlayGameAppSettings().setFrequency(modes[i].getRefreshRate());
          PlayGame.getPlayGameAppSettings().setBitsPerPixel(modes[i].getBitDepth());
          PlayGame.getPlayGameAppSettings().setFullscreen(device.isFullScreenSupported());
    }
    
    @Override
    public void update(float tpf) {
        
        labelSliderVol.setText(sliderVol.getValue()+"%");
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
                
        dropdownRes = screen.findNiftyControl("dropdown_Resolution", DropDown.class);
        dropdownRes.addItem("1366x768");
        dropdownRes.addItem("1920x1080");
        
        dropdownRes.selectItemByIndex(0);
        
        checkboxFullscreen = screen.findNiftyControl("cb_Resolution", CheckBox.class);
        
        
        sliderVol = screen.findNiftyControl("slider_Volume", Slider.class);
        labelSliderVol = screen.findNiftyControl("label_Slider_Volume", Label.class);
                sliderVol.setValue(50f);
                labelSliderVol.setText(sliderVol.getValue()+"%");
    }

    @Override
    public void onStartScreen() {
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
    public void popupApplySettings(){
        
        
    }
    
    public void backToMainMenu(){
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
        PlayGame.getPlayGameAppSettings().setResolution(width, height);
                
        
//                PlayGame.getPlayGameAppSettings().setFullscreen(true);
                PlayGame.getPlayGameApp().setSettings(PlayGame.getPlayGameAppSettings());
        
    }
    
    public void applySettings(){
        changeSettings();
        
        PlayGame.getPlayGameApp().restart();
        
        
    }
   
    
}
