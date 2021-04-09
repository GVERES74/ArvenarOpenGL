/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author TE332168
 */
public class MaterialCreator{
    
    private SimpleApplication app;
    
    
    public void createMaterial(String diffuseTexture, String matdeffile){
    
                Material material = new Material(this.app.getAssetManager(), matdeffile);
                material.setTexture("DiffuseMap", this.app.getAssetManager().loadTexture(diffuseTexture));
                
//                material.setTexture("NormalMap", this.app.getAssetManager().loadTexture("Textures/Material/wooden_normal.tga"));
                material.setBoolean("UseMaterialColors",true);
                material.setColor("Diffuse",ColorRGBA.White);  // minimum material color
                material.setColor("Specular",ColorRGBA.White); // for shininess
                material.setFloat("Shininess", 64f); // [1,128] for shininess
                
    }
               
}
