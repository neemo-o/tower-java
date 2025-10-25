# 🐛 InsecTD - Insect Tower Defense

<div align="center">

![Java](https://img.shields.io/badge/Java-8+-orange?style=for-the-badge&logo=java)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Concluído-success?style=for-the-badge)

**Um jogo de Tower Defense temático sobre defesa contra insetos invasores!**

</div>

---

## 📖 Sobre o Projeto

InsecTD é um jogo de Tower Defense desenvolvido em Java utilizando Swing, onde o jogador deve defender sua casa contra ondas progressivas de insetos invasores. O projeto foi desenvolvido como trabalho acadêmico, implementando padrões de projeto e boas práticas de programação orientada a objetos.

### 🎮 Gameplay

Construa torres estrategicamente para eliminar inimigos antes que alcancem sua casa. Gerencie seus recursos, escolha as torres certas e sobreviva a 10 ondas de dificuldade crescente!

## ✨ Características

### 🏰 Torres Defensivas
- **Badogada** - Torre básica de pedras para inimigos terrestres (80$)
- **Chinelada** - Torre versátil que ataca qualquer tipo de inimigo (90$)
- **Eletrupicada** - Torre elétrica de ataque rápido (100$)

### 🐜 Inimigos
- **Formiga** - Inimigo básico rápido (16 HP, 5$ recompensa)
- **Besouro** - Inimigo tanque resistente (40 HP, 15$ recompensa)
- **Abelha** - Inimigo aéreo veloz (9 HP, 10$ recompensa)
- **Louva-Deus** - Boss final poderoso (500 HP, 400$ recompensa)

### 🎯 Mecânicas
- Sistema de ondas progressivas (10 waves)
- Economia baseada em moedas
- Preview de posicionamento de torres
- Indicadores visuais de dano e recompensas
- Sistema de alcance e targeting inteligente
- Efeitos visuais de projéteis e raios elétricos

### 🎵 Audiovisual
- Sprites customizados para torres e inimigos
- Efeitos sonoros para ações do jogo
- Músicas de fundo (menu e batalha)
- Animações fluidas e indicadores visuais
- Interface intuitiva e responsiva

## 🚀 Instalação

### Pré-requisitos
- Java 8 ou superior
- Maven (opcional, para build)

### Executando o Jogo

1. **Clone o repositório**
```bash
git clone https://github.com/seu-usuario/insectd.git
cd insectd
```

2. **Compile o projeto**
```bash
javac -d bin src/**/*.java
```

3. **Execute o jogo**
```bash
java -cp bin main.Game
```

### Executando via JAR
```bash
java -jar InsecTD.jar
```

## 🎮 Como Jogar

### Controles
- **Mouse**: Selecionar e posicionar torres
- **Teclas 1, 2, 3**: Seleção rápida de torres
- **ESC**: Cancelar seleção de torre
- **Clique Direito**: Cancelar ação

### Objetivo
Defenda sua casa contra 10 ondas de insetos. A casa possui 100 HP e recebe dano quando inimigos alcançam seu destino.

## 👥 Autores

- Neemias de Melo Vasconcelos
- Gustavo Ramon Ribeiro

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais.

---

<div align="center">

**Divirta-se defendendo sua casa dos insetos! 🐜🏰**

</div>
