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
    	Departamento novo = new Departamento (nome, numPessoas, x, y, getControleIndice());
    	if (!existeDepartamento(novo)) {
    	departamentos.add(novo);
        setControleIndice(getControleIndice() + 1);
    	}
    	else{
    		throw new OperacaoInvalidaException("ERRO: O nome do departamento ja existe.");
    	}        
    }

    /*metodo que associa arestas a departamentos
     *  e adiciona na list de arestas*/
    
    public void adicionarAresta(int indiceOrigem, int indiceDestino, double distancia) throws OperacaoInvalidaException {
       if(indiceOrigem <= (this.departamentos.size()-1) && indiceOrigem >= 0 
    		   && indiceDestino <= (this.departamentos.size()-1) && indiceDestino >=0) {
    	 arestas.add(new Aresta(this.departamentos.get(indiceOrigem), this.departamentos.get(indiceDestino), distancia));  
       }
       else {
    	   throw new OperacaoInvalidaException("Os indices informados nao correspondem a departamentos validos!");
       }
    }

    /* método verifica se existe uma aresta
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
     * dois departamentos específicos */

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


    /*  calcula a (AGM) a partir de um departamento de início .
     *  Cria uma nova instância de Grafo chamada agm para armazenar a AGM.
     *  Começa a construir a AGM adicionando departamentos e arestas
     *  Retorna a AGM como um novo objeto Grafo.*/
    
    public Grafo calcularAGM(int inicio) {
        Grafo agm = new Grafo();

        agm.adicionarDepartamento(departamentos.get(inicio).nome, departamentos.get(inicio).numPessoas, departamentos.get(inicio).x,
        		departamentos.get(inicio).y);

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

                agm.adicionarDepartamento(novoDepartamento.getNome(), novoDepartamento.getNumPessoas(), novoDepartamento.x, novoDepartamento.y);
                agm.adicionarAresta(menorAresta.origem.getId(), menorAresta.destino.getId(), menorAresta.distancia);
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
            boolean estaConectado = false;
            
            // Verifica se o departamento está conectado por alguma aresta
            for (Aresta aresta : arestas) {
                if (aresta.origem.equals(dept) || aresta.destino.equals(dept)) {
                    estaConectado = true;
                    break; // Se estiver conectado, não é necessário verificar mais arestas
                }
            }

            // Se o departamento estiver conectado, conta as pessoas atendidas
            if (estaConectado) {
                pessoasAtendidas += dept.numPessoas;
            }
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
        System.out.println("arvore Geradora Minima:");
        for (Aresta aresta : arestas) {
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

	public int getControleIndice() {
		return controleIndice;
	}

	public void setControleIndice(int controleIndice) {
		this.controleIndice = controleIndice;
	}

    
}
