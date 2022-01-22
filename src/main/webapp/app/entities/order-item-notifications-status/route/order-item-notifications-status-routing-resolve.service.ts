import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrderItemNotificationsStatus, OrderItemNotificationsStatus } from '../order-item-notifications-status.model';
import { OrderItemNotificationsStatusService } from '../service/order-item-notifications-status.service';

@Injectable({ providedIn: 'root' })
export class OrderItemNotificationsStatusRoutingResolveService implements Resolve<IOrderItemNotificationsStatus> {
  constructor(protected service: OrderItemNotificationsStatusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrderItemNotificationsStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((orderItemNotificationsStatus: HttpResponse<OrderItemNotificationsStatus>) => {
          if (orderItemNotificationsStatus.body) {
            return of(orderItemNotificationsStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrderItemNotificationsStatus());
  }
}
