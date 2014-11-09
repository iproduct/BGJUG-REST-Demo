package org.iproduct.polling.entity;

import java.io.Serializable;

import java.lang.Long;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class Alternative implements Serializable {

    @TableGenerator(name = "alt_gen",
            table = "id_gen",
            pkColumnName = "GEN_KEY",
            valueColumnName = "GEN_VALUE",
            pkColumnValue = "alt_id",
            allocationSize = 1
    )
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "alt_gen")
    @Basic(optional = false)
    @Column(updatable = false, insertable = true, nullable = false)
    private Long id;

    @Column(unique = false, updatable = true, insertable = true, nullable = false, length = 255)
    @Basic
    @Size(min = 1, max = 255)
    @NotNull
    private String text;

    @Column(unique = false, updatable = true, insertable = true, nullable = false)
    @Basic(optional = false)
    @NotNull
    private int position;

    @XmlTransient
    @ManyToOne(optional = false, targetEntity = Poll.class)
    @NotNull
    private Poll poll;

    @OneToMany(cascade = {CascadeType.ALL}, targetEntity = Vote.class, mappedBy = "alternative")
    private List<Vote> votes;

    public Alternative() {

    }

    public List<Vote> getVotes() {
        return this.votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Poll getPoll() {
        return this.poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final Alternative other = (Alternative) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Alternative{" + "id=" + id + ", text=" + text + ", position=" 
                + position 
                + (poll != null ? ", pollId=" + poll.getId() : "")
                + ", votes=" + votes + '}';
    }

}
