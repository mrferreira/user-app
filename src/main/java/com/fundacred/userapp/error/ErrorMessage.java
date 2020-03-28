package com.fundacred.userapp.error;

import org.springframework.beans.factory.annotation.Value;

public class ErrorMessage {
	
	private String mensagem;
	
	@Value("${msg.email.exists}")
	protected String emailExists;
	@Value("${msg.user.password.invalid}")
	protected String invalidUserPassword;
	@Value("${msg.unauthorized}")
	protected String unauthorized;
	@Value("${msg.invalid.session}")
	protected String invalidSession;

	public String getMensagem() {
		return mensagem;
	}

	public ErrorMessage setMensagem(String mensagem) {
		this.mensagem = mensagem;
		return this;
	}
	
	public static ErrorMessage emailAlreadyExists() {
		ErrorMessage result = new ErrorMessage();
		return result.setMensagem(result.emailExists);
	}
	
	public static ErrorMessage invalidUserPassword() {
		ErrorMessage result = new ErrorMessage();
		return result.setMensagem(result.invalidUserPassword);
	}
	
	public static ErrorMessage unauthorized() {
		ErrorMessage result = new ErrorMessage();
		return result.setMensagem(result.unauthorized);
	}
	
	public static ErrorMessage invalidSession() {
		ErrorMessage result = new ErrorMessage();
		return result.setMensagem(result.invalidSession);
	}
	
}
