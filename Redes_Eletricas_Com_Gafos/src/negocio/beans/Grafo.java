package negocio.beans;

import java.util.ArrayList;
import java.util.List;

import exception.OperacaoInvalidaException;

class Grafo {
	
    List<Departamento> departamentos;
    List<Aresta> arestas;
    int controleIndice = 0;
    private static Grafo instance;
   
    private Grafo() {
        departamentos = new ArrayList<>();
        arestas = new ArrayList<>();
    }
    
    /*
     * singleton (instancia unica)
     * */ 
    
    public static Grafo getInstance() {
    if (instance == null) {
    	instance = new Grafo();
    }
    return instance;
    }
    
    /* metodo que verifica se o departamento existe 
     * e adiciona departamento ao grafo, 
     * caso ja exista levanta ema exception*/
    
    public void adicionarDepartamento(String nome, int numPessoas) {
    	Departamento novo = new Departamento (nome, numPessoas);
    	if (!existeDepartamento(novo)) {
    	departamentos.add(new Departamento(nome, numPessoas));
        controleIndice++;
    	}
    	else{
    		throw new OperacaoInvalidaException("ERRO: O nome do departamento ja existe!");
    	}        
    }

    /*metodo que associa arestas a departamentos
     *  e adiciona na list de arestas*/
    
    public void adicionarAresta(Departamento origem, Departamento destino, double distancia) {
        arestas.add(new Aresta(origem, destino, distancia));
    }

    /* método verifica se existe uma aresta
     *  entre dois departamentos com base nos indices*/
    
    public boolean existeAresta(int origem, int destino) {
        for (Aresta aresta : arestas) {
            if ((aresta.origem.getIndice() == origem) && (aresta.destino.getIndice() == destino) ||
                (aresta.origem.getIndice() == destino) && (aresta.destino.getIndice() == origem)) {
                return true;
            }
        }
        return false;
    }

    /* remove uma aresta entre 
     * dois departamentos específicos */
    
    public void removerAresta(Departamento origem, Departamento destino) {
        for (int i = 0; i < arestas.size(); i++) {
            Aresta aresta = arestas.get(i);
            if ((aresta.origem.equals(origem) && aresta.destino.equals(destino)) ||
                (aresta.origem.equals(destino) && aresta.destino.equals(origem))) {
                arestas.remove(i);
                break;
            }
        }
    }

    /* remove uma aresta entre 
     * dois departamentos específicos */
    
    public boolean existeDepartamento(Departamento dep) {
    	if (dep == null || dep.getNome().isBlank() || dep.getNome().isEmpty()) {
    		return false;
    	}
    	
    	for (Departamento aux: departamentos) {
    		if (aux.getNome().equals(dep.getNome()) && aux.getIndice() == dep.getIndice()) {
    		return true;
    		}
    	}
    		return false;
    }

    /* busca a aresta com base
     * na origemeno destino */
    
    public Aresta buscarAresta(Departamento origem, Departamento destino) {
        for (Aresta aresta : arestas) {
            
            if ((aresta.origem.equals(origem) && aresta.destino.equals(destino)) ||
                (aresta.origem.equals(destino) && aresta.destino.equals(origem))) {
                return aresta; // encontrada
            }
        }
        return null; // não encontrada
    }

    /* remove departamentos
     * com base nos indices, e dps as arestas */
    
    public void removerDepartamento(int indice) {
    	Departamento aux = departamentos.get(indice);
        if (aux != null) {
            departamentos.remove(indice);

            // Remover arestas associadas
            List<Aresta> arestasRemover = new ArrayList<>();
            for (Aresta aresta : arestas) {
                if (aresta.origem.getIndice() == indice || aresta.destino.getIndice() == indice) {
                    arestasRemover.add(aresta);
                }
            }
            arestas.removeAll(arestasRemover);
        }
        else 
        	throw new OperacaoInvalidaException ("O departamento nao existe!");
    }

    /*  calcula a (AGM) a partir de um departamento de início .
     *  Cria uma nova instância de Grafo chamada agm para armazenar a AGM.
     *  Começa a construir a AGM adicionando departamentos e arestas
     *  Retorna a AGM como um novo objeto Grafo.*/ 
    
    public Grafo calcularAGM(int inicio) {
        Grafo agm = new Grafo();

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
                Departamento novoDepartamento = agm.existeDepartamento(menorAresta.origem)
                        ? menorAresta.destino
                        : menorAresta.origem;

                agm.adicionarDepartamento(novoDepartamento.getNome(), novoDepartamento.getNumPessoas());
                agm.adicionarAresta(menorAresta.origem, menorAresta.destino, menorAresta.distancia);
            }
        }

        return agm;
    }
    
    /*Itera sobre todas as arestas e 
     * acumula as distâncias para obter 
     * o custo total*/

    public double calcularCustoTotal() {
        double custoTotal = 0;
        for (Aresta aresta : arestas) {
            custoTotal += aresta.distancia;
        }
        return custoTotal;
    }
    
    /*Itera sobre todos os departamentos 
     * e acumula o número de pessoas 
     * de cada departamento.*/

    public int calcularPessoasAtendidas() {
        int pessoasAtendidas = 0;
        for (Departamento dept : departamentos) {
            pessoasAtendidas += dept.numPessoas;
        }
        return pessoasAtendidas;
    }
    
    /*imprime informações sobre 
     * todos os departamentos no grafo*/
    
    public void imprimirDepartamentos() {
        System.out.println("Departamentos:");
        for (int i = 0; i < departamentos.size(); i++) {
            System.out.println(i + ": " + departamentos.get(i).nome + " (Pessoas: " + departamentos.get(i).numPessoas + ")");
        }
    }
    
    /*imprime informações sobre a
     *  Árvore Geradora Mínima (AGM)*/
    
    public void imprimirAGM() {
        System.out.println("arvore Geradora Mininima:");
        for (Aresta aresta : arestas) {
            System.out.println(aresta.origem.getNome() + " - " +
                               aresta.destino.getNome() + " : " +
                               aresta.distancia + " km");
        }
    }
}
