/*
 * Created on 16.12.2005
 *
 */
package net.raysforge.commons;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 * @author Ray
 *
 */
public class LdapContext {


	public static void listSchema(DirContext ctx) throws NamingException {
		Attributes ab = ctx.getAttributes("cn=schema");
		NamingEnumeration<String> ne = ab.getIDs();

		while (ne.hasMore()) {
			String id = ne.next();
			Attribute att = ab.get(id);
			System.out.println("ID: " + id);
			if (id.equals("objectClasses")) {
			}
			if (id.equals("attributeTypes2")) {
				NamingEnumeration<?> ne2 = att.getAll();
				att.size();
				while (ne2.hasMore()) {
					Object o2 = ne2.next();
					String s = (String) o2;
					// NameClassPair ncp = (NameClassPair) ne.next();
					// ncp.setRelative( false);
					if (s.contains("klb") || s.contains("car"))
						System.out.println("n: " + s);
				}
			}
		}
	}

	public void addOU(DirContext ctx, String dn, String ou) throws NamingException {
		BasicAttribute objclassSet = new BasicAttribute("objectclass");
		objclassSet.add("organizationalUnit");
		// Add single and multi-valued attributes to the
		// load variable (attrs).
		BasicAttributes attrs = new BasicAttributes();
		attrs.put(objclassSet);
		attrs.put("ou", ou);
		// attrs.put("cn", cn);
		ctx.createSubcontext(dn, attrs);
	}

	public static Attributes genUser(String cn, String sn, String givenname, String ou, String password, String email, String phone, String fax, String city) throws NamingException {
		Attribute oc = new BasicAttribute("objectclass");
		oc.add("inetOrgPerson");
		oc.add("organizationalPerson");
		oc.add("person");
		oc.add("top");

		Attribute ouSet = new BasicAttribute("ou");
		ouSet.add(ou);

		Attributes myAttrs = new BasicAttributes(true);
		myAttrs.put(oc);
		myAttrs.put(ouSet);

		myAttrs.put("givenname", givenname);
		myAttrs.put("sn", sn);
		myAttrs.put("cn", cn);
		// myAttrs.put("title", "Herr");
		return myAttrs;
	}

	public static String getPrincipal(String login, String inner_ou, String ou, String o) {
		String principal = "";
		if (Service.hasLength(login))
			principal += "cn=" + login + ", ";
		if (Service.hasLength(inner_ou))
			principal += "ou=" + inner_ou + ", ";
		if (Service.hasLength(ou))
			principal += "ou=" + ou + ", ";
		if (Service.hasLength(o))
			principal += "o=" + o;
		return principal;
	}

	public static void print(Attributes attributes) throws NamingException {
		NamingEnumeration<String> ids = attributes.getIDs();
		while (ids.hasMoreElements()) {
			String id = ids.next();
			Attribute val = attributes.get(id);
			for (int i = 0; i < val.size(); i++) {
				System.out.println(id + ": " + val.get(i));
				// if(!"java.lang.String".equals(val.get(i).getClass().getName()))
				// System.out.println(id + " ------------> " + val.get(i).getClass().getName());
			}
		}
	}

	public static DirContext getLdapContextWithoutSSL(String server, String principal, String pass) throws NamingException {
		return getLdapContext(server, 389, false, "simple", null, null, principal, pass);
	}

	public static DirContext getLdapContextWithSSL(String server, String principal, String pass) throws NamingException {
		return getLdapContext(server, 636, true, "simple", null, null, principal, pass);
	}

	public static DirContext getLdapContext(String server, int port, boolean ssl, String auth, String trustStore, String trustStorePassword, String principal, String pass) throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

		env.put(Context.PROVIDER_URL, "ldap://" + server + ":" + port); // + "/o=demo"
		env.put("java.naming.ldap.attributes.binary", "GUID");
		// env.put("java.naming.ldap.attributes.binary", "networkAddress");
		env.put(Context.AUTHORITATIVE, "true");
		if (ssl) {
			env.put("java.naming.ldap.version", "3");
			env.put(Context.SECURITY_PROTOCOL, "ssl");
			env.put(Context.REFERRAL, "ignore");
			env.put("java.naming.ldap.factory.socket", SimpleSSLSocketFactory.class.getName());
			// System.setProperty( "javax.net.ssl.trustStore", trustStore);
			// System.setProperty( "javax.net.ssl.trustStorePassword", trustStorePassword);
		}

		if ("dig".equals(auth)) {
			env.put(Context.SECURITY_AUTHENTICATION, "DIGEST-MD5");
			env.put(Context.SECURITY_PRINCIPAL, "dn:" + principal);
			env.put(Context.SECURITY_CREDENTIALS, pass);
		} else if ("simple".equals(auth)) {
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, principal);
			env.put(Context.SECURITY_CREDENTIALS, pass);
		}

		return new InitialDirContext(env);
	}

}