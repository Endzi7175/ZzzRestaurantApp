import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRestaurantOrder, getRestaurantOrderIdentifier } from '../restaurant-order.model';

export type EntityResponseType = HttpResponse<IRestaurantOrder>;
export type EntityArrayResponseType = HttpResponse<IRestaurantOrder[]>;

@Injectable({ providedIn: 'root' })
export class RestaurantOrderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/restaurant-orders');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/restaurant-orders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(restaurantOrder: IRestaurantOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(restaurantOrder);
    return this.http
      .post<IRestaurantOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(restaurantOrder: IRestaurantOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(restaurantOrder);
    return this.http
      .put<IRestaurantOrder>(`${this.resourceUrl}/${getRestaurantOrderIdentifier(restaurantOrder) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(restaurantOrder: IRestaurantOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(restaurantOrder);
    return this.http
      .patch<IRestaurantOrder>(`${this.resourceUrl}/${getRestaurantOrderIdentifier(restaurantOrder) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRestaurantOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRestaurantOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRestaurantOrder[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRestaurantOrderToCollectionIfMissing(
    restaurantOrderCollection: IRestaurantOrder[],
    ...restaurantOrdersToCheck: (IRestaurantOrder | null | undefined)[]
  ): IRestaurantOrder[] {
    const restaurantOrders: IRestaurantOrder[] = restaurantOrdersToCheck.filter(isPresent);
    if (restaurantOrders.length > 0) {
      const restaurantOrderCollectionIdentifiers = restaurantOrderCollection.map(
        restaurantOrderItem => getRestaurantOrderIdentifier(restaurantOrderItem)!
      );
      const restaurantOrdersToAdd = restaurantOrders.filter(restaurantOrderItem => {
        const restaurantOrderIdentifier = getRestaurantOrderIdentifier(restaurantOrderItem);
        if (restaurantOrderIdentifier == null || restaurantOrderCollectionIdentifiers.includes(restaurantOrderIdentifier)) {
          return false;
        }
        restaurantOrderCollectionIdentifiers.push(restaurantOrderIdentifier);
        return true;
      });
      return [...restaurantOrdersToAdd, ...restaurantOrderCollection];
    }
    return restaurantOrderCollection;
  }

  protected convertDateFromClient(restaurantOrder: IRestaurantOrder): IRestaurantOrder {
    return Object.assign({}, restaurantOrder, {
      date: restaurantOrder.date?.isValid() ? restaurantOrder.date.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((restaurantOrder: IRestaurantOrder) => {
        restaurantOrder.date = restaurantOrder.date ? dayjs(restaurantOrder.date) : undefined;
      });
    }
    return res;
  }
}
