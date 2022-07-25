/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import static mygame.PlayGame.app;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author TE332168
 */
public class ParticleManager {
    
    
    
    private Node              rootNode;
    ParticleEmitter leafEmitter, fireEmitter, bugsEmitter;
    Material leaves, bug, flame;
            
    public ParticleManager(){
      
        rootNode = app.getRootNode();
    }
    
    public void particleFallingLeaves(String pFileName, float locX, float locY, float locZ){
            leafEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            leaves = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
            leaves.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha); //Alpha needed!!
                leaves.setTexture("Texture", app.getAssetManager().loadTexture("Effects/Particles/Leaves/"+pFileName));
                
            leafEmitter.setMaterial(leaves);
            leafEmitter.setImagesX(2);
            leafEmitter.setImagesY(2);
            
            leafEmitter.setLowLife(50f);
            leafEmitter.setHighLife(100f);
            leafEmitter.setStartSize(0.3f);
            leafEmitter.setEndSize(0.3f);
            leafEmitter.setStartColor(new ColorRGBA(1f,1f,1f,1f));
            leafEmitter.setEndColor(new ColorRGBA(1f,1f,1f,1f));
            leafEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(2,-5,-2));
            leafEmitter.getParticleInfluencer().setVelocityVariation(0.5f); //1.0f = random 360° directions //0.5f = 180 grade
            leafEmitter.setRotateSpeed(1f);
            leafEmitter.setLocalTranslation(locX, locY, locZ);
            leafEmitter.setNumParticles(50);
            leafEmitter.setParticlesPerSec(5);
            leafEmitter.setSelectRandomImage(true);
            leafEmitter.setRandomAngle(true);
            leafEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO,10f));
            //leafEmitter.setShape(new EmitterBoxShape(new Vector3f(-50f,-50f,-50f),new Vector3f(50f,50f,50f)));
            rootNode.attachChild(leafEmitter);
            
        }
    
    public void particleBugs(String pFileName){
            bugsEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            bug = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
            bug.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                bug.setTexture("Texture", app.getAssetManager().loadTexture("Effects/Particles/Bugs/"+pFileName));
                                
            bugsEmitter.setMaterial(bug);
            bugsEmitter.setImagesX(3);
            bugsEmitter.setImagesY(3);
            
            bugsEmitter.setLowLife(50f);
            bugsEmitter.setHighLife(60f);
            bugsEmitter.setStartSize(0.1f);
            bugsEmitter.setEndSize(0.1f);
            bugsEmitter.setStartColor(new ColorRGBA(1f,1f,1f,1f));
            bugsEmitter.setEndColor(new ColorRGBA(1f,1f,1f,1f));
            bugsEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(3,0,-5));
            bugsEmitter.getParticleInfluencer().setVelocityVariation(1.0f); //1.0f = random 360° directions //0.5f = 180 grade
            //bugsEmitter.setRotateSpeed(1f);
            bugsEmitter.setLocalTranslation(950f, 10f, -1200f);
            bugsEmitter.setNumParticles(10);
            bugsEmitter.setParticlesPerSec(3);
            bugsEmitter.setSelectRandomImage(true);
            bugsEmitter.setRandomAngle(true);
            bugsEmitter.setShape(new EmitterSphereShape(Vector3f.ZERO,10f));
            //bugsEmitter.setShape(new EmitterBoxShape(new Vector3f(-50f,-50f,-50f),new Vector3f(50f,50f,50f)));
            rootNode.attachChild(bugsEmitter);
            
        }
    
    
    public void createFirePlace(String pFileName, float locX, float locY, float locZ){
            fireEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            flame = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
            flame.setTexture("Texture", app.getAssetManager().loadTexture("Effects/Explosion/"+pFileName));
            
            fireEmitter.setMaterial(flame);
            fireEmitter.setImagesX(2);
            fireEmitter.setImagesY(2);
            fireEmitter.setStartColor(new ColorRGBA(1f, 0.0f, 0.0f, 0.8f));   // red 
            fireEmitter.setEndColor(new ColorRGBA(1f, 1.0f, 0.0f, 0.5f)); // yellow smoke
            fireEmitter.setLowLife(1.5f);
            fireEmitter.setHighLife(3.5f);
            fireEmitter.setStartSize(0.3f);
            fireEmitter.setEndSize(0.05f);
            fireEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0,1,0));
            fireEmitter.getParticleInfluencer().setVelocityVariation(0.3f);
            fireEmitter.setLocalTranslation(locX, locY, locZ);
            fireEmitter.setNumParticles(100);
            fireEmitter.setParticlesPerSec(20);
            rootNode.attachChild(fireEmitter);
            
        }
     
     public void createCandleFlame(String pFileName, float locX, float locY, float locZ){
            fireEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
            
            flame.setTexture("Texture", app.getAssetManager().loadTexture("Effects/Explosion/"+pFileName));
            fireEmitter.setMaterial(flame);
            fireEmitter.setImagesX(2);
            fireEmitter.setImagesY(2);
            fireEmitter.setStartColor(new ColorRGBA(1f, 0.5f, 0.0f, 0.8f));   // yellow
            fireEmitter.setEndColor(new ColorRGBA(1f, 0.5f, 0f, 0.5f)); // white
            fireEmitter.setLowLife(0.5f);
            fireEmitter.setHighLife(1.5f);
            fireEmitter.setStartSize(0.1f);
            fireEmitter.setEndSize(0.05f);
            fireEmitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0f,0.5f,0f));
            fireEmitter.getParticleInfluencer().setVelocityVariation(0.5f);
            fireEmitter.setLocalTranslation(locX, locY, locZ);
            fireEmitter.setNumParticles(100);
            fireEmitter.setParticlesPerSec(10);
            rootNode.attachChild(fireEmitter);
            
        }
     
     
      
    
}
