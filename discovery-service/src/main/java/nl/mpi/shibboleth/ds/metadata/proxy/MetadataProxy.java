package nl.mpi.shibboleth.ds.metadata.proxy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wilelb
 */
public class MetadataProxy extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(MetadataProxy.class);
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String charset = "UTF-8";
        response.setContentType("text/html;charset="+charset);

        String jsonMetadata = getServletContext().getInitParameter("metadata-source");

        BufferedReader br = null;
        PrintWriter out = response.getWriter();
        
        String acceptHeader = request.getHeader("Accept-Language");
        logger.debug("Accept header: {}", acceptHeader);
        
        List<String> languageList = new ArrayList<String>();
        
        if(acceptHeader == null) {
            languageList.add("en");
        } else {
            String[] languages = acceptHeader.split(",");
            Pattern regex = Pattern.compile("(.+);q=(.+)");
            for(String language : languages) {
                Matcher m = regex.matcher(language);

                String lang = "en";
                float quality = 1; //default quality
                if(m.matches()) {
                    lang = m.group(1);
                    quality = Float.parseFloat(m.group(2));                
                } else {
                    lang = language;
                }

                languageList.add(lang);                
            }
        }
        
        try {
            if(jsonMetadata == null) {
                throw new IllegalStateException("Configuration problem. <metadata-source> is not configured.");
            }            
            URL url = new URL(jsonMetadata);
            if(url.getProtocol().equalsIgnoreCase("file")) {
                String f = jsonMetadata.replaceAll("file://", "");
                logger.info("Proxying local file [{}]", f);
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            } else {
                logger.info("Proxying url [{}]", jsonMetadata);
                br = new BufferedReader(new InputStreamReader(url.openStream(),charset));
            }
            
            //proxy url contents to output
            String line = null;
            StringBuilder discojuiceJsonBuffer = new StringBuilder();
            while((line=br.readLine()) != null) {
                discojuiceJsonBuffer.append(line.trim());
            }
            
            StringBuilder buffer = new StringBuilder();
            buffer.append("{\"custom\": ");
                buffer.append("{\"languages\": [");
                int index = 0;
                for(String lang : languageList) {
                    buffer.append("\"");
                    buffer.append(lang);
                    buffer.append("\"");
                    if(index < languageList.size()-1) {
                        buffer.append(",");
                    }
                    index++;
                }
                buffer.append("]");
            buffer.append("}, \"discojuice\": ");
            buffer.append(discojuiceJsonBuffer.toString());
            buffer.append("}");

            out.println(buffer.toString());
            
            //out.println(discojuiceJsonBuffer.toString());
        } catch(MalformedURLException ex) {
            logger.error("Malformed url [" + jsonMetadata + "]", ex);
            throw ex;
        } catch(IllegalStateException ex) {
            logger.error("", ex);
            throw ex;
        } finally {
            if(out != null) {
                out.close();
            }
            if(br != null) {
                br.close();
            }
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    

}
