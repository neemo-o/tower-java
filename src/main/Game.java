package main;
import ui.MainMenu;

public class Game implements Mediator {

	public static void main(String[] args) {
		Game jogo = new Game();
		jogo.showMainMenu();
	}

	public void showMainMenu() {
        MainMenu menu = new MainMenu(this); // passa o Mediator
        menu.show();
    }

	@Override
    public void startGame() {
        // TODO: RAMON Implementar o cenário do jogo aqui e iniciar o loop principal do jogo
        // Lembrar de usar o Mediator que eu criei para comunicar entre as classes
        // Se possivel tente usar o padrão Composite em algum funcionalidade do jogo
        System.out.println("Teste1");
    }

    @Override
    public void endGame() {
        System.out.println("Teste2");
        System.exit(0);
    }
}
