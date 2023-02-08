package com.justinmtech.gameoflife;

import com.justinmtech.gameoflife.cellularautomata.CellularAutomata;
import com.justinmtech.gameoflife.cellularautomata.Drawing;
import com.justinmtech.gameoflife.config.ConfigManager;
import com.justinmtech.gameoflife.config.GameConfig;
import com.justinmtech.gameoflife.display.GUI;
import com.justinmtech.gameoflife.generation.Environment;
import com.justinmtech.gameoflife.generation.GenerationType;

import javax.naming.ConfigurationException;

public class GameOfLife {

    public static void main(String[] args) throws ConfigurationException {
        ConfigManager configManager = new ConfigManager();
        GameConfig gameConfig = configManager.getGameConfig();
        GenerationType generator = gameConfig.getGenerator();
        if (generator == GenerationType.DYNAMIC) {
            Environment environment = new Environment(configManager);
            Thread environmentThread = new Thread(environment);
            environmentThread.start();

            GUI gui = new GUI(environment, gameConfig);
            Thread guiThread = new Thread(gui);
            guiThread.start();
        } else if (generator == GenerationType.STATIC) {
            CellularAutomata ca = new CellularAutomata();
            ca.setHeight(gameConfig.getHeight());
            ca.setWidth(gameConfig.getWidth());
            if (gameConfig.getSeed().length != 8) {
                throw new ConfigurationException("The seed must be 8 integers long for static generations.");
            }
            ca.setSeed(gameConfig.getSeed());
            ca.run();
            Drawing draw = new Drawing(ca);
            draw.run(gameConfig.getWidth(), gameConfig.getHeight());
        }
    }
}


