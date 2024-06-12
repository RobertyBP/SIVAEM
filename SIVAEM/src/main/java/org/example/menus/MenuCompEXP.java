package org.example.menus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.candidatos.Logout;
import org.example.candidatos.VisCandidato;
import org.example.competencias.CadCompExp;
import org.example.competencias.VisCompExp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MenuCompEXP {
    public static void menuComp_Exp(BufferedReader stdIn, PrintWriter out, BufferedReader in, String token, Socket echoSocket)throws IOException {
        boolean flag = true;

        while (flag) {
            System.out.println("1-cadastrar 2-visualizar 3-editar 4-apagar 5-menuGeral 7-logout");
            int op = Integer.parseInt(stdIn.readLine());
            switch (op) {
                case 1:
                    System.out.println("Cadastrar");

                    System.out.println("email:");
                    String emailC = stdIn.readLine();
                     List<CadCompExp.CompEXP> compExp = List.of();

                    compExp = competencias(stdIn);

                    CadCompExp cadComp = new CadCompExp("cadastrarCompetenciaExperiencia", emailC,compExp, token);
                    final var objectMapperC = new ObjectMapper();
                    final String jsonC = objectMapperC.writeValueAsString(cadComp);
                    System.out.println(jsonC);
                    out.println(jsonC);

                    System.out.println("client: " + in.readLine());
                    break;
                case 2:
                    System.out.println("visualizar CompEXP");
                    System.out.println("email:");
                    String emailV = stdIn.readLine();

                    VisCompExp visCE = new VisCompExp("visualizarCompetenciaExperiencia", emailV, token);
                    final var objectMapperV = new ObjectMapper();
                    final String jsonV = objectMapperV.writeValueAsString(visCE);
                    System.out.println(jsonV);
                    out.println(jsonV);

                    System.out.println("client: " + in.readLine());
                    break;
                case 3:
                    System.out.println("Atualizar");

                    System.out.println("email:");
                    String emailA = stdIn.readLine();

                    List<CadCompExp.CompEXP> compExpA = List.of();
                    compExpA = competencias(stdIn);

                    CadCompExp cadCompA = new CadCompExp("atualizarCompetenciaExperiencia", emailA,compExpA, token);
                    final var objectMapperA = new ObjectMapper();
                    final String jsonA = objectMapperA.writeValueAsString(cadCompA);
                    System.out.println(jsonA);
                    out.println(jsonA);

                    System.out.println("client: " + in.readLine());

                    break;
                case 4:
                    System.out.println("Apagar");

                    System.out.println("email:");
                    String emailAp = stdIn.readLine();

                    List<CadCompExp.CompEXP> compExpAp = List.of();
                    compExpAp = competencias(stdIn);

                    CadCompExp cadCompAp = new CadCompExp("apagarCompetenciaExperiencia", emailAp,compExpAp, token);
                    final var objectMapperAp = new ObjectMapper();
                    final String jsonAp = objectMapperAp.writeValueAsString(cadCompAp);
                    System.out.println(jsonAp);
                    out.println(jsonAp);

                    System.out.println("client: " + in.readLine());
                    break;
                case 5:
                    System.out.println("menu geral");
                    MenuGeral.menuGeral(stdIn,out, in, token, echoSocket);
                    break;
                case 7:
                    flag = Logout.logout(token,out,in,echoSocket);
                    break;
                default:
                    System.out.println("error");
                    break;
            }
        }
    }
    public static List<CadCompExp.CompEXP> competencias(BufferedReader stdIn) throws IOException {
        boolean moreCompetencias = true;
        List<CadCompExp.CompEXP> compExp = new ArrayList<>();
        while (moreCompetencias) {
            System.out.println("Competência:");
            String competencia = stdIn.readLine();
            System.out.println("Experiência (número):");
            int experiencia = Integer.parseInt(stdIn.readLine());
            compExp.add(new CadCompExp.CompEXP(competencia, experiencia));

            System.out.println("Adicionar mais? (s/n):");
            moreCompetencias = stdIn.readLine().equalsIgnoreCase("s");
        }
        return compExp;
    }
}
