package main;

import ui.MainMenu;
import util.Sound;
import ui.GameLoop;
// Mediator interface
public class Game implements Mediator {
// aqui é onde tudo começa. onde o filho chora e a mãe também!
    public static void main(String[] args) {
        Game jogo = new Game();
        jogo.showMainMenu();
    }

    public void showMainMenu() {
        MainMenu menu = new MainMenu(this);
        menu.show();
    }
    // Mediator method implementation. o ingles ta fraco mas ta valendo
    @Override
    public void notify(Object sender, String event) {
        if (sender instanceof MainMenu && event.equals("startGame")) {
            System.out.println("Starting the game...");
            Sound sons = new Sound();
            sons.stopAllLoops();
            GameLoop loop = new GameLoop(this);
            loop.start();
            sons.playLoop("battle-music.wav", 0.55f);
        } else if  (sender instanceof GameLoop && event.equals("MainMenu")) {
            System.out.println("Remaking the main menu...");
            Sound sons = new Sound();
            sons.stopAllLoops();
            showMainMenu();
        }
    }
}
