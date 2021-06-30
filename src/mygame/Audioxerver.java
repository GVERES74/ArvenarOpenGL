/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import static mygame.PlayGame.app;

/**
 *
 * @author TE332168
 */
public class Audioxerver {
    
    public static AudioNode musicPlayer, soundPlayer, soundinstance;
    
     public static void loadMusic(String filepath, boolean start, boolean looping){
        musicPlayer = new AudioNode(app.getAssetManager(), filepath);
        musicPlayer.setDirectional(false);
        musicPlayer.setPositional(false);
        musicPlayer.setLooping(looping);
        musicPlayer.stop();
        app.getRootNode().attachChild(musicPlayer);
        
        if (start == true){
            musicPlayer.play();
        }
        else {
            musicPlayer.stop();
        }
    }

    public static void playSound(String filepath, boolean directional, boolean positional, boolean looping, float maxdist, float volume, float xpos, float ypos, float zpos){
        soundPlayer = new AudioNode(app.getAssetManager(), filepath, AudioData.DataType.Stream);
        soundPlayer.setDirectional(directional);
        soundPlayer.setPositional(positional);
        soundPlayer.setLocalTranslation(xpos, ypos, zpos);
        soundPlayer.setLooping(looping);
        soundPlayer.setVolume(volume);
        soundPlayer.setMaxDistance(maxdist);
        app.getRootNode().attachChild(soundPlayer);
        soundPlayer.play();
        //audioRenderer.playSource(soundPlayer);
        
    }
    
    public static void playSoundInstance(String filepath){
        soundinstance = new AudioNode(app.getAssetManager(), filepath, AudioData.DataType.Buffer);
        soundinstance.setPositional(false);
        app.getRootNode().attachChild(soundinstance);
        soundinstance.playInstance();
    }
    
}
