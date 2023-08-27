package negocio.beans;

import java.util.Scanner;

public class SistemaRedeEletrica {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Grafo grafo = new Grafo();

        System.out.println("\n    <-- Bem-vindo ao Sistema de\n   Rede Elétrica da UFRPE! -->");

        while (true) {
            System.out.println("================================");
            System.out.println("|  1. Adicionar Departamento   |");
            System.out.println("|  2. Adicionar Aresta         |");
            System.out.println("|  3. Remover Aresta           |");
            System.out.println("|  4. Calcular e Imprimir AGM  |");
            System.out.println("================================");
            System.out.println("|  5. Sair                     |");
            System.out.println("================================");
            System.out.print("|  Escolha uma opção: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha após o número

            switch (opcao) {
                case 1:
                    System.out.print("Digite o nome do departamento: ");
                    String nomeDepartamento = scanner.nextLine();
                    System.out.print("Digite o número de pessoas no departamento: ");
                    int numPessoas = scanner.nextInt();
                    scanner.nextLine(); // Consumir a quebra de linha após o número
                    grafo.adicionarDepartamento(nomeDepartamento, numPessoas);
                    System.out.println("Departamento adicionado com sucesso!");
                    break;
                case 2:
                    System.out.print("Digite o índice do departamento de origem: ");
                    int origem = scanner.nextInt();
                    System.out.print("Digite o índice do departamento de destino: ");
                    int destino = scanner.nextInt();
                    System.out.print("Digite a distância entre os departamentos: ");
                    double distancia = scanner.nextDouble();
                    scanner.nextLine(); // Consumir a quebra de linha após o número
                    grafo.adicionarAresta(origem, destino, distancia);
                    System.out.println("Aresta adicionada com sucesso!");
                    break;
                case 3:
                    System.out.print("Digite o índice do departamento de origem: ");
                    origem = scanner.nextInt();
                    System.out.print("Digite o índice do departamento de destino: ");
                    destino = scanner.nextInt();
                    scanner.nextLine(); // Consumir a quebra de linha após o número
                    grafo.removerAresta(origem, destino);
                    System.out.println("Aresta removida com sucesso!");
                    break;
                case 4:
                    System.out.println("\nDepartamentos e Índices:");
                    grafo.imprimirDepartamentos();
                    System.out.print("\nDigite o índice do departamento inicial para calcular a AGM: ");
                    int departamentoInicial = scanner.nextInt();
                    scanner.nextLine(); // Consumir a quebra de linha após o número
                    Grafo agm = grafo.calcularAGM(departamentoInicial);
                    System.out.println("\nÁrvore Geradora Mínima:");
                    agm.imprimirAGM();
                    System.out.println("Custo Total da AGM: " + agm.calcularCustoTotal());
                    System.out.println("Pessoas Atendidas pela AGM: " + agm.calcularPessoasAtendidas());
                    break;
                case 5:
                    System.out.println("Saindo do Sistema de Rede Elétrica. Até logo!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Opção inválida. Por favor, escolha uma opção válida.");
            }
        }
    }
}
