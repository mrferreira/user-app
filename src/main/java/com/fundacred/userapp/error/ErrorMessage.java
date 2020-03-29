package com.fundacred.userapp.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
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
	
	public String json() {
		return String.format("{\"mensagem\": \"%s\"}", mensagem);
	}

	public ErrorMessage setMensagem(String mensagem) {
		this.mensagem = mensagem;
		return this;
	}
	
	public ErrorMessage emailAlreadyExists() {
		this.setMensagem(this.emailExists);
		return this;
	}
	
	public ErrorMessage invalidUserPassword() {
		this.setMensagem(this.invalidUserPassword);
		return this;
	}
	
	public ErrorMessage unauthorized() {
		this.setMensagem(this.unauthorized);
		return this;
	}
	
	public ErrorMessage invalidSession() {
		this.setMensagem(this.invalidSession);
		return this;
	}
	
}
