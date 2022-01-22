import { IMenuItem } from 'app/entities/menu-item/menu-item.model';

export interface IIngredient {
  id?: number;
  name?: string | null;
  alergen?: boolean | null;
  menuItems?: IMenuItem[] | null;
}

export class Ingredient implements IIngredient {
  constructor(public id?: number, public name?: string | null, public alergen?: boolean | null, public menuItems?: IMenuItem[] | null) {
    this.alergen = this.alergen ?? false;
  }
}

export function getIngredientIdentifier(ingredient: IIngredient): number | undefined {
  return ingredient.id;
}
