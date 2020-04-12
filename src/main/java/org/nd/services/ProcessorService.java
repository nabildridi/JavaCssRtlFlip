package org.nd.services;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

import org.nd.models.Css;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ProcessorService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    ThreadPoolTaskExecutor threadPool;
	
	public List<Css> process(List<Css> list) {
		
		LinkedList<Future<Css>> futureslist = new LinkedList<>();
		LinkedList<Css> responses = new LinkedList<>();
		
		for(Css item : list) {		
			
			Future<Css> f = threadPool.submit(new CssThread(item));
			futureslist.add(f);
		}
		
		
		Iterator<Future<Css>> futureIter = futureslist.iterator();		
		while (futureIter.hasNext()) {
			
			try {
				Css response = futureIter.next().get();
				responses.add(response);
				futureIter.remove();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}


		return responses;
	}
	
}
