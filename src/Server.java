import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import model.Jogador;
import enums.Status;

public class Server {

    private static final String MOTD = "" +

            "   | ----  Sistemas Operacionais 2  ---- |  \n" +
            "   |       Paulo Henrique Pereira Silva  |  \n" +
            "   |       Gabriel Lima do Nascimento    |  \n";
    private static Jogo jogoEmEspera;

    public static void main(String[] args) {

        try {

            //@SuppressWarnings("resource")
            ServerSocket server = new ServerSocket(3500);
            System.out.println("Servidor iniciado");

            while (true) {

                Socket conexao = server.accept();
                BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                PrintStream saida = new PrintStream(conexao.getOutputStream());
                Jogador jogador = new Jogador(entrada, saida, conexao);
                jogador.getSaida().println(MOTD);
                Jogo game = buscarJogo();
                if (game != null) {
                    game.conectarJogador(jogador);
                    continue;
                }
                jogador.getSaida().print("Que pena! O servidor está cheio...");
                conexao.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @return uma instância do jogo caso não haja outro em andamento
     */
    private static Jogo buscarJogo() {
        if (jogoEmEspera == null || jogoEmEspera.getStatus() == Status.FINALIZADO) {
            jogoEmEspera = new Jogo();
        }
        if (jogoEmEspera.getStatus() == Status.AGUARDANDO_JOGADORES) {
            return jogoEmEspera;
        }
        return null;
    }

}