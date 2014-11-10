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
package org.iproduct.polling.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Models a polling survey with id, title and question
 * 
 * @author Trayan Iliev, IPT [http://iproduct.org]
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Poll implements Identifiable<Long>{
	Long id;
	String title;
	String question;

	/**
	 * No argument constructor
	 */
	public Poll() {
	}

	/**
	 * Full constructor
	 * 
	 * @param id
	 *            poll identifier
	 * @param title
	 *            poll title
	 * @param question
	 *            poll question
	 */
	public Poll(Long id, String title, String question) {
		this.id = id;
		this.title = title;
		this.question = question;
	}

	/**
	 * Get the poll identifier
	 * @return the poll identifier
	 */
	public Long getId() {
		return id;
	}

//	/**
//	 * Set the poll identifier
//	 * @param id
//	 *            the poll identifier to set
//	 */
//	public void setId(long id) {
//		this.id = id;
//	}

	/**
	 * Get the poll title
	 * @return the poll title
	 */
	public String getTitle() {
		return title;
	}

//	/**
//	 * Set the poll title
//	 * @param title
//	 *            the poll title to set
//	 */
//	public void setTitle(String title) {
//		this.title = title;
//	}

	/**
	 * Get the poll question
	 * @return the poll question
	 */
	public String getQuestion() {
		return question;
	}

//	/**
//	 * Set the poll question
//	 * @param question
//	 *            the poll question to set
//	 */
//	public void setQuestion(String question) {
//		this.question = question;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Poll other = (Poll) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Poll [id=" + id + ", title=" + title + ", question=" + question
				+ "]";
	}

}
