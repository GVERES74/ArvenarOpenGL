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
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.controls.Tab;
import de.lessvoid.nifty.controls.TabGroup;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Comparator;


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
    private DisplayMode currentMode;
    private List <DisplayMode> sorted;
    
    private String mainScreen;
    private TabGroup tabgroup;
    private Tab focusedtab;
    private DropDown dropdownResolution;
    private DropDown dropdownBitDepth;
    private DropDown dropdownRefreshRate;
    private CheckBox checkboxFullscreen, checkboxShowFps;
    private Slider sliderMusicVol, sliderSoundVol;
    private Label labelSliderMusicVol, labelSliderSoundVol;
    private int width, height;
    
        
    @Override
    protected void initialize(Application app) {
        this.app = (SimpleApplication) app;
        this.stateManager = PlayGame.getPlayGameApp().getStateManager();
    }
    
    @Override
    public void update(float tpf) {
        
        labelSliderMusicVol.setText(sliderMusicVol.getValue()+"%");
        labelSliderSoundVol.setText(sliderSoundVol.getValue()+"%");
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
        currentMode = device.getDisplayMode();
    }
    
    public void loadAllowedDisplayModes(){
              
        dropdownResolution.clear();
        
            try {
            
                sorted = new ArrayList<DisplayMode>();
                
                    for (int i=0; i<modes.length; i++) {
                        DisplayMode mode = modes[i];
                              
                        if (mode.getWidth() >= 1366 && mode.getHeight() >= 768) {
                            sorted.add(mode);
                        }
                    
                    }

                  Collections.sort(sorted, new Comparator<DisplayMode>() {
                    @Override
                    public int compare(DisplayMode o1, DisplayMode o2) {
                      int widthCompare = Integer.valueOf(o1.getWidth()).compareTo(Integer.valueOf(o2.getWidth()));
                      if (widthCompare != 0) {
                        return widthCompare;
                      }
                      int heightCompare = Integer.valueOf(o1.getHeight()).compareTo(Integer.valueOf(o2.getHeight()));
                      if (heightCompare != 0) {
                        return heightCompare;
                      }
                      return o1.toString().compareTo(o2.toString());
                    }
                  });

      
              for (DisplayMode mode : sorted) {
                dropdownResolution.addItem(mode);
              }
            } catch (Exception e) {
            }
            
            dropdownResolution.selectItemByIndex(sorted.size()-1);
            dropdownBitDepth.addItem(currentMode.getBitDepth());
            dropdownBitDepth.selectItemByIndex(0);
            dropdownRefreshRate.addItem(currentMode.getRefreshRate());
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
        checkboxFullscreen.setChecked(PlayGame.getPlayGameAppSettings().isFullscreen());
        checkboxShowFps = screen.findNiftyControl("cb_ShowFps", CheckBox.class);
        
        sliderMusicVol = screen.findNiftyControl("slider_MusicVolume", Slider.class);
        labelSliderMusicVol = screen.findNiftyControl("label_Slider_MusicVolume", Label.class);
        labelSliderMusicVol.setText((int)sliderMusicVol.getValue()+"%");
        
        sliderSoundVol = screen.findNiftyControl("slider_SoundVolume", Slider.class);
        labelSliderSoundVol = screen.findNiftyControl("label_Slider_SoundVolume", Label.class);
        labelSliderSoundVol.setText((int)sliderSoundVol.getValue()+"%");
        
    }
    
    public void popupApplySettings() throws BackingStoreException{
        
        applySettings();
    }
    
     public void popupCancelSettings(){
                
    }
     
    public void backToGame(){
        System.out.println("Back to game button pressed...");
        
        PlayGame.detachAppState(PlayGame.settings_screen);
        
        
    }
    
    public void backToMainMenu(){
        System.out.println("Back to main menu button pressed...");
        
        /*THIS THE IDEAL WAY!! 
        When the user clicks on the save button, the OptionsScreenAppState object
        attaches the StartScreenAppState method again, and detaches itself.
        */
        PlayGame.detachAppState(PlayGame.settings_screen);
        PlayGame.attachAppState(PlayGame.mainMenu_screen);
        
        
                
    }
    
    public void changeGamePlaySettings(){
        
        if (checkboxShowFps.isChecked()){
            PlayGame.app.setDisplayFps(true);
        }
        
        else if (!checkboxShowFps.isChecked()){
            PlayGame.app.setDisplayFps(false);
        }
    }
    
    public void changeVideoSettings(){
        
        int selectedResolution = dropdownResolution.getSelectedIndex();
        int selectedBitDepth = dropdownBitDepth.getSelectedIndex();
        int selectedRefreshRate = dropdownRefreshRate.getSelectedIndex();
        
        PlayGame.getPlayGameAppSettings().setResolution(sorted.get(selectedResolution).getWidth(), sorted.get(selectedResolution).getHeight());
//        PlayGame.getPlayGameAppSettings().setDepthBits(modes[selectedBitDepth].getBitDepth());
        PlayGame.getPlayGameAppSettings().setFrequency(currentMode.getRefreshRate());
        
        PlayGame.getPlayGameAppSettings().setFullscreen(checkboxFullscreen.isChecked()&& device.isFullScreenSupported());
        PlayGame.getPlayGameApp().setSettings(PlayGame.getPlayGameAppSettings());
        
    }
    
    public void changeAudioSettings(){
        AudioManager.musicPlayer.setVolume(sliderMusicVol.getValue());
        AudioManager.soundPlayer.setVolume(sliderSoundVol.getValue());
    }
    
    public void applySettings() throws BackingStoreException {
        changeGamePlaySettings();
        changeVideoSettings();
        saveSettings();
        
        PlayGame.getPlayGameApp().restart();
        
    }
   
    public void saveSettings() throws BackingStoreException{
        PlayGame.getPlayGameAppSettings().save("com/foo/ArvenarGL");
    }
    
    @NiftyEventSubscriber(id="slider_MusicVolume")
    public void onMusicVolumeSliderChanged(final String id, @Nonnull final SliderChangedEvent sliderChangedEvent) {
                       
        labelSliderMusicVol.setText((int)sliderMusicVol.getValue()+"%");
        AudioManager.musicPlayer.setVolume(sliderMusicVol.getValue()/100); //values 0.0f - 1.0f !!
        
    }
    
     @NiftyEventSubscriber(id="slider_SoundVolume")
    public void onSoundVolumeSliderChanged(final String id, @Nonnull final SliderChangedEvent sliderChangedEvent) {
                      
        labelSliderSoundVol.setText((int)sliderSoundVol.getValue()+"%");
        AudioManager.soundPlayer.setVolume(sliderSoundVol.getValue()/100); //values 0.0f - 1.0f !!
    }
}
