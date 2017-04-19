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
    private Map<String, String> idsUtilizados;

    public Sistema() {
        this.id = 1L;
        palavras();
        numeros();
        this.idsUtilizados = new HashMap<>();
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
        Character before = '-';
        Character after = '-';
        String aux = "";
        Integer cont = 1;
        linha = in.readLine();
        while (null != linha) {
            if (linha.trim().equalsIgnoreCase("")) {
                linha = in.readLine();
                continue;
            }
            String result = "";
            linha = linha.replace("(", "").replace(")", "").replace(";", "").replace("{", "").replace("}", "");
            for (int i = 0; i < linha.length(); i++) {
                aux += linha.charAt(i);
                aux = aux.replace(" ", "");
                aux = aux.trim();

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
                    aux = "";
                    break;
                } else if (aux.equalsIgnoreCase("/") && after == '*') {
                    Integer auxCont = i + 1;
                    while (!aux.contains("*/")) {
                        try {
                            aux += linha.charAt(auxCont++);
                        } catch (IndexOutOfBoundsException | java.lang.NullPointerException e) {
                            auxCont = 0;
                            linha = in.readLine();
                        }
                    }
                    aux = "";
                    i = auxCont;
                } else if (aux.equalsIgnoreCase("=") && after != '=') {
                    result += ("[Equal_Op, =]");
                    aux = "";
                } else if (null != verificaPalavraReservada(aux) && after != 'u') {
                    result += verificaPalavraReservada(aux);
                    aux = "";
                } else if (null != this.palavrasReservadas.get(aux)) {
                    result += this.palavrasReservadas.get(aux);
                    aux = "";
                } else if (isNumber(aux)) {
                    String auxN = aux;
                    if (after == '.' || isNumber(after.toString())) {
                        auxN += after;
                        Integer contAux = i + 1;
                        try {
                            while (isNumber(linha.charAt(++contAux) + "") || linha.charAt(contAux) == '.') {
                                auxN += linha.charAt(contAux);
                            }
                        } catch (java.lang.StringIndexOutOfBoundsException e) {
                        }
                        i = contAux;
                    }
                    result += "[num, " + auxN + "]";

                    aux = "";
                } else if (!verificaAuxIsSpecial(aux) && after == ' ' && !aux.trim().equalsIgnoreCase("")) {
                    if (null == this.idsUtilizados.get(aux)) {
                        result += "[ID, " + cont + "]";
                        this.idsUtilizados.put(aux, "[ID, " + cont + "]");
                        cont++;
                    } else {
                        result += this.idsUtilizados.get(aux);
                    }
                    aux = "";
                } else if (aux.equalsIgnoreCase("\"")) {
                    aux += linha.charAt(++i);
                    while (!aux.equalsIgnoreCase("\"")) {
                        try {
                            aux += linha.charAt(++i);
                        } catch (IndexOutOfBoundsException e) {
                            break;
                        }
                    }
                    result += "[String, " + aux.replace("\"", "") + "]";
                    aux = "";
                }
            }
            if (!result.trim().equalsIgnoreCase("")) {
                System.out.println(result);
            }
            linha = in.readLine();
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
        aux = aux.trim();
        if (aux.equalsIgnoreCase("if")) {
            return "[reserved_word, if] ";
        } else if (aux.equalsIgnoreCase("else")) {
            return "[reserved_word, else] ";
        } else if (aux.equalsIgnoreCase("double")) {
            return "[reserved_word, double] ";
        } else if (aux.equalsIgnoreCase("do")) {
            return "[reserved_word, do] ";
        } else if (aux.equalsIgnoreCase("while")) {
            return "[reserved_word, while] ";
        } else if (aux.equalsIgnoreCase("for")) {
            return "[reserved_word, for] ";
        } else if (aux.equalsIgnoreCase("printf")) {
            return "[reserved_word, printf] ";
        } else if (aux.equalsIgnoreCase("return")) {
            return "[reserved_word, return] ";
        } else if (aux.equalsIgnoreCase("int")) {
            return "[reserved_word, int] ";
        } else if (aux.equalsIgnoreCase("null")) {
            return "[reserved_word, null] ";
        } else if (aux.equalsIgnoreCase("float")) {
            return "[reserved_word, float] ";
        } else if (aux.equalsIgnoreCase("string")) {
            return "[reserved_word, string] ";
        } else if (aux.equalsIgnoreCase("bool")) {
            return "[reserved_word, bool] ";
        } else if (aux.equalsIgnoreCase("true")
                || aux.equalsIgnoreCase("false")) {
            return "[logic, " + aux + "] ";
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

    public Map<String, String> getPalavrasReservadas() {
        return palavrasReservadas;
    }

    public void setPalavrasReservadas(Map<String, String> palavrasReservadas) {
        this.palavrasReservadas = palavrasReservadas;
    }

    public List<String> getNumeros() {
        return numeros;
    }

    public void setNumeros(List<String> numeros) {
        this.numeros = numeros;
    }

    public Map<String, String> getIdsUtilizados() {
        return idsUtilizados;
    }

    public void setIdsUtilizados(Map<String, String> idsUtilizados) {
        this.idsUtilizados = idsUtilizados;
    }
}
