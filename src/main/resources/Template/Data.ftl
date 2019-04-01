package ${packageName};

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.grnds.facility.log.GrndsTrace;

import com.s1.arch.log.CSTrace;
import com.s1.arch.security.authoriz.TargetEntityKey;
import com.s1.arch.tunneldata.BaseValue;
import com.s1.arch.utilities.ObjectId;
import com.s1.banking.common.CDStatementValue;
import com.s1.banking.common.Constants;
import com.s1.banking.common.StatementDetailValue;
import com.s1.banking.common.StatementValue;
import com.s1.arch.utilities.S1Date;

/**
 * @author Rechard
 * ${nowDate?string("yyyy-MM-dd")}
 * this java is auto generate
 */
public class ${objectNameFirstUpper} extends BaseValue {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public static final String TRACE_TAG = "${objectNameFirstUpper}";
    public static final int TRACE_LEVEL = 6;
    public static final String CLASS_NAME = "${objectNameFirstUpper}";
    /**
     * This object's id.
     */
  	private ObjectId objectId;
    private S1Date updateDateTime;
    private String updateUser;
    private int lockValue;
   
   <#list fieldList as var>                
	 <#if var[1]=='STRING'>					
	     private String ${var[2]};
	 </#if>
	 <#if var[1]=='INTEGER'>
	  	private int	${var[2]};		
	 </#if>
	 <#if var[1]=='BOOLEAN'>
	  	private boolean ${var[2]};				
	 </#if>
	 <#if var[1]=='LONG'>
	    private long ${var[2]};			
	 </#if>
	   <#if var[1]=='DOUBLE'>
       private double ${var[2]};
	   </#if>
	   <#if var[1]=='DATE'>
       private Date ${var[2]};
	   </#if>
	   <#if var[1]=='DATETIME'>
       private Timestamp ${var[2]};
	   </#if>
	</#list>
    /**
	 * @return the objectId
	 */
	public ObjectId getObjectId() {
		return objectId;
	}
	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(ObjectId objectId) {
		this.objectId = objectId;
	}
	
	public int getLockValue() {
       return lockValue;
   }

   public S1Date getUpdateDateTime() {
      return updateDateTime;
   }

   public String getUpdateUser() {
      return updateUser;
   }

   public void setLockValue(int _lockValue) {
      lockValue = _lockValue;
   }

   public void setUpdateDateTime(S1Date _updateDateTime) {
      updateDateTime = _updateDateTime;
   }

   public void setUpdateUser(String _updateUser) {
      updateUser = _updateUser;
   }
   
	<#list fieldList as var>                
	 <#if var[1]=='STRING'>					
		 public String get${var[2]?cap_first}() {
			return ${var[2]};
		 }
		 public void set${var[2]?cap_first}(String ${var[2]}) {
			this.${var[2]} = ${var[2]};
		 }
	 </#if>
	 <#if var[1]=='INTEGER'>
	  	 public int get${var[2]?cap_first}() {
			return ${var[2]};
		 }
		 public void set${var[2]?cap_first}(int ${var[2]}) {
			this.${var[2]} = ${var[2]};
		 }
	 </#if>
	 <#if var[1]=='BOOLEAN'>
	  	 public boolean get${var[2]?cap_first}() {
			return ${var[2]};
		 }
		 public void set${var[2]?cap_first}(boolean ${var[2]}) {
			this.${var[2]} = ${var[2]};
		 }	
	 </#if>
	 <#if var[1]=='LONG'>
	    public long get${var[2]?cap_first}() {
			return ${var[2]};
		 }
		 public void set${var[2]?cap_first}(long ${var[2]}) {
			this.${var[2]} = ${var[2]};
		 }		
	 </#if>
	<#if var[1]=='DATE'>
        public Date get${var[2]?cap_first}() {
        return ${var[2]};
        }
        public void set${var[2]?cap_first}(long ${var[2]}) {
        this.${var[2]} = ${var[2]};
        }
	</#if>
	<#if var[1]=='DATETIME'>
        public DateTime get${var[2]?cap_first}() {
        return ${var[2]};
        }
        public void set${var[2]?cap_first}(long ${var[2]}) {
        this.${var[2]} = ${var[2]};
        }
	</#if>
	<#if var[1]=='DOUBLE'>
        public double get${var[2]?cap_first}() {
        return ${var[2]};
        }
        public void set${var[2]?cap_first}(double ${var[2]}) {
        this.${var[2]} = ${var[2]};
        }
		</#if>
	</#list>
}
