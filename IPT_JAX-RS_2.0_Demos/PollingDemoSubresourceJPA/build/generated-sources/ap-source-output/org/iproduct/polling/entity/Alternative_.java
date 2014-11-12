package org.iproduct.polling.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.iproduct.polling.entity.Poll;
import org.iproduct.polling.entity.Vote;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-08T20:52:06")
@StaticMetamodel(Alternative.class)
public class Alternative_ { 

    public static volatile ListAttribute<Alternative, Vote> votes;
    public static volatile SingularAttribute<Alternative, Long> id;
    public static volatile SingularAttribute<Alternative, String> text;
    public static volatile SingularAttribute<Alternative, Integer> position;
    public static volatile SingularAttribute<Alternative, Poll> poll;

}