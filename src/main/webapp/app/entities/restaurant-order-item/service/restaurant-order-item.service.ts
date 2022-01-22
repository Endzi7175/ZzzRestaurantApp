import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRestaurantOrderItem, getRestaurantOrderItemIdentifier } from '../restaurant-order-item.model';

export type EntityResponseType = HttpResponse<IRestaurantOrderItem>;
export type EntityArrayResponseType = HttpResponse<IRestaurantOrderItem[]>;

@Injectable({ providedIn: 'root' })
export class RestaurantOrderItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/restaurant-order-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/restaurant-order-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(restaurantOrderItem: IRestaurantOrderItem): Observable<EntityResponseType> {
    return this.http.post<IRestaurantOrderItem>(this.resourceUrl, restaurantOrderItem, { observe: 'response' });
  }

  update(restaurantOrderItem: IRestaurantOrderItem): Observable<EntityResponseType> {
    return this.http.put<IRestaurantOrderItem>(
      `${this.resourceUrl}/${getRestaurantOrderItemIdentifier(restaurantOrderItem) as number}`,
      restaurantOrderItem,
      { observe: 'response' }
    );
  }

  partialUpdate(restaurantOrderItem: IRestaurantOrderItem): Observable<EntityResponseType> {
    return this.http.patch<IRestaurantOrderItem>(
      `${this.resourceUrl}/${getRestaurantOrderItemIdentifier(restaurantOrderItem) as number}`,
      restaurantOrderItem,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRestaurantOrderItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRestaurantOrderItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRestaurantOrderItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addRestaurantOrderItemToCollectionIfMissing(
    restaurantOrderItemCollection: IRestaurantOrderItem[],
    ...restaurantOrderItemsToCheck: (IRestaurantOrderItem | null | undefined)[]
  ): IRestaurantOrderItem[] {
    const restaurantOrderItems: IRestaurantOrderItem[] = restaurantOrderItemsToCheck.filter(isPresent);
    if (restaurantOrderItems.length > 0) {
      const restaurantOrderItemCollectionIdentifiers = restaurantOrderItemCollection.map(
        restaurantOrderItemItem => getRestaurantOrderItemIdentifier(restaurantOrderItemItem)!
      );
      const restaurantOrderItemsToAdd = restaurantOrderItems.filter(restaurantOrderItemItem => {
        const restaurantOrderItemIdentifier = getRestaurantOrderItemIdentifier(restaurantOrderItemItem);
        if (restaurantOrderItemIdentifier == null || restaurantOrderItemCollectionIdentifiers.includes(restaurantOrderItemIdentifier)) {
          return false;
        }
        restaurantOrderItemCollectionIdentifiers.push(restaurantOrderItemIdentifier);
        return true;
      });
      return [...restaurantOrderItemsToAdd, ...restaurantOrderItemCollection];
    }
    return restaurantOrderItemCollection;
  }
}
