package nl.mpi.shibboleth.metadata.rest;

import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import nl.mpi.shibboleth.metadata.shibboleth.EntityDescriptor;
import nl.mpi.shibboleth.metadata.shibboleth.IDPSSoDescriptor;
import nl.mpi.shibboleth.metadata.shibboleth.UIInfo;
import nl.mpi.shibboleth.metadata.source.MetadataSource;

/**
 *
 * @author wilelb
 */
public class MetadataLanguageProcessor implements MetadataProcessor {

    private long idpCount = 0;
    
    private final Set<String> languages = new HashSet<>();
    
    @Override
    public void process(EntityDescriptor descriptor, MetadataSource source) {
        
        if (descriptor.isIDPDescriptor()) {
            for(IDPSSoDescriptor d: descriptor.getIdpSsoDescriptors()) {
                if(d.getExtensions() != null) {
                    UIInfo uiInfo = d.getExtensions().getInfo();
                    if(uiInfo != null) {
                        for(UIInfo.Name name : uiInfo.getNames()) {
                            languages.add(name.getLang());
                        }
                    }
                }
            }
                    
            idpCount++;
        }
    }

    /**
     * @return the idpCount
     */
    @Override
    public long getIdpCount() {
        return idpCount;
    }

    /**
     * @return the languages
     */
    public Languages getLanguages() {
        return new Languages(languages);
    }
    
    @XmlRootElement
    public static class Languages {
        public Set<String> languages;
        
        public Languages() {}
        
        public Languages(Set<String> languages) {
            this.languages = languages;
        }
    }
}
