import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Comida
{
    public static int posicao_x;
    public static int posicao_y;
    Random random;

    public Comida()
    {
        // inicializa o gerador de numeros aleatórios
        random = new Random();
    }

    public void CriarNovaPosicao()
    {
        // gerar uma nova posição aleatória para a comida
        posicao_x = random.nextInt(Jogo.LARGURA_TELA / Jogo.TAMANHO_BLOCO) * Jogo.TAMANHO_BLOCO;
        posicao_y = random.nextInt(Jogo.ALTURA_TELA / Jogo.TAMANHO_BLOCO) * Jogo.TAMANHO_BLOCO;
    }

    public void Desenhar(Graphics g)
    {
        // desenhar a comida provisoriamente como um círculo vermelho
        g.setColor(Color.RED);
        g.fillOval(posicao_x, posicao_y, Jogo.TAMANHO_BLOCO, Jogo.TAMANHO_BLOCO);
    }
}