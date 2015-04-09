package nl.mpi.shibboleth.ds.injector;

/**
 *
 * @author wilelb
 */
public abstract class AbstractDiscoJuiceInjector {
    protected String title;
    protected String subtitle;
    protected String metadataLocation;
    protected String helpMore;
    protected String acl;
    private String noScriptFallback;
    
    public abstract String getDiscoJuiceInject();
    
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the subtitle
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * @param subtitle the subtitle to set
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * @return the metadataLocation
     */
    public String getMetadataLocation() {
        return metadataLocation;
    }

    /**
     * @param metadataLocation the metadataLocation to set
     */
    public void setMetadataLocation(String metadataLocation) {
        this.metadataLocation = metadataLocation;
    }

    /**
     * @return the helpMore
     */
    public String getHelpMore() {
        return helpMore;
    }

    /**
     * @param helpMore the helpMore to set
     */
    public void setHelpMore(String helpMore) {
        this.helpMore = helpMore;
    }

    /**
     * @return the acl
     */
    public String getAcl() {
        return acl;
    }

    /**
     * @param acl the acl to set
     */
    public void setAcl(String acl) {
        this.acl = acl;
    }

    /**
     * @return the noScriptFallback
     */
    public String getNoScriptFallback() {
        return noScriptFallback;
    }

    /**
     * @param noScriptFallback the noScriptFallback to set
     */
    public void setNoScriptFallback(String noScriptFallback) {
        this.noScriptFallback = noScriptFallback;
    }
}
