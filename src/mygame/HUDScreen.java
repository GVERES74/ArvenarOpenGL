/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.Label;
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
    private Label gpsInfo;
    
       
    
    
    
    
    @Override
    protected void initialize(Application app) {
        this.app = (SimpleApplication) app;
        nifty = PlayGame.nifty; 
        app.getGuiViewPort().addProcessor(PlayGame.niftyDisplay);
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
       
           
           
        //Called when the state is fully enabled, ie: is attached and         
        //isEnabled() is true or when the setEnabled() status changes after the         
        //state is attached.    
    }
    
    @Override
    protected void onDisable() {
       
       
        //Called when the state was previously enabled but is now disabled         
        //either because setEnabled(false) was called or the state is being         
        //cleaned up.    
    }
    
    @Override
    public void update(float tpf) {
       
    gpsInfo.setText("Location: \n"
            +"X:"+app.getCamera().getLocation().x+"\n"
            +"Y:"+app.getCamera().getLocation().y+"\n"
            +"Z:"+app.getCamera().getLocation().z
                    ); 
    }
    
       
    
    public void createHUDScreen(){
        
        
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
                                text(""+gpsInfo);
                                valignBottom();
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

  
}//end class
