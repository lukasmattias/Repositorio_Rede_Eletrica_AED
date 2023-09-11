package negocio.beans;

import java.util.Scanner;

import exception.OperacaoInvalidaException;

public class Controller {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grafo grafo = Grafo.getInstance();

        System.out.println("\n    <-- Bem-vindo ao Sistema de\n   Rede El�trica da UFRPE! -->");

        while (true) {
            System.out.println("================================");
            System.out.println("|  1. Adicionar Departamento   |");
            System.out.println("|  2. Adicionar Aresta         |");
            System.out.println("|  3. Remover Aresta           |");
            System.out.println("|  4. Calcular e Imprimir AGM  |");
            System.out.println("================================");
            System.out.println("|  5. Sair                     |");
            System.out.println("================================");
            System.out.print("|  Escolha uma op��o: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha ap�s o n�mero

            switch (opcao) {
                case 1:
                    System.out.print("Digite o nome do departamento: ");
                    String nomeDepartamento = scanner.nextLine();
                    System.out.print("Digite o n�mero de pessoas no departamento: ");
                    int numPessoas = scanner.nextInt();
                    scanner.nextLine(); // Consumir a quebra de linha ap�s o n�mero
                    try {
                        grafo.adicionarDepartamento(nomeDepartamento, numPessoas, 2, 5);
                        System.out.println("Departamento adicionado com sucesso!");
                    }
                    catch (OperacaoInvalidaException e) {
                    	System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Digite o �ndice do departamento de origem: ");
                    int origem = scanner.nextInt();
                    System.out.print("Digite o �ndice do departamento de destino: ");
                    int destino = scanner.nextInt();
                    System.out.print("Digite a dist�ncia entre os departamentos: ");
                    double distancia = scanner.nextDouble();
                    scanner.nextLine(); // Consumir a quebra de linha ap�s o n�mero
                    try {
                    grafo.adicionarAresta(origem, destino, distancia);
                    System.out.println("Aresta adicionada com sucesso!");
                    }
                    catch (OperacaoInvalidaException e) {
                    	e.getMessage();
                    }
                    break;
                case 3:
                    System.out.print("Digite o �ndice do departamento de origem: ");
                    origem = scanner.nextInt();
                    System.out.print("Digite o �ndice do departamento de destino: ");
                    destino = scanner.nextInt();
                    scanner.nextLine(); // Consumir a quebra de linha ap�s o n�mero
                    
                    System.out.println("Aresta removida com sucesso!");
                    break;
                case 4:
                    System.out.println("\nDepartamentos e �ndices:");
                    grafo.imprimirDepartamentos();
                    System.out.print("\nDigite o  para calcular a AGM: ");
                    int departamentoInicial = scanner.nextInt();
                    scanner.nextLine(); // Consumir a quebra de linha ap�s o n�mero
                    Grafo agm = grafo.calcularAGM();
                    System.out.println("\n�rvore Geradora M�nima:");
                    agm.imprimirAGM();
                    System.out.println("Custo Total da AGM: " + agm.calcularCustoTotal());
                    System.out.println("Pessoas Atendidas pela AGM: " + agm.calcularPessoasAtendidas());
                    break;
                case 5:
                    System.out.println("Saindo do Sistema de Rede El�trica. At� logo!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Op��o inv�lida. Por favor, escolha uma op��o v�lida.");
            }
        }
    }
}
