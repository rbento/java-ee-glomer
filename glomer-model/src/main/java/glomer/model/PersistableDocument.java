package glomer.model;

import java.io.Serializable;
import org.bson.types.ObjectId;

public interface PersistableDocument extends Serializable {

    ObjectId getId();

    void setId(ObjectId id);

    boolean isNew();
}
