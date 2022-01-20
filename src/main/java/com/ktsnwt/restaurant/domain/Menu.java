package com.ktsnwt.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * The Menu entity,\nRepresents the menu of the restaurant with list of meals.
 */
@Entity
@Table(name = "menu")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * menu items
     */
    @OneToMany(mappedBy = "menu")
    @JsonIgnoreProperties(value = { "picture", "ingredients", "menu" }, allowSetters = true)
    private Set<MenuItem> items = new HashSet<>();

    @JsonIgnoreProperties(value = { "menu", "users" }, allowSetters = true)
    @OneToOne(mappedBy = "menu")
    private Restaurant restaurant;


    public Long getId() {
        return this.id;
    }

    public Menu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<MenuItem> getItems() {
        return this.items;
    }

    public void setItems(Set<MenuItem> menuItems) {
        if (this.items != null) {
            this.items.forEach(i -> i.setMenu(null));
        }
        if (menuItems != null) {
            menuItems.forEach(i -> i.setMenu(this));
        }
        this.items = menuItems;
    }

    public Menu items(Set<MenuItem> menuItems) {
        this.setItems(menuItems);
        return this;
    }

    public Menu addItem(MenuItem menuItem) {
        this.items.add(menuItem);
        menuItem.setMenu(this);
        return this;
    }

    public Menu removeItem(MenuItem menuItem) {
        this.items.remove(menuItem);
        menuItem.setMenu(null);
        return this;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        if (this.restaurant != null) {
            this.restaurant.setMenu(null);
        }
        if (restaurant != null) {
            restaurant.setMenu(this);
        }
        this.restaurant = restaurant;
    }

    public Menu restaurant(Restaurant restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Menu)) {
            return false;
        }
        return id != null && id.equals(((Menu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Menu{" +
            "id=" + getId() +
            "}";
    }
}
