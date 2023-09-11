package negocio.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

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
       if((indiceOrigem <= (this.departamentos.size()-1) && indiceOrigem >= 0) 
    		   && (indiceDestino <= (this.departamentos.size()-1) && indiceDestino >=0)) {
    	 arestas.add(new Aresta(this.departamentos.get(indiceOrigem), this.departamentos.get(indiceDestino), distancia));  
       }
       else {
    	   throw new OperacaoInvalidaException("Os indices informados nao correspondem a departamentos validos!");
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
    

    public Aresta buscarAresta(int indiceOrigem, int indiceDestino) {
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


    /*  calcula a (AGM) a partir de um departamento de in�cio .
     *  Cria uma nova inst�ncia de Grafo chamada agm para armazenar a AGM.
     *  Come�a a construir a AGM adicionando departamentos e arestas
     *  Retorna a AGM como um novo objeto Grafo.*/
    
    public Grafo calcularAGM() {
        Grafo agm = new Grafo();

        // Verifica se há departamentos no grafo original
        if (departamentos.isEmpty()) {
            return agm;
        }

        // Adiciona o primeiro departamento do grafo original à AGM
        Departamento primeiroDepartamento = departamentos.get(0);
        agm.departamentos.add(primeiroDepartamento);

        while (agm.getDepartamentos().size() < departamentos.size()) {
            double menorDistancia = Double.MAX_VALUE;
            Departamento departamentoNaAGM = null;
            Departamento novoDepartamento = null;

            // Encontra a aresta de menor peso que conecta um vértice na AGM a um vértice fora da AGM
            for (Departamento depNaAGM : agm.getDepartamentos()) {
                for (Departamento depForaAGM : departamentos) {
                    if (!agm.existeDepartamento(depForaAGM)) {
                        Aresta aresta = buscarAresta(depNaAGM, depForaAGM);
                        if (aresta != null && aresta.getDistancia() < menorDistancia) {
                            menorDistancia = aresta.getDistancia();
                            departamentoNaAGM = depNaAGM;
                            novoDepartamento = depForaAGM;
                        }
                    }
                }
            }

            // Adiciona o novo departamento à AGM
            agm.departamentos.add(novoDepartamento);
            agm.adicionarAresta(departamentoNaAGM.getId(), novoDepartamento.getId(), menorDistancia);
        }

        return agm;
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
    
    /*imprime informa��es sobre a
     *  �rvore Geradora M�nima (AGM)*/
    
    public void imprimirAGM() {
    	Grafo agm = calcularAGM();
        System.out.println("arvore Geradora Minima:");
        for (Aresta aresta : agm.getArestas()) {
            System.out.println(aresta.origem.getNome() + " - " +
                               aresta.destino.getNome() + " : " +
                               aresta.distancia + " km");
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
