package exception;

public class OperacaoInvalidaException extends RuntimeException {
	private static final long serialVersionUID = -994753840728974048L;

	public OperacaoInvalidaException(String message) {
		super(message);
	}
}
