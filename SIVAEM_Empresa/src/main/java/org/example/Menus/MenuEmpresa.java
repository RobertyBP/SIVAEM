package org.example.Menus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.empresas.AtlEmpresa;
import org.example.empresas.Logout;
import org.example.empresas.VisEmpresa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MenuEmpresa {
    public static void menuEmpresa(BufferedReader stdIn, PrintWriter out, BufferedReader in, String token, Socket echoSocket) throws IOException {
        boolean flag = true;
        while (flag) {
            System.out.println("1-visualizar, 2-editar, 3-apagar 5-logout");
            int op = Integer.parseInt(stdIn.readLine());

            switch (op) {
                case 1:
                    System.out.println("visualizar");

                    System.out.println("email:");
                    String emailV = stdIn.readLine();
                    VisEmpresa visE = new VisEmpresa("visualizarEmpresa", emailV, token);
                    final var objectMapperV = new ObjectMapper();
                    final String jsonV = objectMapperV.writeValueAsString(visE);
                    System.out.println(jsonV);
                    out.println(jsonV);
                    System.out.println("client: " + in.readLine());
                    break;
                case 2:
                    System.out.println("atualizar");

                    System.out.println("email:");
                    String emailA = stdIn.readLine();
                    System.out.println("razão Social:");
                    String razaoSocialA = stdIn.readLine();
                    System.out.println("cnpj:");
                    String cnpjA = stdIn.readLine();
                    System.out.println("senha");
                    String senhaA = stdIn.readLine();
                    System.out.println("descrição:");
                    String descA = stdIn.readLine();
                    System.out.println("ramo:");
                    String ramoA = stdIn.readLine();
                    AtlEmpresa atlE = new AtlEmpresa("atualizarEmpresa", emailA,razaoSocialA, cnpjA, senhaA, descA, ramoA, token);
                    final var objectMapperA = new ObjectMapper();
                    final String jsonA = objectMapperA.writeValueAsString(atlE);
                    System.out.println(jsonA);
                    out.println(jsonA);
                    System.out.println("client: " + in.readLine());
                    break;
                case 3:
                    System.out.println("apagar");

                    System.out.println("email:");
                    String emailAp = stdIn.readLine();
                    VisEmpresa apgE = new VisEmpresa("apagarEmpresa", emailAp, token);
                    final var objectMapperAp = new ObjectMapper();
                    final String jsonAp = objectMapperAp.writeValueAsString(apgE);
                    System.out.println(jsonAp);
                    out.println(jsonAp);
                    System.out.println("client: " + in.readLine());
                    break;
                case 4:
                    System.out.println("menu inicial");
                    MenuGeral.menuGeral(stdIn,out, in, token, echoSocket);
                    break;
                case 5:
                    flag = Logout.logout(token,out,in,echoSocket);
                    break;
                default:
                    System.out.println("error");
                    break;
            }
        }

    }
}
