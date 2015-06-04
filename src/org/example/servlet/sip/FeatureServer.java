package org.example.servlet.sip;

import javax.annotation.Resource;
import javax.servlet.sip.Address;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.TooManyHopsException;
import javax.servlet.sip.URI;

public class FeatureServer extends SipServlet {
	@Resource
	public SipFactory factory;
	
	public void doInvite(SipServletRequest request) throws TooManyHopsException, ServletParseException {
		SipSession sipSession = request.getSession();
		
		System.out.println("Do Invite has been invoked");
		// Create URI w/ factory (Vanessa's IP).
		Address daveAddr = this.factory.createAddress("sip:alice@172.18.102.139:5080");
		Address vanAddr = this.factory.createAddress("sip:alice@172.18.102.134:5080");
		
		// Get the proxy.
		Proxy proxy = request.getProxy();
		
		// Look for attribute in Session.
		int activeState = 0;
		if( null != (sipSession.getAttribute("active")) ) {
			System.out.println("Active Attribute: true");
			activeState++;
			// Push route to 'Call Logging' server.
			request.pushRoute(daveAddr);
			// Push route to my server.
			request.pushRoute(vanAddr);
			// Set Session attribute.
			sipSession.setAttribute("active", activeState);
		} else {
			System.out.println("Active attribute: false");
			activeState = 0;
		}
		proxy.proxyTo(request.getRequestURI());
	}
}
