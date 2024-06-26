/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;
import mygame.PlayGame;


/**
 *
 * @author TE332168
 */
public class ModelManager extends BaseAppState{
   
    
    public RigidBodyControl modelRigidBody;
    private Spatial model;
    private Node aniModel;
    public static Node shootableNode = new Node("shootables");
    public static Node destroyableNode = new Node("destroyables");
    public static Node staticNode = new Node("static");
    
                
    public static TerrainQuad currentMap = null;
    
    private AnimControl control;
    private AnimChannel channel;
    
    public void createStaticModel(String modelfile, Node subNode, float xpos, float elevation, float zpos, float yaw, float pitch, float scale){
            model = PlayGame.app.getAssetManager().loadModel(modelfile).clone();
                                                
//            if(matfile !=""){   
//                model.setMaterial(PlayGame.app.getAssetManager().loadMaterial(matfile));
//            }
            
            float terrainHeight = currentMap.getHeight(new Vector2f(xpos,zpos));
            model.setLocalTranslation(xpos, terrainHeight+elevation, zpos);
            model.rotate(pitch, yaw, 0);
            model.setLocalScale(scale);
            model.setShadowMode(ShadowMode.CastAndReceive);
            CollisionShape sceneModel = CollisionShapeFactory.createMeshShape(model);
            modelRigidBody = new RigidBodyControl(sceneModel,0);
            model.addControl(modelRigidBody);
            
            PlayGame.bulletAppState.getPhysicsSpace().add(modelRigidBody); //uses the main GameAppState's BulletAppState
            subNode.attachChild(model);
            //System.out.println("Child "+subNode.getName()+" added: "+subNode.getChild(model.getName()));
            PlayGame.screenLoading.setAssetName(model.getName());
            System.out.println("Loading model: "+model.getName()); 
            
        }
        
        
            
            public void createRandomizedModel(String modelfile, Node subNode, int count, int minx, int maxx, int minz, int maxz, int yaw, float pitch, float minscale, float maxscale, boolean collision){
                            
            
            for (int i = 0; i < count; i++){
                model = PlayGame.app.getAssetManager().loadModel(modelfile).clone();
                                       
//                if(matfile !=""){   
//                    model.setMaterial(PlayGame.app.getAssetManager().loadMaterial(matfile));
//                }    
                
                int x = FastMath.nextRandomInt(minx, maxx);
                int z = FastMath.nextRandomInt(minz, maxz);
                int ydeg = FastMath.nextRandomInt(0, yaw);
                float terrainHeight = currentMap.getHeight(new Vector2f(x,z));
                
                float modelsize = (FastMath.nextRandomFloat()%(maxscale-minscale)+minscale);
                
                Vector3f modelLocation = new Vector3f(x,terrainHeight,z);
                    model.setLocalTranslation(modelLocation);
                    
                    model.rotate(pitch, ydeg, 0);
                    model.setLocalScale(modelsize);
                                        
                    
            if (collision){
                model.setShadowMode(ShadowMode.CastAndReceive);
                CollisionShape sceneModel = CollisionShapeFactory.createMeshShape(model);
                modelRigidBody = new RigidBodyControl(sceneModel,0);
                model.addControl(modelRigidBody);
                PlayGame.bulletAppState.getPhysicsSpace().add(modelRigidBody); //uses the main GameAppState's BulletAppState
            }
            subNode.attachChild(model);
            //System.out.println("Child random "+subNode.getName()+" added: "+subNode.getChild(model.getName()));
//            PlayGame.screenLoading.setAssetName(model.getName());
            System.out.println("Loading model: "+model.getName()); 
            
            }
        }
            
            
        public void createAnimatedModel(String modelPath, float scale, float xloc, float yloc, float zloc, String animName){
            aniModel = (Node)PlayGame.app.getAssetManager().loadModel(modelPath).clone();
            aniModel.setLocalScale(scale);
            aniModel.setLocalTranslation(xloc, yloc, zloc);
            
            staticNode.attachChild(aniModel);
            System.out.println("Child animated added: "+staticNode.getChild(aniModel.getName()));
            control = aniModel.getControl(AnimControl.class);
            channel = control.createChannel();
            channel.setAnim(animName);
          
        }    
            
        public void createUpdateAnimatedModel(String modelPath, float scale, float xloc, float yloc, float zloc){
            aniModel = (Node)PlayGame.app.getAssetManager().loadModel(modelPath).clone();
            
            aniModel.setLocalScale(scale);
            aniModel.setLocalTranslation(xloc, yloc, zloc);
            aniModel.setShadowMode(ShadowMode.CastAndReceive);
            
            staticNode.attachChild(aniModel);
            //System.out.println("Child updateanimated added: "+staticNode.getChild(aniModel.getName()));
                    
        }        
   
        public Spatial getModel() {
            return model;
        }

   
    @Override
    protected void initialize(Application app) {
        System.out.println(this.getClass().getName()+" initialized....."); 
        
        
    }
    
    @Override
    protected void cleanup(Application app) {
        
    }
    
    @Override
    public void update(float tpf) {
        
//        System.out.println(this.getClass().getName()+" updated....."); 
        
    }

    @Override
    protected void onEnable() {
        
        
    }

    @Override
    protected void onDisable() {
        
    }

    public Node getAniModel(String childName) {
        
        return (Node)staticNode.getChild(childName);
    }
    
    
    
}