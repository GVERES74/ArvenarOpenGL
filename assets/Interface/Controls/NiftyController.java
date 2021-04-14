
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author TE332168
 */
public abstract class NiftyController implements ScreenController{
    
    protected Nifty nifty;
    protected Screen screen;
    Boolean optionsMenuVisible;

    public NiftyController(){
        
    }
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
       nifty.getCurrentScreen().findElementByName("options");
    }

    @Override
    public void onEndScreen() {
        
    }
    
}
