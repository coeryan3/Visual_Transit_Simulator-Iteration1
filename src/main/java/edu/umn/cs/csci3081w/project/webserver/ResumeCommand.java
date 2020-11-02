package edu.umn.cs.csci3081w.project.webserver;

import com.google.gson.JsonObject;

public class ResumeCommand extends MyWebServerCommand {

    private VisualizationSimulator mySim;

    /**
     * Resume simulation constructor functionality.
     * @param sim simulation object
     */
    public ResumeCommand(VisualizationSimulator sim) {
        this.mySim = sim;
    }

    /**
     * This method resumes the simulation.
     *
     * @param session current simulation session
     * @param command the resume simulation command content
     * @param state the state of the simulation session
     */
    @Override
    public void execute(MyWebServerSession session, JsonObject command,
                        MyWebServerSessionState state) {
        mySim.resume();
    }
}
