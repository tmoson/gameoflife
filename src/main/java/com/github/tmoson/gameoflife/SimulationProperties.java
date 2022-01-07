package com.github.tmoson.gameoflife;

import java.io.IOException;
import java.util.Properties;

public class SimulationProperties {
  private Properties simProps;
  private static final String[] propNames = {
    "simulation.width", "simulation.length", "canvas.width", "canvas.length", "random.start"
  };

  public SimulationProperties() {
    // messy initialization of properties, but I really don't want to bring in spring right now just
    // to manage properties.
    simProps = new Properties();
    try {
      simProps.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("./config.properties"));
    } catch (Exception e) {
      System.out.println("unable to load properties file, using default values");
      simProps.setProperty("simulation.width", "50");
      simProps.setProperty("simulation.length", "50");
      simProps.setProperty("canvas.width", "850");
      simProps.setProperty("canvas.length", "850");
      simProps.setProperty("random.start", "true");
    }
    for (String prop : propNames) {
      String systemProp = System.getProperty(prop);
      if (systemProp != null && !systemProp.equals("")) {
        simProps.setProperty(prop, systemProp);
      }
    }
  }


  public String getProperty(String key){
    return simProps.getProperty(key);
  }

}
