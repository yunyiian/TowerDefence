package WizardTD;

import java.util.List;

public class Wave {
    private double duration;
    private double preWavePause;
    private List<MonsterType> monsters;

    public Wave(double duration, double preWavePause, List<MonsterType> monsters) {
        this.duration = duration;
        this.preWavePause = preWavePause;
        this.monsters = monsters;
    }

    // Getters and setters for each attribute...

    // Additional methods to handle wave logic, like spawning monsters, can be added here.
}
