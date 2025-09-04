package ui;
import main.Mediator;
import javax.swing.JFrame;

public class MainMenu {
    private Mediator mediator;

    public MainMenu(Mediator mediator) {
        this.mediator = mediator;
    }

     public void show() {
        System.out.println("Iniciando menu");

        JFrame frame = new JFrame("InsecTD");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);



        frame.setVisible(true);
     }

        
}
