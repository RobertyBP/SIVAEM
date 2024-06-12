package org.example.Menus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MenuGeral {
    public static void menuGeral(BufferedReader stdIn,PrintWriter out, BufferedReader in, String token, Socket echoSocket) throws IOException {
        System.out.println("1-empresa, 2-vagas");
        int op = Integer.parseInt(stdIn.readLine());

        switch (op) {
            case 1:
                MenuEmpresa.menuEmpresa(stdIn,out,in, token,echoSocket);
                break;
            case 2:
                MenuVaga.menuVaga(stdIn,out, in, token, echoSocket);
                break;
        }
    }
}
