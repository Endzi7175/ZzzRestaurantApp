import { IMenu } from 'app/entities/menu/menu.model';
import { IUser } from 'app/entities/user/user.model';

export interface IRestaurant {
  id?: number;
  name?: string | null;
  description?: string | null;
  address?: string | null;
  phone?: string | null;
  xTablePositions?: number | null;
  yTablePositions?: number | null;
  menu?: IMenu | null;
  users?: IUser[] | null;
}

export class Restaurant implements IRestaurant {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public address?: string | null,
    public phone?: string | null,
    public xTablePositions?: number | null,
    public yTablePositions?: number | null,
    public menu?: IMenu | null,
    public users?: IUser[] | null
  ) {}
}

export function getRestaurantIdentifier(restaurant: IRestaurant): number | undefined {
  return restaurant.id;
}
