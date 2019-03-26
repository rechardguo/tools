package rechard.dbs.tool.dao.generate;

public enum Fields {
	STRING("STRING"),
	PRIMARY("PRIMARY"),
	INTEGER("INTEGER"); 
	private final String field;
	private Fields(String field){
	        this.field=field;
	}
	public String toString(){
		return this.field;
	}
}
