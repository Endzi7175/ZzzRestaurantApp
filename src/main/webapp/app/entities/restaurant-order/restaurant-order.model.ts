import dayjs from 'dayjs/esm';
import { IRestaurantOrderItem } from 'app/entities/restaurant-order-item/restaurant-order-item.model';
import { IUser } from 'app/entities/user/user.model';
import { IRestaurantTable } from 'app/entities/restaurant-table/restaurant-table.model';

export interface IRestaurantOrder {
  id?: number;
  date?: dayjs.Dayjs | null;
  priceExcludingTax?: number | null;
  priceIncludingTax?: number | null;
  items?: IRestaurantOrderItem[] | null;
  user?: IUser | null;
  restaurantTable?: IRestaurantTable | null;
}

export class RestaurantOrder implements IRestaurantOrder {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public priceExcludingTax?: number | null,
    public priceIncludingTax?: number | null,
    public items?: IRestaurantOrderItem[] | null,
    public user?: IUser | null,
    public restaurantTable?: IRestaurantTable | null
  ) {}
}

export function getRestaurantOrderIdentifier(restaurantOrder: IRestaurantOrder): number | undefined {
  return restaurantOrder.id;
}
