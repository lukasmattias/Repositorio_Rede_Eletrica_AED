package negocio.beans;

public class Departamento extends No{
    String nome;
    int numPessoas;

    public Departamento(String nome, int numPessoas, double x, double y, int indice) {
    	super(x,y, indice);
        this.nome = nome;
        this.numPessoas = numPessoas;
    }

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getNumPessoas() {
		return numPessoas;
	}

	public void setNumPessoas(int numPessoas) {
		this.numPessoas = numPessoas;
	}
    
}
