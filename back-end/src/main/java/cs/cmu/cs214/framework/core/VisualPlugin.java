package cs.cmu.cs214.framework.core;

import cs.cmu.cs214.ProcessedData;

/**
 * The VisualPlugin interface defines the methods that must be 
 * implemented by any plugin that provides visual representations of data for the framework. 
 * It includes methods for registering the plugin with the framework, drawing processed data, 
 * and handling lifecycle events such as display and input. The interface has four methods:
 *    1. getName() returns the name of the plugin as a string.
 *    2. getDisplayType() returns the type of display provided by the plugin as a string.
 *    3. onRegister(Framework framework) is called when the plugin is registered with the framework.
 *    4. draw(ProcessedData data) is called to draw the processed data provided by the framework.
 * 
 * Additionally, the interface includes three lifecycle methods:
 *    1. onDisplay() is called when the display is activated. 
 *    2. onGetInput() is called to get input from the user.
 *    3. onFinished() is called when the plugin is finished.
 * 
*/
public interface VisualPlugin {
    String getName();
    String getDisplayType();
    void onRegister(Framework framework);
    void draw(ProcessedData data);

    // life cycle method
    void onDisplay();
    void onGetInput();
    void onFinished();
}
