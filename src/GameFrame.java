import javax.swing.*;
import java.awt.*;


/**
 * SESI 2: GameFrame (UPDATED)
 */
public class GameFrame extends JFrame {
    private MazePanel mazePanel;
    private ControlPanel controlPanel;


    public GameFrame() {
        setTitle("Pathfinder Game - SESI 2: + ALGORITMA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));


        mazePanel = new MazePanel();
        controlPanel = new ControlPanel(mazePanel);


        add(mazePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);


        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
}


