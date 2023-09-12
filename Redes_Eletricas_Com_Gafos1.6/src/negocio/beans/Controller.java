package negocio.beans;

import java.util.Scanner;

import exception.OperacaoInvalidaException;

public class Controller {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grafo grafo = Grafo.getInstance();

        System.out.println("\n    <-- Bem-vindo ao Sistema de\n   Rede Eletrica da UFRPE! -->");

        while (true) {
            System.out.println("================================");
            System.out.println("|  1. Adicionar Departamento   |");
            System.out.println("|  2. Adicionar Aresta         |");
            System.out.println("|  3. Remover Aresta           |");
            System.out.println("|  4. Remover Departamento     |");
            System.out.println("|  5. Buscar Departamento      |"); 
            System.out.println("|  6. Limpar Tudo              |"); 
            System.out.println("|  7. Calcular e Imprimir AGM  |");
            System.out.println("================================");
            System.out.println("|  8. Sair                     |");
            System.out.println("================================");
            System.out.print("|  Escolha uma opcao: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcao) {
                case 1:
                    System.out.print("Digite o nome do departamento: ");
                    String nomeDepartamento = scanner.nextLine();
                    System.out.print("Digite o numero de pessoas no departamento: ");
                    int numPessoas = scanner.nextInt();
                    scanner.nextLine(); 
                    try {
                        grafo.adicionarDepartamento(nomeDepartamento, numPessoas, 2, 5);
                        System.out.println("Departamento adicionado com sucesso!");
                    }
                    catch (OperacaoInvalidaException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Digite o indice do departamento de origem: ");
                    int origem = scanner.nextInt();
                    System.out.print("Digite o indice do departamento de destino: ");
                    int destino = scanner.nextInt();
                    System.out.print("Digite a distancia entre os departamentos: ");
                    double distancia = scanner.nextDouble();
                    scanner.nextLine();
                    try {
                        grafo.adicionarAresta(origem, destino, distancia);
                        System.out.println("Aresta adicionada com sucesso!");
                    }
                    catch (OperacaoInvalidaException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Digite o indice do departamento de origem: ");
                    origem = scanner.nextInt();
                    System.out.print("Digite o indice do departamento de destino: ");
                    destino = scanner.nextInt();
                    scanner.nextLine(); 
                    grafo.removerAresta(origem, destino);
                    System.out.println("Aresta removida com sucesso!");
                    break;
                case 4:
                    System.out.print("Digite o indice do departamento a ser removido: ");
                    int indiceDepartamento = scanner.nextInt();
                    scanner.nextLine(); 
                    try {
                        grafo.removerDepartamento(indiceDepartamento);
                        System.out.println("Departamento removido com sucesso!");
                    }
                    catch (OperacaoInvalidaException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.print("Digite o nome do departamento a ser buscado: ");
                    String nomeBusca = scanner.nextLine();
                    Departamento departamentoEncontrado = grafo.buscarDepartamentoPorNome(nomeBusca);
                    if (departamentoEncontrado != null) {
                        System.out.println("Departamento encontrado:\n" + departamentoEncontrado);
                    } else {
                        System.out.println("Departamento não encontrado.");
                    }
                    break;
                case 6:
                    grafo.limparDepartamentos();
                    grafo.limparArestas();
                    System.out.println("Dados apagados com sucesso!");
                    break;
                case 7:
                    System.out.println("\nDepartamentos e indices:");
                    grafo.imprimirDepartamentos();
                    System.out.print("\nDigite o indice para calcular a AGM: ");
                    int departamentoInicial = scanner.nextInt();
                    scanner.nextLine(); 
                    Grafo agm = grafo.calcularAGM(departamentoInicial);
                    System.out.println("\nArvore Geradora Minima:");
                    System.out.println(agm);
                    System.out.println("Custo Total da AGM: " + agm.calcularCustoTotal());
                    System.out.println("Pessoas Atendidas pela AGM: " + agm.calcularPessoasAtendidas());
                    break;
                case 8:
                    System.out.println("Saindo do Sistema de Rede Eletrica. Ate logo!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Opcao invalida. Por favor, escolha uma opcao valida.");
            }
        }
    }
}
