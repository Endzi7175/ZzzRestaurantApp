import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMenuItem, MenuItem } from '../menu-item.model';
import { MenuItemService } from '../service/menu-item.service';

@Injectable({ providedIn: 'root' })
export class MenuItemRoutingResolveService implements Resolve<IMenuItem> {
  constructor(protected service: MenuItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMenuItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((menuItem: HttpResponse<MenuItem>) => {
          if (menuItem.body) {
            return of(menuItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MenuItem());
  }
}
