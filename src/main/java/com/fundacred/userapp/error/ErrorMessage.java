package com.fundacred.userapp.error;

import org.springframework.beans.factory.annotation.Value;

public class ErrorMessage {
	
	@Value("${msg.email.exists}")
	private String emailExistente;
	@Value("${msg.user.password.invalid}")
	private String invalidUserPassword;
	@Value("${msg.unauthorized}")
	private String unauthorized;
	@Value("${msg.invalid.session}")
	private String invalidSession;

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public ErrorMessage emailAlreadyExists() {
		ErrorMessage result = new ErrorMessage();
		result.setMensagem();
	}
	
}
