package ${packageName};
 
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.ArrayList;

import org.grnds.facility.log.GrndsLevel;
import org.grnds.facility.log.GrndsLogger;
import org.grnds.foundation.s1assert.GrndsAsserting;

import com.s1.arch.common.Criteria;
import com.s1.arch.common.Identifiable;
import com.s1.arch.common.Modifiable;
import com.s1.arch.i18n.currency.CurrencyTable;
import com.s1.arch.i18n.currency.CurrencyValue;
import com.s1.arch.i18n.currency.UnknownAlpha3CodeException;
import com.s1.arch.log.CSThreadContext;
import com.s1.arch.log.CSTrace;
import com.s1.arch.persistence.connection.ConnectionFactory;
import com.s1.arch.persistence.exception.PersistenceException;
import com.s1.arch.persistence.peer.PersistencePeer;
import com.s1.arch.persistence.statement.PersistenceStatement;
import com.s1.arch.persistence.statement.PersistenceStatementFactory;
import com.s1.arch.persistence.util.PersistenceConstants;
import com.s1.arch.persistence.util.PersistenceUtils;
import com.s1.arch.persistence.util.RowMapping;
import com.s1.arch.persistence.util.StatementBuilder;
import com.s1.arch.utilities.ObjectId;
import com.s1.common.product.ProductValue;
import com.s1.arch.utilities.S1Date;

/**
 * @author Rechard
 * ${nowDate?string("yyyy-MM-dd")}
 * this java is auto generate
 */
public class ${objectNameFirstUpper}Dao extends PersistencePeer{
    //logging and tracing
    public static final String CLASS_NAME = "${objectNameFirstUpper}Dao";
    public static final String TRACE_TAG = "${packageName}";
    public static final int TRACE_LEVEL = 6;
    private static final GrndsLogger sysLogger = GrndsLogger.getLogger("cs.system.logger");
    private static final String TABLE_NAME = "${tableName}";
    public ${objectNameFirstUpper}Dao() {
        sequenceName = "${tableName}";
    }
    /**
     * Column Names
     */ 
<#list fieldList as var>	
	<#if var[1]=='PRIMARY'>
	  	private static final String KEY = "${tableName}Ky";
	<#else>
		private static final String COL_${var[0]} = "${var[0]}";
	</#if>
</#list>
		private static final String COL_UPDATEUSER ="UPDATEUSER";
		private static final String COL_UPDATEDTTM ="UPDATEDTTM";
    /**
     * <code>getColumnNames</code> returns a list of the columns in the
     * ${tableName} Table.
     * @return List
     */
     protected List getColumnNames() {
		List<String> ret = new ArrayList<String>(); 
<#list fieldList as var>
	<#if var[1]=='PRIMARY'>
	  	ret.add(KEY);
	<#else>
		ret.add(COL_${var[0]});
	</#if>
</#list>
        return ret;
    }

    /**
     * <code>getTableName</code> returns the name of the ${tableName} Table
     * @return String
     */
    protected String getTableName() {
        return TABLE_NAME;
    }

    /**
     * <code>getPrimaryKeyColumnName</code> returns the name of the primary key
     * column of the ${tableName} Table
     * @return String
     */
    protected String getPrimaryKeyColumnName() {
        return KEY;
    }
    /**
     * <code>getRowMapping</code> used for updating.
     * @param Modifiable AccountData Object to be updated/inserted
     * @return RowMapping
     */
    public RowMapping getRowMapping(Modifiable obj) {
        final String METHOD_NAME = "getRowMapping(Modifiable obj)";
		
        try {
            if (CSTrace.isLoggable(TRACE_TAG)){
                Object[] params = {obj};
                CSTrace.entering(CLASS_NAME,METHOD_NAME,TRACE_TAG,params);
            }
            ${objectNameFirstUpper} pd = (${objectNameFirstUpper}) obj;
            RowMapping mapping = new RowMapping(TABLE_NAME, KEY);
                <#list fieldList as var>
                	 <#if var[1]=='PRIMARY'>
					  mapping.setLong(KEY, pd.getObjectId().getId());
					 </#if>
					 <#if var[1]=='STRING'>
					  mapping.setString(COL_${var[0]}, pd.get${var[2]?cap_first}());
					 </#if>
					 <#if var[1]=='INTEGER'>
					  mapping.setInteger(COL_${var[0]}, pd.get${var[2]?cap_first}());				
					 </#if>
					 <#if var[1]=='BOOLEAN'>
					   mapping.setBoolean(COL_${var[0]}, pd.get${var[2]?cap_first}());				
					 </#if>
					 <#if var[1]=='LONG'>
					   mapping.setLong(COL_${var[0]}, pd.get${var[2]?cap_first}());				
					 </#if>
				</#list>				
            
            return mapping;
        }catch (Exception e) {
            sysLogger.log(GrndsLevel.CRITICAL, "Failed in getRowMapping", e);
            CSTrace.log(TRACE_LEVEL,CLASS_NAME,METHOD_NAME,TRACE_TAG,e.getMessage());
            return null;
        }finally {
            if (CSTrace.isLoggable(TRACE_TAG))
                CSTrace.exiting(CLASS_NAME,METHOD_NAME,TRACE_TAG);
        }
    }
    
    /**
     * <code>restoreObject</code>retrieves the data from the row that the input
     * ResultSet is currently positioned at and builds an Identifiable object
     * (e.g., value object).
     * @param rs ResultSet returned from execute of SQL select statement
     * @return value object corresponding to the current database row
     * @throws SQLException thrown by methods invoked on rs (the ResultSet)
     */
    public Identifiable restoreObject(ResultSet rs) throws SQLException {
        final String METHOD_NAME = "restoreObject(ResultSet rs)";
        try {
            if (CSTrace.isLoggable(TRACE_TAG)){
                Object[] params = {rs};
                CSTrace.entering(CLASS_NAME,METHOD_NAME,TRACE_TAG,params);
            }
			${objectNameFirstUpper} result = new ${objectNameFirstUpper}();
			result.setObjectId(new ObjectId(rs.getInt(KEY)));               
            result.setUpdateUser(rs.getString(COL_UPDATEUSER));
             if(rs.getTimestamp(COL_UPDATEDTTM)!=null)
             result.setUpdateDateTime(S1Date.getS1Date(rs.getTimestamp(COL_UPDATEDTTM)));
<#list fieldList as var>
   <#if var[1]=='STRING'>
			result.set${var[2]?cap_first}(rs.getString(COL_${var[0]}));
   </#if>
</#list>
            return result;
        }catch (Exception e) {
            sysLogger.log(GrndsLevel.CRITICAL, "failed in restoreObject method", e);
            CSTrace.catching(CLASS_NAME,METHOD_NAME,TRACE_TAG,e);
            return null;
        }
        finally {
            if (CSTrace.isLoggable(TRACE_TAG))
                CSTrace.exiting(CLASS_NAME,METHOD_NAME,TRACE_TAG);
        }
    }
   
    public void collectGetSql(Criteria criteria, List sqls){
            final String METHOD_NAME = "collectGetSql(Criteria criteria, List sqls)";
            if (CSTrace.isLoggable(TRACE_TAG)){
                Object[] params = {criteria,sqls};
                CSTrace.entering(CLASS_NAME,METHOD_NAME,TRACE_TAG,params);
            }            
            ${objectName}Criteria cri = (${objectName}Criteria)criteria;
            PersistenceStatement statement = PersistenceStatementFactory.createStatement();
            statement.append("SELECT * FROM ");
            statement.append(TABLE_NAME);
            statement.append(" WHERE 1=1 ");
            try {
             if (cri.getObjectId()!=null) {
               statement.append(" and ");
      		   statement.append(KEY);
      		   statement.append(" = ");
      		   statement.add(cri.getObjectId());
      	      }
             sqls.add(statement);
            if(CSTrace.isLoggable(TRACE_TAG)){
                CSTrace.log(TRACE_LEVEL,CLASS_NAME,METHOD_NAME,TRACE_TAG,"Into collectGetSql  ->"+statement.toString());
            }            
        }catch (Exception e) {
            sysLogger.log(GrndsLevel.CRITICAL, "Failed in collectGetSQL method", e);
            CSTrace.catching(CLASS_NAME,METHOD_NAME,TRACE_TAG,e);
            if(CSTrace.isLoggable(TRACE_TAG)){
                CSTrace.log(TRACE_LEVEL,CLASS_NAME,METHOD_NAME,TRACE_TAG,e.getMessage());
            }
        }finally {
            if(CSTrace.isLoggable(TRACE_TAG)){
                CSTrace.exiting(CLASS_NAME,METHOD_NAME,TRACE_TAG);
            }
        }
    }

}
