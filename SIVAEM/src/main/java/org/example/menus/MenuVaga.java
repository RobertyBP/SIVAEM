package org.example.menus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.candidatos.Logout;
import org.example.vaga.PesqVaga;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MenuVaga {
    public static void menuVaga(BufferedReader stdIn, PrintWriter out, BufferedReader in, String token, Socket echoSocket) throws IOException {
        boolean flag = true;
        while (flag) {
            System.out.println("1-pesquisar 3-menuGeral 4-logout");
            int op = Integer.parseInt(stdIn.readLine());

            switch (op) {
                case 1:
                    System.out.println("filtrar Vagas");

                    System.out.println("Competências (separadas por vírgula):");
                    String[] competenciasArray = stdIn.readLine().split(",");
                    List<String> competencias = new ArrayList<>();
                    for (String competencia : competenciasArray) {
                        competencias.add(competencia.trim());
                    }
                    System.out.println("tipo AND ou OR");
                    String tipo = stdIn.readLine();
                    PesqVaga.Filtros pesq = new PesqVaga.Filtros(competencias,tipo);

                    PesqVaga pesqVagas = new PesqVaga("filtrarVagas",pesq,token);
                    final var objectMapperF = new ObjectMapper();
                    final String jsonF = objectMapperF.writeValueAsString(pesqVagas);
                    System.out.println(jsonF);
                    out.println(jsonF);

                    System.out.println("Client: " + in.readLine());

                    break;
                case 3:
                    System.out.println("menu geral");
                    MenuGeral.menuGeral(stdIn, out, in, token, echoSocket);
                    break;
                case 4:
                    flag = Logout.logout(token,out,in,echoSocket);
                    break;
                default:
                    System.out.println("error");
                    break;
            }
        }

    }
}
