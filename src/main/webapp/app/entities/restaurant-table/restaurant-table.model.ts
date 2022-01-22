export interface IRestaurantTable {
  id?: number;
  tableNo?: number | null;
  seatsNo?: number | null;
  description?: string | null;
  xPositionFrom?: number | null;
  xPositionTo?: number | null;
  yPositionFrom?: number | null;
  yPositionTo?: number | null;
}

export class RestaurantTable implements IRestaurantTable {
  constructor(
    public id?: number,
    public tableNo?: number | null,
    public seatsNo?: number | null,
    public description?: string | null,
    public xPositionFrom?: number | null,
    public xPositionTo?: number | null,
    public yPositionFrom?: number | null,
    public yPositionTo?: number | null
  ) {}
}

export function getRestaurantTableIdentifier(restaurantTable: IRestaurantTable): number | undefined {
  return restaurantTable.id;
}
