package com.ielfgame.stupidGame.config;

import com.ielfgame.stupidGame.batch.ElfBatch;
import com.ielfgame.stupidGame.batch.ElfBatch.BatchInput;
import com.ielfgame.stupidGame.data.ElfDataXML;
import com.ielfgame.stupidGame.enumTypes.BatchOp;

public class DefaultBatch extends ElfDataXML{
	public BatchOp batchOp; 
	public String source; 
	public String element; 
	public String attribute; 
	public String value1; 
	public String value2; 
	
	public String getBatch() {
		if(this.batchOp == null) {
			return null;
		}
		
		BatchInput batchInput = new BatchInput();
		batchInput.batchOp = this.batchOp;
		batchInput.source = this.source;
		
		return ElfBatch.batch(batchInput);
	} 
}
