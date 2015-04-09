<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ page import="edu.internet2.middleware.shibboleth.idp.util.HttpServletHelper" %>
<%@ page import="org.opensaml.util.storage.StorageService" %>
<%@ page import="edu.internet2.middleware.shibboleth.idp.authn.LoginContext" %>
<%@ page import="edu.internet2.middleware.shibboleth.common.relyingparty.RelyingPartyConfigurationManager" %>
<%@ page import="org.opensaml.saml2.metadata.EntityDescriptor" %>
 
<%
   StorageService storageService = HttpServletHelper.getStorageService(application);
   RelyingPartyConfigurationManager rpConfigMngr = HttpServletHelper.getRelyingPartyConfigurationManager(application);
   LoginContext loginContext = HttpServletHelper.getLoginContext(storageService,application, request);
   
   
   String spID = loginContext.getRelyingPartyId();
   EntityDescriptor metadata = HttpServletHelper.getRelyingPartyMetadata(spID, rpConfigMngr);
%>
<html>

    <head>
        <title>Shibboleth Identity Provider - Login</title>
        
        <link rel='stylesheet' href='./style.css' type='text/css' />
    </head>

    <body>

        <div id="page-top">
            <div id="inner_header">
                <div id="bannerleft">
                    <a href="http://tla.mpi.nl/" title="The Language Archive"></a>
                </div>
                <div id="bannerright"></div>
            </div>
        </div>
        
        <div id="page-bottom">
            <div id="content">
<!--
                <div id="content_header">
                    <h2>Login to ... service provider</h2>
                </div>
-->
                <div id="left" >
                    
                    <p>You are accessing <%=spID%> and have selected the CLARIN EU website as your home institution.</p>
                    <br/>
                    <div id="formwrapper">
                    <% if ("true".equals(request.getAttribute("loginFailed"))) {%>
                        <p><font color="red">Authentication Failed</font></p>
                    <% }%>

                    <% if (request.getAttribute("actionUrl") != null) {%>
                        <form action="<%=request.getAttribute("actionUrl")%>" method="post">
                    <% } else {%>
                        <form action="j_security_check" method="post">
                    <% }%>
                            <table>
                                <tr>
                                    <td>Username:</td>
                                    <td><input name="j_username" type="text" tabindex="1" /></td>
                                </tr>
                                <tr>
                                    <td>Password:</td>
                                    <td><input name="j_password" type="password" tabindex="2" /></td>
                                </tr>
                                <tr>
                                    <td colspan="2"><center><input type="submit" value="Login" tabindex="3" /></center></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                </div>  

                <div id="right" class="center">
                    <h3>Service Provider</h3>
                    name<br />
                    description
                    <h3>No account yet?</h3>
                    <a href="http://lux16.mpi.nl/ds/RRS_V1/RrsIndex">Register an account</a>
                </div>
            </div>
        </div>
                        
    </body>

</html>