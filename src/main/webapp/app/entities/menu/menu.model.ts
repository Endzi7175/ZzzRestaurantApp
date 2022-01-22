import { IMenuItem } from 'app/entities/menu-item/menu-item.model';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';

export interface IMenu {
  id?: number;
  items?: IMenuItem[] | null;
  restaurant?: IRestaurant | null;
}

export class Menu implements IMenu {
  constructor(public id?: number, public items?: IMenuItem[] | null, public restaurant?: IRestaurant | null) {}
}

export function getMenuIdentifier(menu: IMenu): number | undefined {
  return menu.id;
}
