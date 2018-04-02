import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.cassandra.config.ColumnDefinition;
import org.apache.cassandra.db.Clustering;
import org.apache.cassandra.db.Mutation;
import org.apache.cassandra.db.partitions.Partition;
import org.apache.cassandra.db.rows.Cell;
import org.apache.cassandra.db.rows.Unfiltered;
import org.apache.cassandra.db.rows.UnfilteredRowIterator;
import org.apache.cassandra.triggers.ITrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONObject;

/**
 * Hello world!
 *
 */
public class HelloWorld implements ITrigger
{
    private static final Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    @SuppressWarnings("unchecked")
	public Collection<Mutation> augment(Partition partition)
    {
        String tableName = partition.metadata().cfName;
        logger.info("Table: " + tableName);

        JSONObject obj = new JSONObject();
        obj.put("message_id", partition.metadata().getKeyValidator().getString(partition.partitionKey().getKey()));

        try {
            UnfilteredRowIterator it = partition.unfilteredIterator();
            while (it.hasNext()) {
                Unfiltered un = it.next();
                Clustering clt = (Clustering) un.clustering();  
                Iterator<Cell> cells = partition.getRow(clt).cells().iterator();
                Iterator<ColumnDefinition> columns = partition.getRow(clt).columns().iterator();

                while(columns.hasNext()){
                    ColumnDefinition columnDef = columns.next();
                    Cell cell = cells.next();
                    String data = new String(cell.value().array()); // If cell type is text
                    obj.put(columnDef.toString(), data);
                }
            }
        } catch (Exception e) {

        }
        logger.debug(obj.toString());

        return Collections.emptyList();
    }
}
