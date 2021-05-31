package glomer.mongodb;

import glomer.model.PersistableDocument;

import java.util.Objects;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

public abstract class AbstractDocument implements PersistableDocument {

    private static final long serialVersionUID = 1L;

    @Id
    private ObjectId id;

    @Override
    public boolean isNew() {
        return null == getId();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " { _id: " + id;
    }

    @Override
    public boolean equals(Object that) {

        if (this == that) {
            return true;
        }

        if (!(that instanceof AbstractDocument)) {
            return false;
        }

        final AbstractDocument other = (AbstractDocument) that;

        if (this.isNew() && other.isNew()) {
            return false;
        }

        return Objects.equals(getId(), other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
    }
}
