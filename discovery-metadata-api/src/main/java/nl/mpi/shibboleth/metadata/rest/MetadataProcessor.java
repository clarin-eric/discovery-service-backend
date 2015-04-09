package nl.mpi.shibboleth.metadata.rest;

import nl.mpi.shibboleth.metadata.shibboleth.EntityDescriptor;
import nl.mpi.shibboleth.metadata.source.MetadataSource;

/**
 *
 * @author wilelb
 */
public interface MetadataProcessor {
    public void process(EntityDescriptor descriptor, MetadataSource source);
    public long getIdpCount();
}
