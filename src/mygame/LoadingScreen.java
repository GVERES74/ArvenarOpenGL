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
        
    private String lscrAssetName = "Initialize Assets..";
    private String lscrLevelName = "Initialize Level..";
    private Label lscrLabelAssetName;
    private Label lscrLabelLevelName;
    private Label lscrLabelHinttext1;
    private Label lscrLabelHinttext2;
    private Element lscrImage;
    private NiftyImage lscrRandomNiftyImage;
    
    private int lsFrameCount=0;
    private Screen screen;
    
    
    @Override
    protected void initialize(Application app) {
        
        this.app = (SimpleApplication) app;
        
        createLoadingScreen();
        
       
        
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
       
        lsFrameCount++; 
        
            lscrLabelLevelName.setText("Loading Level - "+getLoadLevelName()); 
            lscrLabelAssetName.setText("Loading asset: "+getAssetName()); 
            
            if (lsFrameCount == 500){
              lscrLabelAssetName.setText("Loading asset: "+getAssetName()); 
              lscrImage.getRenderer(ImageRenderer.class).setImage(nifty.createImage(nifty.getScreen("Screen_Loading"),loadRandomImage(), true)); 
            }
             
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
                                filename("Interface/Images/Loading/ruins.jpg");
                                height("50%");
                                width("50%");
                                alignCenter();
                                valignCenter();
                        }});
                        
                        text(new TextBuilder("text_LoadingHints1") {{
                                text(getLoadingScreenHintText1());
                                font("Interface/Fonts/Default.fnt");
                                height("30%");
                                width("80%");
                                alignCenter();
                                valignCenter();
                        }});   
                        
                        text(new TextBuilder("text_LoadingHints2") {{
                                text(getLoadingScreenHintText2());
                                font("Interface/Fonts/Default.fnt");
                                height("10%");
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
                                text("Loading Level....Done");
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
                                text("Loading Assets....Done");
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
        lscrImage = nifty.getScreen("Screen_Loading").findElementById("img_Loading");
        lscrRandomNiftyImage = nifty.createImage(nifty.getScreen("Screen_Loading"),loadRandomImage(), true);
        lscrImage.getRenderer(ImageRenderer.class).setImage(lscrRandomNiftyImage); 
        lscrLabelAssetName =  nifty.getScreen("Screen_Loading").findNiftyControl("text_LoadingAssetName", Label.class);         
        lscrLabelLevelName =  nifty.getScreen("Screen_Loading").findNiftyControl("text_isLoading", Label.class); 
    } 
     
     private String loadRandomImage(){
        
        String path = "Interface/Images/Loading";
        File folder = new File("assets/"+path);
        
        File[] content = folder.listFiles();
        int i = FastMath.nextRandomInt(0, content.length-1);
        return path+"/"+content[i].getName();
        

    }

    
    public String getAssetName() {
        return lscrAssetName;
    }

    public void setAssetName(String assetName) {
        lscrAssetName = assetName;
        assetName = "";
    }

    public String getLoadLevelName() {
        return lscrLevelName;
        
    }

    public void setLoadLevelName(String loadLevelName) {
        lscrLevelName = loadLevelName;
    }

    public String getLoadingScreenHintText1(){
        String[] content = {
        
                "<< Abandoned Shore >>\n"
                + "You woke up dizzy and feeling sick on an abandoned shore.\n"
                + "The only thing you found in the sand, was a diary,\n"
                + "written by your mother,who had died long ago.\n"
                + "You felt strange and were afraid of something was not right here...",
                
                "<< HotKey bindings >>\n\r"
                + "F1 = Open Settings Screen\n\r"
                + "ESC = Open Paused Menu\n\r"
                + "M = Open Land Map\n\r"
                + "L = Open Diary",
                
                "<< About the Project >>\n\r"
                + "This project was originally planned to be developed in\n\r"
                + "JavaFX. Due to the fact, that the developer has been working\n\r"
                + "on the project alone, came the idea to look for a game engine that not widely\n\r"
                + "used nowadays, but is easy to learn, and still capable of create a good software\n\r"
                + "from the scratch.",
                
                "<< About the Game >>\n\r"
                + "It is always closer to a true gamer to see and play an RPG, that is not\n\r"
                + "about cutting edge menus, that are very beautyful for the eyes, and you can easily\n\r"
                + "lost yourself in, forgetting what was your progress or game status 2 minutes ago.....\n\r"
                + "No. In real life you draw out a map, open a diary and flipping its pages, and die in the world\n\r"
                + "from starving or dehydrating.",
                
                "<< Game Extras >>\n\r"
                + "There is an ogg music player under the Game Extras menu. Go and try it."
                
        };
        
        int r = FastMath.nextRandomInt(0, content.length-1);
          
       return content[r]; 
        
    }
      
    public String getLoadingScreenHintText2(){
        String[] content = {
        
                "In time we hate that which we often fear.",
                "If one does not know to which port one is sailing, no wind is favorable.",
                "Fate leads the willing and drags along the unwilling.",
                "Fidelity purchased with money, money can destroy.",
                "Death is the wish of some, the relief of many, and the end of all.",
                "A great fortune is a great slavery.",
                "Retirement without the love of letters is a living burial.",
                "Where the fear is, happiness is not.",
                "Travel and change of place impart new vigor to the mind.",
                "Sadness usually results from one of the following causes either\n"
                + " when a man does not succeed, or is ashamed of his success.",
                "Every new beginning comes from some other beginning's end.",
                "The display of grief makes more demands than grief itself. How few men are sad in their own company.",
                "Most powerful is he who has himself in his own power.",
                "Night brings our troubles to the light, rather than banishes them.",
                "The mind that is anxious about future events is miserable.",
                "The worst evil of all is to leave the ranks of the living before one dies."
                
        };
        
        int r = FastMath.nextRandomInt(0, content.length-1);
          
       return content[r]; 
        
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
    
    
}//end class
