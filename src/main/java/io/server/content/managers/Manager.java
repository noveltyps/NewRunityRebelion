package io.server.content.managers;

import io.server.Config;
import neytorokx.utils.Logger;

public class Manager extends Thread {

    private final String MANAGER_NAME;

    private boolean running = false;

    private long runtime = 0;
    private long PROCESS_DELAY;

    public Manager(String managerName) {

        this.MANAGER_NAME = managerName;
        this.PROCESS_DELAY = Config.DEFAULT_MANAGER_DELAY;

    }

    public Manager(String managerName, long processDelay) {

        this.MANAGER_NAME = managerName;
        this.PROCESS_DELAY = processDelay;

    }

    public Manager(String managerName, long processDelay, long runtime) {

        this.MANAGER_NAME = managerName;
        this.PROCESS_DELAY = processDelay;
        this.runtime = runtime;

    }

    public Manager startManager() {

        this.running = true;
        this.start();

        Logger.log("Engine", this.MANAGER_NAME + " has started with a process rate of " + this.PROCESS_DELAY + "ms.");

        onStart();

        return this;

    }

    public void onStart() { }

    public void preProcess() { }

    public void process() { }

    public void postProcess() { }

    @Override
    public void run() {

        while(running) {

            try {

                preProcess();

                process();

                postProcess();

                runtime++;

                sleep(PROCESS_DELAY);

            } catch (InterruptedException e) {

                e.printStackTrace(System.out);

            }

        }

    }

    public void onStop() { }

    public void stopManager() {

        this.running = false;

        Logger.log("Engine", this.MANAGER_NAME + " has shutdown.");

        onStop();

    }

    public void setRuntime(long runtime) { this.runtime = runtime; }

    public void setProcessDelay(long delay) { this.PROCESS_DELAY = delay; }

    public boolean isRunning() { return running; }

    public String getManagerName() { return MANAGER_NAME; }

    public long getRuntime() { return runtime; }

}
