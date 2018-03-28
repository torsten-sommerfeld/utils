package progress;

import java.util.List;

public interface Progress {
    double getProgress();

    <T extends Progress> T addChild(T progress, double weight);

    String getDescription();

    Progress getParent();

    void setParent(Progress parent);

    Progress getRoot();

    Progress getActive();

    void setActive(Progress progress);

    void setProgress(double progress);

    void addProgress(double progress);

    void finish();

    boolean isFinished();

    void invokeChangeTricker();

    List<Progress> getChildren();

}
