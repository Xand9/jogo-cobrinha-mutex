import javax.swing.JFrame;//criar uma janela na tela. >  biblioteca Swing do Java.

public class Principal//Classe com o codigo para inciiar o jogo
// cria janela e objeto Jogo e inicia a thread
{
    public static void main(String[] args)// método main 
    {
        // Crie um objeto da classe Jogo e guarde esse objeto na variável chamada game
        Jogo game = new Jogo(); 
        //usar o obj da classe jogo que agr e um tipo(variavel) e criar a variavel GAME

        // criar a janela principal do jogo
        JFrame janelaPrincipal = new JFrame("CobraVSMacã");

        // adicionar o jogo na tela
        janelaPrincipal.add(game);

        // Ajusta o tamanho da janela ao conteúdo
        janelaPrincipal.pack();

        // centralizar a janela na tela
        janelaPrincipal.setLocationRelativeTo(null);//null = Janela no centro da tela do computador

        //  clicar no X da janela, fecha o programa inteiro
        janelaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // não deixar o usuário alterar o tamanho da janela
        janelaPrincipal.setResizable(false);

        // mostrar a janela
        janelaPrincipal.setVisible(true);

        // força o foco no painel do jogo para o teclado funcionar
        game.requestFocusInWindow();

        //Crie uma nova Thread chamada threadJogo, e diga que ela vai executar o objeto game
        Thread threadJogo = new Thread(game);
        threadJogo.start();
        //Inicia a Thread e executa automaticamente o método run() da classe Jogo
    }
}