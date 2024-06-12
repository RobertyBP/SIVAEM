package org.example.menus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.candidatos.AltCandidato;
import org.example.candidatos.Logout;
import org.example.candidatos.VisCandidato;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MenuCandidato {
    public static void menu_candidato(BufferedReader stdIn, PrintWriter out, BufferedReader in, String token, Socket echoSocket) throws IOException {
        boolean flag = true;
        while (flag) {
            System.out.println("1-visualizar, 2-editar, 3-apagar 4-menu geral 6-logout");
            int op = Integer.parseInt(stdIn.readLine());

            switch (op) {
                case 1:
                    System.out.println("visualizar");
                    System.out.println("email:");
                    String emailC = stdIn.readLine();

                    VisCandidato cad = new VisCandidato("visualizarCandidato", emailC, token);
                    final var objectMapperC = new ObjectMapper();
                    final String jsonC = objectMapperC.writeValueAsString(cad);
                    System.out.println(jsonC);
                    out.println(jsonC);
                    System.out.println("client: " + in.readLine());
                    break;
                case 2:
                    System.out.println("atualizar");
                    System.out.println("email:");
                    String emailE = stdIn.readLine();
                    System.out.println("nome:");
                    String nomeE = stdIn.readLine();
                    System.out.println("senha");
                    String senhaE = stdIn.readLine();
                    AltCandidato cad1 = new AltCandidato("atualizarCandidato", nomeE, emailE, senhaE, token);
                    final var objectMapperE = new ObjectMapper();
                    final String jsonE = objectMapperE.writeValueAsString(cad1);
                    System.out.println(jsonE);
                    out.println(jsonE);
                    System.out.println("client: " + in.readLine());
                    break;
                case 3:
                    System.out.println("apagar");
                    System.out.println("email:");
                    String emailA = stdIn.readLine();

                    VisCandidato cad2 = new VisCandidato("apagarCandidato", emailA, token);
                    final var objectMapperA = new ObjectMapper();
                    final String jsonA = objectMapperA.writeValueAsString(cad2);
                    System.out.println(jsonA);
                    out.println(jsonA);
                    System.out.println("client: " + in.readLine());
                    break;
                case 4:
                    System.out.println("menu geral");
                    MenuGeral.menuGeral(stdIn, out, in, token, echoSocket);
                    break;
                case 6:
                    flag = Logout.logout(token,out,in,echoSocket);
                    break;
                default:
                    System.out.println("error");
                    break;
            }
        }

    }
}
