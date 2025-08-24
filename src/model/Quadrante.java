package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author danns
 */
public class Quadrante {

    private int idQuadrante;
    private List<ValorPosicaoXY> campos = new ArrayList<>();
    private List<Integer> numeros = List.of(1,2,3,4,5,6,7,8,9);
    private List<Integer> posicoes= List.of(0,1,2,3,4,5,6,7,8);
    private List<Integer> numerosMutaveis = new ArrayList<>(numeros);
    private List<Integer> posicoesMutaveis = new ArrayList<>(posicoes);

    public int getIdQuadrante() {
        return idQuadrante;
    }

    public void setIdQuadrante(int idQuadrante) {
        this.idQuadrante = idQuadrante;
    }

    public List<ValorPosicaoXY> getCampos() {
        return campos;
    }

    public void addValorPosicaoXY(int posicao, String valor) {
        campos.get(posicao).setValor(valor);
    }

    public Quadrante(int idQuadrante) {
        this.idQuadrante = idQuadrante;
        Collections.shuffle(numerosMutaveis);
        Collections.shuffle(posicoesMutaveis);
        
        switch (idQuadrante) {
            case 1:
                criarQuadrante(0, 0, 3, 3);
                break;
            case 2: 
                criarQuadrante(0, 3, 3, 6);
                break;
            case 3:
                criarQuadrante(0, 6, 3, 9);
                break;
            case 4:
                criarQuadrante(3,0, 6, 3);
                break;
            case 5:
                criarQuadrante(3, 3, 6, 6);
                break;
            case 6:
                criarQuadrante(3, 6, 6, 9);
                break;
            case 7:
                criarQuadrante(6, 0, 9, 3);
                break;
            case 8:
                criarQuadrante(6, 3, 9, 6);
                break;
            case 9:
                criarQuadrante(6, 6, 9, 9);
                break;

            default:
                throw new AssertionError();
        }

    }
    
    /*
    utilLinha = Parâmetro para incrementar coluna ex:
    0 1 2 | 3 4 5 | 6 7 8 <- São as posições das colunas, sempre que chegar ao 3
                             incrementa a linha que se inícia em 0 e retorna a coluna
                             ao valor inicial do quadrante 0
    */
    private void criarQuadrante(int linha, int coluna, int utilLinha, int utilColuna) {
                var valorRetornoLinha = linha;
                var valorRetornoColuna = coluna;
       
                for (int i = 0; i < 9; i++) {
                    if (linha == utilLinha) {
                        linha = valorRetornoLinha;
                        coluna++;
                    }

                    campos.add(i, new ValorPosicaoXY(linha, coluna, "0", 0));

                    coluna++;
                    if (coluna == utilColuna) {
                        linha++;
                        coluna = valorRetornoColuna;
                    }

                }

                for (int i = 0; i < 3; i++) {
                    if (campos.get(posicoesMutaveis.get(i)).getValor().equals("0")) {
                        
                        int valorXY= numerosMutaveis.get(i);
                        if(numerosMutaveis.contains(i)) {
                        
                    }
                        
                        
                        System.out.println(valorXY);
                        campos.get(posicoesMutaveis.get(i)).setValor(String.valueOf(valorXY));
                        campos.get(posicoesMutaveis.get(i)).setTipo(1);
                    }
                }

                for (ValorPosicaoXY x : campos) {
                    System.out.println("Linha: " + x.getLinha() + " Coluna:  " + x.getColuna() + "Valor: " + x.getValor());
                }

    }

    public Quadrante() {
    }
    

}
