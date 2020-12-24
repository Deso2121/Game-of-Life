package agh.cs.gameOfLife.interfaces;


public interface IStatisticsChangeObserver {
    void updateStatistics(int animals, int grass, String genotype, double averageEnergy, double averageLifeLength, double averageChildrenNumber);
}
