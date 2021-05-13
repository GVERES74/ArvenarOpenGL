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
import de.lessvoid.nifty.layout.align.VerticalAlign;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

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
    private Element world_map, local_map, selected_map, map_zoomin, map_zoomout; 
    private Element map_up, map_down, map_left, map_right;
    
    
    @Override
    protected void initialize(Application app) {
    
        
        //It is technically safe to do all initialization and cleanup in the         
        //onEnable()/onDisable() methods. Choosing to use initialize() and         
        //cleanup() for this is a matter of performance specifics for the         
        //implementor.        
        //TODO: initialize your AppState, e.g. attach spatials to rootNode    
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
        scrollPanelMapViewer = screen.findNiftyControl("ScrollPanel_MapView_MapHolder", ScrollPanel.class);
        map_zoomin = screen.findElementById("btn_mapview_ZoomIn");
        map_zoomout = screen.findElementById("btn_mapview_ZoomOut");
    }

    @Override
    public void onStartScreen() {
        scrollPanelMapViewer.getElement().addChild(local_map); //add local map to the screen
        scrollPanelMapViewer.getElement().addChild(world_map); //add world map to the screen
        local_map.setVisible(false); //hide local map until it is selected
        selected_map = world_map;
        
        scrollPanelMapViewer.setEnabled(false);
                        
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
        
        local_map.setVisible(false);
        world_map.setVisible(true);
        System.out.println("World Map selected");
    }
    
        
    public void zoomInMap(){
       selected_map.setHeight((int) (selected_map.getHeight()+50));
       selected_map.setWidth((int) (selected_map.getWidth()+50));
       
       scrollPanelMapViewer.setEnabled(selected_map.getHeight() > scrollPanelMapViewer.getHeight());
       
       System.out.println("Map zoom in");
    }
    
    public void zoomOutMap(){
       if (selected_map.getHeight()>= scrollPanelMapViewer.getHeight()){
       selected_map.setHeight((int)(selected_map.getHeight()-50));
       selected_map.setWidth((int) (selected_map.getWidth()-50));
       scrollPanelMapViewer.setEnabled(selected_map.getHeight() > scrollPanelMapViewer.getHeight());
       }
        
        System.out.println("Map zoom out");
    }
    
    public void mapUp(){
       //if (selected_map.getHeight()>= scrollPanelMapViewer.getHeight()){
        scrollPanelMapViewer.setVerticalPos(scrollPanelMapViewer.getVerticalPos()-10);
           
       //}
    }
    
    public void mapDown(){
       //if (selected_map.getHeight()>= scrollPanelMapViewer.getHeight()){
        scrollPanelMapViewer.setVerticalPos(scrollPanelMapViewer.getVerticalPos()+10);
           
       //}
    }
    
    public void mapLeft(){
       //if (selected_map.getHeight()>= scrollPanelMapViewer.getHeight()){
        scrollPanelMapViewer.setHorizontalPos(scrollPanelMapViewer.getHorizontalPos()-10);
           
       //}
    }
    
    public void mapRight(){
       //if (selected_map.getHeight()>= scrollPanelMapViewer.getHeight()){
        scrollPanelMapViewer.setHorizontalPos(scrollPanelMapViewer.getHorizontalPos()+10);
           
       //}
    }
    
    public void backToGame(){
        System.out.println("Back button pressed...");
        
        PlayGame.detachAppState(PlayGame.mapview_screen);
        
        //also calls screen's onDisable() method
                
    }
}
