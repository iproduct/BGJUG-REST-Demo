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
package org.iproduct.polling.common;

/**
 * Exception thrown when trying to update or delete non-existing 
 * {@link org.iproduct.polling.repository.PollRepository Poll} entity
 * 
 * @author Trayan Iliev, IPT [http://iproduct.org]
 * 
 */
public class EntityDoesNotExistException extends Exception {
	private static final long serialVersionUID = 1L;
	private Object entity;
	
	/**
	 * 
	 */
	public EntityDoesNotExistException(Object entity) {
		this.entity = entity;
	}

	/**
	 * @param message
	 */
	public EntityDoesNotExistException(Object entity, String message) {
		super(message);
		this.entity = entity;
	}

	/**
	 * @param cause
	 */
	public EntityDoesNotExistException(Object entity, Throwable cause) {
		super(cause);
		this.entity = entity;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EntityDoesNotExistException(Object entity, String message, Throwable cause) {
		super(message, cause);
		this.entity = entity;
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public EntityDoesNotExistException(Object entity, String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.entity = entity;
	}
	
	@Override
	public String getMessage() {
		return "Entity " + entity + " does not exist.";
	}

}
