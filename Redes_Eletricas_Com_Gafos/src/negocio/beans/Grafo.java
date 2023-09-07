package negocio.beans;

import java.util.ArrayList;
import java.util.List;

import exception.OperacaoInvalidaException;

public class Grafo {
    List<Departamento> departamentos;
    List<Aresta> arestas;
    int controleIndice = 0;
    private static Grafo instance;
   
    private Grafo() {
        departamentos = new ArrayList<>();
        arestas = new ArrayList<>();
    }
    
    public static Grafo getInstance() {
    if (instance == null) {
    	instance = new Grafo();
    }
    return instance;
    }

    public void adicionarDepartamento(String nome, int numPessoas, double x, double y) throws OperacaoInvalidaException {
    	Departamento novo = new Departamento (nome, numPessoas, x, y, controleIndice);
    	if (!existeDepartamento(novo)) {
    	departamentos.add(novo);
        controleIndice++;
    	}
    	else{
    		throw new OperacaoInvalidaException("ERRO: O nome do departamento já existe.");
    	}        
    }


    public void adicionarAresta(int indiceOrigem, int indiceDestino, double distancia) throws OperacaoInvalidaException {
       if(indiceOrigem <= (this.departamentos.size()-1) && indiceOrigem >= 0 
    		   && indiceDestino <= (this.departamentos.size()-1) && indiceDestino >=0) {
    	 arestas.add(new Aresta(this.departamentos.get(indiceOrigem), this.departamentos.get(indiceDestino), distancia));  
       }
       else {
    	   throw new OperacaoInvalidaException("Os índices informados não correspondem a departamentos válidos.");
       }
    }

    // Implemente as opera��es de busca e remo��o de n�s e arestas
    
    public boolean existeAresta(int origem, int destino) {
        for (Aresta aresta : arestas) {
            if ((aresta.origem.getId() == origem) && (aresta.destino.getId() == destino) ||
                (aresta.origem.getId() == destino) && (aresta.destino.getId() == origem)) {
                return true;
            }
        }
        return false;
    }

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

    public boolean existeDepartamento(Departamento dep) {
    	if (dep == null || dep.getNome().isBlank() || dep.getNome().isEmpty()) {
    		return false;
    	}
    	
    	for (Departamento aux: departamentos) {
    		if (aux.getNome().equals(dep.getNome()) || aux.getId() == dep.getId()) {
    		return true;
    		}
    	}
    		return false;
    }

    public void removerDepartamento(int indice) {
    	Departamento aux = departamentos.get(indice);
        if (aux != null) {
            departamentos.remove(indice);

            // Remover arestas associadas ao departamento removido
            List<Aresta> arestasRemover = new ArrayList<>();
            for (Aresta aresta : arestas) {
                if (aresta.origem.getId() == indice || aresta.destino.getId() == indice) {
                    arestasRemover.add(aresta);
                }
            }
            arestas.removeAll(arestasRemover);
        }
        else 
        	throw new OperacaoInvalidaException ("O departamento não existe");
    }

    // algoritmo para encontrar a �rvore Geradora M�nima (AGM)
    
    public Grafo calcularAGM(int inicio) {
        Grafo agm = new Grafo();

        // Inicializa��o: Adicionar n� inicial � AGM
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
        System.out.println("�rvore Geradora M�nima:");
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
    
}
