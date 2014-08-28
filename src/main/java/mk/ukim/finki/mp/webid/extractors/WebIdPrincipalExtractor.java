package mk.ukim.finki.mp.webid.extractors;

import java.awt.List;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.web.authentication.preauth.x509.X509PrincipalExtractor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

public class WebIdPrincipalExtractor implements X509PrincipalExtractor{
	
	private static final int SUBALTNAME_OTHERNAME = 0;
    private static final int SUBALTNAME_RFC822NAME = 1;
    private static final int SUBALTNAME_DNSNAME = 2;
    private static final int SUBALTNAME_X400ADDRESS = 3;
    private static final int SUBALTNAME_DIRECTORYNAME = 4;
    private static final int SUBALTNAME_EDIPARTYNAME = 5;
    private static final int SUBALTNAME_URI = 6;
    private static final int SUBALTNAME_IPADDRESS = 7;
    private static final int SUBALTNAME_REGISTREDID = 8;
	
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private Pattern subjectDnPattern;

    public WebIdPrincipalExtractor() {
        setSubjectDnRegex("CN=(.*?),");
    }
    
	@Override
	   public Object extractPrincipal(X509Certificate cert)
	   {
		
		String webId ="";
		
		try {
		
			
			Collection<java.util.List<?>> sub=cert.getSubjectAlternativeNames();
			
			if(sub==null)
				throw new BadCredentialsException("Sorry you don't have url for webid");
			
			Iterator<java.util.List<?>> i = sub.iterator();
			
			
			while(i.hasNext()){
				java.util.List<?> list= i.next();
				int OID= Integer.parseInt(list.get(0).toString());
				switch (OID) {
				case SUBALTNAME_URI:
					webId=list.get(1).toString();
					break;
				default:					
					webId+="Sorry you dont have web id";
					break;
				}
				
			}
			
			
		} catch (CertificateParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
		String subjectDN = cert.getSubjectDN().getName();

        System.out.println("Subject DN is '" + subjectDN + "'");

        Matcher matcher = subjectDnPattern.matcher(subjectDN);

        if (!matcher.find()) {
            throw new BadCredentialsException(messages.getMessage("SubjectDnX509PrincipalExtractor.noMatching",
                    new Object[] {subjectDN}, "No matching pattern was found in subject DN: {0}"));
        }

        if (matcher.groupCount() != 1) {
            throw new IllegalArgumentException("Regular expression must contain a single group ");
        }

        String username = matcher.group(1);

        System.out.println("Extracted Principal name is '" + username + "'");

        return username+";"+webId;
		
	   }
	
	public void setSubjectDnRegex(String subjectDnRegex) {
        Assert.hasText(subjectDnRegex, "Regular expression may not be null or empty");
        subjectDnPattern = Pattern.compile(subjectDnRegex, Pattern.CASE_INSENSITIVE);
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
	
}
