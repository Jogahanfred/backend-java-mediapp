package com.jogahanfred;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//para TOKEN BD 

import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

//Primera Clase
@Configuration
//Habilitacion de configuracion de spring security
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	//traemos cadenas de texto que han sido declarados en properties
	@Value("${security.signing-key}")
	private String signingKey;
	@Value("${security.encoding-strength}")
	private Integer encodingStrength;
	@Value("${security.security-realm}")
	private String securityRealm;
	//Para encriptar el los usuarios 
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	//es en el lugar donde le decimos a Spring donde se encuentran los usuarios y claves
	@Autowired	
	private UserDetailsService userDetailsService;	
	//Esto para mas adelante guardar los token
	@Autowired
	private DataSource dataSource;	
	//Aqui generaremos los beans que seran utilizados mas adelante
	//Puedo tener claves encriptadas
	@Bean
	public BCryptPasswordEncoder passwordEnconder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();		
		return bCryptPasswordEncoder;
	}
	//Control de las sesiones
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	//Aqui busca una instancia del metodo de arriba, especifico que los usuarios y claves estan en userDetailsService
	//passwordEncoder(bcrypt).- este es el mecanismo para reconocer las claves 
	@Autowired	
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsService).passwordEncoder(bcrypt);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http		
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//Se coloca cuando el back no tiene un control de la vista
        .and()
        .httpBasic()
        .realmName(securityRealm)//nombre a la configuracion, properties
        .and()
        .csrf()//deshabilitamos los token csrf porque lo gestionara en la vista 
        .disable();        
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();//instancia de token
		converter.setSigningKey(signingKey);		//generando la firma 
		return converter;
	}
	
	@Bean//Aqui le digo donde almacenare los token
	public TokenStore tokenStore() {
		//return new JwtTokenStore(accessTokenConverter()); //EN MEMORIA
		return new JdbcTokenStore(this.dataSource); //EN BASE DE DATOS
	}
	
	@Bean
	@Primary//generacion de token 
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);			
		defaultTokenServices.setReuseRefreshToken(false);//utilizar token de refresco	
		return defaultTokenServices;
	}
	
	
}
