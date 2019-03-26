CREATE TABLE `${tableName}` ( 		
	<#list fieldList as var>	
		<#if var[1] == 'PRIMARY'>
			`${tableName}KY` int(11) NOT NULL PRIMARY KEY,
		<#elseif var[1] == 'INTEGER'>
			`${var[0]}` int(11),
		<#elseif var[1] == 'LONG'>
             `${var[0]}` bigint,
		<#elseif var[1] == 'DATE'>
            `${var[0]}` date,
		<#elseif var[1] == 'DATETIME'>
            `${var[0]}` datetime,
		<#elseif var[1] == 'DOUBLE'>
        	`${var[0]}` double,
		<#else>
			`${var[0]}` varchar(100),
		</#if>
	</#list>
	 `UPDATEDTTM` timestamp NOT NULL DEFAULT current_timestamp(),
 	 `UPDATEUSER` varchar(32) COLLATE utf8_bin NOT NULL,
  	 `VERSIONSTAMP` smallint(6) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



INSERT INTO UTLSeqNumGnrtr (UTLSeqNumGnrtrKy, seqName, nextSeqNum, seqNumRange)
    SELECT DISTINCT(SELECT COALESCE(MAX(UTLSeqNumGnrtrKy), 0) + 1 FROM UTLSeqNumGnrtr), '${tableName}', 1,500  FROM dual
      WHERE NOT EXISTS (SELECT 1 FROM UTLSeqNumGnrtr WHERE seqName = '${tableName}');
