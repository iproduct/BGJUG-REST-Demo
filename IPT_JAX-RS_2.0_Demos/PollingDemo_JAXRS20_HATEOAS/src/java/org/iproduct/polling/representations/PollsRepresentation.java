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
 *
 */
package org.iproduct.polling.representations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ws.rs.core.Link;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.iproduct.polling.entity.Poll;
import static org.iproduct.polling.resources.utils.LinkRelations.SELF;

/**
 * Resource class for {@link org.iproduct.polling.model.Alternative Alternative} resources
 *
 * @author Trayan Iliev, IPT [http://iproduct.org]
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="polls")
public class PollsRepresentation {
    private int size = 0;
    @XmlElementWrapper(name = "_embedded")
    @XmlElement(name = "poll")
    private Collection<PollRepresentationCustomLinks> polls;
    @XmlElement(name="_links")
    @XmlJavaTypeAdapter(LinkMapAdapter.class)
    private Map<String, LinkRepresentation> links = new LinkedHashMap<>();

    public PollsRepresentation() {
    }

    public PollsRepresentation(Collection<Poll> pollsArg, Collection<Link> linksArg) {
        this.size = pollsArg.size();
        if(linksArg != null){
            linksArg.stream().forEachOrdered((Link link) -> {
                links.put(link.getRel(), new LinkRepresentation(link));
            });
        }
        Link selfLink = links.get(SELF) != null ?
            Link.fromUri(links.get(SELF).getHref())
                .rel(SELF).title("Polls collection").type(APPLICATION_JSON)
                .build() : null;

        this.polls = (pollsArg.size() > 0)? 
            pollsArg.stream().map( (Poll poll) -> {
                List<Link> l = null;
                if(selfLink != null){
                    l =  new ArrayList<>();
                    l.add(Link.fromUriBuilder(
                        UriBuilder.fromLink(selfLink).path(Long.toString(poll.getId())))
                        .rel(SELF).type(APPLICATION_JSON).build());
                }
                return new PollRepresentationCustomLinks(poll, l);
            }).collect(Collectors.toList())
            : null;
    } 

    public int getSize() {
        return size;
    }

    public Collection<PollRepresentationCustomLinks> getPolls() {
        return polls;
    }

    public void setPolls(Collection<PollRepresentationCustomLinks> polls) {
        this.polls = polls;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.polls);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PollsRepresentation other = (PollsRepresentation) obj;
        if (!Objects.equals(this.polls, other.polls)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PollsRepresentation{" + "size=" + size + ", polls=" + polls + '}';
    }
}
