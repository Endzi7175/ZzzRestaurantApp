package com.ktsnwt.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ktsnwt.restaurant.domain.enumeration.MenuItemType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * The MenuItem entity,\nRepresents an item in the menu.
 */
@Entity
@Table(name = "menu_item")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "menuitem")
public class MenuItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    /**
     * price of the meal/dish
     */
    @Column(name = "price")
    private Float price;

    /**
     * time needed to prepare meal in minutes
     */
    @Column(name = "prepare_time")
    private Integer prepareTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MenuItemType type;

    @JsonIgnoreProperties(value = { "menuItem" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Picture picture;

    /**
     * Ingredients list of a meal
     */
    @ManyToMany
    @JoinTable(
        name = "rel_menu_item__ingredient",
        joinColumns = @JoinColumn(name = "menu_item_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    @JsonIgnoreProperties(value = { "menuItems" }, allowSetters = true)
    private Set<Ingredient> ingredients = new HashSet<>();

    /**
     * menu
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "items", "restaurant" }, allowSetters = true)
    private Menu menu;


    public Long getId() {
        return this.id;
    }

    public MenuItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MenuItem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public MenuItem description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return this.price;
    }

    public MenuItem price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getPrepareTime() {
        return this.prepareTime;
    }

    public MenuItem prepareTime(Integer prepareTime) {
        this.setPrepareTime(prepareTime);
        return this;
    }

    public void setPrepareTime(Integer prepareTime) {
        this.prepareTime = prepareTime;
    }

    public MenuItemType getType() {
        return this.type;
    }

    public MenuItem type(MenuItemType type) {
        this.setType(type);
        return this;
    }

    public void setType(MenuItemType type) {
        this.type = type;
    }

    public Picture getPicture() {
        return this.picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public MenuItem picture(Picture picture) {
        this.setPicture(picture);
        return this;
    }

    public Set<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public MenuItem ingredients(Set<Ingredient> ingredients) {
        this.setIngredients(ingredients);
        return this;
    }

    public MenuItem addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }

    public MenuItem removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
        return this;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public MenuItem menu(Menu menu) {
        this.setMenu(menu);
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuItem)) {
            return false;
        }
        return id != null && id.equals(((MenuItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", prepareTime=" + getPrepareTime() +
            ", type='" + getType() + "'" +
            "}";
    }
}
