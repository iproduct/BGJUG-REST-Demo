package org.iproduct.polling.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.iproduct.polling.entity.Alternative;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2014-11-08T20:52:06")
@StaticMetamodel(Vote.class)
public class Vote_ { 

    public static volatile SingularAttribute<Vote, Date> voteTime;
    public static volatile SingularAttribute<Vote, Alternative> alternative;
    public static volatile SingularAttribute<Vote, Long> id;
    public static volatile SingularAttribute<Vote, String> email;

}