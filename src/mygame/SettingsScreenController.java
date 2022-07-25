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
    private EffectsManager effectsManager;
    private Screen screen;
    private GraphicsDevice device;
    private DisplayMode[] modes;
    private DisplayMode currentMode;
    private List <DisplayMode> sorted;
    
    private String mainScreen;
    private TabGroup tabgroup;
    private Tab focusedtab;
    private DropDown dropdownResolution, dropdownBitDepth, dropdownRefreshRate;
    private DropDown dropdownShadowQuality, dropdownDOFQuality, dropdownBloomQuality;
    private CheckBox checkboxFullscreen, checkboxShowFps, checkboxShadows, checkboxDOF, checkboxSSAO, checkboxGodRays, checkboxBloom;
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
        initGameplayUiControls();
        initVideoUiControls();
        initGraphicsUiControls();
        initAudioUiControls();
        initKeyboardMouseUiControls();
        
    }

    @Override
    public void onStartScreen() {
        
        initVideoSettings();
        
        setDefaultSettings();
        
    }

    @Override
    public void onEndScreen() {
        
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
            //dropdownResolution.selectItemByIndex(0);
            dropdownResolution.selectItem(currentMode);
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
    
    public void initVideoSettings(){
        initDisplayDevice();
        loadAllowedDisplayModes();
    }
    
    
    
    public void initGameplayUiControls(){
        
        checkboxShowFps = screen.findNiftyControl("cb_ShowFps", CheckBox.class);
        checkboxShowFps.setChecked(PlayGame.displayFpsEnabled);
        
    }
    
    
    public void initVideoUiControls(){
        dropdownResolution = screen.findNiftyControl("dropdown_Resolution", DropDown.class);
        dropdownBitDepth = screen.findNiftyControl("dropdown_BitDepth", DropDown.class);
        dropdownRefreshRate = screen.findNiftyControl("dropdown_RefreshRate", DropDown.class);
        
        checkboxFullscreen = screen.findNiftyControl("cb_Fullscreen", CheckBox.class);
        checkboxFullscreen.setChecked(PlayGame.getPlayGameAppSettings().isFullscreen());
        
    }    
    
    public void initGraphicsUiControls(){
        
        checkboxShadows = screen.findNiftyControl("cb_Shadows", CheckBox.class);
        checkboxShadows.setChecked(EffectsManager.dlsf.isEnabled());
        
        dropdownShadowQuality = screen.findNiftyControl("dropdown_Shadow_Quality", DropDown.class);
        dropdownShadowQuality.setEnabled(checkboxShadows.isChecked());
        dropdownShadowQuality.addItem("High");
        dropdownShadowQuality.addItem("Medium");
        dropdownShadowQuality.addItem("Low");
        dropdownShadowQuality.selectItemByIndex(1);
        
        
        checkboxDOF = screen.findNiftyControl("cb_DOF", CheckBox.class);
        checkboxDOF.setChecked(EffectsManager.dofFilter.isEnabled());
        
        dropdownDOFQuality = screen.findNiftyControl("dropdown_DOF_Quality", DropDown.class);
        dropdownDOFQuality.setEnabled(checkboxDOF.isChecked());
        dropdownDOFQuality.addItem("High");
        dropdownDOFQuality.addItem("Medium");
        dropdownDOFQuality.addItem("Low");
        dropdownDOFQuality.selectItemByIndex(1);
        
        
        checkboxSSAO = screen.findNiftyControl("cb_SSAO", CheckBox.class);
        checkboxSSAO.setChecked(EffectsManager.ssaoFilter.isEnabled());
        
        checkboxGodRays = screen.findNiftyControl("cb_GodRays", CheckBox.class);
        checkboxGodRays.setChecked(EffectsManager.sunGodRays.isEnabled());
        
        checkboxBloom = screen.findNiftyControl("cb_Bloom", CheckBox.class);
        checkboxBloom.setChecked(EffectsManager.bloom.isEnabled());
        
        dropdownBloomQuality = screen.findNiftyControl("dropdown_Bloom_Quality", DropDown.class);
        //dropdownBloomQuality.setEnabled(checkboxBloom.isChecked());
        
        if (checkboxBloom.isChecked()) dropdownBloomQuality.enable();
        else if (!checkboxBloom.isChecked()) dropdownBloomQuality.disable();
        
        dropdownBloomQuality.addItem("High");
        dropdownBloomQuality.addItem("Medium");
        dropdownBloomQuality.addItem("Low");
        dropdownBloomQuality.selectItemByIndex(1); //medium setting is default
        
    }
        
        
    public void initAudioUiControls(){
    
        sliderMusicVol = screen.findNiftyControl("slider_MusicVolume", Slider.class);
        labelSliderMusicVol = screen.findNiftyControl("label_Slider_MusicVolume", Label.class);
        labelSliderMusicVol.setText((int)sliderMusicVol.getValue()+"%");
        
        sliderSoundVol = screen.findNiftyControl("slider_SoundVolume", Slider.class);
        labelSliderSoundVol = screen.findNiftyControl("label_Slider_SoundVolume", Label.class);
        labelSliderSoundVol.setText((int)sliderSoundVol.getValue()+"%");
        
    }
    
    public void initKeyboardMouseUiControls(){
        
    }
    
    
//CHANGE AND APPLY SETTINGS       
    
    public void changeGamePlaySettings(){
        
        PlayGame.app.setDisplayFps(checkboxShowFps.isChecked());
        PlayGame.displayFpsEnabled = checkboxShowFps.isChecked();
              
    }
    
        
    public void changeGraphicsSettings(){
        
        //Enable / Disable shadows
        
            EffectsManager.dlsf.setEnabled(checkboxShadows.isChecked());
            checkboxShadows.setChecked(checkboxShadows.isChecked());
              
                
        //Set Shadow Quality
            switch (dropdownShadowQuality.getSelectedIndex()){
                case 0: EffectsManager.dlsf.setShadowIntensity(0.6f); EffectsManager.SHADOWMAP_SIZE = 1024; EffectsManager.SHADOWMAP_NUMSAMPLES = 4;
                        
                break;
                case 1: EffectsManager.dlsf.setShadowIntensity(0.4f); EffectsManager.SHADOWMAP_SIZE = 1024; EffectsManager.SHADOWMAP_NUMSAMPLES = 3;
                        
                break;
                case 2: EffectsManager.dlsf.setShadowIntensity(0.2f); EffectsManager.SHADOWMAP_SIZE = 512; EffectsManager.SHADOWMAP_NUMSAMPLES = 1;
                        
                break;
            }
                
        //Enable / Disable Depth of Field
                    
            EffectsManager.dofFilter.setEnabled(checkboxDOF.isChecked());
            checkboxDOF.setChecked(checkboxDOF.isChecked());
            
            //Set DOF Quality
            switch (dropdownDOFQuality.getSelectedIndex()){
                case 0: EffectsManager.dofFilter.setBlurScale(0.6f);  
                break;
                case 1: EffectsManager.dofFilter.setBlurScale(0.4f); 
                break;
                case 2: EffectsManager.dofFilter.setBlurScale(0.3f); 
                break;
            }    
        
        //Enable / Disable SSAO
        
            EffectsManager.ssaoFilter.setEnabled(checkboxSSAO.isChecked());
            checkboxSSAO.setChecked(checkboxSSAO.isChecked());
        
        //Enable / Disable GodRays
        
            EffectsManager.sunGodRays.setEnabled(checkboxGodRays.isChecked());
            checkboxGodRays.setChecked(checkboxGodRays.isChecked());
        
         //Enable / Disable Bloom
                    
            EffectsManager.bloom.setEnabled(checkboxBloom.isChecked());
            checkboxBloom.setChecked(checkboxBloom.isChecked());
            
            //Set Bloom Quality
            switch (dropdownBloomQuality.getSelectedIndex()){
                case 0: EffectsManager.bloom.setExposurePower(55f);
                        EffectsManager.bloom.setBloomIntensity(2f);
                break;
                case 1: EffectsManager.bloom.setExposurePower(30f);
                        EffectsManager.bloom.setBloomIntensity(1.5f);
                break;
                case 2: EffectsManager.bloom.setExposurePower(10f);
                        EffectsManager.bloom.setBloomIntensity(1.0f);
                break;
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
    
    
    public void changeKeyboardMouseSettings(){
        
        
    }
    
    public void applySettings() throws BackingStoreException {
        changeGamePlaySettings();
        changeGraphicsSettings();
        changeVideoSettings();
        changeAudioSettings();
        changeKeyboardMouseSettings();
        saveSettings();
        
        PlayGame.getPlayGameApp().restart();
        
    }
   
    public void saveSettings() throws BackingStoreException{
        PlayGame.getPlayGameAppSettings().save("com/foo/ArvenarGL");
    }
    
    
    public void setDefaultSettings(){
        
        //DEFAULT SETTINGS FOR GAMEPLAY OPTIONS
        PlayGame.app.setDisplayFps(false);
        PlayGame.displayFpsEnabled = false;
        
        //DEFAULT SETTINGS FOR VIDEO OPTIONS
        PlayGame.getPlayGameAppSettings().setResolution(PlayGame.getPlayGameAppSettings().getWidth(), PlayGame.getPlayGameAppSettings().getHeight());
        PlayGame.getPlayGameAppSettings().setFullscreen(false);
        
        //DEFAULT SETTINGS FOR GRAPHICS OPTIONS
        EffectsManager.dlsf.setEnabled(true);
        EffectsManager.dofFilter.setEnabled(true);
        EffectsManager.ssaoFilter.setEnabled(false);
        EffectsManager.sunGodRays.setEnabled(true);
        EffectsManager.bloom.setEnabled(true);
        
        //DEFAULT SETTINGS FOR AUDIO OPTIONS
        AudioManager.soundPlayer.setVolume(0.60f);
        AudioManager.musicPlayer.setVolume(0.50f);
        
        //DEFAULT SETTINGS FOR KEYBINDING / MOUSE OPTIONS
        //.......
        
    }
    
    
    public void backToGame(){
        System.out.println("Back to game button pressed...");
        
        PlayGame.detachAppState(PlayGame.screenSettings);
        
        
    }
    
    public void backToMainMenu(){
        System.out.println("Back to main menu button pressed...");
        
        /*THIS THE IDEAL WAY!! 
        When the user clicks on the save button, the OptionsScreenAppState object
        attaches the StartScreenAppState method again, and detaches itself.
        */
        PlayGame.detachAppState(PlayGame.screenSettings);
        PlayGame.attachAppState(PlayGame.screenMainMenu);
               
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
