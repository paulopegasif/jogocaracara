package actions;

import enums.Jogada;
import model.Jogador;


public interface JogoActions {

    void verificarInicio();

    void realizarJogada(Jogador jogador, Jogada jogada);

    void sair(Jogador jogador);

}