/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Managers;

import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.filters.FadeFilter;
import com.jme3.post.filters.FogFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.water.WaterFilter;
import static mygame.PlayGame.app;

/**
 *
 * @author TE332168
 */
public class EffectsManager {
    
    public FilterPostProcessor filterPostProc;
    private FadeFilter fadeFilter;
    public FogFilter fogFilter;
    public WaterFilter waterFilter;
    public static DepthOfFieldFilter dofFilter;
    public static BloomFilter bloom;   
    public static SSAOFilter ssaoFilter;
    public static DirectionalLightShadowRenderer dlsr;
    public static DirectionalLightShadowFilter dlsf;
    public static LightScatteringFilter sunGodRays;
    
    private Node              rootNode;
    
        
    public static int SHADOWMAP_SIZE = 1024;
    public static int SHADOWMAP_NUMSAMPLES = 3;
    
    public EffectsManager(){
        
        dlsr = new DirectionalLightShadowRenderer(app.getAssetManager(), SHADOWMAP_SIZE, SHADOWMAP_NUMSAMPLES);
            //app.getViewPort().addProcessor(dlsr); //shadowrenderer must be added before postprocessorfilter!!
        filterPostProc = new FilterPostProcessor(app.getAssetManager());
            app.getViewPort().addProcessor(filterPostProc);
            filterPostProc.setNumSamples(4);
        
               
    }
    
    public void fadeScreen(boolean in, float secs){
            
            fadeFilter = new FadeFilter(secs); // e.g. 2 seconds
            filterPostProc.addFilter(fadeFilter);
            fogFilter = new FogFilter();
            
            //fade.setDuration(secs);
            if (in ==true){
                fadeFilter.setValue(0);
                fadeFilter.fadeIn();
            }
            else fadeFilter.fadeOut();
            
    }
    
    public void depthOfField(float dist, float range, float blur){
        dofFilter = new DepthOfFieldFilter();
        dofFilter.setFocusDistance(dist);
        dofFilter.setFocusRange(range);
        dofFilter.setBlurScale(blur);
        dofFilter.setEnabled(true);
        filterPostProc.addFilter(dofFilter);
        
    }
    
    public void bloom(int expow, float intensity){
        bloom = new BloomFilter();
        bloom.setEnabled(true);
        bloom.setExposurePower(expow);
        bloom.setBloomIntensity(intensity);
        filterPostProc.addFilter(bloom);
    }
    
    public void SSAO(){
        ssaoFilter = new SSAOFilter(2.0f, 3.0f, 0.33f, 0.61f);
        ssaoFilter.setApproximateNormals(true);
        ssaoFilter.setEnabled(false);
        filterPostProc.addFilter(ssaoFilter);
    }
    
    
    //  1. SHADOW renderer, or....
    public void directionalLightShadowRenderer(DirectionalLight sun){
        
        dlsr.setLight(sun);
        dlsr.setLambda(0.65f);
        dlsr.setShadowIntensity(0.3f);
        //dlsr.setEdgeFilteringMode(EdgeFilteringMode.Nearest);
        
    }
    
    //  2. SHADOW filter
    public void directionalLightShadowFilter(DirectionalLight sun){
        dlsf = new DirectionalLightShadowFilter(app.getAssetManager(), SHADOWMAP_SIZE, SHADOWMAP_NUMSAMPLES);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        dlsf.setLambda(0.65f);
        dlsf.setShadowIntensity(0.3f);
        //dlsf.setEdgeFilteringMode(EdgeFilteringMode.Nearest);
        filterPostProc.addFilter(dlsf);
        
    }
    
    public void createGodRaysEffect(float sunLocX, float sunLocY, float sunLocZ, float density){
            sunGodRays = new LightScatteringFilter(new Vector3f(sunLocX, sunLocY, sunLocZ));
            sunGodRays.setLightDensity(density);
            sunGodRays.setEnabled(true);
            filterPostProc.addFilter(sunGodRays);
    }
    
    public void createGodRaysEffect(DirectionalLight sun, float multiplier, float density){
            Vector3f lightPos = sun.getDirection().mult(multiplier);
            sunGodRays = new LightScatteringFilter(lightPos);
            sunGodRays.setLightDensity(density);
            sunGodRays.setEnabled(true);
            filterPostProc.addFilter(sunGodRays);
    }
    
    public void createFog(float distance, float density){
            
            fogFilter = new FogFilter();
            fogFilter.setFogDistance(distance);
            fogFilter.setFogDensity(density);
            filterPostProc.addFilter(fogFilter);
            
        }
      
    
}
