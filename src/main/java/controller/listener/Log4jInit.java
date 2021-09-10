package controller.listener;

import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Log4jInit implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        PropertyConfigurator.configure("log4j.properties");
    }

    public void contextDestroyed(ServletContextEvent event) {}

}