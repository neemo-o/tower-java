package main;

import ui.MainMenu;
import ui.GameLoop;
public class Game implements Mediator {

    public static void main(String[] args) {
        Game jogo = new Game();
        jogo.showMainMenu();
    }

    public void showMainMenu() {
        MainMenu menu = new MainMenu(this);
        menu.show();
    }

    @Override
    public void notify(Object sender, String event) {
        if (sender instanceof MainMenu && event.equals("startGame")) {
                    

            System.out.println("Starting the game...");
          
            //agora foi :)
            GameLoop gameLoop = new GameLoop(this);
            gameLoop.show();
            
            
            
            

        }
    }
}
