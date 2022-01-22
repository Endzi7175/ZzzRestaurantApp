import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRestaurantOrderItem, RestaurantOrderItem } from '../restaurant-order-item.model';
import { RestaurantOrderItemService } from '../service/restaurant-order-item.service';

@Injectable({ providedIn: 'root' })
export class RestaurantOrderItemRoutingResolveService implements Resolve<IRestaurantOrderItem> {
  constructor(protected service: RestaurantOrderItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRestaurantOrderItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((restaurantOrderItem: HttpResponse<RestaurantOrderItem>) => {
          if (restaurantOrderItem.body) {
            return of(restaurantOrderItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RestaurantOrderItem());
  }
}
