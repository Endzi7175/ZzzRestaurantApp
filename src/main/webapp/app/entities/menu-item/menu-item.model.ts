import { IPicture } from 'app/entities/picture/picture.model';
import { IIngredient } from 'app/entities/ingredient/ingredient.model';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuItemType } from 'app/entities/enumerations/menu-item-type.model';

export interface IMenuItem {
  id?: number;
  name?: string | null;
  description?: string | null;
  price?: number | null;
  prepareTime?: number | null;
  type?: MenuItemType | null;
  picture?: IPicture | null;
  ingredients?: IIngredient[] | null;
  menu?: IMenu | null;
}

export class MenuItem implements IMenuItem {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public price?: number | null,
    public prepareTime?: number | null,
    public type?: MenuItemType | null,
    public picture?: IPicture | null,
    public ingredients?: IIngredient[] | null,
    public menu?: IMenu | null
  ) {}
}

export function getMenuItemIdentifier(menuItem: IMenuItem): number | undefined {
  return menuItem.id;
}
