package com.torstensommerfeld.utils.progress;

import java.util.Collections;
import java.util.List;

public class DummyProgressImpl implements Progress {

    @Override
    public double getProgress() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "Dummy";
    }

    @Override
    public Progress getParent() {
        return null;
    }

    @Override
    public void setParent(Progress parent) {
        // pass
    }

    @Override
    public Progress getRoot() {
        return null;
    }

    @Override
    public Progress getActive() {
        return null;
    }

    @Override
    public void setActive(Progress progress) {
        // pass
    }

    @Override
    public void setProgress(double progress) {
        // pass
    }

    @Override
    public void addProgress(double progress) {
        // pass
    }

    @Override
    public void finish() {
        // pass
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void invokeChangeTricker() {
        // pass
    }

    @Override
    public List<Progress> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public <T extends Progress> T addChild(T progress, double weight) {
        return progress;
    }

}
