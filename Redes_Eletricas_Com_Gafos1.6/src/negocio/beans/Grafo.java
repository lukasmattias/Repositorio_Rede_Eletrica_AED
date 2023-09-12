package negocio.beans;

import java.util.ArrayList;
import java.util.List;

import exception.OperacaoInvalidaException;

public class Grafo {
    List<Departamento> departamentos;
    List<Aresta> arestas;
    private int controleIndice = 0;
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
    
    public void adicionarDepartamento(String nome, int numPessoas, double x, double y) throws OperacaoInvalidaException {
    	Departamento novo = new Departamento (nome, numPessoas, x, y, controleIndice);
    	if (!existeDepartamento(novo)) {
    	departamentos.add(novo);
        controleIndice++;
    	}
    	else{
    		throw new OperacaoInvalidaException("ERRO: O nome do departamento ja existe.");
    	}        
    }

    /*metodo que associa arestas a departamentos
     *  e adiciona na list de arestas*/
    
    public void adicionarAresta(int indiceOrigem, int indiceDestino, double distancia) throws OperacaoInvalidaException {
    	Grafo grafoOriginal = Grafo.getInstance();

    	if((indiceOrigem <= (grafoOriginal.departamentos.size()-1) && indiceOrigem >= 0) 
    		   && (indiceDestino <= (grafoOriginal.departamentos.size()-1) && indiceDestino >=0) && distancia > 0) {
    	   arestas.add(new Aresta(grafoOriginal.departamentos.get(indiceOrigem), grafoOriginal.departamentos.get(indiceDestino), distancia));        }
       else {
    	   throw new OperacaoInvalidaException("Indice ou distancia invalida.");
       }
    }

    /* m�todo verifica se existe uma aresta
     *  entre dois departamentos com base nos indices*/
    
    public boolean existeAresta(int origem, int destino) {
        for (Aresta aresta : arestas) {
            if ((aresta.origem.getId() == origem) && (aresta.destino.getId() == destino) ||
                (aresta.origem.getId() == destino) && (aresta.destino.getId() == origem)) {
                return true;
            }
        }
        return false;
    }
    
    /* remove uma aresta entre 
     * dois departamentos espec�ficos */

    public void removerAresta(int origem, int destino) {
        for (int i = 0; i < arestas.size(); i++) {
            Aresta aresta = arestas.get(i);
            if ((aresta.origem.equals(this.departamentos.get(origem))) && aresta.destino.equals(this.departamentos.get(destino)) ||
                (aresta.origem.equals(this.departamentos.get(destino)) && aresta.destino.equals(this.departamentos.get(origem)))) {
                arestas.remove(i);
                break;
            }
        }
    }

    /*  calcula a (AGM) a partir de um departamento de in�cio .
     *  Cria uma nova inst�ncia de Grafo chamada agm para armazenar a AGM.
     *  Come�a a construir a AGM adicionando departamentos e arestas
     *  Retorna a AGM como um novo objeto Grafo.*/
    
        
        public Grafo calcularAGM(int indiceInicial) {
            Grafo agm = new Grafo();

            // Verifica se há departamentos no grafo original
            if (departamentos.isEmpty()) {
                return agm;
            }

            // Adiciona o departamento inicial do grafo original à AGM
            Departamento primeiroDepartamento = departamentos.get(indiceInicial);
            agm.departamentos.add(primeiroDepartamento);

            while (agm.getDepartamentos().size() < departamentos.size()) {
                double menorDistancia = Double.MAX_VALUE;
                Departamento departamentoNaAGM = null;
                Departamento novoDepartamento = null;

                // Lista para armazenar as arestas com menor distância
                List<Aresta> arestasMenorDistancia = new ArrayList<>();

                // Encontra as arestas de menor peso que conectam um vértice na AGM a um vértice fora da AGM
                for (Departamento depNaAGM : agm.getDepartamentos()) {
                    for (Departamento depForaAGM : departamentos) {
                        if (!agm.existeDepartamento(depForaAGM)) {
                            Aresta aresta = buscarAresta(depNaAGM, depForaAGM);
                            if (aresta != null) {
                                if (aresta.getDistancia() < menorDistancia) {
                                    menorDistancia = aresta.getDistancia();
                                    departamentoNaAGM = depNaAGM;
                                    novoDepartamento = depForaAGM;
                                    arestasMenorDistancia.clear();
                                    arestasMenorDistancia.add(aresta);
                                } else if (aresta.getDistancia() == menorDistancia) {
                                    arestasMenorDistancia.add(aresta);
                                }
                            }
                        }
                    }
                }

                // Adiciona o novo departamento à AGM
                agm.departamentos.add(novoDepartamento);

                // Se houver várias arestas com a mesma distância mínima, adicionar apenas uma à AGM
                if (!arestasMenorDistancia.isEmpty()) {
                    agm.adicionarAresta(departamentoNaAGM.getId(), novoDepartamento.getId(), menorDistancia);
                }
            }

            return agm;
        }
    
        public Grafo buscaDijkstra(Departamento origem, Departamento destino) {
            // Inicialização de listas para acompanhar o menor caminho, distâncias e antecessores.
            List<Departamento> menorCaminho = new ArrayList<>();
            List<Departamento> naoVisitados = new ArrayList<>(departamentos);
            double[] distancias = new double[departamentos.size()];
            int[] antecessores = new int[departamentos.size()];

            // Inicialização das distâncias e antecessores com valores padrão.
            for (int i = 0; i < departamentos.size(); i++) {
                distancias[i] = Double.MAX_VALUE;
                antecessores[i] = -1;
            }

            // Índices dos departamentos de origem e destino no grafo.
            int indiceOrigem = departamentos.indexOf(origem);
            int indiceDestino = departamentos.indexOf(destino);

            // Distância da origem para ela mesma é definida como 0.
            distancias[indiceOrigem] = 0.0;

            // Loop principal para encontrar o menor caminho.
            while (!naoVisitados.isEmpty()) {
                // Encontra o vértice não visitado com a menor distância.
                int u = obterVerticeComMenorDistancia(distancias, naoVisitados);

                // Verifica se não há caminho para o destino ou se todos os vértices já foram visitados.
                if (u == -1 || distancias[u] == Double.MAX_VALUE) {
                    break;
                }

                // Remove o vértice atual da lista de não visitados.
                naoVisitados.remove(departamentos.get(u));

                // Explora vértices adjacentes não visitados.
                for (int v = 0; v < departamentos.size(); v++) {
                    // Verifica se o vértice adjacente não foi visitado e se há uma aresta entre u e v.
                    if (!menorCaminho.contains(departamentos.get(v)) && existeAresta(u, v)) {
                        // Busca a aresta correspondente entre u e v.
                        Aresta aresta = buscarArestaPorIndices(u, v);
                        // Calcula a nova distância da origem até v passando por u.
                        double novaDistancia = distancias[u] + aresta.getDistancia();

                        // Atualiza a distância e o antecessor de v, se a nova distância for menor.
                        if (novaDistancia < distancias[v]) {
                            distancias[v] = novaDistancia;
                            antecessores[v] = u;
                        }
                    }
                }
            }

            // Criação do subgrafo para representar o menor caminho encontrado.
            Grafo subgrafo = new Grafo();

            // Reconstrução do menor caminho a partir dos antecessores.
            for (int v = indiceDestino; v != -1; v = antecessores[v]) {
                // Verifica se não é o último vértice, pois o último não tem antecessor.
                if (antecessores[v] != -1) {
                    int u = antecessores[v];
                    // Adiciona o departamento e a aresta correspondente ao subgrafo.
                    subgrafo.departamentos.add(departamentos.get(v));
                    subgrafo.adicionarAresta(u, v, buscarArestaPorIndices(u, v).getDistancia());
                }
            }

            return subgrafo;
        }

        private int obterVerticeComMenorDistancia(double[] distancias, List<Departamento> naoVisitados) {
            double menorDistancia = Double.MAX_VALUE;
            int verticeComMenorDistancia = -1;

            for (int v = 0; v < distancias.length; v++) {
                // Verifica se o vértice não foi visitado e tem uma distância menor.
                if (naoVisitados.contains(departamentos.get(v)) && distancias[v] < menorDistancia) {
                    menorDistancia = distancias[v];
                    verticeComMenorDistancia = v;
                }
            }

            return verticeComMenorDistancia;
        }

    
    /* verifica se um departamento
     *  especifico existe */
    
    public boolean existeDepartamento(Departamento dep) {

    	for (Departamento aux: departamentos) {
    		if (aux.getNome().equals(dep.getNome()) || aux.getId() == dep.getId()) {
    		return true;
    		}
    	}
    		return false;
    }

    /* remove departamentos
     * com base nos indices, e dps as arestas */
    
    public void removerDepartamento(int indice) {
    	Departamento aux = departamentos.get(indice);
        if (aux != null) {
            departamentos.remove(indice);
            controleIndice--;
            
            List<Aresta> arestasRemover = new ArrayList<>();
            for (Aresta aresta : arestas) {
                if (aresta.origem.getId() == indice || aresta.destino.getId() == indice) {
                    arestasRemover.add(aresta);
                }
            }
            arestas.removeAll(arestasRemover);
        }
        else 
        	throw new OperacaoInvalidaException ("O departamento nao existe");
    }
    
    /* busca a aresta com base
     * na origemeno destino */
    

    public Aresta buscarArestaPorIndices(int indiceOrigem, int indiceDestino) {
        for (Aresta aresta : arestas) {
            if ((aresta.origem.getId() == indiceOrigem && aresta.destino.getId() == indiceDestino) ||
                (aresta.origem.getId() == indiceDestino && aresta.destino.getId() == indiceOrigem)) {
                return aresta;
            }
        }
        return null;
    }
    
    public Departamento buscarDepartamentoPorNome(String nome) {
        for (Departamento dep : departamentos) {
            if (nome.equals(dep.getNome())) {
                return dep;
            }
        }
        return null;
    }

    
    private Aresta buscarAresta(Departamento origem, Departamento destino) {
        for (Aresta aresta : arestas) {
            if ((aresta.getOrigem().equals(origem) && aresta.getDestino().equals(destino)) ||
                (aresta.getOrigem().equals(destino) && aresta.getDestino().equals(origem))) {
                return aresta;
            }
        }
        return null;
    }

    /*Itera sobre todas as arestas e 
     * acumula as dist�ncias para obter 
     * o custo total*/
    
    public double calcularCustoTotal() {
        double custoTotal = 0;
        for (Aresta aresta : arestas) {
            custoTotal += aresta.distancia;
        }
        return custoTotal;
    }
    
    
    /*Itera sobre todos os departamentos 
     * e acumula o n�mero de pessoas 
     * de cada departamento.*/

    public int calcularPessoasAtendidas() {
        int pessoasAtendidas = 0;

        for (Departamento dept : departamentos) {
            boolean estaConectado = false;
            
            // Verifica se o departamento est� conectado por alguma aresta
            for (Aresta aresta : arestas) {
                if (aresta.origem.equals(dept) || aresta.destino.equals(dept)) {
                    estaConectado = true;
                    break; // Se estiver conectado, n�o � necess�rio verificar mais arestas
                }
            }

            // Se o departamento estiver conectado, conta as pessoas atendidas
            if (estaConectado) {
                pessoasAtendidas += dept.numPessoas;
            }
        }

        return pessoasAtendidas;
    }
    
    /*imprime informa��es sobre 
     * todos os departamentos no grafo*/
    
    public void imprimirDepartamentos() {
        System.out.println("Departamentos:");
        for (int i = 0; i < departamentos.size(); i++) {
            System.out.println(i + ": " + departamentos.get(i).nome + " (Pessoas: " + departamentos.get(i).numPessoas + ")");
        }
    }
    
    public List<Departamento> getDepartamentos(){
		return departamentos;
    }
    
    public List<Aresta> getArestas(){
		return arestas;
    }
    
    public void limparDepartamentos() {
        departamentos.clear();
    }

    public void limparArestas() {
        arestas.clear();
    }

	public void setControleIndice(int controleIndice) {
		this.controleIndice = controleIndice;
	}
	
	public void atualizarIndices() {
		int aux = 0;
	
		while (aux < controleIndice) {
			for (Departamento dep : this.departamentos) {			
				dep.setId(aux);
				aux++;
			}	
		}
	}
	
	public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public void setArestas(List<Aresta> arestas) {
        this.arestas = arestas;
    }
    
}
