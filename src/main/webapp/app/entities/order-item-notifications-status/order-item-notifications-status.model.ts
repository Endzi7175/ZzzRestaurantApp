import dayjs from 'dayjs/esm';
import { IRestaurantOrderItem } from 'app/entities/restaurant-order-item/restaurant-order-item.model';

export interface IOrderItemNotificationsStatus {
  id?: number;
  created?: dayjs.Dayjs | null;
  accepted?: dayjs.Dayjs | null;
  prepared?: dayjs.Dayjs | null;
  served?: dayjs.Dayjs | null;
  canceled?: dayjs.Dayjs | null;
  orderItem?: IRestaurantOrderItem | null;
}

export class OrderItemNotificationsStatus implements IOrderItemNotificationsStatus {
  constructor(
    public id?: number,
    public created?: dayjs.Dayjs | null,
    public accepted?: dayjs.Dayjs | null,
    public prepared?: dayjs.Dayjs | null,
    public served?: dayjs.Dayjs | null,
    public canceled?: dayjs.Dayjs | null,
    public orderItem?: IRestaurantOrderItem | null
  ) {}
}

export function getOrderItemNotificationsStatusIdentifier(orderItemNotificationsStatus: IOrderItemNotificationsStatus): number | undefined {
  return orderItemNotificationsStatus.id;
}
