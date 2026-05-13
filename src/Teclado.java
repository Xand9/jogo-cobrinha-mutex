import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Teclado extends KeyAdapter
{
    @Override
    public void keyPressed(KeyEvent e)
    {
        //***regiao critica
        try {
            Jogo.Mutex.acquire();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        switch (e.getKeyCode())
        {
            case KeyEvent.VK_LEFT:
                if (Cobrinha.direcao != 'D')
                    Cobrinha.direcao = 'E';
                break;

            case KeyEvent.VK_RIGHT:
                if (Cobrinha.direcao != 'E')
                    Cobrinha.direcao = 'D';
                break;

            case KeyEvent.VK_UP:
                if (Cobrinha.direcao != 'B')
                    Cobrinha.direcao = 'C';
                break;

            case KeyEvent.VK_DOWN:
                if (Cobrinha.direcao != 'C')
                    Cobrinha.direcao = 'B';
                break;

            default:
                break;
        }

        //***liberar regiao critica
        Jogo.Mutex.release();
    }
}