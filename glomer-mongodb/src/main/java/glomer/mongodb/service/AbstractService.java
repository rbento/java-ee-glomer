package glomer.mongodb.service;

import glomer.model.Annotations;
import glomer.model.annotation.Db;
import glomer.mongodb.AbstractDocument;

import java.net.UnknownHostException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

public class AbstractService {

    private static final Logger log = LoggerFactory.getLogger(AbstractService.class);

    @Resource(name = "host")
    private String host;

    @Resource(name = "port")
    private Integer port;

    @Resource(name = "rootPackage")
    private String rootPackage;

    private Mongo mongo;

    private Morphia morphia;

    @PostConstruct
    public void initialize() {

        log.debug("Initializing...");

        log.debug("host: {}", host);
        log.debug("port: {}", port);

        try {

            mongo = new MongoClient(host, port);

            log.debug("mongo: {}", mongo);
            
        } catch (UnknownHostException ex) {

            throw new IllegalStateException("Couldn't initialize AbstractService. Is MongoDB up and running? Are the host and port values correct?", ex);
        }

        morphia = new Morphia();

        log.debug("morphia: {}", morphia);

        morphia.mapPackage(rootPackage, true);

        log.debug("rootPackage: {}", rootPackage);
    }

    public <T> Query<T> query(final String db, Class<T> type) {
        if (null == type) {
            throw new IllegalArgumentException("Trying to query with a null type.");
        }
        return getDatastore(db).createQuery(type);
    }

    public <T extends AbstractDocument> List<T> findAll(Class<T> type) {
        if (!type.isAnnotationPresent(Db.class)) {
            throw new IllegalArgumentException("Trying to findAll with a type that is missing @Db annotation.");
        }
        String db = Annotations.getValueAsString(type, Db.class, "name");
        return findAll(db, type);
    }

    public <T> List<T> findAll(final String db, Class<T> type) {
        return query(db, type).asList();
    }

    public <T> T findById(final String db, Class<T> type, Object id) {
        return find(db, type, Mapper.ID_KEY, id);
    }

    public <T extends AbstractDocument> T find(Class<T> type, String key, Object value) {
        if (!type.isAnnotationPresent(Db.class)) {
            throw new IllegalArgumentException("Trying to find with a type that is missing @Db annotation.");
        }
        String db = Annotations.getValueAsString(type, Db.class, "name");
        return find(db, type, key, value);
    }

    public <T> T find(final String db, Class<T> type, String key, Object value) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("Trying to find with a blank key.");
        }
        return query(db, type).filter(key, value).get();
    }

    @SuppressWarnings("unchecked")
    public <T> T save(final String db, T object) {

        log.debug("Saving...");

        log.debug("db: {}", db);
        log.debug("object: {}", object);

        Class<T> type = (Class<T>) object.getClass();
        log.debug("type: {}", type);

        Datastore ds = getDatastore(db);

        Key<T> key = ds.save(object);
        log.debug("key: {}", key);

        T result = ds.getByKey(type, key);
        log.debug("result: {}", result);

        return result;
    }

    public <T> T update(final String db, Query<T> query, UpdateOperations<T> uops) {

        log.debug("Updating...");

        log.debug("db: {}", db);
        log.debug("query: {}", query);
        log.debug("uops: {}", uops);

        T result = getDatastore(db).findAndModify(query, uops);

        log.debug("result: {}", result);

        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T merge(final String db, T object) {

        log.debug("Merging...");

        log.debug("db: {}", db);
        log.debug("object: {}", object);

        Class<T> type = (Class<T>) object.getClass();
        log.debug("type: {}", type);

        Datastore ds = getDatastore(db);

        Key<T> key = ds.merge(object);
        log.debug("key: {}", key);

        T result = ds.getByKey(type, key);

        log.debug("result: {}", result);

        return result;
    }

    public <T> void delete(final String db, T object) {
        getDatastore(db).delete(object);
    }

    public <T> boolean valueExists(final String db, final Class<T> type, final String key, final Object value) {
        return null != find(db, type, key, value);
    }

    public void ensureAll(final String db) {
        Datastore ds = getDatastore(db);
        ds.ensureCaps();
        ds.ensureIndexes(true);
    }

    public Datastore getDatastore(final String db) {
        if (StringUtils.isBlank(db)) {
            throw new IllegalArgumentException("Trying to get an Datastore with a blank db.");
        }
        return getMorphia().createDatastore(getMongo(), db);
    }

    public void dropDb(final String db) {
        getMongo().dropDatabase(db);
    }

    public DB getDb(final String db) {
        return getMongo().getDB(db);
    }

    public DBCollection getCollection(final String db, final String collection) {
        return getMongo().getDB(db).getCollection(collection);
    }

    protected Mongo getMongo() {
        return mongo;
    }

    protected Morphia getMorphia() {
        return morphia;
    }

    public String getHost() {
        return host;
    }

    protected void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    protected void setPort(Integer port) {
        this.port = port;
    }

    public String getRootPackage() {
        return rootPackage;
    }

    protected void setRootPackage(String rootPackage) {
        this.rootPackage = rootPackage;
    }
}
