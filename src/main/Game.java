package main;

import ui.MainMenu;

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

            // TODO: RAMON Implementar o cenário do jogo aqui e iniciar o loop principal do
            // jogo
            // Lembrar de usar o Mediator que eu criei para comunicar entre as classes
            // Se possivel tente usar o padrão Composite em algum funcionalidade do jogo
            // beleza, vou estudar como implementar o mediador. tive problemas com o pc. so consegui clonar o repositorio agora. tive q formatar e reinstalar tudo
            // o vs code tinha dado conflito com o avast e apagado arquivos de tudo do meu pc
            public class criar_nome {
    public static void main(String[] args) {
        boolean nomedojogo = true;

        while (nomedojogo) {
            System.out.println("iniciando...");

            if (criarcondiçao) true {
                nomedojogo = false;
                System.out.println("Encerrando");
            }
        }
    }
}
            System.out.println("o jogo...");

            //acredigo que possa funcionar.. mas é so uma base. to lembrando como faz a logica ainda
        }
    }
}
