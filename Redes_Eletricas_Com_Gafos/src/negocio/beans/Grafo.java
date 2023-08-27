package negocio.beans;

import java.util.ArrayList;
import java.util.List;

class Grafo {
    List<Departamento> departamentos;
    List<Aresta> arestas;

    public Grafo() {
        departamentos = new ArrayList<>();
        arestas = new ArrayList<>();
    }

    public void adicionarDepartamento(String nome, int numPessoas) {
        departamentos.add(new Departamento(nome, numPessoas));
    }

    public void adicionarAresta(int origem, int destino, double distancia) {
        arestas.add(new Aresta(origem, destino, distancia));
    }

    // Implemente as operações de busca e remoção de nós e arestas
    
    public boolean existeAresta(int origem, int destino) {
        for (Aresta aresta : arestas) {
            if ((aresta.origem == origem && aresta.destino == destino) ||
                (aresta.origem == destino && aresta.destino == origem)) {
                return true;
            }
        }
        return false;
    }

    public void removerAresta(int origem, int destino) {
        for (int i = 0; i < arestas.size(); i++) {
            Aresta aresta = arestas.get(i);
            if ((aresta.origem == origem && aresta.destino == destino) ||
                (aresta.origem == destino && aresta.destino == origem)) {
                arestas.remove(i);
                break;
            }
        }
    }

    public boolean existeDepartamento(int indice) {
        return indice >= 0 && indice < departamentos.size();
    }

    public void removerDepartamento(int indice) {
        if (existeDepartamento(indice)) {
            departamentos.remove(indice);

            // Remover arestas associadas ao departamento removido
            List<Aresta> arestasRemover = new ArrayList<>();
            for (Aresta aresta : arestas) {
                if (aresta.origem == indice || aresta.destino == indice) {
                    arestasRemover.add(aresta);
                }
            }
            arestas.removeAll(arestasRemover);
        }
    }

    // algoritmo para encontrar a Árvore Geradora Mínima (AGM)
    
    public Grafo calcularAGM(int inicio) {
        Grafo agm = new Grafo();

        // Inicialização: Adicionar nó inicial à AGM
        agm.adicionarDepartamento(departamentos.get(inicio).nome, departamentos.get(inicio).numPessoas);

        while (agm.departamentos.size() < departamentos.size()) {
            Aresta menorAresta = null;

            for (Aresta aresta : arestas) {
                if (agm.existeDepartamento(aresta.origem) ^ agm.existeDepartamento(aresta.destino)) {
                    if (menorAresta == null || aresta.distancia < menorAresta.distancia) {
                        menorAresta = aresta;
                    }
                }
            }

            if (menorAresta != null) {
                int novoDepartamento = agm.existeDepartamento(menorAresta.origem)
                        ? menorAresta.destino
                        : menorAresta.origem;

                agm.adicionarDepartamento(departamentos.get(novoDepartamento).nome, departamentos.get(novoDepartamento).numPessoas);
                agm.adicionarAresta(menorAresta.origem, menorAresta.destino, menorAresta.distancia);
            }
        }

        return agm;
    }

    public double calcularCustoTotal() {
        double custoTotal = 0;
        for (Aresta aresta : arestas) {
            custoTotal += aresta.distancia;
        }
        return custoTotal;
    }

    public int calcularPessoasAtendidas() {
        int pessoasAtendidas = 0;
        for (Departamento dept : departamentos) {
            pessoasAtendidas += dept.numPessoas;
        }
        return pessoasAtendidas;
    }
    
    public void imprimirDepartamentos() {
        System.out.println("Departamentos:");
        for (int i = 0; i < departamentos.size(); i++) {
            System.out.println(i + ": " + departamentos.get(i).nome + " (Pessoas: " + departamentos.get(i).numPessoas + ")");
        }
    }
    
    public void imprimirAGM() {
        System.out.println("Árvore Geradora Mínima:");
        for (Aresta aresta : arestas) {
            System.out.println(departamentos.get(aresta.origem).nome + " - " +
                               departamentos.get(aresta.destino).nome + " : " +
                               aresta.distancia + " km");
        }
    }
}
