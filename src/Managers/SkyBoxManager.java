/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;

import static mygame.PlayGame.app;

import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import java.util.Random;

/**
 *
 * @author TE332168
 */
public class SkyBoxManager {
    
    private Spatial skyImg;
    private Spatial skyDDS;
    private Texture west, east, north, south, up, down;
    private Random random = new Random();
        
        
    public void loadBasicSkyBox(String skytype) {
              
        switch (skytype){
            case "sunny": {         
                        west = app.getAssetManager().loadTexture("Textures/SkyBox/Sunny/sunny1_west.png");
                        east = app.getAssetManager().loadTexture("Textures/SkyBox/Sunny/sunny1_east.png");
                        north = app.getAssetManager().loadTexture("Textures/SkyBox/Sunny/sunny1_north.png");
                        south = app.getAssetManager().loadTexture("Textures/SkyBox/Sunny/sunny1_south.png");
                        up = app.getAssetManager().loadTexture("Textures/SkyBox/Sunny/sunny1_up.png");
                        down = app.getAssetManager().loadTexture("Textures/SkyBox/Sunny/sunny1_down.png");
                        } break;
            
            case "cloudy": {        
                        west = app.getAssetManager().loadTexture("Textures/SkyBox/Cloudy/cloudy_west.png");
                        east = app.getAssetManager().loadTexture("Textures/SkyBox/Cloudy/cloudy_east.png");
                        north = app.getAssetManager().loadTexture("Textures/SkyBox/Cloudy/cloudy_north.png");
                        south = app.getAssetManager().loadTexture("Textures/SkyBox/Cloudy/cloudy_south.png");
                        up = app.getAssetManager().loadTexture("Textures/SkyBox/Cloudy/cloudy_top.png");
                        down = app.getAssetManager().loadTexture("Textures/SkyBox/Cloudy/cloudy_bottom.png");
                        } break;
                        
            case "overcast": {        
                        west = app.getAssetManager().loadTexture("Textures/SkyBox/Overcast02/OcastLeft.jpg");
                        east = app.getAssetManager().loadTexture("Textures/SkyBox/Overcast02/OcastRight.jpg");
                        north = app.getAssetManager().loadTexture("Textures/SkyBox/Overcast02/OcastFront.jpg");
                        south = app.getAssetManager().loadTexture("Textures/SkyBox/Overcast02/OcastBack.jpg");
                        up = app.getAssetManager().loadTexture("Textures/SkyBox/Overcast02/OcastUp.jpg");
                        down = app.getAssetManager().loadTexture("Textures/SkyBox/Overcast02/OcastDown.jpg");
                        } break;
                        
             case "rainstorm": {        
                        west = app.getAssetManager().loadTexture("Textures/SkyBox/Overcast01/CastLeft.jpg");
                        east = app.getAssetManager().loadTexture("Textures/SkyBox/Overcast01/CastRight.jpg");
                        north = app.getAssetManager().loadTexture("Textures/SkyBox/Overcast01/CastFront.jpg");
                        south = app.getAssetManager().loadTexture("Textures/SkyBox/Overcast01/CastBack.jpg");
                        up = app.getAssetManager().loadTexture("Textures/SkyBox/Overcast01/CastUp.jpg");
                        down = app.getAssetManager().loadTexture("Textures/SkyBox/Overcast01/CastDown.jpg");
                        } break;              
                        
                        
            case "night": {
                        west = app.getAssetManager().loadTexture("Textures/SkyBox/Night01/nightLeft.jpg");
                        east = app.getAssetManager().loadTexture("Textures/SkyBox/Night01/nightRight.jpg");
                        north = app.getAssetManager().loadTexture("Textures/SkyBox/Night01/nightFront.jpg");
                        south = app.getAssetManager().loadTexture("Textures/SkyBox/Night01/nightBack.jpg");
                        up = app.getAssetManager().loadTexture("Textures/SkyBox/Night01/nightUp.jpg");
                        down = app.getAssetManager().loadTexture("Textures/SkyBox/Night01/nightDown.jpg");
                        } break;
            
             case "morning": {
                        west = app.getAssetManager().loadTexture("Textures/SkyBox/Tropic01/TropicLeft.jpg");
                        east = app.getAssetManager().loadTexture("Textures/SkyBox/Tropic01/TropicRight.jpg");
                        north = app.getAssetManager().loadTexture("Textures/SkyBox/Tropic01/TropicFront.jpg");
                        south = app.getAssetManager().loadTexture("Textures/SkyBox/Tropic01/TropicBack.jpg");
                        up = app.getAssetManager().loadTexture("Textures/SkyBox/Tropic01/TropicUp.jpg");
                        down = app.getAssetManager().loadTexture("Textures/SkyBox/Tropic01/TropicDown.jpg");
                        } break;   
                        
             case "sunset": {
                        west = app.getAssetManager().loadTexture("Textures/SkyBox/SunSet01/SSLeft.jpg");
                        east = app.getAssetManager().loadTexture("Textures/SkyBox/SunSet01/SSRight.jpg");
                        north = app.getAssetManager().loadTexture("Textures/SkyBox/SunSet01/SSFront.jpg");
                        south = app.getAssetManager().loadTexture("Textures/SkyBox/SunSet01/SSBack.jpg");
                        up = app.getAssetManager().loadTexture("Textures/SkyBox/SunSet01/SSUp.jpg");
                        down = app.getAssetManager().loadTexture("Textures/SkyBox/SunSet01/SSDown.jpg");
                        } break;              
            
            case "snowy_mountain": {
                        west = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Maskonaive2/negx.jpg");
                        east = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Maskonaive2/posx.jpg");
                        north = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Maskonaive2/posz.jpg");
                        south = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Maskonaive2/negz.jpg");
                        up = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Maskonaive2/posy.jpg");
                        down = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Maskonaive2/negy.jpg");
                        } break;                
            
                        
            case "rocky_mountain": {
                        west = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Maskonaive3/negx.jpg");
                        east = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Maskonaive3/posx.jpg");
                        north = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Maskonaive3/posz.jpg");
                        south = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Maskonaive3/negz.jpg");
                        up = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Maskonaive3/posy.jpg");
                        down = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Maskonaive3/negy.jpg");
                        } break;     
                        
                        
            case "foresthill": {
                        west = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Teide/negx.jpg");
                        east = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Teide/posx.jpg");
                        north = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Teide/posz.jpg");
                        south = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Teide/negz.jpg");
                        up = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Teide/posy.jpg");
                        down = app.getAssetManager().loadTexture("Textures/SkyBox/Mountain/Teide/negy.jpg");
                        } break;
             
            case "africa02": {
                        west = app.getAssetManager().loadTexture("Textures/SkyBox/RandomSky/02/africa02_Left.jpg");
                        east = app.getAssetManager().loadTexture("Textures/SkyBox/RandomSky/02/africa02_Right.jpg");
                        north = app.getAssetManager().loadTexture("Textures/SkyBox/RandomSky/02/africa02_Front.jpg");
                        south = app.getAssetManager().loadTexture("Textures/SkyBox/RandomSky/02/africa02_Back.jpg");
                        up = app.getAssetManager().loadTexture("Textures/SkyBox/RandomSky/02/africa02_Up.jpg");
                        down = app.getAssetManager().loadTexture("Textures/SkyBox/RandomSky/02/africa02_Down.jpg");
                        } break;            
            
            case "africa01": {
                        west = app.getAssetManager().loadTexture("Textures/SkyBox/RandomSky/01/africa01_Left.jpg");
                        east = app.getAssetManager().loadTexture("Textures/SkyBox/RandomSky/01/africa01_Right.jpg");
                        north = app.getAssetManager().loadTexture("Textures/SkyBox/RandomSky/01/africa01_Front.jpg");
                        south = app.getAssetManager().loadTexture("Textures/SkyBox/RandomSky/01/africa01_Back.jpg");
                        up = app.getAssetManager().loadTexture("Textures/SkyBox/RandomSky/01/africa01_Up.jpg");
                        down = app.getAssetManager().loadTexture("Textures/SkyBox/RandomSky/01/africa01_Down.jpg");
                        } break;                        
                        
                        
                        
                        
                        
                        
        }    
            skyImg = SkyFactory.createSky( app.getAssetManager(), west, east, north, south, up, down );
            
            app.getRootNode().attachChild(skyImg);
    
    }
    
    public void randomSkyBox() {
              
        int rnum = random.nextInt(5);
        switch (rnum){
        
        }    
              
    }
    
    
    public void loadCubeMapSky(String ddsfile){
        
        app.getRootNode().attachChild(SkyFactory.createSky( app.getAssetManager(), "Textures/SkyBox/"+ddsfile, SkyFactory.EnvMapType.CubeMap));
        
    }
    
    public void loadSphereMapSky(String texfile){
        
        app.getRootNode().attachChild(SkyFactory.createSky( app.getAssetManager(), "Textures/SkySphere/"+texfile, SkyFactory.EnvMapType.SphereMap));
        
    }

    
}
