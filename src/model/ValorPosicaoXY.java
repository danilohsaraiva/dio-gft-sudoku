package model;

import java.util.Objects;

/**
 *
 * @author danns
 */
public class ValorPosicaoXY {
    private int linha;
    private int coluna;
    private String valor;
    private int tipo; //final: 1, mutável: 6

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    

    public ValorPosicaoXY(int linha, int coluna, String valor, int tipo) {
        this.linha = linha;
        this.coluna = coluna;
        this.valor = valor;
        this.tipo = tipo;
    }

    public ValorPosicaoXY(String valor) {
        this.valor = valor;
    }
   

    @Override
    public String toString() {
        return "ValorPosicaoXY{" + "valor=" + valor + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.valor);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValorPosicaoXY other = (ValorPosicaoXY) obj;
        return Objects.equals(this.valor, other.valor);
    }
    
    
}
