/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.FastMath;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.File;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class LoadingScreen extends BaseAppState implements ScreenController{

    private SimpleApplication app;
    private Nifty nifty;
        
    private String assetName = "Asset Name";
    private String loadLevelName = "Level";
    private Label labelAssetName;
    private Element img_Loading;
    private NiftyImage randomLoadingImage;
    private Label textLoadingLevel;
    private int frameCount=0;
    
    
    @Override
    protected void initialize(Application app) {
        
        this.app = (SimpleApplication) app;
        
        createLoadingScreen();
        labelAssetName =  nifty.getScreen("Screen_Loading").findNiftyControl("text_LoadingAssetName", Label.class);         
        textLoadingLevel =  nifty.getScreen("Screen_Loading").findNiftyControl("text_isLoading", Label.class);         
      
    }
    
    
    @Override
    protected void cleanup(Application app) {
        System.out.println(this.nifty.getCurrentScreen().getScreenId()+" screen cleanup called.....");
        //TODO: clean up what you initialized in the initialize method,        
        //e.g. remove all spatials from rootNode    
    }
    //onEnable()/onDisable() can be used for managing things that should     
    //only exist while the state is enabled. Prime examples would be scene     
    //graph attachment or input listener attachment.    
    @Override
    protected void onEnable() {
        showLoadingScreen(); 
        initUIControls();
        System.out.println(this.getClass().getName()+" enabled....."); 
           
        //Called when the state is fully enabled, ie: is attached and         
        //isEnabled() is true or when the setEnabled() status changes after the         
        //state is attached.    
    }
    
    @Override
    protected void onDisable() {
       
        hideLoadingScreen();
        //Called when the state was previously enabled but is now disabled         
        //either because setEnabled(false) was called or the state is being         
        //cleaned up.    
    }
    
    @Override
    public void update(float tpf) {
       
        frameCount++;
        
        textLoadingLevel.setText("LOADING LEVEL - "+getLoadLevelName()); 
        labelAssetName.setText("Loading asset: "+getAssetName()); 
                
             
       //System.out.println("Loading asset: "+getAssetName());  
    }
    
    public static void attachGameAppstate(){    
        
        PlayGame.detachAppState(PlayGame.screenGameMode);
        PlayGame.attachAppState(PlayGame.gameplayAppState);
                
    }    
       
    
    public void createLoadingScreen(){
        
        nifty = PlayGame.getNiftyDisplay().getNifty();
            app.getGuiViewPort().addProcessor(PlayGame.getNiftyDisplay());
            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
                
            nifty.addScreen("Screen_Loading", new ScreenBuilder("LoadingScreen"){{
                controller(new DefaultScreenController());
                                
                layer(new LayerBuilder("Layer_Loading_BackgroundImage"){{
                        childLayoutCenter();
                        image(new ImageBuilder("img_Loading_Background") {{
                            filename("Interface/Images/bkg_pirate_table.jpg");
                            height("100%");
                            width("100%");
                        }});
                }}); //end layer bkgimage
                
                layer(new LayerBuilder("Layer_Loading_Background"){{
                    childLayoutVertical();                        
                    
                    panel(new PanelBuilder("Panel_Loading_Background") {{
                        //backgroundColor("#ccf6");
                        childLayoutVertical();
                        height("75%");
                        width("100%");
                        padding("20px");
                                           
                        image(new ImageBuilder("img_Loading") {{
                                filename("Interface/Images/Levels/S1/ruins.jpg");
                                height("50%");
                                width("50%");
                                alignCenter();
                                valignCenter();
                        }});
                        
                        text(new TextBuilder("text_LoadingHints") {{
                                text("Here comes the hint text for the selected map while loading");
                                font("Interface/Fonts/Default.fnt");
                                height("40%");
                                width("80%");
                                alignCenter();
                                valignCenter();
                        }});        
                        
                    }}); //end panel for image + text
                    
                    panel(new PanelBuilder("Panel_Loading_LoadingTextHolder") {{
                        childLayoutCenter();
                        //backgroundColor("#fff6");
                        height("10%");
                        width("100%");    
                    
                        control(new LabelBuilder("text_isLoading") {{
                                text("LOADING LEVEL - LEVEL");
                                font("Interface/Fonts/verdana-48-regular.fnt");
                                height("100%");
                                width("100%");
                                alignCenter();
                                valignCenter();
                                onActiveEffect(new EffectBuilder("colorPulsate") {{
                                   
                                    effectParameter("startColor", "#fff1");
                                    effectParameter("endColor", "#fff9");
                                    effectParameter("pulsateType", "sin");
                                    effectParameter("period", "2000");
                                    post(false);
                                    neverStopRendering(true);
                                    effectParameter("length", "infinite");
                                }});        
                        }});  // text end    
                    }});  // panel for loadingtext end 
                    
                    
                    panel(new PanelBuilder("Panel_Loading_LoadingAssetName") {{
                        childLayoutCenter();
                        //backgroundColor("#fff6");
                        height("15%");
                        width("100%");    
                    
                        control(new LabelBuilder("text_LoadingAssetName") {{
                                text("Loading..");
                                font("Interface/Fonts/Default.fnt");
                                height("50%");
                                width("100%");
                                alignCenter();
                                valignTop();
                    
                    }});  // assetname text end    
                    }});  // panel for loading assetname text end    
                      
                }}); //layer for laodingscreen end   
            
    }}.build(nifty)); //screenbuilder end
                        
        nifty.gotoScreen("Screen_Loading");
                                    
    } //method end   

    
    public void showLoadingScreen(){
        nifty.gotoScreen("Screen_Loading");
        app.getFlyByCamera().setDragToRotate(false);
                
    }
    
    public void hideLoadingScreen(){
        nifty.removeScreen("Screen_Loading");
        app.getFlyByCamera().setDragToRotate(true);
        
    }
    
         
    public void initUIControls(){
        img_Loading = nifty.getScreen("Screen_Loading").findElementById("img_Loading");
        randomLoadingImage = nifty.createImage(nifty.getScreen("Screen_Loading"),loadRandomImage(), true);
        img_Loading.getRenderer(ImageRenderer.class).setImage(randomLoadingImage); 
    } 
     
     private String loadRandomImage(){
        
        String path = "Interface/Images/Levels/S1";
        File folder = new File("assets/"+path);
        
        File[] content = folder.listFiles();
        int i = FastMath.nextRandomInt(0, content.length-1);
        return path+"/"+content[i].getName();
        

    }

    
    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getLoadLevelName() {
        return loadLevelName;
    }

    public void setLoadLevelName(String loadLevelName) {
        this.loadLevelName = loadLevelName;
    }

      
     
    @Override
    public void bind(Nifty nifty, Screen screen) {
        
    }

    @Override
    public void onStartScreen() {
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
    
}//end class
