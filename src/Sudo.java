
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.AbstractDocument;
import model.Quadrante;
import model.ValorPosicaoXY;
import validators.validadorNumero1ao9eInterrogacao;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author danns
 */
public class Sudo extends javax.swing.JFrame {

    /**
     * Creates new form Sudo
     */
    private List<Quadrante> quadrantes = new ArrayList<>();
    private JLabel[][] labelsUtil = null;
    private JLabel[] labelsRascunhoUtil = null;
    private JLabel labelSelecionada = null;

    public Sudo() {
        initComponents();

        JLabel[][] labels = {
            {label00, label01, label02, label03, label04, label05, label06, label07, label08},
            {label10, label11, label12, label13, label14, label15, label16, label17, label18},
            {label20, label21, label22, label23, label24, label25, label26, label27, label28},
            {label30, label31, label32, label33, label34, label35, label36, label37, label38},
            {label40, label41, label42, label43, label44, label45, label46, label47, label48},
            {label50, label51, label52, label53, label54, label55, label56, label57, label58},
            {label60, label61, label62, label63, label64, label65, label66, label67, label68},
            {label70, label71, label72, label73, label74, label75, label76, label77, label78},
            {label80, label81, label82, label83, label84, label85, label86, label87, label88}
        };

        JLabel[] labelsRasunho
                = {labelRascunho01, labelRascunho02, labelRascunho03, labelRascunho04, labelRascunho05,
                    labelRascunho06, labelRascunho07, labelRascunho08, labelRascunho09,};

        JPanel[] jpanels = {
            pnl1, pnl2, pnl3, pnl4, pnl5, pnl6, pnl7, pnl8, pnl9
        };

        criaEventoParaPreencherRascunho(jpanels);

        labelsUtil = labels;
        labelsRascunhoUtil = labelsRasunho;

        geraTabuleiroSudoKu();
        adicionarListeners();

        ((AbstractDocument) tfValor.getDocument()).setDocumentFilter(new validadorNumero1ao9eInterrogacao());

    }

    private void geraTabuleiroSudoKu() {
        quadrantes.clear();
        for (int i = 1; i < 10; i++) {
            if(i == 1 || i == 5 || i == 9) {
                quadrantes.add(new Quadrante(i));
                continue;
            }
            quadrantes.add(new Quadrante());
            
        }
        /*preencheTabuleiroSudoKu();*/
        if (!ehTabuleiroValido()) {
            geraTabuleiroSudoKu();
        }
        System.out.println(quadrantes);
    }

    private void criaEventoParaPreencherRascunho(JPanel[] jpanels) {
        for (int i = 0; i < 9; i++) {
            final int index = i;
            jpanels[index].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    // Exemplo: alterar cor do outro painel ao clicar
                    pnlRascunho.setBackground(Color.YELLOW);

                    //Soluções possíveis: AtomicInteger/IntStream.range
                    Quadrante quadrante = quadrantes.stream().
                            filter(q -> q.getIdQuadrante() == index + 1)
                            .findFirst()
                            .orElse(null);

                    if (quadrante != null) {
                        List<ValorPosicaoXY> campos = quadrante.getCampos();

                        IntStream.range(0, campos.size()).forEach(
                                i -> {
                                    labelsRascunhoUtil[i].setText(campos.get(i).getValor());
                                }
                        );
                    }

                    // Ou mudar o conteúdo
                    pnlRascunho.revalidate(); // atualiza o layout
                    pnlRascunho.repaint();    // redesenha
                }
            });
        }
    }

    private void preencheTabuleiroSudoKu() {
        for (int q = 0; q < 9; q++) { // quadrantes 0 a 8
            List<ValorPosicaoXY> campos = quadrantes.get(q).getCampos();

            for (int i = 0; i < 9; i++) { // campos do quadrante
                int linha = (q / 3) * 3 + i / 3;   // linha na grade 9x9
                int coluna = (q % 3) * 3 + i % 3;  // coluna na grade 9x9

                String valor = campos.get(i).getValor();
                if (!valor.equals("0")) {
                    labelsUtil[linha][coluna].setText(valor);

                    labelsUtil[linha][coluna].setFont(
                            labelsUtil[linha][coluna].getFont().deriveFont(Font.BOLD)
                    );
                } else {
                    labelsUtil[linha][coluna].setText("");
                }

                labelsUtil[linha][coluna].setText(valor.equals("0") ? "" : valor);

                labelsUtil[linha][coluna].setFont(
                        labelsUtil[linha][coluna].getFont().deriveFont(Font.BOLD)
                );

            }
        }
    }

    private boolean podeSetarValor(int linha, int coluna, String numero) {
        List<String> valores = quadrantes.stream()
                .flatMap( q -> q.getCampos().stream())
                .filter(c -> c.getLinha() == linha || c.getColuna() == coluna && !(c.getColuna() == coluna && c.getLinha()==linha))
                .map(ValorPosicaoXY::getValor)
                .toList();
        
        //verifica valor nas colunas
        for (int c = 0; c < 9; c++) {
            if (labelsUtil[linha][c].equals(numero)) {
                return false;
            }
        }
        //verifica valor nas linhas
        for (int l = 0; l < 9; l++) {
            if (labelsUtil[l][coluna].equals(numero)) {
                return false;
            }
        }
        // Verifica quadrante 3x3
        int inicioLinha = (linha / 3) * 3;
        int inicioColuna = (coluna / 3) * 3;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                
                if (labelsUtil[inicioLinha + i][inicioColuna + j].equals(numero))return false;
            }
        }
        return true;
    }
    
    private boolean preencherSudoku(int linha, int coluna) {
    // caso base: chegou no fim da grade
    if (linha == 9) {
        return true;
    }

    int proximaLinha = (coluna == 8) ? linha + 1 : linha;
    int proximaColuna = (coluna + 1) % 9;

    // pula células que já têm número
    if (!labelsUtil[linha][coluna].getText().equals("")) {
        return preencherSudoku(proximaLinha, proximaColuna);
    }

    // tenta números aleatórios de 1 a 9
    List<String> numeros = Arrays.asList("1","2","3","4","5","6","7","8","9");
    Collections.shuffle(numeros);

    for (String numeroTentado : numeros) {
        if (podeSetarValor(linha, coluna, numeroTentado)) {
            labelsUtil[linha][coluna].setText(numeroTentado);

            if (preencherSudoku(proximaLinha, proximaColuna)) {
                return true;
            }

            // backtrack → desfaz tentativa
            labelsUtil[linha][coluna].setText("");
        }
    }
    return false;
}
    private void geraQuadrantesUmCincoNove() {
        quadrantes.set(0, new Quadrante(1));
        quadrantes.set(4, new Quadrante(5));
        quadrantes.set(8, new Quadrante(9));
    }

    private boolean ehTabuleiroValido() {
        boolean ehTabuleiroValido = true;
        // Para simplificação, assumindo que 0 é célula vazia
        for (Quadrante quadrante : quadrantes) {
            List<String> valores = new ArrayList<>();
            for (ValorPosicaoXY campo : quadrante.getCampos()) {

                List<ValorPosicaoXY> listaValores = quadrantes.stream().flatMap(q -> q.getCampos().stream()).filter(
                        c -> c.getLinha() == campo.getLinha()
                        || c.getColuna() == campo.getColuna() && !(c.getColuna() == campo.getColuna() && c.getLinha() == campo.getLinha())
                ).collect(Collectors.toList());
                String valor = campo.getValor();

                /*
                
                if (!valor.equals("0")) {
                    if (listaValores.contains(new ValorPosicaoXY(valor))) {
                       ehTabuleiroValido = false; // repetido no quadrante
                       return false;
                    }
                }*/
            }
        }
        return ehTabuleiroValido; // nenhum erro encontrado
    }

    private boolean ehTabuleiroCompleto() {
        boolean ehTabuleiroCompleto = true;
        // Para simplificação, assumindo que 0 é célula vazia
        for (Quadrante quadrante : quadrantes) {
            List<String> valores = new ArrayList<>();

            List<ValorPosicaoXY> listaValores = quadrantes.stream().flatMap(q -> q.getCampos().stream())
                    .collect(Collectors.toList());

            for (ValorPosicaoXY item : listaValores) {
                if (item.getValor().equals("") || item.getValor().equals("?") || item.getValor().equals("0")) {
                    ehTabuleiroCompleto = false;
                    System.out.println(ehTabuleiroCompleto);
                }
            }
        }
        return ehTabuleiroCompleto; // nenhum erro encontrado
    }

    private void limpaCampos() {
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                JLabel label = labelsUtil[linha][coluna];
                if (!((label.getFont().getStyle() & Font.BOLD) == 1)) {
                    labelsUtil[linha][coluna].setText("");
                }
            }
        }

        for (int linha = 0; linha < 9; linha++) {
            for (int i = 0; i < 9; i++) {
                JLabel label = labelsRascunhoUtil[i];
                if (!((label.getFont().getStyle() & Font.BOLD) == 1)) {
                    labelsRascunhoUtil[i].setText("");
                }
            }
        }

    }

    private void adicionarListeners() {
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                JLabel label = labelsUtil[linha][coluna];
                label.setName("label" + linha + coluna);
                label.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        // Remove borda da label selecionada anterior
                        if (labelSelecionada != null) {
                            labelSelecionada.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 1));
                        }

                        // Destaca a nova label
                        label.setBorder(javax.swing.BorderFactory.createLineBorder(Color.blue, 3));
                        labelSelecionada = label;

                    }
                });
            }
        }

        for (int i = 0; i < 9; i++) {
            JLabel label = labelsRascunhoUtil[i];
            label.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    // Remove borda da label selecionada anterior
                    if (labelSelecionada != null) {
                        labelSelecionada.setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 1));
                    }

                    // Destaca a nova label
                    label.setBorder(javax.swing.BorderFactory.createLineBorder(Color.blue, 3));
                    labelSelecionada = label;
                }
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpanelPrincipal = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pnl1 = new javax.swing.JPanel();
        label00 = new javax.swing.JLabel();
        label01 = new javax.swing.JLabel();
        label02 = new javax.swing.JLabel();
        label10 = new javax.swing.JLabel();
        label11 = new javax.swing.JLabel();
        label12 = new javax.swing.JLabel();
        label20 = new javax.swing.JLabel();
        label21 = new javax.swing.JLabel();
        label22 = new javax.swing.JLabel();
        pnl2 = new javax.swing.JPanel();
        label03 = new javax.swing.JLabel();
        label04 = new javax.swing.JLabel();
        label05 = new javax.swing.JLabel();
        label13 = new javax.swing.JLabel();
        label14 = new javax.swing.JLabel();
        label15 = new javax.swing.JLabel();
        label23 = new javax.swing.JLabel();
        label24 = new javax.swing.JLabel();
        label25 = new javax.swing.JLabel();
        pnl3 = new javax.swing.JPanel();
        label06 = new javax.swing.JLabel();
        label07 = new javax.swing.JLabel();
        label08 = new javax.swing.JLabel();
        label16 = new javax.swing.JLabel();
        label17 = new javax.swing.JLabel();
        label18 = new javax.swing.JLabel();
        label26 = new javax.swing.JLabel();
        label27 = new javax.swing.JLabel();
        label28 = new javax.swing.JLabel();
        pnl4 = new javax.swing.JPanel();
        label30 = new javax.swing.JLabel();
        label31 = new javax.swing.JLabel();
        label32 = new javax.swing.JLabel();
        label40 = new javax.swing.JLabel();
        label41 = new javax.swing.JLabel();
        label42 = new javax.swing.JLabel();
        label50 = new javax.swing.JLabel();
        label51 = new javax.swing.JLabel();
        label52 = new javax.swing.JLabel();
        pnl5 = new javax.swing.JPanel();
        label33 = new javax.swing.JLabel();
        label34 = new javax.swing.JLabel();
        label35 = new javax.swing.JLabel();
        label43 = new javax.swing.JLabel();
        label44 = new javax.swing.JLabel();
        label45 = new javax.swing.JLabel();
        label53 = new javax.swing.JLabel();
        label54 = new javax.swing.JLabel();
        label55 = new javax.swing.JLabel();
        pnl6 = new javax.swing.JPanel();
        label36 = new javax.swing.JLabel();
        label37 = new javax.swing.JLabel();
        label38 = new javax.swing.JLabel();
        label46 = new javax.swing.JLabel();
        label47 = new javax.swing.JLabel();
        label48 = new javax.swing.JLabel();
        label56 = new javax.swing.JLabel();
        label57 = new javax.swing.JLabel();
        label58 = new javax.swing.JLabel();
        pnl7 = new javax.swing.JPanel();
        label60 = new javax.swing.JLabel();
        label61 = new javax.swing.JLabel();
        label62 = new javax.swing.JLabel();
        label70 = new javax.swing.JLabel();
        label71 = new javax.swing.JLabel();
        label72 = new javax.swing.JLabel();
        label80 = new javax.swing.JLabel();
        label81 = new javax.swing.JLabel();
        label82 = new javax.swing.JLabel();
        pnl8 = new javax.swing.JPanel();
        label63 = new javax.swing.JLabel();
        label64 = new javax.swing.JLabel();
        label65 = new javax.swing.JLabel();
        label73 = new javax.swing.JLabel();
        label74 = new javax.swing.JLabel();
        label75 = new javax.swing.JLabel();
        label83 = new javax.swing.JLabel();
        label84 = new javax.swing.JLabel();
        label85 = new javax.swing.JLabel();
        pnl9 = new javax.swing.JPanel();
        label66 = new javax.swing.JLabel();
        label67 = new javax.swing.JLabel();
        label68 = new javax.swing.JLabel();
        label76 = new javax.swing.JLabel();
        label77 = new javax.swing.JLabel();
        label78 = new javax.swing.JLabel();
        label86 = new javax.swing.JLabel();
        label87 = new javax.swing.JLabel();
        label88 = new javax.swing.JLabel();
        pnlRascunho = new javax.swing.JPanel();
        labelRascunho01 = new javax.swing.JLabel();
        labelRascunho02 = new javax.swing.JLabel();
        labelRascunho03 = new javax.swing.JLabel();
        labelRascunho04 = new javax.swing.JLabel();
        labelRascunho05 = new javax.swing.JLabel();
        labelRascunho06 = new javax.swing.JLabel();
        labelRascunho07 = new javax.swing.JLabel();
        labelRascunho08 = new javax.swing.JLabel();
        labelRascunho09 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        tfValor = new javax.swing.JTextField();
        btnOk = new javax.swing.JButton();
        btnLimparCampos = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jpanelPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        jpanelPrincipal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jpanelPrincipal.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel1.setText(" GFT & DIO");

        jLabel2.setFont(new java.awt.Font("Segoe Print", 1, 48)); // NOI18N
        jLabel2.setText("SUDOKU");

        pnl1.setBackground(new java.awt.Color(204, 204, 204));

        label00.setBackground(new java.awt.Color(255, 255, 255));
        label00.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label00.setText("0");
        label00.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label00.setPreferredSize(new java.awt.Dimension(33, 33));
        label00.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                label00MousePressed(evt);
            }
        });

        label01.setBackground(new java.awt.Color(255, 255, 255));
        label01.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label01.setText("0");
        label01.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label01.setPreferredSize(new java.awt.Dimension(33, 33));

        label02.setBackground(new java.awt.Color(255, 255, 255));
        label02.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label02.setText("0");
        label02.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label02.setPreferredSize(new java.awt.Dimension(33, 33));

        label10.setBackground(new java.awt.Color(255, 255, 255));
        label10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label10.setText("0");
        label10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label10.setPreferredSize(new java.awt.Dimension(33, 33));

        label11.setBackground(new java.awt.Color(255, 255, 255));
        label11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label11.setText("0");
        label11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label11.setPreferredSize(new java.awt.Dimension(33, 33));

        label12.setBackground(new java.awt.Color(255, 255, 255));
        label12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label12.setText("0");
        label12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label12.setPreferredSize(new java.awt.Dimension(33, 33));

        label20.setBackground(new java.awt.Color(255, 255, 255));
        label20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label20.setText("0");
        label20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label20.setPreferredSize(new java.awt.Dimension(33, 33));

        label21.setBackground(new java.awt.Color(255, 255, 255));
        label21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label21.setText("0");
        label21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label21.setPreferredSize(new java.awt.Dimension(33, 33));

        label22.setBackground(new java.awt.Color(255, 255, 255));
        label22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label22.setText("0");
        label22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label22.setPreferredSize(new java.awt.Dimension(33, 33));

        javax.swing.GroupLayout pnl1Layout = new javax.swing.GroupLayout(pnl1);
        pnl1.setLayout(pnl1Layout);
        pnl1Layout.setHorizontalGroup(
            pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl1Layout.createSequentialGroup()
                        .addComponent(label00, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl1Layout.createSequentialGroup()
                        .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl1Layout.createSequentialGroup()
                        .addComponent(label20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl1Layout.setVerticalGroup(
            pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label00, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl2.setBackground(new java.awt.Color(204, 204, 204));

        label03.setBackground(new java.awt.Color(255, 255, 255));
        label03.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label03.setText("0");
        label03.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label03.setPreferredSize(new java.awt.Dimension(33, 33));

        label04.setBackground(new java.awt.Color(255, 255, 255));
        label04.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label04.setText("0");
        label04.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label04.setPreferredSize(new java.awt.Dimension(33, 33));

        label05.setBackground(new java.awt.Color(255, 255, 255));
        label05.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label05.setText("0");
        label05.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label05.setPreferredSize(new java.awt.Dimension(33, 33));

        label13.setBackground(new java.awt.Color(255, 255, 255));
        label13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label13.setText("0");
        label13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label13.setPreferredSize(new java.awt.Dimension(33, 33));

        label14.setBackground(new java.awt.Color(255, 255, 255));
        label14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label14.setText("0");
        label14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label14.setPreferredSize(new java.awt.Dimension(33, 33));

        label15.setBackground(new java.awt.Color(255, 255, 255));
        label15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label15.setText("0");
        label15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label15.setPreferredSize(new java.awt.Dimension(33, 33));

        label23.setBackground(new java.awt.Color(255, 255, 255));
        label23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label23.setText("0");
        label23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label23.setPreferredSize(new java.awt.Dimension(33, 33));

        label24.setBackground(new java.awt.Color(255, 255, 255));
        label24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label24.setText("0");
        label24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label24.setPreferredSize(new java.awt.Dimension(33, 33));

        label25.setBackground(new java.awt.Color(255, 255, 255));
        label25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label25.setText("0");
        label25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label25.setPreferredSize(new java.awt.Dimension(33, 33));

        javax.swing.GroupLayout pnl2Layout = new javax.swing.GroupLayout(pnl2);
        pnl2.setLayout(pnl2Layout);
        pnl2Layout.setHorizontalGroup(
            pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl2Layout.createSequentialGroup()
                        .addComponent(label03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label04, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label05, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl2Layout.createSequentialGroup()
                        .addComponent(label13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl2Layout.createSequentialGroup()
                        .addComponent(label23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl2Layout.setVerticalGroup(
            pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label04, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label05, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl3.setBackground(new java.awt.Color(204, 204, 204));

        label06.setBackground(new java.awt.Color(255, 255, 255));
        label06.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label06.setText("0");
        label06.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label06.setPreferredSize(new java.awt.Dimension(33, 33));

        label07.setBackground(new java.awt.Color(255, 255, 255));
        label07.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label07.setText("0");
        label07.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label07.setPreferredSize(new java.awt.Dimension(33, 33));

        label08.setBackground(new java.awt.Color(255, 255, 255));
        label08.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label08.setText("0");
        label08.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label08.setPreferredSize(new java.awt.Dimension(33, 33));

        label16.setBackground(new java.awt.Color(255, 255, 255));
        label16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label16.setText("0");
        label16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label16.setPreferredSize(new java.awt.Dimension(33, 33));

        label17.setBackground(new java.awt.Color(255, 255, 255));
        label17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label17.setText("0");
        label17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label17.setPreferredSize(new java.awt.Dimension(33, 33));

        label18.setBackground(new java.awt.Color(255, 255, 255));
        label18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label18.setText("0");
        label18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label18.setPreferredSize(new java.awt.Dimension(33, 33));

        label26.setBackground(new java.awt.Color(255, 255, 255));
        label26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label26.setText("0");
        label26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label26.setPreferredSize(new java.awt.Dimension(33, 33));

        label27.setBackground(new java.awt.Color(255, 255, 255));
        label27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label27.setText("0");
        label27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label27.setPreferredSize(new java.awt.Dimension(33, 33));

        label28.setBackground(new java.awt.Color(255, 255, 255));
        label28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label28.setText("0");
        label28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label28.setPreferredSize(new java.awt.Dimension(33, 33));

        javax.swing.GroupLayout pnl3Layout = new javax.swing.GroupLayout(pnl3);
        pnl3.setLayout(pnl3Layout);
        pnl3Layout.setHorizontalGroup(
            pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl3Layout.createSequentialGroup()
                        .addComponent(label06, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label07, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label08, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl3Layout.createSequentialGroup()
                        .addComponent(label16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl3Layout.createSequentialGroup()
                        .addComponent(label26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl3Layout.setVerticalGroup(
            pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label06, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label07, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label08, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl4.setBackground(new java.awt.Color(204, 204, 204));

        label30.setBackground(new java.awt.Color(255, 255, 255));
        label30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label30.setText("0");
        label30.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label30.setPreferredSize(new java.awt.Dimension(33, 33));

        label31.setBackground(new java.awt.Color(255, 255, 255));
        label31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label31.setText("0");
        label31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label31.setPreferredSize(new java.awt.Dimension(33, 33));

        label32.setBackground(new java.awt.Color(255, 255, 255));
        label32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label32.setText("0");
        label32.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label32.setPreferredSize(new java.awt.Dimension(33, 33));

        label40.setBackground(new java.awt.Color(255, 255, 255));
        label40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label40.setText("0");
        label40.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label40.setPreferredSize(new java.awt.Dimension(33, 33));

        label41.setBackground(new java.awt.Color(255, 255, 255));
        label41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label41.setText("0");
        label41.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label41.setPreferredSize(new java.awt.Dimension(33, 33));

        label42.setBackground(new java.awt.Color(255, 255, 255));
        label42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label42.setText("0");
        label42.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label42.setPreferredSize(new java.awt.Dimension(33, 33));

        label50.setBackground(new java.awt.Color(255, 255, 255));
        label50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label50.setText("0");
        label50.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label50.setPreferredSize(new java.awt.Dimension(33, 33));

        label51.setBackground(new java.awt.Color(255, 255, 255));
        label51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label51.setText("0");
        label51.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label51.setPreferredSize(new java.awt.Dimension(33, 33));

        label52.setBackground(new java.awt.Color(255, 255, 255));
        label52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label52.setText("0");
        label52.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label52.setPreferredSize(new java.awt.Dimension(33, 33));

        javax.swing.GroupLayout pnl4Layout = new javax.swing.GroupLayout(pnl4);
        pnl4.setLayout(pnl4Layout);
        pnl4Layout.setHorizontalGroup(
            pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl4Layout.createSequentialGroup()
                        .addComponent(label30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl4Layout.createSequentialGroup()
                        .addComponent(label40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl4Layout.createSequentialGroup()
                        .addComponent(label50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl4Layout.setVerticalGroup(
            pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl5.setBackground(new java.awt.Color(204, 204, 204));

        label33.setBackground(new java.awt.Color(255, 255, 255));
        label33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label33.setText("0");
        label33.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label33.setPreferredSize(new java.awt.Dimension(33, 33));

        label34.setBackground(new java.awt.Color(255, 255, 255));
        label34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label34.setText("0");
        label34.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label34.setPreferredSize(new java.awt.Dimension(33, 33));

        label35.setBackground(new java.awt.Color(255, 255, 255));
        label35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label35.setText("0");
        label35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label35.setPreferredSize(new java.awt.Dimension(33, 33));

        label43.setBackground(new java.awt.Color(255, 255, 255));
        label43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label43.setText("0");
        label43.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label43.setPreferredSize(new java.awt.Dimension(33, 33));

        label44.setBackground(new java.awt.Color(255, 255, 255));
        label44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label44.setText("0");
        label44.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label44.setPreferredSize(new java.awt.Dimension(33, 33));

        label45.setBackground(new java.awt.Color(255, 255, 255));
        label45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label45.setText("0");
        label45.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label45.setPreferredSize(new java.awt.Dimension(33, 33));

        label53.setBackground(new java.awt.Color(255, 255, 255));
        label53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label53.setText("0");
        label53.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label53.setPreferredSize(new java.awt.Dimension(33, 33));

        label54.setBackground(new java.awt.Color(255, 255, 255));
        label54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label54.setText("0");
        label54.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label54.setPreferredSize(new java.awt.Dimension(33, 33));

        label55.setBackground(new java.awt.Color(255, 255, 255));
        label55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label55.setText("0");
        label55.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label55.setPreferredSize(new java.awt.Dimension(33, 33));

        javax.swing.GroupLayout pnl5Layout = new javax.swing.GroupLayout(pnl5);
        pnl5.setLayout(pnl5Layout);
        pnl5Layout.setHorizontalGroup(
            pnl5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl5Layout.createSequentialGroup()
                        .addComponent(label33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl5Layout.createSequentialGroup()
                        .addComponent(label43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl5Layout.createSequentialGroup()
                        .addComponent(label53, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label54, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl5Layout.setVerticalGroup(
            pnl5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label53, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label54, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label55, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl6.setBackground(new java.awt.Color(204, 204, 204));

        label36.setBackground(new java.awt.Color(255, 255, 255));
        label36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label36.setText("0");
        label36.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label36.setPreferredSize(new java.awt.Dimension(33, 33));

        label37.setBackground(new java.awt.Color(255, 255, 255));
        label37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label37.setText("0");
        label37.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label37.setPreferredSize(new java.awt.Dimension(33, 33));

        label38.setBackground(new java.awt.Color(255, 255, 255));
        label38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label38.setText("0");
        label38.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label38.setPreferredSize(new java.awt.Dimension(33, 33));

        label46.setBackground(new java.awt.Color(255, 255, 255));
        label46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label46.setText("0");
        label46.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label46.setPreferredSize(new java.awt.Dimension(33, 33));

        label47.setBackground(new java.awt.Color(255, 255, 255));
        label47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label47.setText("0");
        label47.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label47.setPreferredSize(new java.awt.Dimension(33, 33));

        label48.setBackground(new java.awt.Color(255, 255, 255));
        label48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label48.setText("0");
        label48.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label48.setPreferredSize(new java.awt.Dimension(33, 33));

        label56.setBackground(new java.awt.Color(255, 255, 255));
        label56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label56.setText("0");
        label56.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label56.setPreferredSize(new java.awt.Dimension(33, 33));

        label57.setBackground(new java.awt.Color(255, 255, 255));
        label57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label57.setText("0");
        label57.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label57.setPreferredSize(new java.awt.Dimension(33, 33));

        label58.setBackground(new java.awt.Color(255, 255, 255));
        label58.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label58.setText("0");
        label58.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label58.setPreferredSize(new java.awt.Dimension(33, 33));

        javax.swing.GroupLayout pnl6Layout = new javax.swing.GroupLayout(pnl6);
        pnl6.setLayout(pnl6Layout);
        pnl6Layout.setHorizontalGroup(
            pnl6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl6Layout.createSequentialGroup()
                        .addComponent(label36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl6Layout.createSequentialGroup()
                        .addComponent(label46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl6Layout.createSequentialGroup()
                        .addComponent(label56, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label58, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl6Layout.setVerticalGroup(
            pnl6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label56, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label58, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl7.setBackground(new java.awt.Color(204, 204, 204));

        label60.setBackground(new java.awt.Color(255, 255, 255));
        label60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label60.setText("0");
        label60.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label60.setPreferredSize(new java.awt.Dimension(33, 33));

        label61.setBackground(new java.awt.Color(255, 255, 255));
        label61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label61.setText("0");
        label61.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label61.setPreferredSize(new java.awt.Dimension(33, 33));

        label62.setBackground(new java.awt.Color(255, 255, 255));
        label62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label62.setText("0");
        label62.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label62.setPreferredSize(new java.awt.Dimension(33, 33));

        label70.setBackground(new java.awt.Color(255, 255, 255));
        label70.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label70.setText("0");
        label70.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label70.setPreferredSize(new java.awt.Dimension(33, 33));

        label71.setBackground(new java.awt.Color(255, 255, 255));
        label71.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label71.setText("0");
        label71.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label71.setPreferredSize(new java.awt.Dimension(33, 33));

        label72.setBackground(new java.awt.Color(255, 255, 255));
        label72.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label72.setText("0");
        label72.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label72.setPreferredSize(new java.awt.Dimension(33, 33));

        label80.setBackground(new java.awt.Color(255, 255, 255));
        label80.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label80.setText("0");
        label80.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label80.setPreferredSize(new java.awt.Dimension(33, 33));

        label81.setBackground(new java.awt.Color(255, 255, 255));
        label81.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label81.setText("0");
        label81.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label81.setPreferredSize(new java.awt.Dimension(33, 33));

        label82.setBackground(new java.awt.Color(255, 255, 255));
        label82.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label82.setText("0");
        label82.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label82.setPreferredSize(new java.awt.Dimension(33, 33));

        javax.swing.GroupLayout pnl7Layout = new javax.swing.GroupLayout(pnl7);
        pnl7.setLayout(pnl7Layout);
        pnl7Layout.setHorizontalGroup(
            pnl7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl7Layout.createSequentialGroup()
                        .addComponent(label60, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label61, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label62, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl7Layout.createSequentialGroup()
                        .addComponent(label70, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label71, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label72, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl7Layout.createSequentialGroup()
                        .addComponent(label80, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label81, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label82, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl7Layout.setVerticalGroup(
            pnl7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label60, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label61, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label62, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label71, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label72, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label70, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label80, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label81, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label82, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl8.setBackground(new java.awt.Color(204, 204, 204));

        label63.setBackground(new java.awt.Color(255, 255, 255));
        label63.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label63.setText("0");
        label63.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label63.setPreferredSize(new java.awt.Dimension(33, 33));

        label64.setBackground(new java.awt.Color(255, 255, 255));
        label64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label64.setText("0");
        label64.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label64.setPreferredSize(new java.awt.Dimension(33, 33));

        label65.setBackground(new java.awt.Color(255, 255, 255));
        label65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label65.setText("0");
        label65.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label65.setPreferredSize(new java.awt.Dimension(33, 33));

        label73.setBackground(new java.awt.Color(255, 255, 255));
        label73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label73.setText("0");
        label73.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label73.setPreferredSize(new java.awt.Dimension(33, 33));

        label74.setBackground(new java.awt.Color(255, 255, 255));
        label74.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label74.setText("0");
        label74.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label74.setPreferredSize(new java.awt.Dimension(33, 33));

        label75.setBackground(new java.awt.Color(255, 255, 255));
        label75.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label75.setText("0");
        label75.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label75.setPreferredSize(new java.awt.Dimension(33, 33));

        label83.setBackground(new java.awt.Color(255, 255, 255));
        label83.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label83.setText("0");
        label83.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label83.setPreferredSize(new java.awt.Dimension(33, 33));

        label84.setBackground(new java.awt.Color(255, 255, 255));
        label84.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label84.setText("0");
        label84.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label84.setPreferredSize(new java.awt.Dimension(33, 33));

        label85.setBackground(new java.awt.Color(255, 255, 255));
        label85.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label85.setText("0");
        label85.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label85.setPreferredSize(new java.awt.Dimension(33, 33));

        javax.swing.GroupLayout pnl8Layout = new javax.swing.GroupLayout(pnl8);
        pnl8.setLayout(pnl8Layout);
        pnl8Layout.setHorizontalGroup(
            pnl8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl8Layout.createSequentialGroup()
                        .addComponent(label63, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label64, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label65, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl8Layout.createSequentialGroup()
                        .addComponent(label73, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label74, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label75, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl8Layout.createSequentialGroup()
                        .addComponent(label83, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label84, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label85, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl8Layout.setVerticalGroup(
            pnl8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label63, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label64, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label65, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label74, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label75, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label73, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label83, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label84, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label85, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnl9.setBackground(new java.awt.Color(204, 204, 204));

        label66.setBackground(new java.awt.Color(255, 255, 255));
        label66.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label66.setText("0");
        label66.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label66.setPreferredSize(new java.awt.Dimension(33, 33));

        label67.setBackground(new java.awt.Color(255, 255, 255));
        label67.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label67.setText("0");
        label67.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label67.setPreferredSize(new java.awt.Dimension(33, 33));

        label68.setBackground(new java.awt.Color(255, 255, 255));
        label68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label68.setText("0");
        label68.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label68.setPreferredSize(new java.awt.Dimension(33, 33));

        label76.setBackground(new java.awt.Color(255, 255, 255));
        label76.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label76.setText("0");
        label76.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label76.setPreferredSize(new java.awt.Dimension(33, 33));

        label77.setBackground(new java.awt.Color(255, 255, 255));
        label77.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label77.setText("0");
        label77.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label77.setPreferredSize(new java.awt.Dimension(33, 33));

        label78.setBackground(new java.awt.Color(255, 255, 255));
        label78.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label78.setText("0");
        label78.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label78.setPreferredSize(new java.awt.Dimension(33, 33));

        label86.setBackground(new java.awt.Color(255, 255, 255));
        label86.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label86.setText("0");
        label86.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label86.setPreferredSize(new java.awt.Dimension(33, 33));

        label87.setBackground(new java.awt.Color(255, 255, 255));
        label87.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label87.setText("0");
        label87.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label87.setPreferredSize(new java.awt.Dimension(33, 33));

        label88.setBackground(new java.awt.Color(255, 255, 255));
        label88.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label88.setText("0");
        label88.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        label88.setPreferredSize(new java.awt.Dimension(33, 33));

        javax.swing.GroupLayout pnl9Layout = new javax.swing.GroupLayout(pnl9);
        pnl9.setLayout(pnl9Layout);
        pnl9Layout.setHorizontalGroup(
            pnl9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl9Layout.createSequentialGroup()
                        .addComponent(label66, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label67, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label68, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl9Layout.createSequentialGroup()
                        .addComponent(label76, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label77, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label78, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl9Layout.createSequentialGroup()
                        .addComponent(label86, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label87, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label88, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl9Layout.setVerticalGroup(
            pnl9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label66, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label67, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label68, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label77, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label78, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label76, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnl9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label86, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label87, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label88, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlRascunho.setBackground(new java.awt.Color(204, 204, 204));

        labelRascunho01.setBackground(new java.awt.Color(255, 255, 255));
        labelRascunho01.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRascunho01.setText("0");
        labelRascunho01.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelRascunho01.setPreferredSize(new java.awt.Dimension(33, 33));

        labelRascunho02.setBackground(new java.awt.Color(255, 255, 255));
        labelRascunho02.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRascunho02.setText("0");
        labelRascunho02.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelRascunho02.setPreferredSize(new java.awt.Dimension(33, 33));

        labelRascunho03.setBackground(new java.awt.Color(255, 255, 255));
        labelRascunho03.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRascunho03.setText("0");
        labelRascunho03.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelRascunho03.setPreferredSize(new java.awt.Dimension(33, 33));

        labelRascunho04.setBackground(new java.awt.Color(255, 255, 255));
        labelRascunho04.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRascunho04.setText("0");
        labelRascunho04.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelRascunho04.setPreferredSize(new java.awt.Dimension(33, 33));

        labelRascunho05.setBackground(new java.awt.Color(255, 255, 255));
        labelRascunho05.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRascunho05.setText("0");
        labelRascunho05.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelRascunho05.setPreferredSize(new java.awt.Dimension(33, 33));

        labelRascunho06.setBackground(new java.awt.Color(255, 255, 255));
        labelRascunho06.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRascunho06.setText("0");
        labelRascunho06.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelRascunho06.setPreferredSize(new java.awt.Dimension(33, 33));

        labelRascunho07.setBackground(new java.awt.Color(255, 255, 255));
        labelRascunho07.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRascunho07.setText("0");
        labelRascunho07.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelRascunho07.setPreferredSize(new java.awt.Dimension(33, 33));

        labelRascunho08.setBackground(new java.awt.Color(255, 255, 255));
        labelRascunho08.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRascunho08.setText("0");
        labelRascunho08.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelRascunho08.setPreferredSize(new java.awt.Dimension(33, 33));

        labelRascunho09.setBackground(new java.awt.Color(255, 255, 255));
        labelRascunho09.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRascunho09.setText("0");
        labelRascunho09.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        labelRascunho09.setPreferredSize(new java.awt.Dimension(33, 33));

        javax.swing.GroupLayout pnlRascunhoLayout = new javax.swing.GroupLayout(pnlRascunho);
        pnlRascunho.setLayout(pnlRascunhoLayout);
        pnlRascunhoLayout.setHorizontalGroup(
            pnlRascunhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRascunhoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRascunhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRascunhoLayout.createSequentialGroup()
                        .addComponent(labelRascunho01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelRascunho02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelRascunho03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRascunhoLayout.createSequentialGroup()
                        .addComponent(labelRascunho04, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelRascunho05, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelRascunho06, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRascunhoLayout.createSequentialGroup()
                        .addComponent(labelRascunho07, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelRascunho08, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelRascunho09, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlRascunhoLayout.setVerticalGroup(
            pnlRascunhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRascunhoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRascunhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelRascunho01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelRascunho02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelRascunho03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRascunhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelRascunho05, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelRascunho06, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelRascunho04, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRascunhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelRascunho07, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelRascunho08, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelRascunho09, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("Tempus Sans ITC", 1, 12)); // NOI18N
        jButton1.setText("Novo Game!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tempus Sans ITC", 1, 12)); // NOI18N
        jButton2.setText("Validar Game!");
        jButton2.setToolTipText("");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tempus Sans ITC", 1, 12)); // NOI18N
        jLabel3.setText("Rascunho:");

        tfValor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfValor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfValorActionPerformed(evt);
            }
        });

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnLimparCampos.setText("Limpar Campos!");
        btnLimparCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparCamposActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpanelPrincipalLayout = new javax.swing.GroupLayout(jpanelPrincipal);
        jpanelPrincipal.setLayout(jpanelPrincipalLayout);
        jpanelPrincipalLayout.setHorizontalGroup(
            jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanelPrincipalLayout.createSequentialGroup()
                .addGroup(jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpanelPrincipalLayout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addGroup(jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnl7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnl4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnl1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpanelPrincipalLayout.createSequentialGroup()
                                .addComponent(pnl2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnl3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jpanelPrincipalLayout.createSequentialGroup()
                                    .addComponent(pnl5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(pnl6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jpanelPrincipalLayout.createSequentialGroup()
                                    .addComponent(pnl8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(pnl9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jpanelPrincipalLayout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addGroup(jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton2)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(36, 36, 36)
                        .addGroup(jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfValor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pnlRascunho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpanelPrincipalLayout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(34, 34, 34)))
                            .addComponent(btnLimparCampos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jpanelPrincipalLayout.createSequentialGroup()
                        .addGap(280, 280, 280)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jpanelPrincipalLayout.createSequentialGroup()
                        .addGap(233, 233, 233)
                        .addComponent(jLabel2)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jpanelPrincipalLayout.setVerticalGroup(
            jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpanelPrincipalLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(pnl1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pnl2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pnl3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnl5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnl4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnl6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnl9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnl8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnl7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(jpanelPrincipalLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlRascunho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfValor, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimparCampos)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jpanelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        geraTabuleiroSudoKu();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void label00MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_label00MousePressed
        label00.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLUE, 3));
    }//GEN-LAST:event_label00MousePressed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        if (labelSelecionada != null) {
            labelSelecionada.setText(tfValor.getText());

            if (labelSelecionada.getName() != null) {
                String ultimasDuas = labelSelecionada.getName().substring(labelSelecionada.getName().length() - 2);
                int linha = Character.getNumericValue(ultimasDuas.charAt(0));
                int coluna = Character.getNumericValue(ultimasDuas.charAt(1));

                quadrantes.stream().flatMap(m -> m.getCampos().stream())
                        .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                        .forEach(c -> c.setValor(tfValor.getText()));
            }

        } else {
            // Exibe uma janela de aviso
            JOptionPane.showMessageDialog(
                    this, // frame pai, pode ser 'null' se não houver
                    "Nenhum campo selecionado!", // mensagem
                    "Aviso", // título da janela
                    JOptionPane.WARNING_MESSAGE // tipo de mensagem
            );
        }

    }//GEN-LAST:event_btnOkActionPerformed

    private void btnLimparCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparCamposActionPerformed
        limpaCampos();
    }//GEN-LAST:event_btnLimparCamposActionPerformed

    private void tfValorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfValorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfValorActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (ehTabuleiroValido() && ehTabuleiroCompleto()) {
            JOptionPane.showMessageDialog(
                    this, // frame pai, pode ser 'null' se não houver
                    "Você Venceu! Paranbéns!!!", // mensagem
                    "Aviso", // título da janela
                    JOptionPane.WARNING_MESSAGE // tipo de mensagem
            );
        } else {
            JOptionPane.showMessageDialog(
                    this, // frame pai, pode ser 'null' se não houver
                    "Você falhou!!!", // mensagem
                    "Aviso", // título da janela
                    JOptionPane.WARNING_MESSAGE // tipo de mensagem
            );
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sudo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sudo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sudo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sudo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sudo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLimparCampos;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jpanelPrincipal;
    private javax.swing.JLabel label00;
    private javax.swing.JLabel label01;
    private javax.swing.JLabel label02;
    private javax.swing.JLabel label03;
    private javax.swing.JLabel label04;
    private javax.swing.JLabel label05;
    private javax.swing.JLabel label06;
    private javax.swing.JLabel label07;
    private javax.swing.JLabel label08;
    private javax.swing.JLabel label10;
    private javax.swing.JLabel label11;
    private javax.swing.JLabel label12;
    private javax.swing.JLabel label13;
    private javax.swing.JLabel label14;
    private javax.swing.JLabel label15;
    private javax.swing.JLabel label16;
    private javax.swing.JLabel label17;
    private javax.swing.JLabel label18;
    private javax.swing.JLabel label20;
    private javax.swing.JLabel label21;
    private javax.swing.JLabel label22;
    private javax.swing.JLabel label23;
    private javax.swing.JLabel label24;
    private javax.swing.JLabel label25;
    private javax.swing.JLabel label26;
    private javax.swing.JLabel label27;
    private javax.swing.JLabel label28;
    private javax.swing.JLabel label30;
    private javax.swing.JLabel label31;
    private javax.swing.JLabel label32;
    private javax.swing.JLabel label33;
    private javax.swing.JLabel label34;
    private javax.swing.JLabel label35;
    private javax.swing.JLabel label36;
    private javax.swing.JLabel label37;
    private javax.swing.JLabel label38;
    private javax.swing.JLabel label40;
    private javax.swing.JLabel label41;
    private javax.swing.JLabel label42;
    private javax.swing.JLabel label43;
    private javax.swing.JLabel label44;
    private javax.swing.JLabel label45;
    private javax.swing.JLabel label46;
    private javax.swing.JLabel label47;
    private javax.swing.JLabel label48;
    private javax.swing.JLabel label50;
    private javax.swing.JLabel label51;
    private javax.swing.JLabel label52;
    private javax.swing.JLabel label53;
    private javax.swing.JLabel label54;
    private javax.swing.JLabel label55;
    private javax.swing.JLabel label56;
    private javax.swing.JLabel label57;
    private javax.swing.JLabel label58;
    private javax.swing.JLabel label60;
    private javax.swing.JLabel label61;
    private javax.swing.JLabel label62;
    private javax.swing.JLabel label63;
    private javax.swing.JLabel label64;
    private javax.swing.JLabel label65;
    private javax.swing.JLabel label66;
    private javax.swing.JLabel label67;
    private javax.swing.JLabel label68;
    private javax.swing.JLabel label70;
    private javax.swing.JLabel label71;
    private javax.swing.JLabel label72;
    private javax.swing.JLabel label73;
    private javax.swing.JLabel label74;
    private javax.swing.JLabel label75;
    private javax.swing.JLabel label76;
    private javax.swing.JLabel label77;
    private javax.swing.JLabel label78;
    private javax.swing.JLabel label80;
    private javax.swing.JLabel label81;
    private javax.swing.JLabel label82;
    private javax.swing.JLabel label83;
    private javax.swing.JLabel label84;
    private javax.swing.JLabel label85;
    private javax.swing.JLabel label86;
    private javax.swing.JLabel label87;
    private javax.swing.JLabel label88;
    private javax.swing.JLabel labelRascunho01;
    private javax.swing.JLabel labelRascunho02;
    private javax.swing.JLabel labelRascunho03;
    private javax.swing.JLabel labelRascunho04;
    private javax.swing.JLabel labelRascunho05;
    private javax.swing.JLabel labelRascunho06;
    private javax.swing.JLabel labelRascunho07;
    private javax.swing.JLabel labelRascunho08;
    private javax.swing.JLabel labelRascunho09;
    private javax.swing.JPanel pnl1;
    private javax.swing.JPanel pnl2;
    private javax.swing.JPanel pnl3;
    private javax.swing.JPanel pnl4;
    private javax.swing.JPanel pnl5;
    private javax.swing.JPanel pnl6;
    private javax.swing.JPanel pnl7;
    private javax.swing.JPanel pnl8;
    private javax.swing.JPanel pnl9;
    private javax.swing.JPanel pnlRascunho;
    private javax.swing.JTextField tfValor;
    // End of variables declaration//GEN-END:variables
}
