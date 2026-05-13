import javax.swing.JFrame;

public class Principal
{
    public static void main(String[] args)
    {
        // criar uma instância do jogo
        Jogo game = new Jogo();

        // criar a janela principal do jogo
        JFrame janelaPrincipal = new JFrame("Cobrinha");

        // adicionar o jogo na tela
        janelaPrincipal.add(game);

        // ajustar o tamanho da janela de acordo com o JPanel do jogo
        janelaPrincipal.pack();

        // centralizar a janela na tela
        janelaPrincipal.setLocationRelativeTo(null);

        // encerrar o programa quando clicar no X da janela
        janelaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // não deixar o usuário alterar o tamanho da janela
        janelaPrincipal.setResizable(false);

        // mostrar a janela
        janelaPrincipal.setVisible(true);

        // força o foco no painel do jogo para o teclado funcionar
        game.requestFocusInWindow();

        // iniciar a thread do jogo
        Thread threadJogo = new Thread(game); //Isso é a criação de um objeto da classe JFrame, usando um construtor com parâmetro.
        threadJogo.start();
    }
}