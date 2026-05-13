import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import javax.swing.JPanel;

public class Jogo extends JPanel implements Runnable
{
    private Image imagemFundo;
    private Image imagemFimJogo;
    private Image imagemBotaoFim;

    private final int BOTAO_FIM_LARGURA = 200;
    private final int BOTAO_FIM_ALTURA = 80;
    private final int BOTAO_FIM_X = (LARGURA_TELA - BOTAO_FIM_LARGURA) / 2;
    private final int BOTAO_FIM_Y = (ALTURA_TELA / 2) + 170;
    
    public static final int LARGURA_TELA = 1300;
    public static final int ALTURA_TELA = 750;
    public static final int TAMANHO_BLOCO = 50;
    public static final int UNIDADES = LARGURA_TELA * ALTURA_TELA / (TAMANHO_BLOCO * TAMANHO_BLOCO);
    public static final int INTERVALO = 200;
    public static final String NOME_FONTE = "Ink Free";

    public static Semaphore Mutex;

    private int segundos = 0;
    private Timer cronometro;
    private Cobrinha objetoCobra;
    private Comida objetoComida;
    private boolean GameOver = false;

    public Jogo()
    {
        // cria o semáforo que será usado como Mutex
        Mutex = new Semaphore(1);

        // define o tamanho da tela do jogo
        setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));

        // define a cor de fundo
        setBackground(Color.WHITE);

        // permite que o painel receba foco para capturar teclado
        setFocusable(true);

        // carrega as imagens do jogo
        imagemFundo = new ImageIcon("imagens/fundo.png").getImage();
        imagemFimJogo = new ImageIcon("imagens/fim_jogo.png").getImage();
        imagemBotaoFim = new ImageIcon("imagens/botao_fim.png").getImage();

        // adiciona o leitor de teclado
        addKeyListener(new Teclado());

        // iniciar o Timer do cronômetro
        cronometro = new Timer(1000, e -> {
            if (GameOver == false) {
                segundos++;
                repaint();
            }
        });

        cronometro.start();

        // adiciona leitor de mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (GameOver == true) {

                    int mouseX = e.getX();
                    int mouseY = e.getY();

                    boolean clicouNoBotaoFim =
                            mouseX >= BOTAO_FIM_X &&
                            mouseX <= BOTAO_FIM_X + BOTAO_FIM_LARGURA &&
                            mouseY >= BOTAO_FIM_Y &&
                            mouseY <= BOTAO_FIM_Y + BOTAO_FIM_ALTURA;

                    if (clicouNoBotaoFim) {
                        System.exit(0);
                    }
                }
            }
        });

        // cria a cobrinha
        objetoCobra = new Cobrinha();

        // cria a comida
        objetoComida = new Comida();

        // cria a primeira posição da comida
        objetoComida.CriarNovaPosicao();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        try {
            desenharTela(g);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public void desenharTela(Graphics g) throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        if (GameOver == false)
        {
            // desenha a imagem de fundo do jogo
            g.drawImage(imagemFundo, 0, 0, LARGURA_TELA, ALTURA_TELA, null);

            // desenha comida e cobra por cima do fundo
            objetoComida.Desenhar(g);
            objetoCobra.Desenhar(g);

            // desenha o tempo
            g.setColor(Color.RED);
            g.setFont(new Font(NOME_FONTE, Font.BOLD, 40));
            g.drawString("Tempo: " + segundos + "s", 20, 80);

            // desenha a pontuação
            FontMetrics metrics = getFontMetrics(g.getFont());

            String texto = "Pontos: " + objetoCobra.QuantidadeComida;
            g.drawString(texto, (LARGURA_TELA - metrics.stringWidth(texto)) / 2, g.getFont().getSize());
        }
        else
        {
            fimDeJogo(g);
        }
    }

    public void fimDeJogo(Graphics g)
    {
        // desenha a imagem de fundo da tela de fim de jogo
        g.drawImage(imagemFimJogo, 0, 0, LARGURA_TELA, ALTURA_TELA, null);

        // texto principal
        g.setColor(Color.RED);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 75));

        FontMetrics metrics = getFontMetrics(g.getFont());

        String texto = "Fim do Jogo";
        g.drawString(texto, (LARGURA_TELA - metrics.stringWidth(texto)) / 2, ALTURA_TELA / 2);

        // pontuação final
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 35));
        FontMetrics metricsPontos = getFontMetrics(g.getFont());

        String pontosTexto = "Pontuação final: " + objetoCobra.QuantidadeComida;
        g.drawString(
            pontosTexto,
            (LARGURA_TELA - metricsPontos.stringWidth(pontosTexto)) / 2,
            (ALTURA_TELA / 2) + 60
        );

        // tempo final
        String tempoTexto = "Tempo final: " + segundos + "s";
        g.drawString(
            tempoTexto,
            (LARGURA_TELA - metricsPontos.stringWidth(tempoTexto)) / 2,
            (ALTURA_TELA / 2) + 105
        );

        // desenha a imagem do botão Fim
        g.drawImage(
            imagemBotaoFim,
            BOTAO_FIM_X,
            BOTAO_FIM_Y,
            BOTAO_FIM_LARGURA,
            BOTAO_FIM_ALTURA,
            null
        );
    }

    @Override
    public void run()
    {
        while (GameOver == false)
        {
            try {
                objetoCobra.andar();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            try {
                if (objetoCobra.alcancouComida() == true)
                {
                    objetoComida.CriarNovaPosicao();
                }
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }

            GameOver = objetoCobra.VerificarGameOver();

            repaint();

            try {
                Thread.sleep(INTERVALO);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // para o cronômetro quando o jogo acaba
        cronometro.stop();

        // redesenha a tela final
        repaint();
    }
}