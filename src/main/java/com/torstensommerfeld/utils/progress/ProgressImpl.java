package com.torstensommerfeld.utils.progress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ProgressImpl implements Progress {

    private Progress parent;
    private String description;
    private double totalWeight = 0;
    private double progress;
    private Progress active = this;
    private ChangeListener changeListener;
    private boolean isFinished = false;

    private List<ProgressHolder> progressHolders;
    private List<Progress> children;

    public ProgressImpl(String description) {
        this.description = description;
    }

    public ProgressImpl(String description, ChangeListener changeListener) {
        this.description = description;
        this.changeListener = changeListener;
    }

    public synchronized <T extends Progress> T addChild(T progress, double weight) {
        if (progressHolders == null) {
            progressHolders = new ArrayList<>();
            children = new ArrayList<>();
        }
        progressHolders.add(new ProgressHolder(progress, weight));
        children.add(progress);
        totalWeight += weight;
        progress.setParent(this);
        progress.setActive(getActive());
        return progress;
    }

    public synchronized double getProgress() {
        double currentProgress = progress;
        if (progressHolders != null) {
            for (ProgressHolder holder : progressHolders) {
                currentProgress += holder.getProgress().getProgress() * holder.getWeight() / totalWeight;
            }
        }
        return currentProgress;
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public ChangeListener getChangeListener() {
        return changeListener;
    }

    public String getDescription() {
        return description;
    }

    public Progress getParent() {
        return parent;
    }

    @Override
    public void setParent(Progress parent) {
        this.parent = parent;
    }

    public Progress getActive() {
        return active;
    }

    @Override
    public void setActive(Progress progress) {
        /*-
        if (active != progress) {
            active = progress;
            if (progressHolders != null) {
                for (ProgressHolder holder : progressHolders) {
                    holder.getProgress().setActive(progress);
                }
            }
        }
        */
    }

    @Override
    public Progress getRoot() {
        Progress root = this;
        while (root.getParent() != null) {
            root = root.getParent();
        }
        return root;
    }

    @Override
    public void setProgress(double progress) {
        this.progress = progress;
        Progress root = getRoot();
        root.setActive(this);
        root.invokeChangeTricker();
    }

    @Override
    public void addProgress(double progress) {
        setProgress(this.progress + progress);
    }

    @Override
    public void invokeChangeTricker() {
        if (changeListener != null) {
            changeListener.handleChange(this);
        }
    }

    @Override
    public void finish() {
        progress = 1;
        isFinished = true;
        progressHolders = Collections.emptyList();
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public List<Progress> getChildren() {
        if (children == null) {
            return Collections.emptyList();
        }

        synchronized (this) {
            return new ArrayList<>(children);
        }
    }

    @Override
    public String toString() {
        if (CollectionUtils.isEmpty(progressHolders)) {
            return String.format("%s: progress=%.2f", description, progress);
        } else {
            return String.format("%s: weight=%.2f", description, totalWeight);
        }
    }

    @Getter
    @AllArgsConstructor
    private static class ProgressHolder {
        private Progress progress;
        private double weight;
    }

    public interface ChangeListener {
        void handleChange(Progress progress);
    }

}
