package com.job.tracker;


import com.system.inversion.InversionContainer;

/**
 * The <class>JobTrackerRunner</class> runs the application
 *
 * @author Andrew
 */
public class JobTrackerRunner {
    /**
     * Run the application
     *
     * @param args
     */
    public static void main(String[] args) {
        InversionContainer.startInversion(args);
    }
}