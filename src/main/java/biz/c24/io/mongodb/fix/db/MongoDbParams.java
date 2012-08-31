package biz.c24.io.mongodb.fix.db;

/**
 * Produced on behalf of C24 Technologies Ltd.
 * 
 * @author Matt Vickery - matt.vickery@incept5.com
 * @since 31/08/2012
 */
public interface MongoDbParams {
    public String getDatabase();
    public int getPort();
    public String getServer();
}
