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
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.Tab;
import de.lessvoid.nifty.controls.TabGroup;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.HashSet;
import java.util.prefs.BackingStoreException;


/**
 *
 * @author TE332168
 */
public class SettingsScreenController extends BaseAppState implements ScreenController {
    
    private SimpleApplication app;
    private AppSettings appSettings;
    private Nifty nifty;
    private AppStateManager stateManager;
    private Screen screen;
    private GraphicsDevice device;
    private DisplayMode[] modes;
    
    private String mainScreen;
    private TabGroup tabgroup;
    private Tab focusedtab;
    private DropDown dropdownResolution;
    private DropDown dropdownBitDepth;
    private DropDown dropdownRefreshRate;
    private CheckBox checkboxFullscreen;
    private Slider sliderVol;
    private Label labelSliderVol;
    private int width, height;
    
    HashSet<DisplayMode> hashsetdmodes =new HashSet<DisplayMode>();
    
    @Override
    protected void initialize(Application app) {
        this.app = (SimpleApplication) app;
        this.stateManager = PlayGame.getPlayGameApp().getStateManager();
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
                
        initTabGroup();
        initControls();
        
    }

    @Override
    public void onStartScreen() {
        initGamePlaySettings();
        initVideoSettings();
        initAudioSettings();
        initControlSettings();
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
    
    public void initGamePlaySettings(){
        
    }
    
    
    public void initVideoSettings(){
        initDisplayDevice();
        loadAllowedDisplayModes();
    }
    
    public void initAudioSettings(){
        
    }
    
    public void initControlSettings(){
        
    }
    
    public void initDisplayDevice(){
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        modes = device.getDisplayModes();
    }
    
    public void loadAllowedDisplayModes(){
        for (DisplayMode m: modes){
            hashsetdmodes.add(m);
        }
        
        //for (DisplayMode dm: dmodes){ //for HashSet
            for (DisplayMode dm: modes){
                            
              dropdownResolution.addItem(dm.getWidth()+"x"+dm.getHeight()+"/"+dm.getBitDepth()+"bpp@"+dm.getRefreshRate()+"Hz");
              dropdownBitDepth.addItem(dm.getBitDepth());
              dropdownRefreshRate.addItem(dm.getRefreshRate());
              
              
              
        }  
              //dropdownResolution.selectItemByIndex(hashsetdmodes.size()-1);
              dropdownResolution.selectItemByIndex(modes.length-1);
              dropdownBitDepth.selectItemByIndex(0);
              dropdownRefreshRate.selectItemByIndex(0);
        
    }
    
    public void initTabGroup(){
        
        //start screen with video settings tab
        tabgroup = screen.findNiftyControl("TabGroup_Settings", TabGroup.class);
        focusedtab = screen.findNiftyControl("tab_VideoSettings", Tab.class);
        tabgroup.setSelectedTab(focusedtab);
        
    }
    
    public void initControls(){
        dropdownResolution = screen.findNiftyControl("dropdown_Resolution", DropDown.class);
        dropdownBitDepth = screen.findNiftyControl("dropdown_BitDepth", DropDown.class);
        dropdownRefreshRate = screen.findNiftyControl("dropdown_RefreshRate", DropDown.class);
        
        checkboxFullscreen = screen.findNiftyControl("cb_Fullscreen", CheckBox.class);
        
        sliderVol = screen.findNiftyControl("slider_Volume", Slider.class);
        sliderVol.setValue(50f);
        labelSliderVol = screen.findNiftyControl("label_Slider_Volume", Label.class);
        labelSliderVol.setText(sliderVol.getValue()+"%");
        
    }
    
    public void popupApplySettings(){
        
        
    }
    
     public void popupCancelSettings(){
        PlayGame.getPlayGameApp().stop();
        
    }
    
    public void backToMainMenu() throws BackingStoreException{
        System.out.println("Apply Settings button pressed...");
        applySettings();
//        nifty.gotoScreen(mainScreen);
            
    }
    
    public void changeVideoSettings(){
        
        int selectedResolution = dropdownResolution.getSelectedIndex();
        int selectedBitDepth = dropdownBitDepth.getSelectedIndex();
        int selectedRefreshRate = dropdownRefreshRate.getSelectedIndex();
        
        PlayGame.getPlayGameAppSettings().setResolution(modes[selectedResolution].getWidth(), modes[selectedResolution].getHeight());
//        PlayGame.getPlayGameAppSettings().setDepthBits(modes[selectedBitDepth].getBitDepth());
        PlayGame.getPlayGameAppSettings().setFrequency(modes[selectedRefreshRate].getRefreshRate());
        
        PlayGame.getPlayGameAppSettings().setFullscreen(checkboxFullscreen.isChecked()&& device.isFullScreenSupported());
        PlayGame.getPlayGameApp().setSettings(PlayGame.getPlayGameAppSettings());
        
    }
    
    public void changeAudioSettings(){
//        PlayGame.getMusicPlayer().setVolume(sliderVol.getValue());
    }
    
    public void applySettings() throws BackingStoreException {
        changeVideoSettings();
        saveSettings();
        PlayGame.getPlayGameApp().restart();
        
    }
   
    public void saveSettings() throws BackingStoreException{
        PlayGame.getPlayGameAppSettings().save("com/foo/ArvenarGL");
    }
}
