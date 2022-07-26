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
import com.jme3.math.FastMath;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.screen.Screen;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class HUDScreen extends BaseAppState {

    private SimpleApplication app;
    private Nifty nifty;
    private Screen hudscreen;
    private ListBox dialogListBox;    
    private int screenWidth, screenHeight;
    private Label gpsInfo;
    
    
    @Override
    protected void initialize(Application app) {
        
        this.app = (SimpleApplication) app;
        screenWidth = PlayGame.getPlayGameAppSettings().getWidth();
        screenHeight = PlayGame.getPlayGameAppSettings().getHeight();

        createHUDScreen();
      
    }
    
    
    @Override
    protected void cleanup(Application app) {
        
        //TODO: clean up what you initialized in the initialize method,        
        //e.g. remove all spatials from rootNode    
    }
    //onEnable()/onDisable() can be used for managing things that should     
    //only exist while the state is enabled. Prime examples would be scene     
    //graph attachment or input listener attachment.    
    @Override
    protected void onEnable() {
        gpsInfo = nifty.getScreen("Screen_HUD").findNiftyControl("cameraLocationInfo", Label.class);
        showHUDScreen();
        System.out.println(this.getClass().getName()+" enabled....."); 
           
           
        //Called when the state is fully enabled, ie: is attached and         
        //isEnabled() is true or when the setEnabled() status changes after the         
        //state is attached.    
    }
    
    @Override
    protected void onDisable() {
       
        hideHUDScreen();
        //Called when the state was previously enabled but is now disabled         
        //either because setEnabled(false) was called or the state is being         
        //cleaned up.    
    }
    
    @Override
    public void update(float tpf) {
       
    gpsInfo.setText("Location: "+app.getCamera().getLocation());       
    //TODO: implement behavior during runtime    
    }
    
       
    
    public void createHUDScreen(){
        
        nifty = PlayGame.getNiftyDisplay().getNifty();
            app.getGuiViewPort().addProcessor(PlayGame.getNiftyDisplay());
            nifty.loadStyleFile("nifty-default-styles.xml");
            nifty.loadControlFile("nifty-default-controls.xml");
                
            nifty.addScreen("Screen_HUD", new ScreenBuilder("Ingame HUD"){{
                controller(new mygame.HUDScreenController());
                //defaultFocusElement("settings_Apply");
                
                layer(new LayerBuilder("Layer_HUD"){{
                    childLayoutHorizontal();
                    
                    onStartScreenEffect(new EffectBuilder("fade") {{
                    
                    startDelay(1000);
                    effectValue("time", "0", "value", "0.0");
                    effectValue("time", "2000", "value", "1.0");
                    inherit();
                    post(false);
                    neverStopRendering(true);
                    
                    }});
                    
                    panel(new PanelBuilder("Panel_HUD_Left"){{ //left main panel
                        alignCenter();
                        height("100%");
                        width("50%");
                        //padding("10px");
                        childLayoutVertical();
                        
                        panel(new PanelBuilder("Panel_HUD_PlayerStats"){{
                        //backgroundColor("#ff02");  
                        alignCenter();                       
                        height("50%");
                        width("100%");
                        padding("10px");
                        childLayoutVertical();
                        
                        
                        panel(new PanelBuilder("Panel_HUD_PlayerImgHolder"){{
                        
                            alignLeft();
                            valignCenter();
                            childLayoutCenter();
                            image(new ImageBuilder("HUD_PlayerImg"){{
                            filename("Interface/Images/Hud/player.png");
                            alignCenter();
                            valignCenter();
                            height("96px");
                            width("70px");                          
                            }});
                            
                        }});    
                        
                        panel(new PanelBuilder("Panel_HUD_PlayerHealthInfo"){{
                        
                            alignLeft();
                            valignCenter();
                            height("10%");
                            width("50%");
                            
                            childLayoutAbsoluteInside();
                            
                            image(new ImageBuilder("HUD_PlayerHealthIcon"){{
                            filename("Interface/Images/Hud/heart.png");
                            x("5px");
                            y("5px");                          
                            height("24px");
                            width("24px");                          
                            }});
                            
                            image(new ImageBuilder("HUD_PlayerHealthBar"){{
                            filename("Interface/Images/Hud/healthbar.png");
                            x("50px");
                            y("10px");                          
                            height("10px");
                            width("200px");                          
                            }});
                            
                            image(new ImageBuilder("HUD_PlayerHealthValueBar"){{
                            filename("Interface/Images/Hud/healthvaluebar.png");
                            x("50px");
                            y("10px");                          
                            height("9px");
                            width("200px");                          
                            }});
                            
                            control(new LabelBuilder("HUD_PlayerHealthValueText"){{
                                text("");
                                alignCenter();
                                height("20%");
                                width("50%");
                            }});
                                             
                        }}); //player health info panel end
                        
                    }});    
                         panel(new PanelBuilder("Panel_HUD_MiniMap"){{
                        //backgroundColor("#ee02");  
                            alignCenter();                       
                            height("50%");
                            width("100%");
                            childLayoutCenter();
                            
                            control(new LabelBuilder("cameraLocationInfo"){{
                                text("CamLoc: ");
                                alignCenter();
                                height("20%");
                                width("50%");
                            }});
                                                        
                            image(new ImageBuilder("HUD_MinimapImg"){{
                                filename("Interface/Images/Hud/minimap_base.png");
                                alignLeft();
                                valignBottom();
                                height("250px");
                                width("250px");                          
                            }}); 
                                                       
                                                        
                         }}); //panel MiniMap end
                    
                    }}); //left main panel end
                    
                    panel(new PanelBuilder("Panel_HUD_Right"){{ //right main panel
                        alignCenter();
                        height("100%");
                        width("50%");
                        //padding("10px");
                        childLayoutVertical();
                        
                    }});   //right main panel end  
                
            }}); //layer HUD end ---------------------------------------------------------------
            
            layer(new LayerBuilder("Layer_CrossHair"){{
                    childLayoutCenter();    
                    image(new ImageBuilder("img_crosshair"){{
                            filename("Interface/Images/Hud/crosshair.png");
                            alignCenter();
                            valignCenter();
                            height("26px");
                            width("26px");
                    }});    
            }}); //layer CrossHair end    
                
            
            layer(new LayerBuilder("Layer_AssetInfo"){{
                childLayoutCenter();    
                    panel(new PanelBuilder("Panel_HUD_Dialog"){{
                        backgroundColor("#66c2");    
                            height("10%");
                            width("40%");
                            visible(false);
                            childLayoutCenter();
                            control(new LabelBuilder("dialogText"){{
                                text("");
                                font("Interface/Fonts/Default.fnt");
                                height("100%");
                                width("100%");
                                visible(false);
                                alignCenter();
                                valignCenter();
                            }});
                    }});        
            }}); //layer AssetInfo end    
            
            layer(new LayerBuilder("Layer_Dialogs"){{
                    childLayoutCenter();
                    panel(new PanelBuilder("Panel_Dialog_Container"){{
                        backgroundColor("#6602");
                        visible(false);
                        height("30%");
                        width("50%");
                        childLayoutVertical();
                        
                        panel(new PanelBuilder("Panel_Dialog_NPCDialogText"){{
                        backgroundColor("#ee02");  
                        alignCenter();
                        valignCenter();
                        height("50%");
                        width("100%");
                        padding("10px");
                        childLayoutVertical();
                            control(new LabelBuilder("Text_npcDialogText"){{
                                text("");
                                font("Interface/Fonts/Default.fnt");
                                height("100%");
                                width("100%");
                                visible(false);
                                alignLeft();
                                valignTop();
                            }});
                        }});    
                        
                        panel(new PanelBuilder("Panel_Dialog_PlayerDialogs"){{
                        //backgroundColor("#ee02");  
                        alignCenter();
                        valignCenter();
                        height("50%");
                        width("100%");
                        padding("10px");
                        
                        childLayoutHorizontal();
                        
                            image(new ImageBuilder("Image_Dialog_CharacterImage"){{
                            filename("Interface/Images/Hud/player.png");
                            alignCenter();
                            valignCenter();
                            }});
                        
                        
                            control(new ListBoxBuilder("ListBox_Dialog"){{
                            displayItems(4);
                            selectionModeSingle();
                            hideVerticalScrollbar();
                            hideHorizontalScrollbar();
                            height("100%");
                            width("80%");
                            alignRight();
                            optionalHorizontalScrollbar();
                            optionalVerticalScrollbar();
                            }}); 
                    
                    }});
                    }}); //panel Dialogs container end
        }}); //layer Dialogs end
            
    }}.build(nifty)); //screenbuilder end
                        
        nifty.gotoScreen("Screen_HUD");
                                    
    } //method end   

    
    public void showHUDScreen(){
        nifty.gotoScreen("Screen_HUD");
        app.getFlyByCamera().setDragToRotate(false);
        
    }
    
    public void hideHUDScreen(){
        nifty.removeScreen("Screen_HUD");
        app.getFlyByCamera().setDragToRotate(true);
        
    }
    
    public void decreasePlayerHealthBar(){
        int healthpoints = nifty.getScreen("Screen_HUD").findElementById("HUD_PlayerHealthValueBar").getWidth();
            nifty.getScreen("Screen_HUD").findElementById("HUD_PlayerHealthValueBar").setWidth(healthpoints-1);
            nifty.getScreen("Screen_HUD").findNiftyControl("HUD_PlayerHealthValueText", Label.class).setText(""+healthpoints);
            if (healthpoints < 10){
                nifty.getScreen("Screen_HUD").findElementById("HUD_PlayerHealthValueBar").setWidth(200);
            }
    }
    
    
    
    public void showLookAtDialog(Boolean enabled, String text){ //not used at the moment - alternative solution for dialog
       
        Picture pic = new Picture("DialogPanel");
            pic.setImage(app.getAssetManager(), "Interface/Images/Hud/dialogbox.png", true);
            pic.setWidth(600);
            pic.setHeight(100);
            pic.setPosition(screenWidth/2-300, screenHeight-200);
            
            
        
        BitmapFont dialogFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText dialogText = new BitmapText(dialogFont, false);
        dialogText.setText(text);
        dialogText.setLocalTranslation(screenWidth/2-100, screenHeight-200, 0); // position
        
            if (enabled){
                app.getGuiNode().attachChild(pic);
                app.getGuiNode().attachChild(dialogText);
            }
            else if (!enabled){
                app.getGuiNode().getChildren().clear();

            }
        
    }//not used right now - alternative solution for dialog
    
    public void createAssetInfoPanel(Boolean enabled, String text){
                
        String[] comment = {"This is just a ", "This should be a ", "This looks to be a ", "Hmm, I would say it's a ", "I'm wondering if it's not a "};
        int r = FastMath.nextRandomInt(0, comment.length-1);
        
            
                nifty.getCurrentScreen().findElementById("dialogText").setVisible(enabled);
                nifty.getCurrentScreen().findElementById("Panel_HUD_Dialog").setVisible(enabled);
                nifty.getCurrentScreen().findNiftyControl("dialogText", Label.class).setText(comment[r]+text);
            
            if (text.contains("Oto")){
                PlayGame.screenInGameHUD.showCharacterDialog();
            }
        
    }
       
    
    public void showCharacterDialog(){
            createDialogPanel();    
            app.getFlyByCamera().setDragToRotate(true);
            nifty.getCurrentScreen().findElementById("Panel_Dialog_Container").setVisible(true);
            nifty.getCurrentScreen().findElementById("Text_npcDialogText").setVisible(true);
                     
        
    }
    
    public void createDialogPanel(){
        dialogListBox = nifty.getCurrentScreen().findNiftyControl("ListBox_Dialog", ListBox.class);
        nifty.getCurrentScreen().findNiftyControl("Text_npcDialogText", Label.class).setText(PlayGame.gameplayAppState.getTarget()+": \n"
                + "Hello Stranger,\n"
                + "I'm "+ PlayGame.gameplayAppState.getTarget()+"\n"
                + "How can I help you?"
                + "This place is long forgotten and abandoned\n"
                + "You won't find here anything, but ruins.\n"
                );
            dialogListBox.clear();
            dialogListBox.addItem("I'm looking for my mother");
            dialogListBox.addItem("I'm lost, please tell me where I am");
            dialogListBox.addItem("Not now. (end conversation)");
    }
    
    
    

}//end class
