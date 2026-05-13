import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Cobrinha
{
    public static char direcao = 'D'; // C - Cima, B - Baixo, E - Esquerda, D - Direita
    private int TamanhoDaCobra;
    public int QuantidadeComida;

    // vetores que irão armazenar as posições (x,y) do corpo da cobra
    private final int[] eixoX = new int[Jogo.UNIDADES];
    private final int[] eixoY = new int[Jogo.UNIDADES];

    public Cobrinha()
    {
        TamanhoDaCobra = 6;
        QuantidadeComida = 0;
    }

    public void Desenhar(Graphics g)
    {
        // desenhar o corpo da cobra
        for (int i = 0; i < TamanhoDaCobra; i++)
        {
            if (i == 0) // O é a posição da cabeça: pintar de preto
            {
                g.setColor(Color.ORANGE);
                g.fillRect(eixoX[0], eixoY[0], Jogo.TAMANHO_BLOCO, Jogo.TAMANHO_BLOCO);
            }
            else
            {
                g.setColor(new Color(45, 180, 0));
                g.fillRect(eixoX[i], eixoY[i], Jogo.TAMANHO_BLOCO, Jogo.TAMANHO_BLOCO);
            }
        }
    }

    public void andar() throws InterruptedException
    {
        // deslocar a cobra
        for (int i = TamanhoDaCobra; i > 0; i--)
        {
            eixoX[i] = eixoX[i - 1];
            eixoY[i] = eixoY[i - 1];
        }

        // *** Região crítica
        Jogo.Mutex.acquire();

        // atualizar a posição de acordo com as teclas que o usuário pressionou
        switch (direcao)
        {
            case 'C':
                eixoY[0] = eixoY[0] - Jogo.TAMANHO_BLOCO;
                break;

            case 'B':
                eixoY[0] = eixoY[0] + Jogo.TAMANHO_BLOCO;
                break;

            case 'E':
                eixoX[0] = eixoX[0] - Jogo.TAMANHO_BLOCO;
                break;

            case 'D':
                eixoX[0] = eixoX[0] + Jogo.TAMANHO_BLOCO;
                break;

            default:
                break;
        }

        // *** liberar região crítica
        Jogo.Mutex.release();
    }

    public void TocarEfeito() throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        File arquivo = new File("SomPontuacao.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(arquivo);

        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }

    public boolean alcancouComida() throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        // verificar se atingiu a comida
        if (eixoX[0] == Comida.posicao_x && eixoY[0] == Comida.posicao_y)
        {
            TamanhoDaCobra++;
            QuantidadeComida++;

            // som
            // TocarEfeito();

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean VerificarGameOver()
    {
        boolean perdeu = false;

        // A cabeça bateu no corpo?
        for (int i = TamanhoDaCobra; i > 0; i--)
        {
            if ((eixoX[0] == eixoX[i]) && (eixoY[0] == eixoY[i]))
            {
                perdeu = true;
                break;
            }
        }

        // A cabeça tocou uma das bordas direita ou esquerda?
        if ((eixoX[0] < 0) || (eixoX[0] > Jogo.LARGURA_TELA))
        {
            perdeu = true;
        }

        // A cabeça tocou o piso ou o teto?
        if ((eixoY[0] < 0) || (eixoY[0] > Jogo.ALTURA_TELA))
        {
            perdeu = true;
        }

        return perdeu;
    }
}