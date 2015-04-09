package nl.mpi.shibboleth.ds.injector;

/**
 *
 * @author wilelb
 */
public class DiscoJuice20Injector extends AbstractDiscoJuiceInjector{
    
    @Override
    public String getDiscoJuiceInject() {
        String preselectedIDP = "https://idp.mpi.nl/idp-damlr";
                
        StringBuilder discoJuiceInject = new StringBuilder();
        /* CLARIN discojuice configure method */
        discoJuiceInject.append("function getConfig (title, subtitle, preselectedIDP) {\n");
            discoJuiceInject.append("var options, i;\n");
            discoJuiceInject.append("options = {\n");
                    discoJuiceInject.append("'title': title,\n");
                        //discoJuiceInject.append(title);
                    //discoJuiceInject.append(",\n");
                    discoJuiceInject.append("'subtitle': subtitle,\n");
                        //discoJuiceInject.append(subtitle);
                    //discoJuiceInject.append("',\n");
                    discoJuiceInject.append("'disco': {");
                        discoJuiceInject.append("'url': './discojuiceDiscoveryResponse.html'");
                    discoJuiceInject.append("},\n");
                    discoJuiceInject.append("'cookie': true,\n");
                    discoJuiceInject.append("'country': true,\n");
                    discoJuiceInject.append("'location': true,\n");
                    discoJuiceInject.append("'discoPath': './js/discojuice/',\n");
                    discoJuiceInject.append("'metadata': [],\n");
                    discoJuiceInject.append("'preselected': preselectedIDP,\n");
            discoJuiceInject.append("};\n");
            discoJuiceInject.append("options.metadata.push('");
                discoJuiceInject.append(metadataLocation);
            discoJuiceInject.append("');\n");              
            discoJuiceInject.append("return options;\n");
        discoJuiceInject.append("}\n");
        discoJuiceInject.append("\n");
        /* CLARIN discojuice object */
        discoJuiceInject.append("DiscoJuice.CLARIN = {\n");
            discoJuiceInject.append("'getConfig': getConfig,\n");
            discoJuiceInject.append("'setup': function (target, title, spentityid, responseurl, feeds, redirectURL) {\n");
                    discoJuiceInject.append("var options;\n");
                    discoJuiceInject.append("options = getConfig(title, spentityid, responseurl, feeds, redirectURL);\n");
                discoJuiceInject.append("}\n");
        discoJuiceInject.append("};\n");
        discoJuiceInject.append("\n");
        
        discoJuiceInject.append("DiscoJuice.Dict.helpMore = '");
        discoJuiceInject.append(helpMore);
        discoJuiceInject.append("';");
        
        /* Inject discojuice in the page */
        discoJuiceInject.append("$('document').ready(function() {\n");
            discoJuiceInject.append("var djc = DiscoJuice.CLARIN.getConfig(\n");
                discoJuiceInject.append("'");
                    discoJuiceInject.append(title);
                    discoJuiceInject.append("','");
                    discoJuiceInject.append(subtitle);
                    discoJuiceInject.append("','");
                    discoJuiceInject.append(preselectedIDP);
            discoJuiceInject.append("');\n");
            discoJuiceInject.append("djc.always = true;\n");
            discoJuiceInject.append("djc.callback = IdPDiscovery.setup(djc, '');\n");            
            discoJuiceInject.append("$('a.signin').DiscoJuice(djc);\n");
        discoJuiceInject.append("});\n");
        
        return discoJuiceInject.toString();
    }
}
