package com.ktsnwt.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * The Restaurant entity,\nRepresents the restaurant.
 */
@Entity
@Table(name = "restaurant")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "restaurant")
public class Restaurant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "x_table_positions")
    private Integer xTablePositions;

    @Column(name = "y_table_positions")
    private Integer yTablePositions;

    @JsonIgnoreProperties(value = { "items", "restaurant" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Menu menu;

    @ManyToMany
    @JoinTable(
        name = "rel_restaurant__user",
        joinColumns = @JoinColumn(name = "restaurant_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();


    public Long getId() {
        return this.id;
    }

    public Restaurant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Restaurant name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Restaurant description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return this.address;
    }

    public Restaurant address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public Restaurant phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getxTablePositions() {
        return this.xTablePositions;
    }

    public Restaurant xTablePositions(Integer xTablePositions) {
        this.setxTablePositions(xTablePositions);
        return this;
    }

    public void setxTablePositions(Integer xTablePositions) {
        this.xTablePositions = xTablePositions;
    }

    public Integer getyTablePositions() {
        return this.yTablePositions;
    }

    public Restaurant yTablePositions(Integer yTablePositions) {
        this.setyTablePositions(yTablePositions);
        return this;
    }

    public void setyTablePositions(Integer yTablePositions) {
        this.yTablePositions = yTablePositions;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Restaurant menu(Menu menu) {
        this.setMenu(menu);
        return this;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Restaurant users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Restaurant addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Restaurant removeUser(User user) {
        this.users.remove(user);
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restaurant)) {
            return false;
        }
        return id != null && id.equals(((Restaurant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Restaurant{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", xTablePositions=" + getxTablePositions() +
            ", yTablePositions=" + getyTablePositions() +
            "}";
    }
}
