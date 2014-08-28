package mk.ukim.finki.mp.webid.filters;

import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mk.ukim.finki.mp.webid.extractors.WebIdContentExtraxtorImpl;
import mk.ukim.finki.mp.webid.extractors.WebIdPrincipalExtractor;
import mk.ukim.finki.mp.webid.store.SessionWebIdContentStore;
import mk.ukim.finki.mp.webid.store.WebIdContentStore;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.x509.SubjectDnX509PrincipalExtractor;
import org.springframework.security.web.authentication.preauth.x509.X509PrincipalExtractor;
import org.springframework.context.annotation.Bean;

public class WebIdAuthenticationFilter extends
		AbstractPreAuthenticatedProcessingFilter {

	private X509PrincipalExtractor principalExtractor = new WebIdPrincipalExtractor();

	public static int PRINCIPAL_USERNAME = 0;
	public static int PRINCIPAL_WEBID = 1;
	public String webId;
	public String username;

	public WebIdAuthenticationFilter() {

		System.out.println("Hello from web id authentication filter");

	}

	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		X509Certificate cert = extractClientCertificate(request);

		if (cert == null) {
			return null;
		}

		String result = (String) principalExtractor.extractPrincipal(cert);
		String[] tokens = result.split(";");
		username = tokens[PRINCIPAL_USERNAME];
		webId = tokens[PRINCIPAL_WEBID];

		return username;
	}

	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return extractClientCertificate(request);

	}

	private X509Certificate extractClientCertificate(HttpServletRequest request) {
		X509Certificate[] certs = (X509Certificate[]) request
				.getAttribute("javax.servlet.request.X509Certificate");

		if (certs != null && certs.length > 0) {

			return certs[0];
		}

		System.out.println("No client certificate found in request.");

		return null;
	}

	public void setPrincipalExtractor(X509PrincipalExtractor principalExtractor) {
		this.principalExtractor = principalExtractor;
	}

	@Override
	protected void successfulAuthentication(
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response,
			Authentication authResult) {
		System.out.println("successfulAuthentication");

		WebIdContentExtraxtorImpl extractor = new WebIdContentExtraxtorImpl();

		String content = extractor.extract(webId);
		System.out.println("From successful " + content);

		WebIdContentStore store = new SessionWebIdContentStore();
		store.store(content, request, response);
		store.storeWebId(webId, request, response);

		super.successfulAuthentication(request, response, authResult);

	}

	@Override
	protected void unsuccessfulAuthentication(
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response,
			AuthenticationException failed) {

		System.out.println("Cleared security context due to exception + ");
		failed.printStackTrace();
	}

}