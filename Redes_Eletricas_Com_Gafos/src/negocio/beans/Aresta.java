package negocio.beans;

class Aresta {
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
    
    
}
