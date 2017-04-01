package interpretador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;

/**
 *
 * @author Gustavo
 */
public class Sistema {

    private Long id;
    private Map<String, String> palavrasReservadas;
    private List<String> numeros;

    public Sistema() {
        this.id = 1L;
        palavras();
        numeros();
    }

    private void numeros() {
        this.numeros = new ArrayList<>();
        this.numeros.add("0");
        this.numeros.add("1");
        this.numeros.add("2");
        this.numeros.add("3");
        this.numeros.add("4");
        this.numeros.add("5");
        this.numeros.add("6");
        this.numeros.add("7");
        this.numeros.add("8");
        this.numeros.add("9");
    }

    private void palavras() {
        this.palavrasReservadas = new HashMap<>();
        this.palavrasReservadas.put("<", "[Relational_Op, <]");
        this.palavrasReservadas.put("<=", "[Relational_Op, <=]");
        this.palavrasReservadas.put("==", "[Relational_Op, ==]");
        this.palavrasReservadas.put("!=", "[Relational_Op, !=]");
        this.palavrasReservadas.put(">=", "[Relational_Op, >=]");
        this.palavrasReservadas.put(">", "[Relational_Op, >]");
        this.palavrasReservadas.put("+", "[Arith_ Op, +]");
        this.palavrasReservadas.put("-", "[Arith_ Op, -]");
        this.palavrasReservadas.put("*", "[Arith_ Op, *]");
        this.palavrasReservadas.put("/", "[Arith_ Op, /]");
        this.palavrasReservadas.put(";", "");
        this.palavrasReservadas.put(" ", "");
    }

    public void realizaInterpretacao() throws FileNotFoundException, IOException {
        BufferedReader in;
        FileReader fe;
        JFileChooser jfc = new JFileChooser();
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { //selecao da arquivo
            fe = new FileReader(jfc.getSelectedFile());
            in = new BufferedReader(fe);
        } else {
            return;
        }

        String linha;
        char before = '-';
        char after = '-';
        String aux = "";
        Integer cont = 1;
        while (null != (linha = in.readLine())) {
            String result = "";
            linha = linha.replace("(", "").replace(")", "").replace(";", "");
            for (int i = 0; i < linha.length(); i++) {
                aux += linha.charAt(i);
                aux = aux.replace(" ", "");

                try {
                    before = linha.charAt(i - 1);
                } catch (IndexOutOfBoundsException e) {
                    before = ' ';
                }

                try {
                    after = linha.charAt(i + 1);
                } catch (IndexOutOfBoundsException e) {
                    after = ' ';
                }

                if (aux.equalsIgnoreCase("/") && after == '/') {
                    break;
                } else if (aux.equalsIgnoreCase("/") && after == '*') {

                } else if (aux.equalsIgnoreCase("=") && after != '=') {
                    result += ("[Equal_Op, =]");
                    aux = "";
                } else if (null != verificaPalavraReservada(aux)) {
                    result += verificaPalavraReservada(aux);
                    aux = "";
                } else if (null != this.palavrasReservadas.get(aux)) {
                    result += this.palavrasReservadas.get(aux);
                    aux = "";
                } else if (isNumber(aux)) {
                    String auxN = aux;
                    if (isNumber(after + "")) {
                        Integer contAux = i;
                        try {
                            while (isNumber(linha.charAt(++contAux) + "")) {
                                auxN += linha.charAt(contAux);
                            }
                        } catch (java.lang.StringIndexOutOfBoundsException e) {
                        }
                        i = contAux;
                    }
                    result += "[num, " + auxN + "]";

                    aux = "";
                } else if (!verificaAuxIsSpecial(aux) && after == ' ' && !aux.trim().equalsIgnoreCase("")) {
                    result += "[ID, " + cont++ + "]";
                    aux = "";
                }
            }
            System.out.println(result);
            result = "";
        }
    }

    private Boolean isNumber(String aux) {
        for (String numero : this.numeros) {
            if (numero.equalsIgnoreCase(aux.trim())) {
                return true;
            }
        }
        return false;
    }

    private Boolean verificaAuxIsSpecial(String word) {
        for (Map.Entry<String, String> entry : this.palavrasReservadas.entrySet()) {
            if (entry.getKey().trim().equalsIgnoreCase(word.trim())) {
                return true;
            }
        }
        return false;
    }

    private String verificaPalavraReservada(String aux) {
        if (aux.equalsIgnoreCase("if")) {
            return "[reserved_word, if] ";
        } else if (aux.equalsIgnoreCase("else")) {
            return "[reserved_word, else] ";
        } else if (aux.equalsIgnoreCase("do")) {
            return "[reserved_word, do] ";
        } else if (aux.equalsIgnoreCase("while")) {
            return "[reserved_word, while] ";
        } else if (aux.equalsIgnoreCase("for")) {
            return "[reserved_word, for] ";
        } else if (aux.equalsIgnoreCase("print")) {
            return "[reserved_word, print] ";
        } else if (aux.equalsIgnoreCase("return")) {
            return "[reserved_word, return] ";
        } else if (aux.equalsIgnoreCase("int")) {
            return "[reserved_word, int] ";
        } else if (aux.equalsIgnoreCase("null")) {
            return "[reserved_word, null] ";
        } else if (aux.equalsIgnoreCase("float")) {
            return "[reserved_word, float] ";
        } else if (aux.equalsIgnoreCase("double")) {
            return "[reserved_word, double] ";
        } else if (aux.equalsIgnoreCase("string")) {
            return "[reserved_word, string] ";
        } else if (aux.equalsIgnoreCase("bool")) {
            return "[reserved_word, bool] ";
        } else {
            return null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
