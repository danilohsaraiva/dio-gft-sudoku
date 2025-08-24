package validators;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author danns
 */
public class validadorNumero1ao9eInterrogacao extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (ehValido(fb.getDocument().getText(0, fb.getDocument().getLength()) + string)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        String conteudoAtual = fb.getDocument().getText(0, fb.getDocument().getLength());
        String novoConteudo = conteudoAtual.substring(0, offset) + text + conteudoAtual.substring(offset + length);

        if (ehValido(novoConteudo)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean ehValido(String text) {
        return text.isEmpty() || text.matches("[1-9?]");
    }

}
