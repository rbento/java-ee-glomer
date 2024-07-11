package glomer.model;

import java.io.Serializable;

public interface PersistableEntity extends Serializable {

    Long getId();

    void setId(Long id);

    boolean isNew();
}
