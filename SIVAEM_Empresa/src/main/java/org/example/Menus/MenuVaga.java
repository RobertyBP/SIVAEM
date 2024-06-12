package org.example.Menus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.empresas.Logout;
import org.example.vagas.*;

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
            System.out.println("1-Cadastrar, 2-Visualizar, 3-Listar 4-Atualizar 5-Apagar 6-filtragem 8-menu Geral 9-logout");
            int op = Integer.parseInt(stdIn.readLine());

            switch (op) {
                case 1:
                    System.out.println("Cadastrar Vaga");

                    System.out.println("Email:");
                    String email = stdIn.readLine();
                    System.out.println("Nome:");
                    String nome = stdIn.readLine();
                    System.out.println("Faixa Salarial:");
                    int faixaSalarial = Integer.parseInt(stdIn.readLine());
                    System.out.println("Descrição:");
                    String descricao = stdIn.readLine();
                    System.out.println("Estado (true/false):");
                    boolean estado = Boolean.parseBoolean(stdIn.readLine());
                    System.out.println("Competências (separadas por vírgula):");
                    String[] competenciasArray = stdIn.readLine().split(",");
                    List<String> competencias = new ArrayList<>();
                    for (String competencia : competenciasArray) {
                        competencias.add(competencia.trim());
                    }

                    CadVaga cadVaga = new CadVaga("cadastrarVaga", nome, email, faixaSalarial, descricao, estado, competencias, token);
                    final var objectMapper = new ObjectMapper();
                    final String Json = objectMapper.writeValueAsString(cadVaga);
                    System.out.println(Json);
                    out.println(Json);

                    System.out.println("Client: " + in.readLine());

                    break ;
                case 2:
                    System.out.println("Visualizar Vaga");

                    System.out.println("Email:");
                    String emailV = stdIn.readLine();
                    System.out.println("ID da Vaga:");
                    int idVaga = Integer.parseInt(stdIn.readLine());

                    VisVaga visVaga = new VisVaga("visualizarVaga", idVaga, emailV, token);
                    final var objectMapperV = new ObjectMapper();
                    final String jsonV= objectMapperV.writeValueAsString(visVaga);
                    System.out.println(jsonV);
                    out.println(jsonV);

                    System.out.println("Client: " +in.readLine());

                    break;
                case 3:
                    System.out.println("Listar Vagas");

                    System.out.println("Email:");
                    String emailL = stdIn.readLine();

                    LisVaga listarVagas = new LisVaga("listarVagas", emailL, token);
                    final var objectMapperL = new ObjectMapper();
                    final String jsonL = objectMapperL.writeValueAsString(listarVagas);
                    System.out.println(jsonL);
                    out.println(jsonL);

                    System.out.println("Client: " +in.readLine());

                    break;
                case 4:
                    System.out.println("Atualizar Vaga");

                    System.out.println("Email:");
                    String emailA = stdIn.readLine();

                    System.out.println("ID da Vaga:");
                    int idVagaA = Integer.parseInt(stdIn.readLine());

                    System.out.println("Nome:");
                    String nomeA = stdIn.readLine();

                    System.out.println("Faixa Salarial:");
                    int faixaSalarialA = Integer.parseInt(stdIn.readLine());
                    System.out.println("Descrição:");
                    String descricaoA = stdIn.readLine();
                    System.out.println("Estado (true/false):");
                    boolean estadoA = Boolean.parseBoolean(stdIn.readLine());
                    System.out.println("Competências (separadas por vírgula):");
                    String[] competenciasArrayA = stdIn.readLine().split(",");
                    List<String> competenciasA = new ArrayList<>();
                    for (String competencia : competenciasArrayA) {
                        competenciasA.add(competencia.trim());
                    }

                    AtlVaga atlVaga = new AtlVaga("atualizarVaga", idVagaA, nomeA, emailA, faixaSalarialA, descricaoA, estadoA, competenciasA, token);
                    final var objectMapperA = new ObjectMapper();
                    final String JsonA = objectMapperA.writeValueAsString(atlVaga);
                    System.out.println(JsonA);
                    out.println(JsonA);

                    System.out.println("Client: " + in.readLine());

                    break ;
                case 5:
                    System.out.println("Apagar Vaga");

                    System.out.println("Email:");
                    String emailAp = stdIn.readLine();
                    System.out.println("ID da Vaga:");
                    int idVagaAp = Integer.parseInt(stdIn.readLine());

                    VisVaga apVaga = new VisVaga("apagarVaga", idVagaAp, emailAp, token);
                    final var objectMapperAp = new ObjectMapper();
                    final String jsonAp= objectMapperAp.writeValueAsString(apVaga);
                    System.out.println(jsonAp);
                    out.println(jsonAp);

                    System.out.println("Client: " +in.readLine());

                    break;
                case 6:
                    System.out.println("filtrar Vagas");

                    System.out.println("Competências (separadas por vírgula):");
                    String[] compArray = stdIn.readLine().split(",");
                    List<String> comp = new ArrayList<>();
                    for (String competencia : compArray) {
                        comp.add(competencia.trim());
                    }
                    System.out.println("tipo AND ou OR");
                    String tipo = stdIn.readLine();
                    FilVaga.Filtros pesq = new FilVaga.Filtros(comp,tipo);

                    FilVaga pesqVagas = new FilVaga("filtrarVagas",pesq,token);
                    final var objectMapperF = new ObjectMapper();
                    final String jsonF = objectMapperF.writeValueAsString(pesqVagas);
                    System.out.println(jsonF);
                    out.println(jsonF);

                    System.out.println("Client: " + in.readLine());

                    break;
                case 8:
                    System.out.println("menu geral");
                    MenuGeral.menuGeral(stdIn,out, in, token, echoSocket);
                    break;
                case 9:
                    flag = Logout.logout(token,out,in,echoSocket);
                    break;
                default:
                    System.out.println("error");
                    break;
            }
        }

    }
}
