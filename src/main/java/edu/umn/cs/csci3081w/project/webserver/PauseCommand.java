package edu.umn.cs.csci3081w.project.webserver;

import com.google.gson.JsonObject;

public class PauseCommand extends MyWebServerCommand {

    private VisualizationSimulator mySim;

    /**
     * Pause simulation constructor functionality.
     * @param sim simulation object
     */
    public PauseCommand(VisualizationSimulator sim) {
        this.mySim = sim;
    }

    /**
     * This method pauses the simulation.
     *
     * @param session current simulation session
     * @param command the pause simulation command content
     * @param state the state of the simulation session
     */
    @Override
    public void execute(MyWebServerSession session, JsonObject command,
                        MyWebServerSessionState state) {
        mySim.pause();
    }
}
