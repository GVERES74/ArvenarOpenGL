/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import static mygame.PlayGame.app;

/**
 *
 * @author TE332168
 */
public class ModelManager extends BaseAppState{
   
            
    private static RigidBodyControl modelRigidBody;
    private static Spatial model;
    
     
    public static void createModel(String modelfile, String matfile, float xpos, float ypos, float zpos, float yaw, float pitch, float scale){
            model = app.getAssetManager().loadModel(modelfile);
                                    
            if(matfile !=""){   
            model.setMaterial(app.getAssetManager().loadMaterial(matfile));
            }                    
            model.setLocalTranslation(xpos, ypos, zpos);
            model.rotate(pitch, yaw, 0);
            model.setLocalScale(scale);
            
            CollisionShape sceneModel = CollisionShapeFactory.createMeshShape(model);
            modelRigidBody = new RigidBodyControl(sceneModel,0);
            model.addControl(modelRigidBody);
            
            app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(modelRigidBody);
            app.getRootNode().attachChild(model);
        }
        
        public static void createModelRandomized(String modelfile, String matfile, int count, int minx, int maxx, float ypos, int minz, int maxz, int yaw, float pitch, float minscale, float maxscale){
                            
            
            for (int i = 0; i < count; i++){
                model = app.getAssetManager().loadModel(modelfile);
                       
                    if(matfile !=""){   
                    model.setMaterial(app.getAssetManager().loadMaterial(matfile));
                }    
                
                int x = FastMath.nextRandomInt(minx, maxx);
                int z = FastMath.nextRandomInt(minz, maxz);
                int ydeg = FastMath.nextRandomInt(0, yaw);
                float modelsize = (FastMath.nextRandomFloat()%(maxscale-minscale)+minscale);
                
                    model.setLocalTranslation(x, ypos, z);
                    model.rotate(pitch, ydeg, 0);
                    model.setLocalScale(modelsize);
                                
            
            CollisionShape sceneModel = CollisionShapeFactory.createMeshShape(model);
            modelRigidBody = new RigidBodyControl(sceneModel,0);
            model.addControl(modelRigidBody);
            
            app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(modelRigidBody);
            
            app.getRootNode().attachChild(model);
            }
        }

    @Override
    protected void initialize(Application app) {
        
                
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
    
}
