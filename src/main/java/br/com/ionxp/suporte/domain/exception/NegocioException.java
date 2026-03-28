package br.com.ionxp.suporte.domain.exception;

public class NegocioException extends Exception {

	private static final long serialVersionUID = 1887498916385892265L;
	
	public NegocioException(String message) {
		super(message);
	}
}
