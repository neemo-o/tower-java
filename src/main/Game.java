package main;
import ui.MainMenu;

public class Game implements Mediator {

	public static void main(String[] args) {
		Game jogo = new Game();
		jogo.startGame();
	}

	public void showMainMenu() {
        MainMenu menu = new MainMenu(this); // passa o Mediator
        menu.show();
    }

	@Override
    public void startGame() {
        System.out.println("Teste1");
    }

    @Override
    public void endGame() {
        System.out.println("Teste2");
        System.exit(0);
    }
}
