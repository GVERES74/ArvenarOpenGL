/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.water.SimpleWaterProcessor;

/**
 *
 * @author TE332168
 */
public class WaterCreator extends AbstractAppState{
    
    SimpleApplication app;
    
    public void initialize(Application app) {
    
        this.app = (SimpleApplication) app;
    
    }

    public void createSimpleWater(Spatial scene, Node root, float width, float depth, float posx, float posy, float posz){
            
            SimpleWaterProcessor waterCreator = new SimpleWaterProcessor(this.app.getAssetManager());
                                 waterCreator.setReflectionScene(scene);
            
            Vector3f waterLocation = new Vector3f(0,-6,0);
            
            waterCreator.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
            this.app.getViewPort().addProcessor(waterCreator);
            waterCreator.setWaterDepth(40);
            waterCreator.setDistortionScale(0.05f);
            waterCreator.setWaveSpeed(0.05f);
                                    
            Geometry watergeom = waterCreator.createWaterGeometry(width, depth);
                     watergeom.setLocalTranslation(posx, posy, posz);
                     watergeom.setShadowMode(RenderQueue.ShadowMode.Receive);
                     watergeom.setMaterial(waterCreator.getMaterial());
                     root.attachChild(watergeom);
    }                 

}