import { IOrderItemNotificationsStatus } from 'app/entities/order-item-notifications-status/order-item-notifications-status.model';
import { IRestaurantOrder } from 'app/entities/restaurant-order/restaurant-order.model';
import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';

export interface IRestaurantOrderItem {
  id?: number;
  name?: string | null;
  menuItemId?: number | null;
  priceExcludingTax?: number | null;
  priceIncludingTax?: number | null;
  quantity?: number | null;
  status?: OrderItemStatus | null;
  orderItemStatus?: IOrderItemNotificationsStatus | null;
  restaurantOrder?: IRestaurantOrder | null;
}

export class RestaurantOrderItem implements IRestaurantOrderItem {
  constructor(
    public id?: number,
    public name?: string | null,
    public menuItemId?: number | null,
    public priceExcludingTax?: number | null,
    public priceIncludingTax?: number | null,
    public quantity?: number | null,
    public status?: OrderItemStatus | null,
    public orderItemStatus?: IOrderItemNotificationsStatus | null,
    public restaurantOrder?: IRestaurantOrder | null
  ) {}
}

export function getRestaurantOrderItemIdentifier(restaurantOrderItem: IRestaurantOrderItem): number | undefined {
  return restaurantOrderItem.id;
}
