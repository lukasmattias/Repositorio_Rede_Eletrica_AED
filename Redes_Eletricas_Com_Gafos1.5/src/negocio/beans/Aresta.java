package negocio.beans;

public class Aresta implements Comparable<Aresta> {
	Departamento origem, destino;
    double distancia;

    public Aresta(Departamento origem, Departamento destino, double distancia) {
        this.origem = origem;
        this.destino = destino;
        this.distancia = distancia;
    }

	public Departamento getOrigem() {
		return origem;
	}

	public void setOrigem(Departamento origem) {
		this.origem = origem;
	}

	public Departamento getDestino() {
		return destino;
	}

	public void setDestino(Departamento destino) {
		this.destino = destino;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}
    
    @Override
    public int compareTo(Aresta outraAresta) {
        // Implemente a lógica de comparação aqui.
        // Você deve comparar as distâncias das arestas.
        // Por exemplo, se desejar uma ordem crescente com base na distância:
        if (this.distancia < outraAresta.distancia) {
            return -1;
        } else if (this.distancia > outraAresta.distancia) {
            return 1;
        } else {
            return 0; // As distâncias são iguais
        }
    }
}
