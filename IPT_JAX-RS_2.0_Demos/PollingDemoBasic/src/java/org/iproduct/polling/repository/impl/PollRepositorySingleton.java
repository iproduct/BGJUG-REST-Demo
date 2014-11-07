/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2003-2014 IPT - Intellectual Products & Technologies.
 * All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 with Classpath Exception only ("GPL"). 
 * You may use this file only in compliance with GPL. You can find a copy 
 * of GPL in the root directory of this project in the file named LICENSE.txt.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the GPL file named LICENSE.txt in the root directory of 
 * the project.
 *
 * GPL Classpath Exception:
 * IPT - Intellectual Products & Technologies (IPT) designates this particular 
 * file as subject to the "Classpath" exception as provided by IPT in the GPL 
 * Version 2 License file that accompanies this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 */
package org.iproduct.polling.repository.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.iproduct.polling.model.Poll;
import org.iproduct.polling.repository.Repository;

/**
 * Implements {@link org.iproduct.polling.model.Poll Poll} repository defined by 
 * {@link org.iproduct.polling.repository.PollRepository PollRepository} interfce 
 * 
 * @author Trayan Iliev, IPT [http://iproduct.org]
 * 
 */
public class PollRepositorySingleton extends AbstarctRepository<Long, Poll>{
	private static long sequence = 0; 
	private static PollRepositorySingleton repositoryInstance = new PollRepositorySingleton();
	
	public static PollRepositorySingleton getInstance() {
		return repositoryInstance;
	}
	
	private PollRepositorySingleton() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>> Poll Repository Constructed>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}
	
	@Override
	synchronized
	protected Long getNextId() {
		return ++sequence;
	}

	@Override
	protected Poll makeItemWithModifiedId(Long newId, Poll oldItem) {
		Poll newItem = new Poll(newId, oldItem.getTitle(), oldItem.getQuestion());
		return newItem;
	}

}
