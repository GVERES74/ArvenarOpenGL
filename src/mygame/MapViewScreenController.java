/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ScrollPanel;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyMouseInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import javax.annotation.Nonnull;

/**
 *  
 *  
 *
 * @author TE332168 
 */
public class MapViewScreenController extends BaseAppState implements ScreenController{

    private Nifty nifty;
    private Screen screen;
    private ScrollPanel scrollPanelMapViewer;
    private Element panelMapViewer;
    private Element world_map, local_map, selected_map, map_zoomin, map_zoomout; 
    private Element map_up, map_down, map_left, map_right;
        
    
    @Override
    protected void initialize(Application app) {
        
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
        
    
        //TODO: implement behavior during runtime    
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        
        local_map = screen.findElementById("img_LocalMap");
        world_map = screen.findElementById("img_WorldMap");
        panelMapViewer = screen.findElementById("Panel_MapView_MapImage");  
        scrollPanelMapViewer = screen.findNiftyControl("ScrollPanel_MapView_MapHolder", ScrollPanel.class);
        map_zoomin = screen.findElementById("btn_mapview_ZoomIn");
        map_zoomout = screen.findElementById("btn_mapview_ZoomOut");
    }

    @Override
    public void onStartScreen() {
        scrollPanelMapViewer.setAutoScroll(ScrollPanel.AutoScroll.OFF); //must off to get work the scrollbars!!
        selected_map = world_map;
        local_map.setVisible(false);
        panelMapViewer.setClipChildren(true);
        scrollPanelMapViewer.getElement().setClipChildren(true);
    }

    @Override
    public void onEndScreen() {
        
    }
    
    public void showLocalMap(){
        selected_map = local_map;
        local_map.setVisible(true);
        world_map.setVisible(false);
        System.out.println("Local Map selected");
    }
    
    public void showWorldMap(){
        selected_map = world_map;
        world_map.setVisible(true);
        local_map.setVisible(false);
        System.out.println("World Map selected");
    }
    
    public void zoomCurrentMap(final Element element, @Nonnull final NiftyMouseInputEvent mwevent){
       
        int mouseWheel = mwevent.getMouseWheel();
                
        System.out.println("MouseWheel: "+mouseWheel);
            if (mouseWheel < 0){
            zoomOutMap();
            }
            if (mouseWheel > 0){
            zoomInMap();
            }
        
//       scrollPanelMapViewer.setEnabled(selected_map.getHeight() > scrollPanelMapViewer.getHeight());
     
    }
        
    public void zoomInMap(){
       setSeaMapSize(50);
//       scrollPanelMapViewer.setEnabled(selected_map.getHeight() > scrollPanelMapViewer.getHeight());
       
    }
    
    public void zoomOutMap(){
       setSeaMapSize(-50);
//       scrollPanelMapViewer.setEnabled(selected_map.getHeight() > scrollPanelMapViewer.getHeight());
       
    }
    
    public void mapUp(){
        
        scrollPanelMapViewer.setVerticalPos(scrollPanelMapViewer.getVerticalPos()-50);
      
    }
    
    public void mapDown(){
      
        scrollPanelMapViewer.setVerticalPos(scrollPanelMapViewer.getVerticalPos()+50);
           
      
    }
    
    public void mapLeft(){
      
        scrollPanelMapViewer.setHorizontalPos(scrollPanelMapViewer.getHorizontalPos()-50);
           
      
    }
    
    public void mapRight(){
      
        scrollPanelMapViewer.setHorizontalPos(scrollPanelMapViewer.getHorizontalPos()+50);
           
      
    }
    

    
    public void backToGame(){
        System.out.println("Back button pressed...");
        PlayGame.detachAppState(PlayGame.screenMapView);
        
               
    }
    
    public void setSeaMapSize(int count){
               
       selected_map.setHeight((int) (selected_map.getHeight()+count));
       selected_map.setWidth((int) (selected_map.getWidth()+count));
        if(selected_map.getHeight() < panelMapViewer.getHeight()){
           selected_map.setHeight(panelMapViewer.getHeight());
           selected_map.setWidth(panelMapViewer.getWidth());
        }   
           
        if(selected_map.getHeight() > panelMapViewer.getHeight()+500){
           selected_map.setHeight(panelMapViewer.getHeight()+500);
           selected_map.setWidth(panelMapViewer.getWidth()+500);
        }
    }
    
        
}
