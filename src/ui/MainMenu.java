package ui;
import main.Mediator;
import javax.swing.JFrame;

public class MainMenu extends JFrame {
    private Mediator mediator;

    public MainMenu(Mediator mediator) {
        this.mediator = mediator;
    }

     public void show() {
        System.out.println("Teste3");
        mediator.startGame();
        mediator.endGame();
     }

        
}
