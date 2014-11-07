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
package org.iproduct.polling.repository;

import java.util.Collection;
import java.util.ConcurrentModificationException;

import org.iproduct.polling.common.EntityDoesNotExistException;
import org.iproduct.polling.model.Identifiable;
import org.iproduct.polling.model.Poll;

/**
 * Defines {@link org.iproduct.polling.Item Item} repository providing command and query operations
 * 
 * @author Trayan Iliev, IPT [http://iproduct.org]
 * 
 */
public interface Repository<K extends Comparable<K>, V extends Identifiable<K>> {
	
	/**
	 * Get a item by its identifier or null if such item does not exist
	 * @param id item identifier
	 * @return item instance
	 */
	public V getItemById(K id);
	
	/**
	 * Get all items
	 * @return list of all items
	 */
	public Collection<V> getAllItems();
	
	/**
	 * Get range of items starting with {@code fromId} with maximal number of {@code numberItems}
	 * 
	 * @param fromId the item identifier to start with
	 * @param maxSize the maximal number of items to be returned
	 * @return list of items in a given range
	 */
	public Collection<V> getRangeItems(K fromId, Integer maxSize);
	
	/**
	 * Add new item to repository updating its id by auto-increment sequence
	 * @param item the new item to add 
	 * @return a new item instance with updated id
	 */
	public V addItem(V item);
	
	/**
	 * Update existing item in the repository. If the item does not exist 
	 * {@link org.iproduct.rest.hateoas.jaxrs.iteming.common.EntityDoesNotExistException EntityDoesNotExistException}
	 * is thrown. If item has been concurently modified
	 * @param item the new item to add
	 * @return new instance representing the updated item state
	 */
	public V updateItem(V item, String eTag) throws EntityDoesNotExistException, ConcurrentModificationException;
	
	/**
	 * Delete existing item in the repository
	 * @param item the new item to add
	 * @return new instance representing the updated item state
	 */
	public V deleteItem(K itemId) throws EntityDoesNotExistException;
	
	/**
	 * Get the total number of items in the repository
	 * @return the total number of items
	 */
	public String getItemsCount();
	
}
