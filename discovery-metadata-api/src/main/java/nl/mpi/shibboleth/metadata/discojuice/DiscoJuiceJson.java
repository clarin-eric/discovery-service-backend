package nl.mpi.shibboleth.metadata.discojuice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wilelb
 */
@XmlRootElement
public class DiscoJuiceJson {

    private List<DiscoJuiceJsonObject> objects = new ArrayList<>();

    /**
     * @return the objects
     */
    public List<DiscoJuiceJsonObject> getObjects() {
        return objects;
    }

    /**
     * @param objects the objects to set
     */
    public void setObjects(List<DiscoJuiceJsonObject> objects) {
        this.objects = objects;
    }

    public void addObject(DiscoJuiceJsonObject object) {
        objects.add(object);
    }     
}
