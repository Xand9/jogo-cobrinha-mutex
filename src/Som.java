import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Som
{
    private static Clip musicaFundo;

    public static void tocarMusicaFundo(String caminhoArquivo)
    {
        try {
            File arquivo = new File(caminhoArquivo);

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(arquivo);

            musicaFundo = AudioSystem.getClip();
            musicaFundo.open(audioInputStream);

            // toca em loop contínuo enquanto o jogo estiver aberto
            musicaFundo.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Erro ao tocar música de fundo: " + caminhoArquivo);
            e.printStackTrace();
        }
    }

    public static void pararMusicaFundo()
    {
        if (musicaFundo != null && musicaFundo.isRunning()) {
            musicaFundo.stop();
            musicaFundo.close();
        }
    }
}