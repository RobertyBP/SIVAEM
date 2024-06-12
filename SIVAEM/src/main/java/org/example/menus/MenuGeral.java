package org.example.menus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MenuGeral {
    public static void menuGeral(BufferedReader stdIn, PrintWriter out, BufferedReader in, String token, Socket echoSocket) throws IOException {
        System.out.println("1-candidato 2-competencia/experiencia 3-vagas");
        int op = Integer.parseInt(stdIn.readLine());

        switch (op) {
            case 1:
                MenuCandidato.menu_candidato(stdIn,out,in, token,echoSocket);
                break;
            case 2:
                MenuCompEXP.menuComp_Exp(stdIn,out,in, token,echoSocket);
                break;
            case 3:
                MenuVaga.menuVaga(stdIn,out, in, token, echoSocket);
                break;
            default:
                System.out.println("error");
                break;
        }
    }
}
