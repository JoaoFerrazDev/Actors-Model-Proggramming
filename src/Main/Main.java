package Main;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigSyntax;
import config.GuardianSupervisorStrategy;
import javax.swing.*;
import GUI.GUI;
public class Main {
    public static void main(String[] args) {
        Config configuration = ConfigFactory.parseString("akka.actor.guardian-supervisor-strategy=" +
                GuardianSupervisorStrategy.class.getName(), ConfigParseOptions.defaults().setSyntax(ConfigSyntax.PROPERTIES));
        ActorSystem actorSystem = ActorSystem.create("MeetingSchedulerSystem", configuration);
        GUI gui = new GUI(actorSystem);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    gui.createAndShowGUI(actorSystem);
                });
            }
        });
    }
}